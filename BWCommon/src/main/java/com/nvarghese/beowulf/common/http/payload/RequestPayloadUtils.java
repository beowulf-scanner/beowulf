package com.nvarghese.beowulf.common.http.payload;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestPayloadUtils {

	static Logger logger = LoggerFactory.getLogger(RequestPayloadUtils.class);

	public static SerializedRequestPayload serialize(RequestPayload requestPayload) {

		SerializedRequestPayload serializedRequestPayload = null;
		if (requestPayload instanceof UrlEncodedRequestPayload) {
			UrlEncodedRequestPayload urlEncodedRequestPayload = (UrlEncodedRequestPayload) requestPayload;
			serializedRequestPayload = new SerializedRequestPayload(urlEncodedRequestPayload.getBody(), urlEncodedRequestPayload.getContentType()
					.getMimeType(), urlEncodedRequestPayload.getContentType().getCharset().name(), UrlEncodedRequestPayload.class.getName());

		} else if (requestPayload instanceof MultipartEncodedRequestPayload) {
			MultipartEncodedRequestPayload multipartEncodedRequestPayload = (MultipartEncodedRequestPayload) requestPayload;
			serializedRequestPayload = new SerializedRequestPayload(multipartEncodedRequestPayload.getBody(), multipartEncodedRequestPayload
					.getContentType().getMimeType(), multipartEncodedRequestPayload.getContentType().getCharset().name(),
					MultipartEncodedRequestPayload.class.getName());
		} else if (requestPayload instanceof ByteArrayRequestPayload) {
			ByteArrayRequestPayload byteArrayRequestPayload = (ByteArrayRequestPayload) requestPayload;
			serializedRequestPayload = new SerializedRequestPayload(byteArrayRequestPayload.getBody(), byteArrayRequestPayload.getContentType()
					.getMimeType(), byteArrayRequestPayload.getContentType().getCharset().name(), ByteArrayRequestPayload.class.getName());
		} else if (requestPayload instanceof EntityWrappedRequestPayload) {
			EntityWrappedRequestPayload entityWrappedRequestPayload = (EntityWrappedRequestPayload) requestPayload;
			serializedRequestPayload = new SerializedRequestPayload(entityWrappedRequestPayload.getBody(), entityWrappedRequestPayload
					.getContentType().getMimeType(), entityWrappedRequestPayload.getContentType().getCharset().name(),
					EntityWrappedRequestPayload.class.getName());

		} else {
			logger.warn("Unknown type of request payload received for serializing");
			throw new NotImplementedException();
		}

		return serializedRequestPayload;

	}

	public static RequestPayload deserialize(SerializedRequestPayload serializedRequestPayload) {

		RequestPayload requestPayload = null;
		if (UrlEncodedRequestPayload.class.getName().equalsIgnoreCase(serializedRequestPayload.getTargetClassName())) {
			List<NameValuePair> parameters = URLEncodedUtils.parse(new String(serializedRequestPayload.getBody()), serializedRequestPayload
					.getContentType().getCharset());
			UrlEncodedRequestPayload urlRequestPayload = new UrlEncodedRequestPayload(parameters, serializedRequestPayload.getContentType()
					.getCharset().name());
			return urlRequestPayload;

		} else if (MultipartEncodedRequestPayload.class.getName().equalsIgnoreCase(serializedRequestPayload.getTargetClassName())) {
			logger.warn("MultipartEncodedRequestPayload cannot be deserialized");
			throw new NotImplementedException();
		} else if (ByteArrayRequestPayload.class.getName().equalsIgnoreCase(serializedRequestPayload.getTargetClassName())) {
			ByteArrayRequestPayload byteArrayRequestPayload = new ByteArrayRequestPayload(serializedRequestPayload.getBody(),
					serializedRequestPayload.getContentType());
			return byteArrayRequestPayload;
		} else if (EntityWrappedRequestPayload.class.getName().equalsIgnoreCase(serializedRequestPayload.getTargetClassName())) {
			logger.warn("EntityWrappedRequestPayload cannot be deserialized");
			throw new NotImplementedException();
		} else {
			logger.warn("Unknown type of request payload received for deserializing");
			throw new NotImplementedException();
		}
	}

}
