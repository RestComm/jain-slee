package org.mobicents.slee.runtime.cache;

import java.util.Map;

public class XACacheTestViewer {

	public static Map getXACacheMap() {
		Map actualMaps = XACache.getInstance().getActualMaps();
		Map m =(Map) actualMaps.get("runtimeCache-services:ConvergenceNameTestService-jain.slee.tck-1.0#rootSbbEntities");
		if (m!= null) {
			System.out.println("\nXACache ConvergenceNameTestService rootSbbEntities keys: "+m.keySet());
		}
		return actualMaps;
	}
}
