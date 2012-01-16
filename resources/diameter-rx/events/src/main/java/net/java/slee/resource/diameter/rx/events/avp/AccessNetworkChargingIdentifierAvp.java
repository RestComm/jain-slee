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

package net.java.slee.resource.diameter.rx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * The Access-Network-Charging-Identifier AVP (AVP code 502) is of type Grouped,
 * and contains a charging identifier (e.g. GCID) within the
 * Access-Network-Charging-Identifier-Value AVP along with information about the
 * flows transported within the corresponding bearer within the Flows AVP. If no
 * Flows AVP is provided, the Access Network Charging-Identifier-Value applies
 * for all flows within the AF session. The Access-Network-Charging-Identifier
 * AVP can be sent from the PCRF to the AF. The AF may use this information for
 * charging correlation with session layer. AVP Format:
 * 
 * <pre>
 * Access-Network-Charging-Identifier ::= < AVP Header: 502 >
 *                                        { Access-Network-Charging-Identifier-Value }
 *                                      * [ Flows ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface AccessNetworkChargingIdentifierAvp extends GroupedAvp {

  /**
   * Checks if Access-Network-Charging-Identifier-Value AVP (AVP code 503) is
   * present in Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasAccessNetworkChargingIdentifierValue();

  /**
   * Sets value of the Access-Network-Charging-Identifier-Value AVP (AVP code
   * 503),of type OctetString. It contains a charging identifier (e.g. GCID).
   */
  public void setAccessNetworkChargingIdentifierValue(byte[] anci);

  /**
   * Fetches value of the Access-Network-Charging-Identifier-Value AVP (AVP
   * code 503),of type OctetString.
   * 
   * @return
   */
  public byte[] getAccessNetworkChargingIdentifierValue();

  /**
   * Checks if Flows-AVP (AVP code 513)  is present in GroupedAVP.
   * @return
   */
  public boolean hasFlows();

  /**
   * Fetches values of Flows-AVP (AVP code 513) AVPs.
   * @return
   */
  public FlowsAvp[] getFlows();

  /**
   * Sets value of Flows-AVP (AVP code 513).
   * @param flows
   */
  public void setFlows(FlowsAvp flows);

  public void setFlows(FlowsAvp[] flows);

}
