package com.nvarghese.beowulf.common.webtest.dao;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.webtest.model.ReportThreatTypeDocument;

public class ReportThreatTypeDAO extends AbstractMongoDAO<ReportThreatTypeDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(TestModuleMetaDataDAO.class);

	public ReportThreatTypeDAO(Datastore ds) {

		super(ReportThreatTypeDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public ReportThreatTypeDocument getReportThreatTypeDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public ReportThreatTypeDocument getReportThreatTypeDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

	}

	/**
	 * 
	 * @param reportThreatTypeDocument
	 */
	public void updateReportThreatTypeDocument(ReportThreatTypeDocument reportThreatTypeDocument) {

		logger.debug("Updating reportThreatTypeDocument with id: {}", reportThreatTypeDocument.getId());
		save(reportThreatTypeDocument);

	}

	/**
	 * 
	 * Create a new ReportThreatTypeDocument
	 * 
	 * @param reportThreatTypeDocument
	 */
	public ObjectId createReportThreatTypeDocument(ReportThreatTypeDocument reportThreatTypeDocument) {

		logger.debug("Creating new reportThreatTypeDocument.");
		Key<ReportThreatTypeDocument> key = save(reportThreatTypeDocument);
		return (ObjectId) key.getId();

	}

	public ReportThreatTypeDocument findByThreatSubClass(String threatSubClassName) {

		Query<ReportThreatTypeDocument> q = ds.createQuery(ReportThreatTypeDocument.class).field("wascThreatSubClass").equal(threatSubClassName);
		ReportThreatTypeDocument document = findOne(q);
		return document;
	}

	public ReportThreatTypeDocument findByThreatId(long threatTypeId) {

		Query<ReportThreatTypeDocument> q = ds.createQuery(ReportThreatTypeDocument.class).field("threatTypeId").equal(threatTypeId);
		ReportThreatTypeDocument document = findOne(q);
		return document;
	}

}
