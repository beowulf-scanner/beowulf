package com.nvarghese.beowulf.sfc.report.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDAO;
import com.nvarghese.beowulf.common.http.txn.HttpTxnDocument;
import com.nvarghese.beowulf.common.report.ReportFormat;
import com.nvarghese.beowulf.common.scan.dao.ReportHostDAO;
import com.nvarghese.beowulf.common.scan.dao.ReportIssueDAO;
import com.nvarghese.beowulf.common.scan.model.ReportHostDocument;
import com.nvarghese.beowulf.common.scan.model.ReportIssueDocument;
import com.nvarghese.beowulf.common.scan.model.ReportIssueVariantDocument;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.common.webtest.ReportThreatType;
import com.nvarghese.beowulf.common.webtest.ThreatSeverityType;
import com.nvarghese.beowulf.common.webtest.dao.ReportThreatTypeDAO;
import com.nvarghese.beowulf.sfc.SFControllerManager;
import com.nvarghese.beowulf.sfc.report.ReportException;


public class HtmlReportGenerator extends AbstractReportGenerator{
	
	
		private String newLine;
		static Logger logger = LoggerFactory.getLogger(HtmlReportGenerator.class);
		//private int vulnerableURLCount = 0;

		public HtmlReportGenerator(WebScanDocument webScanDocument, Datastore scanInstanceDataStore, ReportFormat reportFormat, String reportFileName, ThreatSeverityType minSeverity) 
		{
			super(webScanDocument, scanInstanceDataStore, reportFormat, reportFileName, minSeverity);
			if (File.separator.equals("/"))
			{
				newLine = "\n";
			}
			else
			{
				newLine = "\r\n";
			}
			
		}

		@Override
		protected void writeReport() throws ReportException 
		{
			Date date = new Date();
			String dateString = DateFormat.getDateInstance(DateFormat.FULL).format(date) + " ";
			dateString += DateFormat.getTimeInstance(DateFormat.FULL).format(date);

			StringBuilder reportString = new StringBuilder("");		
			FileWriter writer;
			
			try {			
				writer = new FileWriter(super.getReportFilePath());
			} catch (IOException e) {
				logger.error("HtmlReportGenerator: IOException while opening FileStream - " + e.getMessage(), e);
				ReportException reportException = new ReportException("IOException while opening FileStream: " + e.getMessage());
				reportException.initCause(e.getCause());
				throw reportException;
				
			}	
			
			logger.debug("Report generation started.");
			
			/*
			 * 
			 * Caution: Dont make any format or indentation changes 
			 * 
			 * Please keep the formatting as it is. The current fomatting doesnt follow the conventional standards, but it is
			 * kept as it is to ease the readability of hardcoded HTML tags.
			 * 
			 * 
			 */
			
			
			reportString.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" + newLine).append(
							"<html xmlns=\"http://www.w3.org/1999/xhtml\">" + newLine).append(
							"<head>" + newLine).append(
							"<title>" + "Beowulf Scanner" + " Report: " +dateString + "</title>" + newLine).append(
							"<style type=\"text/css\">" + newLine).append(
							"BODY {" + newLine).append(
							"	word-wrap: break-word;" + newLine).append(
							"	font-family: Arial;" + newLine).append(
							"	font-size: 10pt;" + newLine).append(
							"}" + newLine).append( newLine).append(

							".vulnerabilityTitle {" + newLine).append(
							"	text-align: center;" + newLine).append(
							"	font-size: 12pt;" + newLine).append(
							"	font-weight: bold;" + newLine).append(
							"	border-right: gray 1px solid;" + newLine).append(
							"	border-top: gray 1px solid;" + newLine).append(
							"	border-left: gray 1px solid;" + newLine).append(
							"	border-bottom: gray 1px solid;" + newLine).append(
							"}" + newLine).append( newLine).append(
							
							".sectionTitle {" + newLine).append(
							"	background-color: #874709;" + newLine).append(
							"	text-align: left;" + newLine).append( 
							"	font-size: 14pt;" + newLine).append(
							"	font-weight: bold;" + newLine).append(
							"}" + newLine).append( newLine).append(
							
							".heading {" + newLine).append(
							"	vertical-align: top;" + newLine).append( 
							"	border-right: gray 1px solid;" + newLine).append(
							"	border-top: gray 1px solid;" + newLine).append(
							"	border-left: gray 1px solid;" + newLine).append(
							"	border-bottom: gray 1px solid;" + newLine).append(
							"	font-size: 11pt;" + newLine).append(
							"	font-weight: bold;" + newLine).append(
							"	background-color: #efb885;" + newLine).append(
							"}" + newLine).append( newLine).append(

							".headingIssues {" + newLine).append(
							"	vertical-align: top;" + newLine).append(
							"	border-right: gray 1px solid;" + newLine).append(
							"	border-top: gray 1px solid;" + newLine).append(
							"	border-left: gray 1px solid;" + newLine).append(
							"	border-bottom: gray 1px solid;" + newLine).append(
							"	font-size: 11pt;" + newLine).append(
							"	font-weight: bold;" + newLine).append(
							"	background-color: #de9967;" + newLine).append(
							"}" + newLine).append( newLine).append(
							
							".issueClass {" + newLine).append(
							"	background-color: #783201;" + newLine).append(
							"}" + newLine).append( newLine).append(

							".postIssueClass {" + newLine).append( 
							"	background-color: #985321;" + newLine).append(
							"}" + newLine).append( newLine).append(
							
							".variantHeading {" + newLine).append(
							"	vertical-align: center;" + newLine).append(
							"	border-right: gray 1px solid;" + newLine).append(
							"	border-top: gray 1px solid;" + newLine).append(
							"	border-left: gray 1px solid;" + newLine).append(
							"	border-bottom: gray 1px solid;" + newLine).append(
							"	font-size: 11pt;" + newLine).append(
							"	font-weight: bold;" + newLine).append(
							"	background-color: white;" + newLine).append(
							"}" + newLine).append( newLine).append(
							
							".vulnerabilityText {" + newLine).append(
							"	border-right: gray 1px solid;" + newLine).append(
							"	border-top: gray 1px solid;" + newLine).append(
							"	border-left: gray 1px solid;" + newLine).append(
							"	border-bottom: gray 1px solid;" + newLine).append(
							"}" + newLine).append( newLine).append(

							".findingTable {" + newLine).append( 
							"	border-collapse: collapse;" + newLine).append(
							"	border-right: gray 2px solid;" + newLine).append(
							"	border-top: gray 2px solid;" + newLine).append(
							"	vertical-align: top;" + newLine).append(
							"	border-left: gray 2px solid;" + newLine).append(
							"	border-bottom: gray 2px solid;" + newLine).append(
							"	text-align: left;" + newLine).append(
							"}" + newLine).append( newLine).append(

							".threatTable {" + newLine).append(
							"	border-collapse: collapse;" + newLine).append(
							"	border-right: gray 2px solid;" + newLine).append(
							"	border-top: gray 2px solid;" + newLine).append(
							"	vertical-align: top;" + newLine).append(
							"	border-left: gray 2px solid;" + newLine).append(
							"	border-bottom: gray 2px solid;" + newLine).append(
							"	text-align: left;" + newLine).append(
							"}" + newLine).append( newLine).append(
							
							".hidden { display: none; }" + newLine).append(
							".unhidden { display: block; }" + newLine).append(
							"</style>" + newLine).append(
							"</head>" + newLine).append( newLine).append(
							"<body>" + newLine).append(
							"<script type=\"text/javascript\">" + newLine).append(
							"function unhide(divID) {" + newLine).append(
							"	var item = document.getElementById(divID);" + newLine).append(
							"	if (item) {" + newLine).append(
							"		item.className=(item.className==\'hidden\')?\'unhidden':\'hidden\'; " + newLine).append(
							"	}" + newLine).append(
							"}" + newLine).append(
							"</script>" + newLine).append(
							"<center>" + newLine).append(
							"<table style=\"width: 1000px\">" + newLine).append(
							"	<tr>" + newLine).append(
							"		<td>&nbsp;</td>" + newLine).append(
							"	</tr>" + newLine).append(
							"	<tr>" + newLine).append(
							"		<td style=\"text-align: center ; font-size: 20pt; color: #662b00; font-weight: bold;\">Web Application Report<br>" + newLine).append(
							"		<hr>" + newLine).append(	
							"		<div style=\"font-size: 14pt\">" + dateString + "</div><br>" + newLine).append( 
							"		</td>" + newLine).append(
							"	</tr>" + newLine).append(
							"	<tr>" + newLine).append(
							"		<td>&nbsp;</td>" + newLine).append(
							"	</tr>" + newLine).append(

							"	<tr style=\"text-align: left; font-size: 14pt; font-weight: bold;\">" + newLine).append(
							"		<td><font color=\"#874709\"> General Information<br>" + newLine).append(
							"		</font></td>" + newLine).append(
							"	</tr>" + newLine).append(
							"	<tr>" + newLine).append(
							"		<td>&nbsp;</td>" + newLine).append(
							"	</tr>" + newLine).append(
							"	<tr>" + newLine).append(
							"		<td>" + newLine).append(
							"		<table width=\"100%\">" + newLine).append(
							"			<tr>" + newLine).append(
							"				<td style=\"width: 10%%\">&nbsp;</td>" + newLine).append(
							"				<td style=\"text-align: left; font-size: 12pt;\" width=90%%><b><font" + newLine).append(
							"				color=\"#874709\">Scanner Information</font></b><br>" + newLine).append(
							"				<table width=\"80%\">" + newLine).append(
							"					<tr>" + newLine).append(
							"						<td>This report was generated by " + "Beowulf Scanner" + " 1.0.0" + newLine).append(
							"					</tr>" + newLine).append(
							"					<tr>" + newLine).append(
							"						<td>Scan start time: <i>" + webScanDocument.getScanStartTime() + "</i></td>" + newLine).append(
							"					</tr>" + newLine).append(
							"					<tr>" + newLine).append(
							"						<td>Scan finish time: <i>" + webScanDocument.getScanEndTime() + "</i></td>" + newLine).append(
							"					</tr>" + newLine).append(
							"				</table>" + newLine).append(
							"				</td>" + newLine).append(
							"			</tr>" + newLine).append(
							"		</table>" + newLine).append(
							"		</td>" + newLine).append(
							"	</tr>" + newLine).append(
							"	<tr>" + newLine).append(
							"		<td>&nbsp;</td>" + newLine).append(
							"	</tr>" + newLine).append(
							"	<tr>" + newLine).append(
							"		<td>" + newLine).append(
							"		<table width=\"100%\">" + newLine).append(
							"			<tr>" + newLine).append(
							"				<td style=\"width: 10%%\">&nbsp;</td>" + newLine).append(
							"				<td style=\"text-align: left; font-size: 12pt;\" width=90%%><b><font" + newLine).append(
							"				color=\"#874709\">Content Information</font></b><br>" + newLine).append(
							"				<table width=\"80%\">" + newLine).append(
							"					<tr>" + newLine).append(
							"						<td>This report consists of two sections:" + newLine).append(
							"						<ol>" + newLine).append(
							"							<a href=\"#Section1\"><li>Executive Summary</li></a>" + newLine).append(
							"							<a href=\"#Section2\"><li>Detailed Vulnerability Description</li></a>" + newLine).append(
							"						</ol></td>" + newLine).append(
							"					</tr>" + newLine).append(
							"				</table>" + newLine).append(
							"				</td>" + newLine).append(
							"			</tr>" + newLine).append(
							"		</table>" + newLine).append(
							"		</td>" + newLine).append(
							"	</tr>" + newLine).append(
							"	<tr>" + newLine).append(
							"		<td>&nbsp;</td>" + newLine).append(
							"	</tr>"+ newLine) ;
			
			
			
			try {
				writer.append(reportString.toString());
				
				/*
				 * Section 1: Executive Summary
				 * 
				 * Section 2: Detailed Vulnerability Description
				 * 
				 * Section 3: Application Discovery Details
				 */
				
				
				reportWriteExecutiveSection(writer);
				
				writer.append("	<tr>" + newLine).append( 
								"		<td>&nbsp;</td>" + newLine).append(
								"	</tr>" + newLine).append( newLine).append( 
								"	<tr>" + newLine).append(
								"		<td><p align=right><a href=\"#Section1\">[^] Back to Section 1: Executive Summary</a></p></td>" + newLine).append(
								"	</tr>" + newLine);
				
				
				reportWriteDetailedVulnerabilitySection(writer);
				
				writer.append("	<tr>" + newLine).append( 
						"		<td>&nbsp;</td>" + newLine).append(
						"	</tr>" + newLine).append( newLine).append(
						"	<tr>" + newLine).append(
						"		<td><p align=right><a href=\"#Section2\">[^] Back to Section 2: Detailed Vulnerability Description</a></p></td>" + newLine).append(
						"	</tr>" + newLine);
		
		
				//reportWriteApplicationDiscoverySection(writer);
		
				writer.append( "</table></center></body></html>");
				writer.flush();
				
			} catch (IOException e) {			
				logger.error("IOException while generating report", e);
				ReportException reportException = new ReportException("IOException while generating report: " + e.getMessage());
				reportException.initCause(e.getCause());
				throw reportException;
			}
			
			logger.debug("Report generation completed.");
			
		}


		private void reportWriteDetailedVulnerabilitySection(FileWriter writer) throws IOException 
		{
			/*
			 * 
			 * Caution: Dont make any format or indentation changes 
			 * 
			 * Please keep the formatting as it is. The current fomatting doesnt follow the conventional standards, but it is
			 * kept as it is to ease the readability of hardcoded HTML tags.
			 * 
			 * 
			 */
			
			
			writer.append("	<tr class=\"sectionTitle\">" + newLine).append( 
									"		<td><font color=\"white\"><a name=\"Section2\">2. Detailed Vulnerability Description</a><br>" + newLine).append(
									"		</font></td>" + newLine).append(
									"	</tr>" + newLine).append(

									"	<tr>" + newLine).append(
									"		<td>&nbsp;</td>" + newLine).append(
									"	</tr>" + newLine);
			
			writer.append("	<tr>" + newLine).append(
									"		<td>" + newLine).append(
									"		<table width=\"100%\">" + newLine);
			
			ReportIssueDAO reportIssueDAO = new ReportIssueDAO(scanInstanceDatastore);
			int BATCH_SIZE = 100;
			int offset = 0;		
			int totalIssues = (int) reportIssueDAO.count();
			int count = BATCH_SIZE <= totalIssues ? BATCH_SIZE: totalIssues;
			int vulnIter = 1;
			
			while(offset < totalIssues) {				
				for(ReportIssueDocument issue: reportIssueDAO.getReportIssueDocuments(offset, count)) 
				{
					String reasoningText = "";
					String remediationText = "";
					String referencesText = "";
					int variantCount = 0;
					//ReportElementIssueType issueType = reportCollectorEx.getIssueType(issue.getIssueTypeId());
					ReportThreatType threatType = issue.getThreatType();
					reasoningText = issue.getReasoning();
					remediationText = issue.getRemediation();
					referencesText = issue.getReferences();
					
					
					variantCount = 0;		
					writer.append("			<tr>" + newLine +
										"				<td style=\"width: 10%%\">&nbsp;</td>" + newLine +
										"				<td style=\"text-align: left; font-size: 12pt;\" width=90%%>" + newLine +
										"				<table width=\"100%\">" + newLine +
										"					<tr class=\"issueClass\">" + newLine + 
										"						<td colspan=2 style=\"text-align: left; font-size: 14pt;\"><font" + newLine +
										"						color=\"white\"><a name=\""+ vulnIter + "\">Issue#" + vulnIter + 
																"</a> "+ threatType.getName()  + "<br>" + newLine +
										"						</font></td>" + newLine +
										"					</tr>" + newLine +
										"					<tr class=\"postIssueClass\">" + newLine +
										"						<td colspan=2 style=\"text-align: left; font-size: 14pt;\"><font" + newLine +
										"						color=\"white\">Vulnerable URL: " + issue.getIssueUrl() +"<br>" + newLine +
										"						</font></td>" + newLine +
										"					</tr>" + newLine +
										"					<tr class=\"postIssueClass\">" + newLine +
										"						<td width=\"10%%\">&nbsp;</td>" + newLine +
										"						<td style=\"text-align: left; font-size: 12pt;\" width=90%%>" + newLine +
										"						<table width=\"100%\" class=\"findingTable\">" + newLine +
										"							<tr>" + newLine +
										"								<td style=\"width: 200px\" class=\"headingIssues\">Severity:</td>" + newLine +
										"								<td style=\"width: 600px\" class=\"vulnerabilityText\" bgcolor=\"white\">"+ issue.getThreatSeverityType().getFullName()+"</td>" + newLine +
										"							</tr>" + newLine +
										"							<tr>" + newLine +
										"								<td style=\"width: 200px\" class=\"headingIssues\">Reasoning:</td>" + newLine +
										"								<td style=\"width: 600px\" class=\"vulnerabilityText\" bgcolor=\"white\">"+ reasoningText +"</td>" + newLine +
										"							</tr>" + newLine +
										"							<tr>" + newLine +
										"								<td style=\"width: 200px\" class=\"headingIssues\">Threat details:</td>" + newLine +
										"								<td style=\"width: 600px\" class=\"vulnerabilityText\" bgcolor=\"white\"></td>" + newLine +
										"							</tr>" + newLine +
										"							<tr>" + newLine +
										"								<td style=\"width: 200px\" class=\"headingIssues\">Remediation:</td>" + newLine +
										"								<td style=\"width: 600px\" class=\"vulnerabilityText\" bgcolor=\"white\">" + remediationText + "</td>" + newLine +
										"							</tr>" + newLine +
										"							<tr>" + newLine +
										"								<td style=\"width: 200px\" class=\"headingIssues\">References:</td>" + newLine +
										"								<td style=\"width: 600px\" class=\"vulnerabilityText\" bgcolor=\"white\">" + referencesText + "</td>" + newLine +
										"							</tr>" + newLine );	
										
					
					
				
					List<ReportIssueVariantDocument> issueVariants = issue.getIssueVariants();
					for(ReportIssueVariantDocument issueVariant: issueVariants)
					{
						
						AbstractHttpTransaction originalTxn = getHttpTxn(issueVariant.getOrigicalTxn());
						AbstractHttpTransaction testTxn = getHttpTxn(issueVariant.getTestTxn());
						
						variantCount +=1;
						writer.append("							<tr>" + newLine +
												"								<td style=\"width: 200px\" class=\"variantHeading\">Variant " + Integer.toString(variantCount) + " of " + Integer.toString(issueVariants.size()) + "</td>" + newLine +
												"								<td style=\"width: 600px\" bgcolor=\"white\" class=\"vulnerabilityText\">" + newLine +
												"								<table>" + newLine +
												"									<tr>" + newLine +
												"										<td>&nbsp;</td>" + newLine +
												"									</tr>" + newLine +
												"									<tr>" + newLine +
												"										<td bgcolor=\"white\" style=\"font-size: 12pt;\"><font color=\"black\"><b>Description:</b></font></td>" + newLine + 
												"									</tr>" + newLine +
												"									<tr>" + newLine +
												"										<td bgcolor=\"white\">" + issueVariant.getDescription() +"</td>" + newLine +
												"									</tr>" + newLine +
												"									<tr>" + newLine +
												"										<td>&nbsp;</td>" + newLine +
												"									</tr>" + newLine +
												"									<tr>" + newLine +
												"										<td bgcolor=\"white\" style=\"font-size: 12pt;\"><font" + newLine +
												"										color=\"black\"><b>Transaction Details:</b></font></td>" + newLine +
												"									</tr>" + newLine +
												"									<tr>" + newLine +
												"										<td bgcolor=\"white\">Click here to see Original Transaction <a	href=\"javascript:unhide(\'" + variantCount + "_OriginalTransaction" + "\');\">>>></a>" + newLine +
												"										<noscript style=\"color:red\"><br>" + newLine +
												"										<i> Your browser does not support JavaScript!</i>" + newLine +
												"										<p style=\"font-size: 9pt; color: brown; font-style: italic\">Request:</p>" + newLine +
												getProtocolFormat(originalTxn.requestToString()) + 
												"										<p style=\"font-size: 9pt; color: brown; font-style: italic\">Response:</p>" + newLine +
												getProtocolFormat(originalTxn.responseToString()) + newLine +
												"										</noscript>" + newLine +
												"										<div id=\"" + variantCount + "_OriginalTransaction" + "\" class=\"hidden\">" + newLine +
												"										<p style=\"font-size: 9pt; color: brown; font-style: italic\">Request:</p>" + newLine +
												getProtocolFormat(originalTxn.requestToString()) + 
												"										<p style=\"font-size: 9pt; color: brown; font-style: italic\">Response:</p>" + newLine +
												getProtocolFormat(originalTxn.responseToString()) + newLine +
												"										</div>" + newLine +
												"										<br>Click here to see Test Transaction <a href=\"javascript:unhide(\'" + variantCount + "_TestTransaction" + "\');\">>>></a>" + newLine +
												"										<noscript style=\"color:red\"><br>" + newLine +
												"										<i> Your browser does not support JavaScript!</i>" + newLine +
												"										<p style=\"font-size: 9pt; color: brown; font-style: italic\">Request:</p>" + newLine +
												getProtocolFormat(testTxn.requestToString()) + 
												"										<p style=\"font-size: 9pt; color: brown; font-style: italic\">Response:</p>" + newLine +
												
												getProtocolFormat(testTxn.responseToString()) + newLine +
												"										</noscript>" + newLine +
												"										<div id=\"" + variantCount + "_TestTransaction" + "\" class=\"hidden\">" + newLine +
												"										<p style=\"font-size: 9pt; color: brown; font-style: italic\">Request:</p>" + newLine +
												
												getProtocolFormat(testTxn.requestToString()) + 
												"										<p style=\"font-size: 9pt; color: brown; font-style: italic\">Response:</p>" + newLine +
												
												getProtocolFormat(testTxn.responseToString()) + newLine +
												"										</div>" + newLine +
												"									</tr>" + newLine +
												"								</table>" + newLine +
												"								</td>" + newLine +
												"							</tr>" + newLine);
				
						
						
						
					}
					
					writer.append("						</table>" + newLine +
										"						</td>" + newLine +
										"					</tr>" + newLine +
										"				</table>" + newLine +
										"				</td>" + newLine +
										"			</tr>" + newLine );
					
					if(!((vulnIter+1) < totalIssues))
					{
					writer.append("			<tr>" + newLine +
										"				<td style=\"width: 10%%\">&nbsp;</td>" + newLine +
										"				<td style=\"text-align: left; font-size: 10pt;\" width=90%%>" + newLine +
										"				<table width=\"100%\">" + newLine +
										"					<tr>" + newLine + 
										"						<td><p align=left><a href=\"#" + Integer.toString(vulnIter) + "\">[<] Previous</a></p></td>" + newLine);
					if(!((vulnIter+2) < totalIssues))
					{
						
					writer.append(	"						<td><p align=right><a href=\"#" + Integer.toString(vulnIter + 2)+ "\">Next [>]</a></p></td>" + newLine +
										"					</tr>" + newLine +
										"					<tr>" + newLine +
										"						<td colspan =2><p align=right><a href=\"#Section2\">[^] Back to Section 2</a></p></td>" + newLine +
										"					</tr>" + newLine +
										"				</table>" + newLine +
										"				</td>" + newLine + 
										"			</tr>" + newLine);
						
											
					}
					else
					{
					writer.append(	"						<td><p align=right><a>Next [>]</a></p></td>" + newLine +
										"					</tr>" + newLine +
										"					<tr>" + newLine +
										"						<td colspan =2><p align=right><a href=\"#Section2\">[^] Back to Section 2</a></p></td>" + newLine +
										"					</tr>" + newLine +
										"				</table>" + newLine +
										"				</td>" + newLine + 
										"			</tr>" + newLine);
						
						
					}
					}					
								
				}
				writer.flush();
				
				offset = offset + count;
				if(offset+BATCH_SIZE < totalIssues)
					count = BATCH_SIZE;
				else 
					count = totalIssues - offset;
			}
				
			writer.append("		</table>" + newLine).append(
									"		</td>" + newLine).append(
									"	</tr>");
			
		}

		private AbstractHttpTransaction getHttpTxn(ObjectId txnObjId) {
			
			HttpTxnDAO txnDAO = new HttpTxnDAO(scanInstanceDatastore);
			HttpTxnDocument txnDocument = txnDAO.getHttpTxnDocument(txnObjId);
			
			AbstractHttpTransaction httpTransaction = AbstractHttpTransaction.getObject(txnDocument);
			return httpTransaction;
			

			
		}

		private void reportWriteExecutiveSection(FileWriter writer) throws IOException 
		{
			/*
			 * 
			 * Caution: Dont make any format or indentation changes 
			 * 
			 * Please keep the formatting as it is. The current fomatting doesnt follow the conventional standards, but it is
			 * kept as it is to ease the readability of hardcoded HTML tags.
			 * 
			 * 
			 */
			
			
			writer.append("	<tr class=\"sectionTitle\">" + newLine).append(
									"		<td><font color=\"white\"><a name=\"Section1\">1. Executive Summary</a><br>" + newLine).append(
									"			</font></td>" + newLine).append(
									"	</tr>" + newLine).append( newLine).append(

									"	<tr>" + newLine).append(
									"		<td>&nbsp;</td>" + newLine).append(
									"	</tr>" + newLine).append( newLine).append(

									"	<tr>" + newLine).append(
									"	<td>" + newLine).append(
									"		<table width=\"100%\">" + newLine).append(
									"			<tr>" + newLine).append(
									"				<td style=\"width: 10%%\">&nbsp;</td>" + newLine).append(
									"				<td style=\"text-align: left; font-size: 12pt;\" width=90%%><b><font" + newLine).append(
									"					color=\"#874709\">1.1 Security Risks</font></b><br>" + newLine).append(
									"				<table>" + newLine).append(
									"					<tr>" + newLine).append(
									"						<td>The following are the security risks that were identified" + newLine).append(
									"						in this scanning session. Refer <a href=\"#Section2\">Section 2</a>: Detailed Vulnerability" + newLine).append(
									"						Description for more information." + newLine).append(
									"							<ul>" + newLine).append(
									// Section 1.1 reasoning texts
									reportWriteSubSectionReasoning() + 
									
									"							</ul>" + newLine).append(
									"						</td>" + newLine).append(
									"					</tr>" + newLine).append(
									"				</table>" + newLine).append(
									"				</td>" + newLine).append(
									"			</tr>" + newLine).append(
									"		</table>" + newLine).append(
									"	</td>" + newLine).append(
									"	<tr>" + newLine).append(
									"		<td>&nbsp;</td>" + newLine).append(
									"	</tr>" + newLine).append(
									
									"	<tr>" + newLine).append(
									"		<td>" + newLine).append(
									"		<table width=\"100%\">" + newLine).append(
									"			<tr>" + newLine).append(
									"				<td style=\"width: 10%%\">&nbsp;</td>" + newLine).append( 
									"				<td style=\"text-align: left; font-size: 12pt;\" width=90%%><b><font" + newLine).append(
									"				color=\"#874709\">1.2 Host Details </font></b><br>" + newLine).append(
									"				<br>" + newLine).append(
									"				<table width=\"80%\">" + newLine).append(
									"					<tr>" + newLine).append(
									"						<td colspan=3>Enumerated host details are listed below: </td>" + newLine).append(								
									"					</tr>" + newLine).append(
									
									reportWriteSubSectionHostDetails() +
									
									"				</table>" + newLine).append(							
									"				</td>" + newLine).append(
									"			</tr>" + newLine).append(
									"		</table>" + newLine).append(
									"		</td>" + newLine).append(
									"	</tr>" + newLine).append(
									
									"	<tr>" + newLine).append(
									"		<td>" + newLine).append(
									"		<table width=\"100%\">" + newLine).append(
									"			<tr>" + newLine).append(
									"				<td style=\"width: 10%%\">&nbsp;</td>" + newLine).append( 
									"				<td style=\"text-align: left; font-size: 12pt;\" width=90%%><b><font" + newLine).append(
									"				color=\"#874709\">1.3 Issues per Host </font></b><br>" + newLine).append(
									"				<br>" + newLine).append(
									"				<table width=\"80%\" class=\"threatTable\">" + newLine).append(

									"					<tr>" + newLine).append(
									"						<td style=\"width: 400px\" class=\"heading\">Hosts</td>" + newLine).append(
									"						<td style=\"width: 100px\" class=\"heading\">High</td>" + newLine).append(
									"						<td style=\"width: 100px\" class=\"heading\">Medium</td>" + newLine).append(
									"						<td style=\"width: 100px\" class=\"heading\">Low</td>" + newLine).append(
									"						<td style=\"width: 100px\" class=\"heading\">Informational</td>" + newLine).append(
									"						<td style=\"width: 100px\" class=\"heading\">Total</td>" + newLine).append(
									"					</tr>" + newLine).append(
									//Writes the per host statistics
									reportWriteSubSectionPerHostIssueStatistics() +
								//	"					<tr>" + newLine).append(
								//	"						<td style=\"width: 400px\" class=\"vulnerabilityText\">http://www.example.com</td>" + newLine).append(
								//	"						<td style=\"width: 100px\" class=\"vulnerabilityText\">10</td>" + newLine).append(
								//	"						<td style=\"width: 100px\" class=\"vulnerabilityText\">20</td>" + newLine).append(
								//	"						<td style=\"width: 100px\" class=\"vulnerabilityText\">20</td>" + newLine).append(
								//	"						<td style=\"width: 100px\" class=\"vulnerabilityText\">20</td>" + newLine).append(
								//	"						<td style=\"width: 100px\" class=\"vulnerabilityText\">70</td>" + newLine).append(
								//	"					</tr>" + newLine).append(
									"				</table>" + newLine).append(
									"				</td>" + newLine).append(
									"			</tr>" + newLine).append(
									"		</table>" + newLine).append(
									"		</td>" + newLine).append(
									"	</tr>" + newLine).append(
									
									"	<tr>" + newLine).append(
									"		<td>&nbsp;</td>" + newLine).append(
									"	</tr>" + newLine).append(
									
									"	<tr>" + newLine).append(
									"		<td>" + newLine).append(
									"		<table width=\"100%\">" + newLine).append(
									"			<tr>" + newLine).append(
									"				<td style=\"width: 10%%\">&nbsp;</td>" + newLine).append(
									"				<td style=\"text-align: left; font-size: 12pt;\" width=90%%><b><font" + newLine).append(
									"				color=\"#874709\">1.4 Detected Threat Classes</font></b><br>" + newLine).append(
									"				<br>" + newLine).append(
									"				<table width=\"80%\" class=\"threatTable\">" + newLine).append(
									"					<tr>" + newLine).append(
									"						<td style=\"width: 400px\" class=\"heading\">Threat Name</td>" + newLine).append(
									"						<td style=\"width: 100px\" class=\"heading\">Severity</td>" + newLine).append(
									"						<td style=\"width: 100px\" class=\"heading\">Count</td>" + newLine).append(
									"					</tr>" + newLine).append(
									reportWriteSubSectionThreatClasses() + 
								//	"					<tr>" + newLine).append(
								//	"						<td style=\"width: 400px\" class=\"vulnerabilityText\">Command Execution: SQL Injection</td>" + newLine).append(
								//	"						<td style=\"width: 100px\" class=\"vulnerabilityText\">High</td>" + newLine).append(
								//	"						<td style=\"width: 100px\" class=\"vulnerabilityText\">2</td>" + newLine).append(
								//	"						<td style=\"width: 100px\" class=\"vulnerabilityText\">" + newLine).append(
								//	"						<a href=\"#issue1\">1</a>,<a href=\"#issue2\">2</a></td>" + newLine).append(
								//	"					</tr>" + newLine).append(			
									"				</table>" + newLine).append(
									"				</td>" + newLine).append(
									"			</tr>" + newLine).append(
									"		</table>" + newLine).append(
									"		</td>" + newLine).append(
									"	</tr>" + newLine) ;
			
			
		}

		private String reportWriteSubSectionHostDetails() 
		{
			StringBuilder hostDetails = new StringBuilder("");
			ReportHostDAO hostDAO = new ReportHostDAO(scanInstanceDatastore);
			ReportHostDocument hostDocument = hostDAO.find().get();
			{
				hostDetails.append("					<tr>" + newLine).append(
								"						<td colspan=2>&nbsp;</td>" + newLine).append(
								"					</tr>" + newLine).append(
								"					<tr>" + newLine).append(
								"						<td style=\"width: 100px;\">&nbsp;</td>" + newLine).append(
								"						<td style=\"text-align: left;\"><b>Hostname: <i>"+ hostDocument.getHostName() + "</i></b></td>" + newLine).append(
								"					</tr>" + newLine) ;
				
				
					
				hostDetails.append("					<tr>" + newLine).append(
								"						<td style=\"width: 100px;\">&nbsp;</td>" + newLine).append(
								"						<td style=\"text-align: left;\"><i>&nbsp;&nbsp;&nbsp;&nbsp;-"+ "IP" + ": " + hostDocument.getIpAddress()+"</i></td>" + newLine).append(
								"					</tr>" + newLine) ;
				
				hostDetails.append("					<tr>" + newLine).append(
						"						<td style=\"width: 100px;\">&nbsp;</td>" + newLine).append(
						"						<td style=\"text-align: left;\"><i>&nbsp;&nbsp;&nbsp;&nbsp;-"+ "Server" + ": " + hostDocument.getServerValue()+"</i></td>" + newLine).append(
						"					</tr>" + newLine) ;
				
				hostDetails.append("					<tr>" + newLine).append(
						"						<td style=\"width: 100px;\">&nbsp;</td>" + newLine).append(
						"						<td style=\"text-align: left;\"><i>&nbsp;&nbsp;&nbsp;&nbsp;-"+ "-Technology" + ": " + hostDocument.getTechnology()+"</i></td>" + newLine).append(
						"					</tr>" + newLine) ;
			}
			
			
			return hostDetails.toString();
		}

		private String reportWriteSubSectionThreatClasses() 
		{
			/*
			 * 
			 * Caution: Dont make any format or indentation changes 
			 * 
			 * Please keep the formatting as it is. The current fomatting doesnt follow the conventional standards, but it is
			 * kept as it is to ease the readability of hardcoded HTML tags.
			 * 
			 * 
			 */
			
			String formattedText = "\t\t\t\t\t";
			StringBuilder threatClassRowHigh = new StringBuilder("");
			StringBuilder threatClassRowMedium = new StringBuilder("");
			StringBuilder threatClassRowLow = new StringBuilder("");
			StringBuilder threatClassRowInfo = new StringBuilder("");
			StringBuilder threatClassFull = new StringBuilder("");
			
			ReportIssueDAO issueDAO = new ReportIssueDAO(scanInstanceDatastore);
			ReportThreatTypeDAO threatTypeDAO = new ReportThreatTypeDAO(SFControllerManager.getInstance().getDataStore());
			for(ReportThreatType threatType: ReportThreatType.values()) 
			{
				List<ReportIssueDocument> issueDocuments = issueDAO.findByThreatType(threatType, false);
				if(issueDocuments.size() > 0) {
					
					//ReportThreatTypeDocument threatTypeDocument = threatTypeDAO.get
					for(ReportIssueDocument issue: issueDocuments) {
						if(issue.getThreatSeverityType() == ThreatSeverityType.HIGH)
						{
								
							threatClassRowHigh.append(formattedText + "<tr>" + newLine).append(
													formattedText + "\t<td style=\"width: 400px\" class=\"vulnerabilityText\">"+ 	threatType.getName() +"</td>" + newLine).append(
													formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">High</td>" + newLine).append(
													formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">" + Integer.toString(issueDocuments.size()) + "</td>" + newLine).append(
													//formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">" + newLine );
													//formattedText + "<a href=\"#issue1\">1</a>,<a href=\"#issue2\">2</a></td>" + newLine).append(
													//for(Integer issueId: reportCollectorEx.getIssueIds(issueType.getId()))
													//{
															
														//threatClassRowHigh.append(formattedText + "\t<a href=\"#"+ Integer.toString(issueId) + "\">"+ Integer.toString(issueId) + "</a>," + newLine);						
														
													//}
							//threatClassRowHigh.append(formattedText + "\t</td>" + newLine +						
													formattedText + "</tr>" + newLine);
								
						}
						else if(issue.getThreatSeverityType() == ThreatSeverityType.MEDIUM)
						{
								
							threatClassRowMedium.append(formattedText + "<tr>" + newLine).append(
													formattedText).append("\t<td style=\"width: 400px\" class=\"vulnerabilityText\">").append(threatType.getName() + "</td>" + newLine).append(
													formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">Medium</td>" + newLine).append(
													formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">" + Integer.toString(issueDocuments.size()) + "</td>" + newLine).append(
													//formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">" + newLine) ;
													//formattedText + "<a href=\"#issue1\">1</a>,<a href=\"#issue2\">2</a></td>" + newLine +
													//for(Integer issueId: reportCollectorEx.getIssueIds(issueType.getId()))
													//{
															
													//	threatClassRowMedium.append(formattedText + "\t<a href=\"#"+ Integer.toString(issueId) + "\">"+ Integer.toString(issueId) + "</a>," + newLine);						
														
													//}
							//threatClassRowMedium.append(formattedText + "\t</td>" + newLine).append(						
													formattedText + "</tr>" + newLine);
								
						}
						else if(issue.getThreatSeverityType() == ThreatSeverityType.LOW)
						{
								
							threatClassRowLow.append(formattedText + "<tr>" + newLine).append(
													formattedText).append("\t<td style=\"width: 400px\" class=\"vulnerabilityText\">").append(threatType.getName() +"</td>" + newLine).append(
													formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">Low</td>" + newLine).append(
													formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">" + Integer.toString(issueDocuments.size()) + "</td>" + newLine).append(
													//formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">" + newLine) ;
													//formattedText + "<a href=\"#issue1\">1</a>,<a href=\"#issue2\">2</a></td>" + newLine +
													//for(Integer issueId: reportCollectorEx.getIssueIds(issueType.getId()))
													//{
															
													//	threatClassRowLow.append(formattedText + "\t<a href=\"#"+ Integer.toString(issueId) + "\">"+ Integer.toString(issueId) + "</a>," + newLine);						
														
													//}
							//threatClassRowLow.append(formattedText + "\t</td>" + newLine).append(						
													formattedText + "</tr>" + newLine);
								
						}
						else if(issue.getThreatSeverityType() == ThreatSeverityType.LOW)
						{
								
							threatClassRowInfo.append(formattedText + "<tr>" + newLine).append(
													formattedText + "\t<td style=\"width: 400px\" class=\"vulnerabilityText\">"+ threatType.getName() + "</td>" + newLine).append(
													formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">Info</td>" + newLine).append(
													formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">" + Integer.toString(issueDocuments.size()) + "</td>" + newLine).append(
													//formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">" + newLine) ;
													//formattedText + "<a href=\"#issue1\">1</a>,<a href=\"#issue2\">2</a></td>" + newLine).append(
													//for(Integer issueId: reportCollectorEx.getIssueIds(issueType.getId()))
													//{
															
													//	threatClassRowInfo.append(formattedText + "\t<a href=\"#"+ Integer.toString(issueId) + "\">"+ Integer.toString(issueId) + "</a>," + newLine);						
														
													//}
							//threatClassRowInfo.append(formattedText + "\t</td>" + newLine).append(						
													formattedText + "</tr>" + newLine);
								
						}					
					}
				}				
			}
			
			threatClassFull = threatClassRowHigh.append(threatClassRowMedium).append(threatClassRowLow).append(threatClassRowInfo) ;
		
			return threatClassFull.toString();
		}

		private String reportWriteSubSectionPerHostIssueStatistics() 
		{
			/*
			 * 
			 * Caution: Dont make any format or indentation changes 
			 * 
			 * Please keep the formatting as it is. The current fomatting doesnt follow the conventional standards, but it is
			 * kept as it is to ease the readability of hardcoded HTML tags.
			 * 
			 * 
			 */
			
			String formattedText = "\t\t\t\t\t";
			StringBuilder hostIssueStatistics = new StringBuilder("");
			Pattern p;
			String regexURL = "";
			String totalHostIssueStatistics = "";

			int hostIssueHigh = 0;
			int hostIssueMedium = 0;
			int hostIssueLow = 0;
			int hostIssueInfo = 0;
			int hostIssueTotal = 0;

			//Set<String> baseURIList = new HashSet<String>();
			boolean flag = false;

			ReportHostDAO hostDAO = new ReportHostDAO(scanInstanceDatastore);
			ReportHostDocument hostDocument = hostDAO.find().get();
			ReportIssueDAO issueDAO = new ReportIssueDAO(scanInstanceDatastore);
			{
				/*
				 * Only if the host is unique
				 */
				hostIssueHigh = (int) issueDAO.getCountOfIssuesByThreatSeverityType(ThreatSeverityType.HIGH);
				hostIssueMedium = (int) issueDAO.getCountOfIssuesByThreatSeverityType(ThreatSeverityType.MEDIUM);
				hostIssueLow = (int) issueDAO.getCountOfIssuesByThreatSeverityType(ThreatSeverityType.LOW);
				hostIssueInfo = (int) issueDAO.getCountOfIssuesByThreatSeverityType(ThreatSeverityType.INFO);			
				hostIssueTotal = hostIssueHigh + hostIssueMedium + hostIssueLow + hostIssueInfo;

				hostIssueStatistics.append(formattedText + "<tr>" + newLine).append(
									  formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">"+ hostDocument.getHostName() +"</td>" + newLine).append(
									  formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">"+ Integer.toString(hostIssueHigh) +"</td>" + newLine).append(
									  formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">"+ Integer.toString(hostIssueMedium) +"</td>" + newLine).append(
									  formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">"+ Integer.toString(hostIssueLow) +"</td>" + newLine).append(
									  formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">"+ Integer.toString(hostIssueInfo) +"</td>" + newLine).append(
									  formattedText + "\t<td style=\"width: 100px\" class=\"vulnerabilityText\">"+ Integer.toString(hostIssueTotal) +"</td>" + newLine).append(
									  formattedText + "<tr>" + newLine);
									
				
				
			
				totalHostIssueStatistics += hostIssueStatistics.toString();
				
			}
		
			return totalHostIssueStatistics;
		}
		
		private String getProtocolFormat(String text) {
			
			String dataStringFormatted = "";
			dataStringFormatted = "<p width=\"70%%\" style=\"font-size: 10pt; color: #c79262; border: 1px dashed darkgrey; white-space: pre-wrap;\">";
			dataStringFormatted += StringEscapeUtils.escapeHtml(text);
			dataStringFormatted += "</p>";
			
			return dataStringFormatted;
		}

		private String getVulnerableURLCount() 
		{
			return Integer.toString(10);
		}

		private String getBrokenURLCount() 
		{
			return String.valueOf(5);
		}

		private String getScannedURLCount() 
		{
			return String.valueOf(10);
		}

		private String reportWriteSubSectionReasoning() 
		{
					
			/*
			 * This section fetches the reasoning text from the issues.
			 * 
			 * Only one reasoning is fetched per detected issueType ID
			 * 
			 */
			StringBuilder reasoningText = new StringBuilder("");
			StringBuilder reasoningTextHigh = new StringBuilder("");
			StringBuilder reasoningTextMedium = new StringBuilder("");
			StringBuilder reasoningTextLow = new StringBuilder("");
			String formatText = "\t\t\t\t\t\t\t\t";			
			
			ReportIssueDAO issueDAO = new ReportIssueDAO(scanInstanceDatastore);
			
			for(ReportThreatType threatType: ReportThreatType.values()) {
			
				for(ReportIssueDocument issue: issueDAO.findByThreatType(threatType, false))
				{
					
					if(issue.getThreatSeverityType() == ThreatSeverityType.HIGH)
					{
						reasoningTextHigh = reasoningTextHigh.append(formatText)
															 .append("<li>")
															 .append(issue.getReasoning())
															 .append("</li>").append(newLine);
									
					}
					else if(issue.getThreatSeverityType() == ThreatSeverityType.MEDIUM)
					{
						reasoningTextMedium = reasoningTextMedium.append(formatText)
																 .append("<li>").append(issue.getReasoning())
																 .append("</li>").append(newLine);									
					
					}
					else if(issue.getThreatSeverityType() ==  ThreatSeverityType.LOW)
					{
						reasoningTextLow = reasoningTextLow.append(formatText).append("<li>")
										 				   .append(issue.getReasoning())
										 				   .append("</li>").append(newLine);								
					}
					
					break;				
				}
			}
				
			
			
			reasoningText = reasoningText.append(reasoningTextHigh).append(reasoningTextMedium).append(reasoningTextLow);
			
			return reasoningText.toString();
		}

//		private String reportWriteFormatText(ReportFormatText fmtText)
//		{
//			StringBuilder formattedText = new StringBuilder("");
//			
//			for (ReportFormatableString fmtString : fmtText.getFormatStringList()) 
//			{
//				formattedText.append(reportWriteFormtString(fmtString));
//			}
//			
//			return formattedText.toString();
//		}
//		
//		private String reportWriteFormtString()
//		{
//			String dataStringFormatted = "";
//			String dataString = fmtString.toString();
//
//			if(dataString == null) {
//				dataString = "";
//			}
//			
//			dataString = HtmlUtils.escapeHTML(dataString);
//
//			if(fmtString.isFormatTypeCode()) 
//			{
//				dataStringFormatted = "<p width=\"70%%\" style=\"font-size: 10pt; color: #c79262; border: 1px dashed darkgrey; white-space: pre-wrap;\">";
//				dataStringFormatted += dataString;
//				dataStringFormatted += "</p>";
//			}
//			else if(fmtString.isFormatTypeText()) 
//			{
//				//dataStringFormatted += dataString;
//				dataStringFormatted = "<p width=\"70%%\" style=\"white-space: pre-wrap;\">";
//				dataStringFormatted += dataString;
//				dataStringFormatted += "</p>";
//				
//			}
//			else if(fmtString.isFormatTypePre()) 
//			{
//				dataStringFormatted = "<pre>";
//				dataStringFormatted += dataString;
//				dataStringFormatted += "</pre>";
//			}
//			else if(fmtString.isFormatTypeProtocol()) 
//			{
//				dataStringFormatted = "<p width=\"70%%\" style=\"font-size: 10pt; color: #c79262; border: 1px dashed darkgrey; white-space: pre-wrap;\">";
//				dataStringFormatted += dataString;
//				dataStringFormatted += "</p>";
//			}
//			else if(fmtString.isFormatTypeLineBreak()) 
//			{
//				dataStringFormatted = "<br>";
//				
//			}
//			else 
//			{
//				/* RAW */
//				dataStringFormatted =  dataString;
//			
//			}
//			
//			return dataStringFormatted;
//		}
		


}
