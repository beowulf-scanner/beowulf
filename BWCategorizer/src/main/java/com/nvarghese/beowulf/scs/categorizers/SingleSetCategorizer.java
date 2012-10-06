package com.nvarghese.beowulf.scs.categorizers;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.webtest.WebTestType;

/**
 * 
 *  
 */
public abstract class SingleSetCategorizer extends Categorizer {

	protected WebTestType testType;
	protected Set<Integer> testModuleNumbers;

	static Logger logger = LoggerFactory.getLogger(SingleSetCategorizer.class);

	@Override
	public boolean hasModules() {

		return testModuleNumbers.size() > 0 ? true : false;
	}

	public SingleSetCategorizer(WebTestType testType) {

		testModuleNumbers = new HashSet<Integer>();
		this.testType = testType;
	}

}
