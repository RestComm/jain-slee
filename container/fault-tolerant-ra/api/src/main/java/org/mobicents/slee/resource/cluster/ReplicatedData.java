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

/**
 * 
 */
package org.mobicents.slee.resource.cluster;

import java.io.Serializable;
import java.util.Set;

/**
 * A container for serializable data, which are replicated in
 * the SLEE cluster, but don't require any failover.
 * 
 * @author martins
 * 
 */
public interface ReplicatedData<K extends Serializable, V extends Serializable> {

	/**
	 * Retrieves a set with data belonging to the cluster member.
	 * 
	 * Note that this is a VERY expensive operation, with respect to performance.
	 * 
	 * @return
	 */
	public Set<K> getLocalKeyset();

	/**
	 * Put the key, with the specified value (may be null for Set like
	 * usage) into replicate data.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean put(K key, V value);

	/**
	 * Retrieves the value stored for the specified key. Note that this
	 * method may return null but the key exist (follow up of a put(K,null)).
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * Tests if the specified key is in replicated data.
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(K key);

	/**
	 * Removes the specified key from replicated data.
	 * @param key
	 * @return true if the node existed and was removed
	 */
	public boolean remove(K key);

	/**
	 * Retrieves all key being replicated, including the ones "belonging" to
	 * other cluster nodes.
	 * 
	 * @return
	 */
	public Set<K> getKeyset();

}
