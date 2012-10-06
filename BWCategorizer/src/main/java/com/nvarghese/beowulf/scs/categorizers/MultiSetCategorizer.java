package com.nvarghese.beowulf.scs.categorizers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nvarghese.beowulf.common.webtest.WebTestType;

public abstract class MultiSetCategorizer extends Categorizer {

	private WebTestType webTestType;
	protected Map<Object, HashSet<Integer>> modulesByType;

	Logger logger = Logger.getLogger(MultiSetCategorizer.class);

	@Override
	public boolean hasModules() {

		return modulesByType.size() > 0 ? true : false;
	}

	public MultiSetCategorizer(WebTestType webTestType) {

		super();
		modulesByType = Collections.synchronizedMap(new HashMap<Object, HashSet<Integer>>());
		this.webTestType = webTestType;
	}

}
