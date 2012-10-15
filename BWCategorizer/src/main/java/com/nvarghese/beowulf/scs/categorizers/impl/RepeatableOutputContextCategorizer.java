package com.nvarghese.beowulf.scs.categorizers.impl;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.MultiSetCategorizer;

public class RepeatableOutputContextCategorizer extends MultiSetCategorizer {

	public RepeatableOutputContextCategorizer(Datastore ds, WebScanDocument webScanDocument) {

		super(ds, webScanDocument, WebTestType.REPEATABLE_OUTPUT_CONTEXT_TEST);

	}

	public void analyzeRepeatableOutputContext() {

	}

}
