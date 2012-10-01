package com.nvarghese.beowulf.common.scan.model;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

@Embedded
public class SessionSettingScanConfigDocument {

	@Property("username")
	private String userName;

	@Property("password")
	private String password;

	@Property("username_field_pattern")
	private String usernameFieldPattern;

	@Property("password_field_pattern")
	private String passwordFieldPattern;

	@Property("known_session_id_patterns")
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
