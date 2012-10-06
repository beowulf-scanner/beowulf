package com.nvarghese.beowulf.scs.jms.listeners;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.nvarghese.beowulf.common.webtest.scs.jobs.MetaCategorizerJob;
import com.nvarghese.beowulf.scs.services.CategorizationService;

public class BwCategorizerQueueListener implements MessageListener {

	@Override
	public void onMessage(Message message) {

		if (message instanceof ObjectMessage) {
			ObjectMessage objMessage = (ObjectMessage) message;
			routeObjectMessage(objMessage);

		}

	}

	private void routeObjectMessage(ObjectMessage objMessage) {

		try {
			Object object = objMessage.getObject();
			if (object instanceof MetaCategorizerJob) {

				final MetaCategorizerJob metaCategorizerJob = (MetaCategorizerJob) object;
				new Thread() {
					
					public void run() {
						CategorizationService categorizationService = new CategorizationService();
						categorizationService.processMetaCategorization(metaCategorizerJob);
					}
				}.start();

			}
		} catch (JMSException e) {

		}
	}

}
