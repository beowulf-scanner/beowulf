package com.nvarghese.beowulf.common.webtest.scs.jobs;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;

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

}
