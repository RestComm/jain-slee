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
import net.java.slee.resource.diameter.s6a.events.avp.SGSNLocationInformationAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link SGSNLocationInformationAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SGSNLocationInformationAvpImpl extends GroupedAvpImpl implements SGSNLocationInformationAvp {

  public SGSNLocationInformationAvpImpl() {
    super();
  }

  public SGSNLocationInformationAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasCellGlobalIdentity() {
    return hasAvp(DiameterS6aAvpCodes.CELL_GLOBAL_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setCellGlobalIdentity(byte[] eucgi) {
    addAvp(DiameterS6aAvpCodes.CELL_GLOBAL_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, eucgi);
  }

  public byte[] getCellGlobalIdentity() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.CELL_GLOBAL_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasLocationAreaIdentity() {
    return hasAvp(DiameterS6aAvpCodes.LOCATION_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setLocationAreaIdentity(byte[] lai) {
    addAvp(DiameterS6aAvpCodes.LOCATION_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, lai);
  }

  public byte[] getLocationAreaIdentity() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.LOCATION_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasServiceAreaIdentity() {
    return hasAvp(DiameterS6aAvpCodes.SERVICE_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setServiceAreaIdentity(byte[] sai) {
    addAvp(DiameterS6aAvpCodes.SERVICE_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, sai);
  }

  public byte[] getServiceAreaIdentity() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.SERVICE_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasRoutingAreaIdentity() {
    return hasAvp(DiameterS6aAvpCodes.ROUTING_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setRoutingAreaIdentity(byte[] rai) {
    addAvp(DiameterS6aAvpCodes.ROUTING_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, rai);
  }

  public byte[] getRoutingAreaIdentity() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.ROUTING_AREA_IDENTITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
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
