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

package org.mobicents.slee.runtime.sbbentity;

import org.restcomm.cache.tree.Fqn;
import org.restcomm.cache.tree.Node;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.MobicentsCache;

import javax.slee.EventTypeID;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Proxy object for sbb entity data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class SbbEntityCacheData extends CacheData {

	
	// node map keys

	private static final Boolean MISC_NODE_MAP_VALUE = Boolean.TRUE;

	private static final String PRIORITY_NODE_MAP_KEY = "priority";
		
	private static final String ATTACHED_ACs_CHILD_NODE_NAME = "ac";
	private static final Fqn ATTACHED_ACs_CHILD_NODE_FQN = 
		Fqn.fromElements(ATTACHED_ACs_CHILD_NODE_NAME);
	private Node _attachedACsChildNode;
	private Node getAttachedACsChildNode(boolean createIfNotExists) {
		if (_attachedACsChildNode == null) {			
			final Node node = getNode();
			_attachedACsChildNode = node.getChild(ATTACHED_ACs_CHILD_NODE_NAME);
			if (_attachedACsChildNode == null && createIfNotExists) {
				_attachedACsChildNode = node.addChild(ATTACHED_ACs_CHILD_NODE_FQN);
			}
		}
		return _attachedACsChildNode;
	}
	
	protected static final String CHILD_RELATIONs_CHILD_NODE_NAME = "chd";
	protected static final Fqn CHILD_RELATIONs_CHILD_NODE_FQN = 
		Fqn.fromElements(CHILD_RELATIONs_CHILD_NODE_NAME);
	private Node _childRelationsChildNode;
	private Node getChildRelationsChildNode(boolean createIfNotExists) {
		if (_childRelationsChildNode == null) {
			final Node node = getNode();
			_childRelationsChildNode = node.getChild(CHILD_RELATIONs_CHILD_NODE_NAME);
			if (_childRelationsChildNode == null && createIfNotExists) {
				_childRelationsChildNode = node.addChild(CHILD_RELATIONs_CHILD_NODE_FQN);
			}
		}
		return _childRelationsChildNode;
	}
	
	private static final String EVENT_MASKS_CHILD_NODE_NAME = "event-mask";
	private static final Fqn EVENT_MASKS_CHILD_NODE_FQN = 
		Fqn.fromElements(EVENT_MASKS_CHILD_NODE_NAME);
	//private Node<ActivityContextHandle,Set<EventTypeID>> _eventMasksChildNode;
	private Node _eventMasksChildNode;
	//private Node<ActivityContextHandle,Set<EventTypeID>> getEventMasksChildNode(boolean createIfNotExists) {
	private Node getEventMasksChildNode(boolean createIfNotExists) {
		if (_eventMasksChildNode == null) {
			final Node node = getNode();
			_eventMasksChildNode = node.getChild(EVENT_MASKS_CHILD_NODE_NAME);
			if (_eventMasksChildNode == null && createIfNotExists) {
				_eventMasksChildNode = node.addChild(EVENT_MASKS_CHILD_NODE_FQN);
			}
		}
		return _eventMasksChildNode;
	}
	
	private static final String CMP_FIELDS_CHILD_NODE_NAME = "cmp-fields";
	private static final Fqn CMP_FIELDS_CHILD_NODE_FQN = 
		Fqn.fromElements(CMP_FIELDS_CHILD_NODE_NAME);
	//private Node<String,Object> _cmpFieldsChildNode;
	private Node _cmpFieldsChildNode;
	//private Node<String,Object> getCmpFieldsChildNode(boolean createIfNotExists) {
	private Node getCmpFieldsChildNode(boolean createIfNotExists) {
		if (_cmpFieldsChildNode == null) {
			final Node node = getNode();
			_cmpFieldsChildNode = node.getChild(CMP_FIELDS_CHILD_NODE_NAME);
			if (_cmpFieldsChildNode == null && createIfNotExists) {
				_cmpFieldsChildNode = node.addChild(CMP_FIELDS_CHILD_NODE_FQN);
			}
		}
		return _cmpFieldsChildNode;
	}
	
	private final SbbEntityID sbbEntityID;
	
	private static Fqn getFqn(SbbEntityID sbbEntityID) {
		if (sbbEntityID.isRootSbbEntity()) {
			return Fqn.fromElements(SbbEntityFactoryCacheData.SBB_ENTITY_FACTORY_FQN_NAME,sbbEntityID.getServiceID(),sbbEntityID.getServiceConvergenceName());
		}
		else {
			return Fqn.fromRelativeElements(getFqn(sbbEntityID.getParentSBBEntityID()),CHILD_RELATIONs_CHILD_NODE_NAME,sbbEntityID.getParentChildRelation(),((NonRootSbbEntityID)sbbEntityID).getChildID()); 
		}
	}
	
	/**
	 * 
	 * @param sbbEntityId
	 */
	public SbbEntityCacheData(SbbEntityID sbbEntityId, MobicentsCache cache) {
		super(getFqn(sbbEntityId), cache);
		this.sbbEntityID = sbbEntityId;
	}

	public void attachActivityContext(ActivityContextHandle ac) {
		//getAttachedACsChildNode(true).put(ac, MISC_NODE_MAP_VALUE);
		getAttachedACsChildNode(true).put(ac, ac);
	}

	public void detachActivityContext(ActivityContextHandle ac) {
		final Node node  = getAttachedACsChildNode(false);
		if (node != null) {
			node.remove(ac);
		}
	}

	public Set<EventTypeID> getMaskedEventTypes(ActivityContextHandle ac) {
		//final Node<ActivityContextHandle,Set<EventTypeID>> node = getEventMasksChildNode(false);
		final Node node = getEventMasksChildNode(false);
		if (node == null) {
			return null;
		}
		else {
			Set<EventTypeID> eventTypes = null;
			try {
				eventTypes = (Set<EventTypeID>)node.get(ac);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return eventTypes;
		}
	}

	public void setEventMask(ActivityContextHandle ac, Set<EventTypeID> eventMask) {
		if (eventMask != null && !eventMask.isEmpty()) {
			getEventMasksChildNode(true).put(ac,eventMask);
		} else {
			//final Node<ActivityContextHandle,Set<EventTypeID>> eventMasksChildNode = getEventMasksChildNode(false);
			final Node eventMasksChildNode = getEventMasksChildNode(false);
			if (eventMasksChildNode != null) {
				eventMasksChildNode.remove(ac);
			}
		}
	}

	public void updateEventMask(ActivityContextHandle ac, Set<EventTypeID> maskedEvents) {
		//final Node<ActivityContextHandle,Set<EventTypeID>> eventMasksChildNode = getEventMasksChildNode(true);
		final Node eventMasksChildNode = getEventMasksChildNode(true);
		Set<EventTypeID> currentMaskedEvents = null;
		try {
			currentMaskedEvents = (Set<EventTypeID>)eventMasksChildNode.get(ac);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (currentMaskedEvents == null) {
			eventMasksChildNode.put(ac,maskedEvents);
		}
		else {
			currentMaskedEvents.addAll(maskedEvents);
		}
	}

	public Set<ActivityContextHandle> getActivityContexts() {
		final Node node = getAttachedACsChildNode(false);
        System.out.println("!!!! node: "+node);
		Set<ActivityContextHandle> result = null;
		if (node != null) {
			//result = node.getKeys();
            System.out.println("!!!! node.getChildObjects(): "+node.getChildObjects());
			try {
				for (Object o : node.getChildObjects()) {
					System.out.println("!!!! o: "+o);
					System.out.println("!!!! o: "+o.getClass().getCanonicalName());
					if (o instanceof ActivityContextHandle) {
						if (result == null) {
							result = new HashSet<ActivityContextHandle>();
						}
						result.add((ActivityContextHandle) o);
					}
				}
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
		}
		else {
			result = Collections.emptySet();
		}
        System.out.println("!!!! result: "+result);
		return result;
	}

	public boolean isAttached(ActivityContextHandle ach) {
		final Node node = getAttachedACsChildNode(false);
		if (node == null) {
			return false;
		}
		else {
			return node.get(ach) != null;
		}
	}
	
	public Byte getPriority() {
		return (Byte) getNode().get(PRIORITY_NODE_MAP_KEY);
	}

	public void setPriority(Byte priority) {
		getNode().put(PRIORITY_NODE_MAP_KEY, priority);
	}
	
	public void setCmpField(String cmpField, Object cmpValue) {
		//final Node<String,Object> node = getCmpFieldsChildNode(true);
		final Node node = getCmpFieldsChildNode(true);
		node.put(cmpField,cmpValue);
	}

	public Object getCmpField(String cmpField) {
		//final Node<String,Object> node = getCmpFieldsChildNode(false);
		final Node node = getCmpFieldsChildNode(false);
		if (node == null) {
			return null;
		}
		else {
			return node.get(cmpField);
		}
	}
	
	public Set<SbbEntityID> getChildRelationSbbEntities(String getChildRelationMethod) {
		final Node node = getChildRelationsChildNode(false);
		if (node == null) {
			return Collections.emptySet();
		}
		final Node childNode = node.getChild(getChildRelationMethod);
		if (childNode == null) {
			return Collections.emptySet();
		} else {
			Set<SbbEntityID> result = new HashSet<SbbEntityID>();
			for(Object obj : childNode.getChildrenNames()) {
				result.add(new NonRootSbbEntityID(sbbEntityID, getChildRelationMethod,(String)obj));
			}			
			return result;
		}
	}

	public Set<SbbEntityID> getAllChildSbbEntities() {
		Node childRelationsNode = getChildRelationsChildNode(false);
		if (childRelationsNode == null || childRelationsNode.getChildrenNames().isEmpty()) {
			return Collections.emptySet();
		}
		else {
			Set<SbbEntityID> result = new HashSet<SbbEntityID>();
			Node childRelationNode = null;
			for (Object obj : childRelationsNode.getChildren()) {
				childRelationNode = (Node) obj;
				for (Object sbbEntityId : childRelationNode.getChildrenNames()) {
					result.add(new NonRootSbbEntityID(sbbEntityID,(String)childRelationNode.getFqn().getLastElement(),(String)sbbEntityId));
				}
			}
			return result;
		}
	}

}
