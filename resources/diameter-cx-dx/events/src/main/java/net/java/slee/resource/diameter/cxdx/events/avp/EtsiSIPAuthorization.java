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
 * <b>6.3.36  ETSI-SIP-Authorization AVP</b>
 * The ETSI-SIP-Authorization is of type Grouped and it contains a reconstruction of either the
 * SIP Authorization or Proxy-Authorization header fields specified in IETF RFC 2617 [14].
 * 
 * AVP format
 * SIP-Digest-Authenticate ::= < AVP Header: 502 13019>
 * 						   { ETSI-Digest-Username }
 *                         { ETSI-Digest-Realm }
 *                         { ETSI-Digest-Nonce }
 *                         { ETSI-Digest-URI }
 *                         { ETSI-Digest-Response }
 *                         { ETSI-Digest-Algorithm }
 *                         { ETSI-Digest-CNonce }
 *                         { ETSI-Digest-Opaque }
 *                         { ETSI-Digest-QoP }
 *                         { ETSI-Digest-Nonce-Count }
 *                         { ETSI-Digest-Method }
 *                         { ETSI-Digest-Entity-Body-Hash}
 *                         *{ ETSI-Digest-Auth-Param }
 *                        *[ AVP ]
 *
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface EtsiSIPAuthorization extends GroupedAvp {

  /**
  * Returns true if the Digest-Username AVP is present in the message.
  */
  public boolean hasDigestUsername();

  /**
  * Returns the value of the Digest-Username AVP, of type UTF8String.
  * A return value of null implies that the AVP has not been set or some error has been encountered.
  */
  public String getDigestUsername();

  /**
  * Sets the value of the Digest-Username AVP, of type UTF8String.
  * @throws IllegalStateException if setDigestUsername has already been called
  */
  public void setDigestUsername(String digestUsername);
	  
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
   * Returns true if the Digest-Nonce AVP is present in the message.
   */
  public boolean hasDigestNonce();

  /**
   * Returns the value of the Digest-Nonce AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestNonce();

  /**
   * Sets the value of the Digest-Nonce AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestNonce has already been called
   */
  public void setDigestNonce(String digestNonce);

  /**
   * Returns true if the Digest-URI AVP is present in the message.
   */
  public boolean hasDigestURI();

  /**
   * Returns the value of the Digest-URI AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestURI();

  /**
   * Sets the value of the Digest-URI AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestURI has already been called
   */
  public void setDigestURI(String digestURI);  
  
  /**
   * Returns true if the Digest-Response AVP is present in the message.
   */
  public boolean hasDigestResponse();

  /**
   * Returns the value of the Digest-Response AVP, of type OctetString.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestResponse();

  /**
   * Sets the value of the Digest-Response AVP, of type OctetString.
   * @throws IllegalStateException if setDigestResponse has already been called
   */
  public void setDigestResponse(String digestResponse);
  
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
   * Returns true if the Digest-Opaque AVP is present in the message.
   */
  public boolean hasDigestOpaque();
  
  /**
   * Returns the value of the Digest-Opaque AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestOpaque();

  /**
   * Sets the value of the Digest-Opaque AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestOpaque has already been called
   */
  public void setDigestOpaque(String digestOpaque);     

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
  
  /**
   * Returns true if the Digest-Method AVP is present in the message.
   */
  public boolean hasDigestMethod();

  /**
   * Returns the value of the Digest-Method AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestMethod();

  /**
   * Sets the value of the Digest-Method AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestMethod has already been called
   */
  public void setDigestMethod(String digestMethod); 
  
  /**
   * Returns true if the Digest-Entity-Body-Hash AVP is present in the message.
   */
  public boolean hasDigestEntityBodyHash();

  /**
   * Returns the value of the Digest-Entity-Body-Hash AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set or some error has been encountered.
   */
  public String getDigestEntityBodyHash();

  /**
   * Sets the value of the Digest-Entity-Body-Hash AVP, of type UTF8String.
   * @throws IllegalStateException if setDigestEntityBodyHash has already been called
   */
  public void setDigestEntityBodyHash(String digestEntityBodyHash); 

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
