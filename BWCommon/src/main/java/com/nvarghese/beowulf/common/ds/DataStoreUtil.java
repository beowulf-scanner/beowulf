package com.nvarghese.beowulf.common.ds;

import java.net.UnknownHostException;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;

public class DataStoreUtil {

	/**
	 * 
	 * @param dbServerList
	 * @param dbName
	 * @return
	 * @throws UnknownHostException
	 */
	public static Datastore createOrGetDataStore(String mongoDbConnectString, String dbName) throws UnknownHostException {

		MongoURI mongoURI = new MongoURI(mongoDbConnectString);
		Mongo mongo = Mongo.Holder.singleton().connect(mongoURI);
		Datastore ds = new Morphia().createDatastore(mongo, dbName);

		return ds;

	}

}
