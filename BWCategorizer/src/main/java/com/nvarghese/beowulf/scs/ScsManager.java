package com.nvarghese.beowulf.scs;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.common.exception.ServerSettingException;
import com.nvarghese.beowulf.common.zookeeper.ZkClientRunner;

public class ScsManager {

	private ScsServer scsServer;
	private ZkClientRunner zkClientRunner;
	private ScsServerSettings settings;
	private Datastore ds;

	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile ScsManager instance;

	static Logger logger = LoggerFactory.getLogger(ScsManager.class);

	private ScsManager() {

	}

	public static void initialize(ScsServer scsServer, ScsServerSettings settings, boolean override) {

		synchronized (ScsManager.class) {

			if (instance == null || override == true) {

				instance = new ScsManager();
				instance.scsServer = scsServer;
				instance.settings = settings;

				instance.initializeDatastore();
				// start zookeeper service
				instance.notifyZookeeper();

				initialized.set(true);
			}
		}

	}

	private void initializeDatastore() {

		Mongo mongo;
		try {
			mongo = new Mongo(BeowulfCommonConfigManager.getDbServers());
			ds = new Morphia().createDatastore(mongo, BeowulfCommonConfigManager.getDbName());
		} catch (ConfigurationException e) {
			logger.error("Failed to initialize data store. Reason: {}", e.getMessage(), e);
		}

	}

	private void notifyZookeeper() {

		String instanceName = settings.getIpAddress() + ":" + Integer.valueOf(scsServer.getJettyServer().getConnectors()[0].getPort());
		String zkNodeName = settings.getZkGroupNode();
		zkClientRunner = new ZkClientRunner(settings.getZkServers(), instanceName, zkNodeName);
		try {
			joinZkGroup();
		} catch (ServerSettingException e) {
			logger.error("Failed to notify zookeeper.", e);
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

	public static ScsManager getInstance() {

		return instance;
	}

	public ScsServer getScsServer() {

		return scsServer;
	}

	public ScsServerSettings getSettings() {

		return settings;
	}

	public static boolean isInitialized() {

		return initialized.get();
	}

	public Datastore getDataStore() {

		return ds;
	}

}
