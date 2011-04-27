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

import net.java.slee.resource.diameter.rf.events.avp.LcsClientId;
import net.java.slee.resource.diameter.rf.events.avp.LcsInformation;
import net.java.slee.resource.diameter.rf.events.avp.LocationType;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * LcsInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>11:49:03 AM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LcsInformationImpl extends GroupedAvpImpl implements LcsInformation {

  public LcsInformationImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public LcsInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#getLcsClientId()
   */
  public LcsClientId getLcsClientId() {
    return (LcsClientId) getAvpAsCustom(DiameterRfAvpCodes.LCS_CLIENT_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, LcsClientIdImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#getLocationEstimate()
   */
  public String getLocationEstimate() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.LOCATION_ESTIMATE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#getLocationType()
   */
  public LocationType getLocationType() {
    return (LocationType) getAvpAsCustom(DiameterRfAvpCodes.LOCATION_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, LocationTypeImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#hasLcsClientId()
   */
  public boolean hasLcsClientId() {
    return hasAvp( DiameterRfAvpCodes.LCS_CLIENT_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#hasLocationEstimate()
   */
  public boolean hasLocationEstimate() {
    return hasAvp( DiameterRfAvpCodes.LOCATION_ESTIMATE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#hasLocationType()
   */
  public boolean hasLocationType() {
    return hasAvp( DiameterRfAvpCodes.LOCATION_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#setLcsClientId(net.java.slee.resource.diameter.rf.events.avp.LcsClientId)
   */
  public void setLcsClientId( LcsClientId lcsClientId ) {
    addAvp(DiameterRfAvpCodes.LCS_CLIENT_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, lcsClientId.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#setLocationEstimate(String)
   */
  public void setLocationEstimate( String locationEstimate ) {
    addAvp(DiameterRfAvpCodes.LOCATION_ESTIMATE, DiameterRfAvpCodes.TGPP_VENDOR_ID, locationEstimate);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsInformation#setLocationType(net.java.slee.resource.diameter.rf.events.avp.LocationType)
   */
  public void setLocationType( LocationType locationType ) {
    addAvp(DiameterRfAvpCodes.LOCATION_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, locationType.byteArrayValue());
  }

}
