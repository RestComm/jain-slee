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

package org.mobicents.slee.resource.diameter.rx.events.avp;

import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.FinalUnitActionType;
import net.java.slee.resource.diameter.rx.events.avp.FlowsAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation of {@link FlowsAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class FlowsAvpImpl extends GroupedAvpImpl implements FlowsAvp {

  /**
   * 
   */
  public FlowsAvpImpl() {
    super.code = DiameterRxAvpCodes.FLOWS;
    super.vendorId = DiameterRxAvpCodes.TGPP_VENDOR_ID;
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public FlowsAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#hasMediaComponentNumber()
   */
  @Override
  public boolean hasMediaComponentNumber() {
    return super.hasAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#setMediaComponentNumber(long)
   */
  @Override
  public void setMediaComponentNumber(long l) {
    super.addAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID,l);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#getMediaComponentNumber()
   */
  @Override
  public long getMediaComponentNumber() {
    return super.getAvpAsUnsigned32(DiameterRxAvpCodes.MEDIA_COMPONENT_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#hasFlowNumber()
   */
  @Override
  public boolean hasFlowNumber() {
    return super.hasAvp(DiameterRxAvpCodes.FLOW_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#setFlowNumber(long)
   */
  @Override
  public void setFlowNumber(long l) {
    super.addAvp(DiameterRxAvpCodes.FLOW_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID,l);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#getFlowNumbers()
   */
  @Override
  public long[] getFlowNumbers() {
    return super.getAvpsAsUnsigned32(DiameterRxAvpCodes.FLOW_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#setFlowNumbers(long[])
   */
  @Override
  public void setFlowNumbers(long[] l) {
    //FIXME: how should this be done ?
    for(long v: l)
      super.addAvp(DiameterRxAvpCodes.FLOW_NUMBER,DiameterRxAvpCodes.TGPP_VENDOR_ID, v);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#hasFinalUnitAction()
   */
  @Override
  public boolean hasFinalUnitAction() {
    return super.hasAvp(CreditControlAVPCodes.Final_Unit_Action);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#setFinalUnitAction(net.java.slee.resource.diameter.cca.events.avp.FinalUnitActionType)
   */
  @Override
  public void setFinalUnitAction(FinalUnitActionType l) {
    super.addAvp(CreditControlAVPCodes.Final_Unit_Action,l.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.FlowsAvp#getFinalUnitAction()
   */
  @Override
  public FinalUnitActionType getFinalUnitAction() {
    return (FinalUnitActionType) super.getAvpAsEnumerated(CreditControlAVPCodes.Final_Unit_Action, FinalUnitActionType.class);
  }

}
