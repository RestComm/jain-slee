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

package org.mobicents.slee.resource.diameter.ro.events;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.cca.events.avp.SubscriptionIdAvpImpl;
import org.mobicents.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvpImpl;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.MultipleServicesIndicatorType;
import net.java.slee.resource.diameter.cca.events.avp.RequestedActionType;
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp;
import net.java.slee.resource.diameter.ro.events.RoCreditControlRequest;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoCreditControlRequestImpl extends RoCreditControlMessageImpl implements RoCreditControlRequest {

  /**
   * @param message
   */
  public RoCreditControlRequestImpl(Message message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#getServiceContextId()
   */
  @Override
  public String getServiceContextId() {
    return getAvpAsUTF8String(CreditControlAVPCodes.Service_Context_Id);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#setServiceContextId(java.lang.String)
   */
  @Override
  public void setServiceContextId(String serviceContextId) throws IllegalStateException {
    addAvp(CreditControlAVPCodes.Service_Context_Id, serviceContextId);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#hasServiceContextId()
   */
  @Override
  public boolean hasServiceContextId() {
    return hasAvp(CreditControlAVPCodes.Service_Context_Id);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#getSubscriptionIds()
   */
  @Override
  public SubscriptionIdAvp[] getSubscriptionIds() {
    return (SubscriptionIdAvp[]) getAvpsAsCustom(CreditControlAVPCodes.Subscription_Id, SubscriptionIdAvpImpl.class);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#setSubscriptionId(net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp)
   */
  @Override
  public void setSubscriptionId(SubscriptionIdAvp subscriptionId) throws IllegalStateException {
    addAvp(CreditControlAVPCodes.Subscription_Id, subscriptionId.byteArrayValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#setSubscriptionIds(net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp[])
   */
  @Override
  public void setSubscriptionIds(SubscriptionIdAvp[] subscriptionIds) throws IllegalStateException {
    for (SubscriptionIdAvp subscriptionId : subscriptionIds) {
      setSubscriptionId(subscriptionId);
    }
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#getTerminationCause()
   */
  @Override
  public TerminationCauseType getTerminationCause() {
    return (TerminationCauseType) getAvpAsEnumerated(DiameterAvpCodes.TERMINATION_CAUSE, TerminationCauseType.class);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#setTerminationCause(net.java.slee.resource.diameter.base.events.avp.TerminationCauseType)
   */
  @Override
  public void setTerminationCause(TerminationCauseType terminationCause) throws IllegalStateException {
    addAvp(DiameterAvpCodes.TERMINATION_CAUSE, terminationCause.getValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#hasTerminationCause()
   */
  @Override
  public boolean hasTerminationCause() {
    return hasAvp(DiameterAvpCodes.TERMINATION_CAUSE);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#getRequestedAction()
   */
  @Override
  public RequestedActionType getRequestedAction() {
    return (RequestedActionType) getAvpAsEnumerated(CreditControlAVPCodes.Requested_Action, RequestedActionType.class);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#setRequestedAction(net.java.slee.resource.diameter.cca.events.avp.RequestedActionType)
   */
  @Override
  public void setRequestedAction(RequestedActionType requestedAction) throws IllegalStateException {
    addAvp(CreditControlAVPCodes.Requested_Action, requestedAction.getValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#hasRequestedAction()
   */
  @Override
  public boolean hasRequestedAction() {
    return hasAvp(CreditControlAVPCodes.Requested_Action);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#getMultipleServicesIndicator()
   */
  @Override
  public MultipleServicesIndicatorType getMultipleServicesIndicator() {
    return (MultipleServicesIndicatorType) getAvpAsEnumerated(CreditControlAVPCodes.Multiple_Services_Indicator, MultipleServicesIndicatorType.class);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#setMultipleServicesIndicator(net.java.slee.resource.diameter.cca.events.avp.MultipleServicesIndicatorType)
   */
  @Override
  public void setMultipleServicesIndicator(MultipleServicesIndicatorType multipleServicesIndicator) throws IllegalStateException {
    addAvp(CreditControlAVPCodes.Multiple_Services_Indicator, multipleServicesIndicator.getValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#hasMultipleServicesIndicator()
   */
  @Override
  public boolean hasMultipleServicesIndicator() {
    return hasAvp(CreditControlAVPCodes.Multiple_Services_Indicator);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#getUserEquipmentInfo()
   */
  @Override
  public UserEquipmentInfoAvp getUserEquipmentInfo() {
    return (UserEquipmentInfoAvp) getAvpAsCustom(CreditControlAVPCodes.User_Equipment_Info, UserEquipmentInfoAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#setUserEquipmentInfo(net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp)
   */
  @Override
  public void setUserEquipmentInfo(UserEquipmentInfoAvp userEquipmentInfo) throws IllegalStateException {
    addAvp(CreditControlAVPCodes.User_Equipment_Info, userEquipmentInfo.byteArrayValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlRequest#hasUserEquipmentInfo()
   */
  @Override
  public boolean hasUserEquipmentInfo() {
    return hasAvp(CreditControlAVPCodes.User_Equipment_Info);
  }

  @Override
  public String getLongName() {
    return "Credit-Control-Request";
  }

  @Override
  public String getShortName() {
    return "CCR";
  }

}
