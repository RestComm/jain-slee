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
	 * Binds the specified aci name with the specified activity context id
	 * @param acId
	 * @param name
	 * @throws NameAlreadyBoundException
	 */
	public void bindName(String acId, String name)
			throws NameAlreadyBoundException {
		Node node = getNode();
		Fqn childNodeFqn = Fqn.fromElements(name);
		if (node.hasChild(childNodeFqn)) {
			throw new NameAlreadyBoundException("name already bound");
		} else {
			node.addChild(childNodeFqn).put(CACHE_NODE_MAP_KEY, acId);
		}
	}

	/**
	 * Unbinds the specified aci name with the specified activity context id
	 * @param name
	 * @return
	 * @throws NameNotBoundException
	 */
	public String unbindName(String name) throws NameNotBoundException {
		Node node = getNode();
		Fqn childNodeFqn = Fqn.fromElements(name);
		Node childNode = node.getChild(childNodeFqn);
		if (childNode == null) {
			throw new NameNotBoundException("name not bound");
		} else {
			String acId = (String) childNode.get(CACHE_NODE_MAP_KEY);
			node.removeChild(childNodeFqn);
			return acId;
		}
	}

	/**
	 * Lookup of the activity context id bound to the specified aci name
	 * @param name
	 * @return
	 */
	public String lookupName(String name) {
		Node childNode = getNode().getChild(Fqn.fromElements(name));
		if (childNode == null) {
			return null;
		} else {
			return (String) childNode.get(CACHE_NODE_MAP_KEY);
		}
	}

	/**
	 * Retrieves a map of the bindings. Key is the aci name and Value is the activity context id
	 * @return
	 */
	public Map getNameBindings() {

		Map result = new HashMap();
		for (Object obj : getNode().getChildren()) {
			Node childNode = (Node) obj;
			Object key = childNode.getFqn().getLastElement();
			result.put(key, childNode.get(key));
		}
		return result;
	}
}
