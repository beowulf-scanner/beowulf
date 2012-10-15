package com.nvarghese.beowulf.scs.categorizers.impl;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.TokenSingleSetTransactionCategorizer;

public class SetCookieCategorizer extends TokenSingleSetTransactionCategorizer {

	public SetCookieCategorizer(Datastore ds, WebScanDocument webScanDocument) {

		super(ds, webScanDocument, WebTestType.HTTP_SET_COOKIE_HEADER_TEST);
	}

	@Override
	protected String[] getTokens(AbstractHttpTransaction transaction) {

		return new String[] {};
	}

}
