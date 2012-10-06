package com.nvarghese.beowulf.sfc.services;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import com.nvarghese.beowulf.common.webtest.scs.jobs.AbstractCategorizerJob;
import com.nvarghese.beowulf.sfc.SfcQueueManager;


public class BwCategorizerService {
	
	public void submitJob(AbstractCategorizerJob job) throws JMSException {

		ObjectMessage objectMessage = SfcQueueManager.getBwCategorizerJmsQueueClient().getJmsQueueSender().getSession().createObjectMessage(job);
		SfcQueueManager.getBwCategorizerJmsQueueClient().getJmsQueueSender().getProducer().send(objectMessage);

	}

}
