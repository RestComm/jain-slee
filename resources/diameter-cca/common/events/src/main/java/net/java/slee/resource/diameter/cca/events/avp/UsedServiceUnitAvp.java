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

package net.java.slee.resource.diameter.cca.events.avp;

/**
 * <pre>
 *  &lt;b&gt;8.19. Used-Service-Unit AVP&lt;/b&gt;
 * 
 * 
 *   The Used-Service-Unit AVP is of type Grouped (AVP Code 446) and
 *   contains the amount of used units measured from the point when the
 *   service became active or, if interim interrogations are used during
 *   the session, from the point when the previous measurement ended.
 * 
 *   The Used-Service-Unit AVP is defined as follows (per the grouped-
 *   avp-def of RFC 3588 [DIAMBASE]):
 * 
 *      Used-Service-Unit ::= &lt; AVP Header: 446 &gt;
 *                            [ Tariff-Change-Usage ]
 *                            [ CC-Time ]
 *                            [ CC-Money ]
 *                            [ CC-Total-Octets ]
 *                            [ CC-Input-Octets ]
 *                            [ CC-Output-Octets ]
 *                            [ CC-Service-Specific-Units ]
 *                           *[ AVP ]
 * </pre>
 *      
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface UsedServiceUnitAvp extends RequestedServiceUnitAvp {

  // TODO: This extension implies another check in impl, but thats so much easier... :]

  /**
   * Sets the value of the Tariff-Change-Usage AVP, of type Enumerated. <br>
   * See:{@link TariffChangeUsageType}
   */
  public void setTariffChangeUsage(TariffChangeUsageType ttc);

  /**
   * Returns the value of the Tariff-Change-Usage AVP, of type Enumerated.
   * <br>
   * See:{@link TariffChangeUsageType}
   */
  public TariffChangeUsageType getTariffChangeUsage();

  /**
   * Returns true if Tariff-Change-Usage AVP is present in message.
   * 
   * @return
   */
  public boolean hasTariffChangeUsage();

}
