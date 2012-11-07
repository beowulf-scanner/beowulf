package com.nvarghese.beowulf.sfe.webtest.library;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.nvarghese.beowulf.sfe.ConfigurationManager;

public class SqlInjectionLibrary {

	private static HashMap<String, Set<Pattern>> errorRegexs;
	private static Set<Pattern> generalErrorRegexs;
	private static boolean initialized = false;
	public final static String GENERAL = "unknown";
	public final static String MSSQL = "Microsoft SQL Server";
	public final static String MYSQL = "MySQL";
	public final static String POSTGRESQL = "PostgreSQL";
	public final static String ORACLE = "Oracle";
	public final static String MSACCESS = "Microsoft Access";
	public final static String DB2 = "IBM DB2";
	public final static String SYBASE = "Sybase";
	public final static String INFORMIX = "IBM Informix";

	static Logger logger = LoggerFactory.getLogger(SqlInjectionLibrary.class);

	private synchronized static void initializeLibrary() {

		if (!initialized) {
			
			errorRegexs = new HashMap<String, Set<Pattern>>(7);
			generalErrorRegexs = generatePatterns(ConfigurationManager.getScannerConfiguration()
					.getList("sql_injection.error_message_regexs.general"));
			errorRegexs.put(SqlInjectionLibrary.MSSQL,
					generatePatterns(ConfigurationManager.getScannerConfiguration().getList("sql_injection.error_message_regexs.mssql")));
			errorRegexs.put(SqlInjectionLibrary.MYSQL,
					generatePatterns(ConfigurationManager.getScannerConfiguration().getList("sql_injection.error_message_regexs.mysql")));
			errorRegexs.put(SqlInjectionLibrary.POSTGRESQL,
					generatePatterns(ConfigurationManager.getScannerConfiguration().getList("sql_injection.error_message_regexs.postgresql")));
			errorRegexs.put(SqlInjectionLibrary.ORACLE,
					generatePatterns(ConfigurationManager.getScannerConfiguration().getList("sql_injection.error_message_regexs.oracle")));
			errorRegexs.put(SqlInjectionLibrary.MSACCESS,
					generatePatterns(ConfigurationManager.getScannerConfiguration().getList("sql_injection.error_message_regexs.access")));
			errorRegexs.put(SqlInjectionLibrary.DB2,
					generatePatterns(ConfigurationManager.getScannerConfiguration().getList("sql_injection.error_message_regexs.db2")));
			errorRegexs.put(SqlInjectionLibrary.SYBASE,
					generatePatterns(ConfigurationManager.getScannerConfiguration().getList("sql_injection.error_message_regexs.sybase")));
			errorRegexs.put(SqlInjectionLibrary.INFORMIX,
					generatePatterns(ConfigurationManager.getScannerConfiguration().getList("sql_injection.error_message_regexs.informix")));
			initialized = true;

		}
	}

	private static Set<Pattern> generatePatterns(List<String> rawPatterns) {

		Set<Pattern> patterns = new HashSet<Pattern>();
		for (String stringPattern : rawPatterns) {
			try {
				Pattern pattern = Pattern.compile(stringPattern, Pattern.CASE_INSENSITIVE);
				patterns.add(pattern);
			} catch (Exception e) {
				logger.error("Problem loading a SQL error pattern: " + e.getMessage());
			}
		}
		return patterns;
	}

	/**
	 * Returns the database platform constant (e.g. SQLInjection.ORACLE), or a
	 * null if no error was found
	 * 
	 * @param text
	 * @return
	 */
	public static String findSQLErrorMessages(String text) {

		initializeLibrary();
		if (text == null || text.equals("")) {
			return null;
		}

		// eliminate white space
		text = text.replaceAll("[\\x00-\\x20]++", " ");

		for (String platform : errorRegexs.keySet()) {
			for (Pattern pattern : errorRegexs.get(platform)) {
				Matcher m = pattern.matcher(text);
				if (m.find()) {
					return platform;
				}
			}
		}

		// general needs to be tested _after_ all of the platforms are tested
		for (Pattern pattern : generalErrorRegexs) {
			Matcher m = pattern.matcher(text);
			if (m.find()) {
				return SqlInjectionLibrary.GENERAL;
			}
		}

		return null;
	}

}
