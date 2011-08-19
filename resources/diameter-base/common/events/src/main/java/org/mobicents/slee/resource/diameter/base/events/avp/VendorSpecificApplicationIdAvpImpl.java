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

package org.mobicents.slee.resource.diameter.base.events.avp;

import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;

import org.jdiameter.api.Avp;

/**
 * 
 * Implementation of {@link VendorSpecificApplicationIdAvp} interface
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class VendorSpecificApplicationIdAvpImpl extends GroupedAvpImpl implements VendorSpecificApplicationIdAvp {

  public VendorSpecificApplicationIdAvpImpl() {
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
  public VendorSpecificApplicationIdAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#getVendorIds()
   */
  public long[] getVendorIdsAvp() {
    return getAvpsAsUnsigned32(Avp.VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#setVendorId(long)
   */
  public void setVendorIdAvp(long vendorId) {
    addAvp(Avp.VENDOR_ID, vendorId);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#setVendorIds(long[])
   */
  public void setVendorIdsAvp(long[] vendorIds) {
    DiameterAvp[] values = new DiameterAvp[vendorIds.length];

    for(int index = 0; index < vendorIds.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.VENDOR_ID, vendorIds[index]);
    }

    avpSet.removeAvp(Avp.VENDOR_ID);
    this.setExtensionAvps(values);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#hasAuthApplicationId()
   */
  public boolean hasAuthApplicationId() {
    return hasAvp(Avp.AUTH_APPLICATION_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#getAuthApplicationId()
   */
  public long getAuthApplicationId() {
    return getAvpAsUnsigned32(Avp.AUTH_APPLICATION_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#setAuthApplicationId(long)
   */
  public void setAuthApplicationId(long authApplicationId) {
    addAvp(Avp.AUTH_APPLICATION_ID, authApplicationId);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#hasAcctApplicationId()
   */
  public boolean hasAcctApplicationId() {
    return hasAvp(Avp.ACCT_APPLICATION_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#getAcctApplicationId()
   */
  public long getAcctApplicationId() {
    return getAvpAsUnsigned32(Avp.ACCT_APPLICATION_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp#setAcctApplicationId(long)
   */
  public void setAcctApplicationId(long acctApplicationId) {
    addAvp(Avp.ACCT_APPLICATION_ID, acctApplicationId);
  }
}
