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
import com.nvarghese.beowulf.scs.categorizers.model.DirectoryCategorizerDocument;

/**
 * This is a single document DAO class
 * 
 * @author nibin
 * 
 */
public class DirectoryCategorizerDAO extends AbstractMongoDAO<DirectoryCategorizerDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(DirectoryCategorizerDAO.class);

	public DirectoryCategorizerDAO(Datastore ds) {

		super(DirectoryCategorizerDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public DirectoryCategorizerDocument getDirectoryCategorizerDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public DirectoryCategorizerDocument getDirectoryCategorizerDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

	}

	/**
	 * 
	 * @param testModuleMetaDataDocument
	 */
	public void updateDirectoryCategorizerDocument(DirectoryCategorizerDocument directoryCategorizerDocument) {

		logger.debug("Updating directoryCategorizerDocument with id: {}", directoryCategorizerDocument.getId());
		save(directoryCategorizerDocument);

	}

	/**
	 * 
	 * Create a new directoryCategorizerDocument
	 * 
	 * @param directoryCategorizerDocument
	 */
	public ObjectId createDirectoryCategorizerDocument(DirectoryCategorizerDocument directoryCategorizerDocument) {

		logger.debug("Creating new DirectoryCategorizerDocument.");
		Key<DirectoryCategorizerDocument> key = save(directoryCategorizerDocument);
		return (ObjectId) key.getId();

	}

	/**
	 * 
	 * @param dirName
	 * @return
	 */
	public boolean isDirectoryNamePresent(String dirName) {

		boolean present = false;

		Query<DirectoryCategorizerDocument> q = ds.createQuery(DirectoryCategorizerDocument.class).field("testedDirs").contains(dirName);
		long count = count(q);
		if (count > 0l)
			present = true;

		return present;
	}

	public void addDirectoryName(String dirName) {

		UpdateOperations<DirectoryCategorizerDocument> ops = ds.createUpdateOperations(DirectoryCategorizerDocument.class).add("tested_dirs",
				dirName, false);
		ds.update(ds.createQuery(DirectoryCategorizerDocument.class), ops);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public DirectoryCategorizerDocument getDirectoryCategorizerDocument(boolean includeTestedDirField) {

		Query<DirectoryCategorizerDocument> q = null;
		if (includeTestedDirField) {
			q = ds.createQuery(DirectoryCategorizerDocument.class).retrievedFields(true, "lastUpdated", "createdOn", "tested_dirs");
		} else {
			q = ds.createQuery(DirectoryCategorizerDocument.class).retrievedFields(true, "lastUpdated", "createdOn");
		}

		QueryResults<DirectoryCategorizerDocument> qr = find(q);
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
	public DirectoryCategorizerDocument getDirectoryCategorizerDocument() {

		return getDirectoryCategorizerDocument(true);

	}

}
