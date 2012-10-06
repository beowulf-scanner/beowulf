package com.nvarghese.beowulf.scs.categorizers.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.MultiSetTransactionCategorizer;

public class HtmlElementCategorizer extends MultiSetTransactionCategorizer {

	private Set<String> elementHashes;

	public HtmlElementCategorizer() {

		super(WebTestType.HTML_ELEM_TEST);
		elementHashes = Collections.synchronizedSet(new HashSet<String>(500));
	}

	@Override
	public void analyzeTransaction(AbstractHttpTransaction transaction) {

	}

	@Override
	protected Set<String> getTransactionTypeStrings(AbstractHttpTransaction transaction) {

		// TODO Auto-generated method stub
		return null;
	}

}
