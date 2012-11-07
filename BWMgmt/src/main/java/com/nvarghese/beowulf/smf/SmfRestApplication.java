package com.nvarghese.beowulf.smf;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.nvarghese.beowulf.smf.scan.resources.HelloWorld;
import com.nvarghese.beowulf.smf.scan.resources.NewScanResource;
import com.nvarghese.beowulf.smf.scan.resources.ReportResource;
import com.nvarghese.beowulf.smf.scan.resources.SimpleQueryResource;

public class SmfRestApplication extends Application {

	private static Set<Object> services = new HashSet<Object>();

	public SmfRestApplication() {

		services.add(new HelloWorld());
		services.add(new NewScanResource());
		services.add(new SimpleQueryResource());
		services.add(new ReportResource());
	}

	@Override
	public Set<Object> getSingletons() {

		return services;
	}

	public static Set<Object> getServices() {

		return services;
	}

}
