package com.nvarghese.beowulf.common.scan.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Key;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.QueryResults;
import com.google.code.morphia.query.UpdateOperations;
import com.nvarghese.beowulf.common.dao.AbstractMongoDAO;
import com.nvarghese.beowulf.common.scan.model.ReportIssueDocument;
import com.nvarghese.beowulf.common.scan.model.ReportIssueVariantDocument;
import com.nvarghese.beowulf.common.webtest.JobStatus;
import com.nvarghese.beowulf.common.webtest.ReportThreatType;
import com.nvarghese.beowulf.common.webtest.ThreatSeverityType;
import com.nvarghese.beowulf.common.webtest.scs.jobs.CategorizationJobDocument;

public class ReportIssueDAO extends AbstractMongoDAO<ReportIssueDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(ReportIssueDAO.class);

	public ReportIssueDAO(Datastore ds) {

		super(ReportIssueDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public ReportIssueDocument getReportIssueDocument(ObjectId objectId, boolean includeReportIssueVariants) {

		Query<ReportIssueDocument> q = null;
		if (includeReportIssueVariants) {
			return get(objectId);
		} else {
			q = ds.createQuery(ReportIssueDocument.class).retrievedFields(false, "issue_variants");
			q.filter("id", objectId);
		}

		QueryResults<ReportIssueDocument> qr = find(q);
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
	public ReportIssueDocument getReportIssueDocument(ObjectId objectId) {

		return getReportIssueDocument(objectId, false);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public ReportIssueDocument getReportIssueDocument(String objectId, boolean includeReportIssueVariants) {

		ObjectId id = new ObjectId(objectId);
		return getReportIssueDocument(id, includeReportIssueVariants);

	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public ReportIssueDocument getReportIssueDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return getReportIssueDocument(id);

	}

	/**
	 * 
	 * @param reportIssueDocument
	 */
	public void updateReportIssueDocument(ReportIssueDocument reportIssueDocument) {

		logger.debug("Updating reportIssueDocument with id: {}", reportIssueDocument.getId());
		save(reportIssueDocument);

	}

	/**
	 * 
	 * Create a new reportIssueDocument
	 * 
	 * @param reportIssueDocument
	 */
	public ObjectId createReportIssueDocument(ReportIssueDocument reportIssueDocument) {

		logger.debug("Creating new reportIssueDocument.");
		Key<ReportIssueDocument> key = save(reportIssueDocument);
		return (ObjectId) key.getId();

	}

	/**
	 * 
	 * @param url
	 * @param threatType
	 * @param moduleNumber
	 * @return
	 */
	public ReportIssueDocument findByUrlAndThreatTypeAndModuleNumber(String issueUrl, ReportThreatType threatType, long moduleNumber,
			boolean includeReportIssueVariants) {

		logger.debug("Querying to find ReportIssueDocument based on url, threat type and module number");

		Query<ReportIssueDocument> q = ds.createQuery(ReportIssueDocument.class);
		q.and(q.criteria("threatType").equal(threatType), q.criteria("issueUrl").equal(issueUrl), q.criteria("moduleNumber").equal(moduleNumber));

		if (includeReportIssueVariants) {
			return findOne(q);
		} else {
			q = q.retrievedFields(false, "issue_variants");
			return findOne(q);
		}
	}

	/**
	 * 
	 * @param threatType
	 * @param includeReportIssueVariants
	 * @return
	 */
	public List<ReportIssueDocument> findByThreatType(ReportThreatType threatType, boolean includeReportIssueVariants) {

		logger.debug("Querying to find ReportIssueDocument based on threat type");

		Query<ReportIssueDocument> q = ds.createQuery(ReportIssueDocument.class);
		q.and(q.criteria("threatType").equal(threatType));

		if (includeReportIssueVariants) {
			return find(q).asList();
		} else {
			q = q.retrievedFields(false, "issue_variants");
			return find(q).asList();
		}
	}

	/**
	 * 
	 * @param threatType
	 * @param includeReportIssueVariants
	 * @return
	 */
	public ReportIssueDocument findOneByThreatType(ReportThreatType threatType, boolean includeReportIssueVariants) {

		logger.debug("Querying to find ReportIssueDocument based on threat type");

		Query<ReportIssueDocument> q = ds.createQuery(ReportIssueDocument.class);
		q.and(q.criteria("threatType").equal(threatType));

		if (includeReportIssueVariants) {
			return findOne(q);
		} else {
			q = q.retrievedFields(false, "issue_variants");
			return findOne(q);
		}
	}

	/**
	 * 
	 * @param reportIssueObjId
	 * @param issueVariantDocument
	 */
	public void addReportIssueVariants(ObjectId reportIssueObjId, ReportIssueVariantDocument issueVariantDocument) {

		UpdateOperations<ReportIssueDocument> ops = ds.createUpdateOperations(ReportIssueDocument.class).add("issue_variants", issueVariantDocument,
				false);
		ds.update(ds.createQuery(ReportIssueDocument.class).filter("id", reportIssueObjId), ops);

	}

	/**
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<ReportIssueDocument> getReportIssueDocuments(int offset, int limit) {

		Query<ReportIssueDocument> q = ds.createQuery(ReportIssueDocument.class).offset(offset).limit(limit);

		QueryResults<ReportIssueDocument> qr = find(q);

		return qr.asList();
	}
	
	/**
	 * 
	 * @param severityType
	 * @return
	 */
	public long getCountOfIssuesByThreatSeverityType(ThreatSeverityType severityType) {
		
		Query<ReportIssueDocument> q = ds.createQuery(ReportIssueDocument.class).field("threatSeverityType").equal(severityType);
		long count = count(q);

		return count;
	}
}
