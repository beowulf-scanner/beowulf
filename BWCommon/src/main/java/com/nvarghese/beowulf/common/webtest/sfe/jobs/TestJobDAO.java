package com.nvarghese.beowulf.common.webtest.sfe.jobs;

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
import com.nvarghese.beowulf.common.webtest.scs.jobs.CategorizationJobDocument;

public class TestJobDAO extends AbstractMongoDAO<TestJobDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(TestJobDAO.class);

	public TestJobDAO(Datastore ds) {

		super(TestJobDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public TestJobDocument getTestJobDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public TestJobDocument getTestJobDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

	}

	/**
	 * 
	 * @param testJobDocument
	 */
	public void updateTestJobDocument(TestJobDocument testJobDocument) {

		logger.debug("Updating testJobDocument with id: {}", testJobDocument.getId());
		save(testJobDocument);

	}

	/**
	 * 
	 * Create a new testJobDocument
	 * 
	 * @param testJobDocument
	 */
	public ObjectId createTestJobDocument(TestJobDocument testJobDocument) {

		logger.debug("Creating new testJobDocument.");
		Key<TestJobDocument> key = save(testJobDocument);
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

		Query<TestJobDocument> q = ds.createQuery(TestJobDocument.class).field("jobStatus").in(jobStatus);
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
		Query<TestJobDocument> q = ds.createQuery(TestJobDocument.class).field("jobStatus").in(jobStatus);
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

		Query<TestJobDocument> q = ds.createQuery(TestJobDocument.class).field("jobStatus").in(jobStatus);
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

		Query<TestJobDocument> q = ds.createQuery(TestJobDocument.class).field("jobStatus").in(jobStatus);
		long count = count(q);

		return count;
	}

	/**
	 * 
	 * @return
	 */
	public long getCountOfAllJobs() {

		Query<TestJobDocument> q = ds.createQuery(TestJobDocument.class);
		long count = count(q);

		return count;

	}
}
