package com.nvarghese.beowulf.smf.scan.resources;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.exception.ServiceException;
import com.nvarghese.beowulf.common.scan.dto.config.Profile;
import com.nvarghese.beowulf.smf.scan.dto.scanrequest.ScanRequest;
import com.nvarghese.beowulf.smf.scan.services.ScanManagementService;

/**
 * The REST resource file responsible for submission of new web scan requests
 * 
 * @author nibin
 * 
 */

@Path("/api/scan")
public class NewScanResource {

	@Context
	UriInfo uriInfo;

	static Logger logger = LoggerFactory.getLogger(NewScanResource.class);

	@POST
	@Path("new")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response postScanRequest(Profile scanProfile) {

		Response response = null;
		ScanManagementService scanManagementService = new ScanManagementService();
		ScanRequest scanRequest;
		try {
			scanRequest = scanManagementService.createWebScanRequest(scanProfile);
			UriBuilder ub = uriInfo.getBaseUriBuilder();
			URI uri = ub.path("/scan/" + scanRequest.getId()).build();
			response = Response.created(uri).entity(scanRequest).build();
			logger.info("Successfully submitted request for new scan with id:{} ", scanRequest.getId());
		} catch (ServiceException e) {
			response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

		return response;
	}

}
