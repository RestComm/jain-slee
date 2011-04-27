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

package net.java.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the WLAN-Radio-Container grouped AVP type.<br>
 * <br>
 * From the Diameter Rf Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.122 WLAN-Radio-Container AVP
 *  The WLAN-Radio-Container AVP (AVP code 892) is of type Grouped.
 * 
 * The WLAN- Radio-Container AVP has the following format: 
 * The Operator-Name, Location-Type and Location-Information AVPs are defined in TS 29.234 [212]. 
 *  WLAN-Radio-Container ::= AVP Header: 892 
 *      [ Operator-Name ] #exclude 
 *      [ Location-Type ] 
 *      [ Location-Information ] #exclude 
 *      [ WLAN-Technology ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface WlanRadioContainer extends GroupedAvp {

  /**
   * Returns the value of the Location-Type AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract LocationType getLocationType();

  /**
   * Returns the value of the WLAN-Technology AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getWlanTechnology();

  /**
   * Returns true if the Location-Type AVP is present in the message.
   */
  abstract boolean hasLocationType();

  /**
   * Returns true if the WLAN-Technology AVP is present in the message.
   */
  abstract boolean hasWlanTechnology();

  /**
   * Sets the value of the Location-Type AVP, of type Grouped.
   */
  abstract void setLocationType(LocationType locationType);

  /**
   * Sets the value of the WLAN-Technology AVP, of type Unsigned32.
   */
  abstract void setWlanTechnology(long wlanTechnology);

}
