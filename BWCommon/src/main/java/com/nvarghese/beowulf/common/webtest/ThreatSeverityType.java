package com.nvarghese.beowulf.common.webtest;

public enum ThreatSeverityType {

	FALSE_POSITIVE(0, "False Positive"), INFO(1, "Informational"), LOW(2, "Low"), MEDIUM(3, "Medium"), HIGH(4, "High");

	private int key;
	private String value;

	private ThreatSeverityType(int key, String value) {

		this.key = key;
		this.value = value;
	}

	public static ThreatSeverityType getSeverityByName(String name) {

		if (name == null) {
			return null;
		}

		if (name.equalsIgnoreCase(INFO.value))
			return ThreatSeverityType.INFO;
		else if (name.equalsIgnoreCase(HIGH.value))
			return ThreatSeverityType.HIGH;
		else if (name.equalsIgnoreCase(MEDIUM.value))
			return ThreatSeverityType.MEDIUM;
		else if (name.equalsIgnoreCase(LOW.value))
			return ThreatSeverityType.LOW;
		else if (name.equalsIgnoreCase(FALSE_POSITIVE.value))
			return ThreatSeverityType.FALSE_POSITIVE;
		else
			return null;
	}

	public String getFullName() {

		return this.value;
	}

	public static String[] getFullNames() {

		String[] fullNames = { FALSE_POSITIVE.value, INFO.value, LOW.value, MEDIUM.value, HIGH.value };

		return fullNames;
	}

}
