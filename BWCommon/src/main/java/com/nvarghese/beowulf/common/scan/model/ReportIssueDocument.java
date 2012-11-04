package com.nvarghese.beowulf.common.scan.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;
import com.nvarghese.beowulf.common.webtest.ReportThreatType;
import com.nvarghese.beowulf.common.webtest.ThreatSeverityType;

@Entity("report_issues")
public class ReportIssueDocument extends AbstractDocument {

	@Property("issue_url")
	private String issueUrl;

	@Property("threat_severity")
	private ThreatSeverityType threatSeverityType;

	@Property("threat_type")
	private ReportThreatType threatType;
	
	@Embedded("issue_variants")
	private List<ReportIssueVariantDocument> issueVariants;

	@Property("reasoning")
	private String reasoning;

	@Property("remediation")
	private String remediation;

	@Property("references")
	private String references;

	@Property("module_number")
	private Long moduleNumber;

	@Property("module_name")
	private String moduleName;
	
	public ReportIssueDocument() {
		
		setCreatedOn(new Date());
		issueVariants = new ArrayList<ReportIssueVariantDocument>();
		
	}

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

	public String getIssueUrl() {

		return issueUrl;
	}

	public void setIssueUrl(String issueUrl) {

		this.issueUrl = issueUrl;
	}

	public ThreatSeverityType getThreatSeverityType() {

		return threatSeverityType;
	}

	public void setThreatSeverityType(ThreatSeverityType threatSeverityType) {

		this.threatSeverityType = threatSeverityType;
	}

	public ReportThreatType getThreatType() {

		return threatType;
	}

	public void setThreatType(ReportThreatType threatType) {

		this.threatType = threatType;
	}

	
	public List<ReportIssueVariantDocument> getIssueVariants() {
	
		return issueVariants;
	}

	
	public void setIssueVariants(List<ReportIssueVariantDocument> issueVariants) {
	
		this.issueVariants = issueVariants;
	}

	public String getReasoning() {

		return reasoning;
	}

	public void setReasoning(String reasoning) {

		this.reasoning = reasoning;
	}

	public String getRemediation() {

		return remediation;
	}

	public void setRemediation(String remediation) {

		this.remediation = remediation;
	}

	public String getReferences() {

		return references;
	}

	public void setReferences(String references) {

		this.references = references;
	}

	public Long getModuleNumber() {

		return moduleNumber;
	}

	public void setModuleNumber(Long moduleNumber) {

		this.moduleNumber = moduleNumber;
	}

	public String getModuleName() {

		return moduleName;
	}

	public void setModuleName(String moduleName) {

		this.moduleName = moduleName;
	}

}
