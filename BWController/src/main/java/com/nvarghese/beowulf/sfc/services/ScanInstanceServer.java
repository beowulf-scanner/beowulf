package com.nvarghese.beowulf.sfc.services;

import org.bson.types.ObjectId;
import org.msgpack.rpc.Request;
import org.msgpack.rpc.Server;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.JobBuilder.*;

import com.nvarghese.beowulf.common.rpc.BwControllerRpcInterface;
import com.nvarghese.beowulf.sfc.SfcQuartzSchedulerManager;
import com.nvarghese.beowulf.sfc.quartz.ScanMonitorJob;
import com.nvarghese.beowulf.sfc.quartz.TimeBasedRepeatableTriggerBuilder;

public class ScanInstanceServer implements BwControllerRpcInterface {

	private Server server;
	private ObjectId webScanObjId;

	static Logger logger = LoggerFactory.getLogger(ScanInstanceServer.class);

	public ScanInstanceServer(Server server, ObjectId webScanObjId) {

		this.server = server;
		this.webScanObjId = webScanObjId;
	}

	@Override
	public String sayHello(String name) {

		return "Welcome " + name;
	}

	public void sayHello(Request request, String name) {

		request.sendResult(sayHello(name));
	}

	/**
	 * Starts scan monitoring with a back end quartz job
	 * 
	 * @return
	 */
	public boolean startScanMonitoring() {

		boolean scheduled = false;

		String jobId = "scanMonitor-" + webScanObjId.toString();
		String triggerId = "scanMonitorTrigger-" + webScanObjId.toString();
		JobDetail jobDetail = newJob(ScanMonitorJob.class).withIdentity(jobId, "scanMonitorGroup")
				.usingJobData(ScanMonitorJob.WEBSCANOBJID, webScanObjId.toString()).build();
		TimeBasedRepeatableTriggerBuilder triggerBuilder = new TimeBasedRepeatableTriggerBuilder(triggerId, "scanMonitorGroup", 20);

		try {
			SfcQuartzSchedulerManager.scheduleJob(jobDetail, triggerBuilder.build());
			scheduled = true;
		} catch (SchedulerException e) {
			logger.error("Failed to schedule scan monitor for webscanId: {}", webScanObjId, e);
			scheduled = false;
		}
		return scheduled;
	}

	/**
	 * 
	 * @return
	 */
	public boolean stopScanMonitoring() {

		boolean unscheduled = false;
		try {
			String jobId = "scanMonitor-" + webScanObjId.toString();
			unscheduled = SfcQuartzSchedulerManager.unScheduleJob(new JobKey(jobId, "scanMonitorGroup"));
			logger.info("Submitted request to unschedule scan monitoring. Status: {}", unscheduled);
		} catch (SchedulerException e) {
			logger.error("Failed to unschedule scan monitor for webscanId: {}", webScanObjId, e);
			unscheduled = false;
		}
		return unscheduled;

	}

	public void terminateServer() {

		logger.info("Received request to stop the controller server instance for webscanId: {}", webScanObjId);
		
		server.getEventLoop().getWorkerExecutor().shutdownNow();
		server.getEventLoop().getIoExecutor().shutdownNow();
		server.getEventLoop().getWorkerExecutor().shutdownNow();
		
		server.getEventLoop().shutdown();
		server.close();
		
		logger.info("Stopped the controller server instance for webscanId: {}", webScanObjId);
	}

}
