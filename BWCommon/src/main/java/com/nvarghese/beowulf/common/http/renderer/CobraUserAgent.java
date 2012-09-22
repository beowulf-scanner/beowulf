package com.nvarghese.beowulf.common.http.renderer;

import java.net.URL;
import java.security.Policy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.nvarghese.beowulf.common.cobra.html.HttpRequest;
import com.nvarghese.beowulf.common.cobra.html.UserAgentContext;

/**
 * This is used by HttpTransaction and should not be used by itself unless you
 * really know what you are doing. It may go away in the future.
 * 
 *  
 */
public class CobraUserAgent implements UserAgentContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private boolean enableScripting;
	/*
	 * renderingMode tracks when the document rendering should be called off.
	 * This value can be tracked from Cobra as well as from Grendel core
	 * 
	 * This helps in terminating some of the rendering that goes dependent on
	 * event-dispatching thread.
	 */
	private boolean renderingMode;
	
	/* Purpose is to track ajax request state */
	private transient List<HttpRequest> requests;

	public CobraUserAgent(boolean enableScripting, String refId) {

		this.enableScripting = enableScripting;
		this.id = refId;
		renderingMode = true;
		requests = Collections.synchronizedList(new ArrayList<HttpRequest>());
	}

	public HttpRequest createHttpRequest() {

		HttpRequest request = new CobraHttpRequest(this);
		synchronized(requests) {
			requests.add(request);
		}
		return request;
	}
	
	public boolean removeHttpRequest(HttpRequest request) {
		
		boolean removed = false;		
		
		synchronized (requests) {
			removed = requests.remove(request); 
		}
		
		return removed;
	}

	public String getAppCodeName() {

		return "huh?";
	}

	public String getAppMinorVersion() {

		return "1";
	}

	public String getAppName() {

		return "This is Beowulf Cobra renderer";
	}

	public String getAppVersion() {

		return "0";
	}

	public String getBrowserLanguage() {

		return "English";
	}

	public String getCookie(URL arg0) {

		// TODO Auto-generated method stub
		return null;
	}

	public String getPlatform() {

		// TODO Auto-generated method stub
		return null;
	}

	public int getScriptingOptimizationLevel() {

		// TODO Auto-generated method stub
		return 0;
	}

	public Policy getSecurityPolicy() {

		// TODO Auto-generated method stub
		return null;
	}

	public String getUserAgent() {

		return "beowulf";
	}

	public boolean isCookieEnabled() {

		// TODO Auto-generated method stub
		return false;
	}

	public boolean isScriptingEnabled() {

		// TODO Auto-generated method stub
		return enableScripting;
	}

	public void setCookie(URL arg0, String arg1) {

		// TODO Auto-generated method stub

	}

	@Override
	public String getProduct() {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVendor() {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExternalCSSEnabled() {

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMedia(String mediaName) {

		// TODO Auto-generated method stub
		return false;
	}

	public String getId() {

		return id;
	}

	public void setRenderingMode(boolean renderingMode) {

		this.renderingMode = renderingMode;
	}

	/**
	 * This method looks whether the rendering mode is disabled or enabled
	 * 
	 * @return
	 */
	public boolean isRenderingMode() {

		return this.renderingMode;
	}
	
	/**
	 * Inspects the active http requests and look for their state
	 * @return
	 */
	public boolean isRendering() {
		
		List<HttpRequest> reqs = new ArrayList<HttpRequest>();
		
		synchronized (requests) {
			reqs.addAll(requests);
		}
		
		for(HttpRequest r: reqs) {
			if(!(r.getReadyState() == HttpRequest.STATE_UNINITIALIZED) && 
					!(r.getReadyState() == HttpRequest.STATE_COMPLETE)) {
				return true;
			}	
		}
		return false;
	}
}
