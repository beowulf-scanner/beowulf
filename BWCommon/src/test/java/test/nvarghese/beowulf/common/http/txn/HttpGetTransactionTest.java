package test.nvarghese.beowulf.common.http.txn;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.codehaus.jackson.map.introspect.BasicClassIntrospector;
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

	@Test(groups = "BW_http_txn_test")
	public void testSimpleGetCookieRequest() throws URISyntaxException {

		URI uri = new URI("http://localhost:9888/cookie_test.html");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.GET, uri, null,
				null);

		transaction.execute();
		String responseString = transaction.getResponseBodyAsString();
		CookieStore cookieStore = transaction.getCookieStore();

		Assert.assertNotNull(responseString);
		Assert.assertEquals(transaction.getResponse().getStatusLine().getStatusCode(), 200);
		Assert.assertNotSame(cookieStore.getCookies().size(), 0);

	}

	@Test(groups = "BW_http_txn_test")
	public void testAddCookieInRequest() throws URISyntaxException {

		URI uri = new URI("http://localhost:9888/cookie_added.html");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.GET, uri, null,
				null);
		BasicClientCookie cookie = new BasicClientCookie("c", "value");
		cookie.setDomain("localhost");
		cookie.setPath("/");
		cookie.setVersion(0);

		transaction.getCookieStore().addCookie(cookie);

		transaction.execute();

		int statusCode = transaction.getResponseStatusCode();
		String responseString = transaction.getResponseBodyAsString();

		Assert.assertNotNull(responseString);
		Assert.assertEquals(statusCode, 200);
		

	}

}
