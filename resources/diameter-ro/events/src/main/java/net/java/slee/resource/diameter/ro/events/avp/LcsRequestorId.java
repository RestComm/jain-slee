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
 * Defines an interface representing the LCS-Requestor-ID grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.50 LCS-Requestor-ID AVP
 * The LCS-Requestor-ID AVP (AVP code 1239) is of type Grouped and contains information related to the identification
 * of the Requestor. 
 * 
 * It has the following ABNF grammar: 
 *  LCS-Requestor-ID ::= AVP Header: 1239 
 *      [ LCS-Data-Coding-Scheme ] 
 *      [ LCS-Requestor-ID-String ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface LcsRequestorId extends GroupedAvp {

  /**
   * Returns the value of the LCS-Data-Coding-Scheme AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getLcsDataCodingScheme();

  /**
   * Returns the value of the LCS-Requestor-ID-String AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getLcsRequestorIdString();

  /**
   * Returns true if the LCS-Data-Coding-Scheme AVP is present in the message.
   */
  abstract boolean hasLcsDataCodingScheme();

  /**
   * Returns true if the LCS-Requestor-ID-String AVP is present in the message.
   */
  abstract boolean hasLcsRequestorIdString();

  /**
   * Sets the value of the LCS-Data-Coding-Scheme AVP, of type UTF8String.
   */
  abstract void setLcsDataCodingScheme(String lcsDataCodingScheme);

  /**
   * Sets the value of the LCS-Requestor-ID-String AVP, of type UTF8String.
   */
  abstract void setLcsRequestorIdString(String lcsRequestorIdString);

}
