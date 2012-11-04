package com.nvarghese.beowulf.common.webtest;

public enum ReportThreatType {

		BRUTEFORCE("Brute Force", 8001),
		INSUFFICIENT_AUTH("Insufficient Authentication", 8002),
		WEAK_PASSWD("Weak Password Recovery Validation", 8003),
		CRED_SESS_PREDICTION("Credential/Session Prediction", 8004),
		INSUFFICIENT_AUTHZ("Insufficient Authorization", 8005),
		INSUFFICENT_SESS_EXP("Insufficient Session Expiration", 8006),
		SESS_FIXATION("Session Fixation", 8007),
		INSECURE_DIRECT_OBJ_REF("Insecure Direct Object Reference", 8008),
		CONTENT_SPOOFING("Content Spoofing", 8009),
		XSS("Cross Site Scripting", 8010),
		CSRF("Cross Site Request Forgery", 8011),
		BUFFER_OVERFLOW("Buffer Overflow", 8012),
		FORMAT_STRING_ATTACK("Format String Attack", 8013),
		LDAP_INJ("LDAP Injection", 8014),
		OS_COMMANDING("OS Commanding", 8015),
		SQL_INJ("SQL Injection", 8016),
		SSI_INJ("SSI Injection", 8017),
		XPATH_INJ("XPATH Injection", 8018),
		RFI("Remote File Inclusion", 8019),
		DIRECTORY_INDEXING("Directory Indexing", 8020),
		INFORMATION_LEAKAGE("Information Leakage", 8021),
		PATH_TRAVERSAL("Path Traversal", 8022),
		PRED_RESOURCE_LOCATION("Predictable Resource Location", 8023),
		ABUSE_OF_FUNCTIONALITY("Abuse of Functionality", 8024),
		DENIAL_OF_SERVICE("Denial of Service", 8025),
		INSUFF_PROCESS_VALIDATION("Insufficient Process Validation", 8026);

	private String name;
	private long id;

	ReportThreatType(String name, long id) {

		this.name = name;
		this.id = id;
	}

	public String getName() {

		return name;
	}

	public long getId() {

		return id;
	}

}
