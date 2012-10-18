package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;

public interface DirectoryTestType {

	public void testByDirectory(ObjectId txnObjId, String directory);

	
}
