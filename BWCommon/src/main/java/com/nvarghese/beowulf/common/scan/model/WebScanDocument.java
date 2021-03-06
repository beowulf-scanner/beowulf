package com.nvarghese.beowulf.common.scan.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Transient;
import com.nvarghese.beowulf.common.model.AbstractDocument;

@Entity("webscans")
public class WebScanDocument extends AbstractDocument {

	private String bwControllerIPAddress;
	private int bwControllerPort;

	/* Scan status copied from masterscan status */
	private String scanPhase;
	private String restoreScanPhase;

	private boolean scanRunning;
	private boolean scanJobsInProgress;

	private boolean abortRequested;
	private String abortingReason;
	private int abortReasonId;

	private boolean pauseRequested;
	private String pausingReason;
	private int pauseReasonId;

	private boolean resumeRequested;
	private String resumeReason;
	private int resumeReasonId;

	private List<String> baseUris;

	private Date estimatedTime;
	private Date scanStartTime;
	private Date scanEndTime;

	@Reference
	private MasterScanConfigDocument scanConfig;
	
	@Reference
	private MasterScanReportDocument scanReport;

	private long createdCategorizationJobs;
	private long completedCategorizationJobs;
	private long erroredCategorizationJobs;
	private long pendingCategorizationJobs;

	private long createdExecutionJobs;
	private long completedExecutionJobs;
	private long erroredExecutionJobs;
	private long pendingExecutionJobs;

	private int percentageDone;

	private String txnDbName;

	// @Reference
	// private ScanReportDocument reportDocument;

	private List<String> comments;
	private String lastError;

	@Transient
	static Logger logger = LoggerFactory.getLogger(WebScanDocument.class);

	public WebScanDocument() {

		super();

		setCreatedOn(new Date());
		comments = new ArrayList<String>();
		baseUris = new ArrayList<String>();
		txnDbName = "";
		abortRequested = false;
		scanRunning = false;
		scanJobsInProgress = false;
		scanPhase = "";
		scanStartTime = new Date(0);
		scanEndTime = new Date(0);
		estimatedTime = new Date(0);

		createdCategorizationJobs = 0;
		completedCategorizationJobs = 0;
		erroredCategorizationJobs = 0;
		pendingCategorizationJobs = 0;

		createdExecutionJobs = 0;
		completedExecutionJobs = 0;
		erroredExecutionJobs = 0;
		pendingExecutionJobs = 0;

		percentageDone = 0;
		lastError = "";
		setAbortingReason("");

	}

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

	public String getBwControllerIPAddress() {

		return bwControllerIPAddress;
	}

	public void setBwControllerIPAddress(String bwControllerIPAddress) {

		this.bwControllerIPAddress = bwControllerIPAddress;
	}

	public int getBwControllerPort() {

		return bwControllerPort;
	}

	public void setBwControllerPort(int bwControllerPort) {

		this.bwControllerPort = bwControllerPort;
	}

	public boolean isAbortRequested() {

		return abortRequested;
	}

	public void setAbortRequested(boolean isAbortRequested) {

		this.abortRequested = isAbortRequested;
	}

	public int getAbortReasonId() {

		return abortReasonId;
	}

	public void setAbortReasonId(int abortReasonId) {

		this.abortReasonId = abortReasonId;
	}

	public boolean isScanRunning() {

		return scanRunning;
	}

	public boolean isResumeRequested() {

		return resumeRequested;
	}

	public void setResumeRequested(boolean resumeRequested) {

		this.resumeRequested = resumeRequested;
	}

	public String getResumeReason() {

		return resumeReason;
	}

	public void setResumeReason(String resumeReason) {

		this.resumeReason = resumeReason;
	}

	public int getResumeReasonId() {

		return resumeReasonId;
	}

	public void setResumeReasonId(int resumeReasonId) {

		this.resumeReasonId = resumeReasonId;
	}

	public void setScanRunning(boolean scanRunning) {

		this.scanRunning = scanRunning;
	}

	public boolean isScanJobsInProgress() {

		return scanJobsInProgress;
	}

	public void setScanJobsInProgress(boolean scanJobsInProgress) {

		this.scanJobsInProgress = scanJobsInProgress;
	}

	public String getRestoreScanPhase() {

		return restoreScanPhase;
	}

	public void setRestoreScanPhase(String restoreScanPhase) {

		this.restoreScanPhase = restoreScanPhase;
	}

	public String getScanPhase() {

		return scanPhase;
	}

	public void setScanPhase(String scanPhase) {

		this.scanPhase = scanPhase;
	}

	public Date getEstimatedTime() {

		return estimatedTime;
	}

	public void setEstimatedTime(Date estimatedTime) {

		this.estimatedTime = estimatedTime;
	}

	public Date getScanStartTime() {

		return scanStartTime;
	}

	public void setScanStartTime(Date scanStartTime) {

		this.scanStartTime = scanStartTime;
	}

	public Date getScanEndTime() {

		return scanEndTime;
	}

	public void setScanEndTime(Date scanEndTime) {

		this.scanEndTime = scanEndTime;
	}

	public MasterScanConfigDocument getScanConfig() {

		return scanConfig;
	}

	public void setScanConfig(MasterScanConfigDocument scanConfig) {

		this.scanConfig = scanConfig;
	}

	
	public MasterScanReportDocument getScanReport() {
	
		return scanReport;
	}

	
	public void setScanReport(MasterScanReportDocument scanReport) {
	
		this.scanReport = scanReport;
	}

	public long getCreatedCategorizationJobs() {

		return createdCategorizationJobs;
	}

	public void setCreatedCategorizationJobs(long createdCategorizationJobs) {

		this.createdCategorizationJobs = createdCategorizationJobs;
	}

	public long getCompletedCategorizationJobs() {

		return completedCategorizationJobs;
	}

	public void setCompletedCategorizationJobs(long completedCategorizationJobs) {

		this.completedCategorizationJobs = completedCategorizationJobs;
	}

	public long getErroredCategorizationJobs() {

		return erroredCategorizationJobs;
	}

	public void setErroredCategorizationJobs(long erroredCategorizationJobs) {

		this.erroredCategorizationJobs = erroredCategorizationJobs;
	}

	public long getPendingCategorizationJobs() {

		return pendingCategorizationJobs;
	}

	public void setPendingCategorizationJobs(long pendingCategorizationJobs) {

		this.pendingCategorizationJobs = pendingCategorizationJobs;
	}

	public long getCreatedExecutionJobs() {

		return createdExecutionJobs;
	}

	public void setCreatedExecutionJobs(long createdExecutionJobs) {

		this.createdExecutionJobs = createdExecutionJobs;
	}

	public long getCompletedExecutionJobs() {

		return completedExecutionJobs;
	}

	public void setCompletedExecutionJobs(long completedExecutionJobs) {

		this.completedExecutionJobs = completedExecutionJobs;
	}

	public long getErroredExecutionJobs() {

		return erroredExecutionJobs;
	}

	public void setErroredExecutionJobs(long erroredExecutionJobs) {

		this.erroredExecutionJobs = erroredExecutionJobs;
	}

	public long getPendingExecutionJobs() {

		return pendingExecutionJobs;
	}

	public void setPendingExecutionJobs(long pendingExecutionJobs) {

		this.pendingExecutionJobs = pendingExecutionJobs;
	}

	public int getPercentageDone() {

		return percentageDone;
	}

	public void setPercentageDone(int percentageDone) {

		this.percentageDone = percentageDone;
	}

	public List<String> getComments() {

		return comments;
	}

	public void setComments(List<String> logs) {

		this.comments = logs;
	}

	public String getLastError() {

		return lastError;
	}

	public void setLastError(String lastError) {

		this.lastError = lastError;
	}

	public List<String> getBaseUris() {

		return baseUris;
	}

	public void setBaseUris(List<String> baseUris) {

		this.baseUris = baseUris;
	}

	public void addComment(String message) {

		comments.add(new Date() + ": " + message);
	}

	public void setAbortingReason(String abortingReason) {

		this.abortingReason = abortingReason;
	}

	public String getAbortingReason() {

		return abortingReason;
	}

	public void setPauseRequested(boolean pauseRequested) {

		this.pauseRequested = pauseRequested;
	}

	public boolean isPauseRequested() {

		return pauseRequested;
	}

	public void setPausingReason(String pausingReason) {

		this.pausingReason = pausingReason;
	}

	public String getPausingReason() {

		return pausingReason;
	}

	public void setPauseReasonId(int pauseReasonId) {

		this.pauseReasonId = pauseReasonId;
	}

	public int getPauseReasonId() {

		return pauseReasonId;
	}

	public String getTxnDbName() {

		return txnDbName;
	}

	public void setTxnDbName(String txnDbName) {

		this.txnDbName = txnDbName;
	}

}
