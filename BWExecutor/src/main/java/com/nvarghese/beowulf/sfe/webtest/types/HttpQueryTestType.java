package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;

public interface HttpQueryTestType {

	public void testByQuery(ObjectId txnObjId);
}
