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

import net.java.slee.resource.diameter.ro.events.avp.MediaInitiatorFlag;
import net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * 
 * SdpMediaComponentImpl.java
 *
 * <br>Project:  mobicents
 * <br>11:19:56 PM Apr 11, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SdpMediaComponentImpl extends GroupedAvpImpl implements SdpMediaComponent {

  public SdpMediaComponentImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public SdpMediaComponentImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#getAuthorizedQos()
   */
  public String getAuthorizedQos() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.AUTHORIZED_QOS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#getMediaInitiatorFlag()
   */
  public MediaInitiatorFlag getMediaInitiatorFlag() {
    return (MediaInitiatorFlag) getAvpAsEnumerated(DiameterRoAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRoAvpCodes.TGPP_VENDOR_ID, MediaInitiatorFlag.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#getSdpMediaDescriptions()
   */
  public String[] getSdpMediaDescriptions() {
    return getAvpsAsUTF8String(DiameterRoAvpCodes.SDP_MEDIA_DESCRIPTION, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#getSdpMediaName()
   */
  public String getSdpMediaName() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.SDP_MEDIA_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#getTgppChargingId()
   */
  public byte[] getTgppChargingId() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#hasAuthorizedQos()
   */
  public boolean hasAuthorizedQos() {
    return hasAvp(DiameterRoAvpCodes.AUTHORIZED_QOS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#hasMediaInitiatorFlag()
   */
  public boolean hasMediaInitiatorFlag() {
    return hasAvp(DiameterRoAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#hasSdpMediaName()
   */
  public boolean hasSdpMediaName() {
    return hasAvp(DiameterRoAvpCodes.SDP_MEDIA_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#hasTgppChargingId()
   */
  public boolean hasTgppChargingId() {
    return hasAvp(DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#setAuthorizedQos(String)
   */
  public void setAuthorizedQos( String authorizedQos ) {
    addAvp(DiameterRoAvpCodes.AUTHORIZED_QOS, DiameterRoAvpCodes.TGPP_VENDOR_ID, authorizedQos);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#setMediaInitiatorFlag(net.java.slee.resource.diameter.ro.events.avp.MediaInitiatorFlag)
   */
  public void setMediaInitiatorFlag( MediaInitiatorFlag mediaInitiatorFlag ) {
    addAvp(DiameterRoAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRoAvpCodes.TGPP_VENDOR_ID, mediaInitiatorFlag.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#setSdpMediaDescription(String)
   */
  public void setSdpMediaDescription( String sdpMediaDescription ) {
    addAvp(DiameterRoAvpCodes.SDP_MEDIA_DESCRIPTION, DiameterRoAvpCodes.TGPP_VENDOR_ID, sdpMediaDescription);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#setSdpMediaDescriptions(String[])
   */
  public void setSdpMediaDescriptions( String[] sdpMediaDescriptions ) {
    for(String sdpMediaDescription : sdpMediaDescriptions) {
      setSdpMediaDescription(sdpMediaDescription);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#setSdpMediaName(String)
   */
  public void setSdpMediaName( String sdpMediaName ) {
    addAvp(DiameterRoAvpCodes.SDP_MEDIA_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID, sdpMediaName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.SdpMediaComponent#setTgppChargingId(byte[])
   */
  public void setTgppChargingId( byte[] tgppChargingId ) {
    addAvp(DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppChargingId);
  }

}
