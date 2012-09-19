package com.nvarghese.beowulf.categ;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategServerSettings {

	private PropertiesConfiguration propertiesConfiguration;

	private String bwCategRootPath;
	private String defaultConfDir;

	private String ipAddress;

	/* jetty settings */
	private String jettyResourceFileName;
	private String jettyContextDescriptor;
	private String jettyContextResourceBase;
	private String jettyContextRootPath;

	/* zookeeper settings */
	private String zkGroupNode;
	private String zkServers;

	private static final String BWCATEG__IP_ADDR = "bw-categ.ip_address";
	private static final String BWCATEG__DEFAULT_CONF_DIR = "bw-categ.conf.dir";
	private static final String BWCATEG__JETTY__RESOURCE_FILE_NAME = "bw-categ.jetty.resource_file_name";
	private static final String BWCATEG__JETTY__CONTEXT__DESCRIPTOR = "bw-categ.jetty.context.descriptor";
	private static final String BWCATEG__JETTY__CONTEXT__RESOURCE_BASE = "bw-categ.jetty.context.resourcebase";
	private static final String BWCATEG__JETTY__CONTEXT__ROOT_PATH = "bw-categ.jetty.context.rootpath";

	private static final String BWCATEG__ZK__GROUP_NODE = "bw-categ.zookeeper.group_node_name";
	private static final String BWCATEG__ZK__SERVER_LIST = "bw-categ.zookeeper.servers";

	static Logger logger = LoggerFactory.getLogger(CategServerSettings.class);

	public CategServerSettings() throws ConfigurationException, URISyntaxException {

		URL u = CategServerSettings.class.getClassLoader().getResource("bw-categorizer.conf");
		propertiesConfiguration = new PropertiesConfiguration(u);
		initialize();
	}

	public CategServerSettings(String categServerConfigFileName) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(CategServerSettings.class.getClassLoader().getResource(
				categServerConfigFileName));
		initialize();
	}

	public CategServerSettings(File categServerConfigFilePath) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(categServerConfigFilePath);
		initialize();
	}

	private void initialize() throws URISyntaxException {

		{
			String bwCategHome = System.getenv("BWCATEG_HOME");
			if (bwCategHome != null) {
				PropertyConfigurator.configure(bwCategHome + File.separator + "log4j.properties");
				bwCategRootPath = bwCategHome;
			} else {
				PropertyConfigurator.configure("log4j.properties");
				bwCategRootPath = "";
				logger.warn("Environment variable 'BWCATEG_HOME' is not set");

			}

		}

		{
			defaultConfDir = propertiesConfiguration.getString(BWCATEG__DEFAULT_CONF_DIR, "conf");
			ipAddress = propertiesConfiguration.getString(BWCATEG__IP_ADDR);
		}
		{
			jettyResourceFileName = propertiesConfiguration.getString(BWCATEG__JETTY__RESOURCE_FILE_NAME,
					"jetty-web.xml");
			jettyContextDescriptor = propertiesConfiguration.getString(BWCATEG__JETTY__CONTEXT__DESCRIPTOR);
			jettyContextResourceBase = propertiesConfiguration.getString(BWCATEG__JETTY__CONTEXT__RESOURCE_BASE);
			jettyContextRootPath = propertiesConfiguration.getString(BWCATEG__JETTY__CONTEXT__ROOT_PATH);
		}
		{
			zkGroupNode = propertiesConfiguration.getString(BWCATEG__ZK__GROUP_NODE, "categ");
			zkServers = propertiesConfiguration.getString(BWCATEG__ZK__SERVER_LIST);
		}
	}

	public String getIpAddress() {

		return ipAddress;
	}

	public String getBwCategRootPath() {

		return bwCategRootPath;
	}

	public void setBwCategRootPath(String bwCategRootPath) {

		this.bwCategRootPath = bwCategRootPath;
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

	public String getZkGroupNode() {

		return zkGroupNode;
	}

	public String getZkServers() {

		return zkServers;
	}

}
