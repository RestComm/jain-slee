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

package org.mobicents.slee.resource.diameter.rx.events;

import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.rx.events.AAAnswer;
import net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp;
import net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp;
import net.java.slee.resource.diameter.rx.events.avp.IPCANType;
import net.java.slee.resource.diameter.rx.events.avp.RATType;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.DiameterRxAvpCodes;

/**
 * Implementation of {@link AAAnswer}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @see DiameterMessageImpl
 */
public class AAAnswerImpl extends AAMessageImpl implements AAAnswer {

  /**
   * @param message
   */
  public AAAnswerImpl(Message message) {
    super(message);
  }

  @Override
  public String getLongName() {
    return "AA-Answer";
  }

  @Override
  public String getShortName() {
    return "AAA";
  }

  @Override
  public boolean hasAcceptableServiceInfo() {
    return super.hasAvp(DiameterRxAvpCodes.ACCEPTABLE_SERVICE_INFO, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setAcceptableServiceInfo(AcceptableServiceInfoAvp asi) {
    super.addAvp(DiameterRxAvpCodes.ACCEPTABLE_SERVICE_INFO, DiameterRxAvpCodes.TGPP_VENDOR_ID, asi.getExtensionAvps());
  }

  @Override
  public AcceptableServiceInfoAvp getAcceptableServiceInfo() {
    return (AcceptableServiceInfoAvp) super.getAvpAsCustom(DiameterRxAvpCodes.ACCEPTABLE_SERVICE_INFO, DiameterRxAvpCodes.TGPP_VENDOR_ID,
        AcceptableServiceInfoAvpImpl.class);
  }

  @Override
  public boolean hasAccessNetworkChargingAddress() {
    return super.hasAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_ADDRESS, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public Address getAccessNetworkChargingAddress() {
    return super.getAvpAsAddress(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_ADDRESS, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setAccessNetworkChargingAddress(Address a) {
    super.addAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_ADDRESS, DiameterRxAvpCodes.TGPP_VENDOR_ID,a);
  }

  @Override
  public boolean hasAccessNetworkChargingIdentifier() {
    return super.hasAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public AccessNetworkChargingIdentifierAvp[] getAccessNetworkChargingIdentifiers() {
    return (AccessNetworkChargingIdentifierAvp[]) super.getAvpsAsCustom(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID, AccessNetworkChargingIdentifierAvpImpl.class);
  }

  @Override
  public void setAccessNetworkChargingIdentifier(AccessNetworkChargingIdentifierAvp accessNetowrkChardingId) {
    super.addAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID,accessNetowrkChardingId.getExtensionAvps());
  }

  @Override
  public void setAccessNetworkChargingIdentifiers(AccessNetworkChargingIdentifierAvp[] accessNetowrkChardingIds) {
    super.setExtensionAvps(accessNetowrkChardingIds);
  }

  @Override
  public boolean hasIPCANType() {
    return super.hasAvp(DiameterRxAvpCodes.IP_CAN_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setIPCANType(IPCANType t) {
    super.addAvp(DiameterRxAvpCodes.IP_CAN_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID,t.getValue());
  }

  @Override
  public IPCANType getIPCANType() {
    return (IPCANType) super.getAvpAsEnumerated(DiameterRxAvpCodes.IP_CAN_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID, IPCANType.class);
  }

  @Override
  public boolean hasRATType() {
    return super.hasAvp(DiameterRxAvpCodes.RAT_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setRATType(RATType t) {
    super.addAvp(DiameterRxAvpCodes.RAT_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID,t.getValue());
  }

  @Override
  public RATType getRATType() {
    return (RATType) super.getAvpAsEnumerated(DiameterRxAvpCodes.RAT_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID, RATType.class);
  }

  @Override
  public boolean hasExperimentalResult() {
    return super.hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  @Override
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) super.getAvpAsCustom(DiameterAvpCodes.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  @Override
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) throws IllegalStateException {
    super.addAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT,experimentalResult.getExtensionAvps());
  }

  @Override
  public boolean hasProxyInfo() {
    return super.hasAvp(DiameterAvpCodes.PROXY_INFO);
  }

  @Override
  public boolean hasFailedAvp() {
    return super.hasAvp(DiameterAvpCodes.FAILED_AVP);
  }

}
