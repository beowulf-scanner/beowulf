package com.nvarghese.beowulf.common.dao;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;

public class AbstractMongoDAO<T, K> extends BasicDAO<T, K> {

	public AbstractMongoDAO(Class<T> entityClass, Datastore ds) {

		super(entityClass, ds);

	}

}
