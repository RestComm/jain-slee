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

package net.java.slee.resource.diameter.cxdx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * <pre>
 * <b>6.3.36  ETSI-SIP-Authentication-Info AVP</b>
 * The ETSI-SIP-Authentication-Info is of type Grouped and it contains a reconstruction of Sip-Authentication-Info header specified in IETF RFC 2617 [14].
 * 
 * AVP format
 * SIP-Digest-Authenticate ::= < AVP Header: 503 13019>
 *                         { ETSI-Digest-Nextnonce }
 *                         { ETSI-Digest-QoP }
 *                         { ETSI-Digest-Response-Auth}
 *                         { ETSI-Digest-CNonce }
 *                         { ETSI-Digest-Nonce-Count }
 *                        *[ AVP ]
 *
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface EtsiSIPAuthenticationInfo extends GroupedAvp {

  /**
   * Returns true if the Digest-Nextnonce AVP is present in the message.
   */
  public boolean hasDigestNextnonce();

  /**
   * Returns the value of the Digest-Nextnonce AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestNextnonce();

  /**
   * Sets the value of the Digest-Nextnonce AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestNextnonce has already been called
   */
  public void setDigestNextnonce(String digestNextnonce);

  /**
   * Returns true if the Digest-QoP AVP is present in the message.
   */
  public boolean hasDigestQoP();

  /**
   * Returns the value of the Digest-QoP AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestQoP();

  /**
   * Sets the value of the Digest-QoP AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestQoP has already been called
   */
  public void setDigestQoP(String digestQoP);
  
  /**
   * Returns true if the Digest-Response-Auth AVP is present in the message.
   */
  public boolean hasDigestResponseAuth();

  /**
   * Returns the value of the Digest-Response-Auth AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestResponseAuth();

  /**
   * Sets the value of the Digest-Response-Auth AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestResponseAuth has already been called
   */
  public void setDigestResponseAuth(String digestResponseAuth);
  
  /**
   * Returns true if the Digest-CNonce AVP is present in the message.
   */
  public boolean hasDigestCNonce();

  /**
   * Returns the value of the Digest-CNonce AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestCNonce();

  /**
   * Sets the value of the Digest-CNonce AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestCNonce has already been called
   */
  public void setDigestCNonce(String digestCNonce);
  
  /**
   * Returns true if the Digest-Nonce-Count AVP is present in the message.
   */
  public boolean hasDigestNonceCount();

  /**
   * Returns the value of the Digest-Nonce-Count AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestNonceCount();

  /**
   * Sets the value of the Digest-Nonce-Count AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestNonceCount has already been called
   */
  public void setDigestNonceCount(String digestNonceCount);  
}
