package org.mobicents.slee.runtime.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.slee.EventTypeID;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

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
	private final static String parentNodeFqn = "sbb-entity";

	// node map keys

	private static final String SBB_ENTITY_IMMUTABLE_DATA_NODE_MAP_KEY = "idata";
	private static final String PRIORITY_NODE_MAP_KEY = "priority";
	private static final String EVENT_MASK_CHILD_NODE_MAP_KEY = "event-mask";

	private static final String CMP_FIELDS_CHILD_NODE_NAME = "cmp-fields";
	private static final Fqn CMP_FIELDS_CHILD_NODE_FQN = 
		Fqn.fromElements(CMP_FIELDS_CHILD_NODE_NAME);
	private Node _cmpFieldsChildNode;
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

	private static final String EVENT_MASKS_CHILD_NODE_NAME = "event-mask";
	private static final Fqn EVENT_MASKS_CHILD_NODE_FQN = 
		Fqn.fromElements(EVENT_MASKS_CHILD_NODE_NAME);
	private Node _eventMasksChildNode;
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
	
	/**
	 * 
	 * @param sbbEntityId
	 */
	protected SbbEntityCacheData(String sbbEntityId, Cache jBossCache) {
		super(Fqn.fromElements(parentNodeFqn, sbbEntityId), jBossCache);
	}

	public Object getSbbEntityImmutableData() {
		return getNode().get(SBB_ENTITY_IMMUTABLE_DATA_NODE_MAP_KEY);
	}

	public void setSbbEntityImmutableData(Object obj) {
		getNode().put(SBB_ENTITY_IMMUTABLE_DATA_NODE_MAP_KEY, obj);
	}

	public void attachActivityContext(Object ac) {
		getAttachedACsChildNode(true).addChild(Fqn.fromElements(ac));
	}

	public void detachActivityContext(Object ac) {
		final Node node  = getAttachedACsChildNode(false);
		if (node != null) {
			node.removeChild(ac);
		}
	}

	public Set getMaskedEventTypes(Object ac) {
		final Node node = getEventMasksChildNode(false);
		if (node == null) {
			return null;
		}
		else {
			final Node childNode = node.getChild(ac);
			return childNode == null ? null : (Set) childNode.get(EVENT_MASK_CHILD_NODE_MAP_KEY);
		}
	}

	public void setEventMask(Object ac, Set eventMask) {
		if (eventMask != null && !eventMask.isEmpty()) {
			final Node eventMasksChildNode = getEventMasksChildNode(true);
			Node childNode = eventMasksChildNode.getChild(ac);
			if (childNode == null) {
				childNode = eventMasksChildNode.addChild(Fqn.fromElements(ac));
			}
			childNode.put(EVENT_MASK_CHILD_NODE_MAP_KEY, eventMask);
		} else {
			final Node eventMasksChildNode = getEventMasksChildNode(false);
			if (eventMasksChildNode != null) {
				eventMasksChildNode.removeChild(ac);
			}
		}
	}

	public void updateEventMask(Object ac, Set<EventTypeID> maskedEvents) {
		final Node eventMasksChildNode = getEventMasksChildNode(true);
		Node childNode = eventMasksChildNode.getChild(ac);
		if (childNode == null) {
			childNode = eventMasksChildNode.addChild(Fqn.fromElements(ac));
			childNode.put(EVENT_MASK_CHILD_NODE_MAP_KEY, maskedEvents);
		} else {
			Set set = (Set) childNode.get(EVENT_MASK_CHILD_NODE_MAP_KEY);
			if (set == null) {
				childNode.put(EVENT_MASK_CHILD_NODE_MAP_KEY, maskedEvents);
			} else {
				set.addAll(maskedEvents);
			}
		}
	}

	public Set getActivityContexts() {
		final Node node = getAttachedACsChildNode(false);
		return (node == null) ? Collections.EMPTY_SET : node.getChildrenNames();			
	}

	public Byte getPriority() {
		return (Byte) getNode().get(PRIORITY_NODE_MAP_KEY);
	}

	public void setPriority(Byte priority) {
		getNode().put(PRIORITY_NODE_MAP_KEY, priority);
	}

	public void setCmpField(String cmpField, Object cmpValue) {
		final Node node = getCmpFieldsChildNode(true);
		Node childNode = node.getChild(cmpField);
		if (childNode == null) {
			childNode = node.addChild(Fqn.fromElements(cmpField));
		}
		childNode.put(OBJECT, cmpValue);
	}

	public Object getCmpField(String cmpField) {
		final Node node = getCmpFieldsChildNode(false);
		if (node == null) {
			return null;
		}
		final Node childNode = node.getChild(cmpField);
		if (childNode != null) {
			return childNode.get(OBJECT);
		}
		else {
			return null;
		}
	}

	public Set getChildRelationSbbEntities(Object getChildRelationMethod) {
		final Node node = getChildRelationsChildNode(false);
		if (node == null) {
			return Collections.EMPTY_SET;
		}
		final Node childNode = node.getChild(getChildRelationMethod);
		if (childNode == null) {
			return Collections.EMPTY_SET;
		} else {
			return childNode.getKeys();
		}
	}

	public int childRelationSbbEntitiesSize(Object getChildRelationMethod) {
		final Node node = getChildRelationsChildNode(false);
		if (node == null) {
			return 0;
		}
		Node childNode = node.getChild(getChildRelationMethod);
		if (childNode == null) {
			return 0;
		} else {
			return childNode.dataSize();
		}
	}

	public void removeChildRelationSbbEntity(Object getChildRelationMethod,
			String sbbEntityId) {
		final Node node = getChildRelationsChildNode(false);
		if (node != null) {
			final Node childNode = node.getChild(getChildRelationMethod);
			if (childNode != null) {
				childNode.remove(sbbEntityId);
			}
		}
	}

	public void addChildRelationSbbEntity(Object getChildRelationMethod,
			String sbbEntityId) {
		final Node node = getChildRelationsChildNode(true);
		Node childNode = node.getChild(getChildRelationMethod);
		if (childNode == null) {
			childNode = node.addChild(Fqn.fromElements(getChildRelationMethod));
		}
		childNode.put(sbbEntityId, OBJECT);
	}

	public boolean childRelationHasSbbEntity(Object getChildRelationMethod,
			String sbbEntityId) {
		final Node node = getChildRelationsChildNode(false);
		if (node == null) {
			return false;
		}
		Node childNode = node.getChild(getChildRelationMethod);
		if (childNode == null) {
			return false;
		} else {
			return childNode.get(sbbEntityId) != null;
		}
	}

	public void removeChildRelation(Object getChildRelationMethod) {
		final Node node = getChildRelationsChildNode(false);
		if (node != null) {
			node.removeChild(getChildRelationMethod);
		}		
	}

	public Set getAllChildSbbEntities() {
		Node childRelationsNode = getChildRelationsChildNode(false);
		if (childRelationsNode == null || childRelationsNode.isLeaf()) {
			return Collections.EMPTY_SET;
		}
		else {
			Set result = new HashSet();
			Node childRelationNode = null;
			for (Object obj : childRelationsNode.getChildren()) {
				childRelationNode = (Node) obj;
				for (Object sbbEntityId : childRelationNode.getKeys()) {
					result.add(sbbEntityId);
				}
			}
			return result;
		}
	}

	private static final Object OBJECT = new Object();

}
