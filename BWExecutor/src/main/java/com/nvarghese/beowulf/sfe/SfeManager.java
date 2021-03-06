package com.nvarghese.beowulf.sfe;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.zookeeper.KeeperException;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.common.ds.DataStoreUtil;
import com.nvarghese.beowulf.common.exception.ServerSettingException;
import com.nvarghese.beowulf.common.zookeeper.ZkClientRunner;

public class SfeManager {

	private SfeServer execServer;
	private ZkClientRunner zkClientRunner;
	private SfeSettings settings;
	private Datastore ds;

	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile SfeManager instance;

	static Logger logger = LoggerFactory.getLogger(SfeManager.class);

	private SfeManager() {

	}

	public static void initialize(SfeServer execServer, SfeSettings settings, boolean override) {

		synchronized (SfeManager.class) {
			if (instance == null || override == true) {
				instance = new SfeManager();
				instance.execServer = execServer;
				instance.settings = settings;
				instance.initializeDatastore();
				instance.initializeScannerConfiguration();
				instance.notifyZookeeper();
				initialized.set(true);
			}
		}

	}

	private void initializeScannerConfiguration() {

		try {
			Resource rs = getResource(settings.getScannerConfigMasterFile(), settings.getScannerConfigDir());
			if (rs != null) {
				ConfigurationManager.initializeConfiguration(rs.getFile());
			} else {
				logger.info("Trying to load scanner configuration using class loaders");
				ConfigurationManager.initializeConfiguration(settings.getScannerConfigDir() + File.separator + settings.getScannerConfigMasterFile());
			}
			logger.info("Loaded scanner configurations");
		} catch (ConfigurationException e) {
			logger.error("Failed to load scanner configurations", e);
		} catch (MalformedURLException e) {
			logger.error("Failed to load scanner configurations", e);
		} catch (IOException e) {
			logger.error("Failed to load scanner configurations", e);
		}

	}

	private void notifyZookeeper() {

		String instanceName = settings.getIpAddress() + ":" + Integer.valueOf(execServer.getJettyServer().getConnectors()[0].getPort());
		String zkNodeName = settings.getZkGroupNode();
		zkClientRunner = new ZkClientRunner(settings.getZkServers(), instanceName, zkNodeName);
		try {
			joinZkGroup();
		} catch (ServerSettingException e) {
			logger.error("Failed to notify zookeeper.", e);
		}

	}

	private void initializeDatastore() {

		try {
			ds = DataStoreUtil.createOrGetDataStore(BeowulfCommonConfigManager.getDbUri(), BeowulfCommonConfigManager.getDbName());
		} catch (ConfigurationException e) {
			logger.error("Failed to initialize data store. Reason: {}", e.getMessage(), e);
		} catch (UnknownHostException e) {
			logger.error("Failed to initialize data store. Reason: {}", e.getMessage(), e);
		}

	}

	private void joinZkGroup() throws ServerSettingException {

		boolean exists = false;
		try {
			zkClientRunner.connect();
			zkClientRunner.createGroup();
			exists = zkClientRunner.checkMemberInGroup();

			if (exists) {
				String message = "Server already joined to " + zkClientRunner.getGroupName() + " in zookeeper. Try changing port number";
				throw new ServerSettingException(message);
			}

			/*
			 * This will make the zkClientRunner to join the group
			 */
			zkClientRunner.start();

		} catch (IOException e) {

			ServerSettingException sse = new ServerSettingException();
			sse.initCause(e.getCause());
			throw sse;

		} catch (KeeperException e) {
			ServerSettingException sse = new ServerSettingException();
			sse.initCause(e.getCause());
			throw sse;
		} catch (InterruptedException e) {

		}
	}

	private Resource getResource(String resourceName, String directory) throws MalformedURLException, IOException {

		Resource res = null;
		res = Resource.newClassPathResource(resourceName);

		if (res != null && res.exists()) {
			logger.info("Found resource `{}` in classpath", resourceName);
			return res;
		}
		res = Resource.newResource(resourceName);
		if (res != null && res.exists()) {
			logger.info("Found resource file `{}`", resourceName);
			return res;
		}

		logger.warn("Checking resource file in default conf directory");

		String d = "";
		if (directory != null && !directory.isEmpty()) {
			d = directory + File.separator;

		}

		String resPath = settings.getDefaultConfDir() + File.separator + d + resourceName;
		if (!settings.getBwSfExecutorRootPath().equalsIgnoreCase("")) {
			resPath = settings.getBwSfExecutorRootPath() + File.separator + resPath;
		}

		res = Resource.newResource(resPath);
		if (res != null && res.exists()) {
			logger.info("Found resource `{}` in default conf directory", resourceName);
			return res;
		} else {
			logger.warn("Cannot find resource file `{}`", resourceName);
		}

		return null;

	}

	public static SfeManager getInstance() {

		return instance;
	}

	public SfeServer getExecServer() {

		return execServer;
	}

	public SfeSettings getSettings() {

		return settings;
	}

	public static boolean isInitialized() {

		return initialized.get();
	}

	public Datastore getDataStore() {

		return ds;
	}

}
