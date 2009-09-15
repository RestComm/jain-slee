package org.mobicents.slee.runtime.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.slee.facilities.TimerID;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

/**
 * 
 * Proxy object for activity context factory data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ActivityContextCacheData extends CacheData {

	/**
	 * the fqn of the node that holds all activity context cache child nodes
	 */
	public final static String parentNodeFqn = "ac";

	// --- child cache nodes naming

	private static final String ATTACHED_SBBs_NODE_NAME = "attached-sbbs";

	private static final String ATTACHED_TIMERS_NODE_NAME = "attached-timers";

	private static final String NAMES_BOUND_NODE_NAME = "names-bound";

	private static final String CMP_ATTRIBUTES_NODE_NAME = "cmp-attributes";

	private static final String IS_ENDING_NODE_NAME = "is-ending";

	private static final String IS_CHECKING_REFS_NODE_NAME = "is-checking-refs";


	private static final Fqn ATTACHED_SBBs_FQN = Fqn
			.fromElements(ATTACHED_SBBs_NODE_NAME);

	private static final Fqn ATTACHED_TIMERS_FQN = Fqn
			.fromElements(ATTACHED_TIMERS_NODE_NAME);

	private static final Fqn NAMES_BOUND_FQN = Fqn.fromElements(NAMES_BOUND_NODE_NAME);

	private static final Fqn CMP_ATTRIBUTES_FQN = Fqn
			.fromElements(CMP_ATTRIBUTES_NODE_NAME);
	
	private static final Fqn IS_ENDING_FQN = Fqn
	.fromElements(IS_ENDING_NODE_NAME);

	private static final Fqn IS_CHECKING_REFS_FQN = Fqn
	.fromElements(IS_CHECKING_REFS_NODE_NAME);
	
	private static final Object CMP_ATTRIBUTES_NODE_MAP_KEY = new Object();

	private Node _attachedSbbsNode;
	
	private Node getAttachedSbbsNode(boolean createIfNotExists) {
		if (_attachedSbbsNode == null) {
			final Node node = getNode();
			_attachedSbbsNode = node.getChild(ATTACHED_SBBs_NODE_NAME);
			if (_attachedSbbsNode == null && createIfNotExists) {
				_attachedSbbsNode = node.addChild(ATTACHED_SBBs_FQN);
			}
		}
		return _attachedSbbsNode;
	}
	
	private Node _attachedTimersNode;
	
	private Node getAttachedTimersNode(boolean createIfNotExists) {
		if (_attachedTimersNode == null) {
			final Node node = getNode();
			_attachedTimersNode = node.getChild(ATTACHED_TIMERS_NODE_NAME);
			if (_attachedTimersNode == null && createIfNotExists) {
				_attachedTimersNode = node.addChild(ATTACHED_TIMERS_FQN);
			}
		}
		return _attachedTimersNode;
	}
	
	private Node _namesBoundNode;
	
	private Node getNamesBoundNode(boolean createIfNotExists) {
		if (_namesBoundNode == null) {
			final Node node = getNode();
			_namesBoundNode = node.getChild(NAMES_BOUND_NODE_NAME);
			if (_namesBoundNode == null && createIfNotExists) {
				_namesBoundNode = node.addChild(NAMES_BOUND_FQN);
			}
		}
		return _namesBoundNode;
	}
	
	private Node _cmpAttributesNode;
	
	private Node getCmpAttributesNode(boolean createIfNotExists) {
		if (_cmpAttributesNode == null) {
			final Node node = getNode();
			_cmpAttributesNode = node.getChild(CMP_ATTRIBUTES_NODE_NAME);
			if (_cmpAttributesNode == null && createIfNotExists) {
				_cmpAttributesNode = node.addChild(CMP_ATTRIBUTES_FQN);
			}
		}
		return _cmpAttributesNode;
	}
	
	/**
	 * 
	 * @param activityContextHandle
	 */
	protected ActivityContextCacheData(Object activityContextHandle,
			Cache jBossCache) {
		super(Fqn.fromElements(parentNodeFqn, activityContextHandle),
				jBossCache);
	}

	/**
	 * Puts an object in cache data
	 * 
	 * @param key
	 * @param value
	 * @return the old object for the specified key, null if key was not mapped
	 */
	public Object putObject(Object key, Object value) {
		return getNode().put(key, value);
	}

	/**
	 * Retrieves an object in cache data mapped to the specified key
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(Object key) {
		return getNode().get(key);
	}

	/**
	 * Removes an object in cache data mapped to the specified key
	 * 
	 * @param key
	 * @return the object removed, null if the key was not mapped
	 */
	public Object removeObject(Object key) {
		return getNode().remove(key);
	}

	public boolean isEnding() {
		return getNode().hasChild(IS_ENDING_NODE_NAME);
	}
	
	
	public boolean setEnding(boolean value) {
		if (value) {
			if (!isEnding()) {
				getNode().addChild(IS_ENDING_FQN);
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (isEnding()) {
				getNode().removeChild(IS_ENDING_NODE_NAME);
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public boolean isCheckingReferences() {
		return getNode().hasChild(IS_CHECKING_REFS_NODE_NAME);
	}
	
	public boolean setCheckingReferences(boolean value) {
		if (value) {
			Node node = getNode();
			if (!node.hasChild(IS_CHECKING_REFS_NODE_NAME)) {
				node.addChild(IS_CHECKING_REFS_FQN);
				return true;
			}
			else {
				return false;
			}
		}
		else {
			Node node = getNode();
			if (node.hasChild(IS_CHECKING_REFS_NODE_NAME)) {
				node.removeChild(IS_CHECKING_REFS_NODE_NAME);
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * Tries to attaches an sbb entity
	 * 
	 * @param sbbEntityId
	 * @return true if it was attached, false if already was attached
	 */
	public boolean attachSbbEntity(String sbbEntityId) {
		final Node node = getAttachedSbbsNode(true);
		if (!node.hasChild(sbbEntityId)) {
			node.addChild(Fqn.fromElements(sbbEntityId));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Detaches an sbb entity
	 * 
	 * @param sbbEntityId
	 */
	public boolean detachSbbEntity(String sbbEntityId) {
		final Node node  = getAttachedSbbsNode(false);
		return node != null ? node.removeChild(sbbEntityId) : false;		
	}

	/**
	 * Verifies if there at least one sbb entity attached
	 * 
	 * @return false is there are no sbb entities attached, true otherwise
	 */
	public boolean noSbbEntitiesAttached() {
		final Node node  = getAttachedSbbsNode(false);
		return node != null ? node.isLeaf() : true;		
	}

	/**
	 * Return a set with all sbb entities attached.
	 * 
	 * @return
	 */
	public Set getSbbEntitiesAttached() {
		final Node node  = getAttachedSbbsNode(false);
		return node != null ? node.getChildrenNames() : Collections.EMPTY_SET;		
	}

	/**
	 * Attaches a timer
	 * 
	 * @param timerID
	 */
	public void attachTimer(TimerID timerID) {
		final Node node = getAttachedTimersNode(true);
		if (!node.hasChild(timerID)) {
			node.addChild(Fqn.fromElements(timerID));
		}
	}

	/**
	 * Detaches a timer
	 * 
	 * @param timerID
	 */
	public boolean detachTimer(TimerID timerID) {
		final Node node = getAttachedTimersNode(false);
		return node != null ? node.removeChild(timerID) : false;			
	}

	/**
	 * Verifies if there at least one timer attached
	 * 
	 * @return false is there are no timers attached, true otherwise
	 */
	public boolean noTimersAttached() {
		final Node node = getAttachedTimersNode(false);
		return node != null ? node.isLeaf() : true;		
	}

	/**
	 * Returns the set of timers attached to the ac
	 * 
	 * @return
	 */
	public Set getAttachedTimers() {
		final Node node = getAttachedTimersNode(false);
		return node != null ? node.getChildrenNames() : Collections.EMPTY_SET;								
	}

	/**
	 * Adds the specified name to the set of names bound to the ac
	 * 
	 * @param name
	 */
	public void nameBound(String name) {
		final Node node = getNamesBoundNode(true);
		if (!node.hasChild(name)) {
			node.addChild(Fqn.fromElements(name));
		}
	}

	/**
	 * Removes the specified name from the set of names bound to the ac
	 * 
	 * @param name
	 */
	public boolean nameUnbound(String name) {
		final Node node = getNamesBoundNode(false);
		return node != null ? node.removeChild(name) : false;
	}

	/**
	 * Verifies if there at least one name bound to the ac
	 * 
	 * @return false is there are no names bound, true otherwise
	 */
	public boolean noNamesBound() {
		final Node node = getNamesBoundNode(false);
		return node != null ? node.isLeaf() : true;
	}

	/**
	 * Returns the set of names bound to the ac
	 * 
	 * @return
	 */
	public Set getNamesBoundCopy() {
		final Node node = getNamesBoundNode(false);
		return node != null ? node.getChildrenNames() : Collections.EMPTY_SET;
	}

	/**
	 * Sets the aci cmp attribute
	 * 
	 * @param attrName
	 * @param attrValue
	 */
	public void setCmpAttribute(String attrName, Object attrValue) {
		final Node node = getCmpAttributesNode(true);
		Node cmpNode = node.getChild(attrName);
		if (cmpNode == null) {
			cmpNode = node.addChild(Fqn.fromElements(attrName));
		}
		cmpNode.put(CMP_ATTRIBUTES_NODE_MAP_KEY, attrValue);
	}

	/**
	 * Retrieves the aci cmp attribute
	 * 
	 * @param attrName
	 * @return
	 */
	public Object getCmpAttribute(String attrName) {
		final Node node = getCmpAttributesNode(false);
		if(node == null) {
			return null;
		}
		else {
			final Node cmpNode = node.getChild(attrName);
			if (cmpNode != null) {
				return cmpNode.get(CMP_ATTRIBUTES_NODE_MAP_KEY);
			}
			else {
				return null;
			}		
		}
	}

	/**
	 * Retrieves a map copy of the aci attributes set
	 * 
	 * @return
	 */
	public Map getCmpAttributesCopy() {
		final Node node = getCmpAttributesNode(false);
		if(node == null) {
			return Collections.EMPTY_MAP;
		}
		else {
			Map result = new HashMap();
			Node cmpNode = null;
			for (Object obj : node.getChildren()) {
				cmpNode = (Node) obj;
				result.put(cmpNode.getFqn().getLastElement(), cmpNode
						.get(CMP_ATTRIBUTES_NODE_MAP_KEY));
			}
			return result;
		}
	}

}