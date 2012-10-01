package com.nvarghese.beowulf.smf;

import java.util.concurrent.atomic.AtomicBoolean;

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

		Mongo mongo = new Mongo(BeowulfCommonConfigManager.getDbServers());
		ds = new Morphia().createDatastore(mongo, BeowulfCommonConfigManager.getDbName());

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
