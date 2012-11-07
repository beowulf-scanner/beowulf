package com.nvarghese.beowulf.sfc.quartz;

import java.net.UnknownHostException;

import org.apache.commons.configuration.ConfigurationException;
import org.bson.types.ObjectId;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.sfc.SFControllerManager;
import com.nvarghese.beowulf.sfc.services.ScanMonitorService;

public class ScanMonitorJob implements Job {

	public static final String WEBSCANOBJID = "WEBSCANOBJID";
	static Logger logger = LoggerFactory.getLogger(ScanMonitorJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		final ScanMonitorService scanMonitorService = new ScanMonitorService();
		JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
		final ObjectId webScanObjId = new ObjectId(dataMap.getString(WEBSCANOBJID));
		
		logger.info("Running scan monitor job for WebScanDocument#id: {}", webScanObjId);

		try {

			//update job details
			scanMonitorService.updateWebScanDocumentWithScanJobDetails(webScanObjId);
			
			//check status
			boolean running = scanMonitorService.isScanRunning(webScanObjId);
			WebScanDAO webScanDAO = new WebScanDAO(SFControllerManager.getInstance().getDataStore());
			WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(webScanObjId);
			boolean isScanJobsRunning = webScanDocument.isScanJobsInProgress();

			if (running) {
				if (isScanJobsRunning) {
					// no action required
				} else {
					// update scan jobs status
					isScanJobsRunning = true;
					webScanDAO.updateScanJobsInProgress(webScanDocument.getId(), isScanJobsRunning);
				}
			} else {
				if (isScanJobsRunning) {
					// check the scan running status in the next execution of
					// scheduled job
					isScanJobsRunning = false;
					webScanDAO.updateScanJobsInProgress(webScanDocument.getId(), isScanJobsRunning);
				} else {

					logger.info("Scan with id: {} reached complete stage. Shutting down scan instance server", webScanObjId.toString());

					// spawn a new thread
					new Thread() {

						public void run() {

							scanMonitorService.stopScanInstanceServer(webScanObjId);
						}
					}.start();
				}
			}

		} catch (UnknownHostException e) {
			logger.error("Failed to check scan status for id: {}, Reason: {}", webScanObjId.toString(), e.getMessage());
			e.printStackTrace();
		} catch (ConfigurationException e) {
			logger.error("Failed to check scan status for id: {}, Reason: {}", webScanObjId.toString(), e.getMessage());
			e.printStackTrace();
		}

	}

}
