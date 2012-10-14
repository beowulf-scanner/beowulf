package com.nvarghese.beowulf.scs.categorizers.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.MultiSetCategorizer;
import com.nvarghese.beowulf.scs.categorizers.TransactionCategorizer;

public class OutputContextCategorizer extends MultiSetCategorizer implements TransactionCategorizer {

	private Pattern tokenPattern;

	public OutputContextCategorizer(Datastore ds, WebScanDocument webScanDocument) {

		super(ds, webScanDocument, WebTestType.OUTPUT_CONTEXT_TEST);

	}

	protected Set<String> getTestTokens(AbstractHttpTransaction transaction) {

		Set<String> tokens = new HashSet<String>();
		String body = transaction.getResponseBodyAsString();
		Matcher m = tokenPattern.matcher(body);
		while (m.find()) {
			tokens.add(m.group(1));
		}
		return tokens;
	}

	public void analyzeTransaction(AbstractHttpTransaction transaction) {

	}

}
