package com.nvarghese.beowulf.scs.categorizers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.webtest.WebTestType;


public abstract class TokenSingleSetTransactionCategorizer extends SingleSetCategorizer implements TransactionCategorizer {

	private Set<String> tokens;

	public TokenSingleSetTransactionCategorizer(WebTestType webTestType) {

		super(webTestType);
		tokens = Collections.synchronizedSet(new HashSet<String>());
		
	}

	public void analyzeTransaction(AbstractHttpTransaction transaction) {
		
	}

	protected abstract String[] getTokens(AbstractHttpTransaction transaction);

	//protected abstract Set<TestJob> makeTestJobs(AbstractHttpTransaction transaction, TestModule module, String token);
}
