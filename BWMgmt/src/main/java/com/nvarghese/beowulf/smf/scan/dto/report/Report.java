package com.nvarghese.beowulf.smf.scan.dto.report;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "report")
public class Report {

	public String status;
	public String location;
	@XmlElement(name = "comments")
	public Comment comments;
	@XmlElement(name = "oldreports")
	public OldReport oldreports;

	public Report() {

		this.status = "";
		this.location = "";
		comments = new Comment();
		oldreports = new OldReport();

	}

	public static class Comment {

		@XmlElement(name="comment")
		public List<String> commentsList;

		public Comment() {

			commentsList = new ArrayList<String>();
		}

	}

	public static class OldReport {

		@XmlElement(name="oldreport")
		public List<String> oldreportList;

		public OldReport() {

			oldreportList = new ArrayList<String>();
		}

	}

}
