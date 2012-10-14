package com.nvarghese.beowulf.scs.categorizers.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Property;
import com.nvarghese.beowulf.common.model.AbstractDocument;

@Entity("dir_categorizer")
public class DirectoryCategorizerDocument extends AbstractDocument {

	@Property("tested_dirs")
	private List<String> testedDirs;

	public DirectoryCategorizerDocument() {

		super();
		testedDirs = new ArrayList<String>();

	}

	@PrePersist
	void prePersist() {

		setLastUpdated(new Date());

	}

	public List<String> getTestedDirs() {

		return testedDirs;
	}

	public void setTestedDirs(List<String> testedDirs) {

		this.testedDirs = testedDirs;
	}

}
