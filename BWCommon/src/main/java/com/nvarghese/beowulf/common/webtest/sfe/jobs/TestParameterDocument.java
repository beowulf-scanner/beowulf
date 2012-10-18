package com.nvarghese.beowulf.common.webtest.sfe.jobs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Serialized;
import com.google.code.morphia.annotations.Transient;

@Embedded
public class TestParameterDocument {

	@Property("param_type")
	private String parameterType;

	@Transient
	private Object parameterValue;

	@Serialized
	private byte[] _paramValueBytes;

	@Transient
	static Logger logger = LoggerFactory.getLogger(TestParameterDocument.class);

	public TestParameterDocument() {

	}

	@PostLoad
	void postLoadOperation() {

		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(_paramValueBytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			parameterValue = ois.readObject();
		} catch (IOException e) {
			logger.error("Failed to load paramterValue. Reason: {}", e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			logger.error("Failed to load paramterValue. Reason: {}", e.getMessage(), e);
		}

	}

	@PrePersist
	void prePersist() {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(parameterValue);
			oos.flush();
			oos.close();
			bos.close();
			_paramValueBytes = bos.toByteArray();
		} catch (IOException e) {
			logger.error("Failed to serialize paramterValue. Reason: {}", e.getMessage(), e);
		}

	}

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
