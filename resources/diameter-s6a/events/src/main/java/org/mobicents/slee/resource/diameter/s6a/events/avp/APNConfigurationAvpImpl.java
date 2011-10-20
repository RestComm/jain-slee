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

import net.java.slee.resource.diameter.s6a.events.avp.AMBRAvp;
import net.java.slee.resource.diameter.s6a.events.avp.APNConfigurationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.EPSSubscribedQoSProfileAvp;
import net.java.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.PDNGWAllocationType;
import net.java.slee.resource.diameter.s6a.events.avp.PDNType;
import net.java.slee.resource.diameter.s6a.events.avp.VPLMNDynamicAddressAllowed;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link APNConfigurationAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class APNConfigurationAvpImpl extends GroupedAvpImpl implements APNConfigurationAvp {

  public APNConfigurationAvpImpl() {
    super();
  }

  public APNConfigurationAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasContextIdentifier() {
    return hasAvp(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public long getContextIdentifier() {
    return this.getAvpAsUnsigned32(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setContextIdentifier(long contextIdentifier) {
    addAvp(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID, contextIdentifier);
  }

  public boolean hasPDNType() {
    return hasAvp(DiameterS6aAvpCodes.PDN_TYPE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setPDNType(PDNType pt) {
    addAvp(DiameterS6aAvpCodes.PDN_TYPE, DiameterS6aAvpCodes.S6A_VENDOR_ID, pt.getValue());
  }

  public PDNType getPDNType() {
    return (PDNType) getAvpAsEnumerated(DiameterS6aAvpCodes.PDN_TYPE, DiameterS6aAvpCodes.S6A_VENDOR_ID, PDNType.class);
  }

  public boolean hasServiceSelection() {
    return hasAvp(DiameterS6aAvpCodes.SERVICE_SELECTION);
  }

  public String getServiceSelection() {
    return getAvpAsUTF8String(DiameterS6aAvpCodes.SERVICE_SELECTION);
  }

  public void setServiceSelection(String serviceSelection) {
    addAvp(DiameterS6aAvpCodes.SERVICE_SELECTION, serviceSelection);
  }

  public boolean hasEPSSubscribedQoSProfile() {
    return hasAvp(DiameterS6aAvpCodes.EPS_SUBSCRIBED_QOS_PROFILE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public EPSSubscribedQoSProfileAvp getEPSSubscribedQoSProfile() {
    return (EPSSubscribedQoSProfileAvp) getAvpAsCustom(DiameterS6aAvpCodes.EPS_SUBSCRIBED_QOS_PROFILE, DiameterS6aAvpCodes.S6A_VENDOR_ID, EPSSubscribedQoSProfileAvpImpl.class);
  }

  public void setEPSSubscribedQoSProfile(EPSSubscribedQoSProfileAvp qp) {
    addAvp(qp);
  }

  public boolean hasVPLMNDynamicAddressAllowed() {
    return hasAvp(DiameterS6aAvpCodes.VPLMN_DYNAMIC_ADDRESS_ALLOWED, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public VPLMNDynamicAddressAllowed getVPLMNDynamicAddressAllowed() {
    return (VPLMNDynamicAddressAllowed) getAvpAsEnumerated(DiameterS6aAvpCodes.VPLMN_DYNAMIC_ADDRESS_ALLOWED, DiameterS6aAvpCodes.S6A_VENDOR_ID, VPLMNDynamicAddressAllowed.class);
  }

  public void setVPLMNDynamicAddressAllowed(VPLMNDynamicAddressAllowed daa) {
    addAvp(DiameterS6aAvpCodes.VPLMN_DYNAMIC_ADDRESS_ALLOWED, DiameterS6aAvpCodes.S6A_VENDOR_ID, daa.getValue());
  }

  public boolean hasPDNGWAllocationType() {
    return hasAvp(DiameterS6aAvpCodes.PDN_GATEWAY_ALLOCATION_TYPE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public PDNGWAllocationType getPDNGWAllocationType() {
    return (PDNGWAllocationType) getAvpAsEnumerated(DiameterS6aAvpCodes.PDN_GATEWAY_ALLOCATION_TYPE, DiameterS6aAvpCodes.S6A_VENDOR_ID, PDNGWAllocationType.class);
  }

  public void setPDNGWAllocationType(PDNGWAllocationType at) {
    addAvp(DiameterS6aAvpCodes.PDN_GATEWAY_ALLOCATION_TYPE, DiameterS6aAvpCodes.S6A_VENDOR_ID, at.getValue());
  }

  public boolean hasAMBR() {
    return hasAvp(DiameterS6aAvpCodes.AMBR, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public AMBRAvp getAMBR() {
    return (AMBRAvp) getAvpAsCustom(DiameterS6aAvpCodes.AMBR, DiameterS6aAvpCodes.S6A_VENDOR_ID, AMBRAvpImpl.class);
  }

  public void setAMBR(AMBRAvp ambr) {
    addAvp(ambr);
  }

  public boolean hasMIP6AgentInfo() {
    return hasAvp(DiameterS6aAvpCodes.MIP6_AGENT_INFO);
  }

  public MIP6AgentInfoAvp getMIP6AgentInfo() {
    return (MIP6AgentInfoAvp) getAvpAsCustom(DiameterS6aAvpCodes.MIP6_AGENT_INFO, MIP6AgentInfoAvpImpl.class);
  }

  public void setMIP6AgentInfo(MIP6AgentInfoAvp mip) {
    addAvp(mip);
  }

}
