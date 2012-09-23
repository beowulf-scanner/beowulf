package com.nvarghese.beowulf.common.scan.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.nvarghese.beowulf.common.model.AbstractDocument;

@Entity("scanconfigs")
public class MasterScanConfigDocument extends AbstractDocument {

	@Embedded("http_client")
	private HttpClientScanConfigDocument httpClientScanConfig;

	@Embedded("scan_settings")
	private SettingScanConfigDocument settingScanConfig;

	@Embedded("report_settings")
	private ReportScanConfigDocument reportScanConfig;

	@Embedded("session_settings")
	private SessionSettingScanConfigDocument sessionScanConfig;

	@Embedded("test_modules")
	private Map<Integer, TestModuleScanConfigDocument> testModules;

	public MasterScanConfigDocument() {

		setCreatedOn(new Date());
		httpClientScanConfig = new HttpClientScanConfigDocument();
		settingScanConfig = new SettingScanConfigDocument();
		reportScanConfig = new ReportScanConfigDocument();
		sessionScanConfig = new SessionSettingScanConfigDocument();
		testModules = new HashMap<Integer, TestModuleScanConfigDocument>();

	}

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

	public HttpClientScanConfigDocument getHttpClientScanConfig() {

		return httpClientScanConfig;
	}

	public void setHttpClientScanConfig(HttpClientScanConfigDocument httpClientScanConfig) {

		this.httpClientScanConfig = httpClientScanConfig;
	}

	public SettingScanConfigDocument getSettingScanConfig() {

		return settingScanConfig;
	}

	public void setSettingScanConfig(SettingScanConfigDocument settingScanConfig) {

		this.settingScanConfig = settingScanConfig;
	}

	public ReportScanConfigDocument getReportScanConfig() {

		return reportScanConfig;
	}

	public void setReportScanConfig(ReportScanConfigDocument reportScanConfig) {

		this.reportScanConfig = reportScanConfig;
	}

	public SessionSettingScanConfigDocument getSessionScanConfig() {

		return sessionScanConfig;
	}

	public void setSessionScanConfig(SessionSettingScanConfigDocument sessionScanConfig) {

		this.sessionScanConfig = sessionScanConfig;
	}

	public Map<Integer, TestModuleScanConfigDocument> getTestModules() {

		return testModules;
	}

	public void setTestModules(Map<Integer, TestModuleScanConfigDocument> testModules) {

		this.testModules = testModules;
	}

}
