package com.nvarghese.beowulf.smf.scan.services;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.exception.ServiceException;
import com.nvarghese.beowulf.common.jobs.NewScanJob;
import com.nvarghese.beowulf.common.scan.dao.MasterScanConfigDAO;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.dto.config.Profile;
import com.nvarghese.beowulf.common.scan.model.MasterScanConfigDocument;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.smf.SmfManager;
import com.nvarghese.beowulf.smf.scan.dto.Baseuris;
import com.nvarghese.beowulf.smf.scan.dto.Comments;
import com.nvarghese.beowulf.smf.scan.dto.Jobs;
import com.nvarghese.beowulf.smf.scan.dto.ScanRequest;
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

		// persist
		MasterScanConfigDAO scanConfigDAO = new MasterScanConfigDAO(SmfManager.getInstance().getDataStore());
		scanConfigDAO.createMasterScanConfigDocument(scanConfigDocument);

		WebScanDocument webScanDocument = new WebScanDocument();
		webScanDocument.setScanConfig(scanConfigDocument);
		webScanDocument.setBaseUris(scanConfigDocument.getSettingScanConfig().getBaseURIList());

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

	private ScanRequest transformToScanRequest(WebScanDocument webScanDocument) {

		ScanRequest scanRequest = new ScanRequest();

		Baseuris baseuris = new Baseuris();
		baseuris.setBaseuri(webScanDocument.getBaseUris());
		scanRequest.setBaseuris(baseuris);

		Comments comments = new Comments();
		comments.setComment(webScanDocument.getComments());
		scanRequest.setComments(comments);

		Jobs jobs = new Jobs();
		jobs.setCompleted(webScanDocument.getCompletedJobs());
		jobs.setCreated(webScanDocument.getCreatedJobs());
		jobs.setPending(webScanDocument.getPendingJobs());
		scanRequest.setJobs(jobs);

		scanRequest.setId(webScanDocument.getId().toString());
		scanRequest.setPercentagedone(webScanDocument.getPercentageDone());
		scanRequest.setScanphase(webScanDocument.getScanPhase());
		scanRequest.setStartedtime(webScanDocument.getScanStartTime().getTime());
		scanRequest.setEstimatedtime(webScanDocument.getScanEndTime().getTime());

		return scanRequest;
	}

}
