package com.nvarghese.beowulf.common.webtest.model;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

@Embedded
public class TestModuleOptionDocument {

	@Property("option_name")
	private String optionName;

	@Property("option_value")
	private String optionValue;

	@Property("option_group")
	private String optionGroup;

	@Property("option_type")
	private String optionType;

	public String getOptionName() {

		return optionName;
	}

	public void setOptionName(String optionName) {

		this.optionName = optionName;
	}

	public String getOptionValue() {

		return optionValue;
	}

	public void setOptionValue(String optionValue) {

		this.optionValue = optionValue;
	}

	public String getOptionGroup() {

		return optionGroup;
	}

	public void setOptionGroup(String optionGroup) {

		this.optionGroup = optionGroup;
	}

	public String getOptionType() {

		return optionType;
	}

	public void setOptionType(String optionType) {

		this.optionType = optionType;
	}

}
