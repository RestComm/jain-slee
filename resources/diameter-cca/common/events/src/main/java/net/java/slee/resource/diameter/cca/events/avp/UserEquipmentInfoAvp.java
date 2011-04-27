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

package net.java.slee.resource.diameter.cca.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * <pre>
 *  &lt;b&gt;8.49. User-Equipment-Info AVP&lt;/b&gt;
 * 
 * 
 *   The User-Equipment-Info AVP (AVP Code 458) is of type Grouped and
 *   allows the credit-control client to indicate the identity and
 *   capability of the terminal the subscriber is using for the connection
 *   to network.
 * 
 *   It is defined as follows (per the grouped-avp-def of RFC 3588
 *   [DIAMBASE]):
 * 
 *      User-Equipment-Info ::= &lt; AVP Header: 458 &gt;
 *                              { User-Equipment-Info-Type }
 *                              { User-Equipment-Info-Value }
 *                              
 * </pre>
 *      
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface UserEquipmentInfoAvp extends GroupedAvp {

  /**
   * Sets the value of the User-Equipment-Info-Type AVP, of type Enumerated.
   * <br>
   * See: {@link UserEquipmentInfoType}
   * 
   * @param type
   */
  public void setUserEquipmentInfoType(UserEquipmentInfoType type);

  /**
   * Returns the value of the User-Equipment-Info-Type AVP, of type
   * Enumerated. A return value of null implies that the AVP has not been set.
   * <br>
   * See: {@link UserEquipmentInfoType}
   * 
   * @return
   */
  public UserEquipmentInfoType getUserEquipmentInfoType();

  /**
   * Returns true if the User-Equipment-Info-Type AVP is present in the
   * message. <br>
   * See: {@link UserEquipmentInfoType}
   * 
   * @return
   */
  public boolean hasUserEquipmentInfoType();

  /**
   * Sets the value of the User-Equipment-Info-Value AVP, of type OctetString.
   * 
   * @param value
   */
  public void setUserEquipmentInfoValue(byte[] value);

  /**
   * Returns the value of the User-Equipment-Info-Value AVP, of type
   * OctetString. A return value of null implies that the AVP has not been
   * set.
   * 
   * @return
   */
  public byte[] getUserEquipmentInfoValue();

  /**
   * Returns true if the User-Equipment-Info-Value AVP is present in the
   * message.
   * 
   * @return
   */
  public boolean hasUserEquipmentInfoValue();

}
