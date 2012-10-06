package com.nvarghese.beowulf.smf;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.nvarghese.beowulf.smf.scan.resources.HelloWorld;
import com.nvarghese.beowulf.smf.scan.resources.NewScanResource;

public class SmfRestApplication extends Application {

	private static Set<Object> services = new HashSet<Object>();

	public SmfRestApplication() {

		services.add(new HelloWorld());
		services.add(new NewScanResource());
	}

	@Override
	public Set<Object> getSingletons() {

		return services;
	}

	public static Set<Object> getServices() {

		return services;
	}

}
