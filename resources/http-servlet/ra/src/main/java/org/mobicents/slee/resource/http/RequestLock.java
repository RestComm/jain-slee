package org.mobicents.slee.resource.http;

import java.util.concurrent.ConcurrentHashMap;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

public class RequestLock {

	private ConcurrentHashMap<HttpServletRequestEvent, Object> map = new ConcurrentHashMap<HttpServletRequestEvent, Object>();

	public Object getLock(HttpServletRequestEvent event) {
		Object obj = map.get(event);
		if (obj == null) {
			obj = createLock(event);
		}
		return obj;
	}

	private Object createLock(HttpServletRequestEvent event) {
		Object object = new Object();
		Object anotherObject = map.putIfAbsent(event, object);
		if (anotherObject == null) {
			return object;
		}
		else {
			return anotherObject;
		}
	}

	public Object removeLock(HttpServletRequestEvent event) {
		return map.remove(event);
	}

}
