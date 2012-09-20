package com.nvarghese.beowulf.common.http.payload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityWrappedRequestPayload implements RequestPayload {

	private final HttpEntity httpEntity;

	static Logger logger = LoggerFactory.getLogger(EntityWrappedRequestPayload.class);

	public EntityWrappedRequestPayload(final HttpEntity httpEntity) {

		this.httpEntity = httpEntity;
	}

	@Override
	public HttpEntity toHttpEntity() {

		return httpEntity;
	}

	@Override
	public byte[] getBody() {

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {
			httpEntity.writeTo(bout);
		} catch (IOException e) {
			logger.error("Failed to convert to bytearray. Reason: {}", e.getMessage(), e);
		}

		return bout.toByteArray();
	}

	@Override
	public ContentType getContentType() {

		ContentType type = ContentType.getOrDefault(httpEntity);
		return type;
	}

}
