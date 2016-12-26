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

import java.util.Collections;
import java.util.Set;

import org.restcomm.cache.tree.Fqn;
import org.restcomm.cache.tree.Node;
import org.restcomm.cache.CacheData;
import org.restcomm.cluster.MobicentsCluster;
import org.mobicents.slee.container.activity.ActivityContextHandle;

/**
 * 
 * Proxy object for activity context factory data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ActivityContextFactoryCacheData extends CacheData {

	/**
	 * the fqn of the node that holds all activity context cache child nodes
	 */
	final static Fqn NODE_FQN = Fqn.fromElements(ActivityContextCacheData.parentNodeFqn);

	/**
	 * 
	 * @param cluster
	 */
	public ActivityContextFactoryCacheData(MobicentsCluster cluster) {
		super(NODE_FQN, cluster.getMobicentsCache());
	}

	/**
	 * Retrieves a set containing all activity context handles in the factory's
	 * cache data
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<ActivityContextHandle> getActivityContextHandles() {
		final Node node = getNode();

		//// TEST: check getActivityContextHandles
		System.out.println("#### TEST [getActivityContextHandles]: node: "+node);
		if (node != null) {
			System.out.println("###### node: " + node.getFqn());
			System.out.println("###### node: " + node.getChildrenNames());
			System.out.println("###### node: " + node.getChildrenValues());
		}

		Set<ActivityContextHandle> result = Collections.EMPTY_SET;

		if (node != null) {
			Set<Object> values = node.getChildrenValues();
			System.out.println("#### TEST [getActivityContextHandles]: values: "+values);

			for (Object object : values) {
				System.out.println("#### TEST [getActivityContextHandles]: object: "+object);

				if (object instanceof ActivityContextHandle) {
					ActivityContextHandle ach = (ActivityContextHandle)object;
					System.out.println("#### TEST [getActivityContextHandles]: ach: "+ach);

					result.add(ach);
				}
			}
		}

		//return node != null ? node.getChildrenValues() : Collections.EMPTY_SET;
		return result;
	}

	public void WAremove() {
		final Node node = getNode();
		System.out.println("$$$$ node: "+node);
		if (node != null) {
			if (!node.getChildren().isEmpty()) {
				System.out.println("$$$$ is not empty");
				for (Object cho : node.getChildren()) {
					//Node chn = (Node)cho;
					System.out.println("$$$$ object: "+cho);
					//node.removeChild(cho);
				}
				node.removeChildren();
			}

			if (!node.getChildren().isEmpty()) {
				System.out.println("$$$$ is not empty");
			}

			System.out.println("$$$$ is empty");
		}
	}

}