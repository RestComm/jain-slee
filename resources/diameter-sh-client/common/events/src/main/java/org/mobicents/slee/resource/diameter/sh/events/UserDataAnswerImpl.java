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

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.sh.events.UserDataAnswer;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;

/**
 * 
 * Implementation of {@link UserDataAnswer} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UserDataAnswerImpl extends DiameterShMessageImpl implements UserDataAnswer {

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
  public UserDataAnswerImpl(Message msg) {
    super(msg);
    msg.setRequest(false);
    super.longMessageName = "User-Data-Answer";
    super.shortMessageName = "UDA";
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.UserDataAnswer#hasUserData()
   */
  public boolean hasUserData() {
    return hasAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.UserDataAnswer#getUserData()
   */
  public String getUserData() {
    return getAvpAsOctetString(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.UserDataAnswer#getUserDataObject()
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
   * @see net.java.slee.resource.diameter.sh.events.UserDataAnswer#setUserData(byte[])
   */
  public void setUserData(String userData) {
    addAvp(DiameterShAvpCodes.USER_DATA, DiameterShAvpCodes.SH_VENDOR_ID, userData);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.UserDataAnswer#setUserData(byte[])
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
   * @see net.java.slee.resource.diameter.sh.events.UserDataAnswer#hasExperimentalResult()
   */
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.UserDataAnswer#getExperimentalResult()
   */
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(Avp.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.UserDataAnswer#setExperimentalResult(net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp)
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) {
    addAvp(Avp.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

}
