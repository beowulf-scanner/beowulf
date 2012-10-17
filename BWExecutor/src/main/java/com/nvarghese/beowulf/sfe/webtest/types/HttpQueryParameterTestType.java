package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;

public interface HttpQueryParameterTestType {

	public void testByQueryParameter(ObjectId txnObjId, String parameterName);
}
