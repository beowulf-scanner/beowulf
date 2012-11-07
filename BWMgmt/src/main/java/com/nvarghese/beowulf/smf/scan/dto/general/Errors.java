package com.nvarghese.beowulf.smf.scan.dto.general;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="errors")
public class Errors {
	
	@XmlElement(name="error")
	public List<String> errorList;
	
	public Errors() {
		errorList = new ArrayList<String>();
	}
	
	public Errors(List<String> errorList) {
		this.errorList = errorList;
	}

}
