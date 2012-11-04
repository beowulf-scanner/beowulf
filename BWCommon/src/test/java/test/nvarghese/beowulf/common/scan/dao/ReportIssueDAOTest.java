package test.nvarghese.beowulf.common.scan.dao;

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
import com.nvarghese.beowulf.common.scan.dao.ReportIssueDAO;
import com.nvarghese.beowulf.common.scan.model.ReportIssueDocument;
import com.nvarghese.beowulf.common.scan.model.ReportIssueVariantDocument;
import com.nvarghese.beowulf.common.webtest.ReportThreatType;
import com.nvarghese.beowulf.common.webtest.ThreatSeverityType;

public class ReportIssueDAOTest {

	private Datastore ds;

	@BeforeClass
	public void initialize() throws UnknownHostException {

		Mongo mongo = new Mongo("localhost:10001");
		ds = new Morphia().createDatastore(mongo, "webscantestreps");

	}

	@Test(dataProvider = "feedDifferentReportIssueDocuments")
	public void testCreateReportIssueDocument(ReportIssueDocument reportIssueDocument) {

		ReportIssueDAO reportIssueDAO = new ReportIssueDAO(ds);

		ObjectId id = reportIssueDAO.createReportIssueDocument(reportIssueDocument);
		Assert.assertNotNull(id);

		ReportIssueDocument loadedReportIssueDocument = reportIssueDAO.getReportIssueDocument(id);
		Assert.assertEquals(loadedReportIssueDocument.getModuleName(), reportIssueDocument.getModuleName());
	}

	@Test(dependsOnMethods = { "testCreateReportIssueDocument" })
	public void testFindByUrlAndComboQuery() {

		ReportIssueDAO reportIssueDAO = new ReportIssueDAO(ds);

		ReportIssueDocument document = reportIssueDAO.findByUrlAndThreatTypeAndModuleNumber("http://dummysite.com/issue1/",
				ReportThreatType.BRUTEFORCE, 1, false);
		Assert.assertNotNull(document);
		Assert.assertEquals(document.getThreatType().toString(), ReportThreatType.BRUTEFORCE.toString());

	}

	@Test(dependsOnMethods = { "testFindByUrlAndComboQuery" })
	public void testAddReportIssueVariant() {

		ReportIssueDAO reportIssueDAO = new ReportIssueDAO(ds);

		ReportIssueDocument document = reportIssueDAO.findByUrlAndThreatTypeAndModuleNumber("http://dummysite.com/issue1/",
				ReportThreatType.BRUTEFORCE, 1, false);

		ReportIssueVariantDocument issueVariantDocument = new ReportIssueVariantDocument();
		issueVariantDocument.setDescription("SampleDescription");

		reportIssueDAO.addReportIssueVariants(document.getId(), issueVariantDocument);

		ReportIssueDocument loadedDocument = reportIssueDAO.getReportIssueDocument(document.getId(), true);
		Assert.assertEquals(loadedDocument.getIssueVariants().size(), 1);
		Assert.assertEquals(loadedDocument.getIssueVariants().get(0).getDescription(), "SampleDescription");

	}

	@Test
	public void testNonExistingReportIssueDocument() {

		ReportIssueDAO reportIssueDAO = new ReportIssueDAO(ds);

		ObjectId id = new ObjectId();

		ReportIssueDocument reportIssueDocument = reportIssueDAO.getReportIssueDocument(id);
		Assert.assertNull(reportIssueDocument);
	}

	@DataProvider(/* parallel=false */)
	public Object[][] feedDifferentReportIssueDocuments() {

		return new Object[][] { { provideDummyReportIssueDocument("http://dummysite.com/issue1/", "Mod1", 1, ReportThreatType.BRUTEFORCE) },
				{ provideDummyReportIssueDocument("http://dummysite.com/issue2/", "Mod1", 1, ReportThreatType.BRUTEFORCE) },
				{ provideDummyReportIssueDocument("http://dummysite.com/issue3/", "Mod1", 1, ReportThreatType.BRUTEFORCE) }, };

	}

	private ReportIssueDocument provideDummyReportIssueDocument(String uri, String moduleName, long moduleNumber, ReportThreatType threatType) {

		ReportIssueDocument reportIssueDocument = new ReportIssueDocument();
		reportIssueDocument.setIssueUrl(uri);
		reportIssueDocument.setModuleName(moduleName);
		reportIssueDocument.setModuleNumber(moduleNumber);
		reportIssueDocument.setThreatType(threatType);
		reportIssueDocument.setThreatSeverityType(ThreatSeverityType.HIGH);

		return reportIssueDocument;

	}

	@AfterClass
	public void cleanup() {

		ds.getDB().dropDatabase();
	}

}
