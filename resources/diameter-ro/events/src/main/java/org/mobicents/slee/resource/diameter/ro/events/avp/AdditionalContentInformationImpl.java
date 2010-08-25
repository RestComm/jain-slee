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
 */
package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * 
 * AdditionalContentInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>8:17:22 PM Apr 10, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class AdditionalContentInformationImpl extends GroupedAvpImpl implements AdditionalContentInformation {

  public AdditionalContentInformationImpl() {
    super();
  }

  public AdditionalContentInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#getAdditionalTypeInformation()
   */
  public String getAdditionalTypeInformation() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.ADDITIONAL_TYPE_INFORMATION, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#getContentSize()
   */
  public long getContentSize() {
    return getAvpAsUnsigned32(DiameterRoAvpCodes.CONTENT_SIZE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#getTypeNumber()
   */
  public int getTypeNumber() {
    return getAvpAsInteger32(DiameterRoAvpCodes.TYPE_NUMBER, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#hasAdditionalTypeInformation()
   */
  public boolean hasAdditionalTypeInformation() {
    return hasAvp(DiameterRoAvpCodes.ADDITIONAL_TYPE_INFORMATION, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#hasContentSize()
   */
  public boolean hasContentSize() {
    return hasAvp(DiameterRoAvpCodes.CONTENT_SIZE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#hasTypeNumber()
   */
  public boolean hasTypeNumber() {
    return hasAvp(DiameterRoAvpCodes.TYPE_NUMBER, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#setAdditionalTypeInformation(String)
   */
  public void setAdditionalTypeInformation( String additionalTypeInformation ) {
    addAvp(DiameterRoAvpCodes.ADDITIONAL_TYPE_INFORMATION, DiameterRoAvpCodes.TGPP_VENDOR_ID, additionalTypeInformation);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#setContentSize(long)
   */
  public void setContentSize( long contentSize ) {
    addAvp(DiameterRoAvpCodes.CONTENT_SIZE, DiameterRoAvpCodes.TGPP_VENDOR_ID, contentSize);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.AdditionalContentInformation#setTypeNumber(int)
   */
  public void setTypeNumber( int typeNumber ) {
    addAvp(DiameterRoAvpCodes.TYPE_NUMBER, DiameterRoAvpCodes.TGPP_VENDOR_ID, typeNumber);
  }

}
