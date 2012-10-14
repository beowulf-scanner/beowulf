package com.nvarghese.beowulf.common.http.txn;

import java.util.List;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.annotations.Serialized;
import com.nvarghese.beowulf.common.http.payload.SerializedRequestPayload;
import com.nvarghese.beowulf.common.model.AbstractDocument;

@Entity("http_txns")
public class HttpTxnDocument extends AbstractDocument {

	@Property("txn_source")
	private TransactionSource transactionSource;

	@Property("request_method")
	private HttpMethodType requestMethod;

	@Property("request_uri")
	private String requestURI;

	@Serialized("request_headers")
	private List<BasicHeader> requestHeaders;

	@Serialized("request_payload")
	private SerializedRequestPayload requestPayload;

	@Serialized("cookie_store")
	private BasicCookieStore cookieStore;

	@Property("referer_txn_objid")
	private ObjectId refererTxnObjId;

	@Property("referer")
	private String referer;

	@Property("payload_changed")
	private boolean payloadChanged;

	@Property("response_ready")
	private boolean responseReady;

	@Serialized("response_statusline")
	private BasicStatusLine responseStatusLine;

	@Serialized("response_headers")
	private List<BasicHeader> responseHeaders;

	@Property("response_uncompressed")
	private boolean uncompressed;

	@Serialized("response_body")
	private byte[] responseBody;

	public TransactionSource getTransactionSource() {

		return transactionSource;
	}

	public void setTransactionSource(TransactionSource transactionSource) {

		this.transactionSource = transactionSource;
	}

	public HttpMethodType getRequestMethod() {

		return requestMethod;
	}

	public void setRequestMethod(HttpMethodType requestMethod) {

		this.requestMethod = requestMethod;
	}

	public String getRequestURI() {

		return requestURI;
	}

	public void setRequestURI(String requestURI) {

		this.requestURI = requestURI;
	}

	public List<BasicHeader> getRequestHeaders() {

		return requestHeaders;
	}

	public void setRequestHeaders(List<BasicHeader> requestHeaders) {

		this.requestHeaders = requestHeaders;
	}

	public String getReferer() {

		return referer;
	}

	public void setReferer(String referer) {

		this.referer = referer;
	}

	public SerializedRequestPayload getRequestPayload() {

		return requestPayload;
	}

	public void setRequestPayload(SerializedRequestPayload requestPayload) {

		this.requestPayload = requestPayload;
	}

	public BasicCookieStore getCookieStore() {

		return cookieStore;
	}

	public void setCookieStore(BasicCookieStore cookieStore) {

		this.cookieStore = cookieStore;
	}

	public ObjectId getRefererTxnObjId() {

		return refererTxnObjId;
	}

	public void setRefererTxnObjId(ObjectId refererTxnObjId) {

		this.refererTxnObjId = refererTxnObjId;
	}

	public boolean isPayloadChanged() {

		return payloadChanged;
	}

	public void setPayloadChanged(boolean payloadChanged) {

		this.payloadChanged = payloadChanged;
	}

	public boolean isUncompressed() {

		return uncompressed;
	}

	public void setUncompressed(boolean uncompressed) {

		this.uncompressed = uncompressed;
	}

	public boolean isResponseReady() {

		return responseReady;
	}

	public void setResponseReady(boolean responseReady) {

		this.responseReady = responseReady;
	}

	public BasicStatusLine getResponseStatusLine() {

		return responseStatusLine;
	}

	public void setResponseStatusLine(BasicStatusLine responseStatusLine) {

		this.responseStatusLine = responseStatusLine;
	}

	public List<BasicHeader> getResponseHeaders() {

		return responseHeaders;
	}

	public void setResponseHeaders(List<BasicHeader> responseHeaders) {

		this.responseHeaders = responseHeaders;
	}

	public byte[] getResponseBody() {

		return responseBody;
	}

	public void setResponseBody(byte[] responseBody) {

		this.responseBody = responseBody;
	}

}
