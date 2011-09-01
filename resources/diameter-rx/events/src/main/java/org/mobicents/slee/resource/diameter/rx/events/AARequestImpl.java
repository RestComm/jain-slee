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

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.rx.events.AARequest;
import net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp;
import net.java.slee.resource.diameter.rx.events.avp.ReservationPriority;
import net.java.slee.resource.diameter.rx.events.avp.SIPForkingIndication;
import net.java.slee.resource.diameter.rx.events.avp.ServiceInfoStatus;
import net.java.slee.resource.diameter.rx.events.avp.SpecificAction;
import net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.cca.events.avp.SubscriptionIdAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.DiameterRxAvpCodes;
import org.mobicents.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvpImpl;

/**
 * Implementation of {@link AARequest} implementation
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @see DiameterMessageImpl
 */
public class AARequestImpl extends AAMessageImpl implements AARequest {

  /**
   * @param message
   */
  public AARequestImpl(Message message) {
    super(message);
  }

  @Override
  public String getLongName() {
    return "AA-Request";
  }

  @Override
  public String getShortName() {
    return "AAR";
  }

  /**
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rx.events.RxCreditControlRequest#getFramedIPAddress()
   */
  public String getFramedIPAddress() {
    return getAvpAsOctetString(DiameterRxAvpCodes.FRAMED_IP_ADDRESS);
  }

  /**
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rx.events.RxCreditControlRequest#hasFramedIPAddress()
   */
  public boolean hasFramedIPAddress() {
    return hasAvp(DiameterRxAvpCodes.FRAMED_IP_ADDRESS);
  }

  /**
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rx.events.RxCreditControlRequest#setFramedIPAddress()
   */
  public void setFramedIPAddress(String framedIpAddress) {
    addAvp(DiameterRxAvpCodes.FRAMED_IP_ADDRESS, framedIpAddress);
  }

  @Override
  public void setAFApplicationIdentifier(String afAppId) {
    super.addAvp(DiameterRxAvpCodes.AF_APPLICATION_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID, afAppId);
  }

  @Override
  public boolean hasAFApplicationIdentifier() {
    return super.hasAvp(DiameterRxAvpCodes.AF_APPLICATION_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public String getAFApplicationIdentifier() {
    return super.getAvpAsOctetString(DiameterRxAvpCodes.AF_APPLICATION_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setAFChargingIdentifier(String afAppId) {
    super.addAvp(DiameterRxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID, afAppId);
  }

  @Override
  public boolean hasAFChargingIdentifier() {
    return super.hasAvp(DiameterRxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public String getAFChargingIdentifier() {
    return super.getAvpAsOctetString(DiameterRxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public boolean hasCalledStationId() {
    return super.hasAvp(DiameterRxAvpCodes.CALLED_STATION_ID);
  }

  @Override
  public String getCalledStationId() {
    return super.getAvpAsUTF8String(DiameterRxAvpCodes.CALLED_STATION_ID);
  }

  @Override
  public void setCalledStationId(String csi) {
    super.addAvp(DiameterRxAvpCodes.CALLED_STATION_ID, csi);

  }

  @Override
  public String getFramedIPV6Prefix() {
    return super.getAvpAsOctetString(DiameterRxAvpCodes.FRAMED_IPV6_PREFIX);
  }

  @Override
  public boolean hasFramedIPV6Prefix() {
    return super.hasAvp(DiameterRxAvpCodes.FRAMED_IPV6_PREFIX);
  }

  @Override
  public void setFramedIPV6Prefix(String framedIpV6Prefix) {
    super.addAvp(DiameterRxAvpCodes.FRAMED_IPV6_PREFIX,  framedIpV6Prefix);
  }

  @Override
  public boolean hasMediaComponentDescription() {
    return super.hasAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setMediaComponentDescription(MediaComponentDescriptionAvp mcd) {
    super.addAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID, mcd.getExtensionAvps());
  }

  @Override
  public MediaComponentDescriptionAvp[] getMediaComponentDescriptions() {
    return (MediaComponentDescriptionAvp[]) super.getAvpsAsCustom(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID,
        MediaComponentDescriptionAvpImpl.class);
  }

  @Override
  public void setMediaComponentDescriptions(MediaComponentDescriptionAvp[] mcds) {
    super.setExtensionAvps(mcds);

  }

  @Override
  public boolean hasMPSIdentifier() {
    return super.hasAvp(DiameterRxAvpCodes.MPS_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setMPSIdentifier(String mpsIdentifier) {
    super.addAvp(DiameterRxAvpCodes.MPS_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID, mpsIdentifier);
  }

  @Override
  public String getMPSIdentifier() {
    return super.getAvpAsOctetString(DiameterRxAvpCodes.MPS_IDENTIFIER, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public ReservationPriority getReservationPriority() {
    return (ReservationPriority) super.getAvpAsEnumerated(DiameterRxAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterRxAvpCodes.ETSI_VENDOR_ID,
        ReservationPriority.class);
  }

  @Override
  public void setReservationPriority(ReservationPriority reservationPriority) throws IllegalStateException {
    super.addAvp(DiameterRxAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterRxAvpCodes.ETSI_VENDOR_ID, reservationPriority.getValue());

  }

  @Override
  public boolean hasReservationPriority() {
    return super.hasAvp(DiameterRxAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterRxAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  public ServiceInfoStatus getServiceInfoStatus() {
    return (ServiceInfoStatus) super.getAvpAsEnumerated(DiameterRxAvpCodes.SERVICE_INFO_STATUS, DiameterRxAvpCodes.TGPP_VENDOR_ID, ServiceInfoStatus.class);
  }

  @Override
  public void setServiceInfoStatus(ServiceInfoStatus s) {
    super.addAvp(DiameterRxAvpCodes.SERVICE_INFO_STATUS, DiameterRxAvpCodes.TGPP_VENDOR_ID, s.getValue());

  }

  @Override
  public boolean hasServiceInfoStatus() {
    return super.hasAvp(DiameterRxAvpCodes.SERVICE_INFO_STATUS, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public boolean hasServiceURN() {
    return super.hasAvp(DiameterRxAvpCodes.SERVICE_URN, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setServiceURN(String serviceURN) {
    super.addAvp(DiameterRxAvpCodes.SERVICE_URN, DiameterRxAvpCodes.TGPP_VENDOR_ID, serviceURN);

  }

  @Override
  public String getServiceURN() {
    return super.getAvpAsOctetString(DiameterRxAvpCodes.SERVICE_URN, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public boolean hasSIPForkingIndication() {
    return super.hasAvp(DiameterRxAvpCodes.SIP_FORKING_INDICATION, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setSIPForkingIndication(SIPForkingIndication v) {
    super.addAvp(DiameterRxAvpCodes.SIP_FORKING_INDICATION, DiameterRxAvpCodes.TGPP_VENDOR_ID, v.getValue());
  }

  @Override
  public SIPForkingIndication getSIPForkingIndication() {
    return (SIPForkingIndication) super.getAvpAsEnumerated(DiameterRxAvpCodes.SIP_FORKING_INDICATION, DiameterRxAvpCodes.TGPP_VENDOR_ID,
        SIPForkingIndication.class);
  }

  @Override
  public boolean hasSpecificAction() {
    return super.hasAvp(DiameterRxAvpCodes.SPECIFIC_ACTION, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setSpecificAction(SpecificAction v) {
    super.addAvp(DiameterRxAvpCodes.SPECIFIC_ACTION, DiameterRxAvpCodes.TGPP_VENDOR_ID,v.getValue());

  }

  @Override
  public SpecificAction[] getSpecificActions() {
    return (SpecificAction[]) super.getAvpsAsEnumerated(DiameterRxAvpCodes.SPECIFIC_ACTION, DiameterRxAvpCodes.TGPP_VENDOR_ID, SpecificAction.class);
  }

  @Override
  public void setSpecificActions(SpecificAction[] v) {
    // FIXME: fixme
    for(SpecificAction e:v)
      super.addAvp(DiameterRxAvpCodes.SPECIFIC_ACTION, DiameterRxAvpCodes.TGPP_VENDOR_ID, e.getValue());
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
  public boolean hasProxyInfo() {
    return super.hasAvp(DiameterAvpCodes.PROXY_INFO);
  }

  @Override
  public boolean hasRouteRecord() {
    return super.hasAvp(DiameterAvpCodes.ROUTE_RECORD);
  }
}
