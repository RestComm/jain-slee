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

import net.java.slee.resource.diameter.rf.events.avp.AddressDomain;
import net.java.slee.resource.diameter.rf.events.avp.AddressType;
import net.java.slee.resource.diameter.rf.events.avp.AddresseeType;
import net.java.slee.resource.diameter.rf.events.avp.RecipientAddress;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * RecipientAddressImpl.java
 *
 * <br>Project:  mobicents
 * <br>11:06:03 AM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RecipientAddressImpl extends GroupedAvpImpl implements RecipientAddress {

  public RecipientAddressImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public RecipientAddressImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#getAddressData()
   */
  public String getAddressData() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.ADDRESS_DATA, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#getAddressDomain()
   */
  public AddressDomain getAddressDomain() {
    return (AddressDomain) getAvpAsCustom(DiameterRfAvpCodes.ADDRESS_DOMAIN, DiameterRfAvpCodes.TGPP_VENDOR_ID, AddressDomainImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#getAddressType()
   */
  public AddressType getAddressType() {
    return (AddressType) getAvpAsEnumerated(DiameterRfAvpCodes.ADDRESS_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, AddressType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#getAddresseeType()
   */
  public AddresseeType getAddresseeType() {
    return (AddresseeType) getAvpAsEnumerated(DiameterRfAvpCodes.ADDRESSEE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, AddresseeType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#hasAddressData()
   */
  public boolean hasAddressData() {
    return hasAvp( DiameterRfAvpCodes.ADDRESS_DATA, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#hasAddressDomain()
   */
  public boolean hasAddressDomain() {
    return hasAvp( DiameterRfAvpCodes.ADDRESS_DOMAIN, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#hasAddressType()
   */
  public boolean hasAddressType() {
    return hasAvp( DiameterRfAvpCodes.ADDRESS_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#hasAddresseeType()
   */
  public boolean hasAddresseeType() {
    return hasAvp( DiameterRfAvpCodes.ADDRESSEE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#setAddressData(String)
   */
  public void setAddressData( String addressData ) {
    addAvp(DiameterRfAvpCodes.ADDRESS_DATA, DiameterRfAvpCodes.TGPP_VENDOR_ID, addressData);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#setAddressDomain(net.java.slee.resource.diameter.rf.events.avp.AddressDomain)
   */
  public void setAddressDomain( AddressDomain addressDomain ) {
    addAvp(DiameterRfAvpCodes.ADDRESS_DOMAIN, DiameterRfAvpCodes.TGPP_VENDOR_ID, addressDomain.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#setAddressType(net.java.slee.resource.diameter.rf.events.avp.AddressType)
   */
  public void setAddressType( AddressType addressType ) {
    addAvp(DiameterRfAvpCodes.ADDRESS_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, addressType.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.RecipientAddress#setAddresseeType(net.java.slee.resource.diameter.rf.events.avp.AddresseeType)
   */
  public void setAddresseeType( AddresseeType addressType ) {
    addAvp(DiameterRfAvpCodes.ADDRESSEE_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, addressType.getValue());
  }

}
