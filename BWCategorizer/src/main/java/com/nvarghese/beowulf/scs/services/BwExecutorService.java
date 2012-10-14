package com.nvarghese.beowulf.scs.services;

import java.util.Set;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nvarghese.beowulf.common.webtest.sfe.jobs.AbstractTestJob;
import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJob;
import com.nvarghese.beowulf.scs.ScsQueueManager;

public class BwExecutorService {

	static Logger logger = LoggerFactory.getLogger(BwExecutorService.class);

	public void submitJob(AbstractTestJob job) throws JMSException {

		ObjectMessage objectMessage = ScsQueueManager.getBwExecutorJmsQueueClient().getJmsQueueSender().getSession().createObjectMessage(job);
		ScsQueueManager.getBwExecutorJmsQueueClient().getJmsQueueSender().getProducer().send(objectMessage);

	}

	public void submitJobs(Set<TestJob> testJobs) {

		for (TestJob job : testJobs)
			try {
				submitJob(job);
			} catch (JMSException e) {
				logger.error("Failed to submit testjob with id: {} and webscanObjId: {}", job.getTestJobObjId(), job.getWebScanObjId());
				e.printStackTrace();
			}

	}

}
