package com.nvarghese.beowulf.smf;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.scan.dto.metatest.MetaTestModule;
import com.nvarghese.beowulf.common.scan.dto.metatest.MetaTestModules;
import com.nvarghese.beowulf.common.scan.dto.metatest.Options;
import com.nvarghese.beowulf.common.scan.dto.report.WascThreatType;
import com.nvarghese.beowulf.common.scan.dto.report.WascThreatTypes;
import com.nvarghese.beowulf.common.utils.XmlUtils;
import com.nvarghese.beowulf.common.webtest.dao.ReportThreatTypeDAO;
import com.nvarghese.beowulf.common.webtest.dao.TestModuleMetaDataDAO;
import com.nvarghese.beowulf.common.webtest.model.ReportThreatTypeDocument;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;
import com.nvarghese.beowulf.common.webtest.model.TestModuleOptionDocument;

public class SmfDataLoader implements ServletContextListener {

	static Logger logger = LoggerFactory.getLogger(SmfDataLoader.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		logger.info("Checking whether test module list is loaded to database.");
		ServletContext ctx = event.getServletContext();
		String testModuleListFileName = ctx.getInitParameter("test-module-list-filename");
		if (testModuleListFileName != null && !testModuleListFileName.isEmpty()) {
			loadMetaTestModules(testModuleListFileName);
		} else {
			loadMetaTestModules();
		}

		String wascThreatTypesFileName = ctx.getInitParameter("wasc-threat-types-filename");
		if (wascThreatTypesFileName != null && !wascThreatTypesFileName.isEmpty()) {
			loadWascThreatTypes(wascThreatTypesFileName);
		} else {
			loadWascThreatTypes();
		}

	}

	private void loadWascThreatTypes() {

		loadWascThreatTypes("wasc_threat_types.xml");

	}

	private void loadWascThreatTypes(String filename) {

		URL url = SmfDataLoader.class.getClassLoader().getResource(filename);

		try {
			String wascThreatTypesContent = "";
			// the xml is not in jar
			if (url != null && !url.toString().startsWith("jar")) {
				logger.info("The resolved URL: {}", url.toString());
				wascThreatTypesContent = FileUtils.readFileToString(new File(url.toURI()));
			} else {
				wascThreatTypesContent = FileUtils.readFileToString(new File(SmfManager.getInstance().getSettings().getDefaultConfDir(), filename));
			}

			WascThreatTypes wascThreatTypes = XmlUtils.xmlStringToPojo(wascThreatTypesContent, WascThreatTypes.class);
			for (WascThreatType wascThreatType : wascThreatTypes.getWascThreatType()) {
				persistWascThreatType(wascThreatType);
			}

			logger.info("WASC Threat types are loaded to the database");

		} catch (IOException e) {
			logger.error("Failed to load wasc types. Reason: {}", e.getMessage(), e);
		} catch (JAXBException e) {
			logger.error("Failed to load wasc types. Reason: {}", e.getMessage(), e);
		} catch (URISyntaxException e) {
			logger.error("Failed to load wasc types. Reason: {}", e.getMessage(), e);
		}

	}

	private void loadMetaTestModules() {

		loadMetaTestModules("test_modules.xml");
	}

	private void loadMetaTestModules(String filename) {

		URL url = SmfDataLoader.class.getClassLoader().getResource(filename);

		try {
			String testModuleListContent = "";
			// the test_modules.xml is not in jar
			if (url != null && !url.toString().startsWith("jar")) {
				logger.info("The resolved URL: {}", url.toString());
				testModuleListContent = FileUtils.readFileToString(new File(url.toURI()));
			} else {
				testModuleListContent = FileUtils.readFileToString(new File(SmfManager.getInstance().getSettings().getDefaultConfDir(), filename));
			}

			MetaTestModules metaTestModules = XmlUtils.xmlStringToPojo(testModuleListContent, MetaTestModules.class);
			for (MetaTestModule metaTestModule : metaTestModules.getMetaTestModule()) {
				persistTestModule(metaTestModule);
			}

			logger.info("Test module list is loaded to the database");

		} catch (IOException e) {
			logger.error("Failed to load test modules. Reason: {}", e.getMessage(), e);
		} catch (JAXBException e) {
			logger.error("Failed to load test modules. Reason: {}", e.getMessage(), e);
		} catch (URISyntaxException e) {
			logger.error("Failed to load test modules. Reason: {}", e.getMessage(), e);
		}

	}

	private void persistWascThreatType(WascThreatType wascThreatType) {

		Datastore ds = SmfManager.getInstance().getDataStore();

		ReportThreatTypeDAO threatTypeDAO = new ReportThreatTypeDAO(ds);
		ReportThreatTypeDocument threatTypeDocument = threatTypeDAO.findByThreatId(wascThreatType.getThreatTypeId());
		boolean isNew = false;
		if (threatTypeDocument == null) {
			threatTypeDocument = new ReportThreatTypeDocument();
			threatTypeDocument.setThreatTypeId(wascThreatType.getThreatTypeId());
			threatTypeDocument.setWascThreatClass(wascThreatType.getThreatClassName());
			threatTypeDocument.setWascThreatSubClass(wascThreatType.getThreatSubClassName());
			
			isNew = true;
		}

		if (isNew) {
			threatTypeDAO.createReportThreatTypeDocument(threatTypeDocument);
		} else {
			threatTypeDAO.updateReportThreatTypeDocument(threatTypeDocument);
		}

	}

	private void persistTestModule(MetaTestModule metaTestModule) {

		Datastore ds = SmfManager.getInstance().getDataStore();

		TestModuleMetaDataDAO tmMetaDocumentDAO = new TestModuleMetaDataDAO(ds);
		TestModuleMetaDataDocument testModuleMetaDataDocument = tmMetaDocumentDAO.findByModuleNumber(metaTestModule.getModuleNumber().longValue());
		boolean isNew = false;
		if (testModuleMetaDataDocument == null) {
			// create new document
			testModuleMetaDataDocument = new TestModuleMetaDataDocument();
			testModuleMetaDataDocument.setModuleNumber(metaTestModule.getModuleNumber().longValue());
			isNew = true;
		}

		testModuleMetaDataDocument.setEnabled(metaTestModule.isEnabled());
		testModuleMetaDataDocument.setDescription(metaTestModule.getModuleDescription());
		testModuleMetaDataDocument.setModuleName(metaTestModule.getModuleName());
		testModuleMetaDataDocument.setModuleClassName(metaTestModule.getModuleClassName());
		testModuleMetaDataDocument.setTestAttributes(metaTestModule.getTestAttributes().getTestAttribute());
		testModuleMetaDataDocument.setTestCategory(metaTestModule.getWebTestCategory());
		testModuleMetaDataDocument.setTestType(metaTestModule.getWebTestType());

		List<TestModuleOptionDocument> optionDocuments = new ArrayList<TestModuleOptionDocument>();
		for (Options option : metaTestModule.getOptions()) {
			TestModuleOptionDocument optionDocument = new TestModuleOptionDocument();
			optionDocument.setOptionGroup(option.getGroup());
			optionDocument.setOptionName(option.getOptionName());
			optionDocument.setOptionType(option.getType());
			optionDocument.setOptionValue(option.getOptionValue());

			optionDocuments.add(optionDocument);
		}

		testModuleMetaDataDocument.setOptions(optionDocuments);

		if (isNew) {
			tmMetaDocumentDAO.createTestModuleMetaDataDocument(testModuleMetaDataDocument);
		} else {
			tmMetaDocumentDAO.updateTestModuleMetaDataDocument(testModuleMetaDataDocument);
		}
	}

}
