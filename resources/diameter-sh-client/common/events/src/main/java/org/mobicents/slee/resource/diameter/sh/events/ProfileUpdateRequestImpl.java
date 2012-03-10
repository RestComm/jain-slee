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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.sh.events.DiameterShMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.UserIdentityAvpImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ObjectFactory;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TApplicationServer;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TCSLocationInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TChargingInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TDSAI;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.THeader;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TIFCs;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TISDNAddress;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TInitialFilterCriteria;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPSLocationInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentity;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentityExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentityExtension2;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSePoTri;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSePoTriExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TServiceData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSessionDescription;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShDataExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShDataExtension2;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension2;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension3;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TTransparentData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TTrigger;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactoryImpl;

/**
 * 
 * Implementation of {@link ProfileUpdateRequest} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileUpdateRequestImpl extends DiameterShMessageImpl implements ProfileUpdateRequest {

  private static final long serialVersionUID = -5829214729454907100L;

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
  public ProfileUpdateRequestImpl(Message msg) {
    super(msg);

    msg.setRequest(true);

    super.longMessageName = "Profile-Update-Request";
    super.shortMessageName = "PUR";
  }

  /*
   *  (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#getDataReference()
   */
  public DataReferenceType getDataReference() {
    return (DataReferenceType) getAvpAsEnumerated(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, DataReferenceType.class);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#getUserIdentity()
   */
  public UserIdentityAvp getUserIdentity() {
    return (UserIdentityAvp) getAvpAsCustom(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, UserIdentityAvpImpl.class);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#hasDataReference()
   */
  public boolean hasDataReference() {
    return hasAvp(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#hasUserData()
   */
  public boolean hasUserData() {
    return hasAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#hasUserIdentity()
   */
  public boolean hasUserIdentity() {
    return hasAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#setDataReference(net.java.slee.resource.diameter.sh.events.avp.DataReferenceType)
   */
  public void setDataReference(DataReferenceType dataReference) {
    addAvp(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, dataReference.getValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#setUserIdentity(net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp)
   */
  public void setUserIdentity(UserIdentityAvp userIdentity)
  {
    addAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, userIdentity.byteArrayValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#getUserData()
   */
  public byte[] getUserData() {
    return getAvpAsOctetString(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#getUserDataObject()
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
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateRequest#setUserData(java.lang.String)
   */
  public void setUserData(byte[] userData) {
    addAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID, userData);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#setUserDataObject(net.java.slee.resource.diameter.sh.events.avp.userdata.ShData)
   */
  public void setUserDataObject(ShData userData) throws IOException {
    setUserDataObject(userData, new Class[]{});
  }

  /**
   * Method allowing to set user data as object and passing custom classes for the JAXB context
   * 
   * @param userData the user data object
   * @param classes the class(es) to be added to the JAXB context
   * @throws IOException
   */
  public void setUserDataObject(ShData userData, Class<?>... classes) throws IOException {
    try {

      List<Class<?>> ctxClasses = new ArrayList<Class<?>>();
      ctxClasses.add(ObjectFactory.class);
      ctxClasses.add(TIFCs.class);
      ctxClasses.add(TSePoTri.class);
      ctxClasses.add(TShIMSData.class);
      ctxClasses.add(TApplicationServer.class);
      ctxClasses.add(TISDNAddress.class);
      ctxClasses.add(TSePoTriExtension.class);
      ctxClasses.add(TShIMSDataExtension.class);
      ctxClasses.add(TCSLocationInformation.class);
      ctxClasses.add(TInitialFilterCriteria.class);
      ctxClasses.add(TServiceData.class);
      ctxClasses.add(TShIMSDataExtension2.class);
      ctxClasses.add(TChargingInformation.class);
      ctxClasses.add(TPSLocationInformation.class);
      ctxClasses.add(TSessionDescription.class);
      ctxClasses.add(TShIMSDataExtension3.class);
      ctxClasses.add(TDSAI.class);
      ctxClasses.add(TPublicIdentity.class);
      ctxClasses.add(TShData.class);
      ctxClasses.add(TTransparentData.class);
      ctxClasses.add(TExtension.class);
      ctxClasses.add(TPublicIdentityExtension.class);
      ctxClasses.add(TShDataExtension.class);
      ctxClasses.add(TTrigger.class);
      ctxClasses.add(THeader.class);
      ctxClasses.add(TPublicIdentityExtension2.class);
      ctxClasses.add(TShDataExtension2.class);
      //ctxClasses.add(UserDataObjectFactoryImpl.class);

      for(Class<?> clazz : classes) {
        ctxClasses.add(clazz);
      }

      Marshaller marshaller =  JAXBContext.newInstance(ctxClasses.toArray(new Class[ctxClasses.size()])).createMarshaller();

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
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#hasWildcardedPSI()
   */
  public boolean hasWildcardedPSI() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#getWildcardedPSI()
   */
  public String getWildcardedPSI() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#setWildcardedPSI(String)
   */
  public void setWildcardedPSI(String wildcardedPSI) {
    addAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedPSI);
  }
  
  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#hasWildcardedIMPU()
   */
  public boolean hasWildcardedIMPU() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#getWildcardedIMPU()
   */
  public String getWildcardedIMPU() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#setWildcardedIMPU(String)
   */
  public void setWildcardedIMPU(String wildcardedIMPU) {
    addAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedIMPU);
  }
}
