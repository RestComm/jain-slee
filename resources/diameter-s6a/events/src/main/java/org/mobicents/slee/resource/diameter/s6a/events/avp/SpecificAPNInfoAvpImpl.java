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

package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link SpecificAPNInfoAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SpecificAPNInfoAvpImpl extends GroupedAvpImpl implements SpecificAPNInfoAvp {

  /**
   * 
   */
  public SpecificAPNInfoAvpImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public SpecificAPNInfoAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#hasServiceSelection()
   */
  public boolean hasServiceSelection() {
    return hasAvp(DiameterS6aAvpCodes.SERVICE_SELECTION);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#getServiceSelection()
   */
  public String getServiceSelection() {
    return getAvpAsUTF8String(DiameterS6aAvpCodes.SERVICE_SELECTION);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#setServiceSelection(java.lang.String)
   */
  public void setServiceSelection(String serviceSelection) {
    addAvp(DiameterS6aAvpCodes.SERVICE_SELECTION, serviceSelection);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#hasMIP6AgentInfo()
   */
  public boolean hasMIP6AgentInfo() {
    return hasAvp(DiameterS6aAvpCodes.MIP6_AGENT_INFO);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#getMIP6AgentInfo()
   */
  public MIP6AgentInfoAvp getMIP6AgentInfo() {
    return (MIP6AgentInfoAvp) getAvpAsCustom(DiameterS6aAvpCodes.MIP6_AGENT_INFO, MIP6AgentInfoAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#setMIP6AgentInfo(net.java.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvp)
   */
  public void setMIP6AgentInfo(MIP6AgentInfoAvp mip) {
    addAvp(DiameterS6aAvpCodes.MIP6_AGENT_INFO, mip.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#hasVisitedNetworkIdentifier()
   */
  public boolean hasVisitedNetworkIdentifier() {
    return hasAvp(DiameterS6aAvpCodes.VISITED_NETWORK_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#getVisitedNetworkIdentifier()
   */
  public DiameterIdentity getVisitedNetworkIdentifier() {
    return getAvpAsDiameterIdentity(DiameterS6aAvpCodes.VISITED_NETWORK_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp#setVisitedNetworkIdentifier(net.java.slee.resource.diameter.base.events.avp.DiameterIdentity)
   */
  public void setVisitedNetworkIdentifier(DiameterIdentity vni) {
    addAvp(DiameterS6aAvpCodes.VISITED_NETWORK_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID, vni);
  }

}
