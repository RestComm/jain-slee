package org.mobicents.slee.resource.http;

import java.util.concurrent.ConcurrentHashMap;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

public class RequestLock {

	private ConcurrentHashMap<HttpServletRequestEvent, Object> map = new ConcurrentHashMap();

	public Object getLock(HttpServletRequestEvent event) {
		Object obj = null;
		obj = map.get(event);
		if (obj == null) {
			obj = createLock(event);
		}
		return obj;
	}

	private synchronized Object createLock(HttpServletRequestEvent event) {
		Object tempObj = map.get(event);
		if (tempObj == null) {
			tempObj = new Object();
			map.put(event, tempObj);
		}
		return tempObj;
	}

	public void removeLock(HttpServletRequestEvent event) {
		map.remove(event);
	}

}
