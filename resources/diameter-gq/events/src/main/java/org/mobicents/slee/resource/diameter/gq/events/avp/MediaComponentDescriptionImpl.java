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

package org.mobicents.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.gq.events.avp.FlowStatus;
import net.java.slee.resource.diameter.gq.events.avp.MediaComponentDescription;
import net.java.slee.resource.diameter.gq.events.avp.MediaSubComponent;
import net.java.slee.resource.diameter.gq.events.avp.MediaType;
import net.java.slee.resource.diameter.gq.events.avp.ReservationPriority;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;


/**
 * Implementation for {@link MediaComponentDescription}
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class MediaComponentDescriptionImpl extends GroupedAvpImpl implements MediaComponentDescription {

  public MediaComponentDescriptionImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public MediaComponentDescriptionImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getMediaComponentNumber()
   */
  public long getMediaComponentNumber() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getMediaSubComponents()
   */
  public MediaSubComponent[] getMediaSubComponents() {
    return (MediaSubComponent[]) getAvpsAsCustom(DiameterGqAvpCodes.TGPP_MEDIA_SUB_COMPONENT, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        MediaSubComponentImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getAFApplicationIdentifier()
   */
  public byte[] getAFApplicationIdentifier() {
    return getAvpAsOctetString(DiameterGqAvpCodes.TGPP_AF_APPLICATION_IDENTIFIER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getMediaType()
   */
  public MediaType getMediaType() {
    return (MediaType) getAvpAsEnumerated(DiameterGqAvpCodes.TGPP_MEDIA_TYPE, DiameterGqAvpCodes.TGPP_VENDOR_ID, MediaType.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getMaxRequestedBandwidthUL()
   */
  public long getMaxRequestedBandwidthUL() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_UL, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getMaxRequestedBandwidthDL()
   */
  public long getMaxRequestedBandwidthDL() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_DL, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getFlowStatus()
   */
  public FlowStatus getFlowStatus() {
    return (FlowStatus) getAvpAsEnumerated(DiameterGqAvpCodes.TGPP_FLOW_STATUS, DiameterGqAvpCodes.TGPP_VENDOR_ID, FlowStatus.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getRRBandwidth()
   */
  public long getRRBandwidth() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_RR_BANDWIDTH, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getRSBandwidth()
   */
  public long getRSBandwidth() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_RS_BANDWIDTH, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getReservationClass()
   */
  public long getReservationClass() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.ETSI_RESERVATION_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getReservationPriority()
   */
  public ReservationPriority getReservationPriority() {
    return (ReservationPriority) getAvpAsEnumerated(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        ReservationPriority.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getTransportClass()
   */
  public long getTransportClass() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.ETSI_TRANSPORT_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getCodecData()
   */
  public byte[][] getCodecData() {
    return (byte[][]) getAvpsAsOctetString(DiameterGqAvpCodes.TGPP_CODEC_DATA, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#getMediaAuthorizationContextId()
   */
  public String[] getMediaAuthorizationContextId() {
    return (String[]) getAvpsAsUTF8String(DiameterGqAvpCodes.ETSI_MEDIA_AUTHORIZATION_CONTEXT_ID, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasMediaComponentNumber()
   */
  public boolean hasMediaComponentNumber() {
    return hasAvp(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasAFApplicationIdentifier()
   */
  public boolean hasAFApplicationIdentifier() {
    return hasAvp(DiameterGqAvpCodes.TGPP_AF_APPLICATION_IDENTIFIER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasMediaType()
   */
  public boolean hasMediaType() {
    return hasAvp(DiameterGqAvpCodes.TGPP_MEDIA_TYPE, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasMaxRequestedBandwidthUL()
   */
  public boolean hasMaxRequestedBandwidthUL() {
    return hasAvp(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_UL, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasMaxRequestedBandwidthDL()
   */
  public boolean hasMaxRequestedBandwidthDL() {
    return hasAvp(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_DL, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasFlowStatus()
   */
  public boolean hasFlowStatus() {
    return hasAvp(DiameterGqAvpCodes.TGPP_FLOW_STATUS, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasRRBandwidth()
   */
  public boolean hasRRBandwidth() {
    return hasAvp(DiameterGqAvpCodes.TGPP_RR_BANDWIDTH, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasRSBandwidth()
   */
  public boolean hasRSBandwidth() {
    return hasAvp(DiameterGqAvpCodes.TGPP_RS_BANDWIDTH, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasReservationClass()
   */
  public boolean hasReservationClass() {
    return hasAvp(DiameterGqAvpCodes.ETSI_RESERVATION_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasReservationPriority()
   */
  public boolean hasReservationPriority() {
    return hasAvp(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#hasTransportClass()
   */
  public boolean hasTransportClass() {
    return hasAvp(DiameterGqAvpCodes.ETSI_TRANSPORT_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setMediaComponentNumber()
   */
  public void setMediaComponentNumber(long mediaComponentNumber) {
    addAvp(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID, mediaComponentNumber);

  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setMediaSubComponents()
   */
  public void setMediaSubComponents(MediaSubComponent[] mediaSubComponents) {
    for (MediaSubComponent mediaSubComponent : mediaSubComponents) {
      setMediaSubComponent(mediaSubComponent);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setMediaSubComponent()
   */
  public void setMediaSubComponent(MediaSubComponent mediaSubComponent) {
    // addAvp(DiameterGqAvpCodes.TGPP_MEDIA_SUB_COMPONENT, DiameterGqAvpCodes.TGPP_VENDOR_ID, mediaSubComponent.byteArrayValue());
    addAvp(mediaSubComponent);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setAFApplicationIdentifier()
   */
  public void setAFApplicationIdentifier(byte[] AFApplicationIdentifier) {
    addAvp(DiameterGqAvpCodes.TGPP_AF_APPLICATION_IDENTIFIER, DiameterGqAvpCodes.TGPP_VENDOR_ID, AFApplicationIdentifier);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setMediaType()
   */
  public void setMediaType(MediaType mediaType) {
    addAvp(DiameterGqAvpCodes.TGPP_MEDIA_TYPE, DiameterGqAvpCodes.TGPP_VENDOR_ID, mediaType.getValue());

  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setMaxRequestedBandwidthUL()
   */
  public void setMaxRequestedBandwidthUL(long maxRequestedBandwidthUL) {
    addAvp(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_UL, DiameterGqAvpCodes.TGPP_VENDOR_ID, maxRequestedBandwidthUL);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setMaxRequestedBandwidthDL()
   */
  public void setMaxRequestedBandwidthDL(long maxRequestedBandwidthDL) {
    addAvp(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_DL, DiameterGqAvpCodes.TGPP_VENDOR_ID, maxRequestedBandwidthDL);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setFlowStatus()
   */
  public void setFlowStatus(FlowStatus flowStatus) {
    addAvp(DiameterGqAvpCodes.TGPP_FLOW_STATUS, DiameterGqAvpCodes.TGPP_VENDOR_ID, flowStatus.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setRSBandwidth()
   */
  public void setRSBandwidth(long RSBandwidth) {
    addAvp(DiameterGqAvpCodes.TGPP_RS_BANDWIDTH, DiameterGqAvpCodes.TGPP_VENDOR_ID, RSBandwidth);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setRRBandwidth()
   */
  public void setRRBandwidth(long RRBandwidth) {
    addAvp(DiameterGqAvpCodes.TGPP_RR_BANDWIDTH, DiameterGqAvpCodes.TGPP_VENDOR_ID, RRBandwidth);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setReservationClass()
   */
  public void setReservationClass(long reservationClass) {
    addAvp(DiameterGqAvpCodes.ETSI_RESERVATION_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID, reservationClass);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setReservationPriority()
   */
  public void setReservationPriority(ReservationPriority reservationPriority) {
    addAvp(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID, reservationPriority.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setTransportClass()
   */
  public void setTransportClass(long transportClass) {
    addAvp(DiameterGqAvpCodes.ETSI_TRANSPORT_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID, transportClass);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setCodecData()
   */
  public void setCodecData(byte[][] codecsData) {
    for (byte[] codecData : codecsData) {
      setCodecData(codecData);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setCodecData()
   */
  public void setCodecData(byte[] codecData) {
    addAvp(AvpUtilities.createAvp(DiameterGqAvpCodes.TGPP_CODEC_DATA, DiameterGqAvpCodes.TGPP_VENDOR_ID, codecData));
    // addAvp(DiameterGqAvpCodes.TGPP_CODEC_DATA, DiameterGqAvpCodes.TGPP_VENDOR_ID, codecData);

  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setMediaAuthorizationContextId()
   */
  public void setMediaAuthorizationContextId(String[] mediaAuthorizationContextIds) {
    for (String mediaAuthorizationContextId : mediaAuthorizationContextIds) {
      setMediaAuthorizationContextId(mediaAuthorizationContextId);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaComponentDescription#setMediaAuthorizationContextId()
   */
  public void setMediaAuthorizationContextId(String mediaAuthorizationContextId) {
    addAvp(AvpUtilities.createAvp(DiameterGqAvpCodes.ETSI_MEDIA_AUTHORIZATION_CONTEXT_ID, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        mediaAuthorizationContextId));
    // addAvp(DiameterGqAvpCodes.ETSI_MEDIA_AUTHORIZATION_CONTEXT_ID, DiameterGqAvpCodes.ETSI_VENDOR_ID, mediaAuthorizationContextId);
  }
}
