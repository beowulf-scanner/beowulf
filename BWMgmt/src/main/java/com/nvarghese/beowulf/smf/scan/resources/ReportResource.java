//package com.nvarghese.beowulf.smf.scan.resources;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DefaultValue;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
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
//
//import org.apache.log4j.Logger;
//import org.bson.types.ObjectId;
//
//import com.ivizsecurity.verimo.buttercup.core.scan.ReportStatusMessage;
//import com.ivizsecurity.verimo.buttercup.core.scan.jobs.ReportScanJob;
//import com.ivizsecurity.verimo.buttercup.core.scan.queues.ScanQueue;
//import com.ivizsecurity.verimo.buttercup.dao.MongoDBService;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.Errors;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.Report;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.ReportMetaInfo;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.reasons.Reason;
//import com.ivizsecurity.verimo.buttercup.model.jaxb.reasons.Reasons;
//import com.ivizsecurity.verimo.buttercup.model.mongo.ScanReportDocument;
//import com.ivizsecurity.verimo.buttercup.model.mongo.WebScanDocument;
//import com.ivizsecurity.verimo.buttercup.services.ReasonsDispatchService;
//import com.ivizsecurity.verimo.buttercup.utils.ScanRequestUtils;
//import com.ivizsecurity.verimo.scanner.scan.ScanPhase;
//import com.ivizsecurity.verimo.scanner.scan.reportingex.ReportPhase;
//import com.mongodb.gridfs.GridFSDBFile;
//import com.sun.jersey.api.NotFoundException;
//
//@Path("/scan/{id}/report")
//public class ReportResource {
//
//	@Context
//	UriInfo uriInfo;
//
//	private static Logger logger = Logger.getLogger(ReportResource.class);
//
//	/**
//	 * 
//	 * @param id
//	 * @return
//	 */
//	@Path("generate")
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Response getReportGenerate(@PathParam("id") String id) {
//
//		logger.debug("getReportGenerate(): processing scan id: " + id);
//
//		ObjectId objectId = null;
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//
//			WebScanDocument document = MongoDBService.getWebScanDocument(objectId);
//
//			if (document != null) {
//				Reasons reasons = ReasonsDispatchService.getScanReportReasons();
//				return Response.ok(reasons).build();
//			} else {
//				throw new NotFoundException("Scan with id '" + id + "' is not found");
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//	}
//
//	@Path("generate")
//	@POST
//	@Produces(MediaType.APPLICATION_XML)
//	@Consumes(MediaType.APPLICATION_XML)
//	public Response postReportGenerate(@PathParam("id") String id, Reason reason) {
//
//		logger.debug("postReportGenerate(): processing scan id: " + id);
//
//		ObjectId objectId = null;
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//
//			WebScanDocument document = MongoDBService.getWebScanDocument(objectId);
//			Report report = new Report();
//
//			if (document != null) {
//				
//				ScanReportDocument reportDocument = document.getReportDocument();
//				ScanPhase scanPhase = ScanPhase.getScanPhase(document.getScanPhase());
//				
//
//				if (ScanRequestUtils.checkScannerIP(document)) {
//					/* scan is running in this machine */
//
//					if (scanPhase.isStopState() || scanPhase == ScanPhase.MANUAL_VALIDATION) {
//
//						ReportPhase currentReportPhase = ReportPhase.getReportPhase(reportDocument.getReportPhase());
//						if (currentReportPhase == ReportPhase.NOT_STARTED || currentReportPhase.isStopState()) {
//							/* all perfect to generate report */
//
//							reportDocument.setReportGenReasonId(reason.getId());
//							reportDocument.setReportGenReasonStr(reason.getValue());
//							MongoDBService.save(document);
//
//							ReportScanJob reportJob = new ReportScanJob(document);
//							ScanQueue.getInstance().submit(reportJob);
//
//							report.status = ReportStatusMessage.SUBMITTED.getMessage();
//							report.location = "";
//							report.comments.commentsList = reportDocument.getComments();
//							report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
//							return Response.status(202).entity(report).build();
//
//						} else {
//							/* report gen in progress */
//							report.status = ReportStatusMessage.ALREADY_SUBMITTED.getMessage();
//							report.location = "";
//							report.comments.commentsList = reportDocument.getComments();
//							report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
//							return Response.status(200).entity(report).build();
//						}
//					} else {
//						/* scan in progress */
//						report.status = ReportStatusMessage.NOT_ALLOWED.getMessage();
//						report.location = "";
//						report.comments.commentsList = reportDocument.getComments();
//						report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
//						return Response.status(200).entity(report).build();
//					}
//				} else {
//					/* redirect to host that is doing/done the scan */
//
//					UriBuilder ub = uriInfo.getBaseUriBuilder();
//					URI absURI = uriInfo.getAbsolutePath();
//					URI redirectURI = ub.scheme(absURI.getScheme())
//												.path(absURI.getPath())
//												.port(absURI.getPort())
//												.host(document.getScannerIP())
//												.build();
//
//					throw new WebApplicationException(Response.temporaryRedirect(redirectURI).build());
//				}
//			} else {
//				throw new NotFoundException("Scan with id '" + id + "' is not found");
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//	}
//	
//	/**
//	 * 
//	 * @param id
//	 * @return
//	 */
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Response getReport(@PathParam("id") String id) {
//		
//		logger.debug("getReport(): processing scan id: " + id);
//		
//		ObjectId objectId = null;
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			
//			WebScanDocument document = MongoDBService.getWebScanDocument(objectId);
//			Report report = new Report();
//			
//			if (document != null) {
//				
//				ScanReportDocument reportDocument = document.getReportDocument();				
//				
//				if (ScanRequestUtils.checkScannerIP(document)) {
//					/* scan is/was running in this machine */
//					
//					ReportPhase currentReportPhase = ReportPhase.getReportPhase(reportDocument.getReportPhase());
//					if(currentReportPhase == ReportPhase.NOT_STARTED) {
//						
//						report.status = ReportStatusMessage.NOT_STARTED.getMessage();
//						report.location = "";
//						report.comments.commentsList = reportDocument.getComments();
//						report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
//						return Response.status(200).entity(report).build();
//						
//					} else if(currentReportPhase == ReportPhase.REPORT_GENERATION_COMPLETED) {
//						
//						URI absURI = uriInfo.getAbsolutePath();
//						report.status = ReportStatusMessage.COMPLETED.getMessage();
//						String absPath = absURI.getPath().endsWith("/") ? absURI.getPath() : absURI.getPath() + "/";
//						report.location = absPath + reportDocument.getReportFileId().toString();
//						report.comments.commentsList = reportDocument.getComments();
//						report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
//						return Response.status(200).entity(report).build();
//						
//					} else if (currentReportPhase == ReportPhase.REPORT_GENERATION_STARTED) {
//						
//						report.status = ReportStatusMessage.PROCESSING.getMessage();
//						report.location = "";
//						report.comments.commentsList = reportDocument.getComments();
//						report.oldreports.oldreportList = reportDocument.getOldReportFileIdsAsListStr();
//						return Response.status(200).entity(report).build();
//						
//					} else /*if (currentReportPhase == ReportPhase.ERROR)*/ {
//						
//						Errors errors = new Errors();
//						errors.errorList.add(reportDocument.getLastError());
//						throw new WebApplicationException(Response.status(500).entity(errors).build());
//					}
//					
//				} else {
//					/* redirect to host that is doing/done the scan */
//
//					UriBuilder ub = uriInfo.getBaseUriBuilder();
//					URI absURI = uriInfo.getAbsolutePath();
//					URI redirectURI = ub.scheme(absURI.getScheme())
//												.path(absURI.getPath())
//												.port(absURI.getPort())
//												.host(document.getScannerIP())
//												.build();
//
//					throw new WebApplicationException(Response.temporaryRedirect(redirectURI).build());
//				}
//			} else {
//				throw new NotFoundException("Scan with id '" + id + "' is not found");
//			}			
//		} else {
//			throw new WebApplicationException(400);
//		}		
//		
//	}
//	
//	/**
//	 * 
//	 * @param id
//	 * @param reportFileId
//	 * @param metadata enables to receive only meta data of report
//	 * @param overrideScannerIPCheck enables report download from any available host
//	 * @return
//	 * @throws IOException 
//	 */
//	@Path("{reportFileId}")
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public void getReportDownload(@PathParam("id") String id, @PathParam("reportFileId") String reportFileId,
//			@Context HttpServletRequest requestServlet,
//			@Context HttpServletResponse responseServlet,
//			@DefaultValue("false") @QueryParam("metainfoonly") boolean metainfo,
//			@DefaultValue("false") @QueryParam("override_scanner_ip_check") boolean overrideScannerIPCheck) throws IOException {
//		
//	
//		logger.debug("getReportDownload(): processing scan id: " + id);
//		ObjectId objectId = null;
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			WebScanDocument document = MongoDBService.getWebScanDocument(objectId);
//			//Report report = new Report();
//			
//			if (document != null) {
//				ScanReportDocument reportDocument = document.getReportDocument();				
//				
//				if (ScanRequestUtils.checkScannerIP(document) || overrideScannerIPCheck) {
//					
//					/* scan is/was running in this machine or overridden */
//					
//					GridFSDBFile fsDBFile = reportDocument.getGridFSDBFile(reportFileId);
//					if(fsDBFile != null) {
//					
//						if(metainfo) {
//							/* redirect */
//							UriBuilder ub = uriInfo.getBaseUriBuilder();
//							URI absURI = uriInfo.getAbsolutePath();
//							URI redirectURI = ub.scheme(absURI.getScheme())
//														.path(absURI.getPath()+"/meta")
//														.port(absURI.getPort())
//														.host(document.getScannerIP())
//														.queryParam("override_scanner_ip_check", String.valueOf(overrideScannerIPCheck))
//														.build();
//							try {
//								responseServlet.sendRedirect(redirectURI.toString());
//							
//							} catch (IOException e) {
//								
//								throw new WebApplicationException(
//										Response.status(500)
//												.entity("IOException while redirecting: " + e.getMessage())
//												.build());
//							}
//							
//						} else {
//							ServletOutputStream out = null;
//							InputStream in = null;
//							try {
//								/* sends data in a stream */
//								
//								out = responseServlet.getOutputStream();
//								in = fsDBFile.getInputStream();
//								int bytesRead;
//								//byte[] bytedata = new byte[2048]; /* buff size: 2048 */
//								
//								responseServlet.setHeader("Content-Encoding", "gzip");
//								responseServlet.setHeader("Content-Disposition",
//													"attachment; filename="
//										+ ScanRequestUtils.stripOffGzipExtension(fsDBFile.getFilename()));
//								responseServlet.setContentType(MediaType.APPLICATION_XML.toString());
//								/*
//								while((bytesRead = in.read(data,0,2048)) != -1) {
//									out.write(data,0,bytesRead);
//								}*/
//								long bytesCount;
//								bytesCount = fsDBFile.writeTo(out);
//								/*
//								while((bytesCount = fsDBFile.writeTo(out)) != -1) {
//									
//								}*/
//								logger.debug("Streamed report of size: " + bytesCount);
//							
//							} catch(IOException ioe) {
//								logger.error("IOException while streaming report");
//								
//							} finally {
//								
//								if(out != null)
//									out.close();
//								if(in != null)
//									in.close();
//							}
//							
//						}
//					
//					} else {
//						throw new NotFoundException("Report with id '" + reportFileId + "' is not found");
//					}
//					
//				} else {
//					/* redirect to host that is doing/done the scan */
//
//					UriBuilder ub = uriInfo.getBaseUriBuilder();
//					URI absURI = uriInfo.getAbsolutePath();
//					URI redirectURI = ub.scheme(absURI.getScheme())
//												.path(absURI.getPath())
//												.port(absURI.getPort())
//												.host(document.getScannerIP())
//												.build();
//
//					throw new WebApplicationException(Response.temporaryRedirect(redirectURI).build());
//				}
//			} else {
//				throw new NotFoundException("Scan with id '" + id + "' is not found");
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//	}
//	
//	@Path("{reportFileId}/meta")
//	@GET
//	@Produces(MediaType.APPLICATION_XML)
//	public Response getReportMetaDetails(@PathParam("id") String id, @PathParam("reportFileId") String reportFileId,			
//			@DefaultValue("false") @QueryParam("override_scanner_ip_check") boolean overrideScannerIPCheck) {
//		
//	
//		logger.debug("getReportDownload(): processing scan id: " + id);
//		ObjectId objectId = null;
//		if (ObjectId.isValid(id)) {
//			objectId = new ObjectId(id);
//			WebScanDocument document = MongoDBService.getWebScanDocument(objectId);			
//			
//			if (document != null) {
//				ScanReportDocument reportDocument = document.getReportDocument();				
//				
//				if (ScanRequestUtils.checkScannerIP(document) || overrideScannerIPCheck) {
//					
//					/* scan is/was running in this machine or overridden */
//					//ObjectId reportFileObjectId = reportDocument.findAndGetReportFileId(reportFileId);
//					GridFSDBFile fsDBFile = reportDocument.getGridFSDBFile(reportFileId);
//					if(fsDBFile != null) {					
//
//						/* send only metadata */
//						ReportMetaInfo reportMetaInfo = new ReportMetaInfo();
//						reportMetaInfo.filename = fsDBFile.getFilename();
//						reportMetaInfo.md5sum = fsDBFile.getMD5();
//						reportMetaInfo.contenttype = fsDBFile.getContentType();
//						reportMetaInfo.contentlength = fsDBFile.getLength();
//						reportMetaInfo.uploaddate = fsDBFile.getUploadDate().getTime();							
//
//						return Response.ok().entity(reportMetaInfo).build();
//
//					} else {
//						throw new NotFoundException("Report with id '" + reportFileId + "' is not found");
//					}
//					
//				} else {
//					/* redirect to host that is doing/done the scan */
//
//					UriBuilder ub = uriInfo.getBaseUriBuilder();
//					URI absURI = uriInfo.getAbsolutePath();
//					URI redirectURI = ub.scheme(absURI.getScheme())
//												.path(absURI.getPath())
//												.port(absURI.getPort())
//												.host(document.getScannerIP())
//												.build();
//
//					throw new WebApplicationException(Response.temporaryRedirect(redirectURI).build());
//				}
//			} else {
//				throw new NotFoundException("Scan with id '" + id + "' is not found");
//			}
//		} else {
//			throw new WebApplicationException(400);
//		}
//	}
//	
//}
