package com.nvarghese.beowulf.common;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.ServerAddress;

public class BeowulfCommonConfigManager implements ServletContextListener {

	private static PropertiesConfiguration propertiesConfiguration;

	private static final String BWCOMMON__DB__NAME = "bw-common.db.name";
	private static final String BWCOMMON__DB__SERVER_LIST = "bw-common.db.servers";

	static Logger logger = LoggerFactory.getLogger(BeowulfCommonConfigManager.class);

	public BeowulfCommonConfigManager() {

		propertiesConfiguration = new PropertiesConfiguration();
	}

	public static void initialize(String filename) throws ConfigurationException {

		propertiesConfiguration = new PropertiesConfiguration(new File(filename));

	}

	public static void initialize() throws ConfigurationException {

		initialize("bw-common.conf");
	}

	public static String getDbName() {

		return propertiesConfiguration.getString(BWCOMMON__DB__NAME, "beowulfDB");
	}

	public static List<ServerAddress> getDbServers() throws ConfigurationException {

		List<String> addrs = propertiesConfiguration.getList(BWCOMMON__DB__SERVER_LIST);
		List<ServerAddress> dbServers = new ArrayList<ServerAddress>();

		try {
			/* Expected address format is host:port */
			for (String address : addrs) {
				String splits[] = address.split(":");
				if (splits.length == 2) {

					dbServers.add(new ServerAddress(splits[0], Integer.parseInt(splits[1])));
				} else
					throw new ConfigurationException("Expected Server address in host:port format");
			}
		} catch (NumberFormatException e) {
			ConfigurationException ce = new ConfigurationException();
			ce.initCause(e.getCause());
			throw ce;
		} catch (UnknownHostException e) {
			ConfigurationException ce = new ConfigurationException();
			ce.initCause(e.getCause());
			throw ce;
		}

		return dbServers;
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
