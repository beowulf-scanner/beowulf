//package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.net.URI;
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
//import com.ivizsecurity.verimo.buttercup.model.jaxb.summaryinfo.Summaries;
//import com.ivizsecurity.verimo.buttercup.transformers.SummariesTransformer;
//import com.ivizsecurity.verimo.scanner.scan.reportingex.app.generalcategory.ReportElementSummary;
//import com.sun.jersey.api.NotFoundException;
//
///**
// * Resource resposible for providing summary information corresponding to a scan ID.
// * @author deepak
// *
// */
//
//@Path("/scan/{id}/summary")
//public class SummaryResource {
//	
//	@Context UriInfo uriInfo;
//
//	private static Logger logger = Logger.getLogger(SummaryResource.class);
//
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Summaries getScanSummary(@PathParam("id") String id) {
//
//		logger.debug("Start preparing scan summary for the id: " + id);
//
//		Summaries summaries = null;
//
//		ObjectId objectId = null;
//
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			try {
//				summaries = prepareResponse(objectId);
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
//		if (summaries != null) {
//			return summaries;
//		} else {
//			throw new WebApplicationException(Response.status(500)
//					.entity("Server error while fetching summaries")
//					.build());
//		}
//
//	}
//
//	/**
//	 * This method prepares Summaries Object corresponding to a Object id.
//	 * @param ObjectId
//	 * @return Summaries
//	 * @throws ScanNotFoundException 
//	 * @throws ScanRedirectException 
//	 */
//	private Summaries prepareResponse(ObjectId id) throws ScanRedirectException, ScanNotFoundException {
//
//		logger.debug("Preparing response for id: " + id.toString());
//
//		Summaries summaries = null;
//		ReportElementSummary reportElementSummary = ScanAdapter.getSummary(id);
//
//		if (reportElementSummary != null) {
//
//			summaries = SummariesTransformer.doTransform(reportElementSummary);
//		}
//
//		return summaries;
//
//	}
//
//}
