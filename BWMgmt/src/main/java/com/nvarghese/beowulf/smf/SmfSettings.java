package com.nvarghese.beowulf.smf;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmfSettings {

	private PropertiesConfiguration propertiesConfiguration;

	private String bwSmfRootPath;
	private String defaultConfDir;

	/* jetty settings */
	private String jettyResourceFileName;
	private String jettyContextDescriptor;
	private String jettyContextResourceBase;
	private String jettyContextRootPath;

	/* reasons file names */
	private String abortReasonsFileName;
	private String reportGenReasonsFileName;

	private static final String BWSMF__DEFAULT_CONF_DIR = "bw-smf.conf.dir";
	private static final String BWSMF__JETTY__RESOURCE_FILE_NAME = "bw-smf.jetty.resource_file_name";
	private static final String BWSMF__JETTY__CONTEXT__DESCRIPTOR = "bw-smf.jetty.context.descriptor";
	private static final String BWSMF__JETTY__CONTEXT__RESOURCE_BASE = "bw-smf.jetty.context.resourcebase";
	private static final String BWSMF__JETTY__CONTEXT__ROOT_PATH = "bw-smf.jetty.context.rootpath";

	private static final String BWSMF__ABORT_REASONS_FILE_NAME = "bw-smf.abort_reasons_file";
	private static final String BWSMF__REPORTGEN_REASONS_FILE_NAME = "bw-smf.report_gen_reasons_file";

	static Logger logger = LoggerFactory.getLogger(SmfSettings.class);

	public SmfSettings() throws ConfigurationException, URISyntaxException {

		URL u = SmfSettings.class.getClassLoader().getResource("bw-smf.conf");
		propertiesConfiguration = new PropertiesConfiguration(u);
		initialize();
	}

	public SmfSettings(String smfConfigFileName) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(SmfSettings.class.getClassLoader().getResource(smfConfigFileName));
		initialize();
	}

	public SmfSettings(File smfServerConfigFilePath) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(smfServerConfigFilePath);
		initialize();
	}

	private void initialize() throws URISyntaxException {

		{
			String bwSfeHome = System.getenv("BWSMF_HOME");
			if (bwSfeHome != null) {
				PropertyConfigurator.configure(bwSfeHome + File.separator + "log4j.properties");
				bwSmfRootPath = bwSfeHome;
			} else {
				PropertyConfigurator.configure("log4j.properties");
				bwSmfRootPath = "";
				logger.warn("Environment variable 'BWSMF_HOME' is not set");

			}

		}

		{
			defaultConfDir = propertiesConfiguration.getString(BWSMF__DEFAULT_CONF_DIR, "conf");
		}
		{
			jettyResourceFileName = propertiesConfiguration.getString(BWSMF__JETTY__RESOURCE_FILE_NAME, "jetty-web.xml");
			jettyContextDescriptor = propertiesConfiguration.getString(BWSMF__JETTY__CONTEXT__DESCRIPTOR);
			jettyContextResourceBase = propertiesConfiguration.getString(BWSMF__JETTY__CONTEXT__RESOURCE_BASE);
			jettyContextRootPath = propertiesConfiguration.getString(BWSMF__JETTY__CONTEXT__ROOT_PATH);
		}

		{
			abortReasonsFileName = propertiesConfiguration.getString(BWSMF__ABORT_REASONS_FILE_NAME);
			reportGenReasonsFileName = propertiesConfiguration.getString(BWSMF__REPORTGEN_REASONS_FILE_NAME);
		}
	}

	public String getBwSmfRootPath() {

		return bwSmfRootPath;
	}

	public void setBwSmfRootPath(String bwSmfRootPath) {

		this.bwSmfRootPath = bwSmfRootPath;
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

	public String getAbortReasonsFileName() {

		return abortReasonsFileName;
	}

	public String getReportGenReasonsFileName() {

		return reportGenReasonsFileName;
	}

}
