package com.nvarghese.beowulf.common.webtest.model;

import java.util.Date;
import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;
import com.nvarghese.beowulf.common.webtest.WebTestCategory;
import com.nvarghese.beowulf.common.webtest.WebTestType;

@Entity("test_module_meta")
public class TestModuleMetaDataDocument extends AbstractDocument {

	@Property("module_number")
	private long moduleNumber;

	@Property("module_name")
	private String moduleName;

	@Property("module_enabled")
	private boolean enabled;

	@Property("test_category")
	private WebTestCategory testCategory;

	@Property("module_description")
	private String description;

	@Property("module_dependencies")
	private long[] moduleDependencies;

	@Property("test_type")
	private WebTestType testType;

	@Property("test_attributes")
	private String[] testAttributes;

	@Embedded
	private List<TestModuleOptionDocument> options;
	
	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

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

	public List<TestModuleOptionDocument> getOptions() {

		return options;
	}

	public void setOptions(List<TestModuleOptionDocument> options) {

		this.options = options;
	}

	public WebTestCategory getTestCategory() {

		return testCategory;
	}

	public void setTestCategory(WebTestCategory testCategory) {

		this.testCategory = testCategory;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public long[] getModuleDependencies() {

		return moduleDependencies;
	}

	public void setModuleDependencies(long[] moduleDependencies) {

		this.moduleDependencies = moduleDependencies;
	}

	public WebTestType getTestType() {

		return testType;
	}

	public void setTestType(WebTestType testType) {

		this.testType = testType;
	}

	public String[] getTestAttributes() {

		return testAttributes;
	}

	public void setTestAttributes(String[] testAttributes) {

		this.testAttributes = testAttributes;
	}

}
