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
import net.java.slee.resource.diameter.cca.events.avp.UnitValueAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Start time:18:40:53 2008-11-10<br>
 * Project: mobicents-diameter-parent<br>
 * 
 * Implementation of AVP: {@link UnitValueAvp}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UnitValueAvpImpl extends GroupedAvpImpl implements UnitValueAvp {

  public UnitValueAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UnitValueAvp#getExpotent()
   */
  public int getExponent() {
    return getAvpAsInteger32(CreditControlAVPCodes.Exponent);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UnitValueAvp#getValueDigits()
   */
  public long getValueDigits() {
    return getAvpAsInteger64(CreditControlAVPCodes.Value_Digits);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UnitValueAvp#hasExponent()
   */
  public boolean hasExponent() {
    return hasAvp(CreditControlAVPCodes.Exponent);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UnitValueAvp#hasValueDigits()
   */
  public boolean hasValueDigits() {
    return hasAvp(CreditControlAVPCodes.Value_Digits);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UnitValueAvp#setExpotent(int)
   */
  public void setExponent(int exponent) {
    addAvp(CreditControlAVPCodes.Exponent, exponent);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.UnitValueAvp#setValueDigits(long)
   */
  public void setValueDigits(long digits) {
    addAvp(CreditControlAVPCodes.Value_Digits, digits);
  }

}
