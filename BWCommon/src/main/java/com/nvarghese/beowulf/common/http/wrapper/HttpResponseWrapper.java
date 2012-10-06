package com.nvarghese.beowulf.common.http.wrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseWrapper extends HttpMessageWrapper {

	private StatusLine statusLine;
	private HttpResponse httpResponse;
	private ContentType contentType;
	private List<Header> headers;
	private byte[] responseBody;

	static Logger logger = LoggerFactory.getLogger(HttpResponseWrapper.class);

	HttpResponseWrapper() {

		super();
		headers = new ArrayList<Header>();
	}

	public HttpResponseWrapper(HttpResponse httpResponse) {

		this();
		this.httpResponse = httpResponse;
		parseHttpResponse();
	}

	public HttpResponseWrapper(BasicStatusLine statusLine, List<? extends Header> headers, byte[] responseBody) {

		this();
		this.statusLine = statusLine;
		this.headers.addAll(headers);
		this.responseBody = responseBody;
		parseResonseBody();

	}

	private void parseResonseBody() {

		httpResponse = new BasicHttpResponse(statusLine);
		ByteArrayEntity entity = new ByteArrayEntity(responseBody);
		httpResponse.setEntity(entity);
		httpResponse.setHeaders(headers.toArray(new Header[0]));
		
		//set content type
		Header contentTypeHeader = this.getFirstHeader(HttpHeaders.CONTENT_TYPE);
		if (contentTypeHeader != null) {
			contentType = ContentType.parse(contentTypeHeader.getValue());
		} else {
			contentType = ContentType.getOrDefault(entity);
		}

	}

	private void parseHttpResponse() {

		this.headers.addAll(Arrays.asList(httpResponse.getAllHeaders()));

		HttpEntity entity = httpResponse.getEntity();

		// preps an empty response body, incase it was empty
		responseBody = new byte[0];

		Header contentEncodingHeader = this.getFirstHeader(HttpHeaders.CONTENT_ENCODING);

		if (entity != null) {
			
			//set content type
			Header contentTypeHeader = this.getFirstHeader(HttpHeaders.CONTENT_TYPE);
			if (contentTypeHeader != null) {
				contentType = ContentType.parse(contentTypeHeader.getValue());
			} else {
				contentType = ContentType.getOrDefault(entity);
			}
			
			try {
				// already decompressed if ResponseContentEncoding interceptor
				// is enabled
				responseBody = EntityUtils.toByteArray(entity);
			} catch (IOException e) {
				logger.error("Problem processing HTTP response. Reason: {} ", e.getMessage(), e);
			}
		}

		statusLine = httpResponse.getStatusLine();

	}

	public StatusLine getStatusLine() {

		return statusLine;
	}

	public HttpResponse getHttpResponse() {

		return httpResponse;
	}

	public List<? extends Header> getHeaders() {

		return headers;
	}

	public byte[] getResponseBody() {

		return responseBody;
	}

	public Header getFirstHeader(String headerName) {

		return httpResponse.getFirstHeader(headerName);
	}

	public ContentType getContentType() {

		return contentType;
	}

}
