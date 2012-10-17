package com.nvarghese.beowulf.scs.categorizers.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDocument;
import com.nvarghese.beowulf.common.http.txn.TransactionSource;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.utils.HttpUtils;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.scs.categorizers.TokenSingleSetTransactionCategorizer;
import com.nvarghese.beowulf.scs.categorizers.dao.DirectoryCategorizerDAO;
import com.nvarghese.beowulf.scs.categorizers.model.DirectoryCategorizerDocument;

public class DirectoryCategorizer extends TokenSingleSetTransactionCategorizer {

	private DirectoryCategorizerDAO directoryCategorizerDAO;
	static Logger logger = LoggerFactory.getLogger(DirectoryCategorizer.class);

	public DirectoryCategorizer(Datastore ds, WebScanDocument webScanDocument) {

		super(ds, webScanDocument, WebTestType.DIRECTORY_TEST);
		if (ds != null) {
			directoryCategorizerDAO = new DirectoryCategorizerDAO(ds);
			if (directoryCategorizerDAO.getDirectoryCategorizerDocument() == null)
				directoryCategorizerDAO.createDirectoryCategorizerDocument(new DirectoryCategorizerDocument());
		}

	}

	public String[] getTokens(AbstractHttpTransaction transaction) {

		String tokens[] = {};
		HttpTxnDAO txnDAO = new HttpTxnDAO(ds);
		String path = transaction.getResourcePath();

		if (HttpUtils.fileExists(transaction.getResponseStatusCode()) || path.equals("/")) {
			ArrayList<String> dirs = new ArrayList<String>();
			int lastSlashIndex = -1;
			int slashIndex;
			while ((slashIndex = path.indexOf("/", lastSlashIndex + 1)) >= 0) {
				if (!(slashIndex - lastSlashIndex == 1 && lastSlashIndex > 0)) {
					String directory = path.substring(0, slashIndex + 1);
					try {

						URIBuilder uriBuilder = new URIBuilder(transaction.getURI());
						uriBuilder.setPath(directory);
						URI testUri = uriBuilder.build();
						if (!directoryCategorizerDAO.isDirectoryNamePresent(directory)) {
							AbstractHttpTransaction dirTest = HttpTransactionFactory.createTransaction(HttpMethodType.GET, testUri, null, null,
									TransactionSource.CATEGORIZER);
							dirTest.execute();

							// persist transaction
							HttpTxnDocument txnDocument = transaction.toHttpTxnDocument();
							txnDAO.createHttpTxnDocument(txnDocument);

							if (HttpUtils.fileExists(dirTest.getResponseStatusCode())) {
								directoryCategorizerDAO.addDirectoryName(directory);
								dirs.add(directory);
							} else {
								logger.warn("Invalid directory passed to categorizer: {}", testUri);
							}
						}

					} catch (URISyntaxException e) {
						logger.error("Problem with URI syntax. Reason: {}", e.getMessage(), e);
					}
				} else {
					logger.warn("Odd URL detected for the transaction with URI: `{}`", transaction.getURI());
				}
				lastSlashIndex = slashIndex;
			}

			tokens = dirs.toArray(tokens);

		}
		return tokens;

	}

}
