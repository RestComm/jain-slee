/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.runtime.activity;

import org.infinispan.tree.Fqn;
import org.infinispan.tree.Node;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.FqnWrapper;
import org.restcomm.cluster.MobicentsCluster;

import javax.slee.facilities.TimerID;
import java.util.*;

/**
 * 
 * Proxy object for activity context factory data management through JBoss Cache
 * 
 * @author martins
 * 
 */
@SuppressWarnings("rawtypes")
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

	private static final Fqn ATTACHED_SBBs_FQN = Fqn
			.fromElements(ATTACHED_SBBs_NODE_NAME);

	private static final Fqn ATTACHED_TIMERS_FQN = Fqn
			.fromElements(ATTACHED_TIMERS_NODE_NAME);

	private static final Fqn NAMES_BOUND_FQN = Fqn.fromElements(NAMES_BOUND_NODE_NAME);

	private static final Fqn CMP_ATTRIBUTES_FQN = Fqn
			.fromElements(CMP_ATTRIBUTES_NODE_NAME);
	
	private static final Fqn IS_ENDING_FQN = Fqn
	.fromElements(IS_ENDING_NODE_NAME);

	private static final Boolean CMP_ATTRIBUTES_NODE_MAP_KEY = Boolean.TRUE;

	private static final String STRING_ID_ATTRIBUTE_MAP_KEY = "sid";
	
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
	public ActivityContextCacheData(ActivityContextHandle activityContextHandle,
			MobicentsCluster cluster) {
		super(FqnWrapper.fromElementsWrapper(parentNodeFqn, activityContextHandle),
				cluster.getMobicentsCache());
	}

	/**
	 * Puts an object in cache data
	 * 
	 * @param key
	 * @param value
	 * @return the old object for the specified key, null if key was not mapped
	 */
	@SuppressWarnings("unchecked")
	public Object putObject(Object key, Object value) {
		return getNode().put(key, value);
	}

	/**
	 * Retrieves an object in cache data mapped to the specified key
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getObject(Object key) {
		return getNode().get(key);
	}

	/**
	 * Removes an object in cache data mapped to the specified key
	 * 
	 * @param key
	 * @return the object removed, null if the key was not mapped
	 */
	@SuppressWarnings("unchecked")
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
	
	/**
	 * Tries to attaches an sbb entity
	 * 
	 * @param sbbEntityId
	 * @return true if it was attached, false if already was attached
	 */
	public boolean attachSbbEntity(SbbEntityID sbbEntityId) {
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
	public boolean detachSbbEntity(SbbEntityID sbbEntityId) {
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
		return node != null ? node.getChildrenNames().isEmpty() : true;
	}

	/**
	 * Return a set with all sbb entities attached.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<SbbEntityID> getSbbEntitiesAttached() {
		final Node node  = getAttachedSbbsNode(false);
		return node != null ? node.getChildrenNames() : Collections.emptySet();		
	}

	/**
	 * Attaches a timer
	 * 
	 * @param timerID
	 */
	public boolean attachTimer(TimerID timerID) {
		final Node node = getAttachedTimersNode(true);
		if (!node.hasChild(timerID)) {
			node.addChild(Fqn.fromElements(timerID));
			return true;
		}
		else {
			return false;
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
		return node != null ? node.getChildrenNames().isEmpty() : true;
	}

	/**
	 * Returns the set of timers attached to the ac
	 * 
	 * @return
	 */
	public Set getAttachedTimers() {
		final Node node = getAttachedTimersNode(false);
		return node != null ? node.getChildrenNames() : Collections.emptySet();								
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
		return node != null ? node.getChildrenNames().isEmpty() : true;
	}

	/**
	 * Returns the set of names bound to the ac
	 * 
	 * @return
	 */
	public Set getNamesBoundCopy() {
		final Node node = getNamesBoundNode(false);
		return node != null ? node.getChildrenNames() : Collections.emptySet();
	}

	/**
	 * Sets the aci cmp attribute
	 * 
	 * @param attrName
	 * @param attrValue
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
	public Map getCmpAttributesCopy() {
		final Node node = getCmpAttributesNode(false);
		if(node == null) {
			return Collections.emptyMap();
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

	@SuppressWarnings("unchecked")
	public String getStringID() {
		return (String) getNode().get(STRING_ID_ATTRIBUTE_MAP_KEY);
	}

	@SuppressWarnings("unchecked")
	public void setStringID(String sid) {
		getNode().put(STRING_ID_ATTRIBUTE_MAP_KEY, sid);
	}

}