package com.nvarghese.beowulf.sfc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration.ConfigurationException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SFControllerServer {

	private Server jettyServer;

	/* logger */
	static Logger logger = LoggerFactory.getLogger(SFControllerServer.class);

	public static SFControllerServer initializeServer(SFControllerSettings settings) throws Exception {

		Resource jettyEnvXml = findAndGetJettyResource(settings);
		// InputStream in =
		// RatifyServer.class.getClassLoader().getResourceAsStream(settings.getJettyResourceFileName());
		logger.info("Debug jetty: " + jettyEnvXml);
		XmlConfiguration configuration = new XmlConfiguration(jettyEnvXml.getInputStream());
		logger.debug("SFControllerServer configured with jetty web settings");

		SFControllerServer server = new SFControllerServer();

		server.jettyServer = (Server) configuration.configure();
		WebAppContext context = new WebAppContext();
		if (settings.getBwSfControllerRootPath().equalsIgnoreCase("")) {
			context.setDescriptor(settings.getJettyContextDescriptor());
			context.setResourceBase(settings.getJettyContextResourceBase());

		} else {
			context.setDescriptor(settings.getBwSfControllerRootPath() + File.separator
					+ settings.getJettyContextDescriptor());
			context.setResourceBase(settings.getBwSfControllerRootPath() + File.separator
					+ settings.getJettyContextResourceBase());
		}
		context.setContextPath(settings.getJettyContextRootPath());

		context.setParentLoaderPriority(true);
		// context.getSessionHandler().getSessionManager().setSessionCookie("xsessionid");
		server.jettyServer.setHandler(context);

		return server;

	}

	private static Resource findAndGetJettyResource(SFControllerSettings settings) throws MalformedURLException,
			IOException {

		Resource jettyEnvXml = null;
		jettyEnvXml = Resource.newClassPathResource(settings.getJettyResourceFileName());

		if (jettyEnvXml != null && jettyEnvXml.exists()) {
			logger.info("Found jetty resource file in classpath");
			return jettyEnvXml;
		}
		jettyEnvXml = Resource.newResource(settings.getJettyResourceFileName());
		if (jettyEnvXml != null && jettyEnvXml.exists()) {
			logger.info("Found jetty resource file");
			return jettyEnvXml;
		}

		logger.warn("Checking jetty resource file in default conf directory");

		String jettyEnvPath = settings.getDefaultConfDir() + File.separator + settings.getJettyResourceFileName();
		if (!settings.getBwSfControllerRootPath().equalsIgnoreCase("")) {
			jettyEnvPath = settings.getBwSfControllerRootPath() + File.separator + jettyEnvPath;
		}

		jettyEnvXml = Resource.newResource(jettyEnvPath);
		if (jettyEnvXml != null && jettyEnvXml.exists()) {
			logger.info("Found jetty resource file in default conf directory");
			return jettyEnvXml;
		} else {
			logger.error("Cannot find jetty resource file: " + settings.getJettyResourceFileName());
			System.exit(0);
		}
		return null;

	}

	public void startServer(Boolean waitForThreadsToComplete) throws Exception {

		this.jettyServer.start();
		if (waitForThreadsToComplete)
			this.jettyServer.join();

	}

	Server getJettyServer() {

		return jettyServer;
	}

	private static SFControllerSettings handleCommandLine(String[] args) {

		ProgramOptions options = new ProgramOptions();
		SFControllerSettings settings = null;
		try {
			if (args.length > 0) {
				CommandLine commands = options.parseArguments(args);
				if (commands.hasOption(ProgramOptions.HELP_OPTION)) {

					options.printHelp();
					System.exit(0);

				} else if (commands.hasOption(ProgramOptions.CONFIG_FILE_OPTION)) {
					String filename = commands.getOptionValue(ProgramOptions.CONFIG_FILE_OPTION);
					try {
						settings = new SFControllerSettings(new File(filename));
					} catch (ConfigurationException e) {
						logger.error("Error in config file: " + e.getMessage());
						System.exit(0);
					}
				} else {
					options.printHelp();
					System.exit(0);
				}

			} else {
				/* default */
				settings = new SFControllerSettings();
			}
		} catch (ParseException pe) {
			logger.error("ParseException thrown: " + pe.getMessage());
			options.printHelp();
			System.exit(0);
		} catch (ConfigurationException e) {
			logger.error("Error in config file: " + e.getMessage());
			System.exit(0);
		} catch (URISyntaxException e) {
			logger.error("Error in config file: " + e.getMessage());
			System.exit(0);
		}

		return settings;
	}

	public boolean shutdown() {

		try {
			logger.info("Shutting down the server...");
			this.jettyServer.stop();
			// logger.info("Server has stopped.");
			return true;
		} catch (Exception ex) {
			logger.error("Error when stopping Jetty server: " + ex.getMessage(), ex);
			return false;
		}
	}

	public static void main(String[] args) throws Exception {

		// PropertyConfigurator.configure("log4j.properties");
		SFControllerSettings settings = handleCommandLine(args);

		logger.info("SFControllerServer Server initializing...");
		final SFControllerServer sfcServer = initializeServer(settings);
		SFControllerManager.initialize(sfcServer, settings, false);

		// add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {

				logger.info("Kill signal received for SFControllerServer to shutdown");
				boolean shutdown = sfcServer.shutdown();
				if (shutdown)
					logger.info("SFControllerServer shutdown completed gracefully");
				else
					logger.warn("SFControllerServer shutdown failed. Try killing manually");
			}
		});

		sfcServer.startServer(true);
	}
}
