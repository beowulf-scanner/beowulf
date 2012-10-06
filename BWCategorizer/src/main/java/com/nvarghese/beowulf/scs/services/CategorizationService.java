package com.nvarghese.beowulf.scs.services;

import java.net.UnknownHostException;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.common.ds.DataStoreUtil;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDocument;
import com.nvarghese.beowulf.common.rpc.BwControllerRpcInterface;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.scs.jobs.MetaCategorizerJob;
import com.nvarghese.beowulf.scs.ScsManager;

public class CategorizationService {

	static Logger logger = LoggerFactory.getLogger(CategorizationService.class);

	public void processMetaCategorization(MetaCategorizerJob metaCategorizerJob) {

		BwControllerService bwControllerService = new BwControllerService();
		WebScanDAO webScanDAO = new WebScanDAO(ScsManager.getInstance().getDataStore());
		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(metaCategorizerJob.getWebScanObjId());

		try {
			BwControllerRpcInterface bwContollerRpcClient = bwControllerService.getRpcClient(webScanDocument.getBwControllerIPAddress(),
					webScanDocument.getBwControllerPort());
			Datastore ds = DataStoreUtil.createOrGetDataStore(BeowulfCommonConfigManager.getDbServers(), metaCategorizerJob.getDatabaseName());
			HttpTxnDAO txnDAO = new HttpTxnDAO(ds);
			HttpTxnDocument httpTxnDocument = txnDAO.getHttpTxnDocument(metaCategorizerJob.getTxnObjId());
			if (httpTxnDocument != null) {
				
				AbstractHttpTransaction httpTransaction = AbstractHttpTransaction.getObject(httpTxnDocument);
				doProcessMetaCategorization(httpTransaction);

			} else {

			}
			String reply = bwContollerRpcClient.sayHello("BW-Categorization System");
			logger.info("Received message from server. Server says: {}", reply);

		} catch (ConfigurationException e) {
			logger.error("Failed to create/get datastore for scan instance. Reason: {}", e.getMessage(), e);
		} catch (UnknownHostException e) {
			logger.error("Failed to create RPC client for the bw-controller. Reason: {}", e.getMessage(), e);
		}

	}

	private void doProcessMetaCategorization(AbstractHttpTransaction httpTransaction) {

		logger.info("Running meta categorization on transaction with id: {}", httpTransaction.getObjId());

	}

}
