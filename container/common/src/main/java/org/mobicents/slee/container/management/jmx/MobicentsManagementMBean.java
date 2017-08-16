/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

public interface MobicentsManagementMBean {

    public static final String OBJECT_NAME = "org.mobicents.slee:service=MobicentsManagement";

	/**
	 * 
	 * @return
	 */
	public int getEntitiesRemovalDelay();

	/**
	 * 
	 * @param entitiesRemovalDelay
	 */
	public void setEntitiesRemovalDelay(int entitiesRemovalDelay);


	/**
	 * Is SBB CMP fields (with Reference Data Type) initialized with null?
	 * @return
	 */
	public boolean isInitializeReferenceDataTypesWithNull();

	/**
	 *
	 * @param initializeReferenceDataTypesWithNull
	 */
	public void setInitializeReferenceDataTypesWithNull(boolean initializeReferenceDataTypesWithNull);
		
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
	
    //public String getLoggingConfiguration(String configuration) throws ManagementException;

    //public void setLoggingConfiguration(String configuration, String contents) throws ManagementException;
    
    //public void switchLoggingConfiguration(String newProfile) throws ManagementException;
}
