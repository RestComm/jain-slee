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

import org.apache.log4j.Logger;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.restcomm.cache.CacheData;
import org.restcomm.cache.tree.Fqn;
import org.restcomm.cache.tree.Node;
import org.restcomm.cluster.MobicentsCluster;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Proxy object for activity context factory data management through JBoss Cache
 * 
 * @author martins
 * 
 */

public class ActivityContextFactoryCacheData extends CacheData {

	private static Logger logger = Logger.getLogger(ActivityContextFactoryCacheData.class);

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
		logger.debug("#### TEST [getActivityContextHandles]");
		final Node node = getNode();
		//// TEST: check getActivityContextHandles
		logger.debug("#### TEST [getActivityContextHandles]: node: "+node);

		Set<ActivityContextHandle> result = Collections.EMPTY_SET;
		if (node != null) {
			result = new HashSet<ActivityContextHandle>();

			logger.debug("#### TEST [getActivityContextHandles]: node Fqn: "+node.getNodeFqn());

			logger.debug("#### TEST [getActivityContextHandles]: node.getChildren(): "+node.getChildren());
			logger.debug("#### TEST [getActivityContextHandles]: node.getChildNames: "+node.getChildNames());
			logger.debug("#### TEST [getActivityContextHandles]: node.getChildKeys: "+node.getChildKeys());
			logger.debug("#### TEST [getActivityContextHandles]: node.getChildValues: "+node.getChildValues());

			Set<String> names = node.getChildNames();
			for (String name : names) {
				logger.debug("#### TEST [getActivityContextHandles]: name: "+name);
				if (this.getMobicentsCache().getJBossCache().keySet()
						.contains("/ac/"+name)) {

					Object checkNode = this.getMobicentsCache().getJBossCache().get("/ac/" + name + "_/_" + "Node");
					logger.info("@@@@ FOUND-2 checkNode: " + checkNode);

					Fqn nodeFqn = ((Node) checkNode).getFqn();

					if (nodeFqn.size() > 0) {
						for (int i = 0; i < nodeFqn.size(); i++) {
							logger.debug("@@@@ getChildrenNames childFqn: [" + i + "]: " + nodeFqn.get(i));
							logger.debug("@@@@ getChildrenNames childFqn: [" + i + "]: " + nodeFqn.get(i).getClass().getCanonicalName());
						}
					}

					Object achElement = nodeFqn.get(2-1);
					logger.debug("#### TEST [getActivityContextHandles]: achElement: " + achElement);

					if (achElement instanceof ActivityContextHandle) {
						ActivityContextHandle ach = (ActivityContextHandle) achElement;
						logger.debug("#### TEST [getActivityContextHandles]: ach: " + ach);

						if (!result.contains(ach)) {
							logger.debug("#### TEST [getActivityContextHandles]: ADD ach: " + ach);
							result.add(ach);
						} else {
							logger.debug("#### TEST [getActivityContextHandles]: CONTAINS ach: " + ach);
						}
					}
				}
			}

			/*
			Set<Object> values = node.getChildrenNames();
			logger.debug("#### TEST [getActivityContextHandles]: values: "+values);

			for (Object object : values) {
				logger.debug("#### TEST [getActivityContextHandles]: object: "+object);

				if (object instanceof ActivityContextHandle) {
					ActivityContextHandle ach = (ActivityContextHandle) object;
					logger.debug("#### TEST [getActivityContextHandles]: ach: "+ach);

					result.add(ach);
				}
			}
			*/
		}

		//return node != null ? node.getChildrenValues() : Collections.EMPTY_SET;
		return result;
	}

	public void WAremove() {
		final Node node = getNode();
		logger.debug("$$$$ node: "+node);
		if (node != null) {
			if (!node.getChildren().isEmpty()) {
				logger.debug("$$$$ is not empty");
				for (Object cho : node.getChildren()) {
					//Node chn = (Node)cho;
					logger.debug("$$$$ object: "+cho);
					//node.removeChild(cho);
				}
				node.removeChildren();
			}

			if (!node.getChildren().isEmpty()) {
				logger.debug("$$$$ is not empty");
			}

			logger.debug("$$$$ is empty");
		}
	}

}
