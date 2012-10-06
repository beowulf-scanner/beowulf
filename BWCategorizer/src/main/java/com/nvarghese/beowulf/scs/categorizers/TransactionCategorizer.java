package com.nvarghese.beowulf.scs.categorizers;

import com.nvarghese.beowulf.common.http.txn.AbstractHttpTransaction;

/**
 * 
 *  
 */
public interface TransactionCategorizer {

	public void analyzeTransaction(AbstractHttpTransaction transaction);
}
