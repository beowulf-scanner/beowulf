package com.nvarghese.beowulf.common.webtest.scs.jobs;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;
import com.nvarghese.beowulf.common.webtest.CategorizerType;
import com.nvarghese.beowulf.common.webtest.JobStatus;

@Entity("categorization_jobs")
public class CategorizationJobDocument extends AbstractDocument {

	@Property("categorizer_type")
	private CategorizerType categorizerType;

	@Property("webscan_obj_id")
	private ObjectId webScanObjId;

	@Property("txn_obj_id")
	private ObjectId txnObjId;

	@Property("job_status")
	private JobStatus jobStatus;

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

	public CategorizerType getCategorizerType() {

		return categorizerType;
	}

	public void setCategorizerType(CategorizerType categorizerType) {

		this.categorizerType = categorizerType;
	}

	public JobStatus getJobStatus() {

		return jobStatus;
	}

	public void setJobStatus(JobStatus jobStatus) {

		this.jobStatus = jobStatus;
	}

}
