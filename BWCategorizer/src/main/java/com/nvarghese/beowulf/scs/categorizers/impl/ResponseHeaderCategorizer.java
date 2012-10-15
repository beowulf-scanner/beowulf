package com.nvarghese.beowulf.scs.categorizers.impl;

import java.util.HashSet;
import java.util.Set;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.MultiSetTransactionCategorizer;

public class ResponseHeaderCategorizer extends MultiSetTransactionCategorizer {

	public ResponseHeaderCategorizer(Datastore ds, WebScanDocument webScanDocument) {

		super(ds, webScanDocument, WebTestType.HTTP_RESPONSE_HEADER_TEST);
	}

	@Override
	protected Set<String> getTransactionTypeStrings(AbstractHttpTransaction transaction) {

		// TODO Auto-generated method stub
		return new HashSet<String>();
	}

}
