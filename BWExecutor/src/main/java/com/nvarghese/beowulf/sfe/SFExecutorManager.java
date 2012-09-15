package com.nvarghese.beowulf.sfe;

import java.util.concurrent.atomic.AtomicBoolean;

public class SFExecutorManager {

	private SFExecutorServer execServer;
	private SFExecutorSettings settings;

	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile SFExecutorManager instance;

	private SFExecutorManager() {

	}

	public static void initialize(SFExecutorServer execServer, SFExecutorSettings settings, boolean override) {

		synchronized (SFExecutorManager.class) {
			if (instance == null || override == true) {
				instance = new SFExecutorManager();
				instance.execServer = execServer;
				instance.settings = settings;
				initialized.set(true);
			}
		}

	}

	public static SFExecutorManager getInstance() {

		return instance;
	}

	public SFExecutorServer getExecServer() {

		return execServer;
	}

	public SFExecutorSettings getSettings() {

		return settings;
	}

	public static boolean isInitialized() {

		return initialized.get();
	}

}
