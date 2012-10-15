package com.nvarghese.beowulf.scs.categorizers;

import java.util.HashSet;
import java.util.Set;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;

public abstract class Categorizer {

	protected Datastore ds;
	// private WeakReference<WebScanDocument> webScanDocumentRef;
	protected WebScanDocument webScanDocument;

	public Categorizer(Datastore ds, WebScanDocument webScanDocument) {

		this.ds = ds;
		// this.webScanDocumentRef = new
		// WeakReference<WebScanDocument>(webScanDocument);
		this.webScanDocument = webScanDocument;
	}

	public abstract void initialize();

	public Set<Long> getEnabledTestModuleNumbers() {

		Set<Long> testModuleNumbers = new HashSet<Long>();
		if (webScanDocument != null) {
			testModuleNumbers = webScanDocument.getScanConfig().getEnabledTestModules().keySet();
		}
		return testModuleNumbers;

	}

}
