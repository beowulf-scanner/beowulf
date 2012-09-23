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
//import com.ivizsecurity.verimo.buttercup.model.jaxb.snapshots.Snapshots;
//import com.ivizsecurity.verimo.buttercup.transformers.SnapshotTransformer;
//import com.sun.jersey.api.NotFoundException;
//
//@Path("/scan/{id}/snapshot")
//public class SnapshotResource {
//
//	@Context
//	UriInfo uriInfo;
//
//	static Logger logger = Logger.getLogger(SnapshotResource.class);
//
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Response getSnapshots(@PathParam("id") final String id) {
//
//		logger.debug("getSnapshot(): processing scan id: " + id);
//		ObjectId objectId = null;
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			
//			try {
//				
//				Snapshots snapshots = SnapshotTransformer.doTransform(ScanAdapter.getSnapShotEntries(objectId));
//				return Response.ok().entity(snapshots).build();
//
//			} catch (ScanRedirectException sre) {
//				
//				/* redirect to host that is doing/done the scan */
//				UriBuilder ub = uriInfo.getBaseUriBuilder();
//				URI absURI = uriInfo.getAbsolutePath();				
//				URI redirectURI = ub.scheme(absURI.getScheme())
//									.path(absURI.getPath())
//									.port(absURI.getPort())
//									.host(sre.getRedirect())
//									.build();
//				throw new WebApplicationException(Response.temporaryRedirect(redirectURI).build());
//
//			} catch (ScanNotFoundException e) {
//				throw new NotFoundException("Scan with id '" + id + "' is not found");
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//
//	}
//
//}
