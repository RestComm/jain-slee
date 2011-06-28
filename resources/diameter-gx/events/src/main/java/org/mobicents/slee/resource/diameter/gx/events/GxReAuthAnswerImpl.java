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

package org.mobicents.slee.resource.diameter.gx.events;

import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall;
import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleRemove;
import net.java.slee.resource.diameter.gx.events.avp.EventTrigger;
import org.jdiameter.api.Message;


import net.java.slee.resource.diameter.gx.events.GxReAuthAnswer;
import net.java.slee.resource.diameter.gx.events.avp.ChargingInformation;

import org.mobicents.slee.resource.diameter.gx.events.avp.ChargingInformationImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.ChargingRuleInstallImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.ChargingRuleRemoveImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.DiameterGxAvpCodes;

/**
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public class GxReAuthAnswerImpl extends GxReAuthMessageImpl implements GxReAuthAnswer {

    /**
     * @param message
     */
    public GxReAuthAnswerImpl(Message message) {
        super(message);
    }

    /* (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#getEventTrigger()
     */
    public EventTrigger getEventTrigger() {
        return (EventTrigger) getAvpAsEnumerated(DiameterGxAvpCodes.EVENT_TRIGGER, DiameterGxAvpCodes.TGPP_VENDOR_ID, EventTrigger.class);
    }

    /* (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#hasEventTrigger()
     */
    public boolean hasEventTrigger() {
        return hasAvp(DiameterGxAvpCodes.EVENT_TRIGGER, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#etEventTrigger()
     */
    public void setEventTrigger(EventTrigger eventTrigger) {
        addAvp(DiameterGxAvpCodes.EVENT_TRIGGER, DiameterGxAvpCodes.TGPP_VENDOR_ID, eventTrigger.getValue());
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#getChargingRuleRemove()
     */
    public ChargingRuleRemove getChargingRuleRemove() {
        return (ChargingRuleRemove) getAvpAsCustom(DiameterGxAvpCodes.CHARGING_RULE_REMOVE, DiameterGxAvpCodes.TGPP_VENDOR_ID, ChargingRuleRemoveImpl.class);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#hasChargingRuleRemove()
     */
    public boolean hasChargingRuleRemove() {
        return hasAvp(DiameterGxAvpCodes.CHARGING_RULE_REMOVE, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#setChargingRuleRemove()
     */
    public void setChargingRuleRemove(ChargingRuleRemove chargingRuleRemove) {
        addAvp(DiameterGxAvpCodes.CHARGING_RULE_REMOVE, DiameterGxAvpCodes.TGPP_VENDOR_ID, chargingRuleRemove.byteArrayValue());
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#getChargingRuleInstall()
     */
    public ChargingRuleInstall getChargingRuleInstall() {
        return (ChargingRuleInstall) getAvpAsCustom(DiameterGxAvpCodes.CHARGING_RULE_INSTALL, DiameterGxAvpCodes.TGPP_VENDOR_ID, ChargingRuleInstallImpl.class);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#hasChargingRuleInstall()
     */
    public boolean hasChargingRuleInstall() {
        return hasAvp(DiameterGxAvpCodes.CHARGING_RULE_INSTALL, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#setChargingRuleInstall()
     */
    public void setChargingRuleInstall(ChargingRuleInstall chargingRuleInstall) {
        addAvp(DiameterGxAvpCodes.CHARGING_RULE_INSTALL, DiameterGxAvpCodes.TGPP_VENDOR_ID, chargingRuleInstall.byteArrayValue());
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#getChargingInformation()
     */
    public ChargingInformation getChargingInformation() {
        return (ChargingInformation) getAvpAsCustom(DiameterGxAvpCodes.CHARGING_INFORMATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, ChargingInformationImpl.class);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#hasChargingInformation()
     */
    public boolean hasChargingInformation() {
        return hasAvp(DiameterGxAvpCodes.CHARGING_INFORMATION, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxReAuthAnswer#setChargingInformation()
     */
    public void setChargingInformation(ChargingInformation chargingInformation) {
        addAvp(DiameterGxAvpCodes.CHARGING_INFORMATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, chargingInformation.byteArrayValue());
    }

    @Override
    public String getLongName() {
      return "Re-Auth-Answer";
    }

    @Override
    public String getShortName() {
      return "RAA";
    }
}
