package com.nvarghese.beowulf.sfc.services;

import org.msgpack.rpc.Request;

import com.nvarghese.beowulf.common.rpc.BwControllerRpcInterface;

public class BwControllerRpcInterfaceImpl implements BwControllerRpcInterface {

	@Override
	public String sayHello(String name) {

		return "Welcome " + name;
	}

	public void sayHello(Request request, String name) {

		request.sendResult(sayHello(name));
	}

}
