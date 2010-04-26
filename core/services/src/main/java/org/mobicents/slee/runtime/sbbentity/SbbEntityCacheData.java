package org.mobicents.slee.runtime.sbbentity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.slee.EventTypeID;

import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.mobicents.cache.CacheData;
import org.mobicents.cache.MobicentsCache;
import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * 
 * Proxy object for sbb entity data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class SbbEntityCacheData extends CacheData {

	/**
	 * the fqn of the node that holds all activity context cache child nodes
	 */
	public final static String parentNodeFqn = "sbb-entity";

	// node map keys

	private static final Boolean MISC_NODE_MAP_VALUE = Boolean.TRUE;

	private static final String SBB_ENTITY_IMMUTABLE_DATA_NODE_MAP_KEY = "idata";
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
	
	protected static final String CHILD_RELATIONs_CHILD_NODE_NAME = "chd-rel";
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
	private Node<ActivityContextHandle,Set<EventTypeID>> _eventMasksChildNode;
	private Node<ActivityContextHandle,Set<EventTypeID>> getEventMasksChildNode(boolean createIfNotExists) {
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
	private Node<String,CmpWrapper> _cmpFieldsChildNode;
	private Node<String,CmpWrapper> getCmpFieldsChildNode(boolean createIfNotExists) {
		if (_cmpFieldsChildNode == null) {
			final Node node = getNode();
			_cmpFieldsChildNode = node.getChild(CMP_FIELDS_CHILD_NODE_NAME);
			if (_cmpFieldsChildNode == null && createIfNotExists) {
				_cmpFieldsChildNode = node.addChild(CMP_FIELDS_CHILD_NODE_FQN);
			}
		}
		return _cmpFieldsChildNode;
	}
	
	/**
	 * 
	 * @param sbbEntityId
	 */
	public SbbEntityCacheData(String sbbEntityId, MobicentsCache cache) {
		super(Fqn.fromElements(parentNodeFqn, sbbEntityId), cache);
	}

	public Object getSbbEntityImmutableData() {
		return getNode().get(SBB_ENTITY_IMMUTABLE_DATA_NODE_MAP_KEY);
	}

	public void setSbbEntityImmutableData(Object obj) {
		getNode().put(SBB_ENTITY_IMMUTABLE_DATA_NODE_MAP_KEY, obj);
	}

	public void attachActivityContext(Object ac) {
		getAttachedACsChildNode(true).put(ac, MISC_NODE_MAP_VALUE);
	}

	public void detachActivityContext(Object ac) {
		final Node node  = getAttachedACsChildNode(false);
		if (node != null) {
			node.remove(ac);
		}
	}

	public Set<EventTypeID> getMaskedEventTypes(ActivityContextHandle ac) {
		final Node<ActivityContextHandle,Set<EventTypeID>> node = getEventMasksChildNode(false);
		if (node == null) {
			return null;
		}
		else {
			return node.get(ac); 
		}
	}

	public void setEventMask(ActivityContextHandle ac, Set<EventTypeID> eventMask) {
		if (eventMask != null && !eventMask.isEmpty()) {
			getEventMasksChildNode(true).put(ac,eventMask);
		} else {
			final Node<ActivityContextHandle,Set<EventTypeID>> eventMasksChildNode = getEventMasksChildNode(false);
			if (eventMasksChildNode != null) {
				eventMasksChildNode.remove(ac);
			}
		}
	}

	public void updateEventMask(ActivityContextHandle ac, Set<EventTypeID> maskedEvents) {
		final Node<ActivityContextHandle,Set<EventTypeID>> eventMasksChildNode = getEventMasksChildNode(true);
		Set<EventTypeID> currentMaskedEvents = eventMasksChildNode.get(ac);
		if (currentMaskedEvents == null) {
			eventMasksChildNode.put(ac,maskedEvents);
		}
		else {
			currentMaskedEvents.addAll(maskedEvents);
		}
	}

	public Set<ActivityContextHandle> getActivityContexts() {
		final Node node = getAttachedACsChildNode(false);
		Set<ActivityContextHandle> result = null;
		if (node != null) {
			result = node.getKeys();
		}
		else {
			result = Collections.emptySet();
		}
		return result;			
	}

	public Byte getPriority() {
		return (Byte) getNode().get(PRIORITY_NODE_MAP_KEY);
	}

	public void setPriority(Byte priority) {
		getNode().put(PRIORITY_NODE_MAP_KEY, priority);
	}
	
	public void setCmpField(String cmpField, CmpWrapper cmpValue) {
		final Node<String,CmpWrapper> node = getCmpFieldsChildNode(true);
		node.put(cmpField,cmpValue);
	}

	public CmpWrapper getCmpField(String cmpField) {
		final Node<String,CmpWrapper> node = getCmpFieldsChildNode(false);
		if (node == null) {
			return null;
		}
		else {
			return node.get(cmpField);
		}
	}
	
	public Set<String> getChildRelationSbbEntities(String getChildRelationMethod) {
		final Node node = getChildRelationsChildNode(false);
		if (node == null) {
			return Collections.emptySet();
		}
		final Node childNode = node.getChild(getChildRelationMethod);
		if (childNode == null) {
			return Collections.emptySet();
		} else {
			return childNode.getChildrenNames();
		}
	}

	public void removeChildRelationSbbEntity(String getChildRelationMethod,
			String sbbEntityId) {
		final Node node = getChildRelationsChildNode(false);
		if (node != null) {
			final Node childNode = node.getChild(getChildRelationMethod);
			if (childNode != null) {
				childNode.removeChild(sbbEntityId);
			}
		}
	}

	public void addChildRelationSbbEntity(String getChildRelationMethod,
			String sbbEntityId) {
		final Node node = getChildRelationsChildNode(true);
		Node childNode = node.getChild(getChildRelationMethod);
		if (childNode == null) {
			childNode = node.addChild(Fqn.fromElements(getChildRelationMethod));
		}
		childNode.addChild(Fqn.fromElements(sbbEntityId));
	}

	public boolean childRelationHasSbbEntity(String getChildRelationMethod,
			String sbbEntityId) {
		final Node node = getChildRelationsChildNode(false);
		if (node == null) {
			return false;
		}
		Node childNode = node.getChild(getChildRelationMethod);
		if (childNode == null) {
			return false;
		} else {
			return childNode.hasChild(sbbEntityId);
		}
	}

	public Set<String> getAllChildSbbEntities() {
		Node childRelationsNode = getChildRelationsChildNode(false);
		if (childRelationsNode == null || childRelationsNode.isLeaf()) {
			return Collections.emptySet();
		}
		else {
			Set<String> result = new HashSet<String>();
			Node childRelationNode = null;
			for (Object obj : childRelationsNode.getChildren()) {
				childRelationNode = (Node) obj;
				for (Object sbbEntityId : childRelationNode.getChildrenNames()) {
					result.add((String) sbbEntityId);
				}
			}
			return result;
		}
	}

}
