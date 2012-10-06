package com.nvarghese.beowulf.common.webtest;

public enum WebTestCategory {

		SPIDER("Spider"),
		FILE_ENUMERATION("File Enumeration"),
		INFORMATION_LEAKAGE("Information Leakage"),
		SESSION_MANAGEMENT("Session Management"),
		HIDDEN("Hidden"),
		NIKTO("Nikto"),
		SQL_INJECTION("SQL Injection"),
		XPATH_INJECTION("XPATH Injection"),
		SSI_INJECTION("SSI Injection"),
		XSS("XSS"),
		MISCELLANEOUS_ATTACKS("Miscellaneous Attacks"),
		ARCHITECTURE("Application Architecture"),
		WEB_SERVER_CONFIGURATION("Web Server Configuration"),
		BRUTEFORCE("Brute Force Attacks"),
		OS_COMMANDING("OS Command Execution"),
		AUTHORIZATION("Authorization"),
		MALWARE("Malware Detection"),
		FILE_INCLUSION("File Inclusion"),
		NONE("None");

	private String value;

	private WebTestCategory(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public static WebTestCategory getWebTestCategory(String testCategory) {

		for (WebTestCategory category : WebTestCategory.values()) {
			if (category.getValue().equalsIgnoreCase(testCategory)) {
				return category;
			}
		}
		return NONE;
	}

}
