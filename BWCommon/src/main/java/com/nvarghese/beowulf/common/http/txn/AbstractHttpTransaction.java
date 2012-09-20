package com.nvarghese.beowulf.common.http.txn;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.payload.RequestPayload;
import com.nvarghese.beowulf.common.http.wrapper.HttpRequestWrapper;
import com.nvarghese.beowulf.common.http.wrapper.HttpResponseWrapper;

public abstract class AbstractHttpTransaction {

	private HttpRequestWrapper httpRequestWrapper;
	private HttpResponseWrapper httpResponseWrapper;

	private AtomicBoolean responseReady;
	private AtomicBoolean uncompressed;

	private String referer;

	protected AtomicBoolean payloadChanged;

	protected transient static Pattern charSetPattern = Pattern.compile("charset=(.+)", Pattern.CASE_INSENSITIVE);

	static transient Logger logger = LoggerFactory.getLogger(AbstractHttpTransaction.class);

	public AbstractHttpTransaction(HttpRequestWrapper requestWrapper, String referer) {

		this.httpRequestWrapper = requestWrapper;
		this.referer = referer;
		payloadChanged = new AtomicBoolean(false);
		responseReady = new AtomicBoolean(false);

	}

	public void execute() {

		try {
			responseReady.set(false);
			updateRequestPayload();

			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.addRequestInterceptor(new RequestAcceptEncoding());
			httpClient.addResponseInterceptor(new ResponseContentEncoding());

			URI uri = getUri();
			HttpContext localContext = new BasicHttpContext();
			HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());

			// execute request
			HttpResponse httpResponse = httpClient.execute(target, httpRequestWrapper.getHttpRequest(), localContext);

			updateResponse(httpResponse, localContext);
			responseReady.set(true);

		} catch (ClientProtocolException e) {
			logger.error("Problem in client protocol. Reason: {}", e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Problem in client protocol. Reason: {}", e.getMessage(), e);
		}
	}

	private void updateResponse(HttpResponse httpResponse, HttpContext localContext) {

		httpResponseWrapper = new HttpResponseWrapper(httpResponse);

		Object uncompressedAttribute = localContext.getAttribute(ResponseContentEncoding.UNCOMPRESSED);
		if (uncompressedAttribute != null && uncompressedAttribute instanceof Boolean) {
			uncompressed.set(Boolean.getBoolean(uncompressedAttribute.toString()));
		}

	}

	protected void updateRequestPayload() {

		if (payloadChanged.get()) {
			httpRequestWrapper.processUpdates();
		}
	}

	public URI getUri() {

		return httpRequestWrapper.getURI();
	}

	public String getReferer() {

		return referer;
	}

	public HttpResponse getResponse() {

		HttpResponse response = null;
		if (httpResponseWrapper != null) {
			response = httpResponseWrapper.getHttpResponse();
		}
		return response;
	}

	public byte[] getResponseBody() {

		byte[] responseBody = null;
		if (httpResponseWrapper != null) {
			responseBody = httpResponseWrapper.getResponseBody();
		}
		return responseBody;
	}

	public String getResponseBodyAsString() {

		String responseString = new String(getResponseBody());
		return responseString;
	}

	public ContentType getContentType() {

		ContentType contentType = ContentType.DEFAULT_TEXT;
		if (httpResponseWrapper != null) {
			contentType = httpResponseWrapper.getContentType();
		}

		return contentType;
	}

	public long getResponseContentLength() {

		long length = -1;
		if (getResponseBody() != null) {
			length = getResponseBody().length;
		}
		return length;
	}

	public Header[] getResponseHeaders(final String headerName) {

		Header[] headers = new Header[0];
		if (httpResponseWrapper != null) {
			headers = httpResponseWrapper.getHttpResponse().getHeaders(headerName);
		}
		return headers;
	}

	public HttpResponseWrapper getResponseWrapper() {

		return httpResponseWrapper;
	}

	public void setReferer(String referer) {

		this.referer = referer;
	}

	public boolean isResponseReady() {

		return responseReady.get();
	}

	public boolean isUncompressed() {

		return uncompressed.get();
	}

}
