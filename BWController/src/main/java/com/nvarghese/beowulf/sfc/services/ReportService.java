package com.nvarghese.beowulf.sfc.services;

import java.net.UnknownHostException;

import org.apache.commons.configuration.ConfigurationException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.common.ds.DataStoreUtil;
import com.nvarghese.beowulf.common.jobs.ReportGenerateJob;
import com.nvarghese.beowulf.common.report.ReportFormat;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.utils.ByteUtils;
import com.nvarghese.beowulf.common.webtest.ThreatSeverityType;
import com.nvarghese.beowulf.sfc.SFControllerManager;
import com.nvarghese.beowulf.sfc.report.generator.AbstractReportGenerator;
import com.nvarghese.beowulf.sfc.report.generator.HtmlReportGenerator;

public class ReportService {

	static Logger logger = LoggerFactory.getLogger(ReportService.class);

	public void generateReport(ReportGenerateJob reportGenerateJob) {

		logger.info("New report_gen job received to generate report with WebScanDocument#obj_id: {}", reportGenerateJob.getWebScanObjectId());

		ObjectId id = new ObjectId(reportGenerateJob.getWebScanObjectId());
		WebScanDAO webScanDAO = new WebScanDAO(SFControllerManager.getInstance().getDataStore());

		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(id);
		
		if (webScanDocument != null) {
			String reportFileName = "report_" + ByteUtils.toHex(ByteUtils.getRandomBytes(6));
			Datastore scanInstanceDs;
			try {
				scanInstanceDs = DataStoreUtil.createOrGetDataStore(BeowulfCommonConfigManager.getDbUri(), webScanDocument.getTxnDbName());
				AbstractReportGenerator reportGenerator = new HtmlReportGenerator(webScanDocument, scanInstanceDs, ReportFormat.HTML, reportFileName,
						ThreatSeverityType.INFO);
				reportGenerator.generateReport();
			} catch (UnknownHostException e) {
				logger.error("Failed to generate report. Reason: {}", e.getMessage(), e);
			} catch (ConfigurationException e) {
				logger.error("Failed to generate report. Reason: {}", e.getMessage(), e);
			}

		}
	}

}
