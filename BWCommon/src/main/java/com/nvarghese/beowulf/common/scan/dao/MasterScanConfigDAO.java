package com.nvarghese.beowulf.common.scan.dao;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.scan.model.MasterScanConfigDocument;

public class MasterScanConfigDAO extends AbstractMongoDAO<MasterScanConfigDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(MasterScanConfigDAO.class);

	public MasterScanConfigDAO(Datastore ds) {

		super(MasterScanConfigDocument.class, ds);

	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public MasterScanConfigDocument getMasterScanConfigDocument(ObjectId objectId) {

		MasterScanConfigDocument scanConfigDocument = get(objectId);
		
		return scanConfigDocument;
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public MasterScanConfigDocument getMasterScanConfigDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		MasterScanConfigDocument scanConfigDocument = get(id);
		
		return scanConfigDocument;

	}

	/**
	 * 
	 * @param masterScanConfigDocument
	 */
	public void updateMasterScanConfigDocument(MasterScanConfigDocument masterScanConfigDocument) {

		logger.debug("Updating masterScanConfigDocument with id: {}", masterScanConfigDocument.getId());
		save(masterScanConfigDocument);

	}

	/**
	 * 
	 * Create a new scan config document
	 * 
	 * @param masterScanConfigDocument
	 */
	public ObjectId createMasterScanConfigDocument(MasterScanConfigDocument masterScanConfigDocument) {

		logger.debug("Creating new masterScanConfigDocument.");
		Key<MasterScanConfigDocument> key = save(masterScanConfigDocument);		
		return (ObjectId) key.getId();

	}

}
