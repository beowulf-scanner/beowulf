package com.nvarghese.beowulf.common.webtest;

public enum ScanPhase {

		VALIDATION("Validation", 0, false, false),
		INITIALIZATION("Initialization", 1, false, false),
		PRIMARY_SCAN("Primary Scan", 2, false, false),
		RECRAWL("Recrawl", 3, false, false),
		FINAL_REPORTING("Final Reporting", 4, false, false),
		ABORTING("Aborting", 5, false, false),
		TERMINATING("Terminating", 6, false, false),
		PAUSING("Pausing", 7, false, false),
		RESUMING("Resuming", 8, false, false),
		ERROR("Error", 9, true, false), // stop state
		COMPLETE("Complete", 10, true, false), // stop state
		ABORTED("Aborted", 11, true, false), // stop state
		MANUAL_VALIDATION("Manual Validation", 12, false, true), // offline
																	// state
		PAUSED("Paused", 13, true, false); // stop state

	String name;
	int value;
	boolean stopState;
	boolean passiveState;

	ScanPhase(String name, int value, boolean stopState, boolean passiveState) {

		this.name = name;
		this.value = value;
		this.stopState = stopState;
		this.passiveState = passiveState;
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

	public boolean isPassiveState() {

		return passiveState;
	}

	public static ScanPhase getScanPhase(String name) {

		for (ScanPhase scanPhase : ScanPhase.values()) {
			if (scanPhase.getName().equalsIgnoreCase(name))
				return scanPhase;
		}
		return null;
	}

	public static ScanPhase getScanPhase(int value) {

		for (ScanPhase scanPhase : ScanPhase.values()) {
			if (scanPhase.getValue() == value)
				return scanPhase;
		}
		return null;
	}

}
