package org.mobicents.slee.runtime.cache;

import java.util.HashSet;
import java.util.Set;

import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;

/**
 * Start time:14:16:08 2009-03-23<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileManagementCacheData extends CacheData {

  /**
   * root fqn
   */
  public final static Fqn parentNodeFqn = Fqn.fromElements("profile-tables");

  /**
   * the name of the cache node that holds all profile manager data
   */
  public static final String CACHE_NODE_NAME = "profile-management";

  private static final Object CACHE_NODE_MAP_KEY = new Object();

  protected ProfileManagementCacheData(Cache jBossCache)
  {
    super(Fqn.fromRelativeElements(parentNodeFqn, CACHE_NODE_NAME), jBossCache);
  }

  public void add(String profileTableName, Object table)
  {
    getNode().addChild(Fqn.fromElements(profileTableName)).put(CACHE_NODE_MAP_KEY, table);
  }

  public void remove(String profileTableName)
  {
    getNode().removeChild(Fqn.fromElements(profileTableName));
  }

  public Object get(String profileTableName)
  {
    Node childNode = getNode().getChild(Fqn.fromElements(profileTableName));
    if (childNode == null)
    {
      return null;
    }
    else
    {
      return childNode.get(CACHE_NODE_MAP_KEY);
    }
  }

  public boolean exists(String profileTableName)
  {
    return getNode().hasChild(Fqn.fromElements(profileTableName));
  }

  /**
   * Retrieves all profile table names in the profile manager's cache data
   * 
   * @return
   */
  public Set<String> getProfileTables() {
    return new HashSet<String>(getNode().getChildrenNames());
  }

}
