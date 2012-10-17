package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;

public interface ResponseHeaderTestType {

	public void testByResponseHeader(ObjectId txnObjId, String responseHeaderName);

}
