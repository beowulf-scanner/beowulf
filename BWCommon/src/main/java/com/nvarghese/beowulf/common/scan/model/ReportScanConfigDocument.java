package com.nvarghese.beowulf.common.scan.model;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.report.ReportFormat;
import com.nvarghese.beowulf.common.report.Severity;

@Embedded
public class ReportScanConfigDocument {

	@Property("file_name")
	private String fileName;

	@Property("min_severity")
	private Severity minimumSeverity;

	@Property("auto_report_generation")
	private boolean autoReportGeneration;

	@Property("report_format")
	private ReportFormat reportFormat;

	public String getFileName() {

		return fileName;
	}

	public void setFileName(String fileName) {

		this.fileName = fileName;
	}

	public Severity getMinimumSeverity() {

		return minimumSeverity;
	}

	public void setMinimumSeverity(Severity minimumSeverity) {

		this.minimumSeverity = minimumSeverity;
	}

	public boolean isAutoReportGeneration() {

		return autoReportGeneration;
	}

	public void setAutoReportGeneration(boolean autoReportGeneration) {

		this.autoReportGeneration = autoReportGeneration;
	}

	public ReportFormat getReportFormat() {

		return reportFormat;
	}

	public void setReportFormat(ReportFormat reportFormat) {

		this.reportFormat = reportFormat;
	}

}
