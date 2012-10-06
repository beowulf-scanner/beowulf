package com.nvarghese.beowulf.common.jms;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;

public class JmsQueueReceiver {

	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	private Queue queue = null;
	private QueueReceiver consumer = null;
	private AtomicBoolean initialized = new AtomicBoolean(false);

	public JmsQueueReceiver(Queue queue) {

		this.queue = queue;
	}

	void initialize(ConnectionFactory cf, MessageListener jmsMessageListener) throws JMSException {

		qConnect = (QueueConnection) cf.createConnection();
		qSession = (QueueSession) qConnect.createSession(false, Session.AUTO_ACKNOWLEDGE);
		consumer = (QueueReceiver) qSession.createConsumer(queue);
		consumer.setMessageListener(jmsMessageListener);
		initialized.set(true);
	}

	void initialize(ConnectionFactory cf, MessageListener jmsMessageListener, String userName, String password)
			throws JMSException {

		qConnect = (QueueConnection) cf.createConnection(userName, password);
		qSession = (QueueSession) qConnect.createSession(false, Session.AUTO_ACKNOWLEDGE);
		consumer = (QueueReceiver) qSession.createConsumer(queue);
		consumer.setMessageListener(jmsMessageListener);
		initialized.set(true);
	}

	public void startConnection() throws JMSException {

		qConnect.start();
	}

	public void shutdownAll() throws JMSException {

		consumer.close();
		qSession.close();
		qConnect.close();
	}

}
