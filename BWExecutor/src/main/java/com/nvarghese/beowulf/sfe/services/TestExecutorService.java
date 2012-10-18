package com.nvarghese.beowulf.sfe.services;

import java.net.UnknownHostException;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.common.ds.DataStoreUtil;
import com.nvarghese.beowulf.common.rpc.BwControllerRpcInterface;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.TestModuleScanConfigDocument;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.JobStatus;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.common.webtest.dao.TestModuleMetaDataDAO;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJob;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJobDAO;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJobDocument;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestParameterDocument;
import com.nvarghese.beowulf.sfe.SfeManager;
import com.nvarghese.beowulf.sfe.webtest.tm.AbstractTestModule;
import com.nvarghese.beowulf.sfe.webtest.types.DirectoryTestType;

public class TestExecutorService {

	static Logger logger = LoggerFactory.getLogger(TestExecutorService.class);

	public void processTestJob(TestJob testJob) {

		BwControllerService bwControllerService = new BwControllerService();
		WebScanDAO webScanDAO = new WebScanDAO(SfeManager.getInstance().getDataStore());
		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(testJob.getWebScanObjId());

		try {
			BwControllerRpcInterface bwContollerRpcClient = bwControllerService.getRpcClient(webScanDocument.getBwControllerIPAddress(),
					webScanDocument.getBwControllerPort());
			Datastore ds = DataStoreUtil.createOrGetDataStore(BeowulfCommonConfigManager.getDbServers(), testJob.getDatabaseName());

			TestJobDAO testJobDAO = new TestJobDAO(ds);
			TestJobDocument testJobDocument = testJobDAO.getTestJobDocument(testJob.getTestJobObjId());
			if (testJobDocument != null) {

				try {
					testJobDocument.setJobStatus(JobStatus.PROCESSING);
					testJobDAO.updateTestJobDocument(testJobDocument);

					routeTestJob(ds, webScanDocument, testJobDocument);

					testJobDocument.setJobStatus(JobStatus.COMPLETED);
					testJobDAO.updateTestJobDocument(testJobDocument);
				} catch (TestJobException e) {
					logger.error("Failed to execute test job with id: {}. Reason: {}", testJobDocument.getId(), e.getMessage());
					e.printStackTrace();
					testJobDocument.setJobStatus(JobStatus.ERROR);
					testJobDAO.updateTestJobDocument(testJobDocument);
				}

			} else {

			}

			String reply = bwContollerRpcClient.sayHello("BW-Executor System");
			logger.info("RPC Message was sent to server. Server replied: {}", reply);

		} catch (ConfigurationException e) {
			logger.error("Failed to create/get datastore for scan instance. Reason: {}", e.getMessage(), e);
		} catch (UnknownHostException e) {
			logger.error("Failed to create RPC client for the bw-controller. Reason: {}", e.getMessage(), e);
		}

	}

	private void routeTestJob(Datastore ds, WebScanDocument webScanDocument, TestJobDocument testJobDocument) throws TestJobException {

		// load meta testModule
		TestModuleMetaDataDAO testModuleMetaDAO = new TestModuleMetaDataDAO(SfeManager.getInstance().getDataStore());
		TestModuleMetaDataDocument testModuleMetaDocument = testModuleMetaDAO.findByModuleNumber(testJobDocument.getModuleNumber());

		// load enabled test module
		TestModuleScanConfigDocument testModuleScanConfigDocument = webScanDocument.getScanConfig().getTestModules()
				.get(testJobDocument.getModuleNumber());

		if (testModuleMetaDocument != null && testModuleScanConfigDocument != null) {

			try {

				if (testJobDocument.getTestType() == WebTestType.DIRECTORY_TEST) {
					Class targetTestModuleClass = Class.forName(testModuleMetaDocument.getModuleClassName());
					Object targetTestModuleObject = targetTestModuleClass.newInstance();
					DirectoryTestType directoryTestModule = DirectoryTestType.class.cast(targetTestModuleObject);
					((AbstractTestModule) directoryTestModule).initialize(ds, testModuleMetaDocument, testModuleScanConfigDocument);
					TestParameterDocument testParamDocument = testJobDocument.getTestParameters().get(0);
					String paramValue = (String) testParamDocument.getParameterValue();
					directoryTestModule.testByDirectory(testJobDocument.getTxnObjId(), paramValue);
				}

			} catch (ClassNotFoundException e) {

			} catch (InstantiationException e) {

			} catch (IllegalAccessException e) {

			}

		} else {

		}

	}

}
