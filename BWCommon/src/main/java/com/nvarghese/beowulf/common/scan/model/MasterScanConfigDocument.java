package com.nvarghese.beowulf.common.scan.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Transient;
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
	private Map<Long, TestModuleScanConfigDocument> testModules;

	@Transient
	private Map<Long, TestModuleScanConfigDocument> enabledTestModules;

	public MasterScanConfigDocument() {

		setCreatedOn(new Date());
		httpClientScanConfig = new HttpClientScanConfigDocument();
		settingScanConfig = new SettingScanConfigDocument();
		reportScanConfig = new ReportScanConfigDocument();
		sessionScanConfig = new SessionSettingScanConfigDocument();
		testModules = new HashMap<Long, TestModuleScanConfigDocument>();
		enabledTestModules = new HashMap<Long, TestModuleScanConfigDocument>();

	}

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

//	@PostPersist
//	void postPersist() {
//
//		loadEnabledTestModules();
//	}
//
//	@PostLoad
//	void postLoad() {
//
//		loadEnabledTestModules();
//
//	}

	public void loadEnabledTestModules() {

		for (TestModuleScanConfigDocument testModuleConfigDoc : testModules.values()) {

			if (testModuleConfigDoc.isEnabled()) {
				enabledTestModules.put(testModuleConfigDoc.getModuleNumber(), testModuleConfigDoc);
			}
		}

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

	public Map<Long, TestModuleScanConfigDocument> getTestModules() {

		return testModules;
	}

	public void setTestModules(Map<Long, TestModuleScanConfigDocument> testModules) {

		this.testModules = testModules;
	}

	public Map<Long, TestModuleScanConfigDocument> getEnabledTestModules() {

		return enabledTestModules;
	}

}
