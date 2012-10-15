package test.nvarghese.beowulf.common.webtest.dao;

import java.net.UnknownHostException;
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
import com.nvarghese.beowulf.common.webtest.WebTestCategory;
import com.nvarghese.beowulf.common.webtest.WebTestType;
import com.nvarghese.beowulf.common.webtest.dao.TestModuleMetaDataDAO;
import com.nvarghese.beowulf.common.webtest.model.TestModuleMetaDataDocument;

public class TestModuleMetaDataDAOTest {

	private Datastore ds;

	@BeforeClass
	public void initialize() throws UnknownHostException {

		Mongo mongo = new Mongo("localhost:10001");
		ds = new Morphia().createDatastore(mongo, "webscantestmodules");

	}

	@Test(dataProvider = "feedTestModuleMetaDataDocument")
	public void testCreateTestModuleMetaDataDocument(TestModuleMetaDataDocument testModuleMetaDataDocument) {

		TestModuleMetaDataDAO testModuleDAO = new TestModuleMetaDataDAO(ds);

		ObjectId id = testModuleDAO.createTestModuleMetaDataDocument(testModuleMetaDataDocument);
		Assert.assertNotNull(id);

		TestModuleMetaDataDocument loadedTestModuleMetaDataDocument = testModuleDAO.getTestModuleMetaDataDocument(id);
		Assert.assertEquals(loadedTestModuleMetaDataDocument.getModuleNumber(), testModuleMetaDataDocument.getModuleNumber());
		Assert.assertEquals(loadedTestModuleMetaDataDocument.getModuleName(), testModuleMetaDataDocument.getModuleName());

	}

	@Test(dependsOnMethods = { "testCreateTestModuleMetaDataDocument" })
	public void testFindByModuleNumber() {

		TestModuleMetaDataDAO testModuleDAO = new TestModuleMetaDataDAO(ds);
		TestModuleMetaDataDocument loadedTestModuleMetaDataDocument = testModuleDAO.findByModuleNumber(1);
		Assert.assertNotNull(loadedTestModuleMetaDataDocument);

	}

	@Test(dependsOnMethods = { "testFindByModuleNumber" })
	public void testFindByTestType() {

		TestModuleMetaDataDAO testModuleDAO = new TestModuleMetaDataDAO(ds);
		List<TestModuleMetaDataDocument> docs = testModuleDAO.findByTestType(WebTestType.HTML_ELEM_TEST);
		Assert.assertNotNull(docs);
		Assert.assertTrue(docs.size() > 0);

	}

	@DataProvider(/* parallel=false */)
	public Object[][] feedTestModuleMetaDataDocument() {

		return new Object[][] { { provideDummyTestModuleMetaDataDocument(1) }, { provideDummyTestModuleMetaDataDocument(2) },
				{ provideDummyTestModuleMetaDataDocument(3) }, };

	}

	private TestModuleMetaDataDocument provideDummyTestModuleMetaDataDocument(long moduleNumber) {

		TestModuleMetaDataDocument testModuleMetaDataDocument = new TestModuleMetaDataDocument();
		testModuleMetaDataDocument.setModuleNumber(moduleNumber);
		testModuleMetaDataDocument.setModuleName("Module#" + moduleNumber);
		testModuleMetaDataDocument.setEnabled(true);
		testModuleMetaDataDocument.setTestCategory(WebTestCategory.ARCHITECTURE);
		testModuleMetaDataDocument.setTestType(WebTestType.HTML_ELEM_TEST);

		return testModuleMetaDataDocument;
	}

	@AfterClass
	public void cleanup() {

		ds.getDB().dropDatabase();
	}

}
