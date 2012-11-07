package com.nvarghese.beowulf.common.http.txn;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.NotImplementedException;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.client.HttpClientFactory;
import com.nvarghese.beowulf.common.http.client.HttpClientUtil;
import com.nvarghese.beowulf.common.http.payload.MultipartEncodedRequestPayload;
import com.nvarghese.beowulf.common.http.payload.RequestPayload;
import com.nvarghese.beowulf.common.http.payload.RequestPayloadUtils;
import com.nvarghese.beowulf.common.http.payload.UrlEncodedRequestPayload;
import com.nvarghese.beowulf.common.http.wrapper.HttpRequestWrapper;
import com.nvarghese.beowulf.common.http.wrapper.HttpResponseWrapper;

public abstract class AbstractHttpTransaction {

	private ObjectId objId;
	private ObjectId refererTxnObjId;

	private TransactionSource transactionSource;

	private HttpRequestWrapper httpRequestWrapper;
	private HttpResponseWrapper httpResponseWrapper;
	private BasicCookieStore cookieStore;

	private AtomicBoolean responseReady;
	private AtomicBoolean uncompressed;

	private String referer;

	protected AtomicBoolean payloadChanged;
	protected AtomicBoolean saved;

	/* logger */
	static Logger logger = LoggerFactory.getLogger(AbstractHttpTransaction.class);

	public AbstractHttpTransaction() {

		super();
		payloadChanged = new AtomicBoolean(false);
		responseReady = new AtomicBoolean(false);
		uncompressed = new AtomicBoolean(true);
		saved = new AtomicBoolean(false);
		transactionSource = TransactionSource.NONE;
	}

	public AbstractHttpTransaction(HttpRequestWrapper requestWrapper, String referer, TransactionSource transactionSource) {

		this();
		this.cookieStore = new BasicCookieStore();
		this.httpRequestWrapper = requestWrapper;
		this.referer = referer;
		this.transactionSource = transactionSource;

	}

	@SuppressWarnings("unchecked")
	public HttpTxnDocument toHttpTxnDocument() {

		updateRequestPayload();

		HttpTxnDocument txnDocument = new HttpTxnDocument();

		txnDocument.setTransactionSource(transactionSource);
		txnDocument.setRequestMethod(httpRequestWrapper.getMethod());
		txnDocument.setRequestURI(getURI().toString());
		txnDocument.setRequestHeaders((List<BasicHeader>) httpRequestWrapper.getHeaders());
		txnDocument.setRequestPayload(RequestPayloadUtils.serialize(httpRequestWrapper.getRequestPayload()));
		txnDocument.setPayloadChanged(payloadChanged.get());
		txnDocument.setCookieStore(cookieStore);
		txnDocument.setRefererTxnObjId(refererTxnObjId);
		txnDocument.setReferer(referer);
		txnDocument.setResponseReady(responseReady.get());
		txnDocument.setUncompressed(uncompressed.get());
		if (responseReady.get()) {
			txnDocument.setResponseStatusLine((BasicStatusLine) httpResponseWrapper.getStatusLine());
			txnDocument.setResponseHeaders((List<BasicHeader>) httpResponseWrapper.getHeaders());
			txnDocument.setResponseBody(httpResponseWrapper.getResponseBody());
		}

		if (saved.get()) {
			txnDocument.setId(getObjId());
		}

		return txnDocument;

	}

	public static AbstractHttpTransaction getObject(HttpTxnDocument httpTxnDocument) {

		AbstractHttpTransaction httpTxn = null;
		try {
			RequestPayload requestPayload = RequestPayloadUtils.deserialize(httpTxnDocument.getRequestPayload());
			HttpRequest httpRequest = HttpTransactionFactory.createHttpRequest(httpTxnDocument.getRequestMethod(),
					new URI(httpTxnDocument.getRequestURI()), httpTxnDocument.getRequestHeaders().toArray(new BasicHeader[0]),
					requestPayload.toHttpEntity());

			httpTxn = HttpTransactionFactory.createTransaction(httpRequest, httpTxnDocument.getReferer(), httpTxnDocument.getTransactionSource());
			httpTxn.setCookieStore(httpTxnDocument.getCookieStore());
			httpTxn.setObjId(httpTxnDocument.getId());
			httpTxn.setRefererTxnObjId(httpTxnDocument.getRefererTxnObjId());

			httpTxn.payloadChanged.set(httpTxnDocument.isPayloadChanged());
			if (httpTxnDocument.isResponseReady()) {
				httpTxn.httpResponseWrapper = new HttpResponseWrapper(httpTxnDocument.getResponseStatusLine(), httpTxnDocument.getResponseHeaders(),
						httpTxnDocument.getResponseBody());
				httpTxn.responseReady.set(true);
			} else {
				httpTxn.responseReady.set(false);
			}

			httpTxn.uncompressed.set(httpTxnDocument.isUncompressed());
			httpTxn.setSaved(true);

		} catch (URISyntaxException e) {
			logger.error("Failed to retrieve the object. Reason: {}", e.getMessage(), e);
		} catch (NotImplementedException e) {
			logger.error("Failed to retrieve the object. Reason: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Failed to retrieve the object. Reason: {}", e.getMessage(), e);
		}
		return httpTxn;
	}

	public static AbstractHttpTransaction clone(HttpTxnDocument httpTxnDocument) {

		AbstractHttpTransaction httpTxn = null;
		try {
			RequestPayload requestPayload = RequestPayloadUtils.deserialize(httpTxnDocument.getRequestPayload());
			HttpRequest httpRequest = HttpTransactionFactory.createHttpRequest(httpTxnDocument.getRequestMethod(),
					new URI(httpTxnDocument.getRequestURI()), httpTxnDocument.getRequestHeaders().toArray(new BasicHeader[0]),
					requestPayload.toHttpEntity());

			httpTxn = HttpTransactionFactory.createTransaction(httpRequest, httpTxnDocument.getReferer(), httpTxnDocument.getTransactionSource());
			httpTxn.setCookieStore(httpTxnDocument.getCookieStore());
			httpTxn.setRefererTxnObjId(httpTxnDocument.getRefererTxnObjId());

			httpTxn.payloadChanged.set(httpTxnDocument.isPayloadChanged());
			httpTxn.responseReady.set(false);
			httpTxn.uncompressed.set(false);
			httpTxn.setSaved(false);

		} catch (URISyntaxException e) {
			logger.error("Failed to retrieve the object. Reason: {}", e.getMessage(), e);
		} catch (NotImplementedException e) {
			logger.error("Failed to retrieve the object. Reason: {}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Failed to retrieve the object. Reason: {}", e.getMessage(), e);
		}
		return httpTxn;

	}

	/**
	 * 
	 * @return
	 */
	public ObjectId getObjId() {

		return objId;
	}

	/**
	 * 
	 * @param objId
	 */
	public void setObjId(ObjectId objId) {

		this.objId = objId;
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

	public String getQueryParamater(String name) {

		RequestPayload requestPayload = httpRequestWrapper.getRequestPayload();
		if (requestPayload instanceof UrlEncodedRequestPayload) {
			String value = ((UrlEncodedRequestPayload) requestPayload).getParameterValue(name);
			return value;
		} else {
			logger.warn("Request payload is not of content-type: {}", ContentType.APPLICATION_FORM_URLENCODED);
			return null;
		}

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
			payloadChanged.set(false);
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

	public ObjectId getRefererTxnObjId() {

		return refererTxnObjId;
	}

	public void setRefererTxnObjId(ObjectId refererTxnObjId) {

		this.refererTxnObjId = refererTxnObjId;
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

	public String getScheme() {

		return getURI().getScheme();
	}

	public String getHostUriWithoutTrailingSlash() {

		String base = getScheme() + "://" + getHost();
		int port = getPort();
		if (port > 0) {
			base += ":" + port;
		}
		return base;
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

	public boolean isSaved() {

		return saved.get();
	}

	public void setSaved(boolean value) {

		saved.set(value);
	}

	public TransactionSource getTransactionSource() {

		return transactionSource;
	}

	public void setTransactionSource(TransactionSource transactionSource) {

		this.transactionSource = transactionSource;
	}

	/**
	 * 
	 * @return
	 */
	public String requestToString() {

		StringBuilder requestString = new StringBuilder();
		requestString.append(httpRequestWrapper.getHttpRequest().getRequestLine().toString()).append("\n");
		for (Header header : httpRequestWrapper.getHttpRequest().getAllHeaders()) {
			requestString.append(header.toString()).append("\n");
		}
		requestString.append("\n");

		return requestString.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String responseToString() {

		StringBuilder responseString = new StringBuilder();
		if (httpResponseWrapper != null) {
			responseString.append(httpResponseWrapper.getStatusLine().toString() + "\r\n");
			for (Header header : httpResponseWrapper.getHeaders()) {
				responseString.append(header.toString()).append("\n");
			}
			responseString.append("\r\n");
			responseString.append(new String(httpResponseWrapper.getResponseBody()));

		}

		return responseString.toString();
	}

}
