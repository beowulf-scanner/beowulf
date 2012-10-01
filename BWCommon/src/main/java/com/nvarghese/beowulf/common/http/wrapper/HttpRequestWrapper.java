package com.nvarghese.beowulf.common.http.wrapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.payload.RequestPayload;
import com.nvarghese.beowulf.common.http.payload.UrlEncodedRequestPayload;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;

public class HttpRequestWrapper extends HttpMessageWrapper {

	private HttpMethodType method;

	private HttpRequest httpRequest;

	private RequestPayload requestPayload;

	static Logger logger = LoggerFactory.getLogger(HttpRequestWrapper.class);

	public HttpRequestWrapper(HttpMethodType method, HttpRequest httpRequest, RequestPayload requestPayload) {

		super();
		this.method = method;
		this.httpRequest = httpRequest;
		this.requestPayload = requestPayload;
	}

	public URI getURI() {

		HttpUriRequest uriRequest = (HttpUriRequest) httpRequest;
		return uriRequest.getURI();
	}

	public HttpMethodType getMethod() {

		return method;
	}

	public HttpRequest getHttpRequest() {

		return httpRequest;
	}

	public RequestPayload getRequestPayload() {

		return requestPayload;
	}

	public void processUpdates() {

		List<Header> headersList = Arrays.asList(httpRequest.getAllHeaders());
		URI uri = getUpdatedURI();
		if (requestPayload.getContentType() != null) {
			Header contentTypeHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE, requestPayload.getContentType().getMimeType());
			headersList.add(contentTypeHeader);
		}
		HttpRequest newHttpRequest = HttpTransactionFactory.createHttpRequest(method, uri, headersList.toArray(new Header[0]),
				requestPayload.toHttpEntity());
		this.httpRequest = newHttpRequest;

	}

	private URI getUpdatedURI() {

		HttpUriRequest uriRequest = (HttpUriRequest) httpRequest;
		URI uri = uriRequest.getURI();
		try {
			URIBuilder uriBuilder = new URIBuilder(uri);
			if ((httpRequest instanceof HttpEntityEnclosingRequestBase) == false) {
				/* an idempotent request */
				if (requestPayload instanceof UrlEncodedRequestPayload) {
					List<NameValuePair> params = ((UrlEncodedRequestPayload) requestPayload).getParameters();
					for (NameValuePair param : params) {
						uriBuilder.setParameter(param.getName(), param.getValue());
					}

				}

				uri = uriBuilder.build();

			}
		} catch (URISyntaxException e) {
			logger.error("Failed to retrieve the new URI from payload. Reason: {}", e.getMessage(), e);
			logger.warn("Reverted the http request due to problem in URI building");
			uri = uriRequest.getURI();
		}

		return uri;
	}

}
