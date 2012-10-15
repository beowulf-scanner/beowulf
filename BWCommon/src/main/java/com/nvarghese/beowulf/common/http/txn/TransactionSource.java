package com.nvarghese.beowulf.common.http.txn;

public enum TransactionSource {

		SPIDER,
		ENUMERATION,
		TEST,
		BASE,
		COBRA,
		PROXY,
		AUTHENTICATION,
		NIKTO,
		CATEGORIZER,
		PAGE_NOT_FOUND_PROFILER,
		MANUAL_REQUEST,
		FUZZER,
		CUSTOM_CA_CHECK,
		IMPORT_SPIDERED,
		NONE;

}
