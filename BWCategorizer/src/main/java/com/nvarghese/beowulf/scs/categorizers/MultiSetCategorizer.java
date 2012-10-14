package com.nvarghese.beowulf.scs.categorizers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.common.webtest.dao.TestModuleMetaDataDAO;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;
import com.nvarghese.beowulf.scs.ScsManager;

public abstract class MultiSetCategorizer extends Categorizer {

	protected WebTestType webTestType;
	protected Map<String, HashSet<Long>> modulesNumbersByType;

	Logger logger = Logger.getLogger(MultiSetCategorizer.class);

	public MultiSetCategorizer(Datastore ds, WebScanDocument webScanDocument, WebTestType webTestType) {

		super(ds, webScanDocument);
		this.webTestType = webTestType;
		this.modulesNumbersByType = new HashMap<String, HashSet<Long>>();
	}

	@Override
	public void initialize() {

		TestModuleMetaDataDAO testModuleMetaDataDAO = new TestModuleMetaDataDAO(ScsManager.getInstance().getDataStore());
		List<TestModuleMetaDataDocument> testModuleMetaDataDocs = testModuleMetaDataDAO.findByTestType(webTestType);

		for (TestModuleMetaDataDocument metaDoc : testModuleMetaDataDocs) {
			if (getEnabledTestModuleNumbers().contains(metaDoc.getModuleNumber())) {

				for (String attr : metaDoc.getTestAttributes()) {
					if (modulesNumbersByType.containsKey(attr)) {
						modulesNumbersByType.get(attr).add(metaDoc.getModuleNumber());
					} else {
						HashSet<Long> moduleNumbers = new HashSet<Long>();
						moduleNumbers.add(metaDoc.getModuleNumber());
						modulesNumbersByType.put(attr, moduleNumbers);
					}
				}

			}
		}

	}

}
