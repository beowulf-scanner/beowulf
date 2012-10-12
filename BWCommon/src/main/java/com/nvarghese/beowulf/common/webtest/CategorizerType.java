package com.nvarghese.beowulf.common.webtest;

public enum CategorizerType {

		META("META"),
		MULTI_SET("MULTI_SET"),
		SINGLE_SET("SINGLE_SET"),
		MULTI_SET_TXN("MULTI_SET_TXN"),
		TOKEN_SINGLE_SET_TXN("TOKEN_SINGLE_SET_TXN"),
		NONE("NONE");

	String value;

	CategorizerType(String value) {

		this.value = value;

	}

	public String getValue() {

		return value;
	}

	public static CategorizerType getCategorizerType(String categorizerTypeValue) {

		for (CategorizerType type : CategorizerType.values()) {
			if (type.value.equalsIgnoreCase(categorizerTypeValue)) {
				return type;
			}
		}

		return NONE;

	}

}
