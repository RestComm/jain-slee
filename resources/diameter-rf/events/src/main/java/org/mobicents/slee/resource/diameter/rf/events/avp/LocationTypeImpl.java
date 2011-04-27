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

import net.java.slee.resource.diameter.rf.events.avp.LocationEstimateType;
import net.java.slee.resource.diameter.rf.events.avp.LocationType;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;


/**
 * LocationTypeImpl.java
 *
 * <br>Project:  mobicents
 * <br>12:00:23 PM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LocationTypeImpl extends GroupedAvpImpl implements LocationType {

  public LocationTypeImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public LocationTypeImpl( int code, long vendorId, int mnd, int prt, byte[] value )
  {
    super( code, vendorId, mnd, prt, value );
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LocationType#getDeferredLocationEventType()
   */
  public String getDeferredLocationEventType() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.DEFERRED_LOCATION_EVENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LocationType#getLocationEstimateType()
   */
  public LocationEstimateType getLocationEstimateType() {
    return (LocationEstimateType) getAvpAsEnumerated(DiameterRfAvpCodes.LOCATION_ESTIMATE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, LocationEstimateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LocationType#hasDeferredLocationEventType()
   */
  public boolean hasDeferredLocationEventType() {
    return hasAvp( DiameterRfAvpCodes.DEFERRED_LOCATION_EVENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LocationType#hasLocationEstimateType()
   */
  public boolean hasLocationEstimateType() {
    return hasAvp( DiameterRfAvpCodes.LOCATION_ESTIMATE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LocationType#setDeferredLocationEventType(String)
   */
  public void setDeferredLocationEventType( String deferredLocationEventType ) {
    addAvp(DiameterRfAvpCodes.DEFERRED_LOCATION_EVENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, deferredLocationEventType);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LocationType#setLocationEstimateType(net.java.slee.resource.diameter.rf.events.avp.LocationEstimateType)
   */
  public void setLocationEstimateType( LocationEstimateType locationEstimateType ) {
    addAvp(DiameterRfAvpCodes.LOCATION_ESTIMATE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, locationEstimateType.getValue());
  }

}
