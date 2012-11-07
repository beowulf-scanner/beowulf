package com.nvarghese.beowulf.common.scan.dao;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.scan.model.ReportHostDocument;

public class ReportHostDAO extends AbstractMongoDAO<ReportHostDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(ReportHostDAO.class);

	public ReportHostDAO(Datastore ds) {

		super(ReportHostDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public ReportHostDocument getReportHostDocument(ObjectId objectId) {

		ReportHostDocument reportHostDocument = get(objectId);

		return reportHostDocument;
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public ReportHostDocument getReportHostDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return getReportHostDocument(id);

	}

	/**
	 * 
	 * @param reportHostDocument
	 */
	public void updateReportHostDocument(ReportHostDocument reportHostDocument) {

		logger.debug("Updating reportHostDocument with id: {}", reportHostDocument.getId());
		save(reportHostDocument);

	}

	/**
	 * 
	 * Create a new reportHostDocument
	 * 
	 * @param reportHostDocument
	 */
	public ObjectId createReportHostDocument(ReportHostDocument reportHostDocument) {

		logger.debug("Creating new reportHostDocument.");
		Key<ReportHostDocument> key = save(reportHostDocument);
		return (ObjectId) key.getId();

	}

}
