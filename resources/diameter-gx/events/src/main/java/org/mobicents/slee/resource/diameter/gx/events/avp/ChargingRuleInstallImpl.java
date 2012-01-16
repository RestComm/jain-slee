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

package org.mobicents.slee.resource.diameter.gx.events.avp;

import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall;
import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class ChargingRuleInstallImpl extends GroupedAvpImpl implements ChargingRuleInstall {

    public ChargingRuleInstallImpl() {
         super();
    }

    public ChargingRuleInstallImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#getChargingRuleDefinition()
     */
    public ChargingRuleDefinition getChargingRuleDefinition() {
        return (ChargingRuleDefinition) getAvpAsCustom(DiameterGxAvpCodes.CHARGING_RULE_DEFINITION, DiameterGxAvpCodes.TGPP_VENDOR_ID, ChargingRuleDefinitionImpl.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#getChargingRuleName()
     */
    public byte[] getChargingRuleName() {
        return getAvpAsOctetString(DiameterGxAvpCodes.CHARGING_RULE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#getChargingRuleBaseName()
     */
    public String getChargingRuleBaseName() {
        return getAvpAsUTF8String(DiameterGxAvpCodes.CHARGING_RULE_BASE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#hasChargingRuleDefinition()
     */
    public boolean hasChargingRuleDefinition() {
        return hasAvp( DiameterGxAvpCodes.CHARGING_RULE_DEFINITION, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#hasChargingRuleName()
     */
    public boolean hasChargingRuleName() {
        return hasAvp( DiameterGxAvpCodes.CHARGING_RULE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#hasChargingRuleBaseName()
     */
    public boolean hasChargingRuleBaseName() {
        return hasAvp( DiameterGxAvpCodes.CHARGING_RULE_BASE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#setChargingRuleDefinition(net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall)
     */
    public void setChargingRuleDefinition(ChargingRuleDefinition chargingRuleDefinition) {
        addAvp(DiameterGxAvpCodes.CHARGING_RULE_DEFINITION, DiameterGxAvpCodes.TGPP_VENDOR_ID, chargingRuleDefinition.byteArrayValue());
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#setChargingRuleName(String)
     */
    public void setChargingRuleName(byte[] chargingRuleName) {
        addAvp(DiameterGxAvpCodes.CHARGING_RULE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID, chargingRuleName);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall#setChargingRuleBaseName(String)
     */
    public void setChargingRuleBaseName(String chargingRuleBaseName) {
        addAvp(DiameterGxAvpCodes.CHARGING_RULE_BASE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID, chargingRuleBaseName);
    }

}
