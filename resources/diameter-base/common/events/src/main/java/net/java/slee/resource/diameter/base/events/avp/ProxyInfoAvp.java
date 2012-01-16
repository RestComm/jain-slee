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

package net.java.slee.resource.diameter.base.events.avp;

/**
 * Defines an interface representing the Proxy-Info grouped AVP type.
 *
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * <pre>
 * 6.7.2.  Proxy-Info AVP
 * 
 *    The Proxy-Info AVP (AVP Code 284) is of type Grouped.  The Grouped
 *    Data field has the following ABNF grammar:
 * 
 *       Proxy-Info ::= &lt; AVP Header: 284 &gt;
 *                      { Proxy-Host }
 *                      { Proxy-State }
 *                    * [ AVP ]
 * </pre>
 */
public interface ProxyInfoAvp extends GroupedAvp {

  /**
   * Returns true if the Proxy-Host AVP is present in the message.
   */
  public boolean hasProxyHost();

  /**
   * Returns the value of the Proxy-Host AVP, of type DiameterIdentity.
   * A return value of null implies that the AVP has not been set.
   */
  public DiameterIdentity getProxyHost();

  /**
   * Sets the value of the Proxy-Host AVP, of type DiameterIdentity.
   * @throws IllegalStateException if setProxyHost has already been called
   */
  public void setProxyHost(DiameterIdentity proxyHost);

  /**
   * Returns true if the Proxy-State AVP is present in the message.
   */
  public boolean hasProxyState();

  /**
   * Returns the value of the Proxy-State AVP, of type OctetString.
   * A return value of null implies that the AVP has not been set.
   */
  public byte[] getProxyState();

  /**
   * Sets the value of the Proxy-State AVP, of type OctetString.
   * @throws IllegalStateException if setProxyState has already been called
   */
  public void setProxyState(byte[] proxyState);

}
