package com.nvarghese.beowulf.common.report;

public enum Severity {

	INFORMATIONAL("Informational"), LOW("Low"), MEDIUM("Medium"), CRITICAL("Critical"), NONE("");

	String value;

	Severity(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public static Severity getSeverity(String value) {

		for (Severity s : Severity.values()) {
			if (s.value.equalsIgnoreCase(value)) {
				return s;
			}
		}
		return Severity.NONE;

	}

}
