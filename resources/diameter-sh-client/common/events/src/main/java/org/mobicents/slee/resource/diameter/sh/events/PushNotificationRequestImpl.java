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

import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.sh.events.avp.UserIdentityAvpImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ObjectFactory;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactoryImpl;

/**
 * 
 * Implementation of {@link PushNotificationRequest} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class PushNotificationRequestImpl extends DiameterShMessageImpl implements PushNotificationRequest {

  private static final long serialVersionUID = 3454964740059530884L;

  private static JAXBContext jaxbContext = initJAXBContext();
  private static UserDataObjectFactory udof = new UserDataObjectFactoryImpl(new ObjectFactory());

  private static JAXBContext initJAXBContext() {
    try {
      return JAXBContext.newInstance(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData.class);
    }
    catch (Exception e) {
    	e.printStackTrace();
      // we can't throw exception
      return null;
    }
  }

  /**
   * 
   * @param msg
   */
  public PushNotificationRequestImpl(Message msg) {
    super(msg);
    msg.setRequest(true);
    super.longMessageName = "Push-Notification-Request";
    super.shortMessageName = "PNR";
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#getUserIdentity()
   */
  public UserIdentityAvp getUserIdentity() {
    return (UserIdentityAvp) getAvpAsCustom(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, UserIdentityAvpImpl.class);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#hasUserIdentity()
   */
  public boolean hasUserIdentity() {
    return hasAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#setUserIdentity(net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp)
   */
  public void setUserIdentity(UserIdentityAvp userIdentity) {
    addAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, userIdentity.byteArrayValue());
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#hasUserData()
   */
  public boolean hasUserData() {
    return hasAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#getUserData()
   */
  public byte[] getUserData() {
    return getAvpAsOctetString(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#getUserDataObject()
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
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#setUserData(java.lang.String)
   */
  public void setUserData(byte[] userData) {
    addAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID, userData);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#setUserDataObject(net.java.slee.resource.diameter.sh.events.avp.userdata.ShData)
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
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#hasWildcardedPSI()
   */
  public boolean hasWildcardedPSI() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#getWildcardedPSI()
   */
  public String getWildcardedPSI() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#setWildcardedPSI(String)
   */
  public void setWildcardedPSI(String wildcardedPSI) {
    addAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedPSI);
  }
  
  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#hasWildcardedIMPU()
   */
  public boolean hasWildcardedIMPU() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#getWildcardedIMPU()
   */
  public String getWildcardedIMPU() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.PushNotificationRequest#setWildcardedIMPU(String)
   */
  public void setWildcardedIMPU(String wildcardedIMPU) {
    addAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedIMPU);
  }
}
