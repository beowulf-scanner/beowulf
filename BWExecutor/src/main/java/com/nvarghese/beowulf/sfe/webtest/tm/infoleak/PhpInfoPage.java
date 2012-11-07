package com.nvarghese.beowulf.sfe.webtest.tm.infoleak;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDocument;
import com.nvarghese.beowulf.common.http.txn.TransactionSource;
import com.nvarghese.beowulf.common.scan.dao.ReportIssueDAO;
import com.nvarghese.beowulf.common.scan.model.ReportIssueDocument;
import com.nvarghese.beowulf.common.scan.model.ReportIssueVariantDocument;
import com.nvarghese.beowulf.common.utils.HttpUtils;
import com.nvarghese.beowulf.common.webtest.ReportThreatType;
import com.nvarghese.beowulf.common.webtest.ThreatSeverityType;
import com.nvarghese.beowulf.sfe.webtest.tm.AbstractTestModule;
import com.nvarghese.beowulf.sfe.webtest.types.DirectoryTestType;

public class PhpInfoPage extends AbstractTestModule implements DirectoryTestType {

	static Logger logger = LoggerFactory.getLogger(PhpInfoPage.class);

	@Override
	public void testByDirectory(ObjectId txnObjId, String directory) {

		logger.info("Running the PhpInfoPage module for the txnObjId: {} with data store: {}", txnObjId.toString(), scanInstanceDataStore.getDB()
				.getName());

		HttpTxnDAO txnDAO = new HttpTxnDAO(scanInstanceDataStore);
		HttpTxnDocument httpTxnDocument = txnDAO.getHttpTxnDocument(txnObjId);

		if (httpTxnDocument == null)
			return;

		AbstractHttpTransaction originalTxn = AbstractHttpTransaction.getObject(httpTxnDocument);

		List<String> phpInfoNames = loadDefaultPhpInfoNames();

		for (String phpInfoName : phpInfoNames) {

			try {
				URI uri = new URI(originalTxn.getHostUriWithoutTrailingSlash() + directory + phpInfoName + "/");
				AbstractHttpTransaction testTransaction = HttpTransactionFactory.createTransaction(HttpMethodType.GET, uri, null, originalTxn
						.getURI().toString(), TransactionSource.TEST);
				testTransaction.execute();

				if (HttpUtils.fileExists(testTransaction.getResponseStatusCode())) {
					logger.info("Detected directory `{}` by test", uri.getPath());
					ObjectId id = txnDAO.createHttpTxnDocument(testTransaction.toHttpTxnDocument());
					testTransaction.setObjId(id);
					reportFinding(originalTxn, testTransaction, phpInfoName);
				} else {

				}
			} catch (URISyntaxException e) {
				logger.error("Failed to execute the request. Reason: {}", e.getMessage(), e);
			}
		}

	}

	private void reportFinding(AbstractHttpTransaction originalTxn, AbstractHttpTransaction testTransaction, String phpInfoName) {

		String responseBodyText = testTransaction.getResponseBodyAsString();
		Map<String, String> infoMap = testAndRecordPattern(responseBodyText);

		String uri = testTransaction.getURI().toString();
		ReportIssueDAO issueDAO = new ReportIssueDAO(scanInstanceDataStore);

		ReportIssueDocument reportIssueDocument = issueDAO.findByUrlAndThreatTypeAndModuleNumber(uri, ReportThreatType.PRED_RESOURCE_LOCATION,
				moduleNumber, false);

		if (reportIssueDocument == null) {
			reportIssueDocument = new ReportIssueDocument();

			reportIssueDocument.setThreatSeverityType(ThreatSeverityType.MEDIUM);
			reportIssueDocument.setThreatType(ReportThreatType.PRED_RESOURCE_LOCATION);
			reportIssueDocument.setIssueUrl(uri);

			reportIssueDocument.setModuleName(moduleName);
			reportIssueDocument.setModuleNumber(moduleNumber);

			// reasoning
			reportIssueDocument.setReasoning("A file containing phpinfo() details was detected.");

			// remediation
			reportIssueDocument
					.setRemediation("Although this is not a vulnerability by itself and are often used for testing and debugging purpose, however "
							+ "it is recommended not to expose such information on a production application since it can be used for staging "
							+ "other attacks.");

			// references
			reportIssueDocument.setReferences("perishablepress - http://perishablepress.com/press/2010/03/17/htaccess-secure-phpinfo-php/");

			issueDAO.createReportIssueDocument(reportIssueDocument);

		}

		// set issue variant
		ReportIssueVariantDocument issueVariant = new ReportIssueVariantDocument();
		String description = "A file serving details using PHP interpreter's built-in phpinfo() function was detected on the remote server.</br>";
		description += "Following details were extracted: </br></br>";
		description += "&nbsp;&nbsp;System: " + infoMap.get("System") + "</br>";
		if (infoMap.containsKey("BuildDate")) {
			description += "&nbsp;&nbsp;Build Date: " + infoMap.get("BuildDate") + "</br>";
		}
		if (infoMap.containsKey("DocumentRoot")) {
			description += "&nbsp;&nbsp;Document Root: " + infoMap.get("DocumentRoot") + "</br>";
		}
		if (infoMap.containsKey("ScriptFileName")) {
			description += "&nbsp;&nbsp;Script File Name: " + infoMap.get("ScriptFileName") + "</br>";
		}

		issueVariant.setDescription(description);
		issueVariant.setOrigicalTxn(originalTxn.getObjId());
		issueVariant.setTestTxn(testTransaction.getObjId());

		issueDAO.addReportIssueVariants(reportIssueDocument.getId(), issueVariant);

	}

	private Map<String, String> testAndRecordPattern(String responseBodyText) {

		Map<String, String> infoMap = new HashMap<String, String>(1);
		/*
		 * We attempt to extract the System: XXX field since it is common in all
		 * version
		 */
		Pattern p = Pattern.compile("System\\s+<\\/td><td.*>(.*)<\\/td>");
		Matcher m = p.matcher(responseBodyText);

		if (!m.find()) {
			return infoMap;
		} else {
			infoMap.put("System", m.group(1));
		}

		/*
		 * Extract other interesting fields
		 */
		p = Pattern.compile("Build Date\\s+<\\/td><td.*>(.*)<\\/td>");
		m = p.matcher(responseBodyText);

		if (m.find()) {
			infoMap.put("BuildDate", m.group(1));
		}

		p = Pattern.compile("DOCUMENT_ROOT\\s+<\\/td><td.*>(.*)<\\/td>");
		m = p.matcher(responseBodyText);

		if (m.find()) {
			infoMap.put("DocumentRoot", m.group(1));
		}

		p = Pattern.compile("SCRIPT_FILENAME\\s+<\\/td><td.*>(.*)<\\/td>");
		m = p.matcher(responseBodyText);

		if (m.find()) {
			infoMap.put("ScriptFileName", m.group(1));
		}

		return infoMap;
	}

	private List<String> loadDefaultPhpInfoNames() {

		List<String> names = new ArrayList<String>();

		names.add("info.php");
		names.add("phpinfo.php");
		names.add("php_info.php");
		names.add("system.php");
		names.add("sysinfo.php");
		names.add("test.php");

		return names;
	}

}
