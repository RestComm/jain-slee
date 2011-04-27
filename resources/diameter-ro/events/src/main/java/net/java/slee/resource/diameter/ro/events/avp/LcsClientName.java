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
 * Defines an interface representing the LCS-Client-Name grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.44 LCS-Client-Name AVP The LCS-Client-Name AVP (AVP code 1235) is of type Grouped and contains the information
 * related to the name of the LCS Client.
 * 
 * It has the following ABNF grammar:
 *  LCS-Client-Name ::= AVP Header: 1235 
 *      [ LCS-Data-Coding-Scheme ] 
 *      [ LCS-Name-String ] 
 *      [ LCS-Format-Indicator ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface LcsClientName extends GroupedAvp {

  /**
   * Returns the value of the LCS-Data-Coding-Scheme AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getLcsDataCodingScheme();

  /**
   * Returns the value of the LCS-Format-Indicator AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract LcsFormatIndicator getLcsFormatIndicator();

  /**
   * Returns the value of the LCS-Name-String AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getLcsNameString();

  /**
   * Returns true if the LCS-Data-Coding-Scheme AVP is present in the message.
   */
  abstract boolean hasLcsDataCodingScheme();

  /**
   * Returns true if the LCS-Format-Indicator AVP is present in the message.
   */
  abstract boolean hasLcsFormatIndicator();

  /**
   * Returns true if the LCS-Name-String AVP is present in the message.
   */
  abstract boolean hasLcsNameString();

  /**
   * Sets the value of the LCS-Data-Coding-Scheme AVP, of type UTF8String.
   */
  abstract void setLcsDataCodingScheme(String lcsDataCodingScheme);

  /**
   * Sets the value of the LCS-Format-Indicator AVP, of type Enumerated.
   */
  abstract void setLcsFormatIndicator(LcsFormatIndicator lcsFormatIndicator);

  /**
   * Sets the value of the LCS-Name-String AVP, of type UTF8String.
   */
  abstract void setLcsNameString(String lcsNameString);

}
