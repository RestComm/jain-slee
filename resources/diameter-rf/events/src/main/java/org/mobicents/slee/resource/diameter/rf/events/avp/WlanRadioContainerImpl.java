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

package org.mobicents.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.rf.events.avp.LocationType;
import net.java.slee.resource.diameter.rf.events.avp.WlanRadioContainer;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * WlanRadioContainerImpl.java
 *
 * <br>Project:  mobicents
 * <br>4:33:06 PM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class WlanRadioContainerImpl extends GroupedAvpImpl implements WlanRadioContainer {

  public WlanRadioContainerImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public WlanRadioContainerImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.WlanRadioContainer#getLocationType()
   */
  public LocationType getLocationType() {
    return (LocationType) getAvpAsCustom(DiameterRfAvpCodes.LOCATION_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, LocationTypeImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.WlanRadioContainer#getWlanTechnology()
   */
  public long getWlanTechnology() {
    return getAvpAsUnsigned32(DiameterRfAvpCodes.WLAN_TECHNOLOGY, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.WlanRadioContainer#hasLocationType()
   */
  public boolean hasLocationType() {
    return hasAvp( DiameterRfAvpCodes.LOCATION_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.WlanRadioContainer#hasWlanTechnology()
   */
  public boolean hasWlanTechnology() {
    return hasAvp( DiameterRfAvpCodes.WLAN_TECHNOLOGY, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.WlanRadioContainer#setLocationType(net.java.slee.resource.diameter.rf.events.avp.LocationType)
   */
  public void setLocationType( LocationType locationType ) {
    addAvp(DiameterRfAvpCodes.LOCATION_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, locationType.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.WlanRadioContainer#setWlanTechnology(long)
   */
  public void setWlanTechnology( long wlanTechnology ) {
    addAvp(DiameterRfAvpCodes.WLAN_TECHNOLOGY, DiameterRfAvpCodes.TGPP_VENDOR_ID, wlanTechnology);
  }

}
