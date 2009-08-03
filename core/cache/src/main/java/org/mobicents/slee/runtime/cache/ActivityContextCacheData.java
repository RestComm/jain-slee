package org.mobicents.slee.runtime.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
	public final static Fqn parentNodeFqn = Fqn.fromElements("ac");

	// --- child cache nodes naming

	private static final Fqn ATTACHED_SBBs_FQN = Fqn
			.fromElements("attached-sbbs");

	private static final Fqn ATTACHED_TIMERS_FQN = Fqn
			.fromElements("attached-timers");

	private static final Fqn NAMES_BOUND_FQN = Fqn.fromElements("names-bound");

	private static final Fqn CMP_ATTRIBUTES_FQN = Fqn
			.fromElements("cmp-attributes");
	
	private static final Fqn IS_ENDING_FQN = Fqn
	.fromElements("is-ending");

	private static final Fqn IS_CHECKING_REFS_FQN = Fqn
	.fromElements("is-checking-refs");
	
	private static final Object CMP_ATTRIBUTES_NODE_MAP_KEY = new Object();

	/**
	 * 
	 * @param activityContextHandle
	 */
	protected ActivityContextCacheData(Object activityContextHandle,
			Cache jBossCache) {
		super(Fqn.fromRelativeElements(parentNodeFqn, activityContextHandle),
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
		return getNode().hasChild(IS_ENDING_FQN);
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
				getNode().removeChild(IS_ENDING_FQN);
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public boolean isCheckingReferences() {
		return getNode().hasChild(IS_CHECKING_REFS_FQN);
	}
	
	public boolean setCheckingReferences(boolean value) {
		if (value) {
			if (!isEnding()) {
				getNode().addChild(IS_CHECKING_REFS_FQN);
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (isEnding()) {
				getNode().removeChild(IS_CHECKING_REFS_FQN);
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
		Fqn fqn = Fqn.fromRelativeElements(ATTACHED_SBBs_FQN, sbbEntityId);
		Node node = getNode();
		if (!node.hasChild(fqn)) {
			node.addChild(fqn);
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
		Node childNode = getNode().getChild(ATTACHED_SBBs_FQN);
		if (childNode != null) {
			return childNode.removeChild(Fqn.fromElements(sbbEntityId));
		}
		else {
			return false;
		}
	}

	/**
	 * Verifies if there at least one sbb entity attached
	 * 
	 * @return false is there are no sbb entities attached, true otherwise
	 */
	public boolean noSbbEntitiesAttached() {
		Node childNode = getNode().getChild(ATTACHED_SBBs_FQN);
		if (childNode == null || childNode.getChildrenNames().size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Return a set with all sbb entities attached.
	 * 
	 * @return
	 */
	public Set getSbbEntitiesAttached() {
		Set result = null;
		Node childNode = getNode().getChild(ATTACHED_SBBs_FQN);
		if (childNode == null) {
			result = Collections.EMPTY_SET;
		} else {
			result = childNode.getChildrenNames();
		}
		return result;
	}

	/**
	 * Attaches a timer
	 * 
	 * @param timerID
	 */
	public void attachTimer(TimerID timerID) {
		getNode().addChild(
				Fqn.fromRelativeElements(ATTACHED_TIMERS_FQN, timerID));
	}

	/**
	 * Detaches a timer
	 * 
	 * @param timerID
	 */
	public boolean detachTimer(TimerID timerID) {
		Node childNode = getNode().getChild(ATTACHED_TIMERS_FQN);
		if (childNode == null) {
			return false;
		} else {
			return childNode.removeChild(Fqn.fromElements(timerID));
		}
	}

	/**
	 * Verifies if there at least one timer attached
	 * 
	 * @return false is there are no timers attached, true otherwise
	 */
	public boolean noTimersAttached() {
		Node childNode = getNode().getChild(ATTACHED_TIMERS_FQN);
		if (childNode == null || childNode.getChildrenNames().size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the set of timers attached to the ac
	 * 
	 * @return
	 */
	public Set getAttachedTimersCopy() {
		Set result = null;
		Node childNode = getNode().getChild(ATTACHED_TIMERS_FQN);
		if (childNode == null) {
			result = Collections.EMPTY_SET;
		} else {
			result = new HashSet(childNode.getChildrenNames());			
		}
		return result;
	}

	/**
	 * Adds the specified name to the set of names bound to the ac
	 * 
	 * @param name
	 */
	public void nameBound(String name) {
		getNode().addChild(Fqn.fromRelativeElements(NAMES_BOUND_FQN, name));
	}

	/**
	 * Removes the specified name from the set of names bound to the ac
	 * 
	 * @param name
	 */
	public boolean nameUnbound(String name) {
		Node childNode = getNode().getChild(NAMES_BOUND_FQN);
		if (childNode == null) {
			return false;
		} else {
			return childNode.removeChild(Fqn.fromElements(name));
		}
	}

	/**
	 * Verifies if there at least one name bound to the ac
	 * 
	 * @return false is there are no names bound, true otherwise
	 */
	public boolean noNamesBound() {
		Node childNode = getNode().getChild(NAMES_BOUND_FQN);
		if (childNode == null || childNode.getChildren().size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the set of names bound to the ac
	 * 
	 * @return
	 */
	public Set getNamesBoundCopy() {
		Set result = null;
		Node childNode = getNode().getChild(NAMES_BOUND_FQN);
		if (childNode == null) {
			result = Collections.EMPTY_SET;
		} else {
			result = new HashSet(childNode.getChildrenNames());
		}
		return result;
	}

	/**
	 * Sets the aci cmp attribute
	 * 
	 * @param attrName
	 * @param attrValue
	 */
	public void setCmpAttribute(String attrName, Object attrValue) {
		Node node = getNode();
		Fqn cmpNodeFqn = Fqn.fromRelativeElements(CMP_ATTRIBUTES_FQN, attrName);
		Node cmpNode = node.getChild(cmpNodeFqn);
		if (cmpNode == null) {
			cmpNode = node.addChild(cmpNodeFqn);
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
		Object result = null;
		Fqn cmpNodeFqn = Fqn.fromRelativeElements(CMP_ATTRIBUTES_FQN, attrName);
		Node cmpNode = getNode().getChild(cmpNodeFqn);
		if (cmpNode != null) {
			result = cmpNode.get(CMP_ATTRIBUTES_NODE_MAP_KEY);
		}
		return result;
	}

	/**
	 * Retrieves a map copy of the aci attributes set
	 * 
	 * @return
	 */
	public Map getCmpAttributesCopy() {
		Map result = null;
		Node cmpsNode = getNode().getChild(CMP_ATTRIBUTES_FQN);
		if (cmpsNode == null) {
			result = Collections.EMPTY_MAP;
		} else {
			result = new HashMap();
			Node cmpNode = null;
			for (Object obj : cmpsNode.getChildren()) {
				cmpNode = (Node) obj;
				result.put(cmpNode.getFqn().getLastElement(), cmpNode
						.get(CMP_ATTRIBUTES_NODE_MAP_KEY));
			}
		}
		return result;
	}

}