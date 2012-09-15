package com.nvarghese.beowulf.sfe;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SFExecutorSettings {

	private PropertiesConfiguration propertiesConfiguration;

	private String bwSfControllerRootPath;
	private String defaultConfDir;

	/* jetty settings */
	private String jettyResourceFileName;
	private String jettyContextDescriptor;
	private String jettyContextResourceBase;
	private String jettyContextRootPath;

	private static final String BWSFE__DEFAULT_CONF_DIR = "bw-executor.conf.dir";
	private static final String BWSFE__JETTY__RESOURCE_FILE_NAME = "bw-executor.jetty.resource_file_name";
	private static final String BWSFE__JETTY__CONTEXT__DESCRIPTOR = "bw-executor.jetty.context.descriptor";
	private static final String BWSFE__JETTY__CONTEXT__RESOURCE_BASE = "bw-executor.jetty.context.resourcebase";
	private static final String BWSFE__JETTY__CONTEXT__ROOT_PATH = "bw-executor.jetty.context.rootpath";

	static Logger logger = LoggerFactory.getLogger(SFExecutorSettings.class);

	public SFExecutorSettings() throws ConfigurationException, URISyntaxException {

		URL u = SFExecutorSettings.class.getClassLoader().getResource("bw-executor.conf");
		propertiesConfiguration = new PropertiesConfiguration(u);
		initialize();
	}

	public SFExecutorSettings(String execServerConfigFileName) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(SFExecutorSettings.class.getClassLoader().getResource(
				execServerConfigFileName));
		initialize();
	}

	public SFExecutorSettings(File execServerConfigFilePath) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(execServerConfigFilePath);
		initialize();
	}

	private void initialize() throws URISyntaxException {

		{
			String bwSfeHome = System.getenv("BWSFE_HOME");
			if (bwSfeHome != null) {
				PropertyConfigurator.configure(bwSfeHome + File.separator + "log4j.properties");
				bwSfControllerRootPath = bwSfeHome;
			} else {
				PropertyConfigurator.configure("log4j.properties");
				bwSfControllerRootPath = "";
				logger.warn("Environment variable 'BWSFE_HOME' is not set");

			}

		}

		{
			defaultConfDir = propertiesConfiguration.getString(BWSFE__DEFAULT_CONF_DIR, "conf");
		}
		{
			jettyResourceFileName = propertiesConfiguration
					.getString(BWSFE__JETTY__RESOURCE_FILE_NAME, "jetty-web.xml");
			jettyContextDescriptor = propertiesConfiguration.getString(BWSFE__JETTY__CONTEXT__DESCRIPTOR);
			jettyContextResourceBase = propertiesConfiguration.getString(BWSFE__JETTY__CONTEXT__RESOURCE_BASE);
			jettyContextRootPath = propertiesConfiguration.getString(BWSFE__JETTY__CONTEXT__ROOT_PATH);
		}
	}

	public String getBwSfExecutorRootPath() {

		return bwSfControllerRootPath;
	}

	public void setBwSfExecutorRootPath(String bwSfControllerRootPath) {

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
