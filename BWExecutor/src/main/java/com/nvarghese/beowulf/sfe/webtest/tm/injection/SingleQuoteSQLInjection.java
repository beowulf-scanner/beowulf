package com.nvarghese.beowulf.sfe.webtest.tm.injection;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDocument;
import com.nvarghese.beowulf.common.scan.dao.ReportIssueDAO;
import com.nvarghese.beowulf.common.scan.model.ReportIssueDocument;
import com.nvarghese.beowulf.common.scan.model.ReportIssueVariantDocument;
import com.nvarghese.beowulf.common.webtest.ReportThreatType;
import com.nvarghese.beowulf.common.webtest.ThreatSeverityType;
import com.nvarghese.beowulf.sfe.webtest.library.SqlInjectionLibrary;
import com.nvarghese.beowulf.sfe.webtest.tm.AbstractTestModule;
import com.nvarghese.beowulf.sfe.webtest.types.HttpQueryParameterTestType;

public class SingleQuoteSQLInjection extends AbstractTestModule implements HttpQueryParameterTestType {

	static Logger logger = LoggerFactory.getLogger(SingleQuoteSQLInjection.class);

	@Override
	public void testByQueryParameter(ObjectId txnObjId, String parameterName) {

		logger.info("Running the SingleQuoteSQLInjection module for the txnObjId: {} with data store: {}", txnObjId.toString(), scanInstanceDataStore
				.getDB().getName());

		HttpTxnDAO txnDAO = new HttpTxnDAO(scanInstanceDataStore);
		HttpTxnDocument httpTxnDocument = txnDAO.getHttpTxnDocument(txnObjId);

		if (httpTxnDocument == null)
			return;

		AbstractHttpTransaction transaction = AbstractHttpTransaction.getObject(httpTxnDocument);
		AbstractHttpTransaction testTransaction = AbstractHttpTransaction.clone(httpTxnDocument);
		String paramValue = transaction.getQueryParamater(parameterName);
		testTransaction.addQueryParameter(parameterName, paramValue + "'");
		testTransaction.execute();

		String platform = null;

		String bodyText = testTransaction.getResponseBodyAsString();
		bodyText = bodyText.replaceAll("\\s++", " ");
		platform = SqlInjectionLibrary.findSQLErrorMessages(bodyText);

		if (platform != null) {
			txnDAO.createHttpTxnDocument(testTransaction.toHttpTxnDocument());
			recordFinding(transaction, testTransaction, parameterName, platform, bodyText);
		}

	}

	private void recordFinding(AbstractHttpTransaction transaction, AbstractHttpTransaction testTransaction, String parameterName, String platform,
			String bodyText) {

		String uri = testTransaction.getURI().toString();
		ReportIssueDAO issueDAO = new ReportIssueDAO(scanInstanceDataStore);

		ReportIssueDocument reportIssueDocument = issueDAO.findByUrlAndThreatTypeAndModuleNumber(uri, ReportThreatType.SQL_INJ, moduleNumber, false);

		if (reportIssueDocument == null) {
			reportIssueDocument = new ReportIssueDocument();

			reportIssueDocument.setThreatSeverityType(ThreatSeverityType.HIGH);
			reportIssueDocument.setThreatType(ReportThreatType.SQL_INJ);
			reportIssueDocument.setIssueUrl(uri);

			reportIssueDocument.setModuleName(moduleName);
			reportIssueDocument.setModuleNumber(moduleNumber);

			// reasoning
			reportIssueDocument.setReasoning("The application responded with known SQL Error " + "messages to specially crafted test request.");

			// remediation
			reportIssueDocument.setRemediation("Sanitize user input from special characters with proper validation " + "on client and server side");

			// references
			reportIssueDocument.setReferences("OWASP - http://www.owasp.org/index.php/SQL_Injection");

			issueDAO.createReportIssueDocument(reportIssueDocument);

		}

		// set issue variant
		ReportIssueVariantDocument issueVariant = new ReportIssueVariantDocument();
		String description = "When a single quote (') was appended to the parameter listed below, "
				+ "a SQL error message was returned. This could indicate a SQL injection "
				+ "vulnerability. This attack may compromise the integrity of your " + "database and/or expose sensitive information";
		description += "</br>";
		description += "SQL Injection detected with the following details:";
		description += "</br>";
		description += "&nbsp;&nbsp;URL: " + transaction.getResourcePath() + transaction.getResourceName();
		description += "&nbsp;&nbsp;Parameter name: " + parameterName;
		description += "&nbsp;&nbsp;Platform: " + platform;
		issueVariant.setDescription(description);
		issueVariant.setOrigicalTxn(transaction.getObjId());
		issueVariant.setTestTxn(testTransaction.getObjId());

		issueDAO.addReportIssueVariants(reportIssueDocument.getId(), issueVariant);

	}

}
