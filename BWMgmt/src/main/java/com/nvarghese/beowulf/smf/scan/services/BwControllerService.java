package com.nvarghese.beowulf.smf.scan.services;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import com.nvarghese.beowulf.common.jobs.AbstractBeowulfJob;
import com.nvarghese.beowulf.smf.SmfQueueManager;

public class BwControllerService {

	public void submitJob(AbstractBeowulfJob job) throws JMSException {

		ObjectMessage objectMessage = SmfQueueManager.getBwControlJmsQueueClient().getJmsQueueSender().getSession().createObjectMessage(job);
		SmfQueueManager.getBwControlJmsQueueClient().getJmsQueueSender().getProducer().send(objectMessage);

	}

}
