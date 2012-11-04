package com.nvarghese.beowulf.common.webtest.scs.jobs;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.webtest.JobStatus;

public class CategorizationJobDAO extends AbstractMongoDAO<CategorizationJobDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(CategorizationJobDAO.class);

	public CategorizationJobDAO(Datastore ds) {

		super(CategorizationJobDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public CategorizationJobDocument getCategorizationJobDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public CategorizationJobDocument getCategorizationJobDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

	}

	/**
	 * 
	 * @param categorizationJobDocument
	 */
	public void updateCategorizationJobDocument(CategorizationJobDocument categorizationJobDocument) {

		logger.debug("Updating testModuleMetaDataDocument with id: {}", categorizationJobDocument.getId());
		save(categorizationJobDocument);

	}

	/**
	 * 
	 * Create a new categorizationJobDocument
	 * 
	 * @param categorizationJobDocument
	 */
	public ObjectId createCategorizationJobDocument(CategorizationJobDocument categorizationJobDocument) {

		logger.debug("Creating new categorizationJobDocument.");
		Key<CategorizationJobDocument> key = save(categorizationJobDocument);
		return (ObjectId) key.getId();

	}

	/**
	 * 
	 * @return
	 */
	public boolean isInProgressJobsPresent() {

		boolean present = true;

		List<JobStatus> jobStatus = new ArrayList<JobStatus>();
		jobStatus.add(JobStatus.INIT);
		jobStatus.add(JobStatus.PROCESSING);
		jobStatus.add(JobStatus.WAITING);

		Query<CategorizationJobDocument> q = ds.createQuery(CategorizationJobDocument.class).field("jobStatus").in(jobStatus);
		long count = count(q);
		if (count > 0) {
			present = true;
		} else {
			present = false;
		}
		return present;

	}

	/**
	 * 
	 * @return
	 */
	public long getCountOfCompletedJobs() {

		List<JobStatus> jobStatus = new ArrayList<JobStatus>();
		jobStatus.add(JobStatus.COMPLETED);
		Query<CategorizationJobDocument> q = ds.createQuery(CategorizationJobDocument.class).field("jobStatus").in(jobStatus);
		long count = count(q);

		return count;

	}

	/**
	 * 
	 * @return
	 */
	public long getCountOfInProgressJobs() {

		List<JobStatus> jobStatus = new ArrayList<JobStatus>();
		jobStatus.add(JobStatus.INIT);
		jobStatus.add(JobStatus.PROCESSING);
		jobStatus.add(JobStatus.WAITING);

		Query<CategorizationJobDocument> q = ds.createQuery(CategorizationJobDocument.class).field("jobStatus").in(jobStatus);
		long count = count(q);

		return count;
	}

	/**
	 * 
	 * @return
	 */
	public long getCountOfErrorOrTerminatedJobs() {

		List<JobStatus> jobStatus = new ArrayList<JobStatus>();
		jobStatus.add(JobStatus.ERROR);
		jobStatus.add(JobStatus.TERMINATED);

		Query<CategorizationJobDocument> q = ds.createQuery(CategorizationJobDocument.class).field("jobStatus").in(jobStatus);
		long count = count(q);

		return count;
	}

}
