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
 * Start time:14:12:35 2009-05-23<br>
 * Project: diameter-parent<br>
 * Super avp for avps of certain structure:
 * 
 * <pre>
 *                HEADER NAME ::= &lt; AVP Header: CODE &gt;
 *                                   { Unit-Value }
 *                                   [ Currency-Code ]
 * </pre>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MoneyLikeAvpImpl extends GroupedAvpImpl {

  public MoneyLikeAvpImpl()	{
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
  public MoneyLikeAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.CcMoneyAvp#getCurrencyCode()
   */
  public long getCurrencyCode() {
    return getAvpAsUnsigned32(CreditControlAVPCodes.Currency_Code);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.CcMoneyAvp#getUnitValue()
   */
  public UnitValueAvp getUnitValue() {
    return (UnitValueAvp) getAvpAsCustom(CreditControlAVPCodes.Unit_Value, UnitValueAvpImpl.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.CcMoneyAvp#hasCurrencyCode()
   */
  public boolean hasCurrencyCode() {
    return hasAvp(CreditControlAVPCodes.Currency_Code);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.CcMoneyAvp#hasUnitValue()
   */
  public boolean hasUnitValue() {
    return hasAvp(CreditControlAVPCodes.Unit_Value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.CcMoneyAvp#setCurrencyCode(long)
   */
  public void setCurrencyCode(long code) {
    addAvp(CreditControlAVPCodes.Currency_Code, code);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.CcMoneyAvp#setUnitValue(net.java.slee.resource.diameter.cca.events.avp.UnitValueAvp)
   */
  public void setUnitValue(UnitValueAvp unitValue) {
    addAvp(CreditControlAVPCodes.Unit_Value, unitValue.byteArrayValue());
  }
}
