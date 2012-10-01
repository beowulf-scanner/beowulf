package com.nvarghese.beowulf.common.http.txn;

import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpTransactionFactory {

	/* logger */
	static Logger logger = LoggerFactory.getLogger(HttpTransactionFactory.class);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static HttpGet convertToHttpGet(HttpRequest request) {

		HttpGet get = null;
		get = new HttpGet(request.getRequestLine().getUri());
		get.setHeaders(request.getAllHeaders());
		get.setParams(request.getParams());
		return get;
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public static HttpPost convertToHttpPost(BasicHttpEntityEnclosingRequest request) {

		HttpPost post = null;
		post = new HttpPost(request.getRequestLine().getUri());
		post.setHeaders(request.getAllHeaders());
		post.setEntity(request.getEntity());
		post.setParams(request.getParams());
		return post;
	}

	/**
	 * Creates http transaction object
	 * 
	 * @param request
	 * @param referer
	 * @return
	 */
	public static AbstractHttpTransaction createTransaction(HttpRequest request, String referer) {

		AbstractHttpTransaction transaction = null;
		if (request.getRequestLine().getMethod().equals(HttpMethodType.GET.getValue())) {
			HttpGet get = null;
			if (request instanceof BasicHttpRequest) {
				get = convertToHttpGet(request);
			} else if (request instanceof HttpGet) {
				get = (HttpGet) request;
			}

			transaction = new HttpGetTransaction(get, referer);
		} else if (request.getRequestLine().getMethod().equals(HttpMethodType.POST.getValue())) {
			HttpPost post = null;
			if (request instanceof BasicHttpEntityEnclosingRequest) {
				post = convertToHttpPost((BasicHttpEntityEnclosingRequest) request);
			} else if (request instanceof HttpPost) {
				post = (HttpPost) request;
			}

			transaction = new HttpPostTransaction(post, referer);
		}
		return transaction;
	}

	/**
	 * 
	 * @param method
	 * @param uri
	 * @param entity
	 * @param referer
	 * @return
	 */
	public static AbstractHttpTransaction createTransaction(HttpMethodType method, URI uri, HttpEntity entity, String referer) {

		return createTransaction(createHttpRequest(method, uri, null, entity), referer);
	}

	/**
	 * Create http request object
	 * 
	 * @param method
	 * @param uri
	 * @param headers
	 * @param entity
	 * @return
	 */
	public static HttpRequest createHttpRequest(HttpMethodType method, URI uri, Header[] headers, HttpEntity entity) {

		HttpRequest request = null;

		if (method == HttpMethodType.GET) {
			HttpGet get = new HttpGet();
			get.setURI(uri);
			request = get;
		} else if (method == HttpMethodType.TRACE) {
			HttpTrace trace = new HttpTrace();
			trace.setURI(uri);
			request = trace;
		} else if (method == HttpMethodType.OPTIONS) {
			HttpOptions options = new HttpOptions();
			options.setURI(uri);
			request = options;
		} else if (method == HttpMethodType.HEAD) {
			HttpHead head = new HttpHead();
			head.setURI(uri);
			request = head;
		} else if (method == HttpMethodType.POST) {
			HttpPost post = new HttpPost();
			post.setURI(uri);
			post.setEntity(entity);
			request = post;
		} else if (method == HttpMethodType.PUT) {
			HttpPut put = new HttpPut();
			put.setURI(uri);
			put.setEntity(entity);
			request = put;
		}

		if (request != null) {
			if (headers != null) {
				request.setHeaders(headers);
			}

		}

		return request;

	}

}
