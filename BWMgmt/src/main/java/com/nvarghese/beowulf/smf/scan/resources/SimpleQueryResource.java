package com.nvarghese.beowulf.smf.scan.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.smf.scan.dto.scanrequest.ScanRequest;
import com.nvarghese.beowulf.smf.scan.services.ResourceNotFoundException;
import com.nvarghese.beowulf.smf.scan.services.ScanManagementService;

/**
 * This resource provides answers to simple queries such as start time, expected
 * time , percentage done as defined in requirement.
 */

@Path("/api/scan/{id}")
public class SimpleQueryResource {

	static Logger logger = LoggerFactory.getLogger(SimpleQueryResource.class);

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getSimpleInfo(@PathParam("id") String id) {

		logger.info("Getting information for id: " + id);

		ScanRequest scanRequest = null;
		ObjectId objectId = null;
		ScanManagementService scanManagementService = new ScanManagementService();
		Response response = null;
		try {
			if (ObjectId.isValid(id)) {
				objectId = new ObjectId(id);
				scanRequest = scanManagementService.getWebScanRequest(objectId);
				response = Response.status(Status.OK).entity(scanRequest).build();
			} else {
				response = Response.status(Status.BAD_REQUEST).entity("Id is invalid").build();
			}
		} catch (ResourceNotFoundException e) {
			logger.warn("Requested webscandocument for id: {} cannot be found");
			response = Response.status(Status.NOT_FOUND).entity("Resource not found").build();
		} catch (Exception e) {
			logger.error("Failed to retrieve web scan document. Reason: {}", e.getMessage(), e);
			response = Response.status(Status.NOT_FOUND).entity("Resource not found").build();
		}

		return response;

	}

}
