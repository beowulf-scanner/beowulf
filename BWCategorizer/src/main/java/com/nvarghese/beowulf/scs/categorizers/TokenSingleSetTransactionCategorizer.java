package com.nvarghese.beowulf.scs.categorizers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.nvarghese.beowulf.scs.categorizers.dao.TokenSingleSetTransactionCategorizerDAO;
import com.nvarghese.beowulf.scs.categorizers.model.TokenSingleSetTransactionCategorizerDocument;
import com.nvarghese.beowulf.scs.services.BwExecutorService;

public abstract class TokenSingleSetTransactionCategorizer extends SingleSetCategorizer implements TransactionCategorizer {

	private TokenSingleSetTransactionCategorizerDAO tokenSingleSetTransactionCategorizerDAO;

	public TokenSingleSetTransactionCategorizer(Datastore ds, WebScanDocument webScanDocument, WebTestType webTestType) {

		super(ds, webScanDocument, webTestType);
		if (ds != null) {
			tokenSingleSetTransactionCategorizerDAO = new TokenSingleSetTransactionCategorizerDAO(ds);
			if (tokenSingleSetTransactionCategorizerDAO.getTokenSingleSetTransactionCategorizerDocument() == null) {
				tokenSingleSetTransactionCategorizerDAO
						.createTokenSingleSetTransactionCategorizerDocument(new TokenSingleSetTransactionCategorizerDocument());
			}
		}

	}

	@Override
	public void analyzeTransaction(AbstractHttpTransaction transaction) {

		Set<TestJob> testJobs = new HashSet<TestJob>();

		for (String token : getTokens(transaction)) {
			if (token == null) {
				continue;
			}

			String tokenHash = DigestUtils.md5Hex(token);
			if (!tokenSingleSetTransactionCategorizerDAO.isTokenHashPresent(tokenHash)) {
				tokenSingleSetTransactionCategorizerDAO.addTokenHash(tokenHash);
				for (Long moduleNumber : moduleNumbers) {
					TestJob testJob = makeTestJob(transaction, moduleNumber, token);
					testJobs.add(testJob);
				}
			}
		}

		// submit to queue
		BwExecutorService bwExecutorService = new BwExecutorService();
		bwExecutorService.submitJobs(testJobs);

	}

	protected abstract String[] getTokens(AbstractHttpTransaction transaction);

	protected TestJob makeTestJob(AbstractHttpTransaction transaction, long moduleNumber, String token) {

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
		parameterDocument.setParameterValue(token);
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
