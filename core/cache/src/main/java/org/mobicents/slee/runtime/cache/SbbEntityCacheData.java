package org.mobicents.slee.runtime.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;

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
	private final static Fqn parentNodeFqn = Fqn.fromElements("sbb-entity");

	// node map keys

	private static final String PARENT_SBB_ENTITY_ID_NODE_MAP_KEY = "parent-sbbeid";
	private static final String PARENT_CHILD_RELATION_NODE_MAP_KEY = "parent-chdrel-name";
	private static final String ROOT_SBB_ID_NODE_MAP_KEY = "root-sbbid";
	private static final String CREATION_DATE_NODE_MAP_KEY = "date";
	private static final String SERVICE_CONVERGENCE_NAME_NODE_MAP_KEY = "convergence-name";
	private static final String SBB_ID_NODE_MAP_KEY = "sbbid";
	private static final String PRIORITY_NODE_MAP_KEY = "priority";
	private static final String SERVICE_ID_NODE_MAP_KEY = "serviceid";
	private static final String EVENT_MASK_CHILD_NODE_MAP_KEY = "event-mask";

	private static final Fqn CMP_FIELDS_CHILD_NODE_FQN = Fqn
			.fromElements("cmp-fields");

	// --- child cache nodes naming

	private static final Fqn ATTACHED_ACs_CHILD_NODE_FQN = Fqn
			.fromElements("ac");
	private static final Fqn EVENT_MASKS_CHILD_NODE_FQN = Fqn
			.fromElements("event-mask");
	protected static final Fqn CHILD_RELATIONs_CHILD_NODE_FQN = Fqn
			.fromElements("chd-rel");

	/**
	 * 
	 * @param sbbEntityId
	 */
	protected SbbEntityCacheData(String sbbEntityId, Cache jBossCache) {
		super(Fqn.fromRelativeElements(parentNodeFqn, sbbEntityId), jBossCache);
	}

	public ServiceID getServiceId() {
		return (ServiceID) getNode().get(SERVICE_ID_NODE_MAP_KEY);
	}

	public void setServiceId(ServiceID svcId) {
		getNode().put(SERVICE_ID_NODE_MAP_KEY, svcId);
	}

	public void setServiceConvergenceName(String convergenceName) {
		getNode().put(SERVICE_CONVERGENCE_NAME_NODE_MAP_KEY, convergenceName);
	}

	public String getServiceConvergenceName() {
		return (String) getNode().get(SERVICE_CONVERGENCE_NAME_NODE_MAP_KEY);
	}

	public void attachActivityContext(Object ac) {
		getNode().addChild(
				Fqn.fromRelativeElements(ATTACHED_ACs_CHILD_NODE_FQN, ac));
	}

	public void detachActivityContext(Object ac) {
		getNode().removeChild(
				Fqn.fromRelativeElements(ATTACHED_ACs_CHILD_NODE_FQN, ac));
	}

	public Set getMaskedEventTypes(Object ac) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(EVENT_MASKS_CHILD_NODE_FQN, ac));
		if (childNode == null) {
			return Collections.EMPTY_SET;
		} else {
			return (Set) childNode.get(EVENT_MASK_CHILD_NODE_MAP_KEY);
		}
	}

	public void setEventMask(Object ac, Set eventMask) {
		Fqn childNodeFqn = Fqn.fromRelativeElements(EVENT_MASKS_CHILD_NODE_FQN,
				ac);
		if (eventMask != null && !eventMask.isEmpty()) {
			Node node = getNode();
			Node childNode = node.getChild(childNodeFqn);
			if (childNode == null) {
				childNode = node.addChild(childNodeFqn);
			}
			childNode.put(EVENT_MASK_CHILD_NODE_MAP_KEY, eventMask);
		} else {
			getNode().removeChild(childNodeFqn);
		}
	}

	public void updateEventMask(Object ac, Set<EventTypeID> maskedEvents) {
		Node node = getNode();
		Fqn childNodeFqn = Fqn.fromRelativeElements(EVENT_MASKS_CHILD_NODE_FQN,
				ac);
		Node childNode = node.getChild(childNodeFqn);
		if (childNode == null) {
			childNode = node.addChild(childNodeFqn);
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
		final Node attachedACsCacheNode = getNode().getChild(
				ATTACHED_ACs_CHILD_NODE_FQN);
		if (attachedACsCacheNode == null) {
			return Collections.EMPTY_SET;
		} else {
			return new HashSet(attachedACsCacheNode.getChildrenNames());			
		}
	}

	public String getRootSbbId() {
		return (String) getNode().get(ROOT_SBB_ID_NODE_MAP_KEY);
	}

	public void setRootSbbId(String rootSbbEntityId) {
		getNode().put(ROOT_SBB_ID_NODE_MAP_KEY, rootSbbEntityId);
	}

	public Byte getPriority() {
		return (Byte) getNode().get(PRIORITY_NODE_MAP_KEY);
	}

	public void setPriority(Byte priority) {
		getNode().put(PRIORITY_NODE_MAP_KEY, priority);
	}

	public SbbID getSbbId() {
		return (SbbID) getNode().get(SBB_ID_NODE_MAP_KEY);
	}

	public void setSbbId(SbbID sbbId) {
		getNode().put(SBB_ID_NODE_MAP_KEY, sbbId);
	}

	/**
	 * Retreives the name of the child relation of the parent this sbb entity
	 * belongs.
	 * 
	 * @return
	 */
	public String getParentChildRelation() {
		return (String) getNode().get(PARENT_CHILD_RELATION_NODE_MAP_KEY);
	}

	/**
	 * Sets the parent child relation name.
	 * 
	 * @param name
	 */
	public void setParentChildRelation(String parentChildRelation) {
		getNode().put(PARENT_CHILD_RELATION_NODE_MAP_KEY, parentChildRelation);
	}

	/**
	 * Retreives the id of the parent sbb entity.
	 * 
	 * @return
	 */
	public String getParentSbbEntityId() {
		return (String) getNode().get(PARENT_SBB_ENTITY_ID_NODE_MAP_KEY);
	}

	/**
	 * Sets the parent sbb entity id.
	 * 
	 * @param name
	 */
	public void setParentSbbEntityId(String parentSbbEntityId) {
		getNode().put(PARENT_SBB_ENTITY_ID_NODE_MAP_KEY, parentSbbEntityId);
	}

	public void setCmpField(String cmpField, Object cmpValue) {
		Node node = getNode();
		Fqn childNodeFqn = Fqn.fromElements(CMP_FIELDS_CHILD_NODE_FQN);
		Node childNode = node.getChild(childNodeFqn);
		if (childNode == null) {
			childNode = node.addChild(childNodeFqn);
		}
		childNode.put(cmpField, cmpValue);
	}

	public Object getCmpField(String cmpField) {
		Node childNode = getNode().getChild(
				Fqn.fromElements(CMP_FIELDS_CHILD_NODE_FQN));
		if (childNode == null) {
			return null;
		} else {
			return childNode.get(cmpField);
		}
	}

	public Set getChildRelationSbbEntities(Object getChildRelationMethod) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(CHILD_RELATIONs_CHILD_NODE_FQN,
						getChildRelationMethod));
		if (childNode == null) {
			return Collections.EMPTY_SET;
		} else {
			return childNode.getKeys();
		}
	}

	public int childRelationSbbEntitiesSize(Object getChildRelationMethod) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(CHILD_RELATIONs_CHILD_NODE_FQN,
						getChildRelationMethod));
		if (childNode == null) {
			return 0;
		} else {
			return childNode.dataSize();
		}
	}

	public void removeChildRelationSbbEntity(Object getChildRelationMethod,
			String sbbEntityId) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(CHILD_RELATIONs_CHILD_NODE_FQN,
						getChildRelationMethod));
		if (childNode != null) {
			childNode.remove(sbbEntityId);
		}
	}

	public void addChildRelationSbbEntity(Object getChildRelationMethod,
			String sbbEntityId) {
		Node node = getNode();
		Fqn childNodeFqn = Fqn.fromRelativeElements(
				CHILD_RELATIONs_CHILD_NODE_FQN, getChildRelationMethod);
		Node childNode = node.getChild(childNodeFqn);
		if (childNode == null) {
			childNode = node.addChild(childNodeFqn);
		}
		childNode.put(sbbEntityId, MAP_VALUE);
	}

	public boolean childRelationHasSbbEntity(Object getChildRelationMethod,
			String sbbEntityId) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(CHILD_RELATIONs_CHILD_NODE_FQN,
						getChildRelationMethod));
		if (childNode != null) {
			return childNode.get(sbbEntityId) != null;
		} else {
			return false;
		}
	}

	public void removeChildRelation(Object getChildRelationMethod) {
		getNode().removeChild(
				Fqn.fromRelativeElements(CHILD_RELATIONs_CHILD_NODE_FQN,
						getChildRelationMethod));
	}

	public Set getAllChildSbbEntities() {
		Node childRelationsNode = getNode().getChild(
				CHILD_RELATIONs_CHILD_NODE_FQN);
		if (childRelationsNode == null) {
			return Collections.EMPTY_SET;
		}
		Set result = new HashSet();
		for (Object obj : childRelationsNode.getChildren()) {
			Node childRelationNode = (Node) obj;
			for (Object sbbEntityId : childRelationNode.getData().keySet()) {
				result.add(sbbEntityId);
			}
		}
		return result;
	}

	private static final Object MAP_VALUE = new Object();

	public void setCreationDate(Long creationDate) {
		getNode().put(CREATION_DATE_NODE_MAP_KEY, creationDate);
	}

	public Long getCreationDate() {
		return (Long) getNode().get(CREATION_DATE_NODE_MAP_KEY);
	}
}
