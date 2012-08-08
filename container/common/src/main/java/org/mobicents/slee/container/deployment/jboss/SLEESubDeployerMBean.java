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

package org.mobicents.slee.container.deployment.jboss;

import java.net.URL;

import javax.management.ObjectName;

import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.SubDeployerExtMBean;
import org.jboss.mx.util.ObjectNameFactory;

/**
 * Simple MBean for obtaining current deployer status which will show what's
 * deployed and what's on the waiting list for deployment and undeployment.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public interface SLEESubDeployerMBean extends SubDeployerExtMBean {

  // The default ObjectName
  public static final ObjectName OBJECT_NAME = ObjectNameFactory.create("jboss.system:service=SLEESubDeployer");

  String showStatus() throws DeploymentException;

  public void setWaitTimeBetweenOperations(long waitTime);

  public long getWaitTimeBetweenOperations();

  public boolean accepts(URL deployableUnitURL) throws DeploymentException;

  public void init(URL deployableUnitURL) throws DeploymentException;

  public void start(URL deployableUnitURL) throws DeploymentException;

  public void stop(URL deployableUnitURL) throws DeploymentException;

}