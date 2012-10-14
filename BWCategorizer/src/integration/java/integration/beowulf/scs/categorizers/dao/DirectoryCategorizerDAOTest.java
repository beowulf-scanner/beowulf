package integration.beowulf.scs.categorizers.dao;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.nvarghese.beowulf.scs.categorizers.dao.DirectoryCategorizerDAO;
import com.nvarghese.beowulf.scs.categorizers.model.DirectoryCategorizerDocument;

public class DirectoryCategorizerDAOTest {

	private Datastore ds;

	@BeforeClass
	public void initialize() throws UnknownHostException {

		Mongo mongo = new Mongo("localhost:10001");
		ds = new Morphia().createDatastore(mongo, "webscantests");

	}

	@Test(groups = "Scs_integration_test")
	public void testDirectoryCategorizerDocumentOperations() {

		DirectoryCategorizerDocument categDocument = new DirectoryCategorizerDocument();
		categDocument.getTestedDirs().add("/dir1/");
		categDocument.getTestedDirs().add("/dir2/");
		categDocument.getTestedDirs().add("/dir3/");
		
		DirectoryCategorizerDAO dirCategDAO = new DirectoryCategorizerDAO(ds);
		ObjectId id = dirCategDAO.createDirectoryCategorizerDocument(categDocument);

		Assert.assertNotNull(id);
		boolean isPresent = dirCategDAO.isDirectoryNamePresent("/dir2/");
		Assert.assertTrue(isPresent);

		isPresent = dirCategDAO.isDirectoryNamePresent("/dirNOTFOUND/");
		Assert.assertFalse(isPresent);

		DirectoryCategorizerDocument loadedDirectoryCategorizerDocument = dirCategDAO.getDirectoryCategorizerDocument();
		Assert.assertEquals(loadedDirectoryCategorizerDocument.getId().toString(), id.toString());

	}

	@Test(groups = "Scs_integration_test")
	public void testDirectoryCategorizerDocumentWithIgnoredFields() {

		DirectoryCategorizerDAO dirCategDAO = new DirectoryCategorizerDAO(ds);
		DirectoryCategorizerDocument loadedDirectoryCategorizerDocument = dirCategDAO.getDirectoryCategorizerDocument();
		Assert.assertNotNull(loadedDirectoryCategorizerDocument);
				
		DirectoryCategorizerDocument loadedDirectoryCategorizerDocument2 = dirCategDAO.getDirectoryCategorizerDocument(false);
		Assert.assertEquals(loadedDirectoryCategorizerDocument2.getTestedDirs().size(), 0);
	}
	
	@Test(groups = "Scs_integration_test")
	public void testDirectoryCategorizerDocumentWithIgnoredFieldsAndAddItems() {

		DirectoryCategorizerDAO dirCategDAO = new DirectoryCategorizerDAO(ds);
		DirectoryCategorizerDocument loadedDirectoryCategorizerDocument = dirCategDAO.getDirectoryCategorizerDocument(false);		
		Assert.assertEquals(loadedDirectoryCategorizerDocument.getTestedDirs().size(), 0);
		
		// adds new dir
		dirCategDAO.addDirectoryName("/dir4/");
		
		DirectoryCategorizerDocument loadedDirectoryCategorizerDocument2 = dirCategDAO.getDirectoryCategorizerDocument(true);		
		Assert.assertEquals(loadedDirectoryCategorizerDocument2.getTestedDirs().size(), 4);
		
	}

	@AfterClass
	public void cleanup() {

		ds.getDB().dropDatabase();
	}

}
