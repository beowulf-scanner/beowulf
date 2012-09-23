package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.net.URI;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriBuilder;
//import javax.ws.rs.core.UriInfo;
//
//import org.apache.log4j.Logger;
//import org.bson.types.ObjectId;
//
//import com.ivizsecurity.verimo.buttercup.core.scan.ScanAdapter;
//import com.ivizsecurity.verimo.buttercup.exceptions.GenericElementIdException;
//import com.ivizsecurity.verimo.buttercup.exceptions.ScanNotFoundException;
//import com.ivizsecurity.verimo.buttercup.exceptions.ScanRedirectException;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.issues.Issue;
//import com.ivizsecurity.verimo.buttercup.transformers.IssueTransformer;
//import com.ivizsecurity.verimo.buttercup.utils.ScanRequestUtils;
//import com.ivizsecurity.verimo.scanner.scan.reportingex.app.issuecategory.ReportElementIssue;
//import com.sun.jersey.api.NotFoundException;
//
//@Path("/scan/{id}/issue/{issue_id}")
public class IssueResource {
//	
//	@Context UriInfo uriInfo;
//
//	private static Logger logger = Logger.getLogger(IssueResource.class);
//
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Issue getIssue(@PathParam("id") String id, @PathParam("issue_id") String issueId) {
//
//		logger.debug("getIssue(): Getting information for Scan id: " + id + " and Issue Id" + issueId);
//
//		Issue issue = null;
//		ObjectId objectId = null;
//
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			try {
//				issue = prepareResponse(objectId, issueId);
//				
//			} catch (ScanRedirectException e) {
//				
//				UriBuilder ub = uriInfo.getBaseUriBuilder();
//				URI absURI = uriInfo.getAbsolutePath();				
//				URI redirectURI = ub.scheme(absURI.getScheme())
//									.path(absURI.getPath())
//									.port(absURI.getPort())
//									.host(e.getRedirect())
//									.build();
//				
//				logger.info(e.getMessage());				
//				throw new WebApplicationException(Response.temporaryRedirect(redirectURI)
//														.build());
//				
//			} catch (ScanNotFoundException e) {
//				throw new NotFoundException("Scan - id '" + id + "' is not found ");
//			} catch (GenericElementIdException e) {
//				throw new NotFoundException(e.getMessage());
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//
//		if (issue != null)
//			return issue;
//		else {
//			throw new NotFoundException("Issue -id '" + issueId + "' is not found");
//		}
//
//	}
//
//	/**
//	 * This method puts Issue in the databases
//	 * 
//	 * @param id
//	 * @param issueId
//	 * @param reqIssue
//	 * @return Response
//	 */
//	@PUT
//	@Consumes(MediaType.APPLICATION_XML)
//	@Produces(MediaType.APPLICATION_XML)
//	public Response updateIssue(@PathParam("id") String id, @PathParam("issue_id") String issueId, Issue reqIssue) {
//
//		logger.debug("Updating information for Scan id: " + id + " and Issue Id" + issueId);
//		/*
//
//		ObjectId objectId = null;
//		Errors errors = new Errors();
//
//		if (ObjectId.isValid(id) && ScanRequestUtils.validateIssueId(reqIssue, issueId, errors)) {
//			objectId = new ObjectId(id);
//
//		} else {
//			if (errors.errorList.size() > 0) {
//				throw new WebApplicationException(Response.status(400).entity(errors).build());
//			}
//			throw new WebApplicationException(400);
//		}
//
//		ReportElementIssue reportElementIssue = null;
//		try {
//			
//			reportElementIssue = ScanAdapter.getRepElementIssue(objectId, issueId);
//			
//		} catch (ScanRedirectException e) {
//			
//			
//		} catch (ScanNotFoundException e) {
//			
//		}
//
//		Issue jaxbResponseIssue = null;
//
//		if (reportElementIssue != null) {
//
//			ScanRequestUtils.validateIssue(reqIssue, reportElementIssue, errors);
//			if (errors.errorList.size() == 0) {
//				putIssuetoDB(objectId, reqIssue, reportElementIssue);
//				jaxbResponseIssue = prepareResponse(objectId, issueId);
//
//			} else {
//				throw new WebApplicationException(Response.status(400).entity(errors).build());
//			}
//
//		} else {
//			throw new WebApplicationException(400);
//		}
//		logger.debug("End updateIssue");
//
//		return Response.ok().entity(jaxbResponseIssue).build();
//		*/
//		
//		return null;
//
//	}
//
//	/**
//	 * Puts Issue to DB.
//	 * 
//	 * @param id
//	 * @param reqIssue
//	 * @param reportElementIssue
//	 * @return boolean
//	 */
//	/*private void putIssuetoDB(ObjectId id, Issue reqIssue, ReportElementIssue reportElementIssue) {
//
//		logger.debug("Starting put Issue to Db");
//
//		ReportElementIssue issue = IssueTransformer.doTransform(reportElementIssue, reqIssue);
//
//		issue.save();
//
//	}*/
//
//	/**
//	 * Prepares Issue
//	 * 
//	 * @param ObjectId
//	 * @return Issue
//	 * @throws ScanNotFoundException 
//	 * @throws ScanRedirectException 
//	 * @throws GenericElementIdException 
//	 */
//	private Issue prepareResponse(ObjectId id, String issueId) 
//			throws ScanRedirectException, ScanNotFoundException, GenericElementIdException {
//
//		logger.debug("Preparing response for id: " + id.toString());
//
//		Issue jaxbIssue = null;
//
//		ReportElementIssue issue = ScanAdapter.getRepElementIssue(id, ScanRequestUtils.extractIssueIdValue(issueId));
//
//		if (issue != null) {
//			jaxbIssue = IssueTransformer.doTransform(issue);
//		}
//
//		return jaxbIssue;
//
//	}
//
}
