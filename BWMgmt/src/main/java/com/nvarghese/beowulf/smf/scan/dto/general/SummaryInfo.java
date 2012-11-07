package com.nvarghese.beowulf.smf.scan.dto.general;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "summaries")
public class SummaryInfo {

	@XmlElement(name = "summary")
	public Summary summary;

	@XmlType
	public static class Summary {

		@XmlElement(name = "totalissues")
		public int totalIssues;

		@XmlElement(name = "totalissuevariants")
		public int totalIssueVariants;

		@XmlElement(name = "totalissuetypes")
		public int totalIssueTypes;

		public Summary() {

		}
	}

	@XmlElement(name = "hosts")
	public Hosts host;

	@XmlType
	public static class Hosts {

		@XmlElement(name = "host")
		public List<Host> hostList;

		public static class Host {

			@XmlAttribute(name = "name")
			String name;

			@XmlElement(name = "generaldetails")
			public Generaldetails generaldetails;

			@XmlElement(name = "issuedetails")
			public Issuedetails issuedetails;

			public static class Generaldetails {

				@XmlElement(name = "ip")
				public String ip;

				@XmlElement(name = "server")
				public String server;

				@XmlElement(name = "technology")
				public String technology;
			}

			public static class Issuedetails {

				@XmlAttribute(name = "count")
				public int count;

				@XmlElement(name = "informationalissues")
				public int informationalissues;

				@XmlElement(name = "lowseverityissues")
				public int lowseverityissues;

				@XmlElement(name = "mediumseverityissues")
				public int mediumseverityissues;

				@XmlElement(name = "highseverityissues")
				public int highseverityissues;
			}
		}

		public Hosts() {

		};
	}

	public SummaryInfo(Summary summary, Hosts host) {

		super();
		this.summary = summary;
		this.host = host;
	}

	public SummaryInfo() {

		super();
	}
}
