package com.nvarghese.beowulf.sfc;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SFControllerSettings {

	private PropertiesConfiguration propertiesConfiguration;

	private String bwSfControllerRootPath;
	private String defaultConfDir;

	/* jetty settings */
	private String jettyResourceFileName;
	private String jettyContextDescriptor;
	private String jettyContextResourceBase;
	private String jettyContextRootPath;

	private static final String BWSFC__DEFAULT_CONF_DIR = "bw-controller.conf.dir";
	private static final String BWSFC__JETTY__RESOURCE_FILE_NAME = "bw-controller.jetty.resource_file_name";
	private static final String BWSFC__JETTY__CONTEXT__DESCRIPTOR = "bw-controller.jetty.context.descriptor";
	private static final String BWSFC__JETTY__CONTEXT__RESOURCE_BASE = "bw-controller.jetty.context.resourcebase";
	private static final String BWSFC__JETTY__CONTEXT__ROOT_PATH = "bw-controller.jetty.context.rootpath";

	static Logger logger = LoggerFactory.getLogger(SFControllerSettings.class);

	public SFControllerSettings() throws ConfigurationException, URISyntaxException {

		URL u = SFControllerSettings.class.getClassLoader().getResource("bw-controller.conf");
		propertiesConfiguration = new PropertiesConfiguration(u);
		initialize();
	}

	public SFControllerSettings(String categServerConfigFileName) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(SFControllerSettings.class.getClassLoader().getResource(
				categServerConfigFileName));
		initialize();
	}

	public SFControllerSettings(File categServerConfigFilePath) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(categServerConfigFilePath);
		initialize();
	}

	private void initialize() throws URISyntaxException {

		{
			String bwSfcHome = System.getenv("BWSFC_HOME");
			if (bwSfcHome != null) {
				PropertyConfigurator.configure(bwSfcHome + File.separator + "log4j.properties");
				bwSfControllerRootPath = bwSfcHome;
			} else {
				PropertyConfigurator.configure("log4j.properties");
				bwSfControllerRootPath = "";
				logger.warn("Environment variable 'BWSFC_HOME' is not set");

			}

		}

		{
			defaultConfDir = propertiesConfiguration.getString(BWSFC__DEFAULT_CONF_DIR, "conf");
		}
		{
			jettyResourceFileName = propertiesConfiguration
					.getString(BWSFC__JETTY__RESOURCE_FILE_NAME, "jetty-web.xml");
			jettyContextDescriptor = propertiesConfiguration.getString(BWSFC__JETTY__CONTEXT__DESCRIPTOR);
			jettyContextResourceBase = propertiesConfiguration.getString(BWSFC__JETTY__CONTEXT__RESOURCE_BASE);
			jettyContextRootPath = propertiesConfiguration.getString(BWSFC__JETTY__CONTEXT__ROOT_PATH);
		}
	}

	public String getBwSfControllerRootPath() {

		return bwSfControllerRootPath;
	}

	public void setBwSfControllerRootPath(String bwSfControllerRootPath) {

		this.bwSfControllerRootPath = bwSfControllerRootPath;
	}

	public String getDefaultConfDir() {

		return defaultConfDir;
	}

	public void setDefaultConfDir(String defaultConfDir) {

		this.defaultConfDir = defaultConfDir;
	}

	public String getJettyResourceFileName() {

		return jettyResourceFileName;
	}

	public void setJettyResourceFileName(String jettyResourceFileName) {

		this.jettyResourceFileName = jettyResourceFileName;
	}

	public String getJettyContextDescriptor() {

		return jettyContextDescriptor;
	}

	public void setJettyContextDescriptor(String jettyContextDescriptor) {

		this.jettyContextDescriptor = jettyContextDescriptor;
	}

	public String getJettyContextResourceBase() {

		return jettyContextResourceBase;
	}

	public void setJettyContextResourceBase(String jettyContextResourceBase) {

		this.jettyContextResourceBase = jettyContextResourceBase;
	}

	public String getJettyContextRootPath() {

		return jettyContextRootPath;
	}

	public void setJettyContextRootPath(String jettyContextRootPath) {

		this.jettyContextRootPath = jettyContextRootPath;
	}

}
