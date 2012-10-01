package com.nvarghese.beowulf.common.http.client;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.HttpParams;

public class BasicClientConnectionManagerFactory implements ClientConnectionManagerFactory {

	@Override
	public ClientConnectionManager newInstance(HttpParams params, SchemeRegistry scheme) {

		ClientConnectionManager connMrg = new BasicClientConnectionManager(scheme);
		return connMrg;
	}

}
