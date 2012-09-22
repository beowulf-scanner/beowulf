package com.nvarghese.beowulf.common.cobra.html.domimpl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.html2.HTMLMetaElement;

public class HTMLMetaElementImpl extends HTMLElementImpl implements HTMLMetaElement {

	private static final Logger logger = Logger.getLogger(HTMLMetaElementImpl.class.getName());
	private static final boolean loggableInfo = logger.isLoggable(Level.INFO);

	public HTMLMetaElementImpl(String name, boolean noStyleSheet) {

		super(name, noStyleSheet);

	}

	@Override
	public String getContent() {

		return this.getAttribute("content");
	}

	@Override
	public String getHttpEquiv() {

		return this.getAttribute("http-equiv");
	}

	@Override
	public String getName() {

		return this.getAttribute("name");
	}

	@Override
	public String getScheme() {

		return this.getAttribute("scheme");
	}

	@Override
	public void setContent(String content) {

		this.setAttribute("content", content);

	}

	@Override
	public void setHttpEquiv(String httpEquiv) {

		this.setAttribute("http-equiv", httpEquiv);

	}

	@Override
	public void setName(String name) {

		this.setAttribute("name", name);

	}

	@Override
	public void setScheme(String scheme) {

		this.setAttribute("scheme", scheme);

	}

}
