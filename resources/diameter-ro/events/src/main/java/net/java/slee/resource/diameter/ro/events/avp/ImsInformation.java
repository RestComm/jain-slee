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

package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the IMS-Information grouped AVP type.<br>
 * <br> 
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre> 
 *  7.2.37 IMS-Information AVP 
 *  The IMS-Information AVP (AVP code 876) is of type Grouped. 
 *  Its purpose is to allow the transmission of additional IMS service specific information elements. 
 *  
 *  It has the following ABNF grammar: 
 *    IMS-Information ::= AVP Header: 876 
 *      [ Event-Type ] 
 *      [ Role-Of-Node ] 
 *      { Node-Functionality } 
 *      [ User-Session-ID ] 
 *      [ Calling-Party-Address ] 
 *      [ Called-Party-Address ] 
 *      [ Time-Stamps ]
 *    * [ Application-Server-Information ]
 *    * [ Inter-Operator-Identifier ]
 *      [ IMS-Charging-Identifier ]
 *    * [ SDP-Session-Description ]
 *    * [ SDP-Media-Component ]
 *      [ Served-Party-IP-Address ]
 *      [ Server-Capabilities ]
 *      [ Trunk-Group-ID ]
 *      [ Bearer-Service ]
 *      [ Service-Id ]
 *      [ Service-Specific-Data ]
 *    * [ Message-Body ] 
 *      [ Cause-Code ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ImsInformation extends GroupedAvp {

  /**
   * Returns the set of Application-Server-Information AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no Application-Server-Information AVPs have been set. The elements in the given array are ApplicationServerInformation objects.
   */
  abstract ApplicationServerInformation[] getApplicationServerInformations();

  /**
   * Returns the value of the Bearer-Service AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getBearerService();

  /**
   * Returns the value of the Called-Party-Address AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getCalledPartyAddress();

  /**
   * Returns the value of the Calling-Party-Address AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getCallingPartyAddress();

  /**
   * Returns the value of the Cause-Code AVP, of type Integer32. A return value 0< implies that the AVP has not been set.
   */
  abstract int getCauseCode();

  /**
   * Returns the value of the Event-Type AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract EventType getEventType();

  /**
   * Returns the value of the IMS-Charging-Identifier AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getImsChargingIdentifier();

  /**
   * Returns the set of Inter-Operator-Identifier AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no Inter-Operator-Identifier AVPs have been set. The elements in the given array are InterOperatorIdentifier objects.
   */
  abstract InterOperatorIdentifier[] getInterOperatorIdentifiers();

  /**
   * Returns the set of Message-Body AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no Message-Body AVPs have been set. The elements in the given array are MessageBody objects.
   */
  abstract MessageBody[] getMessageBodys();

  /**
   * Returns the value of the Node-Functionality AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract NodeFunctionality getNodeFunctionality();

  /**
   * Returns the value of the Role-Of-Node AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract RoleOfNode getRoleOfNode();

  /**
   * Returns the set of SDP-Media-Component AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no SDP-Media-Component AVPs have been set. The elements in the given array are SdpMediaComponent objects.
   */
  abstract SdpMediaComponent[] getSdpMediaComponents();

  /**
   * Returns the set of SDP-Session-Description AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no SDP-Session-Description AVPs have been set. The elements in the given array are String objects.
   */
  abstract String[] getSdpSessionDescriptions();

  /**
   * Returns the value of the Served-Party-IP-Address AVP, of type Address. A return value of null implies that the AVP has not been set.
   */
  abstract Address getServedPartyIpAddress();

  /**
   * Returns the value of the Server-Capabilities AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract ServerCapabilities getServerCapabilities();

  /**
   * Returns the value of the Service-Id AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getServiceId();

  /**
   * Returns the value of the Service-Specific-Data AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getServiceSpecificData();

  /**
   * Returns the value of the Time-Stamps AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract TimeStamps getTimeStamps();

  /**
   * Returns the value of the Trunk-Group-ID AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract TrunkGroupId getTrunkGroupId();

  /**
   * Returns the value of the User-Session-ID AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getUserSessionId();

  /**
   * Returns true if the Bearer-Service AVP is present in the message.
   */
  abstract boolean hasBearerService();

  /**
   * Returns true if the Called-Party-Address AVP is present in the message.
   */
  abstract boolean hasCalledPartyAddress();

  /**
   * Returns true if the Calling-Party-Address AVP is present in the message.
   */
  abstract boolean hasCallingPartyAddress();

  /**
   * Returns true if the Cause-Code AVP is present in the message.
   */
  abstract boolean hasCauseCode();

  /**
   * Returns true if the Event-Type AVP is present in the message.
   */
  abstract boolean hasEventType();

  /**
   * Returns true if the IMS-Charging-Identifier AVP is present in the message.
   */
  abstract boolean hasImsChargingIdentifier();

  /**
   * Returns true if the Node-Functionality AVP is present in the message.
   */
  abstract boolean hasNodeFunctionality();

  /**
   * Returns true if the Role-Of-Node AVP is present in the message.
   */
  abstract boolean hasRoleOfNode();

  /**
   * Returns true if the Served-Party-IP-Address AVP is present in the message.
   */
  abstract boolean hasServedPartyIpAddress();

  /**
   * Returns true if the Server-Capabilities AVP is present in the message.
   */
  abstract boolean hasServerCapabilities();

  /**
   * Returns true if the Service-Id AVP is present in the message.
   */
  abstract boolean hasServiceId();

  /**
   * Returns true if the Service-Specific-Data AVP is present in the message.
   */
  abstract boolean hasServiceSpecificData();

  /**
   * Returns true if the Time-Stamps AVP is present in the message.
   */
  abstract boolean hasTimeStamps();

  /**
   * Returns true if the Trunk-Group-ID AVP is present in the message.
   */
  abstract boolean hasTrunkGroupId();

  /**
   * Returns true if the User-Session-ID AVP is present in the message.
   */
  abstract boolean hasUserSessionId();

  /**
   * Sets a single Application-Server-Information AVP in the message, of type Grouped.
   */
  abstract void setApplicationServerInformation(ApplicationServerInformation applicationServerInformation);

  /**
   * Sets the set of Application-Server-Information AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getApplicationServerInformations() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setApplicationServerInformations(ApplicationServerInformation[] applicationServerInformations);

  /**
   * Sets the value of the Bearer-Service AVP, of type OctetString.
   */
  abstract void setBearerService(byte[] bearerService);

  /**
   * Sets the value of the Called-Party-Address AVP, of type UTF8String.
   */
  abstract void setCalledPartyAddress(String calledPartyAddress);

  /**
   * Sets the value of the Calling-Party-Address AVP, of type UTF8String.
   */
  abstract void setCallingPartyAddress(String callingPartyAddress);

  /**
   * Sets the value of the Cause-Code AVP, of type Integer32.
   */
  abstract void setCauseCode(int causeCode);

  /**
   * Sets the value of the Event-Type AVP, of type Grouped.
   */
  abstract void setEventType(EventType eventType);

  /**
   * Sets the value of the IMS-Charging-Identifier AVP, of type UTF8String.
   */
  abstract void setImsChargingIdentifier(String imsChargingIdentifier);

  /**
   * Sets a single Inter-Operator-Identifier AVP in the message, of type Grouped.
   */
  abstract void setInterOperatorIdentifier(InterOperatorIdentifier interOperatorIdentifier);

  /**
   * Sets the set of Inter-Operator-Identifier AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getInterOperatorIdentifiers() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setInterOperatorIdentifiers(InterOperatorIdentifier[] interOperatorIdentifiers);

  /**
   * Sets a single Message-Body AVP in the message, of type Grouped.
   */
  abstract void setMessageBody(MessageBody messageBody);

  /**
   * Sets the set of Message-Body AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getMessageBodys() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setMessageBodys(MessageBody[] messageBodys);

  /**
   * Sets the value of the Node-Functionality AVP, of type Enumerated.
   */
  abstract void setNodeFunctionality(NodeFunctionality nodeFunctionality);

  /**
   * Sets the value of the Role-Of-Node AVP, of type Enumerated.
   */
  abstract void setRoleOfNode(RoleOfNode roleOfNode);

  /**
   * Sets a single SDP-Media-Component AVP in the message, of type Grouped.
   */
  abstract void setSdpMediaComponent(SdpMediaComponent sdpMediaComponent);

  /**
   * Sets the set of SDP-Media-Component AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getSdpMediaComponents() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setSdpMediaComponents(SdpMediaComponent[] sdpMediaComponents);

  /**
   * Sets a single SDP-Session-Description AVP in the message, of type UTF8String.
   */
  abstract void setSdpSessionDescription(String sdpSessionDescription);

  /**
   * Sets the set of SDP-Session-Description AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getSdpSessionDescriptions() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setSdpSessionDescriptions(String[] sdpSessionDescriptions);

  /**
   * Sets the value of the Served-Party-IP-Address AVP, of type Address.
   */
  abstract void setServedPartyIpAddress(Address servedPartyIpAddress);

  /**
   * Sets the value of the Server-Capabilities AVP, of type Grouped.
   */
  abstract void setServerCapabilities(ServerCapabilities serverCapabilities);

  /**
   * Sets the value of the Service-Id AVP, of type UTF8String.
   */
  abstract void setServiceId(String serviceId);

  /**
   * Sets the value of the Service-Specific-Data AVP, of type UTF8String.
   */
  abstract void setServiceSpecificData(String serviceSpecificData);

  /**
   * Sets the value of the Time-Stamps AVP, of type Grouped.
   */
  abstract void setTimeStamps(TimeStamps timeStamps);

  /**
   * Sets the value of the Trunk-Group-ID AVP, of type Grouped.
   */
  abstract void setTrunkGroupId(TrunkGroupId trunkGroupId);

  /**
   * Sets the value of the User-Session-ID AVP, of type UTF8String.
   */
  abstract void setUserSessionId(String userSessionId);

}
