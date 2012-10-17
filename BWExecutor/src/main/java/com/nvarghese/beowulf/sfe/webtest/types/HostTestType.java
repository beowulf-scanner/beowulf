package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;

public interface HostTestType {

	public void testByServer(ObjectId txnObjId);
}
