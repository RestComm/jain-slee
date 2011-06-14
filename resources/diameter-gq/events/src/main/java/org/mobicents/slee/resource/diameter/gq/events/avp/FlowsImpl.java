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

package org.mobicents.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.gq.events.avp.Flows;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;


/**
 * Implementation for {@link Flows}
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class FlowsImpl extends GroupedAvpImpl implements Flows {

  public FlowsImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public FlowsImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.Flows#getMediaComponentNumber()
   */
  public long getMediaComponentNumber() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.Flows#getFlowNumber()
   */
  public long[] getFlowNumber() {
    return getAvpsAsUnsigned32(DiameterGqAvpCodes.TGPP_FLOW_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.Flows#hasMediaComponentNumber()
   */
  public boolean hasMediaComponentNumber() {
    return hasAvp(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.Flows#setMediaComponentNumber()
   */
  public void setMediaComponentNumber(long mediaComponentNumber) {
    addAvp(DiameterGqAvpCodes.TGPP_MEDIA_COMPONENT_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID, mediaComponentNumber);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.Flows#setFlowNumber()
   */
  public void setFlowNumber(long flowNumber) {
    addAvp(AvpUtilities.createAvp(DiameterGqAvpCodes.TGPP_FLOW_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID, flowNumber));
    // addAvp(DiameterGqAvpCodes.TGPP_FLOW_NUMBER, DiameterGqAvpCodes.TGPP_VENDOR_ID, flowNumber);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.Flows#setFlowNumbers()
   */
  public void setFlowNumbers(long[] flowNumbers) {
    for (long flowNumber : flowNumbers) {
      setFlowNumber(flowNumber);
    }
  }

}
