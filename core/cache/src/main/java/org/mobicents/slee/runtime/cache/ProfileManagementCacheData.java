/**
 * Start time:14:16:08 2009-03-23<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.runtime.cache;

import java.util.HashSet;
import java.util.Set;

import javax.slee.profile.ProfileTable;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.mobicents.slee.container.profile.ProfileTableConcrete;

/**
 * Start time:14:16:08 2009-03-23<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileManagementCacheData extends CacheData {

	
	// FIXME: Alex do we need more ?
	
	/**
	 * root fqn
	 */
	private final static Fqn parentNodeFqn = Fqn.fromElements("profile-management-root");
	/**
	 * the name of the cache node that holds all profile manager data
	 */
	public static final String CACHE_NODE_NAME = "profile-management";

	protected ProfileManagementCacheData( Cache bossCache) {
		super(parentNodeFqn, bossCache);

	}

	public void add(String profileTableName, ProfileTableConcrete table) {
		getNode().put(profileTableName, table);
	}

	public void remove(String profileTableName) {
		getNode().remove(profileTableName);
	}

	public ProfileTableConcrete get(String profileTableName) {

		return (ProfileTableConcrete) getNode().get(profileTableName);

	}

	public boolean exists(String profileTableName) {
		return getNode().getData().containsKey(profileTableName);
	}

	/**
	 * Retrieves all profile table names in the profile manager's cache data
	 * 
	 * @return
	 */
	public Set<String> getProfileTables() {
		Set<String> result = new HashSet<String>();
		for (Object obj : getNode().getChildren()) {
			Node profileTableNode = (Node) obj;
			result.add(profileTableNode.getFqn().getLastElementAsString());
		}
		return result;
	}

}
