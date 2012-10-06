package com.nvarghese.beowulf.common.http.payload;

import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

public class SerializedRequestPayload implements RequestPayload, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2211424151303208222L;

	private String mimeType;
	private String charset;
	private String targetClassName;
	private byte[] body;

	public SerializedRequestPayload(byte[] body, String mimeType, String charset, String targetClassName) {

		this.mimeType = mimeType;
		this.charset = charset;
		this.body = body;
		this.targetClassName = targetClassName;
	}

	@Override
	public HttpEntity toHttpEntity() {

		return null;
	}

	@Override
	public byte[] getBody() {

		return body;
	}

	@Override
	public ContentType getContentType() {

		ContentType contentType = ContentType.create(mimeType, charset);
		return contentType;
	}

	public String getTargetClassName() {

		return targetClassName;
	}

	public void setTargetClassName(String targetClassName) {

		this.targetClassName = targetClassName;
	}
}
