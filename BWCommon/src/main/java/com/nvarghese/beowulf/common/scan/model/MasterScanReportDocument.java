package com.nvarghese.beowulf.common.scan.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.nvarghese.beowulf.common.model.AbstractDocument;
import com.nvarghese.beowulf.common.webtest.ReportPhase;

@Entity("scanreports")
public class MasterScanReportDocument extends AbstractDocument {

	private ObjectId reportFileId;
	private String reportPhase;
	private int reportGenReasonId;
	private String reportGenReasonStr;
	private Set<ObjectId> oldReportFileIds;

	private List<String> comments;
	private String lastError;

	public MasterScanReportDocument() {

		super();
		id = new ObjectId();
		oldReportFileIds = new HashSet<ObjectId>();
		reportPhase = ReportPhase.NOT_STARTED.getName();
		comments = new ArrayList<String>();
	}

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

	public ObjectId getReportFileId() {

		return reportFileId;
	}

	public void setReportFileId(ObjectId reportFileId) {

		this.reportFileId = reportFileId;
	}

	public String getReportPhase() {

		return reportPhase;
	}

	public void setReportPhase(String reportPhase) {

		this.reportPhase = reportPhase;
	}

	public int getReportGenReasonId() {

		return reportGenReasonId;
	}

	public String getReportGenReasonStr() {

		return reportGenReasonStr;
	}

	public List<String> getOldReportFileIdsAsListStr() {

		List<String> fileIds = new ArrayList<String>();
		/* avoids side effects of modification */
		List<ObjectId> ids = new ArrayList<ObjectId>(oldReportFileIds);

		for (ObjectId id : ids) {
			fileIds.add(id.toString());
		}
		return fileIds;
	}

	public void setReportGenReasonId(int reportGenReasonId) {

		this.reportGenReasonId = reportGenReasonId;
	}

	public void setReportGenReasonStr(String reportGenReasonStr) {

		this.reportGenReasonStr = reportGenReasonStr;
	}

	public void addComment(String message) {

		comments.add(new Date() + ": " + message);
	}

	public List<String> getComments() {

		return comments;
	}

	public String getLastError() {

		return lastError;
	}

	public void setLastError(String lastError) {

		this.lastError = lastError;
	}

}
