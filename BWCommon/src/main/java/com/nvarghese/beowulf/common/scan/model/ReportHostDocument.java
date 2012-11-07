package com.nvarghese.beowulf.common.scan.model;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;

@Entity("report_host")
public class ReportHostDocument extends AbstractDocument {

	@Property("host_name")
	private String hostName;

	@Property("server_value")
	private String serverValue;

	@Property("ip_address")
	private String ipAddress;

	@Property("technology")
	private String technology;

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

	public String getHostName() {

		return hostName;
	}

	public void setHostName(String hostName) {

		this.hostName = hostName;
	}

	public String getIpAddress() {

		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {

		this.ipAddress = ipAddress;
	}

	public String getTechnology() {

		return technology;
	}

	public void setTechnology(String technology) {

		this.technology = technology;
	}

	public String getServerValue() {

		return serverValue;
	}

	public void setServerValue(String serverValue) {

		this.serverValue = serverValue;
	}
}
