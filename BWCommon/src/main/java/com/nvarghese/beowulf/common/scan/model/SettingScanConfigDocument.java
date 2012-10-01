package com.nvarghese.beowulf.common.scan.model;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

@Embedded
public class SettingScanConfigDocument {

	@Property("response_code_override_threshold")
	private long responseCodeOverideThreshold;

	@Property("response_code_override_test_directories")
	private boolean responseCodeOverideTestDirectories;

	@Property("response_code_override_use_automatic")
	private boolean responseCodeOverideUseAutomatic;

	@Property("base_uris")
	private List<String> baseURIList;

	@Property("forbidden_param_names")
	private List<String> forbiddenParamNames;

	@Property("irrelevant_param_names")
	private List<String> irrelevantParamNames;

	@Property("forbidden_mime_types")
	private List<String> forbiddenMimeTypes;

	@Property("url_blacklist_patterns")
	private List<String> urlBlacklistPatterns;

	@Property("url_whitelist_patterns")
	private List<String> urlWhitelistPatterns;

	@Property("imported_spidered_uri_list")
	private List<String> importedSpideredURIList;

	public long getResponseCodeOverideThreshold() {

		return responseCodeOverideThreshold;
	}

	public void setResponseCodeOverideThreshold(long responseCodeOverideThreshold) {

		this.responseCodeOverideThreshold = responseCodeOverideThreshold;
	}

	public boolean isResponseCodeOverideTestDirectories() {

		return responseCodeOverideTestDirectories;
	}

	public void setResponseCodeOverideTestDirectories(boolean responseCodeOverideTestDirectories) {

		this.responseCodeOverideTestDirectories = responseCodeOverideTestDirectories;
	}

	public boolean isResponseCodeOverideUseAutomatic() {

		return responseCodeOverideUseAutomatic;
	}

	public void setResponseCodeOverideUseAutomatic(boolean responseCodeOverideUseAutomatic) {

		this.responseCodeOverideUseAutomatic = responseCodeOverideUseAutomatic;
	}

	public List<String> getBaseURIList() {

		return baseURIList;
	}

	public void setBaseURIList(List<String> baseURIList) {

		this.baseURIList = baseURIList;
	}

	public List<String> getForbiddenParamNames() {

		return forbiddenParamNames;
	}

	public void setForbiddenParamNames(List<String> forbiddenParamNames) {

		this.forbiddenParamNames = forbiddenParamNames;
	}

	public List<String> getIrrelevantParamNames() {

		return irrelevantParamNames;
	}

	public void setIrrelevantParamNames(List<String> irrelevantParamNames) {

		this.irrelevantParamNames = irrelevantParamNames;
	}

	public List<String> getForbiddenMimeTypes() {

		return forbiddenMimeTypes;
	}

	public void setForbiddenMimeTypes(List<String> forbiddenMimeTypes) {

		this.forbiddenMimeTypes = forbiddenMimeTypes;
	}

	public List<String> getUrlBlacklistPatterns() {

		return urlBlacklistPatterns;
	}

	public void setUrlBlacklistPatterns(List<String> urlBlacklistPatterns) {

		this.urlBlacklistPatterns = urlBlacklistPatterns;
	}

	public List<String> getUrlWhitelistPatterns() {

		return urlWhitelistPatterns;
	}

	public void setUrlWhitelistPatterns(List<String> urlWhitelistPatterns) {

		this.urlWhitelistPatterns = urlWhitelistPatterns;
	}

	public List<String> getImportedSpideredURIList() {

		return importedSpideredURIList;
	}

	public void setImportedSpideredURIList(List<String> importedSpideredURIList) {

		this.importedSpideredURIList = importedSpideredURIList;
	}

}
