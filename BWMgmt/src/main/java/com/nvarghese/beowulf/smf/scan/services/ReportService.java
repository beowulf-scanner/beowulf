package com.nvarghese.beowulf.smf.scan.services;

import javax.jms.JMSException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.exception.ServiceException;
import com.nvarghese.beowulf.common.jobs.NewScanJob;
import com.nvarghese.beowulf.common.jobs.ReportGenerateJob;
import com.nvarghese.beowulf.common.scan.dao.MasterScanReportDAO;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.MasterScanReportDocument;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.ReportPhase;
import com.nvarghese.beowulf.common.webtest.ReportStatusMessage;
import com.nvarghese.beowulf.common.webtest.ScanPhase;
import com.nvarghese.beowulf.smf.SmfManager;
import com.nvarghese.beowulf.smf.scan.dto.reasons.Reason;
import com.nvarghese.beowulf.smf.scan.dto.report.Report;

public class ReportService {

	static Logger logger = LoggerFactory.getLogger(ReportService.class);

	public Report submitReportGenerateRequest(ObjectId webScanObjectId, Reason reason) throws ResourceNotFoundException, ServiceException {

		WebScanDAO webScanDAO = new WebScanDAO(SmfManager.getInstance().getDataStore());
		WebScanDocument document = webScanDAO.getWebScanDocument(webScanObjectId);
		MasterScanReportDAO reportDAO = new MasterScanReportDAO(SmfManager.getInstance().getDataStore());
		Report report = new Report();

		if (document != null) {

			MasterScanReportDocument reportDocument = document.getScanReport();
			ScanPhase scanPhase = ScanPhase.getScanPhase(document.getScanPhase());

			if (scanPhase.isStopState() || scanPhase == ScanPhase.MANUAL_VALIDATION) {

				ReportPhase currentReportPhase = ReportPhase.getReportPhase(reportDocument.getReportPhase());
				if (currentReportPhase == ReportPhase.NOT_STARTED || currentReportPhase.isStopState()) {
					/* all perfect to generate report */

					reportDocument.setReportGenReasonId(reason.getId());
					reportDocument.setReportGenReasonStr(reason.getValue());
					reportDAO.updateMasterScanReportDocument(reportDocument);

					try {
						ReportGenerateJob reportGenerateJob = new ReportGenerateJob(webScanObjectId.toString());
						BwControllerService bwControllerService = new BwControllerService();
						bwControllerService.submitJob(reportGenerateJob);
					} catch (JMSException e) {
						logger.error("Failed to submit the new scan job. Reason: {}", e.getMessage(), e);
						ServiceException se = new ServiceException();
						se.initCause(e.getCause());
						throw se;
					}

					report.status = ReportStatusMessage.SUBMITTED.getMessage();
					report.location = "";
					report.comments.commentsList = reportDocument.getComments();
					report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
					return report;
					// return Response.status(202).entity(report).build();

				} else {
					/* report gen in progress */
					report.status = ReportStatusMessage.ALREADY_SUBMITTED.getMessage();
					report.location = "";
					report.comments.commentsList = reportDocument.getComments();
					report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
					return report;
					// return Response.status(200).entity(report).build();
				}
			} else {
				/* scan in progress */
				report.status = ReportStatusMessage.NOT_ALLOWED.getMessage();
				report.location = "";
				report.comments.commentsList = reportDocument.getComments();
				report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
				return report;
				// return Response.status(200).entity(report).build();
			}

		} else {
			// return Response.status(Status.NOT_FOUND).entity("Scan with id '"
			// + id + "' is not found").build();
			throw new ResourceNotFoundException();
		}
	}

}
