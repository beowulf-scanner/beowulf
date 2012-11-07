package com.nvarghese.beowulf.scs.categorizers.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;

@Entity("host_categorizer")
public class HostCategorizerDocument extends AbstractDocument {

	@Property("host_names")
	private List<String> hostNames;

	public HostCategorizerDocument() {

		super();
		hostNames = new ArrayList<String>();

	}

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

	public List<String> getHostNames() {

		return hostNames;
	}

	public void setHostNames(List<String> hostNames) {

		this.hostNames = hostNames;
	}

}
