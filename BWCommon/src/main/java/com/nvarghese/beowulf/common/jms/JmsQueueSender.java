package com.nvarghese.beowulf.common.jms;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

public class JmsQueueSender {

	private QueueConnection qConnect = null;
	private QueueSession qSession = null;
	private Queue queue = null;
	private QueueSender producer;
	private AtomicBoolean initialized = new AtomicBoolean(false);

	public JmsQueueSender(Queue queue) {

		this.queue = queue;

	}

	void initialize(ConnectionFactory cf) throws JMSException {

		qConnect = (QueueConnection) cf.createConnection();
		qSession = (QueueSession) qConnect.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = (QueueSender) qSession.createProducer(queue);
		initialized.set(true);
	}

	void initialize(ConnectionFactory cf, String userName, String password) throws JMSException {

		qConnect = (QueueConnection) cf.createConnection(userName, password);
		qSession = (QueueSession) qConnect.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = (QueueSender) qSession.createProducer(queue);
		initialized.set(true);
	}

	public QueueSession getSession() {

		return qSession;
	}

	public QueueSender getProducer() {

		return producer;
	}

	public void startConnection() throws JMSException {

		qConnect.start();
	}

	public void shutdownAll() throws JMSException {

		producer.close();
		qSession.close();
		qConnect.stop();
		qConnect.close();

	}

}
