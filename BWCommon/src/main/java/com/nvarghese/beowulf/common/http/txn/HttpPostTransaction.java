package com.nvarghese.beowulf.common.http.txn;

import org.apache.http.client.methods.HttpPost;

import com.nvarghese.beowulf.common.http.payload.RequestPayloadFactory;
import com.nvarghese.beowulf.common.http.wrapper.HttpRequestWrapper;

public class HttpPostTransaction extends AbstractHttpTransaction {

	public HttpPostTransaction(HttpPost post, String referer) {

		super(new HttpRequestWrapper(HttpMethodType.GET, post, RequestPayloadFactory.createRequestPayload(post)), referer);

	}
}
