package com.nvarghese.beowulf.common.http.txn;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;

public class HttpTxnDAO extends AbstractMongoDAO<HttpTxnDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(HttpTxnDAO.class);

	public HttpTxnDAO(Datastore ds) {

		super(HttpTxnDocument.class, ds);

	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public HttpTxnDocument getHttpTxnDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public HttpTxnDocument getHttpTxnDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

	}

	/**
	 * 
	 * @param httpTxnDocument
	 */
	public void updateHttpTxnDocument(HttpTxnDocument httpTxnDocument) {

		logger.debug("Updating httpTxnDocument with id: {}", httpTxnDocument.getId());
		save(httpTxnDocument);

	}

	/**
	 * 
	 * Create a new HttpTxnDocument
	 * 
	 * @param httpTxnDocument
	 */
	public ObjectId createHttpTxnDocument(HttpTxnDocument httpTxnDocument) {

		logger.debug("Creating new httpTxnDocument.");
		Key<HttpTxnDocument> key = save(httpTxnDocument);
		return (ObjectId) key.getId();

	}

}
