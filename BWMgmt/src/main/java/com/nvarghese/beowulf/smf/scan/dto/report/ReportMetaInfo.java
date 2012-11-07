package com.nvarghese.beowulf.smf.scan.dto.report;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "reportmetainfo")
public class ReportMetaInfo {
	
	public String filename;
	public String md5sum;
	public long contentlength;
	public String contenttype;
	public long uploaddate;
	
	public ReportMetaInfo() {
		filename = "";
		md5sum = "";
		contentlength = 0;
		contenttype = "";
		uploaddate = 0;
	}
}
