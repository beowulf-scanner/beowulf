package integration.beowulf.scs;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.scs.ScsManager;
import com.nvarghese.beowulf.scs.ScsServer;
import com.nvarghese.beowulf.scs.ScsServerSettings;

public class ScsServerLauncher {

	private ScsServer server;

	static Logger logger = LoggerFactory.getLogger(ScsServerLauncher.class);

	@BeforeTest(groups = "Scs_integration_test")
	public void startServer() throws Exception {

		BeowulfCommonConfigManager.initialize("src/integration/resources/bw-common.conf");
		PropertyConfigurator.configure("log4j.properties");
		logger.info("ScsServer initializing...");
		ScsServerSettings settings = new ScsServerSettings("bw-scs-integration.conf");
		server = ScsServer.initializeServer(settings);
		ScsManager.initialize(server, settings, false);
		server.startServer(false);

	}

	@AfterTest(groups = "Scs_integration_test")
	public void stopServer() {

		server.shutdown();

	}

}
