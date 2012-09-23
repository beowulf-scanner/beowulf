//package com.nvarghese.beowulf.smf.scan.resources;
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
//import com.ivizsecurity.verimo.buttercup.model.jaxb.issuetypes.Issuetype;
//import com.ivizsecurity.verimo.buttercup.transformers.IssueTypeTransformer;
//import com.ivizsecurity.verimo.buttercup.utils.ScanRequestUtils;
//import com.ivizsecurity.verimo.scanner.scan.reportingex.app.issuecategory.ReportElementIssueType;
//import com.sun.jersey.api.NotFoundException;
//
//@Path("/scan/{id}/issuetype/{issuetypeid}")
//public class IssueTypeResource {
//
//	@Context UriInfo uriInfo;
////	
//	private static Logger logger = Logger.getLogger(IssueTypeResource.class);
//
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Issuetype getIssueType(@PathParam("id") String id, @PathParam("issuetypeid") String issueTypeId) {
//
//		logger.debug("Processing getIssueType, Getting information for id: " + id);
//
//		Issuetype issueType = null;
//		ObjectId objectId = null;
//
//		if (ObjectId.isValid(id) && issueTypeId != null) {
//			objectId = new ObjectId(id);
//			try {
//				issueType = prepareResponse(objectId, issueTypeId);
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
//			} catch (GenericElementIdException e) {
//				throw new NotFoundException(e.getMessage());
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//
//		if (issueType != null)
//			return issueType;
//		else {
//			throw new NotFoundException("IssueType with id '" + id + "' is not found");
//		}
//
//	}
//
//	@PUT
//	@Consumes(MediaType.APPLICATION_XML)
//	@Produces(MediaType.APPLICATION_XML)
//	public Issuetype putIssueType(@PathParam("id") String id, @PathParam("issuetypeid") String issueTypeId,
//			Issuetype requestIssueType) {
//
//		//TODO
//		return null;
//
//	}
//
//	/**
//	 * Prepares Issuetype
//	 * 
//	 * @param issueTypeId
//	 * 
//	 * @param ObjectId
//	 * @return Issuetype
//	 * @throws ScanNotFoundException 
//	 * @throws ScanRedirectException 
//	 * @throws GenericElementIdException 
//	 */
//	private Issuetype prepareResponse(ObjectId id, String issueTypeId) 
//			throws ScanRedirectException, ScanNotFoundException, GenericElementIdException {
//
//		logger.debug("Preparing response for IssueType Resource id: " + id.toString());
//
//		Issuetype jaxbIssueType = null;
//
//		ReportElementIssueType issueType = ScanAdapter.getRepEleIssueType(id, ScanRequestUtils.extractIssueTypeIdValue(issueTypeId));
//
//		if (issueType != null) {
//			jaxbIssueType = IssueTypeTransformer.doTransform(issueType);
//		}
//
//		return jaxbIssueType;
//
//	}
//
//}
