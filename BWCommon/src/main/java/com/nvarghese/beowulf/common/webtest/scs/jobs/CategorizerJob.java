package com.nvarghese.beowulf.common.webtest.scs.jobs;

public class CategorizerJob extends AbstractCategorizerJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3963215005873035249L;

	private String databaseName;

	private String webScanObjId;

	private String categorizerJobObjId;

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

	public String getCategorizerJobObjId() {

		return categorizerJobObjId;
	}

	public void setCategorizerJobObjId(String categorizerJobObjId) {

		this.categorizerJobObjId = categorizerJobObjId;
	};

}
