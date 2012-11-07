package com.nvarghese.beowulf.sfc.services;

import java.util.HashMap;
import java.util.Map;

public class ScanInstanceRegister {

	private static Map<String, ScanInstanceServer> scanInstanceServerMap = new HashMap<String, ScanInstanceServer>();

	private static ScanInstanceRegister instance = new ScanInstanceRegister();

	private ScanInstanceRegister() {

	}

	public static ScanInstanceRegister getInstance() {

		return instance;
	}

	public void registerScanInstanceServer(String objectId, ScanInstanceServer scanInstanceServer) {

		scanInstanceServerMap.put(objectId, scanInstanceServer);
	}

	public ScanInstanceServer getScanInstanceServer(String objectId) {

		return scanInstanceServerMap.get(objectId);
	}

	public void unregisterScanInstanceServer(String objectId) {

		scanInstanceServerMap.remove(objectId);
	}

}
