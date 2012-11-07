package com.nvarghese.beowulf.scs.categorizers.dao;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryResults;
import com.google.code.morphia.query.UpdateOperations;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.scs.categorizers.model.HostCategorizerDocument;

public class HostCategorizerDAO extends AbstractMongoDAO<HostCategorizerDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(HostCategorizerDAO.class);

	public HostCategorizerDAO(Datastore ds) {

		super(HostCategorizerDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public HostCategorizerDocument getHostCategorizerDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public HostCategorizerDocument getHostCategorizerDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

	}

	/**
	 * 
	 * @param hostCategorizerDocument
	 */
	public void updateHostCategorizerDocument(HostCategorizerDocument hostCategorizerDocument) {

		logger.debug("Updating hostCategorizerDocument with id: {}", hostCategorizerDocument.getId());
		save(hostCategorizerDocument);

	}

	/**
	 * 
	 * Create a new hostCategorizerDocument
	 * 
	 * @param hostCategorizerDocument
	 */
	public ObjectId createHostCategorizerDocument(HostCategorizerDocument hostCategorizerDocument) {

		logger.debug("Creating new HostCategorizerDocument.");
		Key<HostCategorizerDocument> key = save(hostCategorizerDocument);
		return (ObjectId) key.getId();

	}

	/**
	 * 
	 * @param hostName
	 * @return
	 */
	public boolean isHostNamePresent(String hostName) {

		boolean present = false;

		Query<HostCategorizerDocument> q = ds.createQuery(HostCategorizerDocument.class).field("hostNames").contains(hostName);
		long count = count(q);
		if (count > 0l)
			present = true;

		return present;
	}

	/**
	 * 
	 * @param hostName
	 */
	public void addHostName(String hostName) {

		UpdateOperations<HostCategorizerDocument> ops = ds.createUpdateOperations(HostCategorizerDocument.class).add("host_names", hostName, false);
		ds.update(ds.createQuery(HostCategorizerDocument.class), ops);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public HostCategorizerDocument getHostCategorizerDocument(boolean includeHostNameField) {

		Query<HostCategorizerDocument> q = null;
		if (includeHostNameField) {
			q = ds.createQuery(HostCategorizerDocument.class).retrievedFields(true, "lastUpdated", "createdOn", "host_names");
		} else {
			q = ds.createQuery(HostCategorizerDocument.class).retrievedFields(true, "lastUpdated", "createdOn");
		}

		QueryResults<HostCategorizerDocument> qr = find(q);
		if (qr.countAll() > 0) {
			return qr.get();
		} else {
			return null;
		}

	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public HostCategorizerDocument getHostCategorizerDocument() {

		return getHostCategorizerDocument(true);

	}

}
