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
//import com.ivizsecurity.verimo.buttercup.model.jaxb.issuetypes.Issuetypes;
//import com.ivizsecurity.verimo.buttercup.transformers.IssueTypesTransformer;
//import com.ivizsecurity.verimo.scanner.scan.reportingex.app.issuecategory.ReportElementIssueType;
//import com.sun.jersey.api.NotFoundException;
//
///**
// * This resource is responsible for providing response to the request made in
// * specified path "/scan/{id}/issuetypes". 
// * 
// * @author deepak
// * 
// */
//
//@Path("/scan/{id}/issuetypes")
//public class IssueTypesResource {
//	
//	@Context UriInfo uriInfo;
//
//	private static Logger logger = Logger.getLogger(IssueTypesResource.class);
//
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Issuetypes getIssueTypes(@PathParam("id") String id) {
//
//		logger.debug("Getting information for id: " + id);
//
//		Issuetypes issueTypes = null;
//		ObjectId objectId = null;
//
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			try {
//				issueTypes = prepareResponse(objectId);
//			
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
//		if (issueTypes != null)
//			return issueTypes;
//		else {
//			throw new NotFoundException("IssueTypes is not found");
//		}
//
//	}
//
//	/**
//	 * Prepares Issuetypes
//	 * @param ObjectId
//	 * @return Issuetypes
//	 * @throws ScanNotFoundException 
//	 * @throws ScanRedirectException 
//	 */
//	private Issuetypes prepareResponse(ObjectId id) throws ScanRedirectException, ScanNotFoundException {
//
//		logger.debug("Preparing response for id: " + id.toString());
//
//		Issuetypes issueTypes = null;
//		
//		List<ReportElementIssueType> issueTypeList = ScanAdapter.getListOfRepEleIssueType(id); 
//
//		if (issueTypeList != null) {
//			issueTypes = IssueTypesTransformer.doTransform(issueTypeList);
//		}
//
//		return issueTypes;
//
//	}
//
//}
