package com.nvarghese.beowulf.sfe.webtest.tm.enumeration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
import com.nvarghese.beowulf.common.scan.dto.config.Options;
import com.nvarghese.beowulf.common.scan.model.ReportIssueDocument;
import com.nvarghese.beowulf.common.scan.model.ReportIssueVariantDocument;
import com.nvarghese.beowulf.common.utils.HttpUtils;
import com.nvarghese.beowulf.common.webtest.ReportThreatType;
import com.nvarghese.beowulf.sfe.ConfigurationManager;
import com.nvarghese.beowulf.sfe.webtest.tm.AbstractTestModule;
import com.nvarghese.beowulf.sfe.webtest.types.DirectoryTestType;

public class DirectoryEnumerator extends AbstractTestModule implements DirectoryTestType {

	private static final String SMALL_DIR_LIST = "Small (100 directory names)";
	private static final String MEDIUM_DIR_LIST = "Medium (300 directory names)";
	private static final String LARGE_DIR_LIST = "Large (500 directory names)";
	private static final String EXTRA_LARGE_DIR_LIST = "Extra large (819 directory names)";

	static Logger logger = LoggerFactory.getLogger(DirectoryEnumerator.class);

	@Override
	public void testByDirectory(ObjectId txnObjId, String directory) {

		logger.info("Running the directory_enumerator module for the txnObjId: {} with data store: {}", txnObjId.toString(), scanInstanceDataStore
				.getDB().getName());

		HttpTxnDAO txnDAO = new HttpTxnDAO(scanInstanceDataStore);
		HttpTxnDocument httpTxnDocument = txnDAO.getHttpTxnDocument(txnObjId);

		if (httpTxnDocument == null)
			return;

		AbstractHttpTransaction originalTxn = AbstractHttpTransaction.getObject(httpTxnDocument);

		List<String> directoryNames = loadDirectoryNames();

		for (String newDirectory : directoryNames) {

			try {
				URI uri = new URI(originalTxn.getHostUriWithoutTrailingSlash() + directory + newDirectory + "/");
				AbstractHttpTransaction testTransaction = HttpTransactionFactory.createTransaction(HttpMethodType.GET, uri, null, originalTxn
						.getURI().toString(), TransactionSource.ENUMERATION);
				testTransaction.execute();

				if (HttpUtils.fileExists(testTransaction.getResponseStatusCode())) {
					logger.info("Detected directory `{}` by enumeration", uri.getPath());
					ObjectId id = txnDAO.createHttpTxnDocument(testTransaction.toHttpTxnDocument());
					testTransaction.setObjId(id);
					reportFinding(originalTxn, testTransaction, newDirectory);
				} else {

				}
			} catch (URISyntaxException e) {
				logger.error("Failed to execute the request. Reason: {}", e.getMessage(), e);
			}
		}

	}

	private List<String> loadDirectoryNames() {

		List<String> directoryNames = new ArrayList<String>();

		boolean optionValue;

		for (Options options : optionsMap.values()) {

			optionValue = Boolean.valueOf(options.getOptionValue());
			if (optionValue) {
				if (options.getOptionName().equalsIgnoreCase(SMALL_DIR_LIST)) {
					directoryNames.addAll(ConfigurationManager.getScannerConfiguration().getList("file_enumeration.small_directory_list"));
				} else if (options.getOptionName().equalsIgnoreCase(MEDIUM_DIR_LIST)) {
					directoryNames.addAll(ConfigurationManager.getScannerConfiguration().getList("file_enumeration.medium_directory_list"));
				} else if (options.getOptionName().equalsIgnoreCase(LARGE_DIR_LIST)) {
					directoryNames.addAll(ConfigurationManager.getScannerConfiguration().getList("file_enumeration.large_directory_list"));
				} else if (options.getOptionName().equalsIgnoreCase(EXTRA_LARGE_DIR_LIST)) {
					directoryNames.addAll(ConfigurationManager.getScannerConfiguration().getList("file_enumeration.extra_large_directory_list"));
				}
			}
		}

		return directoryNames;
	}
	
	private void reportFinding(AbstractHttpTransaction originalTransaction, AbstractHttpTransaction testTransaction, String newDirectory) {

		String uri = originalTransaction.getHostUriWithoutTrailingSlash() + newDirectory;
		ReportIssueDAO issueDAO = new ReportIssueDAO(scanInstanceDataStore);

		ReportIssueDocument reportIssueDocument = issueDAO.findByUrlAndThreatTypeAndModuleNumber(uri, ReportThreatType.PRED_RESOURCE_LOCATION,
				moduleNumber, false);

		if (reportIssueDocument == null) {
			reportIssueDocument = new ReportIssueDocument();

			reportIssueDocument.setModuleName(moduleName);
			reportIssueDocument.setModuleNumber(moduleNumber);

			// reasoning
			reportIssueDocument.setReasoning("A directory was discovered by guessing its name.");

			// remediation
			reportIssueDocument.setRemediation("Review the contents of the directory and remove if it is not required.");

			// references
			reportIssueDocument.setReferences("OWASP - http://www.owasp.org/index.php/Testing_for_Old,"
					+ "_Backup_and_Unreferenced_Files_(OWASP-CM-006)");
			
			issueDAO.createReportIssueDocument(reportIssueDocument);

		}
		
		//set issue variant
		ReportIssueVariantDocument issueVariant = new ReportIssueVariantDocument();
		String description = "The following directory was discovered by guessing its name: \n";
		description += " Original Directory: " + uri + "\n";
		description += " Discovered Directory: " + testTransaction.getURI() + "\n";
		description += "If the directory was not linked to by the website," +  
				" they could lead an attacker to unintended areas.";
		issueVariant.setDescription(description);
		issueVariant.setOrigicalTxn(originalTransaction.getObjId());
		issueVariant.setTestTxn(testTransaction.getObjId());
		
		
		issueDAO.addReportIssueVariants(reportIssueDocument.getId(), issueVariant);

	}

}
