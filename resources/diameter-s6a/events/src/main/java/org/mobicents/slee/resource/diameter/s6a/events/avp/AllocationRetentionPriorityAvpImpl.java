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

import net.java.slee.resource.diameter.s6a.events.avp.AllocationRetentionPriorityAvp;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.PreEmptionCapability;
import net.java.slee.resource.diameter.s6a.events.avp.PreEmptionVulnerability;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link AllocationRetentionPriorityAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class AllocationRetentionPriorityAvpImpl extends GroupedAvpImpl implements AllocationRetentionPriorityAvp {

  public AllocationRetentionPriorityAvpImpl() {
    super();
  }

  public AllocationRetentionPriorityAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasPriorityLevel() {
    return hasAvp(DiameterS6aAvpCodes.PRIORITY_LEVEL, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setPriorityLevel(long pl) {
    addAvp(DiameterS6aAvpCodes.PRIORITY_LEVEL, DiameterS6aAvpCodes.S6A_VENDOR_ID, pl);
  }

  public long getPriorityLevel() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.PRIORITY_LEVEL, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasPreEmptionCapability() {
    return hasAvp(DiameterS6aAvpCodes.PRE_EMPTION_CAPABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public PreEmptionCapability getPreEmptionCapability() {
    return (PreEmptionCapability) getAvpAsEnumerated(DiameterS6aAvpCodes.PRE_EMPTION_CAPABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, PreEmptionCapability.class);
  }

  public void setPreEmptionCapability(PreEmptionCapability pec) {
    addAvp(DiameterS6aAvpCodes.PRE_EMPTION_CAPABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, pec.getValue());
  }

  public boolean hasPreEmptionVulnerability() {
    return hasAvp(DiameterS6aAvpCodes.PRE_EMPTION_VULNERABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public PreEmptionVulnerability getPreEmptionVulnerability() {
    return (PreEmptionVulnerability) getAvpAsEnumerated(DiameterS6aAvpCodes.PRE_EMPTION_VULNERABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, PreEmptionVulnerability.class);
  }

  public void setPreEmptionVulnerability(PreEmptionVulnerability pev) {
    addAvp(DiameterS6aAvpCodes.PRE_EMPTION_VULNERABILITY, DiameterS6aAvpCodes.S6A_VENDOR_ID, pev.getValue());
  }

}
