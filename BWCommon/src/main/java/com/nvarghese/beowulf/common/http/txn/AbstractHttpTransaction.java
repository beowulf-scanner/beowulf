package com.nvarghese.beowulf.common.http.txn;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.DefaultedHttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHttpTransaction {

	private HttpRequest httpRequest;
	private HttpResponse httpResponse;

	static transient Logger logger = LoggerFactory.getLogger(AbstractHttpTransaction.class);

	public void execute() {

		try {
			HttpClient httpClient = new DefaultHttpClient();
			URI uri = ((HttpUriRequest) httpRequest).getURI();
			HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
			httpResponse = httpClient.execute(target, httpRequest);
		} catch (ClientProtocolException e) {
			
		} catch (IOException e) {
			
		}

	}

}
