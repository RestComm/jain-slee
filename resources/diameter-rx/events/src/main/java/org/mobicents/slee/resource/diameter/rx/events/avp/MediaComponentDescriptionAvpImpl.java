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

package org.mobicents.slee.resource.diameter.rx.events.avp;

import net.java.slee.resource.diameter.rx.events.avp.FlowStatus;
import net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp;
import net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp;
import net.java.slee.resource.diameter.rx.events.avp.MediaType;
import net.java.slee.resource.diameter.rx.events.avp.ReservationPriority;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.DiameterRxAvpCodes;

/**
 * Implementation of {@link MediaComponentDescriptionAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class MediaComponentDescriptionAvpImpl extends GroupedAvpImpl implements MediaComponentDescriptionAvp {

  /**
   * 
   */
  public MediaComponentDescriptionAvpImpl() {
    code = DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION;
    vendorId = DiameterRxAvpCodes.TGPP_VENDOR_ID;
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public MediaComponentDescriptionAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasMediaComponentNumber()
   */
  @Override
  public boolean hasMediaComponentNumber() {
    return hasAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_NUMBER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setMediaComponentNumber(long)
   */
  @Override
  public void setMediaComponentNumber(long l) {
    addAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_NUMBER, DiameterRxAvpCodes.TGPP_VENDOR_ID,l);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getMediaComponentNumber()
   */
  @Override
  public long getMediaComponentNumber() {
    return getAvpAsUnsigned32(DiameterRxAvpCodes.MEDIA_COMPONENT_NUMBER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasMediaSubComponent()
   */
  @Override
  public boolean hasMediaSubComponent() {
    return hasAvp(DiameterRxAvpCodes.MEDIA_SUBCOMPONENT, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setMediaSubComponent(net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp)
   */
  @Override
  public void setMediaSubComponent(MediaSubComponentAvp msc) {
    addAvp(DiameterRxAvpCodes.MEDIA_SUBCOMPONENT, DiameterRxAvpCodes.TGPP_VENDOR_ID,msc.getExtensionAvps());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setMediaSubComponents(net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp[])
   */
  @Override
  public void setMediaSubComponents(MediaSubComponentAvp[] mscs) {
    //addAvp(DiameterRxAvpCodes.MEDIA_SUBCOMPONENT, DiameterRxAvpCodes.TGPP_VENDOR_ID,mscs);
    setExtensionAvps(mscs);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getMediaSubComponents()
   */
  @Override
  public MediaSubComponentAvp[] getMediaSubComponents() {
    return (MediaSubComponentAvp[]) getAvpsAsCustom(DiameterRxAvpCodes.MEDIA_SUBCOMPONENT, DiameterRxAvpCodes.TGPP_VENDOR_ID, MediaSubComponentAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setAFChargingIdentifier(java.lang.String)
   */
  @Override
  public void setAFChargingIdentifier(byte[] afAppId) {
    addAvp(DiameterRxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID,afAppId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasAFChargingIdentifier()
   */
  @Override
  public boolean hasAFChargingIdentifier() {
    return hasAvp(DiameterRxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getAFChargingIdentifier()
   */
  @Override
  public byte[] getAFChargingIdentifier() {
    return getAvpAsOctetString(DiameterRxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasMediaType()
   */
  @Override
  public boolean hasMediaType() {
    return hasAvp(DiameterRxAvpCodes.MEDIA_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setMediaType(net.java.slee.resource.diameter.rx.events.avp.MediaType)
   */
  @Override
  public void setMediaType(MediaType mediaType) {
    addAvp(DiameterRxAvpCodes.MEDIA_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID,mediaType.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getMediaType()
   */
  @Override
  public MediaType getMediaType() {
    return (MediaType) getAvpAsEnumerated(DiameterRxAvpCodes.MEDIA_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID, MediaType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasMaxRequestedBandwidthDL()
   */
  @Override
  public boolean hasMaxRequestedBandwidthDL() {
    return hasAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setMaxRequestedBandwidthDL(long)
   */
  @Override
  public void setMaxRequestedBandwidthDL(long mrbdl) {
    addAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterRxAvpCodes.TGPP_VENDOR_ID,mrbdl);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getMaxRequestedBandwidthDL()
   */
  @Override
  public long getMaxRequestedBandwidthDL() {
    return getAvpAsUnsigned32(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasMaxRequestedBandwidthUL()
   */
  @Override
  public boolean hasMaxRequestedBandwidthUL() {
    return hasAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setMaxRequestedBandwidthUL(long)
   */
  @Override
  public void setMaxRequestedBandwidthUL(long mrbUL) {
    addAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterRxAvpCodes.TGPP_VENDOR_ID,mrbUL);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getMaxRequestedBandwidthUL()
   */
  @Override
  public long getMaxRequestedBandwidthUL() {
    return getAvpAsUnsigned32(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getFlowStatus()
   */
  @Override
  public FlowStatus getFlowStatus() {
    return (FlowStatus) getAvpAsEnumerated(DiameterRxAvpCodes.FLOW_STATUS, DiameterRxAvpCodes.TGPP_VENDOR_ID, FlowStatus.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasFlowStatus()
   */
  @Override
  public boolean hasFlowStatus() {
    return hasAvp(DiameterRxAvpCodes.FLOW_STATUS, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setFlowStatus(net.java.slee.resource.diameter.rx.events.avp.FlowStatus)
   */
  @Override
  public void setFlowStatus(FlowStatus flowStatus) {
    addAvp(DiameterRxAvpCodes.FLOW_STATUS, DiameterRxAvpCodes.TGPP_VENDOR_ID,flowStatus.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getReservationPriority()
   */
  @Override
  public ReservationPriority getReservationPriority() {
    return (ReservationPriority)getAvpAsEnumerated(DiameterRxAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterRxAvpCodes.ETSI_VENDOR_ID, ReservationPriority.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setReservationPriority(net.java.slee.resource.diameter.rx.events.avp.ReservationPriority)
   */
  @Override
  public void setReservationPriority(ReservationPriority reservationPriority) throws IllegalStateException {
    addAvp(DiameterRxAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterRxAvpCodes.ETSI_VENDOR_ID,reservationPriority.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasReservationPriority()
   */
  @Override
  public boolean hasReservationPriority() {
    return hasAvp(DiameterRxAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterRxAvpCodes.ETSI_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasRSBandwidth()
   */
  @Override
  public boolean hasRSBandwidth() {
    return hasAvp(DiameterRxAvpCodes.RS_BANDWIDTH, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setRSBandwidth(long)
   */
  @Override
  public void setRSBandwidth(long mrbUL) {
    addAvp(DiameterRxAvpCodes.RS_BANDWIDTH, DiameterRxAvpCodes.TGPP_VENDOR_ID,mrbUL);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getRSBandwidth()
   */
  @Override
  public long getRSBandwidth() {
    return getAvpAsUnsigned32(DiameterRxAvpCodes.RS_BANDWIDTH, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#hasRRBandwidth()
   */
  @Override
  public boolean hasRRBandwidth() {
    return hasAvp(DiameterRxAvpCodes.RR_BANDWIDTH, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setRRBandwidth(long)
   */
  @Override
  public void setRRBandwidth(long mrbUL) {
    addAvp(DiameterRxAvpCodes.RR_BANDWIDTH, DiameterRxAvpCodes.TGPP_VENDOR_ID,mrbUL);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#getRRBandwidth()
   */
  @Override
  public long getRRBandwidth() {
    return getAvpAsUnsigned32(DiameterRxAvpCodes.RR_BANDWIDTH, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp#setCodecDatas(byte[][])
   */
  @Override
  public void setCodecDatas(byte[][] codecDatas) {
    for(byte[] codecData : codecDatas) {
      addAvp(DiameterRxAvpCodes.CODEC_DATA, DiameterRxAvpCodes.TGPP_VENDOR_ID, codecData);
    }
  }

  @Override
  public byte[][] getCodecDatas() {
    return getAvpsAsOctetString(DiameterRxAvpCodes.CODEC_DATA, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

}
