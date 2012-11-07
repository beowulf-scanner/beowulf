package com.nvarghese.beowulf.smf.scan.services;

import java.util.Map;

import javax.jms.JMSException;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.exception.ServiceException;
import com.nvarghese.beowulf.common.jobs.NewScanJob;
import com.nvarghese.beowulf.common.scan.dao.MasterScanConfigDAO;
import com.nvarghese.beowulf.common.scan.dao.MasterScanReportDAO;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.dto.config.Profile;
import com.nvarghese.beowulf.common.scan.model.MasterScanConfigDocument;
import com.nvarghese.beowulf.common.scan.model.MasterScanReportDocument;
import com.nvarghese.beowulf.common.scan.model.TestModuleScanConfigDocument;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.ScanPhase;
import com.nvarghese.beowulf.common.webtest.dao.TestModuleMetaDataDAO;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;
import com.nvarghese.beowulf.smf.SmfManager;
import com.nvarghese.beowulf.smf.scan.dto.scanrequest.Baseuris;
import com.nvarghese.beowulf.smf.scan.dto.scanrequest.Comments;
import com.nvarghese.beowulf.smf.scan.dto.scanrequest.Jobs;
import com.nvarghese.beowulf.smf.scan.dto.scanrequest.ScanRequest;
import com.nvarghese.beowulf.smf.scan.transformers.ScanProfileTransformer;

public class ScanManagementService {

	static Logger logger = LoggerFactory.getLogger(ScanManagementService.class);

	public ScanRequest createWebScanRequest(Profile profile) throws ServiceException {

		MasterScanConfigDocument scanConfigDocument = new MasterScanConfigDocument();

		// transform
		ScanProfileTransformer profileTransformer = new ScanProfileTransformer();
		scanConfigDocument.setHttpClientScanConfig(profileTransformer.transformToHttpClientScanConfigDocument(profile.getHttpClient()));
		scanConfigDocument.setSettingScanConfig(profileTransformer.transformToSettingScanConfigDocument(profile.getScanSettings()));
		scanConfigDocument.setReportScanConfig(profileTransformer.transformToReportScanConfigDocument(profile.getReportSettings()));
		scanConfigDocument.setSessionScanConfig(profileTransformer.transformToSessionSettingScanConfigDocument(profile.getSessionSettings()));
		scanConfigDocument.setTestModules(profileTransformer.transformToMappedTestModuleScanConfigDocument(profile.getTestModules()));
		updateTestModulesWithTestType(scanConfigDocument.getTestModules());

		// persist
		MasterScanConfigDAO scanConfigDAO = new MasterScanConfigDAO(SmfManager.getInstance().getDataStore());
		scanConfigDAO.createMasterScanConfigDocument(scanConfigDocument);
		
		MasterScanReportDAO reportDAO = new MasterScanReportDAO(SmfManager.getInstance().getDataStore());
		MasterScanReportDocument scanReport = new MasterScanReportDocument();
		reportDAO.createMasterScanReportDocument(scanReport);

		WebScanDocument webScanDocument = new WebScanDocument();
		webScanDocument.setScanConfig(scanConfigDocument);
		webScanDocument.setScanReport(scanReport);
		webScanDocument.setBaseUris(scanConfigDocument.getSettingScanConfig().getBaseURIList());
		webScanDocument.setScanPhase(ScanPhase.INITIALIZATION.getName());

		// persist
		WebScanDAO webScanDAO = new WebScanDAO(SmfManager.getInstance().getDataStore());
		webScanDAO.createWebScanDocument(webScanDocument);

		try {
			NewScanJob newScanJob = new NewScanJob(webScanDocument.getId().toString());
			BwControllerService bwControllerService = new BwControllerService();
			bwControllerService.submitJob(newScanJob);
		} catch (JMSException e) {
			logger.error("Failed to submit the new scan job. Reason: {}", e.getMessage(), e);
			ServiceException se = new ServiceException();
			se.initCause(e.getCause());
			throw se;
		}

		return transformToScanRequest(webScanDocument);

	}

	private void updateTestModulesWithTestType(Map<Long, TestModuleScanConfigDocument> testModules) {

		TestModuleMetaDataDAO testModuleMetaDataDAO = new TestModuleMetaDataDAO(SmfManager.getInstance().getDataStore());
		for (TestModuleScanConfigDocument scanConfigDocument : testModules.values()) {

			TestModuleMetaDataDocument metaTestModule = testModuleMetaDataDAO.findByModuleNumber(scanConfigDocument.getModuleNumber());
			if (metaTestModule != null) {
				scanConfigDocument.setTestType(metaTestModule.getTestType());
			} else {
				logger.warn("Failed to find metaTestModule with module_number: {}", scanConfigDocument.getModuleNumber());
			}

		}

	}

	/**
	 * 
	 * @param objectId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	public ScanRequest getWebScanRequest(ObjectId objectId) throws ResourceNotFoundException {

		WebScanDAO webScanDAO = new WebScanDAO(SmfManager.getInstance().getDataStore());
		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(objectId);
		if (webScanDocument != null) {
			return transformToScanRequest(webScanDocument);
		} else {
			throw new ResourceNotFoundException();
		}
	}

	private ScanRequest transformToScanRequest(WebScanDocument webScanDocument) {

		ScanRequest scanRequest = new ScanRequest();

		Baseuris baseuris = new Baseuris();
		baseuris.setBaseuri(webScanDocument.getBaseUris());
		scanRequest.setBaseuris(baseuris);

		Comments comments = new Comments();
		comments.setComment(webScanDocument.getComments());
		scanRequest.setComments(comments);

		Jobs jobs = new Jobs();
		long completed = webScanDocument.getCompletedCategorizationJobs() + webScanDocument.getCompletedExecutionJobs();
		long pending = webScanDocument.getPendingCategorizationJobs() + webScanDocument.getPendingExecutionJobs();
		long created = webScanDocument.getCreatedCategorizationJobs() + webScanDocument.getCreatedExecutionJobs();

		jobs.setCompleted(completed);
		jobs.setCreated(created);
		jobs.setPending(pending);
		scanRequest.setJobs(jobs);

		scanRequest.setId(webScanDocument.getId().toString());
		scanRequest.setPercentagedone(webScanDocument.getPercentageDone());
		scanRequest.setScanphase(webScanDocument.getScanPhase());
		scanRequest.setStartedtime(webScanDocument.getScanStartTime().getTime());
		scanRequest.setEstimatedtime(webScanDocument.getScanEndTime().getTime());

		return scanRequest;
	}

}
