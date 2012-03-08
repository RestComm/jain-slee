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
 * <b>6.3.36  SIP-Digest-Authenticate AVP</b>
 * The SIP-Digest-Authenticate is of type Grouped and it contains a reconstruction of either the
 * SIP WWW-Authenticate or Proxy-Authentication header fields specified in IETF RFC 2617 [14].
 * 
 * AVP format
 * SIP-Digest-Authenticate ::= < AVP Header: 228 4491>
 *                         { Digest-Realm }
 *                         { Digest-Domain }
 *                         [ Digest-Algorithm ]
 *                         { Digest-QoP }
 *                         { Digest-HA1}
 *                         *{ Digest-Auth-Param}
 *                        *[ AVP ]
 *
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface CableLabsSIPDigestAuthenticate extends GroupedAvp {

  /**
   * Returns true if the Digest-Realm AVP is present in the message.
   */
  public boolean hasDigestRealm();

  /**
   * Returns the value of the Digest-Realm AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestRealm();

  /**
   * Sets the value of the Digest-Realm AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestRealm has already been called
   */
  public void setDigestRealm(String digestRealm);
  
  /**
   * Returns true if the Digest-Domain AVP is present in the message.
   */
  public boolean hasDigestDomain();

  /**
   * Returns the value of the Digest-Domain AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestDomain();

  /**
   * Sets the value of the Digest-Domain AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestRealm has already been called
   */
  public void setDigestDomain(String digestDomain);

  /**
   * Returns true if the Digest-Algorithm AVP is present in the message.
   */
  public boolean hasDigestAlgorithm();

  /**
   * Returns the value of the Digest-Algorithm AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestAlgorithm();

  /**
   * Sets the value of the Digest-Algorithm AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestAlgorithm has already been called
   */
  public void setDigestAlgorithm(String digestAlgorithm);

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
   * Returns true if the Digest-HA1 AVP is present in the message.
   */
  public boolean hasDigestHA1();

  /**
   * Returns the value of the Digest-HA1 AVP, of type OctetString.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public byte[] getDigestHA1();

  /**
   * Sets the value of the Digest-HA1 AVP, of type OctetString.
   * @throws IllegalStateException if setDigestHA1 has already been called
   */
  public void setDigestHA1(byte[] digestHA1);
  
  /**
   * Returns the value of the Digest-Auth-Param AVP, of type OctetString.
   * @return the value of the Digest-Auth-Param AVP or null if it has not been set on this message
   */
  byte[][] getDigestAuthParams();

  /**
   * Sets the value of the Digest-Auth-Param AVP, of type OctetString.
   * @throws IllegalStateException if setDigestAuthParam has already been called
   */
  void setDigestAuthParam(byte[] digestAuthParam) throws IllegalStateException;

  /**
   * Sets the value of the Digest-Auth-Param AVP, of type OctetString.
   * @throws IllegalStateException if setDigestAuthParam has already been called
   */
  void setDigestAuthParams(byte[][] digestAuthParams) throws IllegalStateException;

}
