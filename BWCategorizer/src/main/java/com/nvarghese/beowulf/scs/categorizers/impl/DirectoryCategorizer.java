package com.nvarghese.beowulf.scs.categorizers.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.TokenCategorizer;

public class DirectoryCategorizer extends TokenCategorizer {

	private Set<String> testedDirs;

	static Logger logger = LoggerFactory.getLogger(DirectoryCategorizer.class);

	public DirectoryCategorizer() {

		super(WebTestType.DIRECTORY_TEST);
		testedDirs = new HashSet<String>();
	}

	protected String[] getTokens(AbstractHttpTransaction transaction) {

		return null;

	}

}
