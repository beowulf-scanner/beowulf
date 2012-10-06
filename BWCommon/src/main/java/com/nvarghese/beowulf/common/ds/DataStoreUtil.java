package com.nvarghese.beowulf.common.ds;

import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.ServerAddress;

public class DataStoreUtil {

	/**
	 * 
	 * @param dbServerList
	 * @param dbName
	 * @return
	 */
	public static Datastore createOrGetDataStore(List<ServerAddress> dbServerList, String dbName) {

		Mongo mongo = new Mongo(dbServerList);
		Datastore ds = new Morphia().createDatastore(mongo, dbName);

		return ds;

	}

}
