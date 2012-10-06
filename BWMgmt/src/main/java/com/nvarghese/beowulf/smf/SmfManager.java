package com.nvarghese.beowulf.smf;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;

public class SmfManager {

	private SmfServer smfServer;
	private SmfSettings settings;
	private Datastore ds;

	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile SmfManager instance;

	static Logger logger = LoggerFactory.getLogger(SmfManager.class);

	private SmfManager() {

	}

	public static void initialize(SmfServer smfServer, SmfSettings settings, boolean override) {

		synchronized (SmfManager.class) {
			if (instance == null || override == true) {
				instance = new SmfManager();
				instance.smfServer = smfServer;
				instance.settings = settings;
				instance.initializeDatastore();
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

	public static SmfManager getInstance() {

		return instance;
	}

	public SmfServer getSmfServer() {

		return smfServer;
	}

	public SmfSettings getSettings() {

		return settings;
	}

	public static boolean isInitialized() {

		return initialized.get();
	}

	public Datastore getDataStore() {

		return ds;
	}

}
