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

import net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp;
import net.java.slee.resource.diameter.rx.events.avp.FlowsAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation of {@link AccessNetworkChargingIdentifierAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class AccessNetworkChargingIdentifierAvpImpl extends GroupedAvpImpl implements AccessNetworkChargingIdentifierAvp {

  /**
   * 
   */
  public AccessNetworkChargingIdentifierAvpImpl() {
    code = DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER;
    vendorId = DiameterRxAvpCodes.TGPP_VENDOR_ID;
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public AccessNetworkChargingIdentifierAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp#hasAccessNetworkChargingIdentifier()
   */
  @Override
  public boolean hasAccessNetworkChargingIdentifierValue() {
    return hasAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER_VALUE, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp#setAccessNetworkChargingIdentifier(java.lang.String)
   */
  @Override
  public void setAccessNetworkChargingIdentifierValue(byte[] anci) {
    addAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER_VALUE, DiameterRxAvpCodes.TGPP_VENDOR_ID, anci);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp#getAccessNetworkChargingIdentifier()
   */
  @Override
  public byte[] getAccessNetworkChargingIdentifierValue() {
    return getAvpAsOctetString(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER_VALUE, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp#hasFlows()
   */
  @Override
  public boolean hasFlows() {
    return hasAvp(DiameterRxAvpCodes.FLOWS, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp#getFlows()
   */
  @Override
  public FlowsAvp[] getFlows() {
    return (FlowsAvp[])getAvpsAsCustom(DiameterRxAvpCodes.FLOWS, DiameterRxAvpCodes.TGPP_VENDOR_ID, FlowsAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp#setFlows(net.java.slee.resource.diameter.rx.events.avp.FlowsAvp)
   */
  @Override
  public void setFlows(FlowsAvp flows) {
    addAvp(DiameterRxAvpCodes.FLOWS, DiameterRxAvpCodes.TGPP_VENDOR_ID,flows.getExtensionAvps());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp#setFlowss(net.java.slee.resource.diameter.rx.events.avp.FlowsAvp[])
   */
  @Override
  public void setFlows(FlowsAvp[] flows) {
    //addAvp(DiameterRxAvpCodes.FLOWS, DiameterRxAvpCodes.TGPP_VENDOR_ID, flows);
    setExtensionAvps(flows);
  }

}
