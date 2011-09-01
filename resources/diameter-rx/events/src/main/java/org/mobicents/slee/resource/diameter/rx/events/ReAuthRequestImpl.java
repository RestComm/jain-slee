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
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.rx.events.ReAuthRequest;
import net.java.slee.resource.diameter.rx.events.avp.AbortCause;
import net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp;
import net.java.slee.resource.diameter.rx.events.avp.FlowsAvp;
import net.java.slee.resource.diameter.rx.events.avp.IPCANType;
import net.java.slee.resource.diameter.rx.events.avp.RATType;
import net.java.slee.resource.diameter.rx.events.avp.SpecificAction;
import net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.cca.events.avp.SubscriptionIdAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.DiameterRxAvpCodes;
import org.mobicents.slee.resource.diameter.rx.events.avp.FlowsAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvpImpl;

/**
 * Implementation of {@link ReAuthRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @see DiameterMessageImpl
 */
public class ReAuthRequestImpl extends DiameterMessageImpl implements ReAuthRequest {

  public ReAuthRequestImpl(Message message) {
    super(message);
  }

  @Override
  public String getLongName() {
    return "Re-Auth-Request";
  }

  @Override
  public String getShortName() {
    return "RAR";
  }

  @Override
  public boolean hasAbortCause() {
    return super.hasAvp(DiameterRxAvpCodes.ABORT_CAUSE, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public AbortCause getAbortCause() {
    return (AbortCause) super.getAvpAsEnumerated(DiameterRxAvpCodes.ABORT_CAUSE, DiameterRxAvpCodes.TGPP_VENDOR_ID, AbortCause.class);
  }

  @Override
  public void setAbortCause(AbortCause abortCause) throws IllegalStateException {
    super.addAvp(DiameterRxAvpCodes.ABORT_CAUSE, DiameterRxAvpCodes.TGPP_VENDOR_ID, abortCause.getValue());
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
    super.addAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_ADDRESS, DiameterRxAvpCodes.TGPP_VENDOR_ID, a);
  }

  @Override
  public boolean hasAccessNetworkChargingIdentifier() {
    return super.hasAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public AccessNetworkChargingIdentifierAvp[] getAccessNetworkChargingIdentifiers() {
    return (AccessNetworkChargingIdentifierAvp[]) super.getAvpsAsCustom(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER,
        DiameterRxAvpCodes.TGPP_VENDOR_ID, AccessNetworkChargingIdentifierAvpImpl.class);
  }

  @Override
  public void setAccessNetworkChargingIdentifier(AccessNetworkChargingIdentifierAvp accessNetowrkChardingId) {
    super.addAvp(DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID, accessNetowrkChardingId.getExtensionAvps());
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
    super.addAvp(DiameterRxAvpCodes.RAT_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID, t.getValue());
  }

  @Override
  public RATType getRATType() {
    return (RATType) super.getAvpAsEnumerated(DiameterRxAvpCodes.RAT_TYPE, DiameterRxAvpCodes.TGPP_VENDOR_ID, RATType.class);
  }

  @Override
  public boolean hasSubscriptionId() {
    return super.hasAvp(CreditControlAVPCodes.Subscription_Id);
  }

  @Override
  public void setSubscriptionId(SubscriptionIdAvp sid) {
    super.addAvp(CreditControlAVPCodes.Subscription_Id, sid.getExtensionAvps());
  }

  @Override
  public void setSubscriptionIds(SubscriptionIdAvp[] sids) {
    super.setExtensionAvps(sids);
  }

  @Override
  public SubscriptionIdAvp[] getSubscriptionIds() {
    return (SubscriptionIdAvp[]) super.getAvpsAsCustom(CreditControlAVPCodes.Subscription_Id, SubscriptionIdAvpImpl.class);
  }

  @Override
  public boolean hasSpecificAction() {
    return super.hasAvp(DiameterRxAvpCodes.SPECIFIC_ACTION, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setSpecificAction(SpecificAction v) {
    super.addAvp(DiameterRxAvpCodes.SPECIFIC_ACTION, DiameterRxAvpCodes.TGPP_VENDOR_ID,v.getValue() );

  }

  @Override
  public SpecificAction getSpecificAction() {
    return (SpecificAction) super.getAvpAsEnumerated(DiameterRxAvpCodes.SPECIFIC_ACTION, DiameterRxAvpCodes.TGPP_VENDOR_ID, SpecificAction.class);
  }

  @Override
  public boolean hasProxyInfo() {
    return super.hasAvp(DiameterAvpCodes.PROXY_INFO);
  }

  @Override
  public boolean hasRouteRecord() {
    return super.hasAvp(DiameterAvpCodes.ROUTE_RECORD);
  }

  @Override
  public boolean hasFlows() {
    return super.hasAvp(DiameterRxAvpCodes.FLOWS, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public FlowsAvp[] getFlows() {
    return (FlowsAvp[]) super.getAvpsAsCustom(DiameterRxAvpCodes.FLOWS, DiameterRxAvpCodes.TGPP_VENDOR_ID, FlowsAvpImpl.class);
  }

  @Override
  public void setFlows(FlowsAvp flows) {
    super.addAvp(DiameterRxAvpCodes.FLOWS, DiameterRxAvpCodes.TGPP_VENDOR_ID, flows.getExtensionAvps());

  }

  @Override
  public void setFlows(FlowsAvp[] flows) {
    super.setExtensionAvps(flows);
  }

  @Override
  public boolean hasSponsoredConnectivityData() {
    return super.hasAvp(DiameterRxAvpCodes.SPONSORED_CONNECTIVITY_DATA, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setSponsoredConnectivityData(SponsoredConnectivityDataAvp scd) {
    super.addAvp(DiameterRxAvpCodes.SPONSORED_CONNECTIVITY_DATA, DiameterRxAvpCodes.TGPP_VENDOR_ID, scd.getExtensionAvps());
  }

  @Override
  public SponsoredConnectivityDataAvp getSponsoredConnectivityData() {
    return (SponsoredConnectivityDataAvp) super.getAvpAsCustom(DiameterRxAvpCodes.SPONSORED_CONNECTIVITY_DATA, DiameterRxAvpCodes.TGPP_VENDOR_ID,
        SponsoredConnectivityDataAvpImpl.class);
  }

}
