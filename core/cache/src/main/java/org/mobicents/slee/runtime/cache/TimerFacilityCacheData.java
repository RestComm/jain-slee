package org.mobicents.slee.runtime.cache;

import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import javax.slee.facilities.TimerID;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

/**
 * 
 * Proxy object for timer facility entity data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class TimerFacilityCacheData extends CacheData {

	/**
	 * root fqn
	 */
	private final static Fqn parentNodeFqn = Fqn.ROOT;
	/**
	 * the name of the cache node that holds all data
	 */
	private static final String CACHE_NODE_NAME = "timer-facility";

	private static final Object CACHE_NODE_MAP_KEY = new Object();

	/**
	 * 
	 * @param txManager
	 */
	protected TimerFacilityCacheData(Cache jBossCache) {
		super(Fqn.fromRelativeElements(parentNodeFqn, CACHE_NODE_NAME),
				jBossCache);
	}

	public void addTask(TimerID timerID, TimerTask task) {
		getNode().addChild(Fqn.fromElements(timerID)).put(CACHE_NODE_MAP_KEY,
				task);
	}

	public boolean removeTask(TimerID timerID) {
		return getNode().removeChild(Fqn.fromElements(timerID));
	}

	public Object getTask(TimerID timerID) {
		Node childNode = getNode().getChild(Fqn.fromElements(timerID));
		if (childNode == null) {
			return null;
		} else {
			return childNode.get(CACHE_NODE_MAP_KEY);
		}
	}

	public Set getTasks() {
		Set result = new HashSet();
		for (Object obj : getNode().getChildren()) {
			Node childNode = (Node) obj;
			// add the task, not the timer id
			result.add(childNode.get(CACHE_NODE_MAP_KEY));
		}
		return result;
	}
}
