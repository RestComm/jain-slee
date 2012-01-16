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

package org.mobicents.slee.resource.diameter.rf.events;

import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;
import net.java.slee.resource.diameter.rf.events.avp.LocationType;
import net.java.slee.resource.diameter.rf.events.avp.ServiceInformation;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.rf.events.avp.DiameterRfAvpCodes;
import org.mobicents.slee.resource.diameter.rf.events.avp.LocationTypeImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.ServiceInformationImpl;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RfAccountingRequestImpl extends RfAccountingMessageImpl implements RfAccountingRequest {

  /**
   * @param message
   */
  public RfAccountingRequestImpl(Message message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest# getServiceInformation()
   */
  @Override
  public ServiceInformation getServiceInformation() {
    return (ServiceInformation) super.getAvpAsCustom(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, ServiceInformationImpl.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest# setServiceInformation
   * (net.java.slee.resource.diameter.rf.events.avp.ServiceInformation)
   */
  @Override
  public void setServiceInformation(ServiceInformation si) {
    super.addAvp(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, si.byteArrayValue());

  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest# hasServiceInformation()
   */
  @Override
  public boolean hasServiceInformation() {
    return super.hasAvp(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest# getCalledStationId()
   */
  @Override
  public String getCalledStationId() {
    return super.getAvpAsUTF8String(DiameterRfAvpCodes.CALLED_STATION_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest# setCalledStationId(java.lang.String)
   */
  @Override
  public void setCalledStationId(String csid) {
    super.addAvp(DiameterRfAvpCodes.CALLED_STATION_ID, csid);

  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest# hasCalledStationId()
   */
  @Override
  public boolean hasCalledStationId() {
    return super.hasAvp(DiameterRfAvpCodes.CALLED_STATION_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#getLocationType ()
   */
  @Override
  public LocationType getLocationType() {
    return (LocationType) super.getAvpAsCustom(DiameterRfAvpCodes.LOCATION_TYPE, LocationTypeImpl.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#setLocationType
   * (net.java.slee.resource.diameter.rf.events.avp.LocationType)
   */
  @Override
  public void setLocationType(LocationType lt) {
    super.addAvp(DiameterRfAvpCodes.LOCATION_TYPE, lt.byteArrayValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#hasLocationType ()
   */
  @Override
  public boolean hasLocationType() {
    return super.hasAvp(DiameterRfAvpCodes.LOCATION_TYPE);
  }

  @Override
  public String getLongName() {
    // return "Rf-Accounting-Request";
    return "Accounting-Request";
  }

  @Override
  public String getShortName() {
    return "ACR";
  }

}
