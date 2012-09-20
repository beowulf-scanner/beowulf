package com.nvarghese.beowulf.common.http.payload;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;

public class ByteArrayRequestPayload implements RequestPayload {

	private byte[] body;
	private ContentType contentType;

	public ByteArrayRequestPayload(byte[] body, ContentType contentType) {

		this.body = body;
		this.contentType = contentType;
	}

	@Override
	public HttpEntity toHttpEntity() {

		HttpEntity entity = new ByteArrayEntity(body);
		return entity;

	}

	@Override
	public byte[] getBody() {

		return body;
	}

	@Override
	public ContentType getContentType() {

		return contentType;
	}

}
