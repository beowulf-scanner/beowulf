package test.nvarghese.beowulf.common.scan.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.bson.types.ObjectId;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;

public class WebScanDAOTest {

	private Datastore ds;

	@BeforeClass
	public void initialize() throws UnknownHostException {

		Mongo mongo = new Mongo("localhost:10001");
		ds = new Morphia().createDatastore(mongo, "webscantests");

	}

	@Test(dataProvider = "feedWebScanDocuments")
	public void testCreateWebScanDocument(WebScanDocument webScanDocument) {

		WebScanDAO webScanDAO = new WebScanDAO(ds);

		ObjectId id = webScanDAO.createWebScanDocument(webScanDocument);
		Assert.assertNotNull(id);

		WebScanDocument loadedWebScanDocument = webScanDAO.getWebScanDocument(id);
		Assert.assertEquals(loadedWebScanDocument.getBaseUris().get(0), webScanDocument.getBaseUris().get(0));

	}

	@Test
	public void testNonExistingWebScanDocument() {

		WebScanDAO webScanDAO = new WebScanDAO(ds);

		ObjectId id = new ObjectId();
		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(id);
		Assert.assertNull(webScanDocument);
	}

	@DataProvider(/* parallel=false */)
	public Object[][] feedWebScanDocuments() {

		return new Object[][] { { provideDummyWebScanDocument("http://dummysite.com") }, { provideDummyWebScanDocument("http://samplesite.com") },
				{ provideDummyWebScanDocument("http://demosite.com") }, };

	}

	private WebScanDocument provideDummyWebScanDocument(String uri) {

		WebScanDocument webScanDocument = new WebScanDocument();
		List<String> baseURIs = new ArrayList<String>();
		baseURIs.add(uri);
		webScanDocument.setBaseUris(baseURIs);

		return webScanDocument;
	}

	@AfterClass
	public void cleanup() {

		ds.getDB().dropDatabase();
	}
}
