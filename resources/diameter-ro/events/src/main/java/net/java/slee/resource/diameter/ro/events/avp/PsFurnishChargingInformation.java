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
 * Defines an interface representing the PS-Furnish-Charging-Information grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification: 
 * <pre>
 * 7.2.86 PS-Furnish-Charging-Information AVP 
 * The PS-Furnish-Charging-Information AVP (AVP code 865) is of type Grouped. Its purpose is to add online charging
 * session specific information, received via the Ro reference point, onto the Rf reference point in order to 
 * facilitate its inclusion in CDRs.
 * This information element may be received in a CCA message via the Ro reference point. In situations where online and
 * offline charging are active in parallel, the information element is transparently copied into an ACR to be sent on
 * the Rf reference point.
 * 
 * It has the following ABNF grammar: 
 *  PS-Furnish-Charging-Information ::= AVP Header: 865 
 *      { TGPP-Charging-Id }
 *      { PS-Free-Format-Data } 
 *      [ PS-Append-Free-Format-Data ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface PsFurnishChargingInformation extends GroupedAvp {

  /**
   * Returns the value of the PS-Append-Free-Format-Data AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract PsAppendFreeFormatData getPsAppendFreeFormatData();

  /**
   * Returns the value of the PS-Free-Format-Data AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getPsFreeFormatData();

  /**
   * Returns the value of the TGPP-Charging-Id AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getTgppChargingId();

  /**
   * Returns true if the PS-Append-Free-Format-Data AVP is present in the message.
   */
  abstract boolean hasPsAppendFreeFormatData();

  /**
   * Returns true if the PS-Free-Format-Data AVP is present in the message.
   */
  abstract boolean hasPsFreeFormatData();

  /**
   * Returns true if the TGPP-Charging-Id AVP is present in the message.
   */
  abstract boolean hasTgppChargingId();

  /**
   * Sets the value of the PS-Append-Free-Format-Data AVP, of type Enumerated.
   */
  abstract void setPsAppendFreeFormatData(PsAppendFreeFormatData psAppendFreeFormatData);

  /**
   * Sets the value of the PS-Free-Format-Data AVP, of type OctetString.
   */
  abstract void setPsFreeFormatData(byte[] psFreeFormatData);

  /**
   * Sets the value of the TGPP-Charging-Id AVP, of type OctetString.
   */
  abstract void setTgppChargingId(byte[] tgppChargingId);

}
