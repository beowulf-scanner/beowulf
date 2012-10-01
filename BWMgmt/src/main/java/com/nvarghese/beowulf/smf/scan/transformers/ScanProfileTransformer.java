package com.nvarghese.beowulf.smf.scan.transformers;

import java.util.HashMap;
import java.util.Map;

import com.nvarghese.beowulf.common.report.ReportFormat;
import com.nvarghese.beowulf.common.report.Severity;
import com.nvarghese.beowulf.common.scan.dto.config.HttpClient;
import com.nvarghese.beowulf.common.scan.dto.config.Options;
import com.nvarghese.beowulf.common.scan.dto.config.ReportSettings;
import com.nvarghese.beowulf.common.scan.dto.config.ScanSettings;
import com.nvarghese.beowulf.common.scan.dto.config.SessionSettings;
import com.nvarghese.beowulf.common.scan.dto.config.TestModule;
import com.nvarghese.beowulf.common.scan.dto.config.TestModules;
import com.nvarghese.beowulf.common.scan.model.HttpClientScanConfigDocument;
import com.nvarghese.beowulf.common.scan.model.ReportScanConfigDocument;
import com.nvarghese.beowulf.common.scan.model.SessionSettingScanConfigDocument;
import com.nvarghese.beowulf.common.scan.model.SettingScanConfigDocument;
import com.nvarghese.beowulf.common.scan.model.TestModuleScanConfigDocument;

public class ScanProfileTransformer {

	public HttpClientScanConfigDocument transformToHttpClientScanConfigDocument(HttpClient httpClient) {

		HttpClientScanConfigDocument httpClientScanConfigDocument = new HttpClientScanConfigDocument();
		httpClientScanConfigDocument.setMaxRedirects(httpClient.getMaxRedirects().longValue());
		httpClientScanConfigDocument.setMaxConsecutiveFailedRequests(httpClient.getMaxConsecutiveFailedRequests()
				.longValue());
		httpClientScanConfigDocument.setMaxFailedRequestsPerServer(httpClient.getMaxFailedRequestsPerServer()
				.longValue());
		httpClientScanConfigDocument.setMaxRequestCount(httpClient.getMaxRequestCount().longValue());
		httpClientScanConfigDocument.setMaxRequestDepth(httpClient.getMaxRequestDepth().longValue());
		httpClientScanConfigDocument.setMaxRequestRetries(httpClient.getMaxRequestRetries().longValue());
		httpClientScanConfigDocument.setMaxSpriderUrls(httpClient.getMaxSpiderUrls().longValue());
		httpClientScanConfigDocument.setSocketReadTimeout(httpClient.getSocketReadTimeout().longValue());
		httpClientScanConfigDocument.setUserAgentValue(httpClient.getUserAgentString());

		return httpClientScanConfigDocument;

	}

	public SettingScanConfigDocument transformToSettingScanConfigDocument(ScanSettings scanSettings) {

		SettingScanConfigDocument settingScanDocument = new SettingScanConfigDocument();

		settingScanDocument.setResponseCodeOverideTestDirectories(scanSettings.getResponseCodeOverrides()
				.isTestAllDirectories());
		settingScanDocument.setResponseCodeOverideThreshold(scanSettings.getResponseCodeOverrides()
				.getOverrideThreshold().longValue());
		settingScanDocument.setResponseCodeOverideUseAutomatic(scanSettings.getResponseCodeOverrides()
				.isUseAutomaticOverrides());

		settingScanDocument.setBaseURIList(scanSettings.getBaseUris().getBaseUri());

		settingScanDocument.setForbiddenParamNames(scanSettings.getRestrictions().getForbiddenParameterNames()
				.getParameterName());
		settingScanDocument.setForbiddenMimeTypes(scanSettings.getRestrictions().getForbiddenMimeTypes()
				.getMimeTypePattern());
		settingScanDocument.setIrrelevantParamNames(scanSettings.getRestrictions().getIrrelevantParameterNames()
				.getParameterName());

		settingScanDocument.setUrlBlacklistPatterns(scanSettings.getRestrictions().getUrlBlacklist().getUrlPattern());
		settingScanDocument.setUrlWhitelistPatterns(scanSettings.getRestrictions().getUrlWhitelist().getUrlPattern());

		settingScanDocument.setImportedSpideredURIList(scanSettings.getImportSpideredUris().getUri());

		return settingScanDocument;

	}

	public ReportScanConfigDocument transformToReportScanConfigDocument(ReportSettings reportSettings) {

		ReportScanConfigDocument reportScanConfigDocument = new ReportScanConfigDocument();
		reportScanConfigDocument.setFileName(reportSettings.getFilename());
		reportScanConfigDocument.setMinimumSeverity(Severity.getSeverity(reportSettings.getMinSeverity()));
		reportScanConfigDocument.setReportFormat(ReportFormat.getReportFormat(reportSettings.getFormat()));
		reportScanConfigDocument.setAutoReportGeneration(reportSettings.isAutoReportGeneration());

		return reportScanConfigDocument;
	}

	public SessionSettingScanConfigDocument transformToSessionSettingScanConfigDocument(SessionSettings sessionSettings) {

		SessionSettingScanConfigDocument sessionConfigDocument = new SessionSettingScanConfigDocument();
		sessionConfigDocument.setUserName(sessionSettings.getLogin().getUsername());
		sessionConfigDocument.setPassword(sessionSettings.getLogin().getPassword());
		sessionConfigDocument.setUsernameFieldPattern(sessionSettings.getLogin().getUsernameFieldPattern());
		sessionConfigDocument.setPasswordFieldPattern(sessionSettings.getLogin().getPasswordFieldPattern());
		sessionConfigDocument.setKnownSessionIdPatterns(sessionSettings.getSessionTracking().getKnownSessionIdlist()
				.getIdPattern());

		return sessionConfigDocument;

	}

	public Map<Long, TestModuleScanConfigDocument> transformToMappedTestModuleScanConfigDocument(TestModules testModules) {

		Map<Long, TestModuleScanConfigDocument> testModuleMap = new HashMap<Long, TestModuleScanConfigDocument>();

		for (TestModule module : testModules.getTestModule()) {
			TestModuleScanConfigDocument testModuleScanConfigDocument = new TestModuleScanConfigDocument();
			testModuleScanConfigDocument.setModuleName(module.getModuleName());
			testModuleScanConfigDocument.setModuleNumber(module.getModuleNumber().longValue());
			testModuleScanConfigDocument.setEnabled(module.isEnabled());
			Map<String, String> options = new HashMap<String, String>();
			for (Options option : module.getOptions()) {
				options.put(option.getOptionName(), option.getOptionValue());
			}
			testModuleScanConfigDocument.setOptions(options);

			testModuleMap.put(testModuleScanConfigDocument.getModuleNumber(), testModuleScanConfigDocument);
		}

		return testModuleMap;
	}
}
