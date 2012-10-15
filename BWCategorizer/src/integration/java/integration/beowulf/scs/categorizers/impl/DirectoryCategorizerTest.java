package integration.beowulf.scs.categorizers.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;
import com.nvarghese.beowulf.common.http.txn.TransactionSource;
import com.nvarghese.beowulf.scs.categorizers.impl.DirectoryCategorizer;

public class DirectoryCategorizerTest {

	private Datastore ds;

	@BeforeClass
	public void initialize() throws UnknownHostException {

		Mongo mongo = new Mongo("localhost:10001");
		ds = new Morphia().createDatastore(mongo, "webscantests");

	}

	@Test(groups = "Scs_integration_test")
	public void testGetTokens() throws URISyntaxException {

		URI uri = new URI("http://localhost:9888/dir1/subdir/somefile.txt");
		AbstractHttpTransaction transaction = HttpTransactionFactory.createTransaction(HttpMethodType.GET, uri, null, null, TransactionSource.TEST);
		transaction.execute();

		DirectoryCategorizer dirCategorizer = new DirectoryCategorizer(ds, null);
		String[] tokens = dirCategorizer.getTokens(transaction);

		Assert.assertTrue(tokens.length > 0);

	}

	@AfterClass
	public void cleanup() {

		ds.getDB().dropDatabase();
	}

}
