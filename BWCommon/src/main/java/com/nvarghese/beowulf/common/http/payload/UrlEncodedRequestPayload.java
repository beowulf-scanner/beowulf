package com.nvarghese.beowulf.common.http.payload;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlEncodedRequestPayload implements RequestPayload {

	private Map<String, NameValuePair> mappedParameters;
	// private final List<NameValuePair> parameters;
	private String charset;

	static Logger logger = LoggerFactory.getLogger(UrlEncodedRequestPayload.class);

	public UrlEncodedRequestPayload(List<NameValuePair> parameters) {

		this(parameters, null);

	}

	public UrlEncodedRequestPayload(List<NameValuePair> parameters, String charset) {

		this.mappedParameters = new HashMap<String, NameValuePair>();
		this.charset = charset;
		if (parameters != null) {
			for (NameValuePair nv : parameters) {
				mappedParameters.put(nv.getName(), nv);
			}
		}

	}

	public void addParameter(NameValuePair parameter) {

		if (parameter == null)
			return;

		mappedParameters.put(parameter.getName(), parameter);
	}

	public void addParameter(String name, String value) {

		BasicNameValuePair bnp = new BasicNameValuePair(name, value);
		addParameter(bnp);
	}

	public void removeParameter(String name) {

		mappedParameters.remove(name);
	}

	public HttpEntity toHttpEntity() {

		UrlEncodedFormEntity formEntity = null;

		try {
			if (charset == null) {
				formEntity = new UrlEncodedFormEntity(mappedParameters.values());
			} else {
				List<NameValuePair> nvp = new ArrayList<NameValuePair>();
				nvp.addAll(mappedParameters.values());
				formEntity = new UrlEncodedFormEntity(nvp, charset);
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("Problem in converting to URL encoded entity. Reason: {}", e.getMessage(), e);
		}
		return formEntity;

	}

	@Override
	public byte[] getBody() {

		if (Charset.isSupported(charset)) {
			return URLEncodedUtils.format(mappedParameters.values(), Charset.forName(charset)).getBytes();
		} else {
			return URLEncodedUtils.format(mappedParameters.values(), Charset.defaultCharset()).getBytes();
		}
	}

	@Override
	public ContentType getContentType() {

		return ContentType.APPLICATION_FORM_URLENCODED;
	}

	public String toUrlQueryString() {

		if (Charset.isSupported(charset)) {
			return URLEncodedUtils.format(mappedParameters.values(), Charset.forName(charset));
		} else {
			return URLEncodedUtils.format(mappedParameters.values(), Charset.defaultCharset());
		}

	}

	public List<NameValuePair> getParameters() {

		List<NameValuePair> nps = new ArrayList<NameValuePair>();
		nps.addAll(mappedParameters.values());

		return nps;
	}

}
