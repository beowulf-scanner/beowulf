package com.nvarghese.beowulf.common.webtest;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum WebTestCategory {

	@XmlEnumValue("SPIDER") SPIDER("Spider"),
	@XmlEnumValue("FILE_ENUMERATION") FILE_ENUMERATION("File Enumeration"),
	@XmlEnumValue("INFORMATION_LEAKAGE") INFORMATION_LEAKAGE("Information Leakage"),
	@XmlEnumValue("SESSION_MANAGEMENT") SESSION_MANAGEMENT("Session Management"),
	@XmlEnumValue("HIDDEN") HIDDEN("Hidden"),
	@XmlEnumValue("NIKTO") NIKTO("Nikto"),
	@XmlEnumValue("SQL_INJECTION") SQL_INJECTION("SQL Injection"),
	@XmlEnumValue("XPATH_INJECTION") XPATH_INJECTION("XPATH Injection"),
	@XmlEnumValue("SSI_INJECTION") SSI_INJECTION("SSI Injection"), 
	@XmlEnumValue("XSS") XSS("XSS"),
	@XmlEnumValue("MISCELLANEOUS_ATTACKS") MISCELLANEOUS_ATTACKS("Miscellaneous Attacks"),
	@XmlEnumValue("ARCHITECTURE") ARCHITECTURE("Application Architecture"),
	@XmlEnumValue("WEB_SERVER_CONFIGURATION") WEB_SERVER_CONFIGURATION("Web Server Configuration"),
	@XmlEnumValue("BRUTEFORCE") BRUTEFORCE("Brute Force Attacks"), 
	@XmlEnumValue("OS_COMMANDING") OS_COMMANDING("OS Command Execution"),
	@XmlEnumValue("AUTHORIZATION") AUTHORIZATION("Authorization"),
	@XmlEnumValue("MALWARE") MALWARE("Malware Detection"),
	@XmlEnumValue("FILE_INCLUSION") FILE_INCLUSION("File Inclusion"),
	@XmlEnumValue("") NONE("None");

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
