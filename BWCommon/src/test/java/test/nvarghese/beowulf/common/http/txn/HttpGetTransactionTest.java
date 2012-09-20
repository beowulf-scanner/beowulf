package test.nvarghese.beowulf.common.http.txn;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;

public class HttpGetTransactionTest {

	@Test(groups = "BW_http_txn_test")
	public void testSimpleGetRequest() throws URISyntaxException {

		URI uri = new URI("http://localhost:9888/index.html");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.GET, uri, null,
				null);

		transaction.execute();
		String responseString = transaction.getResponseBodyAsString();
		Assert.assertNotNull(responseString);
		Assert.assertEquals(transaction.getResponse().getStatusLine().getStatusCode(), 200);

	}
}
