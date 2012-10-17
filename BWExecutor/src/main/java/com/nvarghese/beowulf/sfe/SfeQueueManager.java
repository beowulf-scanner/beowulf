package com.nvarghese.beowulf.sfe;

import java.io.IOException;
import java.net.URL;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.jms.JmsQueueManager;
import com.nvarghese.beowulf.sfe.jms.listeners.BwExecutorQueueListener;

public class SfeQueueManager implements ServletContextListener {

	private static PropertiesConfiguration propertiesConfiguration;

	private static String QUEUES__JNDI_FILE_NAME = "queues.jndi_file";
	private static final String QUEUES__CONNECTION_FACTORY_NAME = "queues.conn_factory_name";

	private static JmsQueueManager bwCategorizerJmsQueueClient;
	private static JmsQueueManager bwExecutorJmsQueueClient;

	static Logger logger = LoggerFactory.getLogger(SfeQueueManager.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		try {
			bwCategorizerJmsQueueClient.shutDownAll();
			bwExecutorJmsQueueClient.shutDownAll();
		} catch (JMSException e) {
			logger.error("Failed to shutdown queue manager: " + e.getMessage(), e);
		} catch (NamingException e) {
			logger.error("Failed to shutdown queue manager: " + e.getMessage(), e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext ctx = event.getServletContext();
		String settingsFile = ctx.getInitParameter("queue-conf-filename");
		try {
			if (settingsFile != null)
				initialize(settingsFile);
			else
				initialize();

			bwCategorizerJmsQueueClient = new JmsQueueManager("/queue/bwCategorizerQueue", getConnectionFactoryName());
			bwCategorizerJmsQueueClient.initializeQueueSenderOnly(SfeQueueManager.class.getClassLoader()
					.getResourceAsStream(getJndiFileName()));

			BwExecutorQueueListener bwExecutorQueueListener = new BwExecutorQueueListener();
			bwExecutorJmsQueueClient = new JmsQueueManager("/queue/bwExecutorQueue", getConnectionFactoryName());
			bwExecutorJmsQueueClient.initializeQueueReceiverOnly(
					SfeQueueManager.class.getClassLoader().getResourceAsStream(getJndiFileName()), 5, bwExecutorQueueListener);

			// start listeners
			bwCategorizerJmsQueueClient.startJmsQueueSender();
			bwExecutorJmsQueueClient.startJmsQueueReceivers();
			logger.info("SfcQueueManager for Bw-SFE started");

		} catch (ConfigurationException e) {
			logger.error("Failed to initialize queue settings: " + e.getMessage(), e);
		} catch (JMSException e) {
			logger.error("Failed to initialize queue settings: " + e.getMessage(), e);
		} catch (NamingException e) {
			logger.error("Failed to initialize queue settings: " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Failed to initialize queue settings: " + e.getMessage(), e);
		}

	}

	private String getConnectionFactoryName() {

		if (propertiesConfiguration == null) {
			try {
				initialize();
			} catch (ConfigurationException e) {
				logger.error("Failed to initialize security queue settings: " + e.getMessage(), e);
			}
		}
		return propertiesConfiguration.getString(QUEUES__CONNECTION_FACTORY_NAME);
	}

	public static void initialize(String filename) throws ConfigurationException {

		URL u1 = SfeQueueManager.class.getClassLoader().getResource(filename);
		propertiesConfiguration = new PropertiesConfiguration(u1);

	}

	public static void initialize() throws ConfigurationException {

		initialize("queues.conf");
	}

	public static String getUserName() {

		if (propertiesConfiguration == null) {
			try {
				initialize();
			} catch (ConfigurationException e) {
				logger.error("Failed to initialize queue settings: " + e.getMessage(), e);
			}
		}
		return propertiesConfiguration.getString(Context.SECURITY_PRINCIPAL);
	}

	public static String getPassword() {

		if (propertiesConfiguration == null) {
			try {
				initialize();
			} catch (ConfigurationException e) {
				logger.error("Failed to initialize queue settings: " + e.getMessage(), e);
			}
		}
		return propertiesConfiguration.getString(Context.SECURITY_CREDENTIALS);
	}

	public static String getJndiFileName() {

		if (propertiesConfiguration == null) {
			try {
				initialize();
			} catch (ConfigurationException e) {
				logger.error("Failed to initialize queue settings: " + e.getMessage(), e);
			}
		}
		return propertiesConfiguration.getString(QUEUES__JNDI_FILE_NAME);
	}

	/**
	 * 
	 * @return bwCategorizerJmsQueueClient
	 */
	public static JmsQueueManager getBwCategorizerJmsQueueClient() {

		return bwCategorizerJmsQueueClient;
	}

	/**
	 * 
	 * @return
	 */
	public static JmsQueueManager getBwExecutorJmsQueueClient() {

		return bwExecutorJmsQueueClient;
	}
}
