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
import org.mobicents.slee.resource.diameter.cca.events.avp.CostInformationAvpImpl;

import net.java.slee.resource.diameter.cca.events.avp.CcSessionFailoverType;
import net.java.slee.resource.diameter.cca.events.avp.CostInformationAvp;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlFailureHandlingType;
import net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoCreditControlAnswerImpl extends RoCreditControlMessageImpl implements RoCreditControlAnswer {

  /**
   * @param message
   */
  public RoCreditControlAnswerImpl(Message message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#getCcSessionFailover()
   */
  @Override
  public CcSessionFailoverType getCcSessionFailover() {
    return (CcSessionFailoverType) getAvpAsEnumerated(CreditControlAVPCodes.CC_Session_Failover, CcSessionFailoverType.class);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#setCcSessionFailover(net.java.slee.resource.diameter.cca.events.avp.CcSessionFailoverType)
   */
  @Override
  public void setCcSessionFailover(CcSessionFailoverType ccSessionFailover) throws IllegalStateException {
    addAvp(CreditControlAVPCodes.CC_Session_Failover, ccSessionFailover.getValue());
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#hasCcSessionFailover()
   */
  @Override
  public boolean hasCcSessionFailover() {
    return hasAvp(CreditControlAVPCodes.CC_Session_Failover);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#getCostInformation()
   */
  @Override
  public CostInformationAvp getCostInformation() {
    return (CostInformationAvp) getAvpAsCustom(CreditControlAVPCodes.Cost_Information, CostInformationAvpImpl.class);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#setCostInformation(net.java.slee.resource.diameter.cca.events.avp.CostInformationAvp)
   */
  @Override
  public void setCostInformation(CostInformationAvp costInformation) throws IllegalStateException {
    addAvp(CreditControlAVPCodes.Cost_Information, costInformation.byteArrayValue());
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#hasCostInformation()
   */
  @Override
  public boolean hasCostInformation() {
    return hasAvp(CreditControlAVPCodes.Cost_Information);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#getCreditControlFailureHandling()
   */
  @Override
  public CreditControlFailureHandlingType getCreditControlFailureHandling() {
    return (CreditControlFailureHandlingType) getAvpAsEnumerated(CreditControlAVPCodes.Credit_Control_Failure_Handling, CreditControlFailureHandlingType.class);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#setCreditControlFailureHandling(net.java.slee.resource.diameter.cca.events.avp.CreditControlFailureHandlingType)
   */
  @Override
  public void setCreditControlFailureHandling(CreditControlFailureHandlingType creditControlFailureHandling) throws IllegalStateException {
    addAvp(CreditControlAVPCodes.Credit_Control_Failure_Handling, creditControlFailureHandling.getValue());
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer#hasCreditControlFailureHandling()
   */
  @Override
  public boolean hasCreditControlFailureHandling() {
    return hasAvp(CreditControlAVPCodes.Credit_Control_Failure_Handling);
  }

  @Override
  public String getLongName() {
    return "Credit-Control-Answer";
  }

  @Override
  public String getShortName() {
    return "CCA";
  }

}
