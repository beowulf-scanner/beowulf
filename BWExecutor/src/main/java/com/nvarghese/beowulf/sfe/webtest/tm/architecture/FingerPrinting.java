package com.nvarghese.beowulf.sfe.webtest.tm.architecture;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.http.Header;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDocument;
import com.nvarghese.beowulf.common.scan.dao.ReportHostDAO;
import com.nvarghese.beowulf.common.scan.model.ReportHostDocument;
import com.nvarghese.beowulf.sfe.webtest.tm.AbstractTestModule;
import com.nvarghese.beowulf.sfe.webtest.types.HostTestType;

public class FingerPrinting extends AbstractTestModule implements HostTestType {

	private static final String XPoweredBy = "X-Powered-By";

	static Logger logger = LoggerFactory.getLogger(FingerPrinting.class);

	@Override
	public void testByServer(ObjectId txnObjId) {

		logger.info("Running the FingerPrinting module for the txnObjId: {} with data store: {}", txnObjId.toString(), scanInstanceDataStore.getDB()
				.getName());

		HttpTxnDAO txnDAO = new HttpTxnDAO(scanInstanceDataStore);
		HttpTxnDocument httpTxnDocument = txnDAO.getHttpTxnDocument(txnObjId);

		if (httpTxnDocument == null)
			return;

		AbstractHttpTransaction originalTxn = AbstractHttpTransaction.getObject(httpTxnDocument);
		String host = originalTxn.getHost();
		String ipAddress = "";
		String server = "";
		String technology = "";

		try {

			InetAddress addr = InetAddress.getByName(host);
			ipAddress = addr.getHostAddress();

		} catch (UnknownHostException e) {
			logger.error("Unkown Host Exception Caught In FingerPrinting Module." + e.getMessage());

		}

		Header[] header = originalTxn.getAllResponseHeaders();

		for (Header head : header) {

			if (head.getName().equalsIgnoreCase("Server")) {
				server = head.getValue();

			}

			if (head.getName().equalsIgnoreCase(XPoweredBy)) {
				technology = head.getValue();

			}

		}

		// report host details
		reportHostDetails(host, server, ipAddress, technology);

	}

	private void reportHostDetails(String hostName, String server, String ipAddress, String technology) {

		logger.info("Detected the following details for host:: HostName: " + hostName + ", Server: " + server + ", IP: " + ipAddress
				+ ", Technology: " + technology);
		ReportHostDocument reportHostDocument = new ReportHostDocument();
		reportHostDocument.setHostName(hostName);
		reportHostDocument.setServerValue(server);
		reportHostDocument.setIpAddress(ipAddress);
		reportHostDocument.setTechnology(technology);

		ReportHostDAO reportHostDAO = new ReportHostDAO(scanInstanceDataStore);
		reportHostDAO.createReportHostDocument(reportHostDocument);

	}

}
