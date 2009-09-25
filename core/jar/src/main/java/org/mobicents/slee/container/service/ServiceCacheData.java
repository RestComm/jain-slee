package org.mobicents.slee.container.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.slee.ServiceID;
import javax.slee.management.ServiceState;

import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.mobicents.cache.CacheData;
import org.mobicents.cache.MobicentsCache;

/**
 * 
 * Proxy object for service data management through JBoss Cache
 * 
 * @author martins
 * 
 */
public class ServiceCacheData extends CacheData {

	/**
	 * the node map key that points to the service state
	 */
	private static final String NODE_MAP_KEY_SERVICE_STATE = "state";

	/**
	 * the fqn of the child node that holds the child sbb entities
	 */
	private static String CHILD_OBJ_NAME = "childs";
	private static Fqn CHILD_OBJ_FQN = Fqn.fromElements(CHILD_OBJ_NAME);
	private Node _childsNode;
	private Node getChildsNode() {
		if (_childsNode == null) {
			_childsNode = getNode().getChild(CHILD_OBJ_NAME);
		}
		return _childsNode;
	}
	
	/**
	 * the node map key that points to the child sbb entity
	 */
	private static final String NODE_MAP_KEY_CHILD_SBB_ENTITY_ID = "sbbeid";

	/**
	 * the fqn of the node that holds all service cache child nodes
	 */
	private final static String parentNodeFqn = "service";

	/**
	 * 
	 * @param serviceID
	 */
	public ServiceCacheData(ServiceID serviceID, MobicentsCache cache) {
		super(Fqn.fromElements(parentNodeFqn, serviceID), cache);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.cache.CacheData#create()
	 */
	@Override
	public boolean create() {
		if (super.create()) {
			// on creation create also the child node to hold root sbb entities
			_childsNode = getNode().addChild(CHILD_OBJ_FQN);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Defines the service state.
	 * 
	 * @param state
	 */
	public void setState(ServiceState state) {
		getNode().put(NODE_MAP_KEY_SERVICE_STATE, state);
	}

	/**
	 * Retrieves the service state
	 * 
	 * @return
	 */
	public ServiceState getState() {
		return (ServiceState) getNode().get(NODE_MAP_KEY_SERVICE_STATE);
	}

	/**
	 * Adds a new child sbb entity with a specific convergence name.
	 * 
	 * @param convergenceName
	 * @param childSbbEntityId
	 */
	public void addChild(String convergenceName, String childSbbEntityId) {
		getChildsNode().addChild(Fqn.fromElements(convergenceName)).put(
				NODE_MAP_KEY_CHILD_SBB_ENTITY_ID, childSbbEntityId);
	}

	/**
	 * Verifies if there is a child sbb entity with the specified convergence
	 * name
	 * 
	 * @param convergenceName
	 * @return
	 */
	public boolean hasChild(String convergenceName) {
		return getChildsNode().hasChild(convergenceName);
	}

	/**
	 * Removes the child sbb entity with the specified convergence name
	 * 
	 * @param convergenceName
	 * @return true if a child was found and removed
	 */
	public boolean removeChild(String convergenceName) {
		return getChildsNode().removeChild(convergenceName);
	}

	/**
	 * Retrieves the set of sbb entity ids (not convergence names) that are
	 * childs of the service
	 * 
	 * @return
	 */
	public Set<String> getChildSbbEntities() {

		final Node childsNode = getChildsNode();
		if (!childsNode.isLeaf()) {
			Set result = new HashSet();
			Node childNode = null;
			for (Object obj : childsNode.getChildren()) {
				childNode = (Node) obj;
				result.add(childNode.get(NODE_MAP_KEY_CHILD_SBB_ENTITY_ID));
			}
			return result;
		} else {
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * Retrieves the sbb entity id of the child mapped with the specified
	 * convergence name
	 * 
	 * @param convergenceName
	 * @return
	 */
	public String getChild(String convergenceName) {
		Node childNode = getChildsNode().getChild(convergenceName);
		return childNode != null ? (String) childNode
				.get(NODE_MAP_KEY_CHILD_SBB_ENTITY_ID) : null;
	}

}
