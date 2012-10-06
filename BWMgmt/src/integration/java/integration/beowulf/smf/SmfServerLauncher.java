package integration.beowulf.smf;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.smf.SmfManager;
import com.nvarghese.beowulf.smf.SmfServer;
import com.nvarghese.beowulf.smf.SmfSettings;

public class SmfServerLauncher {

	private SmfServer server;

	static Logger logger = LoggerFactory.getLogger(SmfServerLauncher.class);

	@BeforeTest(groups = "Smf_integration_test")
	public void startServer() throws Exception {

		BeowulfCommonConfigManager.initialize("src/integration/resources/bw-common.conf");
		PropertyConfigurator.configure("log4j.properties");
		logger.info("SmfServer initializing...");
		SmfSettings settings = new SmfSettings("bw-smf-integration.conf");
		server = SmfServer.initializeServer(settings);
		SmfManager.initialize(server, settings, false);
		server.startServer(false);

	}

	@AfterTest(groups = "Smf_integration_test")
	public void stopServer() {

		server.shutdown();

	}

}
