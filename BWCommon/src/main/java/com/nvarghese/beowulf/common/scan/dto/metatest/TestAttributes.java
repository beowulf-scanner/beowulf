package com.nvarghese.beowulf.common.scan.dto.metatest;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "testAttribute" })
@XmlRootElement(name = "test_attributes")
public class TestAttributes {

	@XmlElement(name = "test_attribute")
	protected List<String> testAttribute;

	public TestAttributes() {

		testAttribute = new ArrayList<String>();
	}

	public List<String> getTestAttribute() {

		return testAttribute;
	}

	public void setTestAttribute(List<String> testAttribute) {

		this.testAttribute = testAttribute;
	}

}
