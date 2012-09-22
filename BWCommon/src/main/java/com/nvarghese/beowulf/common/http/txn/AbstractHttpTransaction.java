package com.nvarghese.beowulf.common.http.txn;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.client.HttpClientFactory;
import com.nvarghese.beowulf.common.http.client.HttpClientUtil;
import com.nvarghese.beowulf.common.http.payload.MultipartEncodedRequestPayload;
import com.nvarghese.beowulf.common.http.payload.RequestPayload;
import com.nvarghese.beowulf.common.http.payload.UrlEncodedRequestPayload;
import com.nvarghese.beowulf.common.http.wrapper.HttpRequestWrapper;
import com.nvarghese.beowulf.common.http.wrapper.HttpResponseWrapper;

public abstract class AbstractHttpTransaction {

	private HttpRequestWrapper httpRequestWrapper;
	private HttpResponseWrapper httpResponseWrapper;
	private BasicCookieStore cookieStore;

	private AtomicBoolean responseReady;
	private AtomicBoolean uncompressed;

	private String referer;

	protected AtomicBoolean payloadChanged;

	/* logger */
	static transient Logger logger = LoggerFactory.getLogger(AbstractHttpTransaction.class);

	public AbstractHttpTransaction(HttpRequestWrapper requestWrapper, String referer) {

		this.cookieStore = new BasicCookieStore();
		this.httpRequestWrapper = requestWrapper;
		this.referer = referer;

		payloadChanged = new AtomicBoolean(false);
		responseReady = new AtomicBoolean(false);

	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void addRequestHeader(String name, String value) {

		httpRequestWrapper.getHttpRequest().addHeader(name, value);
	}

	/**
	 * 
	 * @param name
	 */
	public void removeRequestHeaders(String name) {

		httpRequestWrapper.getHttpRequest().removeHeaders(name);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Header getFirstRequestHeader(String name) {

		return httpRequestWrapper.getHttpRequest().getFirstHeader(name);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Header[] getRequestHeaders(String name) {

		return httpRequestWrapper.getHttpRequest().getHeaders(name);
	}

	/**
	 * Adds query parameter if encoding is of type x-www-urlencoded
	 * 
	 * 
	 */
	public void addQueryParameter(String name, String value) {

		RequestPayload requestPayload = httpRequestWrapper.getRequestPayload();
		if (requestPayload instanceof UrlEncodedRequestPayload) {
			((UrlEncodedRequestPayload) requestPayload).addParameter(name, value);
			payloadChanged.set(true);
		} else {
			logger.warn("Request payload is not of content-type: {}", ContentType.APPLICATION_FORM_URLENCODED);
		}
	}

	/**
	 * Removes the query parameter
	 * 
	 */
	public void removeQueryParameter(String name) {

		RequestPayload requestPayload = httpRequestWrapper.getRequestPayload();
		if (requestPayload instanceof UrlEncodedRequestPayload) {
			((UrlEncodedRequestPayload) requestPayload).removeParameter(name);
			payloadChanged.set(true);
		} else {
			logger.warn("Request payload is not of content-type: {}", ContentType.APPLICATION_FORM_URLENCODED);
		}
	}

	/**
	 * Returns the list of all query parameters
	 * 
	 * @return
	 */
	public List<NameValuePair> getQueryParameters() {

		List<NameValuePair> nps = new ArrayList<NameValuePair>();
		RequestPayload requestPayload = httpRequestWrapper.getRequestPayload();
		if (requestPayload instanceof UrlEncodedRequestPayload) {
			nps = ((UrlEncodedRequestPayload) requestPayload).getParameters();
		} else {
			logger.warn("Request payload is not of content-type: {}", ContentType.APPLICATION_FORM_URLENCODED);
		}

		return nps;

	}

	/**
	 * Adds a contentBody as a part data
	 * 
	 * @param name
	 * @param contentBody
	 */
	public void addPart(String name, ContentBody contentBody) {

		RequestPayload requestPayload = httpRequestWrapper.getRequestPayload();
		if (requestPayload instanceof MultipartEncodedRequestPayload) {
			((MultipartEncodedRequestPayload) requestPayload).addPart(name, contentBody);
			payloadChanged.set(true);
		} else {
			logger.warn("Request payload is not of content-type: {}", ContentType.MULTIPART_FORM_DATA);
		}
	}

	/**
	 * 
	 * 
	 */
	public void removePart(String name) {

		RequestPayload requestPayload = httpRequestWrapper.getRequestPayload();
		if (requestPayload instanceof MultipartEncodedRequestPayload) {
			((MultipartEncodedRequestPayload) requestPayload).removePart(name);
			payloadChanged.set(true);
		} else {
			logger.warn("Request payload is not of content-type: {}", ContentType.MULTIPART_FORM_DATA);
		}
	}

	/**
	 * Get all multi-part data
	 * 
	 */
	public Map<String, ContentBody> getAllParts() {

		Map<String, ContentBody> parts = new HashMap<String, ContentBody>();
		RequestPayload requestPayload = httpRequestWrapper.getRequestPayload();
		if (requestPayload instanceof MultipartEncodedRequestPayload) {
			parts = ((MultipartEncodedRequestPayload) requestPayload).getParts();
			payloadChanged.set(true);
		} else {
			logger.warn("Request payload is not of content-type: {}", ContentType.MULTIPART_FORM_DATA);
		}

		return parts;

	}

	public void execute() {

		try {
			responseReady.set(false);
			updateRequestPayload();

			HttpParams params = HttpClientUtil.createHttpParams(60 * 1000);
			DefaultHttpClient httpClient = (DefaultHttpClient) HttpClientFactory.createHttpClient(params);
			httpClient.addRequestInterceptor(new RequestAcceptEncoding());
			httpClient.addRequestInterceptor(new RequestAddCookies());
			httpClient.addResponseInterceptor(new ResponseContentEncoding());

			URI uri = getURI();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());

			// execute request
			HttpResponse httpResponse = httpClient.execute(target, httpRequestWrapper.getHttpRequest(), localContext);

			responseReady.set(true);
			updateResponse(httpResponse, localContext);

		} catch (ClientProtocolException e) {
			logger.error("Problem in client protocol. Reason: {}", e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Problem in client protocol. Reason: {}", e.getMessage(), e);
		}
	}

	public void clearResponse() {

		httpResponseWrapper = null;
		responseReady.set(false);
		payloadChanged.set(false);
	}

	private void updateResponse(HttpResponse httpResponse, HttpContext localContext) {

		if (!isResponseReady()) {
			logger.warn("Response is not ready to update.");
			return;
		}

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

	public URI getURI() {

		return httpRequestWrapper.getURI();
	}

	public String getReferer() {

		return referer;
	}

	public BasicCookieStore getCookieStore() {

		return cookieStore;
	}

	public void setCookieStore(BasicCookieStore cookieStore) {

		this.cookieStore = cookieStore;
	}

	public CookieOrigin getCookieOrigin(boolean secure) {

		return new CookieOrigin(getHost(), getPort(), getPath(), secure);

	}

	public String getResourceName() {

		return getURI().getPath().replaceFirst(".*/([^/]*)$", "$1");
	}

	public String getResourcePath() {

		String resourcePath = "";
		String path = getPath();
		if (path != null) {
			int lastSlash = path.lastIndexOf("/");
			if (lastSlash >= 0) {
				resourcePath = path.substring(0, lastSlash + 1);
			}
		} else {
			logger.warn("Null path for URI: {}", getURI());
		}

		return resourcePath;
	}

	public String getPath() {

		return getURI().getPath();
	}

	public int getPort() {

		return getURI().getPort();
	}

	public String getHost() {

		return getURI().getHost();
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

	public int getResponseStatusCode() {

		int statusCode = 0;
		if (httpResponseWrapper != null)
			statusCode = httpResponseWrapper.getStatusLine().getStatusCode();

		return statusCode;
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

	public Header[] getAllResponseHeaders() {

		Header[] headers = new Header[0];
		if (httpResponseWrapper != null) {
			headers = httpResponseWrapper.getHttpResponse().getAllHeaders();
		}
		return headers;
	}

	/**
	 * 
	 * @return All response headers formated as they would be found in the raw
	 *         HTTP response.
	 */
	public String getAllResponseHeadersAsString() {

		String headers = "";
		if (httpResponseWrapper != null) {
			for (Header header : getAllResponseHeaders()) {
				headers = headers.concat(header.toString()).concat("\r\n");
			}
			return headers;
		}
		return null;
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
