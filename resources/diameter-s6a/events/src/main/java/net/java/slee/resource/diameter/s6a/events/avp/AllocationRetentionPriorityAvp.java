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

package net.java.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Allocation-Retention-Priority grouped AVP type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.40 Allocation-Retention-Priority
 * 
 * The Allocation-Retention-Priority AVP is of typeGrouped and is defined in 3GPP TS 29.212 [10].
 * It shall indicate the Priority of Allocation and Retention for the corresponding APN configuration.
 * 
 * AVP format
 * Allocation-Retention-Priority ::= < AVP header: 1034 10415 >
 *                                   { Priority-Level }
 *                                   [ Pre-emption-Capability ]
 *                                   [ Pre-emption-Vulnerability ]
 *                                   
 * If the Pre-emption-Capability AVP is not present in the Allocation-Retention-Priority AVP, 
 * the default value shall be PRE-EMPTION_CAPABILITY_DISABLED (1).
 * If the Pre-emption-Vulnerability AVP is not present in the Allocation-Retention-Priority AVP,
 * the default value shall be PRE-EMPTION_VULNERABILITY_ENABLED (0).
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface AllocationRetentionPriorityAvp extends GroupedAvp {

  boolean hasPriorityLevel();

  void setPriorityLevel(long pl);

  long getPriorityLevel();

  public boolean hasPreEmptionCapability();

  public PreEmptionCapability getPreEmptionCapability();

  public void setPreEmptionCapability(PreEmptionCapability pec);

  public boolean hasPreEmptionVulnerability();

  public PreEmptionVulnerability getPreEmptionVulnerability();

  public void setPreEmptionVulnerability(PreEmptionVulnerability pec);
}
