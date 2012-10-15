package com.nvarghese.beowulf.scs.categorizers.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;

@Entity("token_ss_txn_categorizer")
public class TokenSingleSetTransactionCategorizerDocument extends AbstractDocument {

	@Property("token_hashes")
	private List<String> tokenHashes;

	public TokenSingleSetTransactionCategorizerDocument() {

		super();
		tokenHashes = new ArrayList<String>();

	}

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

	public List<String> getTokenHashes() {

		return tokenHashes;
	}

	public void setTokenHashes(List<String> tokenHashes) {

		this.tokenHashes = tokenHashes;
	}

}
