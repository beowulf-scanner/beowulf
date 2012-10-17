package com.nvarghese.beowulf.common.webtest.sfe.jobs;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;
import com.nvarghese.beowulf.common.webtest.JobStatus;
import com.nvarghese.beowulf.common.webtest.WebTestType;

@Entity("test_jobs")
public class TestJobDocument extends AbstractDocument {

	@Property("test_type")
	private WebTestType testType;

	@Property("module_number")
	private long moduleNumber;

	@Embedded("test_parameters")
	private List<TestParameterDocument> testParameters;

	@Property("webscan_obj_id")
	private ObjectId webScanObjId;

	@Property("txn_obj_id")
	private ObjectId txnObjId;

	@Property("job_status")
	private JobStatus jobStatus;

	public TestJobDocument() {

		testParameters = new ArrayList<TestParameterDocument>();
	}

	public WebTestType getTestType() {

		return testType;
	}

	public void setTestType(WebTestType testType) {

		this.testType = testType;
	}

	public long getModuleNumber() {

		return moduleNumber;
	}

	public void setModuleNumber(long moduleNumber) {

		this.moduleNumber = moduleNumber;
	}

	public ObjectId getWebScanObjId() {

		return webScanObjId;
	}

	public void setWebScanObjId(ObjectId webScanObjId) {

		this.webScanObjId = webScanObjId;
	}

	public ObjectId getTxnObjId() {

		return txnObjId;
	}

	public void setTxnObjId(ObjectId txnObjId) {

		this.txnObjId = txnObjId;
	}

	public List<TestParameterDocument> getTestParameters() {

		return testParameters;
	}

	public void setTestParameters(List<TestParameterDocument> testParameters) {

		this.testParameters = testParameters;
	}

	public JobStatus getJobStatus() {

		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {

		this.jobStatus = jobStatus;
	}

}
