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

package org.mobicents.slee.resource.diameter.cxdx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.cxdx.events.avp.AssociatedRegisteredIdentities;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * 
 * AssociatedRegisteredIdentitiesImpl.java
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AssociatedRegisteredIdentitiesImpl extends GroupedAvpImpl implements AssociatedRegisteredIdentities {

  public AssociatedRegisteredIdentitiesImpl() {
    super();
  }

  /**
   * 
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public AssociatedRegisteredIdentitiesImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.avp.AssociatedRegisteredIdentities#getUserNames()
   */
  public String[] getUserNames() {
    return (String[]) getAvpsAsUTF8String(DiameterAvpCodes.USER_NAME);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.avp.AssociatedRegisteredIdentities#setUserName(java.lang.String)
   */
  public void setUserName(String userName) {
    addAvp(DiameterAvpCodes.USER_NAME, userName);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.avp.AssociatedRegisteredIdentities#setUserNames(java.lang.String[])
   */
  public void setUserNames(String[] userNames) {
    for(String userName : userNames) {
      setUserName(userName);
    }
  }

}
