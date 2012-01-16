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

package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.CurrentLocationRetrieved;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.MMELocationInformationAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link MMELocationInformationAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MMELocationInformationAvpImpl extends GroupedAvpImpl implements MMELocationInformationAvp {

  public MMELocationInformationAvpImpl() {
    super();
  }

  public MMELocationInformationAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasEUTRANCellGlobalIdentity() {
    return hasAvp(DiameterS6aAvpCodes.E_UTRAN_CELL_GLOBAL_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setEUTRANCellGlobalIdentity(byte[] eucgi) {
    addAvp(DiameterS6aAvpCodes.E_UTRAN_CELL_GLOBAL_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, eucgi);
  }

  public byte[] getEUTRANCellGlobalIdentity() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.E_UTRAN_CELL_GLOBAL_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasTrackingAreaIdentity() {
    return hasAvp(DiameterS6aAvpCodes.TRACKING_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setTrackingAreaIdentity(byte[] tai) {
    addAvp(DiameterS6aAvpCodes.TRACKING_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, tai);
  }

  public byte[] getTrackingAreaIdentity() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.TRACKING_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasGeographicalInformation() {
    return hasAvp(DiameterS6aAvpCodes.GEOGRAPHICAL_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setGeographicalInformation(byte[] gi) {
    addAvp(DiameterS6aAvpCodes.GEOGRAPHICAL_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, gi);
  }

  public byte[] getGeographicalInformation() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.GEOGRAPHICAL_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasGeodeticInformation() {
    return hasAvp(DiameterS6aAvpCodes.GEODETIC_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setGeodeticInformation(byte[] gi) {
    addAvp(DiameterS6aAvpCodes.GEODETIC_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, gi);
  }

  public byte[] getGeodeticInformation() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.GEODETIC_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasCurrentLocationRetrieved() {
    return hasAvp(DiameterS6aAvpCodes.CURRENT_LOCATION_RETRIEVED, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setCurrentLocationRetrieved(CurrentLocationRetrieved clr) {
    addAvp(DiameterS6aAvpCodes.CURRENT_LOCATION_RETRIEVED, DiameterS6aAvpCodes.S6A_VENDOR_ID, clr.getValue());
  }

  public CurrentLocationRetrieved getCurrentLocationRetrieved() {
    return (CurrentLocationRetrieved) getAvpAsEnumerated(DiameterS6aAvpCodes.CURRENT_LOCATION_RETRIEVED, DiameterS6aAvpCodes.S6A_VENDOR_ID, CurrentLocationRetrieved.class);
  }

  public boolean hasAgeOfLocationInformation() {
    return hasAvp(DiameterS6aAvpCodes.AGE_OF_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setAgeOfLocationInformation(long aoli) {
    addAvp(DiameterS6aAvpCodes.AGE_OF_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, aoli);
  }

  public long getAgeOfLocationInformation() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.AGE_OF_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

}
