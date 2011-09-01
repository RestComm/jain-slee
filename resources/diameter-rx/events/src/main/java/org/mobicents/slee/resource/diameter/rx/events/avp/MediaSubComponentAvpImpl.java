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

import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;
import net.java.slee.resource.diameter.rx.events.avp.AFSignalingProtocol;
import net.java.slee.resource.diameter.rx.events.avp.FlowStatus;
import net.java.slee.resource.diameter.rx.events.avp.FlowUsage;
import net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation of {@link MediaSubComponentAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class MediaSubComponentAvpImpl extends GroupedAvpImpl implements MediaSubComponentAvp {

  /**
   * 
   */
  public MediaSubComponentAvpImpl() {
    super.code = DiameterRxAvpCodes.MEDIA_SUBCOMPONENT;
    super.vendorId = DiameterRxAvpCodes.TGPP_VENDOR_ID;
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public MediaSubComponentAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#getFlowNumber()
   */
  @Override
  public long getFlowNumber() {
    return super.getAvpAsUnsigned32(DiameterRxAvpCodes.FLOW_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#hasFlowNumber()
   */
  @Override
  public boolean hasFlowNumber() {
    return super.hasAvp(DiameterRxAvpCodes.FLOW_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#setFlowNumber(long)
   */
  @Override
  public void setFlowNumber(long flowNumber) {
    super.addAvp(DiameterRxAvpCodes.FLOW_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID,flowNumber);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#getFlowDescriptions()
   */
  @Override
  public IPFilterRule[] getFlowDescriptions() {
    return (IPFilterRule[]) super.getAvpsAsIPFilterRule(DiameterRxAvpCodes.FLOW_DESCRIPTION,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#setFlowDescriptions(net.java.slee.resource.diameter.base.events.avp.IPFilterRule[])
   */
  @Override
  public void setFlowDescriptions(IPFilterRule[] flowDescriptions) {
    for(IPFilterRule f:flowDescriptions)
      super.addAvp(DiameterRxAvpCodes.FLOW_DESCRIPTION,DiameterRxAvpCodes.TGPP_VENDOR_ID,f);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#setFlowDescription(net.java.slee.resource.diameter.base.events.avp.IPFilterRule)
   */
  @Override
  public void setFlowDescription(IPFilterRule flowDescription) {
    super.addAvp(DiameterRxAvpCodes.FLOW_DESCRIPTION,DiameterRxAvpCodes.TGPP_VENDOR_ID,flowDescription);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#hasFlowDesription()
   */
  @Override
  public boolean hasFlowDesription() {
    return super.hasAvp(DiameterRxAvpCodes.FLOW_DESCRIPTION,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#hasFlowStatus()
   */
  @Override
  public boolean hasFlowStatus() {
    return super.hasAvp(DiameterRxAvpCodes.FLOW_STATUS,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#setFlowStatus(net.java.slee.resource.diameter.rx.events.avp.FlowStatus)
   */
  @Override
  public void setFlowStatus(FlowStatus fs) {
    super.addAvp(DiameterRxAvpCodes.FLOW_STATUS,DiameterRxAvpCodes.TGPP_VENDOR_ID,fs.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#getFlowStatus()
   */
  @Override
  public FlowStatus getFlowStatus() {
    return (FlowStatus)super.getAvpAsEnumerated(DiameterRxAvpCodes.FLOW_STATUS,DiameterRxAvpCodes.TGPP_VENDOR_ID, FlowStatus.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#hasFlowUsage()
   */
  @Override
  public boolean hasFlowUsage() {
    return super.hasAvp(DiameterRxAvpCodes.FLOW_USAGE,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#setFlowUsage(net.java.slee.resource.diameter.rx.events.avp.FlowUsage)
   */
  @Override
  public void setFlowUsage(FlowUsage fu) {
    super.addAvp(DiameterRxAvpCodes.FLOW_USAGE,DiameterRxAvpCodes.TGPP_VENDOR_ID,fu.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#getFlowUsage()
   */
  @Override
  public FlowUsage getFlowUsage() {
    return (FlowUsage) super.getAvpAsEnumerated(DiameterRxAvpCodes.FLOW_USAGE,DiameterRxAvpCodes.TGPP_VENDOR_ID, FlowUsage.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#hasMaxRequestedBandwidthDL()
   */
  @Override
  public boolean hasMaxRequestedBandwidthDL() {
    return super.hasAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#setMaxRequestedBandwidthDL(long)
   */
  @Override
  public void setMaxRequestedBandwidthDL(long mrbdl) {
    super.addAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL,DiameterRxAvpCodes.TGPP_VENDOR_ID,mrbdl);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#getMaxRequestedBandwidthDL()
   */
  @Override
  public long getMaxRequestedBandwidthDL() {
    return super.getAvpAsUnsigned32(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#hasMaxRequestedBandwidthUL()
   */
  @Override
  public boolean hasMaxRequestedBandwidthUL() {
    return super.hasAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#setMaxRequestedBandwidthUL(long)
   */
  @Override
  public void setMaxRequestedBandwidthUL(long mrbUL) {
    super.addAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL,DiameterRxAvpCodes.TGPP_VENDOR_ID,mrbUL);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#getMaxRequestedBandwidthUL()
   */
  @Override
  public long getMaxRequestedBandwidthUL() {
    return super.getAvpAsUnsigned32(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#hasAFSignalingProtocol()
   */
  @Override
  public boolean hasAFSignalingProtocol() {
    return super.hasAvp(DiameterRxAvpCodes.AF_SIGNALLING_PROTOCOL,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#setAFSignalingProtocol(net.java.slee.resource.diameter.rx.events.avp.AFSignalingProtocol)
   */
  @Override
  public void setAFSignalingProtocol(AFSignalingProtocol afsp) {
    super.addAvp(DiameterRxAvpCodes.AF_SIGNALLING_PROTOCOL,DiameterRxAvpCodes.TGPP_VENDOR_ID, afsp.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp#getAFSignalingProtocol()
   */
  @Override
  public AFSignalingProtocol getAFSignalingProtocol() {
    return (AFSignalingProtocol) super.getAvpAsEnumerated(DiameterRxAvpCodes.AF_SIGNALLING_PROTOCOL,DiameterRxAvpCodes.TGPP_VENDOR_ID, AFSignalingProtocol.class);
  }

}
