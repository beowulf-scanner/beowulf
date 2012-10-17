package com.nvarghese.beowulf.sfe.webtest.types;

import org.bson.types.ObjectId;
import org.w3c.dom.html2.HTMLElement;

public interface HtmlElementTestType {

	public void testByHtmlElement(ObjectId txnObjId, HTMLElement element, String elementType);

}
