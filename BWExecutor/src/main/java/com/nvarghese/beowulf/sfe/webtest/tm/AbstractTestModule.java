package com.nvarghese.beowulf.sfe.webtest.tm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.scan.dto.config.Options;
import com.nvarghese.beowulf.common.scan.model.TestModuleScanConfigDocument;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;
import com.nvarghese.beowulf.common.webtest.model.TestModuleOptionDocument;

public abstract class AbstractTestModule {

	// module related properties
	protected long moduleNumber;
	protected String moduleName;
	protected boolean moduleEnabled;
	protected Map<String, Options> optionsMap = new HashMap<String, Options>();

	// current test related properties
	protected Datastore scanInstanceDataStore;
	protected ObjectId webScanObjId;

	static Logger logger = LoggerFactory.getLogger(AbstractTestModule.class);

	public AbstractTestModule() {

	}

	public void initialize(Datastore scanInstanceDataStore, TestModuleMetaDataDocument testModuleMetaDocument,
			TestModuleScanConfigDocument testModuleScanConfigDocument) {

		this.scanInstanceDataStore = scanInstanceDataStore;
		List<TestModuleOptionDocument> metaOptionDocuments = testModuleMetaDocument.getOptions();
		for (TestModuleOptionDocument metaOptionDocument : metaOptionDocuments) {
			Options option = new Options();
			option.setOptionName(metaOptionDocument.getOptionName());
			option.setOptionValue(metaOptionDocument.getOptionValue());
			// implement type checks
			optionsMap.put(metaOptionDocument.getOptionName(), option);
		}

		List<TestModuleOptionDocument> moduleOptionDocuments = testModuleScanConfigDocument.getOptions();

		for (TestModuleOptionDocument optionDocument : moduleOptionDocuments) {
			if (optionsMap.containsKey(optionDocument.getOptionName())) {
				Options option = new Options();
				option.setOptionName(optionDocument.getOptionName());
				option.setOptionValue(optionDocument.getOptionValue());
				// implement type checks
				optionsMap.put(optionDocument.getOptionName(), option);
			}
		}

	}

	public long getModuleNumber() {

		return moduleNumber;

	}

	public String getModuleName() {

		return moduleName;
	}

	public boolean isModuleEnabled() {

		return moduleEnabled;
	}

}
