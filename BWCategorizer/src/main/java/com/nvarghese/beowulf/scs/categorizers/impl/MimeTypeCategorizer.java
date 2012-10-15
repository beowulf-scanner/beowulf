package com.nvarghese.beowulf.scs.categorizers.impl;

import java.util.HashSet;
import java.util.Set;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.MultiSetTransactionCategorizer;

public class MimeTypeCategorizer extends MultiSetTransactionCategorizer {

	public MimeTypeCategorizer(Datastore ds, WebScanDocument webScanDocument) {

		super(ds, webScanDocument, WebTestType.HTTP_MIME_TEST);
	}

	@Override
	protected Set<String> getTransactionTypeStrings(AbstractHttpTransaction transaction) {

		return new HashSet<String>();
	}

}
