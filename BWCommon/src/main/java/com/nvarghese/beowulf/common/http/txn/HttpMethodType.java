package com.nvarghese.beowulf.common.http.txn;

/**
 * 
 * @author nibin
 * 
 */
public enum HttpMethodType {

	TRACE("TRACE"), GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE"), HEAD("HEAD"), OPTIONS("OPTIONS"), NONE("");

	String value;

	HttpMethodType(String value) {

		this.value = value;
	}

	public String getValue() {

		return value;
	}

	public static HttpMethodType getHttpMethodType(String method) {

		for (HttpMethodType type : HttpMethodType.values()) {
			if (type.value.equalsIgnoreCase(method)) {
				return type;
			}
		}
		return NONE;
	}

}
