package com.nvarghese.beowulf.categ;

import java.util.concurrent.atomic.AtomicBoolean;

public class CategManager {

	private CategServer categServer;
	private CategServerSettings settings;

	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile CategManager instance;

	private CategManager() {

	}

	public static void initialize(CategServer categServer, CategServerSettings settings, boolean override) {

		synchronized (CategManager.class) {
			if (instance == null || override == true) {
				instance = new CategManager();
				instance.categServer = categServer;
				instance.settings = settings;
				initialized.set(true);
			}
		}

	}

	public static CategManager getInstance() {

		return instance;
	}

	public CategServer getCategServer() {

		return categServer;
	}

	public CategServerSettings getSettings() {

		return settings;
	}

	public static boolean isInitialized() {

		return initialized.get();
	}

}
