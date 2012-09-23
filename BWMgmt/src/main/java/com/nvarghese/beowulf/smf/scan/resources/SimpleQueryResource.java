//package com.nvarghese.beowulf.smf.scan.resources;
//
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.MediaType;
//
//import org.apache.log4j.Logger;
//import org.bson.types.ObjectId;
//
//import com.ivizsecurity.verimo.buttercup.dao.MongoDBService;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.scanrequest.ScanRequest;
//import com.ivizsecurity.verimo.buttercup.model.mongo.WebScanDocument;
//import com.ivizsecurity.verimo.buttercup.transformers.ScanRequestTransformer;
//import com.sun.jersey.api.NotFoundException;
//
///**
// * This resource provides answers to simple queries such as start time, expected
// * time , percentage done as defined in requirement.
// * 
// * @author deepak
// * 
// */
//
//@Path("/scan/{id}")
//public class SimpleQueryResource {
//
//	private static Logger logger = Logger.getLogger(SimpleQueryResource.class);
//
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public ScanRequest getSimpleInfo(@PathParam("id") String id) {
//
//		logger.debug("Getting information for id: " + id);
//		
//		ScanRequest scanRequest = null;
//		ObjectId objectId = null;
//		
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			scanRequest = prepareResponse(objectId);
//		} else {
//			throw new WebApplicationException(400);
//		}
//
//		if (scanRequest != null)
//			return scanRequest;
//		else {
//			throw new NotFoundException("Scan with id '" + id + "' is not found");
//		}
//
//	}
//
//	/**
//	 * 
//	 * @param id
//	 * @return ScanRequest
//	 */
//	private ScanRequest prepareResponse(ObjectId id) {
//
//		logger.debug("Preparing response for id: " + id.toString());
//		
//		ScanRequest scanRequest = null;
//		WebScanDocument scanDoc = MongoDBService.getWebScanDocument(id);
//		
//		if(scanDoc != null) {		
//			scanRequest = ScanRequestTransformer.doTransform(scanDoc);
//		} 
//
//		return scanRequest;
//
//	}
//
//}
