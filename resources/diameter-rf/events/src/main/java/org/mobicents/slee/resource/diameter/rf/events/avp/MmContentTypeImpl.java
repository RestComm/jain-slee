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

package org.mobicents.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.rf.events.avp.AdditionalContentInformation;
import net.java.slee.resource.diameter.rf.events.avp.MmContentType;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * MmContentTypeImpl.java
 *
 * <br>Project:  mobicents
 * <br>9:16:09 AM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MmContentTypeImpl extends GroupedAvpImpl implements MmContentType {

  public MmContentTypeImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public MmContentTypeImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#getAdditionalContentInformations()
   */
  public AdditionalContentInformation[] getAdditionalContentInformations() {
    return (AdditionalContentInformation[]) getAvpsAsCustom(DiameterRfAvpCodes.ADDITIONAL_TYPE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, AdditionalContentInformation.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#getAdditionalTypeInformation()
   */
  public String getAdditionalTypeInformation() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.ADDITIONAL_TYPE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#getContentSize()
   */
  public long getContentSize() {
    return getAvpAsUnsigned32(DiameterRfAvpCodes.CONTENT_SIZE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#getTypeNumber()
   */
  public int getTypeNumber() {
    return getAvpAsInteger32(DiameterRfAvpCodes.TYPE_NUMBER, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#hasAdditionalTypeInformation()
   */
  public boolean hasAdditionalTypeInformation() {
    return hasAvp( DiameterRfAvpCodes.ADDITIONAL_TYPE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#hasContentSize()
   */
  public boolean hasContentSize() {
    return hasAvp( DiameterRfAvpCodes.CONTENT_SIZE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#hasTypeNumber()
   */
  public boolean hasTypeNumber() {
    return hasAvp( DiameterRfAvpCodes.TYPE_NUMBER, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#setAdditionalContentInformation(net.java.slee.resource.diameter.rf.events.avp.AdditionalContentInformation)
   */
  public void setAdditionalContentInformation( AdditionalContentInformation additionalContentInformation ) {
    addAvp(DiameterRfAvpCodes.ADDITIONAL_CONTENT_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, additionalContentInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#setAdditionalContentInformations(net.java.slee.resource.diameter.rf.events.avp.AdditionalContentInformation[])
   */
  public void setAdditionalContentInformations( AdditionalContentInformation[] additionalContentInformations ) {
    for(AdditionalContentInformation additionalContentInformation : additionalContentInformations) {
      setAdditionalContentInformation(additionalContentInformation);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#setAdditionalTypeInformation(String)
   */
  public void setAdditionalTypeInformation( String additionalTypeInformation ) {
    addAvp(DiameterRfAvpCodes.ADDITIONAL_TYPE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, additionalTypeInformation);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#setContentSize(long)
   */
  public void setContentSize( long contentSize ) {
    addAvp(DiameterRfAvpCodes.CONTENT_SIZE, DiameterRfAvpCodes.TGPP_VENDOR_ID, contentSize);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.MmContentType#setTypeNumber(int)
   */
  public void setTypeNumber( int typeNumber ) {
    addAvp(DiameterRfAvpCodes.TYPE_NUMBER, DiameterRfAvpCodes.TGPP_VENDOR_ID, typeNumber);
  }

}
