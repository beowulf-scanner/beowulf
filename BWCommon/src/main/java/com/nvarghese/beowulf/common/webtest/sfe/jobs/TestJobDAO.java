package com.nvarghese.beowulf.common.webtest.sfe.jobs;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;

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

}
