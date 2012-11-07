package com.nvarghese.beowulf.common.scan.dao;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.UpdateOperations;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;

public class WebScanDAO extends AbstractMongoDAO<WebScanDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(WebScanDAO.class);

	public WebScanDAO(Datastore ds) {

		super(WebScanDocument.class, ds);

	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public WebScanDocument getWebScanDocument(ObjectId objectId) {

		WebScanDocument webScanDocument = get(objectId);

		return webScanDocument;
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public WebScanDocument getWebScanDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		WebScanDocument webScanDocument = get(id);

		return webScanDocument;

	}

	/**
	 * 
	 * @param webScanDocument
	 */
	public void updateWebScanDocument(WebScanDocument webScanDocument) {

		logger.debug("Updating webscandocument with id: {}", webScanDocument.getId());
		save(webScanDocument);

	}

	/**
	 * 
	 * Create a new web scan document
	 * 
	 * @param webScanDocument
	 */
	public ObjectId createWebScanDocument(WebScanDocument webScanDocument) {

		logger.debug("Creating new webscandocument.");
		Key<WebScanDocument> key = save(webScanDocument);
		return (ObjectId) key.getId();

	}

	/**
	 * 
	 * @param webScanObjectId
	 * @param scanJobsInProgress
	 */
	public void updateScanJobsInProgress(ObjectId webScanObjectId, boolean scanJobsInProgress) {

		UpdateOperations<WebScanDocument> ops = ds.createUpdateOperations(WebScanDocument.class).set("scanJobsInProgress", scanJobsInProgress);
		ds.update(ds.createQuery(WebScanDocument.class).field("id").equal(webScanObjectId), ops);

	}

	/**
	 * 
	 * @param webScanObjectId
	 * @param scanRunning
	 */
	public void updateScanRunning(ObjectId webScanObjectId, boolean scanRunning) {

		UpdateOperations<WebScanDocument> ops = ds.createUpdateOperations(WebScanDocument.class).set("scanRunning", scanRunning);
		ds.update(ds.createQuery(WebScanDocument.class).field("id").equal(webScanObjectId), ops);

	}

	/**
	 * 
	 * @param webScanObjectId
	 * @param scanRunning
	 */
	public void updateScanPhase(ObjectId webScanObjectId, String scanPhase) {

		UpdateOperations<WebScanDocument> ops = ds.createUpdateOperations(WebScanDocument.class).set("scanPhase", scanPhase);
		ds.update(ds.createQuery(WebScanDocument.class).field("id").equal(webScanObjectId), ops);

	}

}
