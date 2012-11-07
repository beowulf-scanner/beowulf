package com.nvarghese.beowulf.scs.categorizers.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.bson.types.ObjectId;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.JobStatus;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJob;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJobDAO;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJobDocument;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestParameterDocument;
import com.nvarghese.beowulf.scs.categorizers.TokenSingleSetTransactionCategorizer;

public class HttpQueryParameterCategorizer extends TokenSingleSetTransactionCategorizer {
	
	private static final String DELIM = "$DELIM$";

	public HttpQueryParameterCategorizer(Datastore ds, WebScanDocument webScanDocument) {

		super(ds, webScanDocument, WebTestType.HTTP_QUERY_PARAM_TEST);
	}

	@Override
	protected String[] getTokens(AbstractHttpTransaction transaction) {

		String tokens[];
		List<NameValuePair> parameterNames = transaction.getQueryParameters();
		List<String> tmpTokens = new ArrayList<String>(1);
		String resourcePath = transaction.getResourcePath();
		for (NameValuePair name : parameterNames) {
			String tokenWithPrefix = resourcePath + DELIM + name.getName();
			tmpTokens.add(tokenWithPrefix);

		}
		tokens = tmpTokens.toArray(new String[0]);
		return tokens;
	}
	
	@Override
	protected TestJob makeTestJob(AbstractHttpTransaction transaction, long moduleNumber, String token) {
		
		String parameterName = token.split(DELIM)[1];		

		TestJobDocument testJobDocument = new TestJobDocument();
		testJobDocument.setJobStatus(JobStatus.INIT);
		testJobDocument.setModuleNumber(moduleNumber);
		testJobDocument.setTestType(testType);
		testJobDocument.setTxnObjId(transaction.getObjId());
		testJobDocument.setWebScanObjId(webScanDocument.getId());
		
		//add test params
		List<TestParameterDocument> params = new ArrayList<TestParameterDocument>();
		TestParameterDocument parameterDocument = new TestParameterDocument();
		parameterDocument.setParameterType(token.getClass().getName());
		parameterDocument.setParameterValue(parameterName);
		params.add(parameterDocument);
		testJobDocument.setTestParameters(params);
		

		TestJobDAO testJobDAO = new TestJobDAO(ds);
		ObjectId id = testJobDAO.createTestJobDocument(testJobDocument);

		TestJob testJob = new TestJob();
		testJob.setDatabaseName(ds.getDB().getName());
		testJob.setTestJobObjId(id.toString());
		testJob.setWebScanObjId(webScanDocument.getId().toString());

		return testJob;

	}

}
