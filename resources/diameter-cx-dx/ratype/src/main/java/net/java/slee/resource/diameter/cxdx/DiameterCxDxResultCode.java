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

package net.java.slee.resource.diameter.cxdx;

/**
 * Diameter Cx/Dx Result Codes
 */
public class DiameterCxDxResultCode {

  /**
   * The HSS informs the I-CSCF that:
   *   - The user is authorized to register this public identity;
   *   - A S-CSCF shall be assigned to the user.
   */
  public static final int  DIAMETER_FIRST_REGISTRATION = 2001;

  /**
   * The HSS informs the I-CSCF that:
   *   - The user is authorized to register this public identity;
   *   - A S-CSCF is already assigned and there is no need to select a new one.
   */
  public static final int  DIAMETER_SUBSEQUENT_REGISTRATION = 2002;

  /**
   * The HSS informs the I-CSCF that:
   *   - The public identity is not registered but has services related to unregistered state;
   *   - A S-CSCF shall be assigned to the user.
   */
  public static final int  DIAMETER_UNREGISTERED_SERVICE = 2003;

  /**
   * The HSS informs to the S-CSCF that:
   *   - The de-registration is completed;
   *   - The S-CSCF name is not stored in the HSS.
   */
  public static final int  DIAMETER_SUCCESS_SERVER_NAME_NOT_STORED = 2004;

  /**
   * A message was received for a user that is unknown.
   */
  public static final int  DIAMETER_ERROR_USER_UNKNOWN = 5001;

  /**
   * A message was received with a public identity and a private identity for a user, and the server determines that the public identity does not correspond to the private identity.
   */
  public static final int  DIAMETER_ERROR_IDENTITIES_DONT_MATCH = 5002; 

  /**
   * A query for location information is received for a public identity that has not been registered before.
   */
  public static final int  DIAMETER_ERROR_IDENTITY_NOT_REGISTERED = 5003;

  /**
   * The user is not allowed to roam in the visited network.
   */
  public static final int  DIAMETER_ERROR_ROAMING_NOT_ALLOWED = 5004;

  /**
   * The identity being registered has already a server assigned and the registration status does not allow that it is overwritten.
   */
  public static final int  DIAMETER_ERROR_IDENTITY_ALREADY_REGISTERED = 5005;

  /**
   * The authentication scheme indicated in an authentication request is not supported.
   */
  public static final int  DIAMETER_ERROR_AUTH_SCHEME_NOT_SUPPORTED = 5006;

  /**
   * The identity being registered has already the same server assigned and the registration status does not allow the server assignment type.
   */
  public static final int  DIAMETER_ERROR_IN_ASSIGNMENT_TYPE = 5007;

  /**
   * The volume of the data pushed to the receiving entity exceeds its capacity.
   */
  public static final int  DIAMETER_ERROR_TOO_MUCH_DATA = 5008;

  /**
   * The S-CSCF informs HSS that the received subscription data contained information, which was not recognised or supported.
   */
  public static final int  DIAMETER_ERROR_NOT_SUPPORTED_USER_DATA = 5009;

  /**
   * A request application message was received indicating that the origin host requests that the command pair would be handled using a feature which is not supported by the destination host.
   */
  public static final int  DIAMETER_ERROR_FEATURE_UNSUPPORTED = 5011;
}
