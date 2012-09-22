/*
 * GNU LESSER GENERAL PUBLIC LICENSE Copyright (C) 2006 The Lobo Project
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * Contact info: lobochief@users.sourceforge.net
 */
/*
 * Created on Nov 12, 2005
 */
package com.nvarghese.beowulf.common.cobra.html.js;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.Document;
import org.w3c.dom.html2.HTMLCollection;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;

import com.nvarghese.beowulf.common.cobra.html.HtmlRendererContext;
import com.nvarghese.beowulf.common.cobra.html.UserAgentContext;
import com.nvarghese.beowulf.common.cobra.html.domimpl.HTMLDocumentImpl;
import com.nvarghese.beowulf.common.cobra.html.domimpl.HTMLIFrameElementImpl;
import com.nvarghese.beowulf.common.cobra.html.domimpl.HTMLImageElementImpl;
import com.nvarghese.beowulf.common.cobra.html.domimpl.HTMLOptionElementImpl;
import com.nvarghese.beowulf.common.cobra.html.domimpl.HTMLScriptElementImpl;
import com.nvarghese.beowulf.common.cobra.html.domimpl.HTMLSelectElementImpl;
import com.nvarghese.beowulf.common.cobra.js.AbstractScriptableDelegate;
import com.nvarghese.beowulf.common.cobra.js.JavaClassWrapper;
import com.nvarghese.beowulf.common.cobra.js.JavaClassWrapperFactory;
import com.nvarghese.beowulf.common.cobra.js.JavaInstantiator;
import com.nvarghese.beowulf.common.cobra.js.JavaObjectWrapper;
import com.nvarghese.beowulf.common.cobra.js.JavaScript;
import com.nvarghese.beowulf.common.cobra.util.ID;
import com.nvarghese.beowulf.common.http.renderer.CobraUserAgent;

public class Window extends AbstractScriptableDelegate implements AbstractView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 448621862027374546L;
	private static final Logger logger = Logger.getLogger(Window.class.getName());
	private static final Map CONTEXT_WINDOWS = new WeakHashMap();
	// private static final JavaClassWrapper IMAGE_WRAPPER =
	// JavaClassWrapperFactory.getInstance().getClassWrapper(Image.class);
	private static final JavaClassWrapper XMLHTTPREQUEST_WRAPPER = JavaClassWrapperFactory.getInstance()
			.getClassWrapper(XMLHttpRequest.class);

	private static int timerIdCounter = 0;

	private final transient HtmlRendererContext rcontext;
	private final UserAgentContext uaContext;

	private Navigator navigator;
	private Screen screen;
	private Location location;
	private Map taskMap;
	private volatile HTMLDocumentImpl document;

	static org.apache.log4j.Logger logger4J = org.apache.log4j.Logger.getLogger(Window.class);

	public Window(HtmlRendererContext rcontext, UserAgentContext uaContext) {

		// TODO: Probably need to create a new Window instance
		// for every document. Sharing of Window state between
		// different documents is not correct.
		this.rcontext = rcontext;
		this.uaContext = uaContext;

	}

	private static int generateTimerID() {

		synchronized (logger) {
			return timerIdCounter++;
		}
	}

	public HtmlRendererContext getHtmlRendererContext() {

		return this.rcontext;
	}

	public UserAgentContext getUserAgentContext() {

		return this.uaContext;
	}

	private void clearState() {

		Scriptable s = this.getWindowScope();
		if (s != null) {
			Object[] ids = s.getIds();
			for (int i = 0; i < ids.length; i++) {
				Object id = ids[i];
				if (id instanceof String) {
					s.delete((String) id);
				} else if (id instanceof Integer) {
					s.delete(((Integer) id).intValue());
				}
			}
		}
	}

	public void setDocument(HTMLDocumentImpl document) {

		Document prevDocument = this.document;
		if (prevDocument != document) {
			// Should clearing of the state be done
			// when window "unloads"?
			if (prevDocument != null) {
				// Only clearing when the previous document was not null
				// because state might have been set on the window before
				// the very first document is added.
				this.clearState();
			}
			this.initWindowScope(document);
			this.forgetAllTasks();
			Function onunload = this.onunload;
			if (onunload != null) {
				HTMLDocumentImpl oldDoc = (HTMLDocumentImpl) this.document;
				Executor.executeFunction(this.getWindowScope(), onunload, oldDoc.getDocumentURL(), this.uaContext);
				this.onunload = null;
			}
			this.document = document;
		}
	}

	public DocumentView getDocument() {

		return this.document;
	}

	public Document getDocumentNode() {

		return this.document;
	}

	private void putAndStartTask(Integer timeoutID, Timer timer, Object retained) {

		TaskWrapper oldTaskWrapper = null;
		synchronized (this) {
			Map taskMap = this.taskMap;
			if (taskMap == null) {
				taskMap = new HashMap(4);
				this.taskMap = taskMap;
			} else {
				oldTaskWrapper = (TaskWrapper) taskMap.get(timeoutID);
			}
			taskMap.put(timeoutID, new TaskWrapper(timer, retained));
		}
		// Do this outside synchronized block, just in case.
		if (oldTaskWrapper != null) {
			oldTaskWrapper.timer.stop();
		}
		timer.start();
	}

	private void forgetTask(Integer timeoutID, boolean cancel) {

		TaskWrapper oldTimer = null;
		synchronized (this) {
			Map taskMap = this.taskMap;
			if (taskMap != null) {
				oldTimer = (TaskWrapper) taskMap.remove(timeoutID);
			}
		}
		if (oldTimer != null && cancel) {
			oldTimer.timer.stop();
		}
	}

	private void forgetAllTasks() {

		TaskWrapper[] oldTaskWrappers = null;
		synchronized (this) {
			Map taskMap = this.taskMap;
			if (taskMap != null) {
				oldTaskWrappers = (TaskWrapper[]) taskMap.values().toArray(new TaskWrapper[0]);
				this.taskMap = null;
			}
		}
		if (oldTaskWrappers != null) {
			for (int i = 0; i < oldTaskWrappers.length; i++) {
				TaskWrapper taskWrapper = oldTaskWrappers[i];
				taskWrapper.timer.stop();
			}
		}
	}

	// private Timer getTask(Long timeoutID) {
	// synchronized(this) {
	// Map taskMap = this.taskMap;
	// if(taskMap != null) {
	// return (Timer) taskMap.get(timeoutID);
	// }
	// }
	// return null;
	// }

	/**
	 * Delay is set to zero to prevent problems with scanner (patched)
	 * 
	 * @param aFunction
	 *            Javascript function to invoke on each loop.
	 * @param aTimeInMs
	 *            Time in millisecund between each loop.
	 * @return Return the timer ID to use as reference
	 * @see <a
	 *      href="http://developer.mozilla.org/en/docs/DOM:window.setInterval">Window.setInterval
	 *      interface definition</a>
	 * @todo Make proper and refactore with
	 *       {@link Window#setTimeout(Function, double)}.
	 */
	public int setInterval(final Function aFunction, double aTimeInMs) {

		/*
		 * aTimeInMs = 0; //patched if (aTimeInMs > Integer.MAX_VALUE ||
		 * aTimeInMs < 0) { throw new IllegalArgumentException("Timeout value "
		 * + aTimeInMs + " is not supported."); } final int timeID =
		 * generateTimerID(); final Integer timeIDInt = new Integer(timeID);
		 * ActionListener task = new FunctionTimerTask(this, timeIDInt,
		 * aFunction, false); int t = (int) aTimeInMs; if(t < 1) { t = 1; }
		 * Timer timer = new Timer(t, task); timer.setRepeats(true); // The only
		 * difference with setTimeout this.putAndStartTask(timeIDInt, timer,
		 * aFunction); return timeID;
		 */
		return setTimeout(aFunction, aTimeInMs);
	}

	/**
	 * Delay is set to zero to prevent problems with scanner (patched)
	 * 
	 * @param aExpression
	 *            Javascript expression to invoke on each loop.
	 * @param aTimeInMs
	 *            Time in millisecund between each loop.
	 * @return Return the timer ID to use as reference
	 * @see <a
	 *      href="http://developer.mozilla.org/en/docs/DOM:window.setInterval">Window.setInterval
	 *      interface definition</a>
	 * @todo Make proper and refactore with
	 *       {@link Window#setTimeout(String, double)}.
	 */
	public int setInterval(final String aExpression, double aTimeInMs) {

		/*
		 * aTimeInMs = 0; //patched if (aTimeInMs > Integer.MAX_VALUE ||
		 * aTimeInMs < 0) { throw new IllegalArgumentException("Timeout value "
		 * + aTimeInMs + " is not supported."); } final int timeID =
		 * generateTimerID(); final Integer timeIDInt = new Integer(timeID);
		 * ActionListener task = new ExpressionTimerTask(this, timeIDInt,
		 * aExpression, false); int t = (int) aTimeInMs; if(t < 1) { t = 1; }
		 * Timer timer = new Timer(t, task); timer.setRepeats(true); // The only
		 * difference with setTimeout this.putAndStartTask(timeIDInt, timer,
		 * null); return timeID;
		 */
		return setTimeout(aExpression, aTimeInMs);
	}

	/**
	 * @param aTimerID
	 *            Timer ID to stop.
	 * @see <a
	 *      href="http://developer.mozilla.org/en/docs/DOM:window.clearInterval">Window.clearInterval
	 *      interface Definition</a>
	 */
	public void clearInterval(int aTimerID) {

		Integer key = new Integer(aTimerID);
		this.forgetTask(key, true);
	}

	/**
	 * Disabled to prevent problems in scanner
	 */
	public void alert(String message) {

		/*
		 * if(this.rcontext != null) { this.rcontext.alert(message); }
		 */
		logger.info("My Window alert" + message);
	}

	/**
	 * Disabled to prevent problems in scanner
	 */
	public void back() {

		/*
		 * HtmlRendererContext rcontext = this.rcontext; if(rcontext != null) {
		 * rcontext.back(); }
		 */
	}

	public void blur() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.blur();
		}
	}

	public void clearTimeout(int timeoutID) {

		Integer key = new Integer(timeoutID);
		this.forgetTask(key, true);
	}

	public void close() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.close();
		}
	}

	/**
	 * Disabled to prevent problems in scanner
	 * 
	 * @param message
	 * @return Always returns true
	 */
	public boolean confirm(String message) {

		/*
		 * HtmlRendererContext rcontext = this.rcontext; if(rcontext != null) {
		 * return rcontext.confirm(message); } else { return false; }
		 */
		return true;
	}

	public Object eval(String javascript) {

		HTMLDocumentImpl document = (HTMLDocumentImpl) this.document;
		if (document == null) {
			throw new IllegalStateException("Cannot evaluate if document is not set.");
		}
		Context ctx = Executor.createContext(document.getDocumentURL(), this.uaContext);
		try {
			Scriptable scope = this.getWindowScope();
			if (scope == null) {
				throw new IllegalStateException(
						"Scriptable (scope) instance was expected to be keyed as UserData to document using "
								+ Executor.SCOPE_KEY);
			}
			String scriptURI = "window.eval";
			if (logger.isLoggable(Level.INFO)) {
				logger.info("eval(): javascript follows...\r\n" + javascript);
			}
			return ctx.evaluateString(scope, javascript, scriptURI, 1, null);
		} finally {
			Context.exit();
		}
	}

	public void focus() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.focus();
		}
	}

	private void initWindowScope(final Document doc) {

		// Special Javascript class: XMLHttpRequest
		final Scriptable ws = this.getWindowScope();
		JavaInstantiator xi = new JavaInstantiator() {

			public Object newInstance() {

				Document d = doc;
				if (d == null) {
					throw new IllegalStateException("Cannot perform operation when document is unset.");
				}
				HTMLDocumentImpl hd;
				try {
					hd = (HTMLDocumentImpl) d;
				} catch (ClassCastException err) {
					throw new IllegalStateException("Cannot perform operation with documents of type "
							+ d.getClass().getName() + ".");
				}
				return new XMLHttpRequest(uaContext, hd.getDocumentURL(), ws);
			}
		};
		Function xmlHttpRequestC = JavaObjectWrapper.getConstructor("XMLHttpRequest", XMLHTTPREQUEST_WRAPPER, ws, xi);
		ScriptableObject.defineProperty(ws, "XMLHttpRequest", xmlHttpRequestC, ScriptableObject.READONLY);

		// HTML element classes
		this.defineElementClass(ws, doc, "Image", "img", HTMLImageElementImpl.class);
		this.defineElementClass(ws, doc, "Script", "script", HTMLScriptElementImpl.class);
		this.defineElementClass(ws, doc, "IFrame", "iframe", HTMLIFrameElementImpl.class);
		this.defineElementClass(ws, doc, "Option", "option", HTMLOptionElementImpl.class);
		this.defineElementClass(ws, doc, "Select", "select", HTMLSelectElementImpl.class);
	}

	private ScriptableObject windowScope;

	public Scriptable getWindowScope() {

		synchronized (this) {
			ScriptableObject windowScope = this.windowScope;
			if (windowScope != null) {
				return windowScope;
			}
			// Context.enter() OK in this particular case.
			Context ctx = Context.enter();
			try {
				// Window scope needs to be top-most scope.
				windowScope = (ScriptableObject) JavaScript.getInstance().getJavascriptObject(this, null);
				ctx.initStandardObjects(windowScope);
				this.windowScope = windowScope;
				return windowScope;
			} finally {
				Context.exit();
			}
		}
	}

	private final void defineElementClass(Scriptable scope, final Document document, final String jsClassName,
			final String elementName, Class javaClass) {

		JavaInstantiator ji = new JavaInstantiator() {

			public Object newInstance() {

				Document d = document;
				if (d == null) {
					throw new IllegalStateException("Document not set in current context.");
				}
				return d.createElement(elementName);
			}
		};
		JavaClassWrapper classWrapper = JavaClassWrapperFactory.getInstance().getClassWrapper(javaClass);
		Function constructorFunction = JavaObjectWrapper.getConstructor(jsClassName, classWrapper, scope, ji);
		ScriptableObject.defineProperty(scope, jsClassName, constructorFunction, ScriptableObject.READONLY);
	}

	public static Window getWindow(HtmlRendererContext rcontext) {

		if (rcontext == null) {
			return null;
		}
		synchronized (CONTEXT_WINDOWS) {
			Reference wref = (Reference) CONTEXT_WINDOWS.get(rcontext);
			if (wref != null) {
				Window window = (Window) wref.get();
				if (window != null) {
					return window;
				}
			}
			Window window = new Window(rcontext, rcontext.getUserAgentContext());
			CONTEXT_WINDOWS.put(rcontext, new WeakReference(window));
			return window;
		}
	}

	/**
	 * For now, disabled to prevent problems with scanner. May be reenabled if
	 * possible.
	 * 
	 * @param relativeUrl
	 * @param windowName
	 * @param windowFeatures
	 * @param replace
	 * @return Always returns null
	 */

	public Window open(String relativeUrl, String windowName, String windowFeatures, boolean replace) {

		// return null;

		/*
		 * HtmlRendererContext rcontext = this.rcontext; CobraUserAgent
		 * uaContext = (CobraUserAgent) this.uaContext; if(rcontext != null) {
		 * URI uri = null; Object document = this.document; if
		 * (URIStringUtils.isUsableUri(relativeUrl) || false) { try {
		 * if(document instanceof HTMLDocumentImpl) { uri =
		 * UriFactory.makeAbsoluteUri(relativeUrl, ((HTMLDocumentImpl)
		 * document).getBaseURI()); } } catch (URISyntaxException e) { // Don't
		 * really care }
		 * 
		 * if(uri!=null) {
		 * 
		 * try { HttpGetTransaction request = new
		 * HttpGetTransaction(uaContext.getScan(), uri, uaContext.getRefId(),
		 * TransactionSource.SPIDER);
		 * uaContext.getScan().getRequesterQueue().addSpiderRequest(request,
		 * false, "Javascript: Window.open "); } catch (Exception e) { // TODO:
		 * handle exception }
		 * 
		 * } } }
		 */
		/*
		 * HtmlRendererContext rcontext = this.rcontext; if (rcontext != null) {
		 * java.net.URL url; Object document = this.document; if (document
		 * instanceof HTMLDocumentImpl) { url = ((HTMLDocumentImpl)
		 * document).getFullURL(relativeUrl); } else { try { url = new
		 * java.net.URL(relativeUrl); } catch (java.net.MalformedURLException
		 * mfu) { throw new IllegalArgumentException("Malformed URI: " +
		 * relativeUrl); } } // SimpleHtmlRendererContext newContext = //
		 * (SimpleHtmlRendererContext) rcontext.open(url, windowName, //
		 * windowFeatures, replace); // return getWindow(newContext);
		 * 
		 * return null; } else { return null; }
		 */
		return null;
	}

	public Window open(String url) {

		return this.open(url, "window:" + String.valueOf(ID.generateLong()));
	}

	/**
	 * For now, disabled to prevent problems with scanner. May be reenabled if
	 * possible.
	 * 
	 * @param url
	 * @param windowName
	 * @return Always returns null
	 */
	public Window open(String url, String windowName) {

		return this.open(url, windowName, "", false);
		// return null;
	}

	/**
	 * For now, disabled to prevent problems with scanner. May be reenabled if
	 * possible.
	 * 
	 * @param url
	 * @param windowName
	 * @param windowFeatures
	 * @return Always returns null
	 */
	public Window open(String url, String windowName, String windowFeatures) {

		return this.open(url, windowName, windowFeatures, false);
		// return null;
	}

	/**
	 * Disabled to prevent problems with the scanner.
	 * 
	 * @param message
	 * @return Always returns an empty string
	 */
	public String prompt(String message) {

		return "";
		// return this.prompt(message, "");
	}

	/**
	 * Disabled to prevent problems with the scanner.
	 * 
	 * @param message
	 * @param inputDefault
	 * @return Always returns the default
	 */
	public String prompt(String message, int inputDefault) {

		// return this.prompt(message, String.valueOf(inputDefault));
		return String.valueOf(inputDefault);
	}

	/**
	 * Disabled to prevent problems with the scanner.
	 * 
	 * @param message
	 * @param inputDefault
	 * @returnAlways returns the default
	 */
	public String prompt(String message, String inputDefault) {

		return inputDefault;
		/*
		 * HtmlRendererContext rcontext = this.rcontext; if(rcontext != null) {
		 * return rcontext.prompt(message, inputDefault); } else { return null;
		 * }
		 */
	}

	public void scrollTo(int x, int y) {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.scroll(x, y);
		}
	}

	public void scrollBy(int x, int y) {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.scrollBy(x, y);
		}
	}

	public void resizeTo(int width, int height) {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.resizeTo(width, height);
		}
	}

	public void resizeBy(int byWidth, int byHeight) {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.resizeBy(byWidth, byHeight);
		}
	}

	/*
	 * Delay is always set to zero to prevent problems with the scanner
	 * 
	 * @param expr
	 * 
	 * @param millis
	 */
	public int setTimeout(final String expr, double millis) {

		CobraUserAgent cobraAgent = (CobraUserAgent) this.getUserAgentContext();
		if (!cobraAgent.isRenderingMode()) {
			return 0;
		}
		millis = 0; // patched here
		if (millis > Integer.MAX_VALUE || millis < 0) {
			throw new IllegalArgumentException("Timeout value " + millis + " is not supported.");
		}
		final int timeID = generateTimerID();
		final Integer timeIDInt = new Integer(timeID);
		// ActionListener task = new ExpressionTimerTask(this, timeIDInt, expr,
		// true);

		/*
		 * Parser fails to recognize a function object if it doesn't use (). For
		 * eg if showLink is a function which is defined in SCRIPT tags, then
		 * 
		 * settimeout(showLink(),3000) parses correctly to have showLink as a
		 * function object. But settimeout(showLink,3000) parses to have
		 * showLink as a string object and execution comes here with the whole
		 * function code as function showLink() {...}
		 */

		if (expr.trim().toLowerCase().startsWith("function")) {
			Function jFn = null;
			String sourceName = expr.substring("function".length() + 1, expr.indexOf("(")).trim();
			HTMLDocumentImpl doc = document;
			Context ctx = Executor.createContext(doc.getDocumentURL(), uaContext);
			try {
				Scriptable thisScope = (Scriptable) JavaScript.getInstance()
						.getJavascriptObject(getWindowScope(), null);
				try {
					// TODO: Get right line number for script. //TODO: Optimize
					// this in case it's called multiple times? Is that done?

					jFn = ctx.compileFunction(thisScope, expr.trim(), sourceName, 1, null);
				} catch (EcmaError ecmaError) {
					logger4J.error("Javascript error at " + ecmaError.getSourceName() + ":" + ecmaError.getLineNumber()
							+ ": " + ecmaError.getMessage(), ecmaError);

				} catch (Throwable err) {
					logger4J.error("Unable to evaluate Javascript code: " + err.getMessage(), err);

				}
			} finally {
				Context.exit();
			}

			if (jFn != null) {
				Executor.executeFunction(getWindowScope(), jFn, doc.getDocumentURL(), uaContext);
			}
			// return setTimeout(jFn,millis);

		}

		ActionListener task = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// This executes in the GUI thread and that's good.
				Window.this.forgetTask(timeIDInt, false);
				Window.this.eval(expr);
			}
		};

		int t = (int) millis;
		if (t < 1) {
			t = 1;
		}
		Timer timer = new Timer(t, task);
		timer.setRepeats(false);
		this.putAndStartTask(timeIDInt, timer, null);
		return timeID;
	}

	/*
	 * Delay is always set to zero to prevent problems with the scanner
	 * 
	 * @param function
	 * 
	 * @param millis
	 */
	public int setTimeout(final Function function, double millis) {

		CobraUserAgent cobraAgent = (CobraUserAgent) this.getUserAgentContext();
		if (!cobraAgent.isRenderingMode()) {
			return 0;
		}
		millis = 0; // patched here
		if (millis > Integer.MAX_VALUE || millis < 0) {
			throw new IllegalArgumentException("Timeout value " + millis + " is not supported.");
		}
		final int timeID = generateTimerID();
		final Integer timeIDInt = new Integer(timeID);
		// ActionListener task = new FunctionTimerTask(this, timeIDInt,
		// function, true);
		ActionListener task = new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				// This executes in the GUI thread and that's good.
				Window.this.forgetTask(timeIDInt, false);
				HTMLDocumentImpl doc = document;
				if (doc == null) {
					throw new IllegalStateException("Cannot perform operation when document is unset.");
				}
				Executor.executeFunction(getWindowScope(), function, doc.getDocumentURL(), uaContext);
			}
		};
		int t = (int) millis;
		if (t < 1) {
			t = 1;
		}
		Timer timer = new Timer(t, task);
		timer.setRepeats(false);
		this.putAndStartTask(timeIDInt, timer, function);
		return timeID;
	}

	public boolean isClosed() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			return rcontext.isClosed();
		} else {
			return false;
		}
	}

	public String getDefaultStatus() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			return rcontext.getDefaultStatus();
		} else {
			return null;
		}
	}

	public HTMLCollection getFrames() {

		return this.document.getFrames();
	}

	private int length;
	private boolean lengthSet = false;

	/**
	 * Gets the number of frames.
	 */
	public int getLength() {

		if (this.lengthSet) {
			return this.length;
		} else {
			HTMLCollection frames = this.getFrames();
			return frames == null ? 0 : frames.getLength();
		}
	}

	public void setLength(int length) {

		this.lengthSet = true;
		this.length = length;
	}

	public String getName() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			return rcontext.getName();
		} else {
			return null;
		}
	}

	public Window getParent() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			return Window.getWindow(rcontext.getParent());
		} else {
			return null;
		}
	}

	public Window getOpener() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			return Window.getWindow(rcontext.getOpener());
		} else {
			return null;
		}
	}

	public void setOpener(Window opener) {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			if (opener == null) {
				rcontext.setOpener(null);
			} else {
				rcontext.setOpener(opener.rcontext);
			}
		}
	}

	public Window getSelf() {

		return this;
	}

	public String getStatus() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			return rcontext.getStatus();
		} else {
			return null;
		}
	}

	public void setStatus(String message) {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.setStatus(message);
		}
	}

	public Window getTop() {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			return Window.getWindow(rcontext.getTop());
		} else {
			return null;
		}
	}

	public Window getWindow() {

		return this;
	}

	public Navigator getNavigator() {

		synchronized (this) {
			Navigator nav = this.navigator;
			if (nav == null) {
				nav = new Navigator(this.uaContext);
				this.navigator = nav;
			}
			return nav;
		}
	}

	public Screen getScreen() {

		synchronized (this) {
			Screen nav = this.screen;
			if (nav == null) {
				nav = new Screen();
				this.screen = nav;
			}
			return nav;
		}
	}

	public Location getLocation() {

		synchronized (this) {
			Location location = this.location;
			if (location == null) {
				location = new Location(this);
				this.location = location;
			}
			return location;
		}
	}

	/**
	 * 
	 * 
	 * @param location
	 */
	public void setLocation(String location) {

		this.getLocation().setHref(location);
		// logger.info("setLocation: Disabled method; location="+ location);
	}

	private History history;

	public History getHistory() {

		synchronized (this) {
			History history = this.history;
			if (history == null) {
				history = new History(this);
				this.history = history;
			}
			return history;
		}
	}

	/*
	 * public CSS2Properties getComputedStyle(HTMLElement element, String
	 * pseudoElement) { if(element instanceof HTMLElementImpl) { return
	 * ((HTMLElementImpl) element).getComputedStyle(pseudoElement); } else {
	 * throw new
	 * java.lang.IllegalArgumentException("Element implementation unknown: " +
	 * element); } }
	 */

	public Function getOnload() {
		
		return this.document.getOnloadHandler();
	}

	public void setOnload(Function onload) {

		// Note that body.onload overrides
		// window.onload.
		// Document doc = this.document;

		this.document.setOnloadHandler(onload);

	}

	private Function onunload;

	public Function getOnunload() {

		return onunload;
	}

	public void setOnunload(Function onunload) {

		this.onunload = onunload;
	}

	public org.w3c.dom.Node namedItem(String name) {

		// Bug 1928758: Element IDs are named objects in context.
		HTMLDocumentImpl doc = this.document;
		if (doc == null) {
			return null;
		}
		org.w3c.dom.Node node = doc.getElementById(name);
		if (node != null) {
			return node;
		}
		return null;
	}

	public void forceGC() {

		// System.gc();
	}

	private static abstract class WeakWindowTask implements ActionListener {

		private final WeakReference windowRef;

		public WeakWindowTask(Window window) {

			this.windowRef = new WeakReference(window);
		}

		protected Window getWindow() {

			WeakReference ref = this.windowRef;
			return ref == null ? null : (Window) ref.get();
		}
	}

	private static class FunctionTimerTask extends WeakWindowTask {

		// Implemented as a static WeakWindowTask to allow the Window
		// to get garbage collected, especially in infinite loop
		// scenarios.
		private final Integer timeIDInt;
		private final WeakReference functionRef;
		private final boolean removeTask;

		public FunctionTimerTask(Window window, Integer timeIDInt, Function function, boolean removeTask) {

			super(window);
			this.timeIDInt = timeIDInt;
			this.functionRef = new WeakReference(function);
			this.removeTask = removeTask;
		}

		public void actionPerformed(ActionEvent e) {

			// This executes in the GUI thread and that's good.
			try {
				Window window = this.getWindow();
				if (window == null) {
					if (logger.isLoggable(Level.INFO)) {
						logger.info("actionPerformed(): Window is no longer available.");
					}
					return;
				}
				if (this.removeTask) {
					window.forgetTask(this.timeIDInt, false);
				}
				HTMLDocumentImpl doc = (HTMLDocumentImpl) window.getDocument();
				if (doc == null) {
					throw new IllegalStateException("Cannot perform operation when document is unset.");
				}
				Function function = (Function) this.functionRef.get();
				if (function == null) {
					throw new IllegalStateException("Cannot perform operation. Function is no longer available.");
				}
				Executor.executeFunction(window.getWindowScope(), function, doc.getDocumentURL(), window
						.getUserAgentContext());
			} catch (Throwable err) {
				logger.log(Level.WARNING, "actionPerformed()", err);
			}
		}
	}

	private static class ExpressionTimerTask extends WeakWindowTask {

		// Implemented as a static WeakWindowTask to allow the Window
		// to get garbage collected, especially in infinite loop
		// scenarios.
		private final Integer timeIDInt;
		private final String expression;
		private final boolean removeTask;

		public ExpressionTimerTask(Window window, Integer timeIDInt, String expression, boolean removeTask) {

			super(window);
			this.timeIDInt = timeIDInt;
			this.expression = expression;
			this.removeTask = removeTask;
		}

		public void actionPerformed(ActionEvent e) {

			// This executes in the GUI thread and that's good.
			try {
				Window window = this.getWindow();
				if (window == null) {
					if (logger.isLoggable(Level.INFO)) {
						logger.info("actionPerformed(): Window is no longer available.");
					}
					return;
				}
				if (this.removeTask) {
					window.forgetTask(this.timeIDInt, false);
				}
				HTMLDocumentImpl doc = (HTMLDocumentImpl) window.getDocument();
				if (doc == null) {
					throw new IllegalStateException("Cannot perform operation when document is unset.");
				}
				window.eval(this.expression);
			} catch (Throwable err) {
				logger.log(Level.WARNING, "actionPerformed()", err);
			}
		}
	}

	private static class TaskWrapper {

		public final Timer timer;
		private final Object retained;

		public TaskWrapper(Timer timer, Object retained) {

			super();
			this.timer = timer;
			this.retained = retained;
		}
	}

	/**
	 * Used for testing for XSS using a function name that won't be blocked by
	 * stupid filters
	 * 
	 * @param token
	 */
	public void testXSS(String token) {

		document.setXssToken(token);
	}

	@Deprecated
	public void scroll(int x, int y) {

		HtmlRendererContext rcontext = this.rcontext;
		if (rcontext != null) {
			rcontext.scroll(x, y);
		}
	}

}
