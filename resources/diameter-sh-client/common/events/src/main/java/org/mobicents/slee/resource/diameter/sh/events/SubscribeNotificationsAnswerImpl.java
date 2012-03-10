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

package org.mobicents.slee.resource.diameter.sh.events;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.util.Date;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ObjectFactory;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactoryImpl;

/**
 * 
 * Implementation of {@link SubscribeNotificationsAnswer} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SubscribeNotificationsAnswerImpl extends DiameterShMessageImpl implements SubscribeNotificationsAnswer {

  private static final long serialVersionUID = -66848235143082970L;

  private static JAXBContext jaxbContext = initJAXBContext();
  private static UserDataObjectFactory udof = new UserDataObjectFactoryImpl(new ObjectFactory());

  private static JAXBContext initJAXBContext() {
    try {
      return JAXBContext.newInstance(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData.class);
    }
    catch (Exception e) {
      // we can't throw exception
      return null;
    }
  }
  
  /**
   * 
   * @param msg
   */
  public SubscribeNotificationsAnswerImpl(Message msg) {
    super(msg);
    msg.setRequest(false);
    msg.setReTransmitted(false); // just in case. answers never have T flag set
    super.longMessageName = "Subscribe-Notification-Answer";
    super.shortMessageName = "SNA";
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#getExpiryTime()
   */
  public Date getExpiryTime() {
    return getAvpAsTime(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#hasExpiryTime()
   */
  public boolean hasExpiryTime() {
    return hasAvp(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#setExpiryTime(java.util.Date)
   */
  public void setExpiryTime(Date expiryTime) {
    addAvp(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID, expiryTime);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#hasExperimentalResult()
   */
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#getExperimentalResult()
   */
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(Avp.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#setExperimentalResult(net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp)
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) {
    addAvp(Avp.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#hasUserData()
   */
  public boolean hasUserData() {
    return hasAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#getUserData()
   */
  public byte[] getUserData() {
    return getAvpAsRaw(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#getUserDataObject()
   */
  public ShData getUserDataObject() throws IOException {
    ShData shDataObject = null;
    try {
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

      byte[] data = getAvpAsRaw(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
      JAXBElement<TShData> jaxbElem = udof.createShData((TShData) unmarshaller.unmarshal(new ByteArrayInputStream(data)));
      shDataObject = (ShData) jaxbElem.getValue();
    }
    catch (Exception e) {
      throw new IOException("Failed to unmarshal User-Data AVP into JAXB Object", e);
    }
    return shDataObject;
  }
  
  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#setUserData(byte[])
   */
  public void setUserData(byte[] userData) {
    addAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID, userData);
  }
  
  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#setUserData(byte[])
   */
  public void setUserDataObject(ShData userData) throws IOException {
    try {
      Marshaller marshaller = jaxbContext.createMarshaller();

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      marshaller.marshal(userData, baos);
      addAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID, baos.toByteArray());
    }
    catch (Exception e) {
      throw new IOException("Failed to marshal JAXB Object to User-Data AVP", e);
    }
  }
  
  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#hasWildcardedPSI()
   */
  public boolean hasWildcardedPSI() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#getWildcardedPSI()
   */
  public String getWildcardedPSI() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#setWildcardedPSI(String)
   */
  public void setWildcardedPSI(String wildcardedPSI) {
    addAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedPSI);
  }
  
  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#hasWildcardedIMPU()
   */
  public boolean hasWildcardedIMPU() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#getWildcardedIMPU()
   */
  public String getWildcardedIMPU() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#setWildcardedIMPU(String)
   */
  public void setWildcardedIMPU(String wildcardedIMPU) {
    addAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedIMPU);
  }
}
