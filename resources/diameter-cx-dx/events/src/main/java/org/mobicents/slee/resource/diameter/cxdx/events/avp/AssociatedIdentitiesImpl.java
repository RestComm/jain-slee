/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.diameter.cxdx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.cxdx.events.avp.AssociatedIdentities;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * AssociatedIdentitiesImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AssociatedIdentitiesImpl extends GroupedAvpImpl implements AssociatedIdentities {

  public AssociatedIdentitiesImpl() {
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
  public AssociatedIdentitiesImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.AssociatedIdentities#getUserNames()
   */
  public String[] getUserNames() {
    return (String[]) getAvpsAsUTF8String(DiameterAvpCodes.USER_NAME);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.AssociatedIdentities#setUserName(java.lang.String)
   */
  public void setUserName(String userName) {
    addAvp(DiameterAvpCodes.USER_NAME, userName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.AssociatedIdentities#setUserNames(java.lang.String[])
   */
  public void setUserNames(String[] userNames) {
    for(String userName : userNames) {
      setUserName(userName);
    }
  }

}
