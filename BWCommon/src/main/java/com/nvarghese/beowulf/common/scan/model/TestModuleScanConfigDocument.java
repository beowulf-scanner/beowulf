package com.nvarghese.beowulf.common.scan.model;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

@Embedded
public class TestModuleScanConfigDocument {

	@Property("module_number")
	private long moduleNumber;

	@Property("module_name")
	private String moduleName;

	@Property("module_enabled")
	private boolean enabled;

	@Property("options")
	private Map<String, String> options = new HashMap<String, String>();

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

	public Map<String, String> getOptions() {

		return options;
	}

	public void setOptions(Map<String, String> options) {

		this.options = options;
	}

}
