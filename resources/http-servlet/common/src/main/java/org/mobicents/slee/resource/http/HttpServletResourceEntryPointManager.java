package org.mobicents.slee.resource.http;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the active resource entry points. 
 * @author martins
 *
 */
public class HttpServletResourceEntryPointManager {

	/**
	 * 
	 */
	private static final ConcurrentHashMap<String, HttpServletResourceEntryPoint> map = new ConcurrentHashMap<String, HttpServletResourceEntryPoint>(1);
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static HttpServletResourceEntryPoint getResourceEntryPoint(String name) {
		return map.get(name);
	}
	
	/**
	 * 
	 * @param name
	 */
	public static void removeResourceEntryPoint(String name) {
		map.remove(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param resourceEntryPoint
	 */
	public static void putResourceEntryPoint(String name, HttpServletResourceEntryPoint resourceEntryPoint) {
		map.put(name,resourceEntryPoint);
	}
}
