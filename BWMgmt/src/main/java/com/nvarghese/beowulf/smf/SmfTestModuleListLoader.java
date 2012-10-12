package com.nvarghese.beowulf.smf;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.scan.dto.config.TestModule;
import com.nvarghese.beowulf.common.scan.dto.config.TestModules;
import com.nvarghese.beowulf.common.utils.XmlUtils;
import com.nvarghese.beowulf.common.webtest.dao.TestModuleMetaDataDAO;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;

public class SmfTestModuleListLoader implements ServletContextListener {

	static Logger logger = LoggerFactory.getLogger(SmfTestModuleListLoader.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		logger.info("Checking whether test module list is loaded to database.");
		ServletContext ctx = event.getServletContext();
		String testModuleListFileName = ctx.getInitParameter("test-module-list-filename");
		if (testModuleListFileName != null && !testModuleListFileName.isEmpty()) {
			initialize(testModuleListFileName);
		} else {
			initialize();
		}
	}

	private void initialize() {

		initialize("test_modules.xml");
	}

	private void initialize(String filename) {

		URL url = SmfQueueManager.class.getClassLoader().getResource(filename);
		try {
			String testModuleListContent = FileUtils.readFileToString(new File(url.toURI()));
			TestModules testModules = XmlUtils.xmlStringToPojo(testModuleListContent, TestModules.class);
			for(TestModule testModule: testModules.getTestModule()) {
				persistTestModule(testModule);
			}

		} catch (IOException e) {
			logger.error("Failed to load test modules. Reason: {}", e.getMessage(), e);
		} catch (URISyntaxException e) {
			logger.error("Failed to load test modules. Reason: {}", e.getMessage(), e);
		} catch (JAXBException e) {
			logger.error("Failed to load test modules. Reason: {}", e.getMessage(), e);
		}

	}

	private void persistTestModule(TestModule testModule) {

		TestModuleMetaDataDocument tmMetaDocument = new TestModuleMetaDataDocument();
		Datastore ds = SmfManager.getInstance().getDataStore();
		
		TestModuleMetaDataDAO tmMetaDocumentDAO = new TestModuleMetaDataDAO(ds);
		TestModuleMetaDataDocument testModuleMetaDataDocument = tmMetaDocumentDAO.findByModuleNumber(testModule.getModuleNumber().longValue());
		if(testModuleMetaDataDocument != null) {
			testModuleMetaDataDocument.setEnabled(testModule.isEnabled());
			testModuleMetaDataDocument.setDescription("");
			testModuleMetaDataDocument.setModuleName(testModule.getModuleName());
			//testModuleMetaDataDocument.set
		}
	}
	
	
	

}
