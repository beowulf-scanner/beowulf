package com.nvarghese.beowulf.sfe.jms.listeners;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.nvarghese.beowulf.common.webtest.sfe.jobs.TestJob;

public class BwExecutorQueueListener implements MessageListener {

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
			if (object instanceof TestJob) {

				final TestJob testJob = (TestJob) object;
				new Thread() {
					public void run() {
						
					}
				}.start();

			}
		} catch (JMSException e) {

		}
	}

}
