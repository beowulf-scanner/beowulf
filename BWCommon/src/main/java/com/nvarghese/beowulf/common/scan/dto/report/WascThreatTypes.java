package com.nvarghese.beowulf.common.scan.dto.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "wascThreatType" })
@XmlRootElement(name = "wasc_threat_types")
public class WascThreatTypes {

	@XmlElement(name = "wasc_threat_type", required = true)
	protected List<WascThreatType> wascThreatType;

	public List<WascThreatType> getWascThreatType() {

		if (wascThreatType == null) {
			wascThreatType = new ArrayList<WascThreatType>();
		}
		return this.wascThreatType;
	}

}
