package com.nvarghese.beowulf.sfc.services;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Date;

import javax.jms.JMSException;

import org.apache.commons.configuration.ConfigurationException;
import org.bson.types.ObjectId;
import org.msgpack.rpc.Server;
import org.msgpack.rpc.loop.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.common.ds.DataStoreUtil;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.TransactionSource;
import com.nvarghese.beowulf.common.jobs.NewScanJob;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.utils.ByteUtils;
import com.nvarghese.beowulf.common.webtest.CategorizerType;
import com.nvarghese.beowulf.common.webtest.JobStatus;
import com.nvarghese.beowulf.common.webtest.ScanPhase;
import com.nvarghese.beowulf.common.webtest.scs.jobs.CategorizationJobDAO;
import com.nvarghese.beowulf.common.webtest.scs.jobs.CategorizationJobDocument;
import com.nvarghese.beowulf.common.webtest.scs.jobs.CategorizerJob;
import com.nvarghese.beowulf.sfc.SFControllerManager;

public class NewScanService {

	static Logger logger = LoggerFactory.getLogger(NewScanService.class);

	public void startScan(NewScanJob newScanJob) {

		logger.info("New scan job received to kick start the service with WebScanDocument#obj_id: {}", newScanJob.getWebScanObjectId());

		ObjectId id = new ObjectId(newScanJob.getWebScanObjectId());
		WebScanDAO webScanDAO = new WebScanDAO(SFControllerManager.getInstance().getDataStore());

		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(id);
		Datastore ds = null;

		if (webScanDocument != null) {

			// create data store
			ds = createScanDataStore();

			// start rpc server
			int rpcPort = startRpcServerInstance(id);

			// update web scan document
			webScanDocument.setBwControllerIPAddress(SFControllerManager.getInstance().getSettings().getIpAddress());
			webScanDocument.setBwControllerPort(rpcPort);
			webScanDocument.setScanRunning(true);
			webScanDocument.setScanStartTime(new Date());
			webScanDocument.setScanJobsInProgress(true);
			webScanDocument.setScanPhase(ScanPhase.PRIMARY_SCAN.getName());
			webScanDocument.setTxnDbName(ds.getDB().getName());
			webScanDAO.updateWebScanDocument(webScanDocument);

			// crawl base URI
			URI baseURI;
			ObjectId txnId = null;
			try {
				baseURI = getBaseURI(webScanDocument);
				txnId = requestBaseURI(ds, baseURI);
			} catch (URISyntaxException e) {
				logger.error("Failed to crawl base URI. Reason: {}", e.getMessage(), e);
				webScanDocument.getComments().add(e.getMessage());
			} finally {
				webScanDAO.updateWebScanDocument(webScanDocument);
			}

			// persist job and post message
			ObjectId categJobObjId = createMetaCategorizationJobDocument(ds, txnId, webScanDocument.getId());
			CategorizerJob categJob = new CategorizerJob();
			categJob.setCategorizerJobObjId(categJobObjId.toString());
			categJob.setWebScanObjId(webScanDocument.getId().toString());
			categJob.setDatabaseName(ds.getDB().getName());

			BwCategorizerService categService = new BwCategorizerService();
			try {
				categService.submitJob(categJob);
			} catch (JMSException e) {
				logger.error("Failed to send JMS message to categorizer system. Reason: {}", e.getMessage(), e);
				webScanDocument.getComments().add(e.getMessage());
			} finally {
				webScanDAO.updateWebScanDocument(webScanDocument);
			}

		} else {
			logger.warn("Web scan document with id: {} cannot be found. Failed to start scan");
		}
	}

	private ObjectId createMetaCategorizationJobDocument(Datastore ds, ObjectId txnObjId, ObjectId webscanObjId) {

		CategorizationJobDocument jobDoc = new CategorizationJobDocument();
		jobDoc.setJobStatus(JobStatus.INIT);
		jobDoc.setTxnObjId(txnObjId);
		jobDoc.setWebScanObjId(webscanObjId);
		jobDoc.setCategorizerType(CategorizerType.META);

		CategorizationJobDAO categorizationJobDAO = new CategorizationJobDAO(ds);
		ObjectId id = categorizationJobDAO.createCategorizationJobDocument(jobDoc);
		return id;
	}

	private URI getBaseURI(WebScanDocument webScanDocument) throws URISyntaxException {

		if (webScanDocument.getBaseUris().size() > 0) {
			URI uri = new URI(webScanDocument.getBaseUris().get(0));
			return uri;
		} else {
			throw new URISyntaxException(null, "BaseURI is null");
		}

	}

	private int startRpcServerInstance(final ObjectId webScanObjId) {

		final int port = findFreePort();
		new Thread() {

			public void run() {

				EventLoop loop = EventLoop.defaultEventLoop();

				Server svr = new Server(loop);
				ScanInstanceServer scanInstanceServer = new ScanInstanceServer(svr, webScanObjId);

				// start rpc interface
				svr.serve(scanInstanceServer);

				// start scan monitoring
				scanInstanceServer.startScanMonitoring();
				ScanInstanceRegister.getInstance().registerScanInstanceServer(webScanObjId.toString(), scanInstanceServer);
				try {
					svr.listen(port);
					logger.info("Server listening on port TCP/" + port + " for connections");
					loop.join();
				} catch (IOException e) {
					logger.error("Failed to start rpc server. Reason: {}", e.getMessage(), e);
					svr.close();
				} catch (InterruptedException e) {
					logger.error("Interrupted rpc server running at port: {}. ", port, e);
					svr.close();
				}

			}
		}.start();

		return port;

	}

	private ObjectId requestBaseURI(Datastore ds, URI baseUri) {

		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.GET, baseUri, null, null,
				TransactionSource.BASE);
		transaction.execute();

		HttpTxnDAO txnDAO = new HttpTxnDAO(ds);
		ObjectId id = txnDAO.createHttpTxnDocument(transaction.toHttpTxnDocument());

		return id;

	}

	private Datastore createScanDataStore() {

		String databaseName = "scan_inst_" + ByteUtils.toHex(ByteUtils.getRandomBytes(6));
		Datastore ds = null;
		try {
			ds = DataStoreUtil.createOrGetDataStore(BeowulfCommonConfigManager.getDbUri(), databaseName);
		} catch (ConfigurationException e) {
			logger.error("Failed to create scan data store. Reason: {}", e.getMessage(), e);
		} catch (UnknownHostException e) {
			logger.error("Failed to create scan data store. Reason: {}", e.getMessage(), e);
		}

		return ds;
	}

	private int findFreePort() {

		int port = 0;
		try {
			ServerSocket server = new ServerSocket(0);
			port = server.getLocalPort();
			server.close();
			return port;
		} catch (IOException e) {
			logger.error("Failed to retrieve free port. Reason: {}", e.getMessage(), e);
		}

		return port;

	}

}
