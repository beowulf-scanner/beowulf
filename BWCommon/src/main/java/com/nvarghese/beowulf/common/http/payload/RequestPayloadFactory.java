package com.nvarghese.beowulf.common.http.payload;

import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestPayloadFactory {

	static Logger logger = LoggerFactory.getLogger(RequestPayloadFactory.class);

	public static RequestPayload createRequestPayload(HttpRequest request) {

		if (request instanceof HttpEntityEnclosingRequestBase) {
			HttpEntity entity = ((HttpEntityEnclosingRequestBase) request).getEntity();
			return convertToRequestPayload(entity);
		} else {
			HttpUriRequest uriRequest = (HttpUriRequest) request;
			return new UrlEncodedRequestPayload(URLEncodedUtils.parse(uriRequest.getURI(), "ISO-8559-1"));
		}
	}

	public static RequestPayload convertToRequestPayload(HttpEntity entity) {

		if (entity instanceof UrlEncodedFormEntity) {
			UrlEncodedFormEntity UrlEncodedFormEntity = (UrlEncodedFormEntity) entity;
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			try {
				nvps = URLEncodedUtils.parse(UrlEncodedFormEntity);
			} catch (IOException e) {
				logger.error("Failed to parse urlencoded string. Reason: {}", e.getMessage(), e);
			}
			return new UrlEncodedRequestPayload(nvps);
		} else if (entity instanceof MultipartEntity) {
			// no parser for multipart data
			return new MultipartEncodedRequestPayload();
		} else if (entity instanceof StringEntity) {
			StringEntity stringEntity = (StringEntity) entity;
			byte[] bytes = new byte[0];
			try {
				bytes = EntityUtils.toByteArray(stringEntity);
			} catch (UnsupportedCharsetException e) {
				logger.error("Failed to convert to bytearray. Reason: {}", e.getMessage(), e);
			} catch (ParseException e) {
				logger.error("Failed to convert to bytearray. Reason: {}", e.getMessage(), e);
			} catch (IOException e) {
				logger.error("Failed to convert to bytearray. Reason: {}", e.getMessage(), e);
			}
			return new ByteArrayRequestPayload(bytes, ContentType.get(stringEntity));
		} else {
			return new EntityWrappedRequestPayload(entity);
		}

	}

}
