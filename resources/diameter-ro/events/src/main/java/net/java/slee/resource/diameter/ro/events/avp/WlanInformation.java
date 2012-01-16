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
 * Defines an interface representing the WLAN-Information grouped AVP type.<br> 
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.121 WLAN-Information AVP
 *  The WLAN-Information AVP (AVP code 875) is of type Grouped. 
 *  Its purpose is to allow the transmission of additional WLAN service specific information elements. 
 *  The format and the contents of the fields inside the WLAN- Information AVP is specified in TS 32.252 [22]. 
 *  
 *  It has the following ABNF grammar: 
 *    WLAN-Information ::= AVP Header: 875 
 *      [ WLAN-Session-Id ] 
 *      [ PDG-Address ] 
 *      [ PDG-Charging-Id ] 
 *      [ WAG-Address ] 
 *      [ WAG-PLMN-Id ] 
 *      [ WLAN-Radio-Container ] 
 *      [ WLAN-UE-Local-IPAddress ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface WlanInformation extends GroupedAvp {

  /**
   * Returns the value of the PDG-Address AVP, of type Address. A return value of null implies that the AVP has not been set.
   */
  abstract net.java.slee.resource.diameter.base.events.avp.Address getPdgAddress();

  /**
   * Returns the value of the PDG-Charging-Id AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getPdgChargingId();

  /**
   * Returns the value of the WAG-Address AVP, of type Address. A return value of null implies that the AVP has not been set.
   */
  abstract net.java.slee.resource.diameter.base.events.avp.Address getWagAddress();

  /**
   * Returns the value of the WAG-PLMN-Id AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getWagPlmnId();

  /**
   * Returns the value of the WLAN-Radio-Container AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract WlanRadioContainer getWlanRadioContainer();

  /**
   * Returns the value of the WLAN-Session-Id AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getWlanSessionId();

  /**
   * Returns the value of the WLAN-UE-Local-IPAddress AVP, of type Address. A return value of null implies that the AVP has not been set.
   */
  abstract net.java.slee.resource.diameter.base.events.avp.Address getWlanUeLocalIpaddress();

  /**
   * Returns true if the PDG-Address AVP is present in the message.
   */
  abstract boolean hasPdgAddress();

  /**
   * Returns true if the PDG-Charging-Id AVP is present in the message.
   */
  abstract boolean hasPdgChargingId();

  /**
   * Returns true if the WAG-Address AVP is present in the message.
   */
  abstract boolean hasWagAddress();

  /**
   * Returns true if the WAG-PLMN-Id AVP is present in the message.
   */
  abstract boolean hasWagPlmnId();

  /**
   * Returns true if the WLAN-Radio-Container AVP is present in the message.
   */
  abstract boolean hasWlanRadioContainer();

  /**
   * Returns true if the WLAN-Session-Id AVP is present in the message.
   */
  abstract boolean hasWlanSessionId();

  /**
   * Returns true if the WLAN-UE-Local-IPAddress AVP is present in the message.
   */
  abstract boolean hasWlanUeLocalIpaddress();

  /**
   * Sets the value of the PDG-Address AVP, of type Address.
   */
  abstract void setPdgAddress(net.java.slee.resource.diameter.base.events.avp.Address pdgAddress);

  /**
   * Sets the value of the PDG-Charging-Id AVP, of type Unsigned32.
   */
  abstract void setPdgChargingId(long pdgChargingId);

  /**
   * Sets the value of the WAG-Address AVP, of type Address.
   */
  abstract void setWagAddress(net.java.slee.resource.diameter.base.events.avp.Address wagAddress);

  /**
   * Sets the value of the WAG-PLMN-Id AVP, of type OctetString.
   */
  abstract void setWagPlmnId(byte[] wagPlmnId);

  /**
   * Sets the value of the WLAN-Radio-Container AVP, of type Grouped.
   */
  abstract void setWlanRadioContainer(WlanRadioContainer wlanRadioContainer);

  /**
   * Sets the value of the WLAN-Session-Id AVP, of type UTF8String.
   */
  abstract void setWlanSessionId(String wlanSessionId);

  /**
   * Sets the value of the WLAN-UE-Local-IPAddress AVP, of type Address.
   */
  abstract void setWlanUeLocalIpaddress(net.java.slee.resource.diameter.base.events.avp.Address wlanUeLocalIpaddress);

}
