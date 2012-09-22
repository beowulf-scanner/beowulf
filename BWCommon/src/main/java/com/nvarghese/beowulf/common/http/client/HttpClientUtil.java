package com.nvarghese.beowulf.common.http.client;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpClientUtil {
	
	/* logger */
	static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	/**
	 * Creates the customized set of http params
	 * 
	 * @param maxTotalConnections
	 * @param socketTimeOutValue
	 * @return
	 */
	public static HttpParams createHttpParams(int socketTimeOutValue) {

		HttpParams params = new BasicHttpParams();
		params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeOutValue);
		return params;
	}
}
