package com.nvarghese.beowulf.common.http.renderer;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.EventObject;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import com.nvarghese.beowulf.common.cobra.html.HttpRequest;
import com.nvarghese.beowulf.common.cobra.html.ReadyStateChangeListener;
import com.nvarghese.beowulf.common.cobra.util.EventDispatch;
import com.nvarghese.beowulf.common.cobra.util.GenericEventListener;
import com.nvarghese.beowulf.common.cobra.util.Urls;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;
import com.nvarghese.beowulf.common.http.txn.HttpMethodType;
import com.nvarghese.beowulf.common.http.txn.HttpTransactionFactory;
import com.nvarghese.beowulf.common.http.txn.TransactionSource;

public class CobraHttpRequest implements HttpRequest {

	private static final Logger logger = Logger.getLogger(CobraHttpRequest.class.getName());

	private AbstractHttpTransaction rawTransaction;
	private final CobraUserAgent cobraUserAgent;

	private int readyState;
	private int status;
	private String statusText;
	private byte[] responseBytes;

	private boolean isAsync;
	private java.net.URL requestURL;
	protected String requestMethod;
	protected String requestUserName;
	protected String requestPassword;
	private boolean send;

	protected String responseHeaders;

	public CobraHttpRequest(CobraUserAgent context) {

		super();
		this.cobraUserAgent = context;

	}

	public synchronized int getReadyState() {

		return this.readyState;
	}

	public synchronized String getResponseText() {

		if (rawTransaction != null) {
			return rawTransaction.getResponseBodyAsString();
		}
		return null;
	}

	public synchronized Document getResponseXML() {

		if (rawTransaction != null) {
			try {
				java.io.InputStream in = new ByteArrayInputStream(rawTransaction.getResponseBody());
				return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			} catch (Exception err) {
				logger.error("Unable to parse response as XML." + err.getMessage());
				return null;
			}
		}
		return null;
	}

	public synchronized byte[] getResponseBytes() {

		if (rawTransaction != null) {
			this.responseBytes = rawTransaction.getResponseBody();
		}

		return this.responseBytes;
	}

	public synchronized Image getResponseImage() {

		// Not implemented
		return null;

	}

	public synchronized int getStatus() {

		if (rawTransaction != null) {
			this.status = rawTransaction.getResponseStatusCode();
		}
		return this.status;
	}

	public synchronized String getStatusText() {

		if (rawTransaction != null) {
			this.statusText = rawTransaction.getResponse().getStatusLine().getReasonPhrase();
		}
		return this.statusText;
	}

	public void abort() {

	}

	public synchronized String getAllResponseHeaders() {

		if (rawTransaction != null) {
			this.responseHeaders = rawTransaction.getAllResponseHeadersAsString();
		}
		return this.responseHeaders;
	}

	public synchronized String getResponseHeader(String headerName) {

		if (rawTransaction != null) {
			return rawTransaction.getResponse().getFirstHeader(headerName).getValue();
		}
		return null;

	}

	public synchronized void setRequestHeader(String headerName, String value) {

		if (rawTransaction != null) {
			if (headerName.equalsIgnoreCase(HTTP.CONTENT_TYPE) && rawTransaction.getRequestHeaders(HTTP.CONTENT_TYPE).length > 0) {
				rawTransaction.removeRequestHeaders(HTTP.CONTENT_TYPE);
			}
			rawTransaction.addRequestHeader(headerName, value);
		}
	}

	public void open(String method, String url) throws IOException {

		this.open(method, url, true);
	}

	public void open(String method, URL url) throws IOException {

		this.open(method, url, true, null, null);
	}

	public void open(String method, URL url, boolean asyncFlag) throws IOException {

		this.open(method, url, asyncFlag, null, null);
	}

	public void open(String method, String url, boolean asyncFlag) throws IOException {

		URL urlObj = Urls.createURL(null, url);
		this.open(method, urlObj, asyncFlag, null);
	}

	public void open(String method, java.net.URL url, boolean asyncFlag, String userName) throws IOException {

		this.open(method, url, asyncFlag, userName, null);
	}

	/**
	 * Opens the request. Call {@link #send(String)} to complete it.
	 * 
	 * @param method
	 *            The request method.
	 * @param url
	 *            The request URL.
	 * @param asyncFlag
	 *            Whether the request should be asynchronous.
	 * @param userName
	 *            The user name of the request (not supported.)
	 * @param password
	 *            The password of the request (not supported.)
	 */
	public void open(final String method, final java.net.URL url, boolean asyncFlag, final String userName, final String password)
			throws java.io.IOException {

		this.abort();
		// Proxy proxy = this.proxy;

		synchronized (this) {

			this.isAsync = asyncFlag;
			this.requestMethod = method;
			this.requestURL = url;
			this.requestUserName = userName;
			this.requestPassword = password;
			this.send = false;
			createTransaction();
		}
		this.changeState(HttpRequest.STATE_LOADING, 0, null, null);
	}

	/**
	 * Sends POST content, if any, and causes the request to proceed.
	 * <p>
	 * In the case of asynchronous requests, a new thread is created.
	 * 
	 * @param content
	 *            POST content or <code>null</code> if there's no such content.
	 */
	public void send(final String content) throws java.io.IOException {

		final java.net.URL url = this.requestURL;
		if (url == null) {
			throw new IOException("No URL has been provided.");
		}
		if (this.isAsync) {
			// Should use a thread pool instead

			Runnable asyncTask = new Runnable() {

				@Override
				public void run() {

					try {
						sendSync(content);
					} catch (Throwable thrown) {
						logger.warn("send(): Error in asynchronous request on " + url, thrown);
					}
				}
			};

			/* executor tasks are tracked to complete before shutdown */
			// scan.getGenericExecutorService().getExecutor().execute(asyncTask);
			// TODO: Bring in an executor

		} else {
			sendSync(content);
		}
	}

	public boolean wasSend() {

		return send;
	}

	public void setSent(boolean send) {

		this.send = send;
	}

	/**
	 * This is the charset used to post data provided to {@link #send(String)}.
	 * It returns "UTF-8" unless overridden.
	 */
	protected String getPostCharset() {

		return "UTF-8";
	}

	private void createTransaction() {

		try {

			URI uri = this.requestURL.toURI();
			rawTransaction = HttpTransactionFactory.createTransaction(HttpMethodType.getHttpMethodType(this.requestMethod), uri, null, null,
					TransactionSource.COBRA);

		} catch (URISyntaxException e) {
			logger.error("Problem with URI syntax at CobraHttpRequest.open: " + e.getMessage(), e);
			throw new DOMException(DOMException.SYNTAX_ERR, "URL Syntax error");

		} finally {

			/* clean up */
			// cobraUserAgent.removeHttpRequest(this);

		}

	}

	/**
	 * This is a synchronous implementation of {@link #send(String)} method
	 * functionality. It may be overridden to change the behavior of the class.
	 * 
	 * @param content
	 *            POST content if any. It may be <code>null</code>.
	 * @throws IOException
	 */
	protected void sendSync(String content) throws IOException {

		try {
			int transactionId = -1;
			boolean wasRequested = false;
			URI uri = this.requestURL.toURI();
			String craftedUri = uri.toASCIIString();
			// String username =
			// cobraUserAgent.getScan().getTransactionRecord().getTransaction(cobraUserAgent.getId())
			// .getUsername();
			// if (username == null) {
			// username = "";
			// }
			// if (content != null && content.length() > 0) {
			// if (this.requestMethod.equalsIgnoreCase("POST")) {
			//
			// craftedUri = craftedUri + "?" + content;
			// wasRequested =
			// scan.getTransactionRecord().isUriRequested(this.requestMethod,
			// craftedUri, username,
			// false);
			// transactionId =
			// scan.getTransactionRecord().getTransactionId(this.requestMethod,
			// craftedUri,
			// username);
			// }
			// }
			//
			// if (this.requestMethod.equalsIgnoreCase("GET")) {
			//
			// wasRequested =
			// scan.getTransactionRecord().isUriRequested(this.requestMethod,
			// uri.toASCIIString(),
			// username, false);
			// transactionId =
			// scan.getTransactionRecord().getTransactionId(this.requestMethod,
			// uri.toASCIIString(),
			// username);
			// }

			/*
			 * Possibility of 4 cases
			 * 
			 * Case 1: wasRequested = True, transactionId > 0 This means we can
			 * fetch the request from Cache/DB
			 * 
			 * Case 2: wasRequested = False, transactionId < 0 This means the
			 * request is new. So we have to take care of it here and also add
			 * it for spidering
			 * 
			 * Case 3: wasRequested = False, transactionId > 0 This is a Race
			 * condition. Very uncertain, unpredicatble and with low
			 * probability.
			 * 
			 * Case 4: wasRequested = True, transactionId < 0 This is also a
			 * Race condition. This state happens when spidering added the URL
			 * and before the transaction was saved to DB. Cache DB
			 * add/update/refresh happens just after the transaction is saved to
			 * DB.
			 */

			/* if (transactionId == -1 && !wasRequested) { */
			/*
			 * Solves Case 2
			 * 
			 * If the transaction is not in cache and if it was not requested
			 * before, add it to the requester queue for spidering
			 */

			/*
			 * if (this.requestMethod.equalsIgnoreCase("POST")) {
			 * 
			 * Header[] contentHeaders =
			 * rawTransaction.getRequestHeaders("Content-Type"); boolean
			 * isUrlEncoded = false;
			 * 
			 * if (contentHeaders.length > 0 &&
			 * contentHeaders[0].getValue().equalsIgnoreCase
			 * ("application/x-www-form-urlencoded")) { isUrlEncoded = true; }
			 * 
			 * if (!isUrlEncoded) { ((HttpPostTransaction)
			 * rawTransaction).setRequestPayload(new
			 * ByteArrayRequestPayload(content.getBytes(),
			 * ContentType.create(contentHeaders[0].getValue())) ); } else {
			 * ((HttpPostTransaction) rawTransaction).setRequestPayload(new
			 * URLEncodedRequestPayload(
			 * StringUtils.getDefaultCharset().toString(), content)); } }
			 * 
			 * TransactionSource referenceTransactionSource =
			 * scan.getTransactionRecord()
			 * .getTransaction(cobraUserAgent.getId()).getSource(); if
			 * (referenceTransactionSource != TransactionSource.TEST) {
			 * cobraUserAgent
			 * .getScan().getRequesterQueue().addSpiderRequest(rawTransaction,
			 * false, "Cobra"); }
			 * 
			 * } else if (transactionId > 0 && wasRequested) {
			 */
			/*
			 * Solves Case 1 and Case 3
			 * 
			 * If the request is found in cache, then fetch it and continue
			 */

			/*
			 * rawTransaction =
			 * scan.getTransactionRecord().getTransaction(transactionId);
			 * 
			 * } else if (transactionId == -1 && wasRequested) {
			 */
			/*
			 * Solves Case 4
			 * 
			 * 1. Wait to complete the processing by requesterThread? or 2.
			 * Duplicate request
			 * 
			 * Currently duplicating.
			 */

			/*
			 * if (this.requestMethod.equalsIgnoreCase("POST")) {
			 * 
			 * Header[] contentHeaders =
			 * rawTransaction.getRequestHeaders("Content-Type"); boolean
			 * isUrlEncoded = false;
			 * 
			 * if (contentHeaders.length > 0 &&
			 * contentHeaders[0].getValue().equalsIgnoreCase
			 * ("application/x-www-form-urlencoded")) { isUrlEncoded = true; }
			 * 
			 * if (!isUrlEncoded) { ((HttpPostTransaction)
			 * rawTransaction).setRequestPayload(new
			 * SimpleStringRequestPayload(content .getBytes())); } else {
			 * ((HttpPostTransaction) rawTransaction).setRequestPayload(new
			 * URLEncodedRequestPayload(
			 * StringUtils.getDefaultCharset().toString(), content)); }
			 * 
			 * }
			 * 
			 * TransactionSource referenceTransactionSource =
			 * scan.getTransactionRecord()
			 * .getTransaction(cobraUserAgent.getId()).getSource(); if
			 * (referenceTransactionSource != TransactionSource.TEST) {
			 * cobraUserAgent
			 * .getScan().getRequesterQueue().addSpiderRequest(rawTransaction,
			 * false, "Cobra"); }
			 * 
			 * }
			 */

			/*
			 * if (rawTransaction != null) { synchronized (rawTransaction) {
			 */

			/*
			 * Solves uncertain conditions
			 */

			/*
			 * if (!rawTransaction.isResponsePresent() ||
			 * !rawTransaction.isSuccessfullExecution()) {
			 */

			/*
			 * This should solve uncertain conditions like
			 * 
			 * - Transaction from cache/DB might not have response - A newly
			 * created transaction which is not executed
			 */

			/*
			 * rawTransaction.execute("CobraHttpRequest", false);
			 * 
			 * } else { // Debug.debug("Debug: raw ID: " + //
			 * rawTransaction.getId() + " rawUri: " + //
			 * rawTransaction.getMethod() // + " "
			 * +rawTransaction.getAbsoluteUriString() + //
			 * " Reason_ren:  Response loaded from cache");
			 * 
			 * /* Load it from cache
			 */

			/*
			 * } } }
			 */

			synchronized (this) {
				this.responseHeaders = rawTransaction.getAllResponseHeadersAsString();
				this.send = true;
			}
			this.changeState(HttpRequest.STATE_LOADED, rawTransaction.getResponseStatusCode(), rawTransaction.getResponse().getStatusLine()
					.getReasonPhrase(), null);
			this.changeState(HttpRequest.STATE_INTERACTIVE, rawTransaction.getResponseStatusCode(), rawTransaction.getResponse().getStatusLine()
					.getReasonPhrase(), null);
			this.changeState(HttpRequest.STATE_COMPLETE, rawTransaction.getResponseStatusCode(), rawTransaction.getResponse().getStatusLine()
					.getReasonPhrase(), rawTransaction.getResponseBody());

		} catch (URISyntaxException e) {
			logger.error("Problem with URI syntax at CobraHttpRequest.open: " + e.getMessage(), e);
			throw new DOMException(DOMException.SYNTAX_ERR, "URL Syntax error");

		} finally {

			/* clean up */
			// cobraUserAgent.removeHttpRequest(this);

		}
	}

	private final EventDispatch readyEvent = new EventDispatch();

	public void addReadyStateChangeListener(final ReadyStateChangeListener listener) {

		readyEvent.addListener(new GenericEventListener() {

			public void processEvent(EventObject event) {

				listener.readyStateChanged();
			}
		});
	}

	private void changeState(int readyState, int status, String statusMessage, byte[] bytes) {

		synchronized (this) {
			this.readyState = readyState;
			this.status = status;
			this.statusText = statusMessage;
			this.responseBytes = bytes;
		}
		this.readyEvent.fireEvent(null);
	}

	private void requestCleanup() {

	}

}
