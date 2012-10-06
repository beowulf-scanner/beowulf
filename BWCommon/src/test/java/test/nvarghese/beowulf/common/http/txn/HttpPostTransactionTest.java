package test.nvarghese.beowulf.common.http.txn;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;

public class HttpPostTransactionTest {

	@Test(groups = "BW_http_post_txn_test")
	public void testSimplePostRequest() throws URISyntaxException, UnsupportedEncodingException {

		URI uri = new URI("http://localhost:9888/index.html");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.POST, uri, getUrlEncodedFormEntity(), null);
		transaction.execute();
		String responseString = transaction.getResponseBodyAsString();
		Assert.assertNotNull(responseString);
		Assert.assertEquals(transaction.getResponse().getStatusLine().getStatusCode(), 200);

	}

	@Test(groups = "BW_http_post_txn_test")
	public void testSimplePostCookieRequest() throws URISyntaxException, UnsupportedEncodingException {

		URI uri = new URI("http://localhost:9888/cookie_test.html");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.POST, uri, getUrlEncodedFormEntity(), null);

		transaction.execute();
		String responseString = transaction.getResponseBodyAsString();
		CookieStore cookieStore = transaction.getCookieStore();

		Assert.assertNotNull(responseString);
		Assert.assertEquals(transaction.getResponse().getStatusLine().getStatusCode(), 200);
		Assert.assertNotSame(cookieStore.getCookies().size(), 0);

	}

	@Test(groups = "BW_http_post_txn_test")
	public void testPostAddCookieInRequest() throws URISyntaxException, UnsupportedEncodingException {

		URI uri = new URI("http://localhost:9888/cookie_added.html");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.POST, uri, getUrlEncodedFormEntity(), null);
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

	private UrlEncodedFormEntity getUrlEncodedFormEntity() throws UnsupportedEncodingException {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		formparams.add(new BasicNameValuePair("param1", "value1"));
		formparams.add(new BasicNameValuePair("param2", "value2"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);

		return entity;
	}

}
