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

import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.rf.events.avp.ApplicationServerInformation;
import net.java.slee.resource.diameter.rf.events.avp.EventType;
import net.java.slee.resource.diameter.rf.events.avp.ImsInformation;
import net.java.slee.resource.diameter.rf.events.avp.InterOperatorIdentifier;
import net.java.slee.resource.diameter.rf.events.avp.MessageBody;
import net.java.slee.resource.diameter.rf.events.avp.NodeFunctionality;
import net.java.slee.resource.diameter.rf.events.avp.RoleOfNode;
import net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent;
import net.java.slee.resource.diameter.rf.events.avp.ServerCapabilities;
import net.java.slee.resource.diameter.rf.events.avp.TimeStamps;
import net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * ImsInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>11:34:57 AM Apr 11, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */ 
public class ImsInformationImpl extends GroupedAvpImpl implements ImsInformation {

  public ImsInformationImpl() {
    super();
  }

  /**
   * 
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public ImsInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getApplicationServerInformations()
   */
  public ApplicationServerInformation[] getApplicationServerInformations() {
    return (ApplicationServerInformation[]) getAvpsAsCustom(DiameterRfAvpCodes.APPLICATION_SERVER_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, ApplicationServerInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getBearerService()
   */
  public byte[] getBearerService() {
    return getAvpAsOctetString(DiameterRfAvpCodes.BEARER_SERVICE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getCalledPartyAddress()
   */
  public String getCalledPartyAddress() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.CALLED_PARTY_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getCallingPartyAddress()
   */
  public String getCallingPartyAddress() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.CALLING_PARTY_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getCauseCode()
   */
  public int getCauseCode() {
    return getAvpAsInteger32(DiameterRfAvpCodes.CAUSE_CODE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getEventType()
   */
  public EventType getEventType() {
    return (EventType) getAvpAsCustom(DiameterRfAvpCodes.EVENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, EventTypeImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getImsChargingIdentifier()
   */
  public String getImsChargingIdentifier() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.IMS_CHARGING_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getInterOperatorIdentifiers()
   */
  public InterOperatorIdentifier[] getInterOperatorIdentifiers() {
    return (InterOperatorIdentifier[]) getAvpsAsCustom(DiameterRfAvpCodes.INTER_OPERATOR_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID, InterOperatorIdentifierImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getMessageBodys()
   */
  public MessageBody[] getMessageBodys() {
    return (MessageBody[]) getAvpsAsCustom(DiameterRfAvpCodes.MESSAGE_BODY, DiameterRfAvpCodes.TGPP_VENDOR_ID, MessageBodyImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getNodeFunctionality()
   */
  public NodeFunctionality getNodeFunctionality() {
    return (NodeFunctionality) getAvpAsEnumerated(DiameterRfAvpCodes.NODE_FUNCTIONALITY, DiameterRfAvpCodes.TGPP_VENDOR_ID, NodeFunctionality.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getRoleOfNode()
   */
  public RoleOfNode getRoleOfNode() {
    return (RoleOfNode) getAvpAsEnumerated(DiameterRfAvpCodes.ROLE_OF_NODE, DiameterRfAvpCodes.TGPP_VENDOR_ID, RoleOfNode.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getSdpMediaComponents()
   */
  public SdpMediaComponent[] getSdpMediaComponents() {
    return (SdpMediaComponent[]) getAvpsAsCustom(DiameterRfAvpCodes.SDP_MEDIA_COMPONENT, DiameterRfAvpCodes.TGPP_VENDOR_ID, SdpMediaComponentImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getSdpSessionDescriptions()
   */
  public String[] getSdpSessionDescriptions() {
    return getAvpsAsUTF8String(DiameterRfAvpCodes.SDP_SESSION_DESCRIPTION, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getServedPartyIpAddress()
   */
  public Address getServedPartyIpAddress() {
    return getAvpAsAddress(DiameterRfAvpCodes.SERVED_PARTY_IP_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getServerCapabilities()
   */
  public ServerCapabilities getServerCapabilities() {
    return (ServerCapabilities) getAvpAsCustom(DiameterRfAvpCodes.SERVER_CAPABILITIES, DiameterRfAvpCodes.TGPP_VENDOR_ID, ServerCapabilitiesImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getServiceId()
   */
  public String getServiceId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.SERVICE_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getServiceSpecificData()
   */
  public String getServiceSpecificData() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.SERVICE_SPECIFIC_DATA, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getTimeStamps()
   */
  public TimeStamps getTimeStamps() {
    return (TimeStamps) getAvpAsCustom(DiameterRfAvpCodes.TIME_STAMPS, DiameterRfAvpCodes.TGPP_VENDOR_ID, TimeStampsImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getTrunkGroupId()
   */
  public TrunkGroupId getTrunkGroupId() {
    return (TrunkGroupId) getAvpAsCustom(DiameterRfAvpCodes.TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, TrunkGroupIdImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#getUserSessionId()
   */
  public String getUserSessionId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.USER_SESSION_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasBearerService()
   */
  public boolean hasBearerService() {
    return hasAvp( DiameterRfAvpCodes.BEARER_SERVICE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasCalledPartyAddress()
   */
  public boolean hasCalledPartyAddress() {
    return hasAvp( DiameterRfAvpCodes.CALLED_PARTY_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasCallingPartyAddress()
   */
  public boolean hasCallingPartyAddress() {
    return hasAvp( DiameterRfAvpCodes.CALLING_PARTY_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasCauseCode()
   */
  public boolean hasCauseCode() {
    return hasAvp( DiameterRfAvpCodes.CAUSE_CODE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasEventType()
   */
  public boolean hasEventType() {
    return hasAvp( DiameterRfAvpCodes.EVENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasImsChargingIdentifier()
   */
  public boolean hasImsChargingIdentifier() {
    return hasAvp( DiameterRfAvpCodes.IMS_CHARGING_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasNodeFunctionality()
   */
  public boolean hasNodeFunctionality() {
    return hasAvp( DiameterRfAvpCodes.NODE_FUNCTIONALITY, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasRoleOfNode()
   */
  public boolean hasRoleOfNode() {
    return hasAvp( DiameterRfAvpCodes.ROLE_OF_NODE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasServedPartyIpAddress()
   */
  public boolean hasServedPartyIpAddress() {
    return hasAvp( DiameterRfAvpCodes.SERVED_PARTY_IP_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasServerCapabilities()
   */
  public boolean hasServerCapabilities() {
    return hasAvp( DiameterRfAvpCodes.SERVER_CAPABILITIES, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasServiceId()
   */
  public boolean hasServiceId() {
    return hasAvp( DiameterRfAvpCodes.SERVICE_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasServiceSpecificData()
   */
  public boolean hasServiceSpecificData() {
    return hasAvp( DiameterRfAvpCodes.SERVICE_SPECIFIC_DATA, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasTimeStamps()
   */
  public boolean hasTimeStamps() {
    return hasAvp( DiameterRfAvpCodes.TIME_STAMPS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasTrunkGroupId()
   */
  public boolean hasTrunkGroupId() {
    return hasAvp( DiameterRfAvpCodes.TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#hasUserSessionId()
   */
  public boolean hasUserSessionId() {
    return hasAvp( DiameterRfAvpCodes.USER_SESSION_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setApplicationServerInformation(net.java.slee.resource.diameter.rf.events.avp.ApplicationServerInformation)
   */
  public void setApplicationServerInformation( ApplicationServerInformation applicationServerInformation ){
    addAvp(DiameterRfAvpCodes.APPLICATION_SERVER_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, applicationServerInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setApplicationServerInformations(net.java.slee.resource.diameter.rf.events.avp.ApplicationServerInformation[])
   */
  public void setApplicationServerInformations( ApplicationServerInformation[] applicationServerInformations ){
    for(ApplicationServerInformation applicationServerInformation : applicationServerInformations) {
      setApplicationServerInformation(applicationServerInformation);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setBearerService(String)
   */
  public void setBearerService( byte[] bearerService ) {
    addAvp(DiameterRfAvpCodes.BEARER_SERVICE, DiameterRfAvpCodes.TGPP_VENDOR_ID, bearerService);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setCalledPartyAddress(String)
   */
  public void setCalledPartyAddress( String calledPartyAddress ) {
    addAvp(DiameterRfAvpCodes.CALLED_PARTY_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, calledPartyAddress);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setCallingPartyAddress(String)
   */
  public void setCallingPartyAddress( String callingPartyAddress ) {
    addAvp(DiameterRfAvpCodes.CALLING_PARTY_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, callingPartyAddress);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setCauseCode(int)
   */
  public void setCauseCode( int causeCode ) {
    addAvp(DiameterRfAvpCodes.CAUSE_CODE, DiameterRfAvpCodes.TGPP_VENDOR_ID, causeCode);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setEventType(net.java.slee.resource.diameter.rf.events.avp.EventType)
   */
  public void setEventType( EventType eventType ) {
    addAvp(DiameterRfAvpCodes.EVENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, eventType.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setImsChargingIdentifier(String)
   */
  public void setImsChargingIdentifier( String imsChargingIdentifier ) {
    addAvp(DiameterRfAvpCodes.IMS_CHARGING_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID, imsChargingIdentifier);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setInterOperatorIdentifier(net.java.slee.resource.diameter.rf.events.avp.InterOperatorIdentifier)
   */
  public void setInterOperatorIdentifier( InterOperatorIdentifier interOperatorIdentifier ) {
    addAvp(DiameterRfAvpCodes.INTER_OPERATOR_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID, interOperatorIdentifier.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setInterOperatorIdentifiers(net.java.slee.resource.diameter.rf.events.avp.InterOperatorIdentifier[])
   */
  public void setInterOperatorIdentifiers( InterOperatorIdentifier[] interOperatorIdentifiers ) {
    for(InterOperatorIdentifier interOperatorIdentifier : interOperatorIdentifiers) {
      setInterOperatorIdentifier(interOperatorIdentifier);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setMessageBody(net.java.slee.resource.diameter.rf.events.avp.MessageBody)
   */
  public void setMessageBody( MessageBody messageBody ) {
    addAvp(DiameterRfAvpCodes.MESSAGE_BODY, DiameterRfAvpCodes.TGPP_VENDOR_ID, messageBody.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setMessageBodys(net.java.slee.resource.diameter.rf.events.avp.MessageBody[])
   */
  public void setMessageBodys( MessageBody[] messageBodys ) {
    for(MessageBody messageBody : messageBodys) {
      setMessageBody(messageBody);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setNodeFunctionality(net.java.slee.resource.diameter.rf.events.avp.NodeFunctionality)
   */
  public void setNodeFunctionality( NodeFunctionality nodeFunctionality ) {
    addAvp(DiameterRfAvpCodes.NODE_FUNCTIONALITY, DiameterRfAvpCodes.TGPP_VENDOR_ID, nodeFunctionality.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setRoleOfNode(net.java.slee.resource.diameter.rf.events.avp.RoleOfNode)
   */
  public void setRoleOfNode( RoleOfNode roleOfNode ) {
    addAvp(DiameterRfAvpCodes.ROLE_OF_NODE, DiameterRfAvpCodes.TGPP_VENDOR_ID, roleOfNode.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setSdpMediaComponent(net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent)
   */
  public void setSdpMediaComponent( SdpMediaComponent sdpMediaComponent ) {
    addAvp(DiameterRfAvpCodes.SDP_MEDIA_COMPONENT, DiameterRfAvpCodes.TGPP_VENDOR_ID, sdpMediaComponent.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setSdpMediaComponents(net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent[])
   */
  public void setSdpMediaComponents( SdpMediaComponent[] sdpMediaComponents ) {
    for(SdpMediaComponent sdpMediaComponent : sdpMediaComponents) {
      setSdpMediaComponent(sdpMediaComponent);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setSdpSessionDescription(String)
   */
  public void setSdpSessionDescription( String sdpSessionDescription )
  {
    addAvp(DiameterRfAvpCodes.SDP_SESSION_DESCRIPTION, DiameterRfAvpCodes.TGPP_VENDOR_ID, sdpSessionDescription);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setSdpSessionDescriptions(String[])
   */
  public void setSdpSessionDescriptions( String[] sdpSessionDescriptions ) {
    for(String sdpSessionDescription : sdpSessionDescriptions) {
      setSdpSessionDescription(sdpSessionDescription);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setServedPartyIpAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setServedPartyIpAddress( Address servedPartyIpAddress ) {
    addAvp(DiameterRfAvpCodes.SERVED_PARTY_IP_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, servedPartyIpAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setServerCapabilities(net.java.slee.resource.diameter.rf.events.avp.ServerCapabilities)
   */
  public void setServerCapabilities( ServerCapabilities serverCapabilities ) {
    addAvp(DiameterRfAvpCodes.SERVER_CAPABILITIES, DiameterRfAvpCodes.TGPP_VENDOR_ID, serverCapabilities.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setServiceId(String)
   */
  public void setServiceId( String serviceId ) {
    addAvp(DiameterRfAvpCodes.SERVICE_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, serviceId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setServiceSpecificData(String)
   */
  public void setServiceSpecificData( String serviceSpecificData ) {
    addAvp(DiameterRfAvpCodes.SERVICE_SPECIFIC_DATA, DiameterRfAvpCodes.TGPP_VENDOR_ID, serviceSpecificData);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setTimeStamps(net.java.slee.resource.diameter.rf.events.avp.TimeStamps)
   */
  public void setTimeStamps( TimeStamps timeStamps ) {
    addAvp(DiameterRfAvpCodes.TIME_STAMPS, DiameterRfAvpCodes.TGPP_VENDOR_ID, timeStamps.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setTrunkGroupId(net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId)
   */
  public void setTrunkGroupId( TrunkGroupId trunkGroupId ) {
    addAvp(DiameterRfAvpCodes.TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, trunkGroupId.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ImsInformation#setUserSessionId(String)
   */
  public void setUserSessionId( String userSessionId ) {
    addAvp(DiameterRfAvpCodes.USER_SESSION_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, userSessionId);
  }

}
