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

package net.java.slee.resource.diameter.gx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Charging-Rule-Remove grouped AVP type.<br>
 * <br>
 * Charging rule provisioning over Gx interface(3GPP TS 29.210 V6.7.0) specification:
 * <pre>
 *  5.2.3 Charging-Rule-Remove AVP
 *   The Charging-Rule-Remove AVP (AVP code 1002) is of type Grouped, and it is used to deactivate or remove charging rules from a bearer.
 *   Charging-Rule-Name AVP is a reference for a specific charging rule at the TPF to be removed or for a specific charging rule predefined at the TPF to be
 *   deactivated. The Charging-Rule-Base-Name AVP is a reference for a group of charging rules predefined at the TPF to be deactivated.
 *   AVP Format:
 *   Charging-Rule-Remove ::= < AVP Header: 1002 >
 *                           *[ Charging-Rule-Name ]
 *                           *[ Charging-Rule-Base-Name ]
 *                           *[ AVP ]
 * </pre>
 *
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public interface ChargingRuleRemove extends GroupedAvp {

    /**
     * Returns the value of the Charging-Rule-Name AVP, of type Octet String.
     * @return String
     */
    abstract byte[] getChargingRuleName();

    /**
     * Returns the value of the Charging-Rule-Base-Name AVP, of type UTF8 String.
     * @return String
     */
    abstract String getChargingRuleBaseName();

    /**
     * Returns true if the Charging-Rule-Name AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasChargingRuleName();

    /**
     * Returns true if the Charging-Rule-Base-Name AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasChargingRuleBaseName();

    /**
     * Sets the value of the Charging-Rule-Name AVP, of type OctetString.
     * @param flowDescription
     */
    abstract void setChargingRuleName(byte[] chargingRuleName);

    /**
     * Sets the value of the Charging-Rule-Base-Name AVP, of type UTF8 String.
     * @param flowDescription
     */
    abstract void setChargingRuleBaseName(String chargingRuleBaseName);

}
