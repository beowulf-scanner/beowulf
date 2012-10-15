package com.nvarghese.beowulf.common.webtest.sfe.jobs;

public class TestJob extends AbstractTestJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7596740024625407037L;

	private String databaseName;

	private String webScanObjId;

	private String testJobObjId;

	public String getDatabaseName() {

		return databaseName;
	}

	public void setDatabaseName(String databaseName) {

		this.databaseName = databaseName;
	}

	public String getWebScanObjId() {

		return webScanObjId;
	}

	public void setWebScanObjId(String webScanObjId) {

		this.webScanObjId = webScanObjId;
	}

	public String getTestJobObjId() {

		return testJobObjId;
	}

	public void setTestJobObjId(String testJobObjId) {

		this.testJobObjId = testJobObjId;
	}

}
