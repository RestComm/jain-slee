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
 * Defines an interface representing the MBMS-Information grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.55 MBMS-Information AVP 
 * The MBMS-Information AVP (AVP code 880) is of type Grouped. Its purpose is to allow the transmission of additional MBMS service specific information elements. 
 * 
 * It has the following ABNF grammar: 
 *  MBMS-Information ::= AVP Header: 880 
 *       { TMGI } 
 *       { MBMS-Service-Type } 
 *       { MBMS-User-Service-Type } 
 *       [ File-Repair-Supported ] 
 *       [ Required-MBMS-Bearer-Capabilities ] 
 *       [ MBMS-2G-3G-Indicator ] 
 *       [ RAI ] 
 *     * [ MBMS-Service-Area ] 
 *       [ MBMS-Session-Identity ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface MbmsInformation extends GroupedAvp {

  /**
   * Returns the value of the File-Repair-Supported AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract FileRepairSupported getFileRepairSupported();

  /**
   * Returns the value of the MBMS-2G-3G-Indicator AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract Mbms2g3gIndicator getMbms2g3gIndicator();

  /**
   * Returns the set of MBMS-Service-Area AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no MBMS-Service-Area AVPs have been set. The elements in the given array are byte[] objects.
   */
  abstract byte[][] getMbmsServiceAreas();

  /**
   * Returns the value of the MBMS-Service-Type AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract MbmsServiceType getMbmsServiceType();

  /**
   * Returns the value of the MBMS-Session-Identity AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getMbmsSessionIdentity();

  /**
   * Returns the value of the MBMS-User-Service-Type AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract MbmsUserServiceType getMbmsUserServiceType();

  /**
   * Returns the value of the RAI AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getRai();

  /**
   * Returns the value of the Required-MBMS-Bearer-Capabilities AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getRequiredMbmsBearerCapabilities();

  /**
   * Returns the value of the TMGI AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getTmgi();

  /**
   * Returns true if the File-Repair-Supported AVP is present in the message.
   */
  abstract boolean hasFileRepairSupported();

  /**
   * Returns true if the MBMS-2G-3G-Indicator AVP is present in the message.
   */
  abstract boolean hasMbms2g3gIndicator();

  /**
   * Returns true if the MBMS-Service-Type AVP is present in the message.
   */
  abstract boolean hasMbmsServiceType();

  /**
   * Returns true if the MBMS-Session-Identity AVP is present in the message.
   */
  abstract boolean hasMbmsSessionIdentity();

  /**
   * Returns true if the MBMS-User-Service-Type AVP is present in the message.
   */
  abstract boolean hasMbmsUserServiceType();

  /**
   * Returns true if the RAI AVP is present in the message.
   */
  abstract boolean hasRai();

  /**
   * Returns true if the Required-MBMS-Bearer-Capabilities AVP is present in the message.
   */
  abstract boolean hasRequiredMbmsBearerCapabilities();

  /**
   * Returns true if the TMGI AVP is present in the message.
   */
  abstract boolean hasTmgi();

  /**
   * Sets the value of the File-Repair-Supported AVP, of type Enumerated.
   */
  abstract void setFileRepairSupported(FileRepairSupported fileRepairSupported);

  /**
   * Sets the value of the MBMS-2G-3G-Indicator AVP, of type OctetString.
   */
  abstract void setMbms2g3gIndicator(Mbms2g3gIndicator mbms2g3gIndicator);

  /**
   * Sets a single MBMS-Service-Area AVP in the message, of type OctetString.
   */
  abstract void setMbmsServiceArea(byte[] mbmsServiceArea);

  /**
   * Sets the set of MBMS-Service-Area AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getMbmsServiceAreas() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setMbmsServiceAreas(byte[][] mbmsServiceAreas);

  /**
   * Sets the value of the MBMS-Service-Type AVP, of type Enumerated.
   */
  abstract void setMbmsServiceType(MbmsServiceType mbmsServiceType);

  /**
   * Sets the value of the MBMS-Session-Identity AVP, of type OctetString.
   */
  abstract void setMbmsSessionIdentity(byte[] mbmsSessionIdentity);

  /**
   * Sets the value of the MBMS-User-Service-Type AVP, of type Enumerated.
   */
  abstract void setMbmsUserServiceType(MbmsUserServiceType mbmsUserServiceType);

  /**
   * Sets the value of the RAI AVP, of type UTF8String.
   */
  abstract void setRai(String rai);

  /**
   * Sets the value of the Required-MBMS-Bearer-Capabilities AVP, of type UTF8String.
   */
  abstract void setRequiredMbmsBearerCapabilities(String requiredMbmsBearerCapabilities);

  /**
   * Sets the value of the TMGI AVP, of type OctetString.
   */
  abstract void setTmgi(byte[] tmgi);

}
