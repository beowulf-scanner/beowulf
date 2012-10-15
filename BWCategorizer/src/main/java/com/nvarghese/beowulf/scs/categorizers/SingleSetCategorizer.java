package com.nvarghese.beowulf.scs.categorizers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.common.webtest.dao.TestModuleMetaDataDAO;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;
import com.nvarghese.beowulf.scs.ScsManager;

/**
 * 
 *  
 */
public abstract class SingleSetCategorizer extends Categorizer {

	protected WebTestType testType;
	protected Set<Long> moduleNumbers;

	static Logger logger = LoggerFactory.getLogger(SingleSetCategorizer.class);

	public SingleSetCategorizer(Datastore ds, WebScanDocument webScanDocument, WebTestType testType) {

		super(ds, webScanDocument);
		this.testType = testType;
		moduleNumbers = new HashSet<Long>();
	}

	@Override
	public void initialize() {

		TestModuleMetaDataDAO testModuleMetaDataDAO = new TestModuleMetaDataDAO(ScsManager.getInstance().getDataStore());
		List<TestModuleMetaDataDocument> testModuleMetaDataDocs = testModuleMetaDataDAO.findByTestType(testType);

		for (TestModuleMetaDataDocument metaDoc : testModuleMetaDataDocs) {
			if (getEnabledTestModuleNumbers().contains(metaDoc.getModuleNumber())) {
				moduleNumbers.add(metaDoc.getModuleNumber());
			}
		}

	}

}
