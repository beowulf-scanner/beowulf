package com.nvarghese.beowulf.scs.categorizers.impl;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.TokenSingleSetTransactionCategorizer;
import com.nvarghese.beowulf.scs.categorizers.dao.HostCategorizerDAO;
import com.nvarghese.beowulf.scs.categorizers.model.HostCategorizerDocument;

/**
 * 
 *  
 */
public class HostCategorizer extends TokenSingleSetTransactionCategorizer {

	private HostCategorizerDAO hostCategorizerDAO;

	public HostCategorizer(Datastore ds, WebScanDocument webScanDocument) {

		super(ds, webScanDocument, WebTestType.HOST_TEST);
		if (ds != null) {
			hostCategorizerDAO = new HostCategorizerDAO(ds);
			if (hostCategorizerDAO.getHostCategorizerDocument() == null)
				hostCategorizerDAO.createHostCategorizerDocument(new HostCategorizerDocument());
		}
	}

	@Override
	protected String[] getTokens(AbstractHttpTransaction transaction) {

		String hostName = transaction.getHost();
		if (!hostCategorizerDAO.isHostNamePresent(hostName)) {
			hostCategorizerDAO.addHostName(hostName);
			return new String[] { hostName };
		} else {
			return new String[] {};
		}

	}

}
