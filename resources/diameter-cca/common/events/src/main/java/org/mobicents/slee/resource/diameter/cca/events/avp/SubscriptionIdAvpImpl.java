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
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdType;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Start time:18:25:13 2008-11-10<br>
 * Project: mobicents-diameter-parent<br>
 * Implementation of AVP: {@link SubscriptionIdAvp}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SubscriptionIdAvpImpl extends GroupedAvpImpl implements SubscriptionIdAvp {

  public SubscriptionIdAvpImpl() {
    super();
  }

  public SubscriptionIdAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp#getSubscriptionIdData()
   */
  public String getSubscriptionIdData() {
    return getAvpAsUTF8String(CreditControlAVPCodes.Subscription_Id_Data);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp#getSubscriptionIdType()
   */
  public SubscriptionIdType getSubscriptionIdType() {
    return (SubscriptionIdType) getAvpAsEnumerated(CreditControlAVPCodes.Subscription_Id_Type, SubscriptionIdType.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp#hasSubscriptionIdData()
   */
  public boolean hasSubscriptionIdData() {
    return hasAvp(CreditControlAVPCodes.Subscription_Id_Data);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp#hasSubscriptionIdType()
   */
  public boolean hasSubscriptionIdType() {
    return hasAvp(CreditControlAVPCodes.Subscription_Id_Type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp#setSubscriptionIdData(java.lang.String)
   */
  public void setSubscriptionIdData(String data) {
    addAvp(CreditControlAVPCodes.Subscription_Id_Data, data);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp#setSubscriptionIdType(net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdType)
   */
  public void setSubscriptionIdType(SubscriptionIdType type) {
    addAvp(CreditControlAVPCodes.Subscription_Id_Type, type.getValue());
  }

}
