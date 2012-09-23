//package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.net.URI;
//import java.util.List;
//
//import javax.ws.rs.GET;
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
//import com.ivizsecurity.verimo.buttercup.exceptions.ScanNotFoundException;
//import com.ivizsecurity.verimo.buttercup.exceptions.ScanRedirectException;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.issues.Issues;
//import com.ivizsecurity.verimo.buttercup.transformers.IssuesTransformer;
//import com.ivizsecurity.verimo.scanner.scan.reportingex.app.issuecategory.ReportElementIssue;
//import com.sun.jersey.api.NotFoundException;
//
//@Path("/scan/{id}/issues")
//public class IssuesResource {
//
//	@Context UriInfo uriInfo;
//	
//	private static Logger logger = Logger.getLogger(IssuesResource.class);
//
//	@GET
////	@Produces(MediaType.APPLICATION_XML)
//	public Issues getIssueTypes(@PathParam("id") String id) {
//
//		logger.debug("Getting all Issues information for Scan id: " + id);
//
//		Issues issues = null;
//		ObjectId objectId = null;
//
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			try {
//				issues = prepareResponse(objectId);
//			} catch (ScanRedirectException e) {
//				UriBuilder ub = uriInfo.getBaseUriBuilder();
//				URI absURI = uriInfo.getAbsolutePath();				
//				URI redirectURI = ub.scheme(absURI.getScheme())
//									.path(absURI.getPath())
//									.port(absURI.getPort())
//									.host(e.getRedirect())
//									.build();
//				
//				logger.info(e.getMessage());				
//				throw new WebApplicationException(Response.created(redirectURI)
//														.status(302)
//														.build());
//				
//			} catch (ScanNotFoundException e) {
//				throw new NotFoundException("Scan - id '" + id + "' is not found ");
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//
//		if (issues != null)
//			return issues;
//		else {
//			throw new NotFoundException("Issues is not found ");
//		}
//
//	}
//
//	/**
//	 * Prepares Issue
//	 * 
//	 * @param ObjectId
//	 * @return Issue
//	 * @throws ScanNotFoundException 
//	 * @throws ScanRedirectException 
//	 */
//	private Issues prepareResponse(ObjectId id) throws ScanRedirectException, ScanNotFoundException {
//
//		logger.debug("Preparing response for id: " + id.toString());
//
//		Issues jaxbIssues = null;
//
//		List<ReportElementIssue> issueList = ScanAdapter.getRepElementIssueList(id);
//
//		if (issueList != null) {
//			jaxbIssues = IssuesTransformer.doTransform(issueList);
//		}
//
//		return jaxbIssues;
//
//	}
//
//	
//}
