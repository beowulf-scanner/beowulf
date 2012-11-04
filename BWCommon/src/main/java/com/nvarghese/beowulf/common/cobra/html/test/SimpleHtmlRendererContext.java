/*
 * GNU LESSER GENERAL PUBLIC LICENSE Copyright (C) 2006 The Lobo Project This
 * library is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this library; if not, write
 * to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston,
 * MA 02110-1301 USA Contact info: lobochief@users.sourceforge.net
 */
/*
 * Created on Oct 22, 2005
 */
package com.nvarghese.beowulf.common.cobra.html.test;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.html2.HTMLCollection;
import org.w3c.dom.html2.HTMLElement;
import org.w3c.dom.html2.HTMLLinkElement;

import com.nvarghese.beowulf.common.cobra.html.BrowserFrame;
import com.nvarghese.beowulf.common.cobra.html.FormInput;
import com.nvarghese.beowulf.common.cobra.html.HtmlObject;
import com.nvarghese.beowulf.common.cobra.html.HtmlRendererContext;
import com.nvarghese.beowulf.common.cobra.html.UserAgentContext;
import com.nvarghese.beowulf.common.cobra.html.domimpl.FrameNode;
import com.nvarghese.beowulf.common.cobra.html.domimpl.HTMLDocumentImpl;
import com.nvarghese.beowulf.common.cobra.html.parser.DocumentBuilderImpl;
import com.nvarghese.beowulf.common.cobra.html.parser.InputSourceImpl;
import com.nvarghese.beowulf.common.cobra.util.Urls;
import com.nvarghese.beowulf.common.cobra.util.io.RecordedInputStream;
import com.nvarghese.beowulf.common.http.renderer.CobraUserAgent;
import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;

/**
 * The <code>SimpleHtmlRendererContext</code> class implements the
 * {@link com.nvarghese.beowulf.common.cobra.html.HtmlRendererContext}
 * interface. Note that this class provides rudimentary implementations of most
 * callback methods. Overridding some of the methods in this class will usually
 * be necessary in a professional application.
 * <p>
 * A simple way to load a URL into the {@link HtmlPanel} of the renderer context
 * is to invoke {@link #navigate(String)}.
 */
public class SimpleHtmlRendererContext implements HtmlRendererContext {

	private static final Logger logger = Logger.getLogger(SimpleHtmlRendererContext.class.getName());

	// private final HtmlPanel htmlPanel;
	private final HtmlRendererContext parentRcontext;

	private static final Set mediaNames = new HashSet();
	static {
		// Media names claimed by this context.
		Set mn = mediaNames;
		mn.add("screen");
		mn.add("tv");
		mn.add("tty");
		mn.add("all");
	}

	/**
	 * Constructs a SimpleHtmlRendererContext.
	 */
	public SimpleHtmlRendererContext() {

		this((UserAgentContext) null);
	}

	/**
	 * Constructs a SimpleHtmlRendererContext.
	 * 
	 * @param contextComponent
	 *            The component that will render HTML.
	 * @see SimpleUserAgentContext
	 */
	public SimpleHtmlRendererContext(final UserAgentContext ucontext) {

		super();
		// this.htmlPanel = contextComponent;
		this.parentRcontext = null;
		this.bcontext = ucontext;
	}

	/**
	 * Constructs a SimpleHtmlRendererContext that is a child of another
	 * <code>{@link HtmlRendererContext}</code>.
	 * 
	 * @param contextComponent
	 *            The component that will render HTML.
	 * @param parentRcontext
	 *            The parent's renderer context.
	 */
	public SimpleHtmlRendererContext(final HtmlRendererContext parentRcontext) {

		super();
		this.parentRcontext = parentRcontext;
		this.bcontext = parentRcontext == null ? null : parentRcontext.getUserAgentContext();
	}

	public SimpleHtmlRendererContext(final UserAgentContext ucontext, final HtmlRendererContext parentRcontext) {

		this.parentRcontext = parentRcontext;
		this.bcontext = ucontext;
	}

	/*
	 * public HtmlPanel getHtmlPanel() { return this.htmlPanel; }
	 */

	private volatile String sourceCode;

	/**
	 * Gets the source code of the current HTML document.
	 */
	public String getSourceCode() {

		return this.sourceCode;
	}

	/**
	 * Gets a collection of current document frames, by querying the document
	 * currently held by the local {@link org.cobra_grendel.html.gui.HtmlPanel}
	 * instance. Currently Disabled.
	 */
	public HTMLCollection getFrames() {

		return null;
		/*
		 * Object rootNode = this.htmlPanel.getRootNode(); if(rootNode
		 * instanceof HTMLDocumentImpl) { return ((HTMLDocumentImpl)
		 * rootNode).getFrames(); } else { return null; }
		 */
	}

	/**
	 * Implements reload as navigation to current URL. Override to implement a
	 * more robust reloading mechanism. Disabled currently.
	 */
	public void reload() {

		/*
		 * HTMLDocumentImpl document = (HTMLDocumentImpl)
		 * this.htmlPanel.getRootNode(); if(document != null) { try { URL url =
		 * new URL(document.getDocumentURI()); this.navigate(url, null); }
		 * catch(java.net.MalformedURLException throwable) {
		 * this.warn("reload(): Malformed URL", throwable); } }
		 */
	}

	/**
	 * Implements the link click handler by invoking
	 * {@link #navigate(URL, String)}.
	 */
	public void linkClicked(final HTMLElement linkNode, final URL url, final String target) {

		this.navigate(url, target);
	}

	/**
	 * Gets the connection proxy used in {@link #navigate(URL, String)}. This
	 * implementation calls {@link SimpleUserAgentContext#getProxy()} if
	 * {@link #getUserAgentContext()} returns an instance assignable to
	 * {@link SimpleUserAgentContext}. The method may be overridden to provide a
	 * different proxy setting.
	 */
	protected Proxy getProxy() {

		Object ucontext = this.getUserAgentContext();
		if (ucontext instanceof SimpleUserAgentContext) {
			return ((SimpleUserAgentContext) ucontext).getProxy();
		}
		return Proxy.NO_PROXY;
	}

	/**
	 * Implements simple navigation with incremental rendering by invoking
	 * {@link #submitForm(String, URL, String, String, FormInput[])} with a
	 * <code>GET</code> request method.
	 */
	// public void navigate(final URL href, String target) {
	// this.submitForm("GET", href, target, null, null);
	// }

	/**
	 * Implements simple navigation with incremental rendering, and target
	 * processing, including frame lookup. Should be overridden to allow for
	 * more robust browser navigation.
	 * <p>
	 * <b>Notes:</b>
	 * <ul>
	 * <li>Encoding ISO-8859-1 assumed always.
	 * <li>Caching is not implemented.
	 * <li>Cookies are not implemented.
	 * <li>Incremental rendering is not optimized for ignorable document change
	 * notifications.
	 * <li>Other HTTP features are not implemented.
	 * </ul>
	 */
	public void navigate(final URL href, String target) {

		// This method implements simple incremental rendering.
		if (target != null) {
			HtmlRendererContext topCtx = getTop();
			HTMLCollection frames = topCtx.getFrames();
			if (frames != null) {
				org.w3c.dom.Node frame = frames.namedItem(target);
				if (frame instanceof FrameNode) {
					BrowserFrame bframe = ((FrameNode) frame).getBrowserFrame();
					if (bframe == null) {
						throw new IllegalStateException("Frame node without a BrowserFrame instance: " + frame);
					}
					if (bframe.getHtmlRendererContext() != this) {
						bframe.loadURL(href);
						return;
					}
				}
			}
			target = target.trim().toLowerCase();
			if ("_top".equals(target)) {
				getTop().navigate(href, null);
				return;
			} else if ("_parent".equals(target)) {
				HtmlRendererContext parent = getParent();
				if (parent != null) {
					parent.navigate(href, null);
					return;
				}
			} else if ("_blank".equals(target)) {
				this.open(href.toExternalForm(), "cobra.blank", "", false);
				return;
			} else {
				// fall through
			}
		}

		URL urlForLoading;
		if (href.getProtocol().equals("file")) {
			// Remove query so it works.
			try {
				urlForLoading = new URL(href.getProtocol(), href.getHost(), href.getPort(), href.getPath());
			} catch (java.net.MalformedURLException throwable) {
				this.warn("malformed", throwable);
				urlForLoading = href;
			}
		} else {
			urlForLoading = href;
		}
		final URL finalURLForLoading = urlForLoading;

		CobraUserAgent uaContext = (CobraUserAgent) this.getUserAgentContext();
		URI requestURI = null;

		try {
			requestURI = urlForLoading.toURI();
		} catch (URISyntaxException e) {
			logger.warning("navigate(): Error in converting URL to URI for uri=" + urlForLoading);
		}

		int transactionId = -1;
		boolean wasRequested = false;
		String username = "";

		AbstractHttpTransaction rawTransaction = null;
		final long time0 = System.currentTimeMillis();
		if (requestURI != null) {

			// try {
			//
			// username = "";
			// //TODO:
			// //username =
			// uaContext.getScan().getTransactionRecord().getTransaction(uaContext.getId()).getUsername();
			//
			// if(username != null && !username.equalsIgnoreCase("")) {

			// transactionId =
			// uaContext.getScan().getTransactionRecord().getTransactionId("GET",
			// finalURLForLoading.toString(), username);
			//
			// wasRequested =
			// uaContext.getScan().getTransactionRecord().isUriRequested("GET",
			// finalURLForLoading.toString(), username, false);
			// } else {
			// transactionId =
			// uaContext.getScan().getTransactionRecord().getTransactionId("GET",
			// finalURLForLoading.toString(), null);
			//
			// wasRequested =
			// uaContext.getScan().getTransactionRecord().isUriRequested("GET",
			// finalURLForLoading.toString(), null, false);

			// }
			//

			/*
			 * Possibility of 4 cases Case 1: wasRequested = True, transactionId
			 * > 0 This means we can fetch the request from Cache/DB Case 2:
			 * wasRequested = False, transactionId < 0 This means the request is
			 * new. So we have to take care of it here and also add it for
			 * spidering Case 3: wasRequested = False, transactionId > 0 This is
			 * a Race condition. Very uncertain, unpredicatble and with low
			 * probability. Case 4: wasRequested = True, transactionId < 0 This
			 * is also a Race condition. This state happens when spidering added
			 * the URL and before the transaction was saved to DB. Cache DB
			 * add/update/refresh happens just after the transaction is saved to
			 * DB
			 */

			// if (transactionId == -1 && !wasRequested) {
			// /*
			// * Solves Case 2 If the transaction is not in cache and if
			// * it was not requested before, add it to the requester
			// * queue for spidering
			// */
			// rawTransaction =
			// HttpTransactionFactory.createTransaction(uaContext.getScan(),
			// "GET", requestURI,
			// uaContext.getId(), TransactionSource.COBRA);
			// // Debug.debug("Debug: raw ID: " + rawTransaction.getId() +
			// // " rawUri: " + rawTransaction.getMethod() + " " +
			// // rawTransaction.getAbsoluteUriString() +
			// // " Reason_ren: No cache");
			// TransactionSource referenceTransactionSource =
			// uaContext.getScan().getTransactionRecord()
			// .getTransaction(uaContext.getId()).getSource();
			// if (referenceTransactionSource != TransactionSource.TEST) {
			// uaContext.getScan().getRequesterQueue().addSpiderRequest(rawTransaction,
			// false,
			// "Javascript: Asynchronous Requests ");
			// }
			//
			// } else if (transactionId > 0) {
			// /*
			// * Solves Case 1 and Case 3 If the request is found in
			// * cache, then fetch it and continue
			// */
			// rawTransaction =
			// uaContext.getScan().getTransactionRecord().getTransaction(transactionId);
			// // Debug.debug("Debug: raw ID: " + rawTransaction.getId() +
			// // " rawUri: " + rawTransaction.getMethod() + " "
			// // +rawTransaction.getAbsoluteUriString() +
			// // " Reason_ren: Cache hit");
			//
			// } else if (transactionId == -1 && wasRequested) {
			// /*
			// * Solves Case 4 1. Wait to complete the processing by
			// * requesterThread? or 2. Duplicate request Currently
			// * duplicating.
			// */
			// rawTransaction =
			// HttpTransactionFactory.createTransaction(uaContext.getScan(),
			// "GET", requestURI,
			// uaContext.getId(), TransactionSource.COBRA);
			// // Debug.debug("Debug: raw ID: " + rawTransaction.getId() +
			// // " rawUri: " + rawTransaction.getMethod() + " " +
			// // rawTransaction.getAbsoluteUriString() +
			// // " Reason_ren: No cache, but requested");
			// }
			//
			// if (rawTransaction != null) {
			// synchronized (rawTransaction) {
			//
			// /*
			// * Solves uncertain conditions
			// */
			//
			// if (!rawTransaction.isResponsePresent() ||
			// !rawTransaction.isSuccessfullExecution()) {
			//
			// /*
			// * This should solve uncertain conditions like -
			// * Transaction from cache/DB might not have response
			// * - A newly created transaction which is not
			// * executed
			// */
			//
			// rawTransaction.execute("CobraHttpRequest", false);
			//
			// } else {
			//
			//
			// /*
			// * Load it from cache
			// */
			//
			// }
			// }
			// }
			//
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			//
		}

		final AbstractHttpTransaction finalTransaction = rawTransaction;

		// Make request asynchronously.
		new Thread() {

			@Override
			public void run() {

				try {
					URL uri = href;

					logger.info("process(): Loading URI=[" + uri + "].");

					if (finalTransaction != null) {

						// try {
						// sourceCode = null;
						// long time1 = System.currentTimeMillis();
						// byte[] input = finalTransaction.getResponseBody();
						// if (input != null) {
						// ByteArrayInputStream inputStream = new
						// ByteArrayInputStream(input);
						// InputSource inputSource = new
						// InputSourceImpl(inputStream,
						// finalTransaction.getURI()
						// , finalTransaction.getContentType().getCharset());
						//
						// DocumentBuilderImpl builder = new
						// DocumentBuilderImpl(SimpleHtmlRendererContext.this
						// .getUserAgentContext(),
						// SimpleHtmlRendererContext.this);
						//
						// if (inputSource != null) {
						// HTMLDocumentImpl document = (HTMLDocumentImpl)
						// builder.createDocument(inputSource);
						// // Now start loading.
						// document.load();
						// long time2 = System.currentTimeMillis();
						// logger.info("Parsed URI=[" + uri +
						// "]: Parse elapsed: " + (time2 - time1)
						// + " ms. Fetched from Cache/DB: " + (time1 - time0) +
						// " ms.");
						// sourceCode = inputStream.toString();
						// }
						//
						// }
						// } finally {
						//
						// }

					} else {
						/*
						 * The last ditch effort using java libraries
						 */

						// Using potentially different URL for loading.

						URLConnection connection = finalURLForLoading.openConnection();
						connection.setRequestProperty("User-Agent", getUserAgentContext().getUserAgent());
						connection.setRequestProperty("Cookie", "");
						if (connection instanceof HttpURLConnection) {
							HttpURLConnection hc = (HttpURLConnection) connection;
							hc.setInstanceFollowRedirects(true);
							int responseCode = hc.getResponseCode();
							logger.info("process(): HTTP response code: " + responseCode);
						}

						InputStream in = connection.getInputStream();
						try {
							sourceCode = null;
							long time1 = System.currentTimeMillis();
							RecordedInputStream rin = new RecordedInputStream(in, 8192);
							InputStream bin = new BufferedInputStream(rin, 8192);
							// HtmlParserContext pcontext =
							// createParserContext(uri);
							// SimpleUserAgentContext ucontext = new
							// SimpleUserAgentContext();

							DocumentBuilderImpl builder = new DocumentBuilderImpl(SimpleHtmlRendererContext.this.getUserAgentContext(),
									SimpleHtmlRendererContext.this);
							String actualURI = uri.toExternalForm();
							// Only create document, don't parse.
							HTMLDocumentImpl document = (HTMLDocumentImpl) builder.createDocument(new InputSourceImpl(bin, actualURI, "ISO-8859-1"));
							// Set document in HtmlPanel. Safe to call outside
							// GUI
							// thread.
							// SimpleHtmlRendererContext.this.htmlPanel.setDocument(document,
							// SimpleHtmlRendererContext.this);
							// Now start loading.
							document.load();
							long time2 = System.currentTimeMillis();
							logger.info("Parsed URI=[" + uri + "]: Parse elapsed: " + (time2 - time1) + " ms. Connection elapsed: " + (time1 - time0)
									+ " ms.");
							sourceCode = rin.getString("ISO-8859-1");
						} finally {
							in.close();
						}
					}
				} catch (Exception err) {
					SimpleHtmlRendererContext.this.error("navigate(): Error loading or parsing request.", err);
				}
			}
		}.start();
	}

	/**
	 * Convenience method provided to allow loading a document into the
	 * renderer.
	 * 
	 * @param fullURL
	 *            The absolute URL of the document.
	 * @see #navigate(URL, String)
	 */
	public void navigate(final String fullURL) throws java.net.MalformedURLException {

		java.net.URL href = Urls.createURL(null, fullURL);
		this.navigate(href, "_this");
	}

	/**
	 * Redirects form submission through scanner. Disabled other features for
	 * incremental rendering. Original Text follows: Implements simple
	 * navigation and form submission with incremental rendering and target
	 * processing, including frame lookup. Should be overridden to allow for
	 * more robust browser navigation and form submission. Use
	 * #navigate(URL,String)
	 * <p>
	 * <b>Notes:</b>
	 * <ul>
	 * <li>Document encoding is defined by
	 * {@link #getDocumentCharset(URLConnection)}.
	 * <li>Caching is not implemented.
	 * <li>Cookies are not implemented.
	 * <li>Incremental rendering is not optimized for ignorable document change
	 * notifications.
	 * <li>Other HTTP features are not implemented.
	 * <li>The only form encoding type supported is
	 * <code>application/x-www-form-urlencoded</code>.
	 * <li>Navigation is normally asynchronous. See
	 * {@link #isNavigationAsynchronous()}.
	 * </ul>
	 * 
	 * @see #navigate(URL, String)
	 */
	public void submitForm(final String method, final java.net.URL action, final String target, final String enctype, final FormInput[] formInputs) {

		/*
		 * StringBuffer sb = new StringBuffer(); String lineBreak =
		 * System.getProperty("line.separator"); if (formInputs != null) { for
		 * (int i = 0; i < formInputs.length; i++) { sb.append("INPUT: " +
		 * formInputs[i].toString()); sb.append(lineBreak); } }
		 * //this.warn("submitForm(): Not overridden; method=" + method +
		 * "; action=" + action + "; target=" + target // + "; enctype=" +
		 * enctype + lineBreak + sb);
		 */
		// URI actionURI = null;
		// AbstractStandardHttpTransaction request = null;
		// CobraUserAgent uaContext = (CobraUserAgent)
		// this.getUserAgentContext();
		// try {
		// actionURI = action.toURI();
		// } catch (URISyntaxException e) {
		// this.warn("submitForm(): Error in converting URL to URI for action="
		// + action);
		//
		// }
		//
		// try {
		// request =
		// HttpTransactionFactory.createTransaction(uaContext.getScan(), method,
		// actionURI, uaContext
		// .getId(), TransactionSource.SPIDER);
		// if (formInputs != null) {
		// String defaultText =
		// ConfigurationManager.getString("default_form_values.default_text");
		// List<String> formParamsList =
		// ConfigurationManager.getList("default_form_values.params_list");
		// for (int i = 0; i < formInputs.length; i++) {
		// if (formInputs[i].isText() &&
		// !(formInputs[i].getTextValue().equalsIgnoreCase(""))) {
		// request.addQueryParameter(formInputs[i].getName(),
		// formInputs[i].getTextValue());
		// } else {
		// request.addQueryParameter(formInputs[i].getName(),
		// ConfigurationManager.getString(
		// "default_form_values." + getConfigparam(formInputs[i].getName(),
		// formParamsList),
		// formInputs[i].getName() + defaultText));
		// }
		// }
		// }
		//
		// uaContext.getScan().getRequesterQueue().addSpiderRequest(request,
		// false, "Javascript: Form Submit() ");
		// } catch (URISyntaxException e) {
		//
		// this.warn("URI syntax problem in (" + action + "): " + e.toString());
		// }

	}

	/*
	 * public void submitForm(final String method, final java.net.URL action,
	 * final String target, final String enctype, final FormInput[] formInputs)
	 * { // This method implements simple incremental rendering. if(target !=
	 * null) { HtmlRendererContext topCtx = this.getTop(); HTMLCollection frames
	 * = topCtx.getFrames(); if(frames != null) { org.w3c.dom.Node frame =
	 * frames.namedItem(target); if(logger.isLoggable(Level.INFO)) {
	 * logger.info("submitForm(): Frame matching target=" + target + " is " +
	 * frame); } if(frame instanceof FrameNode) { BrowserFrame bframe =
	 * ((FrameNode) frame).getBrowserFrame(); if(bframe == null) { throw new
	 * IllegalStateException("Frame node without a BrowserFrame instance: " +
	 * frame); } if(bframe.getHtmlRendererContext() != this) {
	 * bframe.loadURL(action); return; } } } String actualTarget =
	 * target.trim().toLowerCase(); if("_top".equals(actualTarget)) {
	 * this.getTop().navigate(action, null); return; } else if
	 * ("_parent".equals(actualTarget)) { HtmlRendererContext parent =
	 * this.getParent(); if(parent != null) { parent.navigate(action, null);
	 * return; } } else if("_blank".equals(actualTarget)) { this.open(action,
	 * "cobra.blank", "", false); return; } else
	 * if("_this".equals(actualTarget)) { // fall through } else {
	 * logger.warning("submitForm(): Link target unrecognized: " +
	 * actualTarget); } } // Make request asynchronously.
	 * if(this.isNavigationAsynchronous()) { new Thread() { public void run() {
	 * try { SimpleHtmlRendererContext.this.submitFormSync(method, action,
	 * target, enctype, formInputs); } catch (Exception err) {
	 * SimpleHtmlRendererContext.this.error(
	 * "navigate(): Error loading or parsing request.", err); } } }.start(); }
	 * else { try { SimpleHtmlRendererContext.this.submitFormSync(method,
	 * action, target, enctype, formInputs); } catch (Exception err) {
	 * SimpleHtmlRendererContext.this.error(
	 * "navigate(): Error loading or parsing request.", err); } } }
	 */

	private String getConfigparam(final String formParam, final List<String> formParamList) {

		for (String param : formParamList) {
			if (formParam.toLowerCase().contains(param)) {
				return param;
			}
		}
		return formParam;
	}

	/**
	 * Indicates whether navigation (via
	 * {@link #submitForm(String, URL, String, String, FormInput[])}) should be
	 * asynchronous. This overridable implementation returns <code>true</code>.
	 */
	protected boolean isNavigationAsynchronous() {

		return true;
	}

	/**
	 * The connection currently opened by openSync() if any.
	 */
	protected URLConnection currentConnection;

	/**
	 * Submits a form and/or navigates by making a <i>synchronous</i> request.
	 * This method is invoked by
	 * {@link #submitForm(String, URL, String, String, FormInput[])}.
	 * 
	 * @param method
	 *            The request method.
	 * @param action
	 *            The action URL.
	 * @param target
	 *            The target identifier.
	 * @param enctype
	 *            The encoding type.
	 * @param formInputs
	 *            The form inputs.
	 * @throws IOException
	 * @throws org.xml.sax.SAXException
	 * @see #submitForm(String, URL, String, String, FormInput[])
	 */
	/*
	 * protected void submitFormSync(final String method, final java.net.URL
	 * action, final String target, String enctype, final FormInput[]
	 * formInputs) throws IOException, org.xml.sax.SAXException { final String
	 * actualMethod = method.toUpperCase(); URL resolvedURL;
	 * if("GET".equals(actualMethod) && formInputs != null) { boolean firstParam
	 * = true; //TODO: What about the userInfo part of the URL? URL noRefAction
	 * = new URL(action.getProtocol(), action.getHost(), action.getPort(),
	 * action.getFile()); StringBuffer newUrlBuffer = new
	 * StringBuffer(noRefAction.toExternalForm()); if(action.getQuery() == null)
	 * { newUrlBuffer.append("?"); } else { newUrlBuffer.append("&"); } for(int
	 * i = 0; i < formInputs.length; i++) { FormInput parameter = formInputs[i];
	 * String name = parameter.getName(); String encName =
	 * URLEncoder.encode(name, "UTF-8"); if(parameter.isText()) { if(firstParam)
	 * { firstParam = false; } else { newUrlBuffer.append("&"); } String
	 * valueStr = parameter.getTextValue(); String encValue =
	 * URLEncoder.encode(valueStr, "UTF-8"); newUrlBuffer.append(encName);
	 * newUrlBuffer.append("="); newUrlBuffer.append(encValue); } else {
	 * logger.warning("postData(): Ignoring non-textual parameter " + name +
	 * " for GET."); } } resolvedURL = new
	 * java.net.URL(newUrlBuffer.toString()); } else { resolvedURL = action; }
	 * URL urlForLoading; if(resolvedURL.getProtocol().equalsIgnoreCase("file"))
	 * { // Remove query so it works. try { String ref = action.getRef(); String
	 * refText = ref == null || ref.length() == 0 ? "" : "#" + ref;
	 * urlForLoading = new URL(resolvedURL.getProtocol(), action.getHost(),
	 * action.getPort(), action.getPath() + refText); }
	 * catch(java.net.MalformedURLException throwable) { this.warn("malformed",
	 * throwable); urlForLoading = action; } } else { urlForLoading =
	 * resolvedURL; } if(logger.isLoggable(Level.INFO)) {
	 * logger.info("process(): Loading URI=[" + urlForLoading + "]."); } long
	 * time0 = System.currentTimeMillis(); // Using potentially different URL
	 * for loading. Proxy proxy = SimpleHtmlRendererContext.this.getProxy();
	 * boolean isPost = "POST".equals(actualMethod); URLConnection connection =
	 * proxy == null || proxy == Proxy.NO_PROXY ? urlForLoading.openConnection()
	 * : urlForLoading.openConnection(proxy); this.currentConnection =
	 * connection; try { connection.setRequestProperty("User-Agent",
	 * getUserAgentContext().getUserAgent());
	 * connection.setRequestProperty("Cookie", ""); if (connection instanceof
	 * HttpURLConnection) { HttpURLConnection hc = (HttpURLConnection)
	 * connection; hc.setRequestMethod(actualMethod);
	 * hc.setInstanceFollowRedirects(false); } if(isPost) {
	 * connection.setDoOutput(true); ByteArrayOutputStream bufOut = new
	 * ByteArrayOutputStream(); boolean firstParam = true; if(formInputs !=
	 * null) { for(int i = 0; i < formInputs.length; i++) { FormInput parameter
	 * = formInputs[i]; String name = parameter.getName(); String encName =
	 * URLEncoder.encode(name, "UTF-8"); if(parameter.isText()) { if(firstParam)
	 * { firstParam = false; } else { bufOut.write((byte) '&'); } String
	 * valueStr = parameter.getTextValue(); String encValue =
	 * URLEncoder.encode(valueStr, "UTF-8");
	 * bufOut.write(encName.getBytes("UTF-8")); bufOut.write((byte) '=');
	 * bufOut.write(encValue.getBytes("UTF-8")); } else {
	 * logger.warning("postData(): Ignoring non-textual parameter " + name +
	 * " for POST."); } } } // Do not add a line break to post content. Some
	 * servers // can be picky about that (namely, java.net). byte[] postContent
	 * = bufOut.toByteArray(); if(connection instanceof HttpURLConnection) {
	 * ((HttpURLConnection)
	 * connection).setFixedLengthStreamingMode(postContent.length); }
	 * connection.setRequestProperty("Content-Type",
	 * "application/x-www-form-urlencoded");
	 * //connection.setRequestProperty("Content-Length",
	 * String.valueOf(postContent.length)); OutputStream postOut =
	 * connection.getOutputStream(); postOut.write(postContent);
	 * postOut.flush(); } if (connection instanceof HttpURLConnection) {
	 * HttpURLConnection hc = (HttpURLConnection) connection; int responseCode =
	 * hc.getResponseCode(); if(logger.isLoggable(Level.INFO)) {
	 * logger.info("process(): HTTP response code: " + responseCode); }
	 * if(responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode ==
	 * HttpURLConnection.HTTP_MOVED_TEMP || responseCode ==
	 * HttpURLConnection.HTTP_SEE_OTHER) { String location =
	 * hc.getHeaderField("Location"); if(location == null) {
	 * logger.warning("No Location header in redirect from " + action + "."); }
	 * else { java.net.URL href; href = Urls.createURL(action, location);
	 * SimpleHtmlRendererContext.this.navigate(href, target); } return; } }
	 * InputStream in = connection.getInputStream(); try {
	 * SimpleHtmlRendererContext.this.sourceCode = null; long time1 =
	 * System.currentTimeMillis(); RecordedInputStream rin = new
	 * RecordedInputStream(in, 1000000); InputStream bin = new
	 * BufferedInputStream(rin, 8192); String actualURI =
	 * urlForLoading.toExternalForm(); // Only create document, don't parse.
	 * HTMLDocumentImpl document = this.createDocument(new InputSourceImpl(bin,
	 * actualURI, getDocumentCharset(connection))); // Set document in
	 * HtmlPanel. Safe to call outside GUI thread. HtmlPanel panel = htmlPanel;
	 * panel.setDocument(document, SimpleHtmlRendererContext.this); // Now start
	 * loading. document.load(); long time2 = System.currentTimeMillis();
	 * if(logger.isLoggable(Level.INFO)) { logger.info("Parsed URI=[" +
	 * urlForLoading + "]: Parse elapsed: " + (time2 - time1) +
	 * " ms. Connection elapsed: " + (time1 - time0) + " ms."); } String ref =
	 * urlForLoading.getRef(); if(ref != null && ref.length() != 0) {
	 * panel.scrollToElement(ref); } try {
	 * SimpleHtmlRendererContext.this.sourceCode = rin.getString("ISO-8859-1");
	 * } catch(BufferExceededException bee) {
	 * SimpleHtmlRendererContext.this.sourceCode = "[TOO BIG]"; } } finally {
	 * in.close(); } } finally { this.currentConnection = null; } }
	 */

	/**
	 * Creates a blank document instance. This method is invoked whenever
	 * navigation or form submission occur. It is provided so it can be
	 * overridden to create specialized document implmentations.
	 * 
	 * @param inputSource
	 *            The document input source.
	 * @throws IOException
	 * @throws org.xml.sax.SAXException
	 */
	protected HTMLDocumentImpl createDocument(final org.xml.sax.InputSource inputSource) throws IOException, org.xml.sax.SAXException {

		DocumentBuilderImpl builder = new DocumentBuilderImpl(this.getUserAgentContext(), SimpleHtmlRendererContext.this);
		return (HTMLDocumentImpl) builder.createDocument(inputSource);
	}

	/**
	 * This method is invoked by
	 * {@link #submitForm(String, URL, String, String, FormInput[])} to
	 * determine the charset of a document. The charset is determined by looking
	 * at the <code>Content-Type</code> header.
	 * 
	 * @param connection
	 *            A URL connection.
	 */
	protected String getDocumentCharset(final URLConnection connection) {

		String encoding = Urls.getCharset(connection);
		return encoding == null ? "ISO-8859-1" : encoding;
	}

	// Methods useful to Window below:

	/**
	 * Opens a simple message dialog. Currently Disabled.
	 */
	public void alert(final String message) {

	}

	/**
	 * It should give up focus on the current browser window. This
	 * implementation does nothing and should be overridden.
	 */
	public void blur() {

		this.warn("back(): Not overridden");
	}

	/**
	 * It should close the current browser window. This implementation does
	 * nothing and should be overridden.
	 */
	public void close() {

		this.warn("close(): Not overridden");
	}

	/**
	 * Opens a simple confirmation window.
	 */
	public boolean confirm(final String message) {

		return true;
		/*
		 * int retValue = JOptionPane.showConfirmDialog(htmlPanel, message,
		 * "Confirm", JOptionPane.YES_NO_OPTION); return retValue ==
		 * JOptionPane.YES_OPTION;
		 */
	}

	/**
	 * It should request focus for the current browser window. This
	 * implementation does nothing and should be overridden.
	 */
	public void focus() {

		this.warn("focus(): Not overridden");
	}

	/**
	 * @deprecated Use {@link #open(URL, String, String, boolean)}.
	 */
	public final HtmlRendererContext open(String url, String windowName, String windowFeatures, boolean replace) {

		URL urlObj;
		try {
			urlObj = new URL(url);
		} catch (MalformedURLException mfu) {
			throw new IllegalArgumentException("Malformed URL: " + url);
		}
		return this.open(urlObj, windowName, windowFeatures, replace);
	}

	/**
	 * It should open a new browser window. This implementation does nothing and
	 * should be overridden.
	 * 
	 * @param url
	 *            The requested URL.
	 * @param windowName
	 *            A window identifier.
	 * @param windowFeatures
	 *            Window features specified in a format equivalent to that of
	 *            window.open() in Javascript.
	 * @param replace
	 *            Whether an existing window with the same name should be
	 *            replaced.
	 */
	public HtmlRendererContext open(java.net.URL url, String windowName, String windowFeatures, boolean replace) {

		// this.warn("open(): Not overridden");

		boolean wasRequested = false;

		AbstractHttpTransaction rawTransaction = null;
		CobraUserAgent uaContext = (CobraUserAgent) this.getUserAgentContext();

		if (url != null) {

			// try {
			//
			// String username =
			// uaContext.getScan().getTransactionRecord().getTransaction(uaContext.getId()).getUsername();
			// if(username == null) {
			// username = "";
			// }
			// wasRequested =
			// uaContext.getScan().getTransactionRecord().isUriRequested("GET",
			// url.toString(), username, false);
			//
			// /*
			// * A cache look-up is not required here
			// */
			//
			// if (!wasRequested) {
			// /*
			// * If the request was not requested before, add it to the
			// * requester queue for spidering
			// */
			// rawTransaction =
			// HttpTransactionFactory.createTransaction(uaContext.getScan(),
			// "GET", url.toURI(),
			// uaContext.getId(), TransactionSource.COBRA);
			// // Debug.debug("Debug: raw ID: " + rawTransaction.getId() +
			// // " rawUri: " + rawTransaction.getMethod() + " " +
			// // rawTransaction.getAbsoluteUriString() +
			// // " Reason_ren: No cache");
			// TransactionSource referenceTransactionSource =
			// uaContext.getScan().getTransactionRecord()
			// .getTransaction(uaContext.getId()).getSource();
			// if (referenceTransactionSource != TransactionSource.TEST) {
			// uaContext.getScan().getRequesterQueue().addSpiderRequest(rawTransaction,
			// false,
			// "Javascript: Window.open ");
			// }
			//
			// }
			//
			// } catch (Exception e) {
			// // TODO: handle exception
			// }

		}

		return null;
	}

	/**
	 * Shows a simple prompt dialog.
	 */
	public String prompt(String message, String inputDefault) {

		return "";
	}

	/*
	 * public String prompt(String message, String inputDefault) { return
	 * JOptionPane.showInputDialog(htmlPanel, message); }
	 */

	/**
	 * Changes the origin of the HTML block's scrollable area according to the
	 * position given.
	 * <p>
	 * This method may be called outside of the GUI thread. The operation is
	 * scheduled immediately in that thread as needed.
	 * 
	 * @param x
	 *            The new x coordinate for the origin.
	 * @param y
	 *            The new y coordinate for the origin.
	 */
	/*
	 * public void scroll(int x, int y) { this.htmlPanel.scroll(x, y); } public
	 * void scrollBy(int x, int y) { this.htmlPanel.scrollBy(x, y); }
	 */

	/**
	 * Should return true if and only if the current browser window is closed.
	 * This implementation returns false and should be overridden.
	 */
	public boolean isClosed() {

		this.warn("isClosed(): Not overridden");
		return false;
	}

	/**
	 * Should return true if and only if the current browser window is closed.
	 * This implementation returns false and should be overridden.
	 */
	public String getDefaultStatus() {

		this.warn("getDefaultStatus(): Not overridden");
		return "";
	}

	/**
	 * It should return the name of the browser window, if this renderer context
	 * is for the top frame in the window. This implementation returns a blank
	 * string, so it should be overridden.
	 */
	public String getName() {

		this.warn("getName(): Not overridden");
		return "";
	}

	public HtmlRendererContext getParent() {

		return this.parentRcontext;
	}

	private volatile HtmlRendererContext opener;

	public HtmlRendererContext getOpener() {

		return this.opener;
	}

	public void setOpener(HtmlRendererContext opener) {

		this.opener = opener;
	}

	public String getStatus() {

		this.warn("getStatus(): Not overridden");
		return "";
	}

	public void setStatus(String message) {

		this.warn("setStatus(): Not overridden");
	}

	public HtmlRendererContext getTop() {

		HtmlRendererContext ancestor = this.parentRcontext;
		if (ancestor == null) {
			return this;
		}
		return ancestor.getTop();
	}

	public BrowserFrame createBrowserFrame() {

		return null;
		// return new SimpleBrowserFrame(this);
	}

	public void warn(String message, Throwable throwable) {

		if (logger.isLoggable(Level.WARNING)) {
			logger.log(Level.WARNING, message, throwable);
		}
	}

	public void error(String message, Throwable throwable) {

		if (logger.isLoggable(Level.SEVERE)) {
			logger.log(Level.SEVERE, message, throwable);
		}
	}

	public void warn(String message) {

		if (logger.isLoggable(Level.WARNING)) {
			logger.log(Level.WARNING, message);
		}
	}

	public void error(String message) {

		if (logger.isLoggable(Level.SEVERE)) {
			logger.log(Level.SEVERE, message);
		}
	}

	/**
	 * Returns <code>null</code>. This method should be overridden to provide
	 * OBJECT, EMBED or APPLET functionality.
	 */
	public HtmlObject getHtmlObject(HTMLElement element) {

		// Retaining old cobra_grendel implementation

		HtmlObject result;
		if ("OBJECT".equalsIgnoreCase(element.getTagName())) {
			result = null;
		} else {
			result = new SimpleHtmlObject(element);
		}
		this.warn("getHtmlObject(): Not overridden; returning " + result + " for " + element + ".");
		return result;
	}

	/*
	 * public HtmlObject getHtmlObject(HTMLElement element) { return null; }
	 */

	public void setDefaultStatus(String message) {

		this.warn("setDefaultStatus(): Not overridden.");
	}

	private UserAgentContext bcontext = null;

	/**
	 * If a {@link com.nvarghese.beowulf.common.cobra.html.UserAgentContext}
	 * instance was provided in the constructor, then that instance is returned.
	 * Otherwise, an instance of {@link SimpleUserAgentContext} is created and
	 * returned.
	 * <p>
	 * The context returned by this method is used by local request facilities
	 * and other parts of the renderer.
	 */
	public UserAgentContext getUserAgentContext() {

		synchronized (this) {
			if (this.bcontext == null) {
				this.warn("getUserAgentContext(): UserAgentContext not provided in constructor. Creating a simple one.");
				this.bcontext = new SimpleUserAgentContext();
			}
			return this.bcontext;
		}
	}

	/**
	 * Should be overridden to return true if the link has been visited.
	 */
	public boolean isVisitedLink(final HTMLLinkElement link) {

		return false;
	}

	/**
	 * This method must be overridden to implement a context menu.
	 */
	public boolean onContextMenu(final HTMLElement element, final MouseEvent event) {

		return true;
	}

	/**
	 * This method can be overridden to receive notifications when the mouse
	 * leaves an element.
	 */
	public void onMouseOut(final HTMLElement element, final MouseEvent event) {

	}

	/**
	 * This method can be overridden to receive notifications when the mouse
	 * first enters an element.
	 */
	public void onMouseOver(final HTMLElement element, final MouseEvent event) {

	}

	public boolean isImageLoadingEnabled() {

		return true;
	}

	public boolean onDoubleClick(final HTMLElement element, final MouseEvent event) {

		return true;
	}

	public boolean onMouseClick(final HTMLElement element, final MouseEvent event) {

		return true;
	}

	private static java.awt.Window getWindow(final Component c) {

		java.awt.Component current = c;
		while (current != null && !(current instanceof java.awt.Window)) {
			current = current.getParent();
		}
		return (java.awt.Window) current;
	}

	/*
	 * public void resizeBy(int byWidth, int byHeight) { java.awt.Window window
	 * = getWindow(this.htmlPanel); if(window != null) {
	 * window.setSize(window.getWidth() + byWidth, window.getHeight() +
	 * byHeight); } }
	 */

	/*
	 * public void resizeTo(int width, int height) { java.awt.Window window =
	 * getWindow(this.htmlPanel); if(window != null) { window.setSize(width,
	 * height); } }
	 */

	/**
	 * It should navigate back one page. This implementation does nothing and
	 * should be overridden.
	 */
	public void back() {

		/*
		 * if(logger.isLoggable(Level.WARNING)) { logger.log(Level.WARNING,
		 * "back() does nothing, unless overridden."); }
		 */
	}

	public void forward() {

		/*
		 * if(logger.isLoggable(Level.WARNING)) { logger.log(Level.WARNING,
		 * "forward() does nothing, unless overridden."); }
		 */
	}

	/*
	 * public String getCurrentURL() { Object node =
	 * this.htmlPanel.getRootNode(); if(node instanceof HTMLDocumentImpl) {
	 * HTMLDocumentImpl doc = (HTMLDocumentImpl) node; return
	 * doc.getDocumentURI(); } return null; }
	 */

	public int getHistoryLength() {

		return 0;
	}

	public String getNextURL() {

		return null;
	}

	public String getPreviousURL() {

		return null;
	}

	public void goToHistoryURL(final String url) {

		if (logger.isLoggable(Level.WARNING)) {
			logger.log(Level.WARNING, "goToHistoryURL() does nothing, unless overridden.");
		}
	}

	public void moveInHistory(final int offset) {

		if (logger.isLoggable(Level.WARNING)) {
			logger.log(Level.WARNING, "moveInHistory() does nothing, unless overridden.");
		}
	}

	@Override
	public String getCurrentURL() {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resizeBy(final int byWidth, final int byHeight) {

		// TODO Auto-generated method stub

	}

	@Override
	public void resizeTo(final int width, final int height) {

		// TODO Auto-generated method stub

	}

	@Override
	public void scroll(final int x, final int y) {

		// TODO Auto-generated method stub

	}

	@Override
	public void scrollBy(final int x, final int y) {

		// TODO Auto-generated method stub

	}
}
