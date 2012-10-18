package com.nvarghese.beowulf.sfe.webtest.tm.enumeration;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDocument;
import com.nvarghese.beowulf.sfe.webtest.tm.AbstractTestModule;
import com.nvarghese.beowulf.sfe.webtest.types.DirectoryTestType;

public class DirectoryEnumerator extends AbstractTestModule implements DirectoryTestType {

	static Logger logger = LoggerFactory.getLogger(DirectoryEnumerator.class);

	@Override
	public void testByDirectory(ObjectId txnObjId, String directory) {

		logger.info("Running the directory_enumerator module for the txnObjId: {} with data store: {}", txnObjId.toString(), scanInstanceDataStore
				.getDB().getName());
		
		HttpTxnDAO txnDAO = new HttpTxnDAO(scanInstanceDataStore);
		HttpTxnDocument httpTxnDocument = txnDAO.getHttpTxnDocument(txnObjId);

		if (httpTxnDocument == null)
			return;

	}
}
