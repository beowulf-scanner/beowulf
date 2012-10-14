package com.nvarghese.beowulf.common.webtest.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryResults;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;

public class TestModuleMetaDataDAO extends AbstractMongoDAO<TestModuleMetaDataDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(TestModuleMetaDataDAO.class);

	public TestModuleMetaDataDAO(Datastore ds) {

		super(TestModuleMetaDataDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public TestModuleMetaDataDocument getTestModuleMetaDataDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public TestModuleMetaDataDocument getTestModuleMetaDataDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

	}

	/**
	 * 
	 * @param testModuleMetaDataDocument
	 */
	public void updateTestModuleMetaDataDocument(TestModuleMetaDataDocument testModuleMetaDataDocument) {

		logger.debug("Updating testModuleMetaDataDocument with id: {}", testModuleMetaDataDocument.getId());
		save(testModuleMetaDataDocument);

	}

	/**
	 * 
	 * Create a new TestModuleMetaDataDocument
	 * 
	 * @param webScanDocument
	 */
	public ObjectId createTestModuleMetaDataDocument(TestModuleMetaDataDocument testModuleMetaDataDocument) {

		logger.debug("Creating new testModuleMetaDataDocument.");
		Key<TestModuleMetaDataDocument> key = save(testModuleMetaDataDocument);
		return (ObjectId) key.getId();

	}

	public TestModuleMetaDataDocument findByModuleNumber(long monduleNumber) {

		Query<TestModuleMetaDataDocument> q = ds.createQuery(TestModuleMetaDataDocument.class).field("moduleNumber").equal(monduleNumber);
		TestModuleMetaDataDocument document = findOne(q);
		return document;
	}

	public List<TestModuleMetaDataDocument> findByTestType(WebTestType testType) {

		Query<TestModuleMetaDataDocument> q = ds.createQuery(TestModuleMetaDataDocument.class).field("testType").equal(testType);
		QueryResults<TestModuleMetaDataDocument> results = find(q);
		// List<TestModuleMetaDataDocument> docs = find(q);
		List<TestModuleMetaDataDocument> docs = results.asList();
		return docs;
	}

}
