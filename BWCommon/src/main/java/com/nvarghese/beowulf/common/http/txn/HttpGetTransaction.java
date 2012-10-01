package com.nvarghese.beowulf.common.http.txn;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;

import com.nvarghese.beowulf.common.http.payload.UrlEncodedRequestPayload;
import com.nvarghese.beowulf.common.http.wrapper.HttpRequestWrapper;

public class HttpGetTransaction extends AbstractHttpTransaction {

	public HttpGetTransaction(HttpGet get, String referer) {

		super(new HttpRequestWrapper(HttpMethodType.GET, get, new UrlEncodedRequestPayload(URLEncodedUtils.parse(get.getURI(), "ISO-8559-1"))),
				referer);

	}

}
