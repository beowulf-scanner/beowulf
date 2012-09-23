package com.nvarghese.beowulf.common.scan.model;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class SessionSettingScanConfigDocument {

	private String userName;
	private String password;
	private String usernameFieldPattern;
	private String passwordFieldPattern;

	private List<String> knownSessionIdPatterns;

	public String getUserName() {

		return userName;
	}

	public void setUserName(String userName) {

		this.userName = userName;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	public String getUsernameFieldPattern() {

		return usernameFieldPattern;
	}

	public void setUsernameFieldPattern(String usernameFieldPattern) {

		this.usernameFieldPattern = usernameFieldPattern;
	}

	public String getPasswordFieldPattern() {

		return passwordFieldPattern;
	}

	public void setPasswordFieldPattern(String passwordFieldPattern) {

		this.passwordFieldPattern = passwordFieldPattern;
	}

	public List<String> getKnownSessionIdPatterns() {

		return knownSessionIdPatterns;
	}

	public void setKnownSessionIdPatterns(List<String> knownSessionIdPatterns) {

		this.knownSessionIdPatterns = knownSessionIdPatterns;
	}

}
