package com.nvarghese.beowulf.common.scan.model;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class SettingScanConfigDocument {

	private long responseCodeOverideThreshold;
	private boolean responseCodeOverideTestDirectories;
	private boolean responseCodeOverideUseAutomatic;

	private List<String> baseURIList;

	private List<String> forbiddenParamNames;

	private List<String> irrelevantParamNames;

	private List<String> forbiddenMimeTypes;

	private List<String> urlBlacklistPatterns;

	private List<String> urlWhiltelistPatterns;

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

	public List<String> getUrlWhiltelistPatterns() {

		return urlWhiltelistPatterns;
	}

	public void setUrlWhiltelistPatterns(List<String> urlWhiltelistPatterns) {

		this.urlWhiltelistPatterns = urlWhiltelistPatterns;
	}

	public List<String> getImportedSpideredURIList() {

		return importedSpideredURIList;
	}

	public void setImportedSpideredURIList(List<String> importedSpideredURIList) {

		this.importedSpideredURIList = importedSpideredURIList;
	}

}
