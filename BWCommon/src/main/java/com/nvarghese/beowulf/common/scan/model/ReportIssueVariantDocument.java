package com.nvarghese.beowulf.common.scan.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

@Embedded
public class ReportIssueVariantDocument {

	private Date createdOn;

	@Property("description")
	private String description;

	@Property("original_txn")
	private ObjectId origicalTxn;

	@Property("test_txn")
	private ObjectId testTxn;

	public ReportIssueVariantDocument() {

		createdOn = new Date();
	}

	public Date getCreatedOn() {

		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {

		this.createdOn = createdOn;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public ObjectId getOrigicalTxn() {

		return origicalTxn;
	}

	public void setOrigicalTxn(ObjectId origicalTxn) {

		this.origicalTxn = origicalTxn;
	}

	public ObjectId getTestTxn() {

		return testTxn;
	}

	public void setTestTxn(ObjectId testTxn) {

		this.testTxn = testTxn;
	}

}
