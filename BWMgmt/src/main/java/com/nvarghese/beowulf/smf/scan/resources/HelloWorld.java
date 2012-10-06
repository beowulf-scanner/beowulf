package com.nvarghese.beowulf.smf.scan.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This is a resource for debugging and connection test
 * 
 * @author nibin
 *
 */
@Path("/hello")
public class HelloWorld {

	// This method is called if TEXT_PLAIN is request
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello from Beowulf Server";
	}

	// This method is called if XMLis request
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public String sayXMLHello() {
		return "<?xml version=\"1.0\"?>" + "<hello> Hello from Beowulf Server" + "</hello>";
	}

	// This method is called if HTML is request
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String sayHtmlHello() {
		return "<html> " + "<title>" + "Hello from Beowulf Server" + "</title>"
		+ "<body><h1>" + "Hello from Beowulf Server" + "</body></h1>" + "</html> ";
	}


}
