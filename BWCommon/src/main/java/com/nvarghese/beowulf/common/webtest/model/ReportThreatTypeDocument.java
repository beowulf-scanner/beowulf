package com.nvarghese.beowulf.common.webtest.model;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;

@Entity("report_threat_types")
public class ReportThreatTypeDocument extends AbstractDocument {

	@Property("threat_type_id")
	private long threatTypeId;

	@Property("wasc_threat_class")
	private String wascThreatClass;

	@Property("wasc_threat_subclass")
	private String wascThreatSubClass;

	public String getWascThreatClass() {

		return wascThreatClass;
	}

	public void setWascThreatClass(String wascThreatClass) {

		this.wascThreatClass = wascThreatClass;
	}

	public long getThreatTypeId() {

		return threatTypeId;
	}

	public void setThreatTypeId(long threatTypeId) {

		this.threatTypeId = threatTypeId;
	}

	public String getWascThreatSubClass() {

		return wascThreatSubClass;
	}

	public void setWascThreatSubClass(String wascThreatSubClass) {

		this.wascThreatSubClass = wascThreatSubClass;
	}

}
