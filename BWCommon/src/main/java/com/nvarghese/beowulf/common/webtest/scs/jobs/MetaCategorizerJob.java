package com.nvarghese.beowulf.common.webtest.scs.jobs;

public class MetaCategorizerJob extends AbstractCategorizerJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2154760240035698132L;

	private String databaseName;

	private String webScanObjId;

	private String txnObjId;

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

	public String getTxnObjId() {

		return txnObjId;
	}

	public void setTxnObjId(String txnObjId) {

		this.txnObjId = txnObjId;
	}

}
