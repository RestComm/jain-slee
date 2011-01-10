/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.slee.resource.diameter.sh.events;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.sh.events.DiameterShMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.UserIdentityAvpImpl;

/**
 * 
 * Implementation of {@link ProfileUpdateRequest} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileUpdateRequestImpl extends DiameterShMessageImpl implements ProfileUpdateRequest {

  private static JAXBContext jaxbContext = initJAXBContext();

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
    addAvp(DiameterShAvpCodes.DATA_REFERENCE, DiameterShAvpCodes.SH_VENDOR_ID, (long)dataReference.getValue());
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
  public String getUserData() {
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
      JAXBElement jaxbElem = (JAXBElement) unmarshaller.unmarshal(new ByteArrayInputStream(data));
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
  public void setUserData(String userData) {
    addAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID, userData);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest#setUserDataObject(net.java.slee.resource.diameter.sh.events.avp.userdata.ShData)
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

}
