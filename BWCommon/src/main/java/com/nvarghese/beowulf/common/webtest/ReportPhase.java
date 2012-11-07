package com.nvarghese.beowulf.common.webtest;

public enum ReportPhase {

		NOT_STARTED("Not Started", 0, false),
		REPORT_GENERATION_STARTED("Report Generation Started", 1, false),
		REPORT_GENERATION_COMPLETED("Report Generation Completed", 2, true), // stop_phase
		ERROR("Error", 3, true); // stop_phase

	private String name;
	private int value;
	private boolean stopState;

	private ReportPhase(String name, int value, boolean stopState) {

		this.name = name;
		this.value = value;
		this.stopState = stopState;
	}

	public int getValue() {

		return value;
	}

	public String getName() {

		return name;
	}

	public boolean isStopState() {

		return stopState;
	}

	public static ReportPhase getReportPhase(String name) {

		for (ReportPhase reportPhase : ReportPhase.values()) {
			if (reportPhase.getName().equalsIgnoreCase(name))
				return reportPhase;
		}
		/*
		 * Do not want to return null. If it didnt match anything, lets start it
		 * all over again
		 */
		return ReportPhase.NOT_STARTED;
	}

}
