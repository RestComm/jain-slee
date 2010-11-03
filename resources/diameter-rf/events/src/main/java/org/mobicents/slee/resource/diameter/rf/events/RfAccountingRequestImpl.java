/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */package org.mobicents.slee.resource.diameter.rf.events;

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
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#getServiceInformation()
    */
   @Override
   public ServiceInformation getServiceInformation() {
     return (ServiceInformation) super.getAvpAsCustom(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, ServiceInformationImpl.class);
   }

   /*
    * (non-Javadoc)
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#setServiceInformation(net.java.slee.resource.diameter.rf.events.avp.ServiceInformation)
    */
   @Override
   public void setServiceInformation(ServiceInformation si) {
     super.addAvp(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, si.byteArrayValue());

   }

   /*
    * (non-Javadoc)
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#hasServiceInformation()
    */
   @Override
   public boolean hasServiceInformation() {
     return super.hasAvp(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID);
   }

   /*
    * (non-Javadoc)
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#getCalledStationId()
    */
   @Override
   public String getCalledStationId() {
     return super.getAvpAsUTF8String(DiameterRfAvpCodes.CALLED_STATION_ID);
   }

   /*
    * (non-Javadoc)
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#setCalledStationId(java.lang.String)
    */
   @Override
   public void setCalledStationId(String csid) {
     super.addAvp(DiameterRfAvpCodes.CALLED_STATION_ID, csid);

   }

   /*
    * (non-Javadoc)
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#hasCalledStationId()
    */
   @Override
   public boolean hasCalledStationId() {
     return super.hasAvp(DiameterRfAvpCodes.CALLED_STATION_ID);
   }

   /*
    * (non-Javadoc)
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#getLocationType()
    */
   @Override
   public LocationType getLocationType() {
     return (LocationType)super.getAvpAsCustom(DiameterRfAvpCodes.LOCATION_TYPE, LocationTypeImpl.class);
   }

   /*
    * (non-Javadoc)
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#setLocationType(net.java.slee.resource.diameter.rf.events.avp.LocationType)
    */
   @Override
   public void setLocationType(LocationType lt) {
     super.addAvp(DiameterRfAvpCodes.LOCATION_TYPE, lt.byteArrayValue());
   }

   /*
    * (non-Javadoc)
    * @see net.java.slee.resource.diameter.rf.events.RfAccountingRequest#hasLocationType()
    */
   @Override
   public boolean hasLocationType() {
     return super.hasAvp(DiameterRfAvpCodes.LOCATION_TYPE);
   }

   //  @Override
   //  public byte[] getLocationInformation() {
   //    return super.getAvpAsRaw(DiameterRfAvpCodes.LOCATION_INFORMATION);
   //  }
   //
   //  @Override
   //  public void setLocationInformation(byte[] si) {
   //    super.addAvp(DiameterRfAvpCodes.LOCATION_INFORMATION,si);
   //  }
   //
   //  @Override
   //  public boolean hasLocationInformation() {
   //    return super.hasAvp(DiameterRfAvpCodes.LOCATION_INFORMATION);
   //  }
   //
   //  @Override
   //  public byte[] getOperatorName() {
   //    return super.getAvpAsRaw(DiameterRfAvpCodes.OPERATOR_NAME);
   //  }
   //
   //  @Override
   //  public void setOperatorName(byte[] si) {
   //    super.addAvp(DiameterRfAvpCodes.OPERATOR_NAME,si);
   //  }
   //
   //  @Override
   //  public boolean hasOperatorName() {
   //    return super.hasAvp(DiameterRfAvpCodes.OPERATOR_NAME);
   //  }

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
