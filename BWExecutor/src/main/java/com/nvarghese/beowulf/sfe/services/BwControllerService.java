package com.nvarghese.beowulf.sfe.services;

import java.net.UnknownHostException;

import org.msgpack.rpc.Client;
import org.msgpack.rpc.loop.EventLoop;

import com.nvarghese.beowulf.common.rpc.BwControllerRpcInterface;

public class BwControllerService {

	public BwControllerRpcInterface getRpcClient(String hostName, int port) throws UnknownHostException {

		EventLoop loop = EventLoop.defaultEventLoop();

		Client cli = new Client(hostName, port, loop);
		// RPCInterface iface = cli.proxy(RPCInterface.class);
		BwControllerRpcInterface iface = cli.proxy(BwControllerRpcInterface.class);

		return iface;

	}

}
