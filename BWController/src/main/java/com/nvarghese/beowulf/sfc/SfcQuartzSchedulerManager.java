package com.nvarghese.beowulf.sfc;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SfcQuartzSchedulerManager implements ServletContextListener {

	/* scheduler */
	private static Scheduler scheduler;

	/* logger */
	static Logger logger = LoggerFactory.getLogger(SfcQuartzSchedulerManager.class);

	@Override
	public void contextDestroyed(ServletContextEvent event) {

		try {
			if (scheduler != null)
				scheduler.shutdown();
			else {
				logger.warn("Scheduler was null when tried to shutdown");
			}
		} catch (SchedulerException e) {
			logger.error("Failed to shutdown quartz scheduler", e);
		}

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext ctx = event.getServletContext();
		String sfcQuartzProperties = ctx.getInitParameter("sfc-quartz-filename");

		try {
			// kick start scheduler
			SchedulerFactory schedFact = new StdSchedulerFactory(sfcQuartzProperties);
			scheduler = schedFact.getScheduler();
			scheduler.start();

		} catch (SchedulerException e) {
			logger.error("Failed to start quartz scheduler", e);
		}

	}

	/**
	 * Schedules job with trigger to quartz scheduler
	 * 
	 * @param job
	 * @param trigger
	 * @throws SchedulerException
	 */
	public static void scheduleJob(JobDetail job, Trigger trigger) throws SchedulerException {

		if (scheduler != null)
			scheduler.scheduleJob(job, trigger);
		else {
			logger.warn("Scheduler was not initialized properly. Failed to schedule the submitted job");
		}
	}

}
