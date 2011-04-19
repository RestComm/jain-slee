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

package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

import org.jboss.system.ServiceMBean;

public interface MobicentsManagementMBean extends ServiceMBean {

    public final String OBJECT_NAME = "org.mobicents.slee:service=MobicentsManagement";

	/**
	 * 
	 * @return
	 */
	public double getEntitiesRemovalDelay();

	/**
	 * 
	 * @param entitiesRemovalDelay
	 */
	public void setEntitiesRemovalDelay(double entitiesRemovalDelay);
		
	/**
	 * 
	 * @return string representation of container's version.
	 */
	public String getVersion();

	/**
	 * Dumps the container state, useful for a quick check up of leaks. 
	 * 
	 * @return
	 * @throws ManagementException 
	 */
	public String dumpState() throws ManagementException;
	
    public String getLoggingConfiguration(String configuration) throws ManagementException;

    public void setLoggingConfiguration(String configuration, String contents) throws ManagementException;
    
    public void switchLoggingConfiguration(String newProfile) throws ManagementException;
}
