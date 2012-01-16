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

package org.mobicents.slee.resource.diameter.cca.events.avp;

import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp;
import net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoType;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Start time:20:36:25 2008-11-10<br>
 * Project: mobicents-diameter-parent<br>
 * Implementation of AVP: {@link UserEquipmentInfoAvp}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UserEquipmentInfoAvpImpl extends GroupedAvpImpl implements UserEquipmentInfoAvp {

  public UserEquipmentInfoAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp#getUserEquipmentInfoType()
   */
  public UserEquipmentInfoType getUserEquipmentInfoType() {
    return (UserEquipmentInfoType) getAvpAsEnumerated(CreditControlAVPCodes.User_Equipment_Info_Type, UserEquipmentInfoType.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp#getUserEquipmentInfoValue()
   */
  public byte[] getUserEquipmentInfoValue() {
    return getAvpAsOctetString(CreditControlAVPCodes.User_Equipment_Info_Value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp#hasUserEquipmentInfoType()
   */
  public boolean hasUserEquipmentInfoType() {
    return hasAvp(CreditControlAVPCodes.User_Equipment_Info_Type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp#hasUserEquipmentInfoValue()
   */
  public boolean hasUserEquipmentInfoValue() {
    return hasAvp(CreditControlAVPCodes.User_Equipment_Info_Value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp#setUserEquipmentInfoType
   * (net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoType)
   */
  public void setUserEquipmentInfoType(UserEquipmentInfoType type) {
    addAvp(CreditControlAVPCodes.User_Equipment_Info_Type, type.getValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp#setUserEquipmentInfoValue(byte[])
   */
  public void setUserEquipmentInfoValue(byte[] value) {
    addAvp(CreditControlAVPCodes.User_Equipment_Info_Value, value);
  }

}
