package com.nvarghese.beowulf.common.jobs;

public class NewScanJob extends AbstractBeowulfJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8912117255202069939L;
	private final String webScanObjectId;

	public String getWebScanObjectId() {

		return webScanObjectId;
	}

	public NewScanJob(String webScanObjectId) {

		super();
		this.webScanObjectId = webScanObjectId;
	}

}
