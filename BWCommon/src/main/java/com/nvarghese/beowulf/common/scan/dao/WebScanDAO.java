package com.nvarghese.beowulf.common.scan.dao;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;

public class WebScanDAO extends AbstractMongoDAO<WebScanDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(WebScanDAO.class);

	public WebScanDAO(Class<WebScanDocument> entityClass, Datastore ds) {

		super(entityClass, ds);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public WebScanDocument getWebScanDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public WebScanDocument getWebScanDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

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

}
