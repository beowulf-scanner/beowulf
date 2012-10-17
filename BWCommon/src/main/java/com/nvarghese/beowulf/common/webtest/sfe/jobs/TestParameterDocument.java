package com.nvarghese.beowulf.common.webtest.sfe.jobs;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Serialized;

@Embedded
public class TestParameterDocument {

	@Property("param_type")
	private String parameterType;

	@Serialized("param_value")
	private Object parameterValue;

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public Object getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Object parameterValue) {
		this.parameterValue = parameterValue;
	}

}
