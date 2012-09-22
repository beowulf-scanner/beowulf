package com.nvarghese.beowulf.common.http.payload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipartEncodedRequestPayload implements RequestPayload {

	// private String charset;
	private Map<String, ContentBody> parts = new HashMap<String, ContentBody>();

	String boundary;

	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();

	static Logger logger = LoggerFactory.getLogger(MultipartEncodedRequestPayload.class);

	public MultipartEncodedRequestPayload() {

		parts.clear();

		if (boundary == null)
			boundary = generateBoundary();

	}

	public MultipartEncodedRequestPayload(Map<String, ContentBody> localparts) {

		parts.clear();

		if (boundary == null)
			boundary = generateBoundary();

		for (String name : localparts.keySet()) {
			parts.put(name, localparts.get(name));

		}

	}

	public MultipartEncodedRequestPayload(String characterSet, String body) {

		/*
		 * No decoding logic. Sd we do ?
		 */
	}

	@Override
	public byte[] getBody() {

		MultipartEntity reqEntity = (MultipartEntity) toHttpEntity();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try {

			reqEntity.writeTo(bout);
		} catch (IOException e) {
			logger.error("Failed to convert to bytearray. Reason: {}", e.getMessage(), e);
		}

		return bout.toByteArray();
	}

	@Override
	public HttpEntity toHttpEntity() {

		if (boundary == null) {
			boundary = generateBoundary();
		}

		MultipartEntity reqEntity = new MultipartEntity(null, boundary, null);
		for (String name : parts.keySet()) {

			reqEntity.addPart(name, parts.get(name));
		}

		return reqEntity;

	}

	public void addPart(String name, ContentBody contentBody) {

		parts.put(name, contentBody);

	}

	public Map<String, ContentBody> getParts() {

		Map<String, ContentBody> cParts = new HashMap<String, ContentBody>();
		cParts.putAll(parts);

		return cParts;
	}

	public boolean removePart(String name) {

		if (parts.keySet().contains(name)) {
			parts.remove(name);
			return true;
		} else
			return false;
	}

	private String generateBoundary() {

		// copied shamelessly from httpmime ;-)

		StringBuilder buffer = new StringBuilder();
		Random rand = new Random();
		int count = rand.nextInt(11) + 30; // a random size from 30 to 40

		for (int i = 0; i < count; i++) {
			buffer.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
		}
		return buffer.toString();

	}

	public ContentBody getPart(String name) {

		if (parts.keySet().contains(name)) {
			return parts.get(name);

		} else
			return null;
	}

	@Override
	public ContentType getContentType() {

		return ContentType.MULTIPART_FORM_DATA;
	}
}
