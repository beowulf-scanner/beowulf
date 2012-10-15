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
import com.nvarghese.beowulf.scs.categorizers.model.TokenSingleSetTransactionCategorizerDocument;

public class TokenSingleSetTransactionCategorizerDAO extends AbstractMongoDAO<TokenSingleSetTransactionCategorizerDocument, ObjectId> {

	static Logger logger = LoggerFactory.getLogger(TokenSingleSetTransactionCategorizerDAO.class);

	public TokenSingleSetTransactionCategorizerDAO(Datastore ds) {

		super(TokenSingleSetTransactionCategorizerDocument.class, ds);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public TokenSingleSetTransactionCategorizerDocument getTokenSingleSetTransactionCategorizerDocument(ObjectId objectId) {

		return get(objectId);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public TokenSingleSetTransactionCategorizerDocument getTokenSingleSetTransactionCategorizerDocument(String objectId) {

		ObjectId id = new ObjectId(objectId);
		return get(id);

	}

	/**
	 * 
	 * @param tokenSingleSetTransactionCategorizerDocument
	 */
	public void updateTokenSingleSetTransactionCategorizerDocument(
			TokenSingleSetTransactionCategorizerDocument tokenSingleSetTransactionCategorizerDocument) {

		logger.debug("Updating tokenSingleSetTransactionCategorizerDocument with id: {}", tokenSingleSetTransactionCategorizerDocument.getId());
		save(tokenSingleSetTransactionCategorizerDocument);

	}

	/**
	 * 
	 * Create a new tokenSingleSetTransactionCategorizerDocument
	 * 
	 * @param directoryCategorizerDocument
	 */
	public ObjectId createTokenSingleSetTransactionCategorizerDocument(
			TokenSingleSetTransactionCategorizerDocument tokenSingleSetTransactionCategorizerDocument) {

		logger.debug("Creating new tokenSingleSetTransactionCategorizerDocument.");
		Key<TokenSingleSetTransactionCategorizerDocument> key = save(tokenSingleSetTransactionCategorizerDocument);
		return (ObjectId) key.getId();

	}

	/**
	 * 
	 * @param dirName
	 * @return
	 */
	public boolean isTokenHashPresent(String tokenHash) {

		boolean present = false;

		Query<TokenSingleSetTransactionCategorizerDocument> q = ds.createQuery(TokenSingleSetTransactionCategorizerDocument.class)
				.field("tokenHashes").contains(tokenHash);
		long count = count(q);
		if (count > 0l)
			present = true;

		return present;
	}

	public void addTokenHash(String tokenHash) {

		UpdateOperations<TokenSingleSetTransactionCategorizerDocument> ops = ds.createUpdateOperations(
				TokenSingleSetTransactionCategorizerDocument.class).add("token_hashes", tokenHash, false);
		ds.update(ds.createQuery(TokenSingleSetTransactionCategorizerDocument.class), ops);
	}

	/**
	 * 
	 * @param objectId
	 * @return
	 */
	public TokenSingleSetTransactionCategorizerDocument getTokenSingleSetTransactionCategorizerDocument(boolean includeTokenHashField) {

		Query<TokenSingleSetTransactionCategorizerDocument> q = null;
		if (includeTokenHashField) {
			q = ds.createQuery(TokenSingleSetTransactionCategorizerDocument.class).retrievedFields(true, "lastUpdated", "createdOn", "token_hashes");
		} else {
			q = ds.createQuery(TokenSingleSetTransactionCategorizerDocument.class).retrievedFields(true, "lastUpdated", "createdOn");
		}

		QueryResults<TokenSingleSetTransactionCategorizerDocument> qr = find(q);
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
	public TokenSingleSetTransactionCategorizerDocument getTokenSingleSetTransactionCategorizerDocument() {

		return getTokenSingleSetTransactionCategorizerDocument(true);

	}

}
