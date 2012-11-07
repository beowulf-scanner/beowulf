package com.nvarghese.beowulf.common.scan.dao;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.scan.model.MasterScanReportDocument;

public class MasterScanReportDAO extends AbstractMongoDAO<MasterScanReportDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(MasterScanReportDAO.class);

	public MasterScanReportDAO(Datastore ds) {

		super(MasterScanReportDocument.class, ds);

	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public MasterScanReportDocument getMasterScanReportDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public MasterScanReportDocument getMasterScanReportDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return getMasterScanReportDocument(id);
	}

	/**
	 * 
	 * @param masterScanReportDocument
	 */
	public void updateMasterScanReportDocument(MasterScanReportDocument masterScanReportDocument) {

		logger.debug("Updating masterScanReportDocument with id: {}", masterScanReportDocument.getId());
		save(masterScanReportDocument);

	}

	/**
	 * 
	 * Create a new scan report document
	 * 
	 * @param masterScanReportDocument
	 */
	public ObjectId createMasterScanReportDocument(MasterScanReportDocument masterScanReportDocument) {

		logger.debug("Creating new masterScanReportDocument.");
		Key<MasterScanReportDocument> key = save(masterScanReportDocument);
		return (ObjectId) key.getId();

	}
}
