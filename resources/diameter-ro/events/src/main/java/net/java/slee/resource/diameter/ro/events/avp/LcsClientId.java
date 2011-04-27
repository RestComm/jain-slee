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

package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the LCS-Client-ID grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.43 LCS-Client-ID AVP 
 * The LCS-Client-Id AVP (AVP code 1232) is of type Grouped and holds information related to the identity of an LCS
 * client. 
 * 
 * It has the following ABNF grammar: 
 *  LCS-Client-ID ::= AVP Header: 1232 
 *      [ LCS-Client-Type ] 
 *      [ LCS-Client-External-ID ] 
 *      [ LCS-Client-Dialed-By-MS ] 
 *      [ LCS-Client-Name ]
 *      [ LCS-APN ] 
 *      [ LCS-Requestor-ID ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface LcsClientId extends GroupedAvp {

  /**
   * Returns the value of the LCS-APN AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getLcsApn();

  /**
   * Returns the value of the LCS-Client-Dialed-By-MS AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getLcsClientDialedByMs();

  /**
   * Returns the value of the LCS-Client-External-ID AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getLcsClientExternalId();

  /**
   * Returns the value of the LCS-Client-Name AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract LcsClientName getLcsClientName();

  /**
   * Returns the value of the LCS-Client-Type AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract LcsClientType getLcsClientType();

  /**
   * Returns the value of the LCS-Requestor-ID AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract LcsRequestorId getLcsRequestorId();

  /**
   * Returns true if the LCS-APN AVP is present in the message.
   */
  abstract boolean hasLcsApn();

  /**
   * Returns true if the LCS-Client-Dialed-By-MS AVP is present in the message.
   */
  abstract boolean hasLcsClientDialedByMs();

  /**
   * Returns true if the LCS-Client-External-ID AVP is present in the message.
   */
  abstract boolean hasLcsClientExternalId();

  /**
   * Returns true if the LCS-Client-Name AVP is present in the message.
   */
  abstract boolean hasLcsClientName();

  /**
   * Returns true if the LCS-Client-Type AVP is present in the message.
   */
  abstract boolean hasLcsClientType();

  /**
   * Returns true if the LCS-Requestor-ID AVP is present in the message.
   */
  abstract boolean hasLcsRequestorId();

  /**
   * Sets the value of the LCS-APN AVP, of type UTF8String.
   */
  abstract void setLcsApn(String lcsApn);

  /**
   * Sets the value of the LCS-Client-Dialed-By-MS AVP, of type UTF8String.
   */
  abstract void setLcsClientDialedByMs(String lcsClientDialedByMs);

  /**
   * Sets the value of the LCS-Client-External-ID AVP, of type UTF8String.
   */
  abstract void setLcsClientExternalId(String lcsClientExternalId);

  /**
   * Sets the value of the LCS-Client-Name AVP, of type Grouped.
   */
  abstract void setLcsClientName(LcsClientName lcsClientName);

  /**
   * Sets the value of the LCS-Client-Type AVP, of type Enumerated.
   */
  abstract void setLcsClientType(LcsClientType lcsClientType);

  /**
   * Sets the value of the LCS-Requestor-ID AVP, of type Grouped.
   */
  abstract void setLcsRequestorId(LcsRequestorId lcsRequestorId);

}
