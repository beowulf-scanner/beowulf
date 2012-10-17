package com.nvarghese.beowulf.sfe.webtest.types;

import org.apache.http.cookie.Cookie;
import org.bson.types.ObjectId;

public interface SetCookieTestType {

	public void testBySetCookie(ObjectId txnObjId, Cookie cookie);
}
