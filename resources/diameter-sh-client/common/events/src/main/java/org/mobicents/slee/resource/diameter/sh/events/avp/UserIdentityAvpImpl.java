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

package org.mobicents.slee.resource.diameter.sh.events.avp;

import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * 
 * Implementation of AVP: {@link UserIdentityAvp} UserIdentityAvp.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UserIdentityAvpImpl extends GroupedAvpImpl implements UserIdentityAvp {

  public UserIdentityAvpImpl() {
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
  public UserIdentityAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp#getMsisdn()
   */
  public String getMsisdn() {
    return getAvpAsUTF8String(DiameterShAvpCodes.MSISDN, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp#getPublicIdentity()
   */
  public String getPublicIdentity() {
    return getAvpAsUTF8String(DiameterShAvpCodes.PUBLIC_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp#hasMsisdn()
   */
  public boolean hasMsisdn() {
    return hasAvp(DiameterShAvpCodes.MSISDN, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp#hasPublicIdentity()
   */
  public boolean hasPublicIdentity() {
    return hasAvp(DiameterShAvpCodes.PUBLIC_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp#setMsisdn(java.lang.String)
   */
  public void setMsisdn(String msisdn) {
    addAvp(DiameterShAvpCodes.MSISDN, DiameterShAvpCodes.SH_VENDOR_ID, msisdn);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp#setPublicIdentity(java.lang.String)
   */
  public void setPublicIdentity(String publicIdentity) {
    addAvp(DiameterShAvpCodes.PUBLIC_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, publicIdentity);
  }

}
