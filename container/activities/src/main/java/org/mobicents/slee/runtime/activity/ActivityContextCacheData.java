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

import org.apache.log4j.Logger;
import org.restcomm.cache.tree.Fqn;
import org.restcomm.cache.tree.Node;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.CacheData;
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

	private static Logger logger = Logger.getLogger(ActivityContextCacheData.class);

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

	private static final Fqn ATTACHED_SBBs_FQN = Fqn.fromElements(ATTACHED_SBBs_NODE_NAME);

	private static final Fqn ATTACHED_TIMERS_FQN = Fqn.fromElements(ATTACHED_TIMERS_NODE_NAME);

	private static final Fqn NAMES_BOUND_FQN = Fqn.fromElements(NAMES_BOUND_NODE_NAME);

	private static final Fqn CMP_ATTRIBUTES_FQN = Fqn.fromElements(CMP_ATTRIBUTES_NODE_NAME);
	
	private static final Fqn IS_ENDING_FQN = Fqn.fromElements(IS_ENDING_NODE_NAME);

	private static final Boolean CMP_ATTRIBUTES_NODE_MAP_KEY = Boolean.TRUE;

	private static final String STRING_ID_ATTRIBUTE_MAP_KEY = "sid";
	
	private Node _attachedSbbsNode;
	
	private Node getAttachedSbbsNode(boolean createIfNotExists) {
		//// TEST: check getAttachedSbbsNode
		logger.debug("#### TEST [getAttachedSbbsNode]: _attachedSbbsNode: "+_attachedSbbsNode);

		if (_attachedSbbsNode == null) {
			final Node node = getNode();
			logger.debug("#### TEST [getAttachedSbbsNode]: node: "+node);

			_attachedSbbsNode = node.getChild(ATTACHED_SBBs_NODE_NAME);
			logger.debug("#### TEST [getAttachedSbbsNode]: _attachedSbbsNode: "+_attachedSbbsNode);

			if (_attachedSbbsNode == null && createIfNotExists) {
				logger.debug("#### TEST [getAttachedSbbsNode]: addChild!");

				_attachedSbbsNode = node.addChild(ATTACHED_SBBs_FQN);
				logger.debug("#### TEST [getAttachedSbbsNode]: _attachedSbbsNode: "+_attachedSbbsNode);

				//// TEST: check addChild
				if (node.hasChild(ATTACHED_SBBs_FQN)) {
					logger.trace("#### TEST [getAttachedSbbsNode]: hasChild success!");
					final Node child = node.getChild(ATTACHED_SBBs_FQN);
					logger.trace("#### TEST [getAttachedSbbsNode]: getChild: "+child);
				} else {
					logger.trace("#### TEST [getAttachedSbbsNode]: hasChild failed!");
				}
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
		super(Fqn.fromElements(parentNodeFqn, activityContextHandle),
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
		//// TEST: check hasChild
		logger.debug("#### TEST [isEnding]: hasChild: "+getNode().hasChild(IS_ENDING_NODE_NAME));

		return getNode().hasChild(IS_ENDING_NODE_NAME);
	}
	
	
	public boolean setEnding(boolean value) {
		if (value) {
			if (!isEnding()) {
				getNode().addChild(IS_ENDING_FQN);

				//// TEST: check addChild
				if (getNode().hasChild(IS_ENDING_FQN)) {
					logger.trace("#### TEST [setEnding-add]: hasChild success!");
					final Node child = getNode().getChild(IS_ENDING_FQN);
					logger.trace("#### TEST [setEnding-add]: getChild: "+child);
				} else {
					logger.trace("#### TEST [setEnding-add]: hasChild failed!");
				}

				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (isEnding()) {
				getNode().removeChild(IS_ENDING_NODE_NAME);

				//// TEST: check removeChild
				if (getNode().hasChild(IS_ENDING_FQN)) {
					logger.trace("#### TEST [setEnding-remove]: hasChild failed!");
					final Node child = getNode().getChild(IS_ENDING_FQN);
					logger.trace("#### TEST [setEnding-remove]: getChild: "+child);
				} else {
					logger.trace("#### TEST [setEnding-remove]: hasChild success!");
				}

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
		logger.debug("#### TEST [attachSbbEntity]: "+sbbEntityId);

		final Node node = getAttachedSbbsNode(true);

		logger.debug("#### TEST [attachSbbEntity]: node: "+node);

		if (!node.hasChild(sbbEntityId)) {

			logger.debug("#### TEST [attachSbbEntity]: addChild: "+Fqn.fromElements(sbbEntityId));

			node.addChild(Fqn.fromElements(sbbEntityId));

			//// TEST: check addChild
			if (node.hasChild(sbbEntityId)) {
				logger.debug("#### TEST [attachSbbEntity]: hasChild success!");
				final Node child = node.getChild(sbbEntityId);
				logger.debug("#### TEST [attachSbbEntity]: getChild: "+child);
			} else {
				logger.debug("#### TEST [attachSbbEntity]: hasChild failed!");
			}

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
		logger.debug("#### TEST [detachSbbEntity]: "+sbbEntityId);
		final Node node  = getAttachedSbbsNode(false);
		logger.debug("#### TEST [detachSbbEntity]: node: "+node);

		boolean result = false;
		//// TEST
		if (node != null) {
			result = node.removeChild(sbbEntityId);

			//// TEST: check removeChild
			if (getNode().hasChild(sbbEntityId)) {
				logger.trace("#### TEST [detachSbbEntity]: hasChild failed!");
				final Node child = getNode().getChild(sbbEntityId);
				logger.trace("#### TEST [detachSbbEntity]: getChild: "+child);
			} else {
				logger.trace("#### TEST [detachSbbEntity]: hasChild success!");
			}
		}

		//return node != null ? node.removeChild(sbbEntityId) : false;
		return result;
	}

	/**
	 * Verifies if there at least one sbb entity attached
	 * 
	 * @return false is there are no sbb entities attached, true otherwise
	 */
	public boolean noSbbEntitiesAttached() {
		final Node node  = getAttachedSbbsNode(false);
		return node != null ? node.getChildNames().isEmpty() : true;
	}

	/**
	 * Return a set with all sbb entities attached.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<SbbEntityID> getSbbEntitiesAttached() {
		logger.debug("#### TEST [getSbbEntitiesAttached]");
		final Node node  = getAttachedSbbsNode(false);
		logger.debug("#### TEST [getSbbEntitiesAttached]: node: "+node);

		/*
		Fqn childFqn = node.getNodeFqn();

		if (childFqn.size() > 0) {
			for (int i = 0; i < childFqn.size(); i++) {
				logger.debug("**** childFqn: ["+i+"]: "+childFqn.get(i));
				logger.debug("**** childFqn: ["+i+"]: "+childFqn.get(i).getClass().getCanonicalName());
			}
		}
		*/

		Set<SbbEntityID> result = null;
		if (node != null) {
			result = new HashSet<SbbEntityID>();

			logger.debug("#### TEST [getSbbEntitiesAttached]: node Fqn: "+node.getNodeFqn());
			logger.trace("#### TEST [getSbbEntitiesAttached]: node.getChildren(): "+node.getChildren());
			logger.trace("#### TEST [getSbbEntitiesAttached]: node.getChildNames: "+node.getChildNames());
			logger.trace("#### TEST [getSbbEntitiesAttached]: node.getChildKeys: "+node.getChildKeys());
			logger.trace("#### TEST [getSbbEntitiesAttached]: node.getChildValues: "+node.getChildValues());

			for (Node childNode : node.getChildren()) {
				logger.trace("!!!! childNode: "+childNode);
				logger.trace("!!!! childNode: "+childNode.getFqn());
				Object last = childNode.getFqn().getLastElement();
				logger.trace("!!!! last: "+last);
				logger.trace("!!!! last: "+last.getClass().getCanonicalName());
				if (last instanceof SbbEntityID) {
					//if (result == null) {
					//	result = new HashSet<SbbEntityID>();
					//}
					result.add((SbbEntityID) last);
				}
			}
		} else {
			result = Collections.emptySet();
		}

		logger.debug("#### TEST [getSbbEntitiesAttached]: result: "+result);

		return result;
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
		return node != null ? node.getChildNames().isEmpty() : true;
	}

	/**
	 * Returns the set of timers attached to the ac
	 * 
	 * @return
	 */
	public Set getAttachedTimers() {
		final Node node = getAttachedTimersNode(false);

		Set result = Collections.EMPTY_SET;
		if (node != null) {
			result = new HashSet();

			//logger.debug("#### TEST [getActivityContextHandles]: node Fqn: "+node.getNodeFqn());
			//logger.debug("#### TEST [getActivityContextHandles]: node.getChildren(): "+node.getChildren());
			//logger.debug("#### TEST [getActivityContextHandles]: node.getChildNames: "+node.getChildNames());
			//logger.debug("#### TEST [getActivityContextHandles]: node.getChildKeys: "+node.getChildKeys());
			//logger.debug("#### TEST [getActivityContextHandles]: node.getChildValues: "+node.getChildValues());

			Set<String> names = node.getChildNames();
			for (String name : names) {
				logger.debug("#### TEST [getAttachedTimers]: name: "+name);
				if (this.getMobicentsCache().getJBossCache().keySet()
						.contains(node.getFqn().toString()+"/"+name)) {

					Object checkNode = this.getMobicentsCache().getJBossCache().get(node.getFqn().toString()+"/" + name + "_/_" + "Node");
					logger.info("@@@@ FOUND-2 checkNode: " + checkNode);

					Fqn nodeFqn = ((Node) checkNode).getFqn();

					if (nodeFqn.size() > 0) {
						for (int i = 0; i < nodeFqn.size(); i++) {
							logger.debug("@@@@ getAttachedTimers childFqn: [" + i + "]: " + nodeFqn.get(i));
							logger.debug("@@@@ getAttachedTimers childFqn: [" + i + "]: " + nodeFqn.get(i).getClass().getCanonicalName());
						}
					}

					Object element = nodeFqn.get(4-1);
					logger.debug("#### TEST [getAttachedTimers]: element: " + element);

					if (element instanceof TimerID) {
						TimerID timerID = (TimerID) element;
						logger.debug("#### TEST [getAttachedTimers]: timerID: " + timerID);

						if (!result.contains(timerID)) {
							logger.debug("#### TEST [getAttachedTimers]: ADD timerID: " + timerID);
							result.add(timerID);
						} else {
							logger.debug("#### TEST [getAttachedTimers]: CONTAINS timerID: " + timerID);
						}
					}
				}
			}

			/*
			Set<Object> values = node.getChildrenNames();
			logger.debug("#### TEST [getActivityContextHandles]: values: "+values);

			for (Object object : values) {
				logger.debug("#### TEST [getActivityContextHandles]: object: "+object);

				if (object instanceof ActivityContextHandle) {
					ActivityContextHandle ach = (ActivityContextHandle) object;
					logger.debug("#### TEST [getActivityContextHandles]: ach: "+ach);

					result.add(ach);
				}
			}
			*/
		}

		//return node != null ? node.getChildNames() : Collections.EMPTY_SET;
		return result;
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
		return node != null ? node.getChildNames().isEmpty() : true;
	}

	/**
	 * Returns the set of names bound to the ac
	 * 
	 * @return
	 */
	public Set getNamesBoundCopy() {
		final Node node = getNamesBoundNode(false);
		return node != null ? node.getChildNames() : Collections.EMPTY_SET;
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

	@SuppressWarnings("unchecked")
	public String getStringID() {
		return (String) getNode().get(STRING_ID_ATTRIBUTE_MAP_KEY);
	}

	@SuppressWarnings("unchecked")
	public void setStringID(String sid) {
		getNode().put(STRING_ID_ATTRIBUTE_MAP_KEY, sid);
	}

}