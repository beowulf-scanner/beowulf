//package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriBuilder;
//import javax.ws.rs.core.UriInfo;
//import javax.xml.transform.sax.SAXSource;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.configuration.XMLConfiguration;
//import org.apache.log4j.Logger;
//import org.bson.types.ObjectId;
//
//import com.ivizsecurity.verimo.buttercup.core.scan.events.EventCollector;
//import com.ivizsecurity.verimo.buttercup.core.scan.jobs.SubmitScanJob;
//import com.ivizsecurity.verimo.buttercup.core.scan.queues.ScanQueue;
//import com.ivizsecurity.verimo.buttercup.dao.MongoDBService;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.Errors;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.scanrequest.Baseuris;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.scanrequest.Comments;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.scanrequest.Jobs;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.scanrequest.ObjectFactory;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.scanrequest.ScanRequest;
//import com.ivizsecurity.verimo.buttercup.model.mongo.ScanConfigDocument;
//import com.ivizsecurity.verimo.buttercup.model.mongo.WebScanDocument;
//import com.ivizsecurity.verimo.buttercup.mongo.MongoDocumentType;
//import com.ivizsecurity.verimo.buttercup.mongo.factories.MongoDocumentFactory;
//import com.ivizsecurity.verimo.buttercup.services.ScanConfigValidatorService;
//import com.ivizsecurity.verimo.buttercup.transformers.ScanConfigTransformer;
//import com.ivizsecurity.verimo.scanner.scan.ScanConfigSection;
//import com.ivizsecurity.verimo.scanner.scan.functors.StringBeginsWithPredicate;
//
//
///**
// * The REST resource file responsible for submission of new 
// * web scan requests
// * 
// * @author nibin
// *
// */
//
//@Path("/scan/new")
//public class NewScanResource {
//	
//	@Context UriInfo uriInfo;
//	
//	private static Logger logger = Logger.getLogger(NewScanResource.class);
//	
//	@POST
//	@Consumes(MediaType.APPLICATION_XML)
//	@Produces(MediaType.APPLICATION_XML)	
//	public Response postScanRequest(SAXSource saxSource) { 
//		
//		logger.debug("postScanRequest(): Received a scan request");
//		ScanRequest scanRequest = null;
//		Response response = null;
//		boolean valid = false;
//		EventCollector scEventCollector = new EventCollector();
//		XMLConfiguration scanConfig = new XMLConfiguration();		
//		valid = ScanConfigValidatorService.doValidate(scanConfig, saxSource, ScanConfigSection.ALL, scEventCollector);
//		
//		if(valid == false) {
//			Errors errors = new Errors();
//			errors.errorList = scEventCollector.getEvents();
//			throw new WebApplicationException(Response.status(400).entity(errors).build());
//		} else {
//			List<String> baseURIs = new ArrayList<String>();
//			ObjectId id = new ObjectId();			
//			WebScanDocument webScanDocument = (WebScanDocument) MongoDocumentFactory.createDocument(MongoDocumentType.WEB_SCAN_DOCUMENT);
//			ScanConfigDocument scanConfigDocument = webScanDocument.getScanConfig();			
//			webScanDocument.setId(id);
//			
//			ScanConfigTransformer.doTransform(scanConfigDocument, scanConfig);
//			StringBeginsWithPredicate baseURIpredicate = new StringBeginsWithPredicate("scan_settings.base_uris.base_uri"); 
//			Collection<String> keys = CollectionUtils.select(scanConfigDocument.getKeySet(), baseURIpredicate);			
//			
//			Iterator<String> it = keys.iterator();
//			while(it.hasNext()) {
//				String key = (String) it.next();
//				baseURIs.add((String) scanConfigDocument.getValue(key));
//			}
//			
//			webScanDocument.setBaseUris(baseURIs);
//			webScanDocument.addComment("Successfully parsed scan configuration");
//			MongoDBService.save(webScanDocument);			
//			SubmitScanJob scanjob = new SubmitScanJob(webScanDocument);
//			ScanQueue.getInstance().submit(scanjob);
//			
//			scanRequest = prepareResponse(webScanDocument);
//			UriBuilder ub = uriInfo.getBaseUriBuilder();
//			URI uri = ub.path("/scan/" + id.toString()).build();
//			response = Response.created(uri).entity(scanRequest).build();
//			
//		}
//		
//		
//		return response;
//	}
//
//	private ScanRequest prepareResponse(WebScanDocument webScanDocument) {
//
//		ScanRequest scanRequest = null;
//		ObjectFactory objectFactory = new ObjectFactory();
//		scanRequest = objectFactory.createScanRequest();
//		
//		Baseuris baseuris = objectFactory.createBaseuris();
//		baseuris.setBaseuri(webScanDocument.getBaseUris());
//		scanRequest.setBaseuris(baseuris);
//
//		Comments comments = objectFactory.createComments();
//		comments.setComment(webScanDocument.getComments());
//		scanRequest.setComments(comments);
//
//		Jobs jobs = objectFactory.createJobs();
//		jobs.setCompleted(webScanDocument.getCompletedJobs());
//		jobs.setCreated(webScanDocument.getCreatedJobs());
//		jobs.setPending(webScanDocument.getPendingJobs());
//		scanRequest.setJobs(jobs);
//		
//		scanRequest.setId(webScanDocument.getId().toString());
//		scanRequest.setPercentagedone(webScanDocument.getPercentageDone());
//		scanRequest.setScanphase(webScanDocument.getScanPhase());
//		scanRequest.setStartedtime(webScanDocument.getScanStartTime().getTime());
//		scanRequest.setEstimatedtime(webScanDocument.getScanEndTime().getTime());				
//		
//		return scanRequest;
//	}
//	
//
//	
//
//}
