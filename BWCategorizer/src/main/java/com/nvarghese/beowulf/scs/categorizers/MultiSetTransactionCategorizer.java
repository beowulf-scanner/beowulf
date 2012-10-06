package com.nvarghese.beowulf.scs.categorizers;

import java.util.Set;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.webtest.WebTestType;

/**
 * The type strings are not case-sensitive.
 * 
 *  
 * 
 */
public abstract class MultiSetTransactionCategorizer extends MultiSetCategorizer implements TransactionCategorizer {

	public MultiSetTransactionCategorizer(WebTestType webTestType) {

		super(webTestType);
	}

	public void analyzeTransaction(AbstractHttpTransaction transaction) {

	}

	protected abstract Set<String> getTransactionTypeStrings(AbstractHttpTransaction transaction);

	//protected abstract TestJob makeTestJob(AbstractHttpTransaction transaction, TestModule module, String type);

}
