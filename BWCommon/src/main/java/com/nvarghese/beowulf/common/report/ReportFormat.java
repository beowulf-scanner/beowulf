package com.nvarghese.beowulf.common.report;

public enum ReportFormat {

	PDF("PDF"), XML("XML"), TEXT("TEXT"), HTML("HTML"), NONE("");

	String value;

	ReportFormat(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public static ReportFormat getReportFormat(String value) {

		for (ReportFormat format : ReportFormat.values()) {
			if (format.value.equalsIgnoreCase(value)) {
				return format;
			}
		}
		return ReportFormat.NONE;

	}
}
