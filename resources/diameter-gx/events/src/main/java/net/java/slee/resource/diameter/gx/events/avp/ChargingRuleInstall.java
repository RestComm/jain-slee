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
 * Defines an interface representing the Charging-Rule-Install grouped AVP type.<br>
 * <br>
 * Charging rule provisioning over Gx interface(3GPP TS 29.210 V6.7.0) specification:
 * <pre>
 *  5.2.2 Charging-Rule-Install AVP
 *
 *  The Charging-Rule-Install AVP (AVP code 1001) is of type Grouped, and it is used to activate,
 *  install or modify charging rules for a bearer as instructed from the CRF to the TPF.
 *  For installing a new charging rule or modifying a Charging Rule already installed,
 *  Charging-Rule-Name AVP and Charging-Rule-Definition AVP shall be used.
 *  For activating a specific charging rule predefined at the TPF, Charging-Rule-Name AVP shall be used
 *  as a reference for that charging rule. The Charging-Rule-Base-Name AVP is a reference that may be used
 *  for activating a group of charging rules predefined at the TPF.
    AVP Format:
    Charging-Rule-Install ::= < AVP Header: 1001 >
                                *[ Charging-Rule-Definition ]
                                *[ Charging-Rule-Name ]
                                *[ Charging-Rule-Base-Name ]
                                *[ AVP ]
 * </pre>
 *
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public interface ChargingRuleInstall extends GroupedAvp {

    /**
     * Returns the value of the Charging-Rule-Definition AVP, of type Grouped.
     * @return String
     */
    abstract ChargingRuleDefinition getChargingRuleDefinition();

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
     * Returns true if the Charging-Rule-Definition AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasChargingRuleDefinition();

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
     * Sets the value of the Charging-Rule-Definition AVP, of type Group.
     * @param serviceIdentifier
     */
    abstract void setChargingRuleDefinition(ChargingRuleDefinition chargingRuleDefinition);

    /**
     * Sets the value of the Charging-Rule-Name AVP, of type OctetString.
     * @param serviceIdentifier
     */
    abstract void setChargingRuleName(byte[] chargingRuleName);

    /**
     * Sets the value of the Charging-Rule-Base-Name AVP, of type UTF8 String.
     * @param serviceIdentifier
     */
    abstract void setChargingRuleBaseName(String chargingRuleBaseName);

}
