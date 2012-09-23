package com.nvarghese.beowulf.common.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Transient;

public abstract class AbstractDocument {

	@Id
	protected ObjectId id;

	@Transient
	protected boolean saved;

	@Transient
	protected int retries;

	/* life cycle management */
	protected Date lastUpdated;
	protected Date createdOn;

	public AbstractDocument() {

		saved = false;
		retries = 0;
	}

	public ObjectId getId() {

		return id;
	}

	public void setId(final ObjectId id) {

		this.id = id;
	}

	public Date getLastUpdated() {

		return lastUpdated;
	}

	public void setLastUpdated(final Date lastUpdated) {

		this.lastUpdated = lastUpdated;
	}

	public Date getCreatedOn() {

		return createdOn;
	}

	public void setCreatedOn(final Date createdOn) {

		this.createdOn = createdOn;
	}

	public boolean isSaved() {

		return saved;
	}

	public void setSaved(final boolean isSaved) {

		this.saved = isSaved;
	}

	public int getRetries() {

		return retries;
	}

	public void setRetries(final int retries) {

		this.retries = retries;
	}

}
