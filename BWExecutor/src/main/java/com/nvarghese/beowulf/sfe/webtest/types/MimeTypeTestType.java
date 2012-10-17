package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;

public interface MimeTypeTestType {

	public void testByMimeType(ObjectId txnObjId, String mimeType);

}
