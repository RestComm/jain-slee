package org.mobicents.slee.runtime.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.slee.profile.ProfileSpecificationID;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

/**
 * 
 * Proxy object for profile table data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ProfileTableCacheData extends CacheData {

	/**
	 * the fqn of the node that holds all activity context cache child nodes
	 */
	private final static Fqn parentNodeFqn = Fqn
			.fromElements(ProfileManagerCacheData.CACHE_NODE_NAME);

	// the data stored directly in the cache node
	private static final String PROFILE_TABLE_PROFILE_SPEC_ID_CACHE_NODE_MAP_KEY = "prof-spec-id";
	private static final String PROFILE_TABLE_CMP_INTERFACE_NAME_CACHE_NODE_MAP_KEY = "cmp-interface-name";
	private static final String PROFILE_TABLE_SINGLE_PROFILE_CACHE_NODE_MAP_KEY = "single-profile";
	private static final String PROFILE_TABLE_PROFILE_INDEXES_CACHE_NODE_MAP_KEY = "profile-indexes";

	// indexed attrs child node
	private static final Fqn INDEXED_ATTRIBUTES_CHILD_NODE_FQN = Fqn
			.fromElements("indexed-attr");

	// and each indexed attr child node data
	private static final String INDEXED_ATTR_CLASS_TYPE_CACHE_NODE_MAP_KEY = "class-type";
	private static final String INDEXED_ATTR_IS_ARRAY_CACHE_NODE_MAP_KEY = "is-array";
	private static final String INDEXED_ATTR_IS_UNIQUE_CACHE_NODE_MAP_KEY = "is-unique";
	private static final String INDEXED_ATTR_INDEX_MAP_CACHE_NODE_MAP_KEY = "indexes";

	// profiles (inside the table) child node
	private static final Fqn profilesCacheChildNodeName = Fqn
			.fromElements("profiles");
	// profile id in a profile cache node
	private static final String IS_COMMITTED_CACHE_NODE_MAP_KEY = "is-committed";
	private static final String PROFILE_ATTRIBUTES_CACHE_NODE_MAP_KEY = "attrs";

	// default profile cache node name
	private static final Object DEFAULT_PROFILE_NODE_NAME = Integer
			.valueOf(0);

	/**
	 * 
	 * @param profileTableName
	 */
	protected ProfileTableCacheData(String profileTableName, Cache jBossCache) {
		super(Fqn.fromRelativeElements(parentNodeFqn, profileTableName),
				jBossCache);
	}

	public void setProfileTableData(
			ProfileSpecificationID profileSpecificationID,
			String cmpInterfaceName, boolean isSingleProfile, Map profileIndexes) {
		Node node = getNode();
		node.put(PROFILE_TABLE_PROFILE_SPEC_ID_CACHE_NODE_MAP_KEY,
				profileSpecificationID);
		node.put(PROFILE_TABLE_CMP_INTERFACE_NAME_CACHE_NODE_MAP_KEY,
				cmpInterfaceName);
		node.put(PROFILE_TABLE_SINGLE_PROFILE_CACHE_NODE_MAP_KEY, Boolean
				.valueOf(isSingleProfile));
		node.put(PROFILE_TABLE_PROFILE_INDEXES_CACHE_NODE_MAP_KEY,
				profileIndexes);
	}

	public void addIndexedAttribute(String attributeName, String classtype,
			Boolean isArray, Boolean isUnique) {
		Node childNode = getNode().addChild(
				Fqn.fromRelativeElements(INDEXED_ATTRIBUTES_CHILD_NODE_FQN,
						attributeName));
		childNode.put(INDEXED_ATTR_CLASS_TYPE_CACHE_NODE_MAP_KEY, classtype);
		childNode.put(INDEXED_ATTR_IS_ARRAY_CACHE_NODE_MAP_KEY, isArray);
		childNode.put(INDEXED_ATTR_IS_UNIQUE_CACHE_NODE_MAP_KEY, isUnique);
	}

	public boolean isIndexedAttribute(String attributeName) {
		return getNode().hasChild(
				Fqn.fromRelativeElements(INDEXED_ATTRIBUTES_CHILD_NODE_FQN,
						attributeName));
	}

	public Boolean isUniqueIndexedAttribute(String attributeName) {
		return (Boolean) getNode().getChild(
				Fqn.fromRelativeElements(INDEXED_ATTRIBUTES_CHILD_NODE_FQN,
						attributeName)).get(
				INDEXED_ATTR_IS_UNIQUE_CACHE_NODE_MAP_KEY);
	}

	public String getIndexedAttributeClassType(String attributeName) {
		return (String) getNode().getChild(
				Fqn.fromRelativeElements(INDEXED_ATTRIBUTES_CHILD_NODE_FQN,
						attributeName)).get(
				INDEXED_ATTR_CLASS_TYPE_CACHE_NODE_MAP_KEY);
	}

	public Map getIndexedAttributeIndexMap(String attributeName) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(INDEXED_ATTRIBUTES_CHILD_NODE_FQN,
						attributeName));
		Map map = (Map) childNode
				.get(INDEXED_ATTR_INDEX_MAP_CACHE_NODE_MAP_KEY);
		if (map == null) {
			map = new HashMap();
			childNode.put(INDEXED_ATTR_INDEX_MAP_CACHE_NODE_MAP_KEY, map);
		}
		return map;
	}

	public ProfileSpecificationID getProfileSpecId() {
		return (ProfileSpecificationID) getNode().get(
				PROFILE_TABLE_PROFILE_SPEC_ID_CACHE_NODE_MAP_KEY);
	}

	public String getCmpInterfaceName() {
		return (String) getNode().get(
				PROFILE_TABLE_CMP_INTERFACE_NAME_CACHE_NODE_MAP_KEY);
	}

	public Set getAndRemoveProfiles() {
		Set result = new HashSet();
		Node childNode = getNode().getChild(profilesCacheChildNodeName);
		if (childNode != null) {
			for (Object obj : childNode.getChildren()) {
				Node node = (Node) obj;
				Object profileName = node.getFqn().getLastElement();
				// default profile doesn't count
				if (profileName != DEFAULT_PROFILE_NODE_NAME) {
					result.add(profileName);
					childNode.removeChild(profileName);
				}
			}
		}
		return result;
	}

	public Boolean isSingleProfile() {
		return (Boolean) getNode().get(
				PROFILE_TABLE_SINGLE_PROFILE_CACHE_NODE_MAP_KEY);
	}

	public Map getProfileIndexes() {
		return (Map) getNode().get(
				PROFILE_TABLE_PROFILE_INDEXES_CACHE_NODE_MAP_KEY);
	}

	public boolean hasCommittedProfiles() {
		Node childNode = getNode().getChild(profilesCacheChildNodeName);
		if (childNode != null) {
			for (Object obj : childNode.getChildren()) {
				Node node = (Node) obj;
				// default profile doesn't count
				if (node.getFqn().getLastElement() != DEFAULT_PROFILE_NODE_NAME) {
					if ((Boolean) node.get(IS_COMMITTED_CACHE_NODE_MAP_KEY)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public Set getProfiles() {
		Set result = new HashSet();
		Node childNode = getNode().getChild(profilesCacheChildNodeName);
		if (childNode != null) {
			for (Object obj : childNode.getChildren()) {
				Node node = (Node) obj;
				Object profileName = node.getFqn().getLastElement();
				// just add non default profiles
				if (profileName != DEFAULT_PROFILE_NODE_NAME) {
					if ((Boolean) node.get(IS_COMMITTED_CACHE_NODE_MAP_KEY)) {
						result.add(profileName);
					}
				}
			}
		}
		return Collections.unmodifiableSet(result);
	}

	public void addProfile(String profileName) {
		getNode().addChild(
				Fqn.fromRelativeElements(profilesCacheChildNodeName,
						getProfileMapKey(profileName))).put(
				IS_COMMITTED_CACHE_NODE_MAP_KEY, Boolean.FALSE);
	}

	public void setProfileAsCommitted(String profileName) {
		getNode().getChild(
				Fqn.fromRelativeElements(profilesCacheChildNodeName,
						getProfileMapKey(profileName))).put(
				IS_COMMITTED_CACHE_NODE_MAP_KEY, Boolean.TRUE);
	}

	public boolean isProfileCommitted(String profileName) {
		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(profilesCacheChildNodeName,
						getProfileMapKey(profileName)));
		if (childNode != null) {
			return childNode.get(IS_COMMITTED_CACHE_NODE_MAP_KEY) == Boolean.TRUE;
		}
		return false;
	}

	public void setProfileAttributeValue(String profileName,
			String attributeName, Object attributeValue) {

		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(profilesCacheChildNodeName,
						getProfileMapKey(profileName)));
		Map map = (Map) childNode.get(PROFILE_ATTRIBUTES_CACHE_NODE_MAP_KEY);
		if (map == null) {
			map = new HashMap();
			childNode.put(PROFILE_ATTRIBUTES_CACHE_NODE_MAP_KEY, map);
		}
		map.put(attributeName, attributeValue);
	}

	public Object getProfileAttributeValue(String profileName,
			String attributeName) {

		Node childNode = getNode().getChild(
				Fqn.fromRelativeElements(profilesCacheChildNodeName,
						getProfileMapKey(profileName)));
		Map map = (Map) childNode.get(PROFILE_ATTRIBUTES_CACHE_NODE_MAP_KEY);
		if (map != null) {
			return map.get(attributeName);
		}
		return null;
	}

	public void removeProfile(String profileName) {
		getNode().removeChild(
				Fqn.fromRelativeElements(profilesCacheChildNodeName,
						getProfileMapKey(profileName)));
	}

	// --- HELPERS

	private Object getProfileMapKey(String profileName) {
		return profileName == null ? DEFAULT_PROFILE_NODE_NAME : profileName;
	}

}
