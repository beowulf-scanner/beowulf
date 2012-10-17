package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;

public interface HttpMethodTestType {

	public void testByHttpMethod(ObjectId txnObjId);
}
