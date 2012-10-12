package com.nvarghese.beowulf.common.webtest;

public enum JobStatus {

		INIT("INIT"), 
		PROCESSING("PROCESSING"), 
		WAITING("WAITING"), 
		ERROR("ERROR"), 
		TERMINATED("TERMINATED"), 
		COMPLETED("COMPLETED"), 
		NONE("NONE");

	String value;

	JobStatus(String value) {

		this.value = value;

	}

	public String getValue() {

		return value;
	}

	public static JobStatus getJobStatus(String jobStatusValue) {

		for (JobStatus status : JobStatus.values()) {
			if (status.value.equalsIgnoreCase(jobStatusValue)) {
				return status;
			}
		}

		return NONE;

	}

}
