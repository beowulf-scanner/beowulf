package com.nvarghese.beowulf.common.jobs;

public class ReportGenerateJob extends AbstractBeowulfJob {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8863434704685590323L;

	private final String webScanObjectId;

	public ReportGenerateJob(String webScanObjectId) {

		super();
		this.webScanObjectId = webScanObjectId;
	}

	public String getWebScanObjectId() {

		return webScanObjectId;
	}

}
