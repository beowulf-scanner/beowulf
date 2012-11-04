package com.nvarghese.beowulf.sfc.services;

import java.net.UnknownHostException;

import org.apache.commons.configuration.ConfigurationException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.BeowulfCommonConfigManager;
import com.nvarghese.beowulf.common.ds.DataStoreUtil;
import com.nvarghese.beowulf.common.scan.dao.WebScanDAO;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.sfc.SFControllerManager;

public class ScanMonitorService {

	static Logger logger = LoggerFactory.getLogger(ScanMonitorService.class);

	public boolean isScanRunning(ObjectId webScanObjectId) throws UnknownHostException, ConfigurationException {

		boolean running = true;

		WebScanDAO webScanDAO = new WebScanDAO(SFControllerManager.getInstance().getDataStore());
		WebScanDocument webScanDocument = webScanDAO.getWebScanDocument(webScanObjectId);

		Datastore ds = DataStoreUtil.createOrGetDataStore(BeowulfCommonConfigManager.getDbUri(), webScanDocument.getTxnDbName());

		return running;

	}
	
	
	

}
