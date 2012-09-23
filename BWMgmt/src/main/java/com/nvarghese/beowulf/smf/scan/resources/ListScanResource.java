//package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.ws.rs.DefaultValue;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.apache.log4j.Logger;
//
//import com.ivizsecurity.verimo.buttercup.dao.MongoDBService;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.scanrequest.ScanRequests;
//import com.ivizsecurity.verimo.buttercup.model.mongo.WebScanDocument;
//import com.ivizsecurity.verimo.buttercup.server.ButterCupSettings;
//import com.ivizsecurity.verimo.buttercup.transformers.ScanRequestTransformer;
//
//@Path("/scan/list")
//public class ListScanResource {
//
//	private static Logger logger = Logger.getLogger(ListScanResource.class);
//
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Response getListOfScans(@QueryParam("scan_phase") String scanPhase,
//			@DefaultValue("false") @QueryParam("override_scanner_ip_check") boolean overrideScannerIPCheck) {
//
//		logger.debug("Getting the list of scans");
//
//		ScanRequests scanRequests = null;
//		Map<String, Object> criteriaMap = new HashMap<String, Object>();
//
//		if (scanPhase != null && !scanPhase.equalsIgnoreCase("")) {
//			criteriaMap.put("scanPhase =", scanPhase);
//		}
//		if (!overrideScannerIPCheck)
//			criteriaMap.put("scannerIP =", ButterCupSettings.getInstance().getServerHostAddress());			
//
//		List<WebScanDocument> webscanDocs = MongoDBService.findWebScanDocuments(criteriaMap);
//
//		if (webscanDocs != null) {
//			scanRequests = ScanRequestTransformer.doTransform(webscanDocs);
//			return Response.ok().entity(scanRequests).build();
//		} else {
//			throw new WebApplicationException(500);
//		}
//	}
//
//}
