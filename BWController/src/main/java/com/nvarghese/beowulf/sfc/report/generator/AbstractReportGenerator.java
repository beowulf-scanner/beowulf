package com.nvarghese.beowulf.sfc.report.generator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.report.ReportFormat;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.ReportPhase;
import com.nvarghese.beowulf.common.webtest.ThreatSeverityType;
import com.nvarghese.beowulf.sfc.SFControllerManager;
import com.nvarghese.beowulf.sfc.report.ReportException;

public abstract class AbstractReportGenerator {

	protected WebScanDocument webScanDocument;
	protected Datastore scanInstanceDatastore;
	private String reportFileName;
	private ThreatSeverityType minSeverity;
	private ReportFormat reportFileFormat;
	private ReportPhase reportPhase;
	private final String GZIP_EXTN = ".gz";

	static Logger logger = LoggerFactory.getLogger(AbstractReportGenerator.class);

	public AbstractReportGenerator(final WebScanDocument webScanDocument, final Datastore scanInstanceDatastore, final ReportFormat reportFormat,
			final String reportFileName, final ThreatSeverityType minSeverity) {

		this.webScanDocument = webScanDocument;
		this.scanInstanceDatastore = scanInstanceDatastore;
		this.reportFileName = reportFileName;
		this.minSeverity = minSeverity;
		this.reportFileFormat = reportFormat;
		this.reportPhase = ReportPhase.NOT_STARTED;

	}

	public String getReportFileName() {

		return this.reportFileName;
	}

	public void setReportFileName(final String reportFileName) {

		this.reportFileName = reportFileName;
	}

	public String getReportFilePath() {

		String filePath = SFControllerManager.getInstance().getSettings().getReportDir() + File.separator + reportFileName;

		return filePath;
	}

	public ThreatSeverityType getMinSeverity() {

		return this.minSeverity;
	}

	public void setMinSeverity(final ThreatSeverityType severityType) {

		this.minSeverity = severityType;
	}

	public ReportFormat getReportFileFormat() {

		return reportFileFormat;
	}

	public void setReportFileFormat(final ReportFormat reportFileFormat) {

		this.reportFileFormat = reportFileFormat;
	}

	public ReportPhase getReportPhase() {

		return reportPhase;
	}

	public void setReportPhase(final ReportPhase reportPhase) {

		this.reportPhase = reportPhase;
	}

	public void generateReport() {

		try {

			logger.info("Report generation started with format: {}", reportFileFormat.getValue());
			reportPhase = gotoPhase(ReportPhase.REPORT_GENERATION_STARTED, "Report generation started with format: " + reportFileFormat.getValue());
			writeReport();
			// compressReport();
			reportPhase = gotoPhase(ReportPhase.REPORT_GENERATION_COMPLETED,
					"Report generation completed with format: " + reportFileFormat.getValue());
			logger.info("Report generation completed with format: {}", reportFileFormat.getValue());
		} catch (Exception e) {
			reportPhase = gotoPhase(ReportPhase.ERROR, "Report generation failed: " + e.getMessage());
			logger.error("Failed to generate report. Reason: {}", e.getMessage(), e);
		}
	}

	private void compressReport() throws ReportException {

		String reportFilePath = getReportFilePath();
		String compressedFilePath = reportFilePath + GZIP_EXTN;
		try {
			int count;
			byte[] data = new byte[2048];

			BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File(reportFilePath)), 2048);
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File(compressedFilePath)), 2048);
			GZIPOutputStream gzipOutStream = new GZIPOutputStream(bout);

			while ((count = bin.read(data, 0, 2048)) != -1) {
				gzipOutStream.write(data, 0, count);
			}
			gzipOutStream.flush();
			gzipOutStream.close();
			bout.close();
			bin.close();

		} catch (FileNotFoundException e) {
			ReportException reportException = new ReportException("FileNotFoundException while opening FileStream: " + e.getMessage());
			reportException.initCause(e.getCause());
			throw reportException;

		} catch (IOException e) {
			ReportException reportException = new ReportException("IOException while compressing file: " + e.getMessage());
			reportException.initCause(e.getCause());
			throw reportException;
		}

	}

	public String getCompressedReportFilePath() {

		return getReportFilePath() + GZIP_EXTN;
	}

	abstract protected void writeReport() throws ReportException;

	private ReportPhase gotoPhase(final ReportPhase newReportPhase, final String message) {

		if (newReportPhase == reportPhase)
			return reportPhase;

		/*
		 * Fire events in a different thread
		 */
		final ReportPhase oldReportPhase = reportPhase;

		return newReportPhase;

	}

}
