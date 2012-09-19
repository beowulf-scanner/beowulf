package com.nvarghese.beowulf.sfc;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.exception.ServerSettingException;
import com.nvarghese.beowulf.common.zookeeper.ZkClientRunner;

public class SFControllerManager {

	private SFControllerServer controllerServer;
	private ZkClientRunner zkClientRunner;
	private SFControllerSettings settings;

	private static AtomicBoolean initialized = new AtomicBoolean(false);
	private static volatile SFControllerManager instance;

	static Logger logger = LoggerFactory.getLogger(SFControllerManager.class);

	private SFControllerManager() {

	}

	public static void initialize(SFControllerServer controllerServer, SFControllerSettings settings, boolean override) {

		synchronized (SFControllerManager.class) {
			if (instance == null || override == true) {
				instance = new SFControllerManager();
				instance.controllerServer = controllerServer;
				instance.settings = settings;

				// start zookeeper service
				instance.notifyZookeeper();

				initialized.set(true);
			}
		}

	}

	private void notifyZookeeper() {

		String instanceName = settings.getIpAddress() + ":"
				+ Integer.valueOf(controllerServer.getJettyServer().getConnectors()[0].getPort());
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
				String message = "Server already joined to " + zkClientRunner.getGroupName()
						+ " in zookeeper. Try changing port number";
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

	public static SFControllerManager getInstance() {

		return instance;
	}

	public SFControllerServer getCategServer() {

		return controllerServer;
	}

	public SFControllerSettings getSettings() {

		return settings;
	}

	public static boolean isInitialized() {

		return initialized.get();
	}

}
