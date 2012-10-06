package com.nvarghese.beowulf.scs.categorizers.impl;

import java.util.Set;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.MultiSetTransactionCategorizer;

public class ResponseHeaderCategorizer extends MultiSetTransactionCategorizer {

	public ResponseHeaderCategorizer() {

		super(WebTestType.HTTP_RESPONSE_HEADER_TEST);
	}

	@Override
	protected Set<String> getTransactionTypeStrings(AbstractHttpTransaction transaction) {

		// TODO Auto-generated method stub
		return null;
	}

}
