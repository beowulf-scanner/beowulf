package com.nvarghese.beowulf.common.scan.model;

import com.nvarghese.beowulf.common.report.ReportFormat;
import com.nvarghese.beowulf.common.report.Severity;

public class ReportScanConfigDocument {

	private String fileName;
	private Severity minimumSeverity;
	private boolean autoReportGeneration;
	private ReportFormat reportFormat;

}
