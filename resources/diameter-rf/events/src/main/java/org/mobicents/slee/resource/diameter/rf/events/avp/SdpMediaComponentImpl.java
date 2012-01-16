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

import net.java.slee.resource.diameter.rf.events.avp.MediaInitiatorFlag;
import net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent;

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
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getAuthorizedQos()
   */
  public String getAuthorizedQos() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.AUTHORIZED_QOS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getMediaInitiatorFlag()
   */
  public MediaInitiatorFlag getMediaInitiatorFlag() {
    return (MediaInitiatorFlag) getAvpAsEnumerated(DiameterRfAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRfAvpCodes.TGPP_VENDOR_ID, MediaInitiatorFlag.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getSdpMediaDescriptions()
   */
  public String[] getSdpMediaDescriptions() {
    return getAvpsAsUTF8String(DiameterRfAvpCodes.SDP_MEDIA_DESCRIPTION, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getSdpMediaName()
   */
  public String getSdpMediaName() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.SDP_MEDIA_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#getTgppChargingId()
   */
  public byte[] getTgppChargingId() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#hasAuthorizedQos()
   */
  public boolean hasAuthorizedQos() {
    return hasAvp(DiameterRfAvpCodes.AUTHORIZED_QOS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#hasMediaInitiatorFlag()
   */
  public boolean hasMediaInitiatorFlag() {
    return hasAvp(DiameterRfAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#hasSdpMediaName()
   */
  public boolean hasSdpMediaName() {
    return hasAvp(DiameterRfAvpCodes.SDP_MEDIA_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#hasTgppChargingId()
   */
  public boolean hasTgppChargingId() {
    return hasAvp(DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setAuthorizedQos(String)
   */
  public void setAuthorizedQos( String authorizedQos ) {
    addAvp(DiameterRfAvpCodes.AUTHORIZED_QOS, DiameterRfAvpCodes.TGPP_VENDOR_ID, authorizedQos);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setMediaInitiatorFlag(net.java.slee.resource.diameter.rf.events.avp.MediaInitiatorFlag)
   */
  public void setMediaInitiatorFlag( MediaInitiatorFlag mediaInitiatorFlag ) {
    addAvp(DiameterRfAvpCodes.MEDIA_INITIATOR_FLAG, DiameterRfAvpCodes.TGPP_VENDOR_ID, mediaInitiatorFlag.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setSdpMediaDescription(String)
   */
  public void setSdpMediaDescription( String sdpMediaDescription ) {
    addAvp(DiameterRfAvpCodes.SDP_MEDIA_DESCRIPTION, DiameterRfAvpCodes.TGPP_VENDOR_ID, sdpMediaDescription);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setSdpMediaDescriptions(String[])
   */
  public void setSdpMediaDescriptions( String[] sdpMediaDescriptions ) {
    for(String sdpMediaDescription : sdpMediaDescriptions) {
      setSdpMediaDescription(sdpMediaDescription);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setSdpMediaName(String)
   */
  public void setSdpMediaName( String sdpMediaName ) {
    addAvp(DiameterRfAvpCodes.SDP_MEDIA_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID, sdpMediaName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent#setTgppChargingId(byte[])
   */
  public void setTgppChargingId( byte[] tgppChargingId ) {
    addAvp(DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppChargingId);
  }

}
