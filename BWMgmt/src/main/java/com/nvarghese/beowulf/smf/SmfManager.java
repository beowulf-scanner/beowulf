package com.nvarghese.beowulf.smf;

import java.util.concurrent.atomic.AtomicBoolean;

public class SmfManager {

	private SmfServer execServer;
	private SmfSettings settings;

	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile SmfManager instance;

	private SmfManager() {

	}

	public static void initialize(SmfServer execServer, SmfSettings settings, boolean override) {

		synchronized (SmfManager.class) {
			if (instance == null || override == true) {
				instance = new SmfManager();
				instance.execServer = execServer;
				instance.settings = settings;
				initialized.set(true);
			}
		}

	}

	public static SmfManager getInstance() {

		return instance;
	}

	public SmfServer getExecServer() {

		return execServer;
	}

	public SmfSettings getSettings() {

		return settings;
	}

	public static boolean isInitialized() {

		return initialized.get();
	}

}
