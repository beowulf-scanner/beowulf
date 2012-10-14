package com.nvarghese.beowulf.common.scan.model;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.common.webtest.model.TestModuleOptionDocument;

@Embedded
public class TestModuleScanConfigDocument {

	@Property("module_number")
	private long moduleNumber;

	@Property("module_name")
	private String moduleName;

	@Property("module_enabled")
	private boolean enabled;

	@Property("test_type")
	private WebTestType testType;

	@Embedded
	private List<TestModuleOptionDocument> options;

	public long getModuleNumber() {

		return moduleNumber;
	}

	public void setModuleNumber(long moduleNumber) {

		this.moduleNumber = moduleNumber;
	}

	public String getModuleName() {

		return moduleName;
	}

	public void setModuleName(String moduleName) {

		this.moduleName = moduleName;
	}

	public boolean isEnabled() {

		return enabled;
	}

	public void setEnabled(boolean enabled) {

		this.enabled = enabled;
	}

	public WebTestType getTestType() {

		return testType;
	}

	public void setTestType(WebTestType testType) {

		this.testType = testType;
	}

	public List<TestModuleOptionDocument> getOptions() {

		return options;
	}

	public void setOptions(List<TestModuleOptionDocument> options) {

		this.options = options;
	}

}
