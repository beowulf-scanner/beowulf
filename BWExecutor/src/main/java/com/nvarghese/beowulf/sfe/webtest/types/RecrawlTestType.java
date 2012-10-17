package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;

/**
 * 
 *  
 */
public interface RecrawlTestType {

	public void testAllTransactions(ObjectId txnObjId);
}
