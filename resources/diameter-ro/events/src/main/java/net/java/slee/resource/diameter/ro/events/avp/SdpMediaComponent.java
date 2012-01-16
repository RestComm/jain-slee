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
 * Defines an interface representing the SDP-Media-Component grouped AVP type.<br> 
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre> 
 * 7.2.95 SDP-Media-Component AVP 
 *  The SDP-Media-Component AVP (AVP code 843) is of type Grouped and contains information about media used for a IMS
 *  session. 
 *  
 *  It has the following ABNF grammar: 
 *   SDP-Media-Component ::= AVP Header: 843 
 *      [ SDP-Media-Name ] 
 *    * [ SDP-Media-Description ] 
 *      [ Media-Initiator-Flag] 
 *      [ Authorized-QoS ] 
 *      [ TGPP-Charging-Id ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface SdpMediaComponent extends GroupedAvp {

  /**
   * Returns the value of the Authorized-QoS AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getAuthorizedQos();

  /**
   * Returns the value of the Media-Initiator-Flag AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract MediaInitiatorFlag getMediaInitiatorFlag();

  /**
   * Returns the set of SDP-Media-Description AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no SDP-Media-Description AVPs have been set. The elements in the given array are String objects.
   */
  abstract String[] getSdpMediaDescriptions();

  /**
   * Returns the value of the SDP-Media-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getSdpMediaName();

  /**
   * Returns the value of the TGPP-Charging-Id AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getTgppChargingId();

  /**
   * Returns true if the Authorized-QoS AVP is present in the message.
   */
  abstract boolean hasAuthorizedQos();

  /**
   * Returns true if the Media-Initiator-Flag AVP is present in the message.
   */
  abstract boolean hasMediaInitiatorFlag();

  /**
   * Returns true if the SDP-Media-Name AVP is present in the message.
   */
  abstract boolean hasSdpMediaName();

  /**
   * Returns true if the TGPP-Charging-Id AVP is present in the message.
   */
  abstract boolean hasTgppChargingId();

  /**
   * Sets the value of the Authorized-QoS AVP, of type UTF8String.
   */
  abstract void setAuthorizedQos(String authorizedQos);

  /**
   * Sets the value of the Media-Initiator-Flag AVP, of type Enumerated.
   */
  abstract void setMediaInitiatorFlag(MediaInitiatorFlag mediaInitiatorFlag);

  /**
   * Sets a single SDP-Media-Description AVP in the message, of type UTF8String.
   */
  abstract void setSdpMediaDescription(String sdpMediaDescription);

  /**
   * Sets the set of SDP-Media-Description AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getSdpMediaDescriptions() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setSdpMediaDescriptions(String[] sdpMediaDescriptions);

  /**
   * Sets the value of the SDP-Media-Name AVP, of type UTF8String.
   */
  abstract void setSdpMediaName(String sdpMediaName);

  /**
   * Sets the value of the TGPP-Charging-Id AVP, of type OctetString.
   */
  abstract void setTgppChargingId(byte[] tgppChargingId);

}
