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
import net.java.slee.resource.diameter.cca.events.avp.RedirectAddressType;
import net.java.slee.resource.diameter.cca.events.avp.RedirectServerAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Start time:17:27:03 2008-11-10<br>
 * Project: mobicents-diameter-parent<br>
 * Implementation of AVP: {@link RedirectServerAvp}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RedirectServerAvpImpl extends GroupedAvpImpl implements RedirectServerAvp {

  public RedirectServerAvpImpl() {
    super();
  }

  public RedirectServerAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.RedirectServerAvp#getRedirectAddressType()
   */
  public RedirectAddressType getRedirectAddressType() {
    return (RedirectAddressType) getAvpAsEnumerated(CreditControlAVPCodes.Redirect_Address_Type, RedirectAddressType.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @seenet.java.slee.resource.diameter.cca.events.avp.RedirectServerAvp#
   * getRedirectServerAddress()
   */
  public String getRedirectServerAddress() {
    return getAvpAsUTF8String(CreditControlAVPCodes.Redirect_Server_Address);
  }

  /*
   * (non-Javadoc)
   * 
   * @seenet.java.slee.resource.diameter.cca.events.avp.RedirectServerAvp#
   * hasRedirectAddressType()
   */
  public boolean hasRedirectAddressType() {
    return hasAvp(CreditControlAVPCodes.Redirect_Address_Type);
  }

  /*
   * (non-Javadoc)
   * 
   * @seenet.java.slee.resource.diameter.cca.events.avp.RedirectServerAvp#
   * hasRedirectServerAddress()
   */
  public boolean hasRedirectServerAddress() {
    return hasAvp(CreditControlAVPCodes.Redirect_Server_Address);
  }

  /*
   * (non-Javadoc)
   * 
   * @seenet.java.slee.resource.diameter.cca.events.avp.RedirectServerAvp#
   * setRedirectAddressType
   * (net.java.slee.resource.diameter.cca.events.avp.RedirectAddressType)
   */
  public void setRedirectAddressType(RedirectAddressType redirectAddressType) {
    addAvp(CreditControlAVPCodes.Redirect_Address_Type, redirectAddressType.getValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @seenet.java.slee.resource.diameter.cca.events.avp.RedirectServerAvp#
   * setRedirectServerAddress(java.lang.String)
   */
  public void setRedirectServerAddress(String redirectServerAddress) {
    addAvp(CreditControlAVPCodes.Redirect_Server_Address, redirectServerAddress);
  }

}
