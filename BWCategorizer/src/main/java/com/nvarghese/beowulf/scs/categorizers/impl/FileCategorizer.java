package com.nvarghese.beowulf.scs.categorizers.impl;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.TokenCategorizer;

/**
 * 
 *  
 */
public class FileCategorizer extends TokenCategorizer {

	public FileCategorizer() {

		super(WebTestType.FILE_TEST);
	}

	@Override
	protected String[] getTokens(AbstractHttpTransaction transaction) {

		return null;

	}

}
