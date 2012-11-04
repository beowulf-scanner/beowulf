package com.nvarghese.beowulf.sfc.quartz;

import java.net.UnknownHostException;

import org.apache.commons.configuration.ConfigurationException;
import org.bson.types.ObjectId;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.sfc.services.ScanMonitorService;

public class ScanMonitorJob implements Job {

	public static final String WEBSCANOBJID = "WEBSCANOBJID";
	static Logger logger = LoggerFactory.getLogger(ScanMonitorJob.class);

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		ScanMonitorService scanMonitorService = new ScanMonitorService();
		JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
		ObjectId webScanObjId = new ObjectId(dataMap.getString(WEBSCANOBJID));

		try {
			if (!scanMonitorService.isScanRunning(webScanObjId)) {

			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
