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
import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;
import net.java.slee.resource.diameter.gq.events.avp.FlowStatus;
import net.java.slee.resource.diameter.gq.events.avp.FlowUsage;
import net.java.slee.resource.diameter.gq.events.avp.MediaSubComponent;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;


/**
 * Implementation for {@link MediaSubComponent}
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class MediaSubComponentImpl extends GroupedAvpImpl implements MediaSubComponent {

  public MediaSubComponentImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public MediaSubComponentImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#getFlowNumber()
   */
  public long getFlowNumber() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_FLOW_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#getFlowDescriptions()
   */
  public IPFilterRule[] getFlowDescriptions() {
    return getAvpsAsIPFilterRule(DiameterGqAvpCodes.TGPP_FLOW_DESCRIPTION, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#getFlowStatus()
   */
  public FlowStatus getFlowStatus() {
    return (FlowStatus) getAvpAsEnumerated(DiameterGqAvpCodes.TGPP_FLOW_STATUS, DiameterGqAvpCodes.TGPP_VENDOR_ID, FlowStatus.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#getFlowUsage()
   */
  public FlowUsage getFlowUsage() {
    return (FlowUsage) getAvpAsEnumerated(DiameterGqAvpCodes.TGPP_FLOW_USAGE, DiameterGqAvpCodes.TGPP_VENDOR_ID, FlowUsage.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#getMaxRequestedBandwidthUL()
   */
  public long getMaxRequestedBandwidthUL() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_UL, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#getMaxRequestedBandwidthDL()
   */
  public long getMaxRequestedBandwidthDL() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_DL, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#hasFlowNumber()
   */
  public boolean hasFlowNumber() {
    return hasAvp(DiameterGqAvpCodes.TGPP_FLOW_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#hasFlowStatus()
   */
  public boolean hasFlowStatus() {
    return hasAvp(DiameterGqAvpCodes.TGPP_FLOW_STATUS, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#hasFlowUsage()
   */
  public boolean hasFlowUsage() {
    return hasAvp(DiameterGqAvpCodes.TGPP_FLOW_USAGE, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#hasMaxRequestedBandwidthUL()
   */
  public boolean hasMaxRequestedBandwidthUL() {
    return hasAvp(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_UL, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#hasMaxRequestedBandwidthDL()
   */
  public boolean hasMaxRequestedBandwidthDL() {
    return hasAvp(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_DL, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#setFlowNumber()
   */
  public void setFlowNumber(long flowNumber) {
    addAvp(DiameterGqAvpCodes.TGPP_FLOW_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID, flowNumber);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#setFlowDescriptions()
   */
  public void setFlowDescriptions(IPFilterRule[] flowDescriptions) {
    for (IPFilterRule flowDescription : flowDescriptions) {
      setFlowDescription(flowDescription);
    }

  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#setFlowDescription()
   */
  public void setFlowDescription(IPFilterRule flowDescription) {
    addAvp(AvpUtilities.createAvp(DiameterGqAvpCodes.TGPP_FLOW_DESCRIPTION, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        flowDescription.getRuleString()));
    // addAvp(DiameterGqAvpCodes.TGPP_FLOW_DESCRIPTION,DiameterGqAvpCodes.TGPP_VENDOR_ID, flowDescription.getRuleString());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#setFlowStatus()
   */
  public void setFlowStatus(FlowStatus flowStatus) {
    addAvp(DiameterGqAvpCodes.TGPP_FLOW_STATUS, DiameterGqAvpCodes.TGPP_VENDOR_ID, flowStatus.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#setFlowUsage()
   */
  public void setFlowUsage(FlowUsage flowUsage) {
    addAvp(DiameterGqAvpCodes.TGPP_FLOW_USAGE, DiameterGqAvpCodes.TGPP_VENDOR_ID, flowUsage.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#setMaxRequestedBandwidthUL()
   */
  public void setMaxRequestedBandwidthUL(long maxRequestedBandwidthUL) {
    addAvp(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_UL, DiameterGqAvpCodes.TGPP_VENDOR_ID, maxRequestedBandwidthUL);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see MediaSubComponent#setMaxRequestedBandwidthDL()
   */
  public void setMaxRequestedBandwidthDL(long maxRequestedBandwidthDL) {
    addAvp(DiameterGqAvpCodes.TGPP_MAX_REQUESTED_BANDWIDTH_DL, DiameterGqAvpCodes.TGPP_VENDOR_ID, maxRequestedBandwidthDL);
  }

}
