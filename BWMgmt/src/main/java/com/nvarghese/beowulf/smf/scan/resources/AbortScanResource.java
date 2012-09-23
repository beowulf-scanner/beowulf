package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.net.URI;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//import javax.ws.rs.core.UriBuilder;
//import javax.ws.rs.core.UriInfo;
//import javax.xml.bind.JAXBException;
//
//import org.bson.types.ObjectId;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//
//@Path("/scan/{id}/abort")
public class AbortScanResource {
//
//	@Context UriInfo uriInfo;
//	
//	private static Logger logger = LoggerFactory.getLogger(AbortScanResource.class);	
//
//	/**
//	 * Process Abort Request with a given reason. If first time then returns
//	 * submitted otherwise returns already submitted. If any occurs happens then
//	 * returns error. When aborted simply return HTTP status code 200.
//	 */
//	@POST
//	@Consumes(MediaType.APPLICATION_XML)
//	@Produces(MediaType.APPLICATION_XML)
//	public Response postAbort(final @PathParam("id") String id, final Reason reason) {
//
//		logger.debug("postAbort() processing scan id: " + id);
//
//		Response response = null;
//
//		ObjectId objectId = null;
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//
//			WebScanDocument document = MongoDBService.getWebScanDocument(objectId);
//			Status status = new Status();
//
//			if (document != null) {
//
//				if (ScanRequestUtils.checkScannerIP(document)) {
//					/* scan is/was running in this machine */
//					
//					ScanPhase scanPhase = ScanPhase.getScanPhase(document.getScanPhase());
//
//					if (scanPhase == ScanPhase.ERROR || scanPhase == null) {
//
//						Errors errors = new Errors();
//						errors.errorList.add(document.getLastError());
//						throw new WebApplicationException(Response.status(500).entity(errors).build());
//
//					} else if (document.isAbortRequested() && !scanPhase.isPassiveState()) {
//
//						if (scanPhase == ScanPhase.ABORTED) {
//
//							status.value = AbortStatusMessage.ABORTED.getMessage();
//							response = Response.status(200).entity(status).build();
//
//						} else if(document.isScanRunning() && scanPhase == ScanPhase.ABORTING){
//
//							status.value = AbortStatusMessage.ALREADY_SUBMITTED.getMessage();
//							response = Response.status(200).entity(status).build();
//						} else {
//							
//							Errors errors = new Errors();
//							errors.errorList.add("Something terrible happened. Request after some time");
//							throw new WebApplicationException(Response.status(500).entity(errors).build());
//						}
//
//					} else {
//
//						if (document.isScanRunning()) {
//
//							document.setAbortRequested(true);
//							document.setAbortingReason(reason.getValue());
//							document.setAbortReasonId(reason.getId());
//							MongoDBService.save(document);
//
//							AbortScanJob abortScanJob = new AbortScanJob(document);
//							ScanQueue.getInstance().submit(abortScanJob);
//
//							status.value = AbortStatusMessage.SUBMITTED.getMessage();
//							response = Response.status(202).entity(status).build();
//
//						} else {
//
//							status.value = AbortStatusMessage.NOT_ALLOWED.getMessage();
//							response = Response.status(200).entity(status).build();
//
//						}
//					}
//				} else {
//					
//					/* redirect to host that is doing the scan */
//					
//					UriBuilder ub = uriInfo.getBaseUriBuilder();
//					URI absURI = uriInfo.getAbsolutePath();				
//					URI redirectURI = ub.scheme(absURI.getScheme())
//										.path(absURI.getPath())
//										.port(absURI.getPort())
//										.host(document.getScannerIP())
//										.build();			
//									
//					throw new WebApplicationException(
//							Response.created(redirectURI)
//									.status(302)
//									.build());
//				}
//			} else {
//				response = Response.status(Status.NOT_FOUND).build();
//			}
//
//		} else {
//			response = Response.status(Status.BAD_REQUEST).build();
//		}
//
//		return response;
//	}
//
//	/**
//	 * Returns Aborting reasons along with a HTTP Status code of created (202)
//	 * 
//	 * @param id
//	 * @return
//	 * @throws JAXBException
//	 */
//	@GET
//	@Consumes(MediaType.APPLICATION_XML)
//	@Produces(MediaType.APPLICATION_XML)
//	public Response getAbortReason(@PathParam("id") final String id) {
//
//		logger.debug("getAbortReason() processing scan id: " + id);
//
//		ObjectId objectId = null;
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//
//			WebScanDocument document = MongoDBService.getWebScanDocument(objectId);
//
//			if (document != null) {
//				Reasons reasons = ReasonsDispatchService.getScanAbortReasons();
//				return Response.ok(reasons).build();
//			} else {
//				throw new NotFoundException("Scan with id '" + id + "' is not found");
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//	}
}
