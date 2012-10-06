package test.nvarghese.beowulf.common.http.txn;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.bson.types.ObjectId;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDocument;

public class HttpTxnDAOTest {

	private Datastore ds;

	@BeforeClass
	public void initialize() throws UnknownHostException {

		Mongo mongo = new Mongo("localhost:10001");
		ds = new Morphia().createDatastore(mongo, "webscantxntests");

	}

	@Test(groups = "BW_http_get_txn_dao_test")
	public void testSimpleGetRequestPersisted() throws URISyntaxException {

		URI uri = new URI("http://localhost:9888/index.html");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.GET, uri, null, null);

		HttpTxnDAO txnDAO = new HttpTxnDAO(ds);
		HttpTxnDocument txnDocument = transaction.toHttpTxnDocument();
		ObjectId id = txnDAO.createHttpTxnDocument(txnDocument);
		Assert.assertNotNull(id);

		HttpTxnDocument loadedTransactionDocument = txnDAO.getHttpTxnDocument(id);
		Assert.assertEquals(loadedTransactionDocument.getRequestURI(), txnDocument.getRequestURI());

		// execute
		AbstractHttpTransaction loadedTransaction = AbstractHttpTransaction.getObject(loadedTransactionDocument);
		loadedTransaction.execute();
		HttpTxnDocument txnDocument2 = loadedTransaction.toHttpTxnDocument();
		txnDAO.updateHttpTxnDocument(txnDocument2);

		HttpTxnDocument loadedTransactionDocument2 = txnDAO.getHttpTxnDocument(id);
		AbstractHttpTransaction loadedTransaction2 = AbstractHttpTransaction.getObject(loadedTransactionDocument2);
		Assert.assertEquals(loadedTransaction2.getResponseStatusCode(), loadedTransaction.getResponseStatusCode());

	}

	@Test(groups = "BW_http_post_txn_dao_test")
	public void testSimplePostRequestPersisted() throws URISyntaxException, UnsupportedEncodingException {

		URI uri = new URI("http://localhost:9888/index.html");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.POST, uri, getUrlEncodedFormEntity(), null);

		HttpTxnDAO txnDAO = new HttpTxnDAO(ds);
		HttpTxnDocument txnDocument = transaction.toHttpTxnDocument();
		ObjectId id = txnDAO.createHttpTxnDocument(txnDocument);
		Assert.assertNotNull(id);

		HttpTxnDocument loadedTransactionDocument = txnDAO.getHttpTxnDocument(id);
		Assert.assertEquals(loadedTransactionDocument.getRequestURI(), txnDocument.getRequestURI());

		// execute
		AbstractHttpTransaction loadedTransaction = AbstractHttpTransaction.getObject(loadedTransactionDocument);
		loadedTransaction.execute();
		HttpTxnDocument txnDocument2 = loadedTransaction.toHttpTxnDocument();
		txnDAO.updateHttpTxnDocument(txnDocument2);

		HttpTxnDocument loadedTransactionDocument2 = txnDAO.getHttpTxnDocument(id);
		AbstractHttpTransaction loadedTransaction2 = AbstractHttpTransaction.getObject(loadedTransactionDocument2);
		Assert.assertEquals(loadedTransaction2.getResponseStatusCode(), loadedTransaction.getResponseStatusCode());

	}

	private UrlEncodedFormEntity getUrlEncodedFormEntity() throws UnsupportedEncodingException {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("param1", "value1"));
		formparams.add(new BasicNameValuePair("param2", "value2"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);

		return entity;
	}

	@AfterClass
	public void cleanup() {

		// ds.getDB().dropDatabase();
	}

}
