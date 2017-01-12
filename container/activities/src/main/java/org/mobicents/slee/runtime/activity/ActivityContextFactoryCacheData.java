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
 * Proxy object for activity context factory data management through JBoss Cache
 *
 * @author martins
 */

public class ActivityContextFactoryCacheData extends CacheData {

    private static Logger logger = Logger.getLogger(ActivityContextFactoryCacheData.class);

    /**
     * the fqn of the node that holds all activity context cache child nodes
     */
    final static Fqn NODE_FQN = Fqn.fromElements(ActivityContextCacheData.parentNodeFqn);

    /**
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
        return node != null ? node.getChildrenNames() : Collections.EMPTY_SET;
    }

    public void WAremove(String type) {
        final Node node = getNode();
        logger.debug("$$$$ node: " + node);
        if (node != null) {
            if (!node.getChildren().isEmpty()) {
                logger.debug("$$$$ is not empty");
                for (Object cho : node.getChildren()) {
                    logger.debug("$$$$ object: " + cho);
                    if (type != "") {
                        if (cho instanceof Node) {
                            Node chn = (Node) cho;
                            logger.debug("$$$$ object: " + chn.getNodeFqn().getLastElementAsString());
                            if (chn.getNodeFqn().getLastElementAsString().startsWith(type)) {
                                logger.debug("$$$$ REMOVE object: " + chn.getNodeFqn().getLastElementAsString());
                                node.removeChild(chn.getNodeFqn());
                            }
                        }
                    }
                }
                if (type == "") {
                    node.removeChildren();
                }
            }

            if (!node.getChildren().isEmpty()) {
                logger.debug("$$$$ is not empty");
            }

            logger.debug("$$$$ is empty");
        }
    }

}