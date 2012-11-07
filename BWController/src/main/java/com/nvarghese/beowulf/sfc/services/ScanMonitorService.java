package com.nvarghese.beowulf.sfc.services;

import java.net.UnknownHostException;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.common.ds.DataStoreUtil;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.ScanPhase;
import com.nvarghese.beowulf.common.webtest.scs.jobs.CategorizationJobDAO;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJobDAO;
import com.nvarghese.beowulf.sfc.SFControllerManager;

public class ScanMonitorService {

	static Logger logger = LoggerFactory.getLogger(ScanMonitorService.class);

	/**
	 * Checks whether the scan is running
	 * 
	 * @param webScanObjectId
	 * @return
	 * @throws UnknownHostException
	 * @throws ConfigurationException
	 */
	public boolean isScanRunning(ObjectId webScanObjectId) throws UnknownHostException, ConfigurationException {

		boolean running = true;

		WebScanDAO webScanDAO = new WebScanDAO(SFControllerManager.getInstance().getDataStore());
		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(webScanObjectId);

		Datastore ds = DataStoreUtil.createOrGetDataStore(BeowulfCommonConfigManager.getDbUri(), webScanDocument.getTxnDbName());

		// check categ jobs
		CategorizationJobDAO categJobDAO = new CategorizationJobDAO(ds);
		boolean categJobsPresent = categJobDAO.isInProgressJobsPresent();

		// check test jobs
		TestJobDAO testJobDAO = new TestJobDAO(ds);
		boolean testJobsPresent = testJobDAO.isInProgressJobsPresent();

		running = categJobsPresent || testJobsPresent;

		return running;

	}

	/**
	 * Updates exection job and categorization job count to web scan document
	 * 
	 * @param webScanObjId
	 * @throws UnknownHostException
	 * @throws ConfigurationException
	 */
	public void updateWebScanDocumentWithScanJobDetails(ObjectId webScanObjId) throws UnknownHostException, ConfigurationException {

		WebScanDAO webScanDAO = new WebScanDAO(SFControllerManager.getInstance().getDataStore());
		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(webScanObjId);

		Datastore ds = DataStoreUtil.createOrGetDataStore(BeowulfCommonConfigManager.getDbUri(), webScanDocument.getTxnDbName());

		// check categ jobs
		CategorizationJobDAO categJobDAO = new CategorizationJobDAO(ds);
		webScanDocument.setCompletedCategorizationJobs(categJobDAO.getCountOfCompletedJobs());
		webScanDocument.setPendingCategorizationJobs(categJobDAO.getCountOfInProgressJobs());
		webScanDocument.setCreatedCategorizationJobs(categJobDAO.getCountOfAllJobs());
		webScanDocument.setErroredCategorizationJobs(categJobDAO.getCountOfErrorOrTerminatedJobs());

		// check test jobs
		TestJobDAO testJobDAO = new TestJobDAO(ds);
		webScanDocument.setCompletedExecutionJobs(testJobDAO.getCountOfCompletedJobs());
		webScanDocument.setPendingExecutionJobs(testJobDAO.getCountOfInProgressJobs());
		webScanDocument.setCreatedExecutionJobs(testJobDAO.getCountOfAllJobs());
		webScanDocument.setErroredExecutionJobs(testJobDAO.getCountOfErrorOrTerminatedJobs());

		// update
		webScanDAO.updateWebScanDocument(webScanDocument);

	}

	public void stopScanInstanceServer(ObjectId webScanObjId) {

		ScanInstanceServer scanInstanceServer = ScanInstanceRegister.getInstance().getScanInstanceServer(webScanObjId.toString());

		if (scanInstanceServer == null) {
			logger.warn("Failed to stop scan instance for the id: {}. Reason: Scan Instance is not present in register map", webScanObjId.toString());
			return;
		}

		scanInstanceServer.stopScanMonitoring();

		// update web scan
		WebScanDAO webScanDAO = new WebScanDAO(SFControllerManager.getInstance().getDataStore());
		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(webScanObjId);
		webScanDocument.setScanPhase(ScanPhase.COMPLETE.getName());
		webScanDocument.setScanRunning(false);
		webScanDocument.setScanEndTime(new Date());
		webScanDAO.updateWebScanDocument(webScanDocument);
		// webScanDAO.updateScanPhase(webScanObjId,
		// ScanPhase.COMPLETE.getName());
		// webScanDAO.updateScanRunning(webScanObjId, false);

		// terminate scan
		scanInstanceServer.terminateServer();

		// unregister for clean up
		ScanInstanceRegister.getInstance().unregisterScanInstanceServer(webScanObjId.toString());

	}

}
