package com.nvarghese.beowulf.common.http.client;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {

	/**
	 * Creates HttpClient with customized schemes
	 * 
	 * @param params
	 * @param schemeRegistry
	 * @return
	 */
	public static HttpClient createHttpClient(HttpParams params, SchemeRegistry schemeRegistry) {

		BasicClientConnectionManagerFactory managerConnectionManagerFactory = new BasicClientConnectionManagerFactory();
		ClientConnectionManager connectionManager = managerConnectionManagerFactory.newInstance(params, schemeRegistry);

		return new DefaultHttpClient(connectionManager, params);
	}

	/**
	 * Creates HttpClient with default schemes (http -> tcp/80 & https ->
	 * tcp/443)
	 * 
	 * @param params
	 * @return
	 */
	public static HttpClient createHttpClient(HttpParams params) {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

		return createHttpClient(params, schemeRegistry);
	}

}
