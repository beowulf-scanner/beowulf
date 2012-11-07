package com.nvarghese.beowulf.common.webtest;


public enum ReportStatusMessage {
	
	NOT_STARTED("Not Started"),
	NOT_ALLOWED("Not Allowed"),
	SUBMITTED("Submitted"),
	ALREADY_SUBMITTED("Already Submitted"),	
	PROCESSING("Processing"), 
	COMPLETED("Completed"),
	ERROR("Error");
	
	private String message;
	
	
	ReportStatusMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
