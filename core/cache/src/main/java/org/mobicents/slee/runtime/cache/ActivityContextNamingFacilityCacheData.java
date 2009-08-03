package org.mobicents.slee.runtime.cache;

import java.util.HashMap;
import java.util.Map;

import javax.slee.facilities.NameAlreadyBoundException;
import javax.slee.facilities.NameNotBoundException;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

/**
 * 
 * Proxy object for ac naming facility data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ActivityContextNamingFacilityCacheData extends CacheData {

	/**
	 * root fqn
	 */
	private final static Fqn parentNodeFqn = Fqn.ROOT;
	/**
	 * the name of the cache node that holds all data
	 */
	public static final String CACHE_NODE_NAME = "aci-names";

	private static final Object CACHE_NODE_MAP_KEY = new Object();

	/**
	 * 
	 * @param txManager
	 */
	protected ActivityContextNamingFacilityCacheData(Cache jBossCache) {
		super(Fqn.fromRelativeElements(parentNodeFqn, CACHE_NODE_NAME),
				jBossCache);
	}

	/**
	 * Binds the specified aci name with the specified activity context handle
	 * @param ach
	 * @param name
	 * @throws NameAlreadyBoundException
	 */
	public void bindName(Object ach, String name)
			throws NameAlreadyBoundException {
		Node node = getNode();
		Fqn childNodeFqn = Fqn.fromElements(name);
		if (node.hasChild(childNodeFqn)) {
			throw new NameAlreadyBoundException("name already bound");
		} else {
			node.addChild(childNodeFqn).put(CACHE_NODE_MAP_KEY, ach);
		}
	}

	/**
	 * Unbinds the specified aci name with the specified activity context id
	 * @param name
	 * @return
	 * @throws NameNotBoundException
	 */
	public Object unbindName(String name) throws NameNotBoundException {
		Node node = getNode();
		Fqn childNodeFqn = Fqn.fromElements(name);
		Node childNode = node.getChild(childNodeFqn);
		if (childNode == null) {
			throw new NameNotBoundException("name not bound");
		} else {
			Object ach = childNode.get(CACHE_NODE_MAP_KEY);
			node.removeChild(childNodeFqn);
			return ach;
		}
	}

	/**
	 * Lookup of the activity context id bound to the specified aci name
	 * @param name
	 * @return
	 */
	public Object lookupName(String name) {
		Node childNode = getNode().getChild(Fqn.fromElements(name));
		if (childNode == null) {
			return null;
		} else {
			return childNode.get(CACHE_NODE_MAP_KEY);
		}
	}

	/**
	 * Retrieves a map of the bindings. Key is the aci name and Value is the activity context handle
	 * @return
	 */
	public Map getNameBindings() {

		Map result = new HashMap();
		Node childNode = null;
		Object key = null;
		for (Object obj : getNode().getChildren()) {
			childNode = (Node) obj;
			key = childNode.getFqn().getLastElement();
			result.put(key, childNode.get(key));
		}
		return result;
	}
}
