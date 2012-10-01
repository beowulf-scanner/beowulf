package com.nvarghese.beowulf.common.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZkClientRunner will create the group in zookeeper, and joins as an ephemeral
 * node for indefinite time
 * 
 * @author nibin
 * 
 */
public class ZkClientRunner implements Runnable {

	private ZooKeeper zk;
	private ZkConnectionWatcher zkConnectionWatcher;
	private final String connectString;
	private final String groupName;
	private Thread thread;
	private final String memberIpAddressAndPort;

	private static final int SESSION_TIMEOUT = 5000;

	static Logger logger = LoggerFactory.getLogger(ZkClientRunner.class);

	public ZkClientRunner(String connectString, String memberIpAddressAndPort, String groupName) {

		zkConnectionWatcher = new ZkConnectionWatcher();
		this.connectString = connectString;
		this.memberIpAddressAndPort = memberIpAddressAndPort;
		this.groupName = groupName;
		thread = new Thread(this, "zkClientRunner");
	}

	public String getGroupName() {

		return groupName;
	}

	public void connect() throws IOException {

		logger.info("Connecting to zookeeper servers");
		zk = new ZooKeeper(connectString, SESSION_TIMEOUT, zkConnectionWatcher);

		try {
			zkConnectionWatcher.getConnectSignal().await();
			logger.info("Connection established with zookeeper servers");

		} catch (InterruptedException ie) {

		}
	}

	/**
	 * If group doesn't exit, then this will create the group
	 * 
	 * @throws InterruptedException
	 * @throws KeeperException
	 */
	public void createGroup() throws KeeperException, InterruptedException {

		Stat stat;

		String path = "/" + this.groupName;
		stat = zk.exists(path, false);

		if (stat == null) {
			logger.info("`{}` group doesn't exist in zookeeper. Trying to create the group", groupName);
			String createdPath = zk.create(path, null/* data */, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info("Created `{}` Group: {}", groupName, createdPath);
		} else {
			logger.info("`{}` group exists in zookeeper", groupName);
		}

	}

	/**
	 * Joins the group with registered IPAddress
	 * 
	 * @param memberIpAddressAndPort
	 * @throws InterruptedException
	 * @throws KeeperException
	 * @throws KeeperException
	 */
	private void joinGroup() throws InterruptedException, KeeperException {

		String path = "/" + groupName + "/" + memberIpAddressAndPort;
		String createdPath = zk.create(path, null/* data */, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
		logger.info("Joined `{}` Group: {}", groupName, createdPath);
	}

	public boolean checkMemberInGroup() throws KeeperException, InterruptedException {

		boolean exists = false;
		Stat stat;

		String path = "/" + groupName + "/" + memberIpAddressAndPort;

		stat = zk.exists(path, false);

		if (stat != null)
			exists = true;

		return exists;

	}

	private void close() throws InterruptedException {

		zk.close();
	}

	@Override
	public void run() {

		try {

			joinGroup();

			/* stay alive indefinetly */
			Thread.sleep(Long.MAX_VALUE);

		} catch (InterruptedException ie) {

		} catch (KeeperException e) {
			logger.error("KeeperException while ZkClientRunner join group", e);
		} finally {

			try {
				close();
			} catch (InterruptedException e) {

			}
		}
	}

	public void interupt() {

		thread.interrupt();
	}

	public void start() {

		thread.start();
	}

}
