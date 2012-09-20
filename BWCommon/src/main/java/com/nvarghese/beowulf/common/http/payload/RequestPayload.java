package com.nvarghese.beowulf.common.http.payload;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

public interface RequestPayload {

	/**
	 * Convert to basic HTTPEntity type
	 * 
	 * @return
	 */
	public HttpEntity toHttpEntity();

	/**
	 * Byte stream of the request payload
	 * 
	 * @return
	 */
	public byte[] getBody();
	

	/**
	 * Get the content type
	 * @return
	 */
	public ContentType getContentType();
}
