package com.nvarghese.beowulf.common.cobra.util;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class Items {

	private Items() {

	}

	private static Map sourceMap = new WeakHashMap();

	public static Object getItem(final Object source, final String name) {

		Map sm = sourceMap;
		synchronized (sm) {
			Map itemMap = (Map) sm.get(source);
			if (itemMap == null) {
				return null;
			}
			return itemMap.get(name);
		}
	}

	public static void setItem(final Object source, final String name, final Object value) {

		Map sm = sourceMap;
		synchronized (sm) {
			Map itemMap = (Map) sm.get(source);
			if (itemMap == null) {
				itemMap = new HashMap(1);
				sm.put(source, itemMap);
			}
			itemMap.put(name, value);
		}
	}
}
