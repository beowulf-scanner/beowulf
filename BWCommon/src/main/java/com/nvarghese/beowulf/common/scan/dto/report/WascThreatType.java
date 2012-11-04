package com.nvarghese.beowulf.common.scan.dto.report;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "threatClassName", "threatTypeId", "threatSubClassName" })
@XmlRootElement(name = "wasc_threat_type")
public class WascThreatType {

	@XmlElement(name = "threat_type_id", required = true)
	protected long threatTypeId;

	@XmlElement(name = "threat_class_name", required = true)
	protected String threatClassName;

	@XmlElement(name = "threat_subclass_name", required = true)
	protected String threatSubClassName;

	public long getThreatTypeId() {

		return threatTypeId;
	}

	public void setThreatTypeId(long threatTypeId) {

		this.threatTypeId = threatTypeId;
	}

	public String getThreatClassName() {

		return threatClassName;
	}

	public void setThreatClassName(String threatClassName) {

		this.threatClassName = threatClassName;
	}

	public String getThreatSubClassName() {

		return threatSubClassName;
	}

	public void setThreatSubClassName(String threatSubClassName) {

		this.threatSubClassName = threatSubClassName;
	}

}
