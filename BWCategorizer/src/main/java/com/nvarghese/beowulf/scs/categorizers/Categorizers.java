//package com.nvarghese.beowulf.scs.categorizers;
//
//import java.util.ArrayList;
//
//import com.ivizsecurity.verimo.scanner.categorizers.impl.AllTransactionsCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByAuthenticationPackageCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByDirectoryCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByFileCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByHostCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByHtmlElementCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByHttpMethodCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByHttpQueryCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByHttpQueryParameterCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByHttpResponseCodeCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByMimeTypeCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByOutputContextCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByRepeatableOutputContextCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.ByResponseHeaderCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.BySetCookieCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.FinalAnalysisCategorizer;
//import com.ivizsecurity.verimo.scanner.categorizers.impl.InitialAuthenticationCategorizer;
//import com.ivizsecurity.verimo.scanner.scan.Scan;
//
//public class Categorizers {
//
//	private ArrayList<Categorizer> allCategorizers;
//	private Scan scan;
//	private ArrayList<TransactionCategorizer> transactionCategorizers;
//
//	private AllTransactionsCategorizer allTransactionsCategorizerCategorizer;
//	private ByAuthenticationPackageCategorizer byAuthenticationPackageCategorizerCategorizer;
//	private ByHtmlElementCategorizer byHtmlElementCategorizerCategorizer;
//	private ByHttpResponseCodeCategorizer byHttpResponseCodeCategorizerCategorizer;
//	private ByMimeTypeCategorizer byMimeTypeCategorizerCategorizer;
//	private ByResponseHeaderCategorizer byResponseHeaderCategorizerCategorizer;
//	private ByHostCategorizer byHostCategorizerCategorizer;
//	private ByHttpQueryCategorizer byHttpQueryCategorizer;
//	private ByHttpQueryParameterCategorizer byHttpQueryParameterCategorizer;
//	private ByFileCategorizer byFileCategorizer;
//	private ByHttpMethodCategorizer byHttpMethodCategorizer;
//	private ByDirectoryCategorizer byDirectoryCategorizer;
//	private InitialAuthenticationCategorizer initialAuthenticationCategorizer;
//	private FinalAnalysisCategorizer finalAnalysisCategorizer;
//	private ByOutputContextCategorizer byOutputContextCategorizer;
//	private ByRepeatableOutputContextCategorizer byRepeatableOutputContextCategorizer;
//	private BySetCookieCategorizer bySetCookieCategorizer;
//
//	public Categorizers(Scan scan) {
//
//		this.scan = scan;
//		initCategorizers();
//	}
//
//	public ByOutputContextCategorizer getByOutputContextCategorizer() {
//
//		return byOutputContextCategorizer;
//	}
//
//	public ArrayList<Categorizer> getAllCategorizers() {
//
//		return allCategorizers;
//	}
//
//	public AllTransactionsCategorizer getAllTransactionsCategorizerCategorizer() {
//
//		return allTransactionsCategorizerCategorizer;
//	}
//
//	public ByAuthenticationPackageCategorizer getByAuthenticationPackageCategorizerCategorizer() {
//
//		return byAuthenticationPackageCategorizerCategorizer;
//	}
//
//	public ByHtmlElementCategorizer getByHtmlElementCategorizerCategorizer() {
//
//		return byHtmlElementCategorizerCategorizer;
//	}
//
//	public ByHttpResponseCodeCategorizer getByHttpResponseCodeCategorizerCategorizer() {
//
//		return byHttpResponseCodeCategorizerCategorizer;
//	}
//
//	public ByMimeTypeCategorizer getByMimeTypeCategorizerCategorizer() {
//
//		return byMimeTypeCategorizerCategorizer;
//	}
//
//	public ByResponseHeaderCategorizer getByResponseHeaderCategorizerCategorizer() {
//
//		return byResponseHeaderCategorizerCategorizer;
//	}
//
//	public ByHostCategorizer getByHostCategorizerCategorizer() {
//
//		return byHostCategorizerCategorizer;
//	}
//
//	public ArrayList<TransactionCategorizer> getTransactionCategorizers() {
//
//		return transactionCategorizers;
//	}
//
//	private void initCategorizers() {
//
//		// Change the starting element count in the HashSet
//		// to match the number of categorizers being
//		// initialized
//		transactionCategorizers = new ArrayList<TransactionCategorizer>(8);
//		allCategorizers = new ArrayList<Categorizer>(10);
//
//		// Repeat for all categorizers used during the main
//		// testing period. By host should always be first.
//
//		byHostCategorizerCategorizer = new ByHostCategorizer(scan);
//		//byHostCategorizerCategorizer.setScan(scan);
//		transactionCategorizers.add(byHostCategorizerCategorizer);
//		allCategorizers.add(byHostCategorizerCategorizer);
//
//		byFileCategorizer = new ByFileCategorizer(scan);
//		//byFileCategorizer.setScan(scan);
//		transactionCategorizers.add(byFileCategorizer);
//		allCategorizers.add(byFileCategorizer);
//
//		byDirectoryCategorizer = new ByDirectoryCategorizer(scan);
//		//byDirectoryCategorizer.setScan(scan);
//		transactionCategorizers.add(byDirectoryCategorizer);
//		allCategorizers.add(byDirectoryCategorizer);
//
//		allTransactionsCategorizerCategorizer = new AllTransactionsCategorizer(scan);
//		//allTransactionsCategorizerCategorizer.setScan(scan);
//		transactionCategorizers.add(allTransactionsCategorizerCategorizer);
//		allCategorizers.add(allTransactionsCategorizerCategorizer);
//
//		byHtmlElementCategorizerCategorizer = new ByHtmlElementCategorizer(scan);
//		//byHtmlElementCategorizerCategorizer.setScan(scan);
//		transactionCategorizers.add(byHtmlElementCategorizerCategorizer);
//		allCategorizers.add(byHtmlElementCategorizerCategorizer);
//
//		byHttpResponseCodeCategorizerCategorizer = new ByHttpResponseCodeCategorizer(scan);
//		//byHttpResponseCodeCategorizerCategorizer.setScan(scan);
//		transactionCategorizers.add(byHttpResponseCodeCategorizerCategorizer);
//		allCategorizers.add(byHttpResponseCodeCategorizerCategorizer);
//
//		byMimeTypeCategorizerCategorizer = new ByMimeTypeCategorizer(scan);
//		//byMimeTypeCategorizerCategorizer.setScan(scan);
//		transactionCategorizers.add(byMimeTypeCategorizerCategorizer);
//		allCategorizers.add(byMimeTypeCategorizerCategorizer);
//
//		byResponseHeaderCategorizerCategorizer = new ByResponseHeaderCategorizer(scan);
//		//byResponseHeaderCategorizerCategorizer.setScan(scan);
//		transactionCategorizers.add(byResponseHeaderCategorizerCategorizer);
//		allCategorizers.add(byResponseHeaderCategorizerCategorizer);
//
//		byHttpQueryCategorizer = new ByHttpQueryCategorizer(scan);
//		//byHttpQueryCategorizer.setScan(scan);
//		transactionCategorizers.add(byHttpQueryCategorizer);
//		allCategorizers.add(byHttpQueryCategorizer);
//
//		byHttpQueryParameterCategorizer = new ByHttpQueryParameterCategorizer(scan);
//		//byHttpQueryParameterCategorizer.setScan(scan);
//		allCategorizers.add(byHttpQueryParameterCategorizer);
//		transactionCategorizers.add(byHttpQueryParameterCategorizer);
//
//		byOutputContextCategorizer = new ByOutputContextCategorizer(scan);
//		//byOutputContextCategorizer.setScan(scan);
//		transactionCategorizers.add(byOutputContextCategorizer);
//		allCategorizers.add(byOutputContextCategorizer);
//
//		byHttpMethodCategorizer = new ByHttpMethodCategorizer(scan);
//		//byHttpMethodCategorizer.setScan(scan);
//		transactionCategorizers.add(byHttpMethodCategorizer);
//		allCategorizers.add(byHttpMethodCategorizer);
//
//		bySetCookieCategorizer = new BySetCookieCategorizer(scan);
//		//bySetCookieCategorizer.setScan(scan);
//		transactionCategorizers.add(bySetCookieCategorizer);
//		allCategorizers.add(bySetCookieCategorizer);
//
//		// Technically, this is a transaction categorizer, but it isn't called
//		// for all transactions
//		initialAuthenticationCategorizer = new InitialAuthenticationCategorizer(scan);
//		//initialAuthenticationCategorizer.setScan(scan);
//		allCategorizers.add(initialAuthenticationCategorizer);
//
//		// Non transaction categorizers go below here
//		byAuthenticationPackageCategorizerCategorizer = new ByAuthenticationPackageCategorizer(scan);
//		//byAuthenticationPackageCategorizerCategorizer.setScan(scan);
//		allCategorizers.add(byAuthenticationPackageCategorizerCategorizer);
//
//		finalAnalysisCategorizer = new FinalAnalysisCategorizer(scan);
//		//finalAnalysisCategorizer.setScan(scan);
//		allCategorizers.add(finalAnalysisCategorizer);
//
//		byRepeatableOutputContextCategorizer = new ByRepeatableOutputContextCategorizer(scan);
//		//byRepeatableOutputContextCategorizer.setScan(scan);
//		allCategorizers.add(byRepeatableOutputContextCategorizer);
//
//	}
//
//	public ByHttpQueryCategorizer getByHttpQueryCategorizer() {
//
//		return byHttpQueryCategorizer;
//	}
//
//	public ByFileCategorizer getByFileCategorizer() {
//
//		return byFileCategorizer;
//	}
//
//	public ByDirectoryCategorizer getByDirectoryCategorizer() {
//
//		return byDirectoryCategorizer;
//	}
//
//	public InitialAuthenticationCategorizer getInitialAuthenticationCategorizer() {
//
//		return initialAuthenticationCategorizer;
//	}
//
//	public FinalAnalysisCategorizer getFinalAnalysisCategorizer() {
//
//		return finalAnalysisCategorizer;
//	}
//
//	public ByRepeatableOutputContextCategorizer getByRepeatableOutputContextCategorizer() {
//
//		return byRepeatableOutputContextCategorizer;
//	}
//
//	public ByHttpQueryParameterCategorizer getByHttpQueryParameterCategorizer() {
//
//		return byHttpQueryParameterCategorizer;
//	}
//
//	public ByHttpMethodCategorizer getByHttpMethodCategorizer() {
//
//		return byHttpMethodCategorizer;
//	}
//
//	public BySetCookieCategorizer getBySetCookieCategorizer() {
//
//		return bySetCookieCategorizer;
//	}
//
//}
