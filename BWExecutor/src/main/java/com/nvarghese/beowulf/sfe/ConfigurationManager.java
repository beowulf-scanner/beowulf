package com.nvarghese.beowulf.sfe;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationManager {

	private static Configuration scannerConfiguration;

	static Logger logger = LoggerFactory.getLogger(ConfigurationManager.class);

	public static void initializeConfiguration(String path) throws ConfigurationException {

		scannerConfiguration = new PropertiesConfiguration(ConfigurationManager.class.getClassLoader().getResource(path));

	}

	public static void initializeConfiguration(File path) throws ConfigurationException {

		scannerConfiguration = new PropertiesConfiguration(path);
	}

	public static Configuration getScannerConfiguration() {

		return scannerConfiguration;
	}

}
