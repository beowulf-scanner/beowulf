package com.nvarghese.beowulf.sfe;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SfeSettings {

	private PropertiesConfiguration propertiesConfiguration;

	private String bwSfExecutorRootPath;
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

	private static final String BWSFE__IP_ADDR = "bw-sfe.ip_address";
	private static final String BWSFE__DEFAULT_CONF_DIR = "bw-sfe.conf.dir";
	private static final String BWSFE__JETTY__RESOURCE_FILE_NAME = "bw-sfe.jetty.resource_file_name";
	private static final String BWSFE__JETTY__CONTEXT__DESCRIPTOR = "bw-sfe.jetty.context.descriptor";
	private static final String BWSFE__JETTY__CONTEXT__RESOURCE_BASE = "bw-sfe.jetty.context.resourcebase";
	private static final String BWSFE__JETTY__CONTEXT__ROOT_PATH = "bw-sfe.jetty.context.rootpath";

	private static final String BWSFE__ZK__GROUP_NODE = "bw-sfe.zookeeper.group_node_name";
	private static final String BWSFE__ZK__SERVER_LIST = "bw-sfe.zookeeper.servers";

	static Logger logger = LoggerFactory.getLogger(SfeSettings.class);

	public SfeSettings() throws ConfigurationException, URISyntaxException {

		URL u = SfeSettings.class.getClassLoader().getResource("bw-sfe.conf");
		propertiesConfiguration = new PropertiesConfiguration(u);
		initialize();
	}

	public SfeSettings(String execServerConfigFileName) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(SfeSettings.class.getClassLoader().getResource(
				execServerConfigFileName));
		initialize();
	}

	public SfeSettings(File execServerConfigFilePath) throws ConfigurationException, URISyntaxException {

		propertiesConfiguration = new PropertiesConfiguration(execServerConfigFilePath);
		initialize();
	}

	private void initialize() throws URISyntaxException {

		{
			String bwSfeHome = System.getenv("BWSFE_HOME");
			if (bwSfeHome != null) {
				PropertyConfigurator.configure(bwSfeHome + File.separator + "log4j.properties");
				bwSfExecutorRootPath = bwSfeHome;
			} else {
				PropertyConfigurator.configure("log4j.properties");
				bwSfExecutorRootPath = "";
				logger.warn("Environment variable 'BWSFE_HOME' is not set");

			}

		}

		{
			defaultConfDir = propertiesConfiguration.getString(BWSFE__DEFAULT_CONF_DIR, "conf");
			ipAddress = propertiesConfiguration.getString(BWSFE__IP_ADDR);
		}
		{
			jettyResourceFileName = propertiesConfiguration
					.getString(BWSFE__JETTY__RESOURCE_FILE_NAME, "jetty-web.xml");
			jettyContextDescriptor = propertiesConfiguration.getString(BWSFE__JETTY__CONTEXT__DESCRIPTOR);
			jettyContextResourceBase = propertiesConfiguration.getString(BWSFE__JETTY__CONTEXT__RESOURCE_BASE);
			jettyContextRootPath = propertiesConfiguration.getString(BWSFE__JETTY__CONTEXT__ROOT_PATH);
		}
		{
			zkGroupNode = propertiesConfiguration.getString(BWSFE__ZK__GROUP_NODE, "executor");
			zkServers = propertiesConfiguration.getString(BWSFE__ZK__SERVER_LIST);
		}

	}

	public String getBwSfExecutorRootPath() {

		return bwSfExecutorRootPath;
	}

	public void setBwSfExecutorRootPath(String bwSfExecutorRootPath) {

		this.bwSfExecutorRootPath = bwSfExecutorRootPath;
	}

	public String getIpAddress() {

		return ipAddress;
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
