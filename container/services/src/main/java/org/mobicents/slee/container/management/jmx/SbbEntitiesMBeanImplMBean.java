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

/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.mobicents.slee.container.management.jmx;

import javax.slee.management.ManagementException;

public interface SbbEntitiesMBeanImplMBean {

	public static final String OBJECT_NAME = "org.mobicents.slee:name=SbbEntitiesMBean";
	
  /**
   * Returns an array of details about the SBB Entitiy.
   * 
   * @param sbbeId
   * @return
   * @throws ManagementException
   */
  public Object[] retrieveSbbEntityInfo(String sbbeId) throws ManagementException;

  /**
   * Returns an array of the details about all SBB Entities. 
   * Retrieves the current sbb entities by iterating over the whole sbb entity tree, of each
   * root sbb entity, on a active service.
   * 
   * @return
   * @throws ManagementException
   */
  public Object[] retrieveAllSbbEntities() throws ManagementException;

  /**
   * Administrative remove of SBB Entity.
   * 
   * @param sbbeId
   */
  public void removeSbbEntity(String sbbeId) throws ManagementException;

  /**
   * SBB Entities associated with SBB.
   * 
   * @param sbbId
   * @return
   * @throws ManagementException
   */
  public Object[] retrieveSbbEntitiesBySbbId(String sbbId) throws ManagementException;

}
