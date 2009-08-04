package org.mobicents.slee.runtime.cache;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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

public class TimerTasksCacheData extends CacheData {

	/**
	 * the name of the cache node that holds all data
	 */
	private static final String CACHE_NODE_NAME = "timertasks";

	private static final Object CACHE_NODE_MAP_KEY = new Object();

	/**
	 * 
	 * @param txManager
	 */
	protected TimerTasksCacheData(Cache jBossCache) {
		super(Fqn.fromElements(CACHE_NODE_NAME),
				jBossCache);
	}

	public void addTaskData(Serializable taskID, Object taskData) {
		getNode().addChild(Fqn.fromElements(taskID)).put(CACHE_NODE_MAP_KEY,
				taskData);
	}

	public boolean removeTaskData(Serializable taskID) {
		return getNode().removeChild(taskID);
	}

	public Object getTaskData(Serializable taskID) {
		final Node childNode = getNode().getChild(taskID);
		if (childNode == null) {
			return null;
		} else {
			return childNode.get(CACHE_NODE_MAP_KEY);
		}
	}

	public Set getTaskDatas() {
		final Node node = getNode();
		if (!node.isLeaf()) {
			Set result = new HashSet();
			Node childNode = null;
			for (Object obj : node.getChildren()) {
				childNode = (Node) obj;
				// add the task, not the timer id
				result.add(childNode.get(CACHE_NODE_MAP_KEY));
			}
			return result;
		}
		else {
			return Collections.EMPTY_SET;
		}
	}
}
