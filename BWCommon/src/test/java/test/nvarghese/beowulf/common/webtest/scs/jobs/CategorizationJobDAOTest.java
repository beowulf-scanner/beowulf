package test.nvarghese.beowulf.common.webtest.scs.jobs;

import java.net.UnknownHostException;

import org.bson.types.ObjectId;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.nvarghese.beowulf.common.webtest.CategorizerType;
import com.nvarghese.beowulf.common.webtest.JobStatus;
import com.nvarghese.beowulf.common.webtest.scs.jobs.CategorizationJobDAO;
import com.nvarghese.beowulf.common.webtest.scs.jobs.CategorizationJobDocument;

public class CategorizationJobDAOTest {

	private Datastore ds;

	@BeforeClass
	public void initialize() throws UnknownHostException {

		Mongo mongo = new Mongo("localhost:10001");
		ds = new Morphia().createDatastore(mongo, "webscancatjobs");

	}

	@Test(dataProvider = "feedCategorizationJobDocument")
	public void testCreateCategorizationJobDocument(CategorizationJobDocument categJobDocument) {

		CategorizationJobDAO jobDAO = new CategorizationJobDAO(ds);

		ObjectId id = jobDAO.createCategorizationJobDocument(categJobDocument);
		Assert.assertNotNull(id);

		CategorizationJobDocument loadedCategorizationJobDocument = jobDAO.getCategorizationJobDocument(id);
		Assert.assertEquals(loadedCategorizationJobDocument.getWebScanObjId(), categJobDocument.getWebScanObjId());

	}

	@Test(dependsOnMethods = { "testCreateCategorizationJobDocument" })
	public void testIsInProgressJobsPresent() {

		CategorizationJobDAO jobDAO = new CategorizationJobDAO(ds);
		boolean present = jobDAO.isInProgressJobsPresent();
		Assert.assertTrue(present);

	}

	@Test(dependsOnMethods = { "testIsInProgressJobsPresent" })
	public void testCountOfJobs() {

		CategorizationJobDAO jobDAO = new CategorizationJobDAO(ds);
		long completedJobs = jobDAO.getCountOfCompletedJobs();
		long errorJobs = jobDAO.getCountOfErrorOrTerminatedJobs();
		long inprogressJobs = jobDAO.getCountOfInProgressJobs();

		Assert.assertEquals(completedJobs, 2);
		Assert.assertEquals(inprogressJobs, 4);
		Assert.assertEquals(errorJobs, 1);

	}

	@DataProvider(/* parallel=false */)
	public Object[][] feedCategorizationJobDocument() {

		return new Object[][] { { provideDummyCategorizationJobDocument(JobStatus.WAITING) },
				{ provideDummyCategorizationJobDocument(JobStatus.COMPLETED) }, { provideDummyCategorizationJobDocument(JobStatus.COMPLETED) },
				{ provideDummyCategorizationJobDocument(JobStatus.PROCESSING) }, { provideDummyCategorizationJobDocument(JobStatus.PROCESSING) },
				{ provideDummyCategorizationJobDocument(JobStatus.WAITING) }, { provideDummyCategorizationJobDocument(JobStatus.TERMINATED) }

		};

	}

	private CategorizationJobDocument provideDummyCategorizationJobDocument(JobStatus jobStatus) {

		CategorizationJobDocument doc = new CategorizationJobDocument();
		doc.setCategorizerType(CategorizerType.MULTI_SET);
		doc.setTxnObjId(new ObjectId());
		doc.setWebScanObjId(new ObjectId());
		doc.setJobStatus(jobStatus);
		return doc;
	}

	@AfterClass
	public void cleanup() {

		ds.getDB().dropDatabase();
	}

}
