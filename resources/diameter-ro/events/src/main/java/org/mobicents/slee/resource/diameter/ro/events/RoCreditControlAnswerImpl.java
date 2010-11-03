/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
    addAvp(CreditControlAVPCodes.CC_Session_Failover, (long) ccSessionFailover.getValue());
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
    addAvp(CreditControlAVPCodes.Credit_Control_Failure_Handling, (long) creditControlFailureHandling.getValue());
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
