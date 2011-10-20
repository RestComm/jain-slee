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

import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Allocation-Retention-Priority grouped AVP type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.82 Specific-APN-Info AVP
 * 
 * The Specific-APN-Info AVP is of type Grouped. It shall only be present in the APN configuration
 * when the APN is a wild card APN. It shall contain the APN which is not present in the
 * subscription context but the UE is authorized to connect to and the identity of the registered
 * PDN-GW.
 * 
 * The AVP format shall conform to:
 * Specific-APN-Info ::= < AVP header: 1472 10415 >
 *                       { Service-Selection }
 *                       { MIP6-Agent-Info }
 *                       [ Visited-Network-Identifier ]
 *                      *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface SpecificAPNInfoAvp extends GroupedAvp {

  /**
   * Returns true if the Service-Selection AVP is present in the message.
   * 
   * @return true if the Service-Selection AVP is present in the message, false otherwise
   */
  public boolean hasServiceSelection();

  /**
   * Returns the value of the Service-Selection AVP, of type UTF8String.
   * 
   * @return the value of the Service-Selection AVP or null if it has not been set on this message
   */
  public String getServiceSelection();

  /**
   * Sets the value of the Service-Selection AVP, of type UTF8String.
   * 
   * @param serviceSelection
   */
  public void setServiceSelection(String serviceSelection);

  /**
   * Returns true if the MIP6-Agent-Info AVP is present in the message.
   * 
   * @return true if the MIP6-Agent-Info AVP is present in the message, false otherwise
   */
  public boolean hasMIP6AgentInfo();

  /**
   * Returns the value of the MIP6-Agent-Info AVP, of type Grouped.
   * 
   * @return the value of the MIP6-Agent-Info AVP or null if it has not been set on this message
   */
  public MIP6AgentInfoAvp getMIP6AgentInfo();

  /**
   * Sets the value of the MIP6-Agent-Info AVP, of type Grouped.
   * 
   * @param mip the new value for the MIP6-Agent-Info AVP
   */
  public void setMIP6AgentInfo(MIP6AgentInfoAvp mip);

  /**
   * Returns true if the Visited-Network-Identifier AVP is present in the message.
   * 
   * @return true if the Visited-Network-Identifier AVP is present in the message, false otherwise
   */
  public boolean hasVisitedNetworkIdentifier();

  /**
   * Returns the value of the Visited-Network-Identifier AVP, of type DiameterIdentity.
   * 
   * @return the value of the Visited-Network-Identifier AVP or null if it has not been set on this message
   */
  public DiameterIdentity getVisitedNetworkIdentifier();

  /**
   * Sets the value of the Visited-Network-Identifier AVP, of type DiameterIdentity.
   * 
   * @param vni the new value for the Visited-Network-Identifier AVP
   */
  public void setVisitedNetworkIdentifier(DiameterIdentity vni);

}
