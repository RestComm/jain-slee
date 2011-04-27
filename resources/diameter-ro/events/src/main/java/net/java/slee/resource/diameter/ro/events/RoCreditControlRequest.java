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

package net.java.slee.resource.diameter.ro.events;

import java.util.Date;

import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;
import net.java.slee.resource.diameter.cca.events.avp.MultipleServicesIndicatorType;
import net.java.slee.resource.diameter.cca.events.avp.RequestedActionType;
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp;

/**
 * Interface for RoCreditControlRequest type of message. It has following structure:
 * 
 * <pre>
 *     <CCR> ::= < Diameter Header: 272, REQ, PXY >
 *             < Session-Id > 
 *                  { Origin-Host } 
 *                  { Origin-Realm } 
 *                  { Destination-Realm } 
 *                  { Auth-Application-Id }
 *                  { Service-Context-Id } 
 *                  { CC-Request-Type } 
 *                  { CC-Request-Number } 
 *                  [ Destination-Host ] 
 *                  [ User-Name ] 
 *                  [ Origin-State-Id ] 
 *                  [ Event-Timestamp ] 
 *                 *[ Subscription-Id ]  
 *                  [ Termination-Cause ] 
 *                  [ Requested-Action ] 
 *                  [ Multiple-Services-Indicator ] 
 *                 *[ Multiple-Services-Credit-Control ]  
 *                  [ User-Equipment-Info ] 
 *                 *[ Proxy-Info ]
 *                 *[ Route-Record ]
 *             [ Service-Information ]
 *                 *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface RoCreditControlRequest extends RoCreditControlMessage {

  static final int commandCode = 272;

  /**
   * Returns the value of the Service-Context-Id AVP, of type UTF8String.
   * 
   * @return
   */
  String getServiceContextId();

  /**
   * Sets the value of the Service-Context-Id AVP, of type UTF8String.
   * 
   * @param serviceContextId
   * @throws IllegalStateException
   */
  void setServiceContextId(String serviceContextId) throws IllegalStateException;

  /**
   * Returns true if the Service-Context-Id AVP is present in the message.
   * 
   * @return
   */
  boolean hasServiceContextId();

  /**
   * Returns the value of the User-Name AVP, of type UTF8String.
   * 
   * @return
   */
  String getUserName();

  /**
   * Sets the value of the User-Name AVP, of type UTF8String.
   * 
   * @param userName
   * @throws IllegalStateException
   */
  void setUserName(String userName) throws IllegalStateException;

  /**
   * Returns true if the User-Name AVP is present in the message.
   * 
   * @return
   */
  boolean hasUserName();

  /**
   * Returns the value of the Origin-State-Id AVP, of type Unsigned32.
   * 
   * @return
   */
  long getOriginStateId();

  /**
   * Sets the value of the Origin-State-Id AVP, of type Unsigned32.
   * 
   * @param originStateId
   * @throws IllegalStateException
   */
  void setOriginStateId(long originStateId) throws IllegalStateException;

  /**
   * Returns true if the Origin-State-Id AVP is present in the message.
   * 
   * @return
   */
  boolean hasOriginStateId();

  /**
   * Returns the value of the Event-Timestamp AVP, of type Time.
   * 
   * @return
   */
  Date getEventTimestamp();

  /**
   * Sets the value of the Event-Timestamp AVP, of type Time.
   * 
   * @param eventTimestamp
   * @throws IllegalStateException
   */
  void setEventTimestamp(java.util.Date eventTimestamp) throws IllegalStateException;

  /**
   * Returns true if the Event-Timestamp AVP is present in the message.
   * 
   * @return
   */
  boolean hasEventTimestamp();

  /**
   * Returns the set of Subscription-Id AVPs.
   * 
   * @return
   */
  SubscriptionIdAvp[] getSubscriptionIds();

  /**
   * Sets a single Subscription-Id AVP in the message, of type Grouped.
   * 
   * @param subscriptionId
   * @throws IllegalStateException
   */
  void setSubscriptionId(SubscriptionIdAvp subscriptionId) throws IllegalStateException;

  /**
   * Sets the set of Subscription-Id AVPs, with all the values in the given
   * array.
   * 
   * @param subscriptionIds
   * @throws IllegalStateException
   */
  void setSubscriptionIds(SubscriptionIdAvp[] subscriptionIds) throws IllegalStateException;

  /**
   * Returns the value of the Termination-Cause AVP, of type Enumerated.
   * 
   * @return
   */
  TerminationCauseType getTerminationCause();

  /**
   * Sets the value of the Termination-Cause AVP, of type Enumerated.
   * 
   * @param terminationCause
   * @throws IllegalStateException
   */
  void setTerminationCause(TerminationCauseType terminationCause) throws IllegalStateException;

  /**
   * Returns true if the Termination-Cause AVP is present in the message.
   * 
   * @return
   */
  boolean hasTerminationCause();

  /**
   * Returns the value of the Requested-Action AVP, of type Enumerated.
   * 
   * @return
   */
  RequestedActionType getRequestedAction();

  /**
   * Sets the value of the Requested-Action AVP, of type Enumerated.
   * 
   * @param requestedAction
   * @throws IllegalStateException
   */
  void setRequestedAction(RequestedActionType requestedAction) throws IllegalStateException;

  /**
   * Returns true if the Requested-Action AVP is present in the message.
   * 
   * @return
   */
  boolean hasRequestedAction();

  /**
   * Returns the value of the Multiple-Services-Indicator AVP, of type
   * Enumerated.
   * 
   * @return
   */
  MultipleServicesIndicatorType getMultipleServicesIndicator();

  /**
   * Sets the value of the Multiple-Services-Indicator AVP, of type
   * Enumerated.
   * 
   * @param multipleServicesIndicator
   * @throws IllegalStateException
   */
  void setMultipleServicesIndicator(MultipleServicesIndicatorType multipleServicesIndicator) throws IllegalStateException;

  /**
   * Returns true if the Multiple-Services-Indicator AVP is present in the
   * message.
   * 
   * @return
   */
  boolean hasMultipleServicesIndicator();

  /**
   * Returns the value of the User-Equipment-Info AVP, of type Grouped.
   * 
   * @return
   */
  UserEquipmentInfoAvp getUserEquipmentInfo();

  /**
   * Sets the value of the User-Equipment-Info AVP, of type Grouped.
   * 
   * @param userEquipmentInfo
   * @throws IllegalStateException
   */
  void setUserEquipmentInfo(UserEquipmentInfoAvp userEquipmentInfo) throws IllegalStateException;

  /**
   * Returns true if the User-Equipment-Info AVP is present in the message.
   * 
   * @return
   */
  boolean hasUserEquipmentInfo();
}
