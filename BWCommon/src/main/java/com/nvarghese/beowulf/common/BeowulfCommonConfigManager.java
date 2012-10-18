package com.nvarghese.beowulf.common;

import java.io.File;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeowulfCommonConfigManager implements ServletContextListener {

	private static PropertiesConfiguration propertiesConfiguration;

	private static final String MONGODB_URI = "mongodb://127.0.0.1:10001";

	private static final String BWCOMMON__DB__NAME = "bw-common.db.name";
	private static final String BWCOMMON__DB__URI = "bw-common.db_uri";

	static Logger logger = LoggerFactory.getLogger(BeowulfCommonConfigManager.class);

	public BeowulfCommonConfigManager() {

	}

	public static void initialize(String filename) throws ConfigurationException {

		File file = new File(filename);
		if (file.exists()) {
			logger.info("Found the configuration file for bw-common library");
			propertiesConfiguration = new PropertiesConfiguration(new File(filename));
		} else {
			logger.info("Trying to find the bw-common configuration resource using class loader");
			URL url = BeowulfCommonConfigManager.class.getClassLoader().getResource(filename);
			logger.info("Found the configuration file for bw-common library at `{}`", url);
			propertiesConfiguration = new PropertiesConfiguration(url);
		}

	}

	public static void initialize() throws ConfigurationException {

		initialize("bw-common.conf");
	}

	public static String getDbName() {

		if (propertiesConfiguration == null) {
			try {
				initialize();
			} catch (ConfigurationException e) {
				logger.error("Failed to initialize bw-common.", e);
			}
		}

		return propertiesConfiguration.getString(BWCOMMON__DB__NAME, "beowulfDB");
	}

	public static String getDbUri() throws ConfigurationException {

		if (propertiesConfiguration == null) {
			try {
				initialize();
			} catch (ConfigurationException e) {
				logger.error("Failed to initialize bw-common.", e);
			}
		}

		return propertiesConfiguration.getString(BWCOMMON__DB__URI, MONGODB_URI);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext ctx = event.getServletContext();
		String settingsFile = ctx.getInitParameter("bw-common-conf-filename");
		try {

			if (settingsFile != null) {
				initialize(settingsFile);
			} else
				initialize();
		} catch (ConfigurationException e) {
			logger.error("ConfigurationException while initializing BW-Common: " + e.getMessage(), e);
		}

	}

}
