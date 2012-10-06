package com.nvarghese.beowulf.scs.categorizers.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.TokenCategorizer;

public class HttpQueryParameterCategorizer extends TokenCategorizer {

	public HttpQueryParameterCategorizer() {

		super(WebTestType.HTTP_QUERY_PARAM_TEST);
	}

	@Override
	protected String[] getTokens(AbstractHttpTransaction transaction) {

		return null;
	}

}
