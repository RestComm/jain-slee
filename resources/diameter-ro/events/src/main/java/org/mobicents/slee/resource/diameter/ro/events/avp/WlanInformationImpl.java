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

package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.ro.events.avp.WlanInformation;
import net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * WlanInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>4:15:14 PM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class WlanInformationImpl extends GroupedAvpImpl implements WlanInformation {

  public WlanInformationImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public WlanInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#getPdgAddress()
   */
  public Address getPdgAddress() {
    return getAvpAsAddress(DiameterRoAvpCodes.PDG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#getPdgChargingId()
   */
  public long getPdgChargingId() {
    return getAvpAsUnsigned32(DiameterRoAvpCodes.PDG_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#getWagAddress()
   */
  public Address getWagAddress() {
    return getAvpAsAddress(DiameterRoAvpCodes.WAG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#getWagPlmnId()
   */
  public byte[] getWagPlmnId() {
    return getAvpAsOctetString(DiameterRoAvpCodes.WAG_PLMN_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#getWlanRadioContainer()
   */
  public WlanRadioContainer getWlanRadioContainer() {
    return (WlanRadioContainer) getAvpAsCustom(DiameterRoAvpCodes.WLAN_RADIO_CONTAINER, DiameterRoAvpCodes.TGPP_VENDOR_ID, WlanRadioContainerImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#getWlanSessionId()
   */
  public String getWlanSessionId() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.WLAN_SESSION_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#getWlanUeLocalIpaddress()
   */
  public Address getWlanUeLocalIpaddress() {
    return getAvpAsAddress(DiameterRoAvpCodes.WLAN_UE_LOCAL_IPADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#hasPdgAddress()
   */
  public boolean hasPdgAddress() {
    return hasAvp( DiameterRoAvpCodes.PDG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#hasPdgChargingId()
   */
  public boolean hasPdgChargingId() {
    return hasAvp( DiameterRoAvpCodes.PDG_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#hasWagAddress()
   */
  public boolean hasWagAddress() {
    return hasAvp( DiameterRoAvpCodes.WAG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#hasWagPlmnId()
   */
  public boolean hasWagPlmnId() {
    return hasAvp( DiameterRoAvpCodes.WAG_PLMN_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#hasWlanRadioContainer()
   */
  public boolean hasWlanRadioContainer() {
    return hasAvp( DiameterRoAvpCodes.WLAN_RADIO_CONTAINER, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#hasWlanSessionId()
   */
  public boolean hasWlanSessionId() {
    return hasAvp( DiameterRoAvpCodes.WLAN_SESSION_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#hasWlanUeLocalIpaddress()
   */
  public boolean hasWlanUeLocalIpaddress() {
    return hasAvp( DiameterRoAvpCodes.WLAN_UE_LOCAL_IPADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#setPdgAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setPdgAddress( Address pdgAddress ) {
    addAvp(DiameterRoAvpCodes.PDG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, pdgAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#setPdgChargingId(long)
   */
  public void setPdgChargingId( long pdgChargingId ) {
    addAvp(DiameterRoAvpCodes.PDG_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, pdgChargingId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#setWagAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setWagAddress( Address wagAddress ) {
    addAvp(DiameterRoAvpCodes.WAG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, wagAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#setWagPlmnId(byte[])
   */
  public void setWagPlmnId( byte[] wagPlmnId ) {
    addAvp(DiameterRoAvpCodes.WAG_PLMN_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, wagPlmnId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#setWlanRadioContainer(net.java.slee.resource.diameter.ro.events.avp.WlanRadioContainer)
   */
  public void setWlanRadioContainer( WlanRadioContainer wlanRadioContainer ) {
    addAvp(DiameterRoAvpCodes.WLAN_RADIO_CONTAINER, DiameterRoAvpCodes.TGPP_VENDOR_ID, wlanRadioContainer.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#setWlanSessionId(String)
   */
  public void setWlanSessionId( String wlanSessionId ) {
    addAvp(DiameterRoAvpCodes.WLAN_SESSION_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, wlanSessionId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.WlanInformation#setWlanUeLocalIpaddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setWlanUeLocalIpaddress( Address wlanUeLocalIpaddress ) {
    addAvp(DiameterRoAvpCodes.WLAN_UE_LOCAL_IPADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, wlanUeLocalIpaddress.encode());
  }

}
