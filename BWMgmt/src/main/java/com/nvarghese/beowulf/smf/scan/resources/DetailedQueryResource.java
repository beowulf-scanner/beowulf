package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.io.FileNotFoundException;
//import java.io.OutputStream;
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
//import javax.ws.rs.core.StreamingOutput;
//import javax.ws.rs.core.UriBuilder;
//import javax.ws.rs.core.UriInfo;
//
//import org.apache.log4j.Logger;
//import org.bson.types.ObjectId;
//
//import com.ivizsecurity.verimo.buttercup.dao.MongoDBService;
//import com.ivizsecurity.verimo.buttercup.exceptions.ScanNotFoundException;
//import com.ivizsecurity.verimo.buttercup.exceptions.ScanRedirectException;
//import com.ivizsecurity.verimo.buttercup.model.mongo.WebScanDocument;
//import com.ivizsecurity.verimo.buttercup.server.ButterCupSettings;
//import com.ivizsecurity.verimo.buttercup.utils.ScanRequestUtils;
//import com.mongodb.DB;
//import com.mongodb.DBCollection;
//import com.mongodb.DBCursor;
//import com.mongodb.DBObject;
//import com.mongodb.gridfs.GridFS;
//import com.sun.jersey.api.NotFoundException;
//
//@Path("/scan/{id}/details")
public class DetailedQueryResource {
//
//	@Context
//	UriInfo uriInfo;
//
//	static Logger logger = Logger.getLogger(DetailedQueryResource.class);
//
//	@GET
//	@Produces(MediaType.TEXT_PLAIN)
//	public StreamingOutput getDetails(@PathParam("id") final String scanid) throws FileNotFoundException {
//
//		ObjectId objectId = null;
//
//		if (ObjectId.isValid(scanid)) {
//			objectId = new ObjectId(scanid);
//
//			try {
//
//				return prepareStreamingResponse(objectId);
//			} catch (ScanNotFoundException sne) {
//				throw new NotFoundException("Scan with id '" + scanid + "' is not found");
//			} catch (ScanRedirectException e) {
//				UriBuilder ub = uriInfo.getBaseUriBuilder();
//				URI absURI = uriInfo.getAbsolutePath();
//				URI redirectURI = ub.scheme(absURI.getScheme()).path(absURI.getPath()).port(absURI.getPort())
//						.host(e.getRedirect()).build();
//
//				logger.info(e.getMessage());
//				throw new WebApplicationException(Response.temporaryRedirect(redirectURI).build());
//			}
//
//		} else {
//			throw new WebApplicationException(400);
//		}
//	}
//
//	private StreamingOutput prepareStreamingResponse(final ObjectId objectId) throws ScanNotFoundException,
//			ScanRedirectException, FileNotFoundException {
//
//		WebScanDocument scanDoc = MongoDBService.getWebScanDocument(objectId);
//
//		if (scanDoc != null) {
//
//			if (!ScanRequestUtils.checkScannerIP(scanDoc)) {
//				throw new ScanRedirectException("Scan was executed by " + scanDoc.getScannerIP(),
//						scanDoc.getScannerIP());
//			} // else return the streaming output
//			
//			GridFS gridFS = ButterCupSettings.getInstance().getMongoDBWrapper().getGridFS();
//			DB db = gridFS.getDB();
//			DBCollection chunkCollection = db.getCollection("fs.chunks");
//			if(gridFS.findOne(objectId.toString() + "-scanlog.log") == null)
//				throw new FileNotFoundException();
//
//			return new StreamingOutput() {
//
//				public void write(OutputStream output) {
//
//					try {
//
//						GridFS gridFS = ButterCupSettings.getInstance().getMongoDBWrapper().getGridFS();
//						DB db = gridFS.getDB();
//						DBCollection chunkCollection = db.getCollection("fs.chunks");
//						String file_id;
//						DBObject oldFile = gridFS.findOne(objectId.toString() + "-scanlog.log");
//						file_id = oldFile.get("_id").toString();
//						output.write(objectId.toString().getBytes());
//						output.write("\n".getBytes());
//						output.flush();
//						DBCursor cursor = chunkCollection.find();
//						cursor.addOption(1); // tailable cursor
//						cursor.addOption(5); // await data
//						boolean wait = false;
//
//						while (true) {
//
//							if (wait)
//								Thread.sleep(3000);
//
//							DBObject obj = cursor.next();
//							String id = obj.get("files_id").toString();
//
//							if (id.equalsIgnoreCase(file_id)) {
//								byte[] by = (byte[]) obj.get("data");
//								String data = new String(by);
//								output.write(data.getBytes());
//								output.write("\n".getBytes());
//								wait = true;
//							}
//
//							output.flush();
//						}
//					} catch (Exception e) {
//						try {
//							throw new FileNotFoundException();
//						} catch (FileNotFoundException e1) {
//							logger.error("Error in detailed query interface ", e1);
//						}
//					}
//				}
//			};
//		} else {
//			throw new ScanNotFoundException();
//		}
//	}
//
}
