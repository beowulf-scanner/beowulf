package com.nvarghese.beowulf.common.webtest;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum WebTestType {

		@XmlEnumValue("DIRECTORY_TEST") DIRECTORY_TEST,
		@XmlEnumValue("FILE_TEST") FILE_TEST,
		@XmlEnumValue("HOST_TEST") HOST_TEST,
		@XmlEnumValue("HTML_ELEM_TEST") HTML_ELEM_TEST,
		@XmlEnumValue("HTTP_METHOD_TEST") HTTP_METHOD_TEST,
		@XmlEnumValue("HTTP_QUERY_PARAM_TEST") HTTP_QUERY_PARAM_TEST,
		@XmlEnumValue("HTTP_RESPONSE_CODE_TEST") HTTP_RESPONSE_CODE_TEST,
		@XmlEnumValue("HTTP_MIME_TEST") HTTP_MIME_TEST,
		@XmlEnumValue("OUTPUT_CONTEXT_TEST") OUTPUT_CONTEXT_TEST,
		@XmlEnumValue("REPEATABLE_OUTPUT_CONTEXT_TEST") REPEATABLE_OUTPUT_CONTEXT_TEST,
		@XmlEnumValue("HTTP_REQUEST_HEADER_TEST") HTTP_REQUEST_HEADER_TEST,
		@XmlEnumValue("HTTP_RESPONSE_HEADER_TEST") HTTP_RESPONSE_HEADER_TEST,
		@XmlEnumValue("HTTP_SET_COOKIE_HEADER_TEST") HTTP_SET_COOKIE_HEADER_TEST,
		@XmlEnumValue("") NONE;

}
