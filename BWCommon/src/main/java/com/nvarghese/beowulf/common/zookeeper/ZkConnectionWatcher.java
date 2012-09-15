package com.nvarghese.beowulf.common.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZkConnectionWatcher implements Watcher {

	private CountDownLatch connectSignal = new CountDownLatch(1);

	@Override
	public void process(WatchedEvent event) {

		if (event.getState() == KeeperState.SyncConnected) {
			connectSignal.countDown();
		}
	}

	public CountDownLatch getConnectSignal() {

		return connectSignal;
	}
}
