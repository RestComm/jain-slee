 /*
  * Mobicents: The Open Source SLEE Platform      
  *
  * Copyright 2003-2005, CocoonHive, LLC., 
  * and individual contributors as indicated
  * by the @authors tag. See the copyright.txt 
  * in the distribution for a full listing of   
  * individual contributors.
  *
  * This is free software; you can redistribute it
  * and/or modify it under the terms of the 
  * GNU Lesser General Public License as
  * published by the Free Software Foundation; 
  * either version 2.1 of
  * the License, or (at your option) any later version.
  *
  * This software is distributed in the hope that 
  * it will be useful, but WITHOUT ANY WARRANTY; 
  * without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  * PURPOSE. See the GNU Lesser General Public License
  * for more details.
  *
  * You should have received a copy of the 
  * GNU Lesser General Public
  * License along with this software; 
  * if not, write to the Free
  * Software Foundation, Inc., 51 Franklin St, 
  * Fifth Floor, Boston, MA
  * 02110-1301 USA, or see the FSF site:
  * http://www.fsf.org.
  */

package org.mobicents.slee.runtime.cache;

import java.util.Map;

import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;

import org.jboss.cache.Node;

/**
 * 
 * Contract for accessing transactional, replicated cache within SLEE.
 * 
 * TODO: This interface should obsolete usage of the SleeTransactionManager for cached data.
 * It should be probably made available as a proprietary Cache RA until the Profiles move up to SLEE 1.1 to allow R/W by SBBs.
 * 
 * @author Ivelin Ivanov
 * 
 * @deprecated Use CacheableMap or CacheableSet instead
 *
 */

public interface OldCacheManager {

    public static final String JNDI_NAME = "SleeCacheManager";

	/**
	 * Put a set of objects in the persistent store.
	 * 
	 * @param fqn -- the fully qualified name under which put the set of key-value pairs
	 * @param data -- the set of key-value pair. can be null
	 * 
	 * @throws TransactionRequiredLocalException if not called in the context of a 
	 * current transaction.
	 * @throws SystemException if failure occurs for any other reason.
	 */        
	public void createNode(String cacheName, String fqn, Map data);

	/**
	 * Retrieve the children of a node
	 * @param the fqn of the node
	 * @return a map with all the children or null if no children for this node exists
	 * @throws SystemException if a low level exception occurs
	 */    
	public Map getChildren(String cacheName, String key) throws SystemException;
	
	/**
	 * 
	 * Retrieves an entry from a transaction local map. The map is not thread safe.
	 * The assumption is that transaction encapsulated code is sequential in regard to
	 * an individual active transaction.
	 * 
	 * @param key
	 * @return
	 */
	public Object getTxLocalData(Object key);

	/**
	 * 
	 * Puts an entry in a transaction local map. The map is not thread safe.
	 * The assumption is that transaction encapsulated code is sequential in regard to
	 * an individual active transaction.
	 * 
	 * @param key
	 * @return
	 */
	public void putTxLocalData(Object key, Object value);

	/**
	 * Get a node from the persistent store.
	 * 
	 * @param fqn -- the fully qualified name of the noe to retrieve
	 * @return the jboss cache node for the fqn
	 * @throws 
	 */        
	public Node getNode(String cacheName, String key);

	/**
	 * Get an object from the persistent store.
	 * 
	 * @deprecated use {@link #getObject(String, String, Object)}
	 */        
	public Object getObject(String cacheName, String fqn,String key)
	throws SystemException;
	
	/**
	 * @deprecated use {@link #putObject(String, String, Object, Object)}
	 */
	public  void putObject(String cacheName, String fqn, String key, Object object);

	public void removeTxLocalData(Object key) throws SystemException;

	/**
	 * Remove a node and all its sub node and its hashmap
	 * @param fqn -- the fully qualified name of the node to retrieve
	 */    
	public void removeNode(String cacheName, String fqn);

}
