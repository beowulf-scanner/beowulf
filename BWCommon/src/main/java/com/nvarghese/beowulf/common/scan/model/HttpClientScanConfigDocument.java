package com.nvarghese.beowulf.common.scan.model;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Property;

@Embedded
public class HttpClientScanConfigDocument {

	@Property("max_redirects")
	private long maxRedirects;

	@Property("max_consecutive_failed_requests")
	private long maxConsecutiveFailedRequests;

	@Property("max_failed_requests_per_server")
	private long maxFailedRequestsPerServer;

	@Property("max_request_count")
	private long maxRequestCount;

	@Property("max_request_depth")
	private long maxRequestDepth;

	@Property("max_request_retries")
	private long maxRequestRetries;

	@Property("max_spider_urls")
	private long maxSpriderUrls;

	@Property("socket_read_timeout")
	private long socketReadTimeout;

	@Property("user_agent_string")
	private String userAgentValue;

	@Property("character_encoding")
	private String characterEncoding;

	public long getMaxRedirects() {

		return maxRedirects;
	}

	public void setMaxRedirects(long maxRedirects) {

		this.maxRedirects = maxRedirects;
	}

	public long getMaxConsecutiveFailedRequests() {

		return maxConsecutiveFailedRequests;
	}

	public void setMaxConsecutiveFailedRequests(long maxConsecutiveFailedRequests) {

		this.maxConsecutiveFailedRequests = maxConsecutiveFailedRequests;
	}

	public long getMaxFailedRequestsPerServer() {

		return maxFailedRequestsPerServer;
	}

	public void setMaxFailedRequestsPerServer(long maxFailedRequestsPerServer) {

		this.maxFailedRequestsPerServer = maxFailedRequestsPerServer;
	}

	public long getMaxRequestCount() {

		return maxRequestCount;
	}

	public void setMaxRequestCount(long maxRequestCount) {

		this.maxRequestCount = maxRequestCount;
	}

	public long getMaxRequestDepth() {

		return maxRequestDepth;
	}

	public void setMaxRequestDepth(long maxRequestDepth) {

		this.maxRequestDepth = maxRequestDepth;
	}

	public long getMaxRequestRetries() {

		return maxRequestRetries;
	}

	public void setMaxRequestRetries(long maxRequestRetries) {

		this.maxRequestRetries = maxRequestRetries;
	}

	public long getMaxSpriderUrls() {

		return maxSpriderUrls;
	}

	public void setMaxSpriderUrls(long maxSpriderUrls) {

		this.maxSpriderUrls = maxSpriderUrls;
	}

	public long getSocketReadTimeout() {

		return socketReadTimeout;
	}

	public void setSocketReadTimeout(long socketReadTimeout) {

		this.socketReadTimeout = socketReadTimeout;
	}

	public String getUserAgentValue() {

		return userAgentValue;
	}

	public void setUserAgentValue(String userAgentValue) {

		this.userAgentValue = userAgentValue;
	}

	public String getCharacterEncoding() {

		return characterEncoding;
	}

	public void setCharacterEncoding(String characterEncoding) {

		this.characterEncoding = characterEncoding;
	}
}
