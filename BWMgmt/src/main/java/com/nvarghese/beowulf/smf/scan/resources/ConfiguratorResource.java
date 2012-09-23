package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.io.StringWriter;
//import java.net.URI;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DefaultValue;
//import javax.ws.rs.GET;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.UriBuilder;
//import javax.ws.rs.core.UriInfo;
//import javax.xml.transform.sax.SAXSource;
//
//import org.apache.commons.configuration.ConfigurationException;
//import org.apache.commons.configuration.XMLConfiguration;
//import org.apache.log4j.Logger;
//import org.bson.types.ObjectId;
//
//import com.ivizsecurity.verimo.buttercup.core.scan.ScanRegistry;
//import com.ivizsecurity.verimo.buttercup.core.scan.events.EventCollector;
//import com.ivizsecurity.verimo.buttercup.core.scan.jobs.ConfigureChangeScanJob;
//import com.ivizsecurity.verimo.buttercup.core.scan.queues.ScanQueue;
//import com.ivizsecurity.verimo.buttercup.dao.MongoDBService;
//import com.ivizsecurity.verimo.buttercup.exceptions.ScanNotFoundException;
//import com.ivizsecurity.verimo.buttercup.exceptions.ScanRedirectException;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.Errors;
//import com.ivizsecurity.verimo.buttercup.model.mongo.ScanConfigDocument;
//import com.ivizsecurity.verimo.buttercup.model.mongo.WebScanDocument;
//import com.ivizsecurity.verimo.buttercup.services.ScanConfigValidatorService;
//import com.ivizsecurity.verimo.buttercup.utils.ScanRequestUtils;
//import com.ivizsecurity.verimo.scanner.scan.ScanConfigSection;
//import com.sun.jersey.api.NotFoundException;
//
//@Path("/scan/{id}/config")
public class ConfiguratorResource {
//
//	@Context
//	UriInfo uriInfo;
//
//	private static Logger logger = Logger.getLogger(ConfiguratorResource.class);
//
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Response getConfiguration(@PathParam("id") String id,
//			@DefaultValue("all") @QueryParam("section") String section) {
//
//		XMLConfiguration scanConfig = null;
//		ObjectId objectId = null;
//		StringWriter stringWriter = new StringWriter();
//		ScanConfigSection scanConfigSection = ScanConfigSection.getSection(section);
//
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			try {
//
//				if (scanConfigSection != null) {
//					scanConfig = prepareResponse(objectId, scanConfigSection);
//					scanConfig.save(stringWriter);
//				} else {
//					throw new WebApplicationException(Response.status(400).entity("Invalid section parameter value")
//							.type("text/plain").build());
//
//				}
//			} catch (ScanRedirectException e) {
//				UriBuilder ub = uriInfo.getBaseUriBuilder();
//				URI absURI = uriInfo.getAbsolutePath();
//				URI redirectURI = ub.scheme(absURI.getScheme()).path(absURI.getPath()).port(absURI.getPort())
//						.host(e.getRedirect()).build();
//
//				logger.info(e.getMessage());
//				throw new WebApplicationException(Response.created(redirectURI).status(302).build());
//
//			} catch (ScanNotFoundException e) {
//				throw new NotFoundException("Scan - id '" + id + "' is not found ");
//			} catch (ConfigurationException e) {
//				throw new WebApplicationException(Response.status(500).entity(e.getMessage()).build());
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//
//		if (scanConfig != null)
//			return Response.status(200).entity(stringWriter.toString()).build();
//		else {
//			throw new NotFoundException("Scan config for id '" + id + "' is not found");
//		}
//	}
//
//	private XMLConfiguration prepareResponse(ObjectId objectId, ScanConfigSection section)
//			throws ScanNotFoundException, ScanRedirectException {
//
//		WebScanDocument webScanDocument = MongoDBService.getWebScanDocument(objectId);
//
//		if (webScanDocument != null) {
//
//			if (ScanRequestUtils.checkScannerIP(webScanDocument)) {
//				ScanConfigDocument scanConfigDocument = webScanDocument.getScanConfig();
//				return scanConfigDocument.getLoadedXMLConfigurationBySection(section);
//			} else {
//				throw new ScanRedirectException("Scan was executed by " + webScanDocument.getScannerIP(),
//						webScanDocument.getScannerIP());
//			}
//		} else {
//			throw new ScanNotFoundException();
//		}
//	}
//
//	@PUT
//	@Consumes(MediaType.APPLICATION_XML)
//	@Produces(MediaType.TEXT_PLAIN)
//	public Response setConfiguration(@PathParam("id") String id,
//			@DefaultValue("all") @QueryParam("section") String section, SAXSource saxSource) {
//
//		logger.debug("setConfiguration(): Received a configuration change request");
//		Response response = null;
//		ObjectId objectId = null;
//		boolean valid = false;
//		EventCollector scEventCollector = new EventCollector();
//		XMLConfiguration scanConfig = new XMLConfiguration();
//		ScanConfigSection scanConfigSection = ScanConfigSection.getSection(section);
//		valid = ScanConfigValidatorService.doValidate(scanConfig, saxSource, scanConfigSection, scEventCollector);
//
//		if (valid == false) {
//			Errors errors = new Errors();
//			errors.errorList = scEventCollector.getEvents();
//			throw new WebApplicationException(Response.status(400).entity(errors).build());
//
//		} else {
//			if (ObjectId.isValid(id)) {
//				objectId = new ObjectId(id);
//				
//				if (ScanRegistry.getInstance().isScanObjectPresentInRegistry(objectId)) {
//					WebScanDocument webScanDocument = MongoDBService.getWebScanDocument(objectId);
//					ConfigureChangeScanJob configChangejob = new ConfigureChangeScanJob(webScanDocument, scanConfig);
//					ScanQueue.getInstance().submit(configChangejob);
//					return Response.status(202).build();
//				} else {
//					throw new NotFoundException("The requested scan id is not an active running scan");
//				}
//			} else {
//				throw new WebApplicationException(400);
//			}
//		}
//	}
}
