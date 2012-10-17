package com.nvarghese.beowulf.scs.categorizers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.morphia.Datastore;
import com.nvarghese.beowulf.common.scan.model.WebScanDocument;
import com.nvarghese.beowulf.scs.ScsManager;

public class MetaCategorizer {

	private List<TransactionCategorizer> transactionCategorizers = new ArrayList<TransactionCategorizer>();
	private List<Categorizer> categorizers = new ArrayList<Categorizer>();

	static Logger logger = LoggerFactory.getLogger(MetaCategorizer.class);

	public void initCategorizers(Datastore ds, WebScanDocument webScanDocument) {

		Reflections reflections = ScsManager.getInstance().getReflections();
		Set<Class<? extends Categorizer>> subTypes = reflections.getSubTypesOf(Categorizer.class);
		for (Class c : subTypes) {
			if (c.getName().contains(".impl.")) {
				try {
					Constructor constructor = c.getConstructor(Datastore.class, WebScanDocument.class);

					Categorizer categorizer = (Categorizer) constructor.newInstance(ds, webScanDocument);
					categorizer.initialize();
					categorizers.add(categorizer);
					logger.debug("Added class `{}` to categorizer list", c.getName());
				} catch (InstantiationException e) {
					logger.error("Failed to initialize categorizer of type: {}", c.getName(), e);
				} catch (IllegalAccessException e) {
					logger.error("Failed to initialize categorizer of type: {}", c.getName(), e);
				} catch (SecurityException e) {
					logger.error("Failed to initialize categorizer of type: {}", c.getName(), e);
				} catch (NoSuchMethodException e) {
					logger.error("Failed to initialize categorizer of type: {}", c.getName(), e);
				} catch (IllegalArgumentException e) {
					logger.error("Failed to initialize categorizer of type: {}", c.getName(), e);
				} catch (InvocationTargetException e) {
					logger.error("Failed to initialize categorizer of type: {}", c.getName(), e);
				}
			} else {
				logger.debug("Excluded class `{}` from categorizer list", c.getName());
			}
		}
	}

	public void initTransactionCategorizers(Datastore ds, WebScanDocument webScanDocument) {

		Reflections reflections = ScsManager.getInstance().getReflections();
		Set<Class<? extends TransactionCategorizer>> subTypes = reflections.getSubTypesOf(TransactionCategorizer.class);
		for (Class c : subTypes) {
			if (c.getName().contains(".impl.")) {
				try {
					Constructor constructor = c.getConstructor(Datastore.class, WebScanDocument.class);
					TransactionCategorizer txnCategorizer = (TransactionCategorizer) constructor.newInstance(ds, webScanDocument);
					((Categorizer) txnCategorizer).initialize();

					transactionCategorizers.add(txnCategorizer);
					logger.info("Added class `{}` to TransactionCategorizer list", c.getName());
				} catch (InstantiationException e) {
					logger.error("Failed to initialize TransactionCategorizer of type: {}", c.getName(), e);
				} catch (IllegalAccessException e) {
					logger.error("Failed to initialize TransactionCategorizer of type: {}", c.getName(), e);
				} catch (SecurityException e) {
					logger.error("Failed to initialize TransactionCategorizer of type: {}", c.getName(), e);
				} catch (NoSuchMethodException e) {
					logger.error("Failed to initialize TransactionCategorizer of type: {}", c.getName(), e);
				} catch (IllegalArgumentException e) {
					logger.error("Failed to initialize TransactionCategorizer of type: {}", c.getName(), e);
				} catch (InvocationTargetException e) {
					logger.error("Failed to initialize TransactionCategorizer of type: {}", c.getName(), e);
				}
			} else {
				logger.info("Excluded class `{}` from TransactionCategorizer list", c.getName());
			}
		}

	}

	public void runAllCategorizers() {

	}

	public List<TransactionCategorizer> getTransactionCategorizers() {

		return transactionCategorizers;
	}

	public List<Categorizer> getCategorizers() {

		return categorizers;
	}

}
