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

package org.mobicents.slee.resource.diameter.gq.events;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.gq.events.GqAARequest;
import net.java.slee.resource.diameter.gq.events.avp.BindingInformation;
import net.java.slee.resource.diameter.gq.events.avp.FlowGrouping;
import net.java.slee.resource.diameter.gq.events.avp.GloballyUniqueAddress;
import net.java.slee.resource.diameter.gq.events.avp.LatchingIndication;
import net.java.slee.resource.diameter.gq.events.avp.MediaComponentDescription;
import net.java.slee.resource.diameter.gq.events.avp.OverbookingIndicator;
import net.java.slee.resource.diameter.gq.events.avp.ReservationPriority;
import net.java.slee.resource.diameter.gq.events.avp.SIPForkingIndication;
import net.java.slee.resource.diameter.gq.events.avp.SpecificAction;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.BindingInformationImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.DiameterGqAvpCodes;
import org.mobicents.slee.resource.diameter.gq.events.avp.FlowGroupingImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.GloballyUniqueAddressImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.MediaComponentDescriptionImpl;




/**
 * <pre>
 * <b>7.1.1 AA-Request(AAR) command</b>
 * The AAR command, indicated by the Command-Code field set to 265 and the "R" bit set in the Command Flags field,
 * is sent by an AF to the SPDF in order to request the authorization for the bearer usage for the AF session.
 * Message Format:
 *   &lt;AA-Request&gt; ::= < Diameter Header: 265, REQ, PXY >
 *                    < Session-Id >
 *                    { Auth-Application-Id }
 *                    { Origin-Host }
 *                    { Origin-Realm }
 *                    { Destination-Realm }
 *                   *[ Media-Component-Description ]
 *                   *[ Flow-Grouping ]
 *                    [ AF-Charging-Identifier ]
 *                    [ SIP-Forking-Indication ]
 *                   *[ Specific-Action ]
 *                    [ User-Name ]
 *                    [ Binding-Information ]
 *                    [ Latching-Indication ]
 *                    [ Reservation-Priority ]
 *                    [ Globally-Unique-Address ]
 *                    [ Service-Class ]
 *                    [ Authorization-Lifetime ]
 *                   *[ Proxy-Info ]
 *                   *[ Route-Record ]
 *                    [ Overbooking-Indicator ]
 *                   *[ Authorization-Package-Id ]
 *                   *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class GqAARequestImpl extends DiameterMessageImpl implements GqAARequest {

  public GqAARequestImpl(Message message) {
    super(message);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getAuthorizationLifetime
   */
  public long getAuthorizationLifetime() {
    return getAvpAsUnsigned32(DiameterAvpCodes.AUTHORIZATION_LIFETIME);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setAuthorizationLifetime
   */
  public void setAuthorizationLifetime(long authorizationLifetime) throws IllegalStateException {
    addAvp(DiameterAvpCodes.AUTHORIZATION_LIFETIME, authorizationLifetime);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasAuthorizationLifetime
   */
  public boolean hasAuthorizationLifetime() {
    return hasAvp(DiameterAvpCodes.AUTHORIZATION_LIFETIME);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getMediaComponentDescriptions
   */
  public MediaComponentDescription[] getMediaComponentDescriptions() {
    return (MediaComponentDescription[]) getAvpsAsCustom(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_DESCRIPTION,
        DiameterGqAvpCodes.TGPP_VENDOR_ID, MediaComponentDescriptionImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setMediaComponentDescription
   */
  public void setMediaComponentDescription(MediaComponentDescription mediaComponentDescription) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_DESCRIPTION, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        mediaComponentDescription.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setMediaComponentDescriptions
   */
  public void setMediaComponentDescriptions(MediaComponentDescription[] mediaComponentDescriptions) throws IllegalStateException {
    for (MediaComponentDescription mediaComponentDescription : mediaComponentDescriptions) {
      setMediaComponentDescription(mediaComponentDescription);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getFlowGrouping
   */
  public FlowGrouping[] getFlowGroupings() {
    return (FlowGrouping[]) getAvpsAsCustom(DiameterGqAvpCodes.TGPP_FLOW_GROUPING, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        FlowGroupingImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setFlowGrouping
   */
  public void setFlowGrouping(FlowGrouping flowGrouping) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_FLOW_GROUPING, DiameterGqAvpCodes.TGPP_VENDOR_ID, flowGrouping.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setFlowGroupings
   */
  public void setFlowGroupings(FlowGrouping[] flowGroupings) throws IllegalStateException {
    for (FlowGrouping flowGrouping : flowGroupings) {
      setFlowGrouping(flowGrouping);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getAFChargingIdentifier
   */
  public byte[] getAFChargingIdentifier() {
    return getAvpAsOctetString(DiameterGqAvpCodes.TGPP_AF_CHARGING_IDENTIFIER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setAFChargingIdentifier
   */
  public void setAFChargingIdentifier(byte[] AFChargingIdentifier) {
    addAvp(DiameterGqAvpCodes.TGPP_AF_CHARGING_IDENTIFIER, DiameterGqAvpCodes.TGPP_VENDOR_ID, AFChargingIdentifier);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasAFChargingIdentifier
   */
  public boolean hasAFChargingIdentifier() {
    return hasAvp(DiameterGqAvpCodes.TGPP_AF_CHARGING_IDENTIFIER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getSIPForkingIndication
   */
  public SIPForkingIndication getSIPForkingIndication() {
    return (SIPForkingIndication) getAvpAsEnumerated(DiameterGqAvpCodes.TGPP_SIP_FORKING_INDICATION, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        SIPForkingIndication.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setSIPForkingIndication
   */
  public void setSIPForkingIndication(SIPForkingIndication SIPForkingIndication) {
    addAvp(DiameterGqAvpCodes.TGPP_SIP_FORKING_INDICATION, DiameterGqAvpCodes.TGPP_VENDOR_ID, SIPForkingIndication.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasSIPForkingIndication
   */
  public boolean hasSIPForkingIndication() {
    return hasAvp(DiameterGqAvpCodes.TGPP_SIP_FORKING_INDICATION, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getSpecificAction
   */
  public SpecificAction[] getSpecificActions() {
    return (SpecificAction[]) getAvpsAsEnumerated(DiameterGqAvpCodes.TGPP_SPECIFIC_ACTION, DiameterGqAvpCodes.TGPP_VENDOR_ID,
        SpecificAction.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setSpecificAction
   */
  public void setSpecificAction(SpecificAction specificAction) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.TGPP_SPECIFIC_ACTION, DiameterGqAvpCodes.TGPP_VENDOR_ID, specificAction.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setSpecificActions
   */
  public void setSpecificActions(SpecificAction[] specificActions) throws IllegalStateException {
    for (SpecificAction specificAction : specificActions) {
      setSpecificAction(specificAction);
    }
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getBindingInformation
   */
  public BindingInformation getBindingInformation() {
    return (BindingInformation) getAvpAsCustom(DiameterGqAvpCodes.ETSI_BINDING_INFORMATION, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        BindingInformationImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setBindingInformation
   */
  public void setBindingInformation(BindingInformation bindingInformation) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_BINDING_INFORMATION, DiameterGqAvpCodes.ETSI_VENDOR_ID, bindingInformation.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasBindingInformation
   */
  public boolean hasBindingInformation() {
    return hasAvp(DiameterGqAvpCodes.ETSI_BINDING_INFORMATION, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getLatchingIndication
   */
  public LatchingIndication getLatchingIndication() {
    return (LatchingIndication) getAvpAsEnumerated(DiameterGqAvpCodes.ETSI_LATCHING_INDICATION, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        LatchingIndication.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setLatchingIndication
   */
  public void setLatchingIndication(LatchingIndication latchingIndication) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_LATCHING_INDICATION, DiameterGqAvpCodes.ETSI_VENDOR_ID, latchingIndication.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasLatchingIndication
   */
  public boolean hasLatchingIndication() {
    return hasAvp(DiameterGqAvpCodes.ETSI_LATCHING_INDICATION, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getReservationPriority
   */
  public ReservationPriority getReservationPriority() {
    return (ReservationPriority) getAvpAsEnumerated(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        ReservationPriority.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setReservationPriority
   */
  public void setReservationPriority(ReservationPriority reservationPriority) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID, reservationPriority.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasReservationPriority
   */
  public boolean hasReservationPriority() {
    return hasAvp(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getGloballyUniqueAddress
   */
  public GloballyUniqueAddress getGloballyUniqueAddress() {
    return (GloballyUniqueAddress) getAvpAsCustom(DiameterGqAvpCodes.ETSI_GLOBALLY_UNIQUE_ADDRESS, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        GloballyUniqueAddressImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setGloballyUniqueAddress
   */
  public void setGloballyUniqueAddress(GloballyUniqueAddress globallyUniqueAddress) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_GLOBALLY_UNIQUE_ADDRESS, DiameterGqAvpCodes.ETSI_VENDOR_ID, globallyUniqueAddress.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasGloballyUniqueAddress
   */
  public boolean hasGloballyUniqueAddress() {
    return hasAvp(DiameterGqAvpCodes.ETSI_GLOBALLY_UNIQUE_ADDRESS, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasServiceClass
   */
  public boolean hasServiceClass() {
    return hasAvp(DiameterGqAvpCodes.ETSI_SERVICE_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getServiceClass
   */
  public String getServiceClass() {
    return getAvpAsUTF8String(DiameterGqAvpCodes.ETSI_SERVICE_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setServiceClass
   */
  public void setServiceClass(String serviceClass) {
    addAvp(DiameterGqAvpCodes.ETSI_SERVICE_CLASS, DiameterGqAvpCodes.ETSI_VENDOR_ID, serviceClass);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getOverbookingIndicator
   */
  public OverbookingIndicator getOverbookingIndicator() {
    return (OverbookingIndicator) getAvpAsEnumerated(DiameterGqAvpCodes.ETSI_OVERBOOKING_INDICATOR, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        OverbookingIndicator.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setOverbookingIndicator
   */
  public void setOverbookingIndicator(OverbookingIndicator overbookingIndicator) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_OVERBOOKING_INDICATOR, DiameterGqAvpCodes.ETSI_VENDOR_ID, overbookingIndicator.getValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#hasOverbookingIndicator
   */
  public boolean hasOverbookingIndicator() {
    return hasAvp(DiameterGqAvpCodes.ETSI_OVERBOOKING_INDICATOR, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#getAuthorizationPackageId
   */
  public String[] getAuthorizationPackageIds() {
    return getAvpsAsUTF8String(DiameterGqAvpCodes.ETSI_AUTHORIZATION_PACKAGE_ID, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setAuthorizationPackageId
   */
  public void setAuthorizationPackageId(String authorizationPackageId) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_AUTHORIZATION_PACKAGE_ID, DiameterGqAvpCodes.ETSI_VENDOR_ID, authorizationPackageId);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAARequest#setAuthorizationPackageIds
   */
  public void setAuthorizationPackageIds(String[] authorizationPackageIds) throws IllegalStateException {
    for (String authorizationPackageId : authorizationPackageIds) {
      setAuthorizationPackageId(authorizationPackageId);
    }
  }

  @Override
  public String getLongName() {
    return "AA-Request";
  }

  @Override
  public String getShortName() {
    return "AAR";
  }
}
