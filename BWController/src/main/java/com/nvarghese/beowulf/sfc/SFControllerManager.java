package com.nvarghese.beowulf.sfc;

import java.util.concurrent.atomic.AtomicBoolean;

public class SFControllerManager {

	private SFControllerServer categServer;
	private SFControllerSettings settings;

	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile SFControllerManager instance;

	private SFControllerManager() {

	}

	public static void initialize(SFControllerServer categServer, SFControllerSettings settings, boolean override) {

		synchronized (SFControllerManager.class) {
			if (instance == null || override == true) {
				instance = new SFControllerManager();
				instance.categServer = categServer;
				instance.settings = settings;
				initialized.set(true);
			}
		}

	}

	public static SFControllerManager getInstance() {

		return instance;
	}

	public SFControllerServer getCategServer() {

		return categServer;
	}

	public SFControllerSettings getSettings() {

		return settings;
	}

	public static boolean isInitialized() {

		return initialized.get();
	}

}
