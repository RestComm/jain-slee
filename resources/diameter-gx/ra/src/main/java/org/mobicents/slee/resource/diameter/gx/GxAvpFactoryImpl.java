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

package org.mobicents.slee.resource.diameter.gx;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.gx.GxAvpFactory;
import net.java.slee.resource.diameter.gx.events.avp.BearerUsage;
import net.java.slee.resource.diameter.gx.events.avp.ChargingInformation;
import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition;
import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall;
import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleRemove;
import net.java.slee.resource.diameter.gx.events.avp.EventTrigger;
import net.java.slee.resource.diameter.gx.events.avp.Flows;
import net.java.slee.resource.diameter.gx.events.avp.MeteringMethod;
import net.java.slee.resource.diameter.gx.events.avp.Offline;
import net.java.slee.resource.diameter.gx.events.avp.Online;
import net.java.slee.resource.diameter.gx.events.avp.PDPSessionOperation;
import net.java.slee.resource.diameter.gx.events.avp.ReportingLevel;
import net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation;
import org.mobicents.slee.resource.diameter.cca.CreditControlAVPFactoryImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.ChargingInformationImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.ChargingRuleDefinitionImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.ChargingRuleInstallImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.ChargingRuleRemoveImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.DiameterGxAvpCodes;
import org.mobicents.slee.resource.diameter.gx.events.avp.FlowsImpl;
import org.mobicents.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformationImpl;

/**
 * Implementation of {@link GxAvpFactory}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public class GxAvpFactoryImpl extends CreditControlAVPFactoryImpl implements GxAvpFactory {

    // TODO: Add helper create methods for the composite AVPs

    public GxAvpFactoryImpl(final DiameterAvpFactory baseAvpFactory) {
        super(baseAvpFactory);

        // FIXME: isn't the super class doing this?
        this.baseAvpFactory = baseAvpFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CreditControlAVPFactory getCreditControlAVPFactory() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BearerUsage createBearerUsage() {
        return (BearerUsage) AvpUtilities.createAvp(DiameterGxAvpCodes.BEARER_USAGE, DiameterGxAvpCodes.TGPP_VENDOR_ID, null, BearerUsage.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingInformation createChargingInformation() {
        return (ChargingInformation) AvpUtilities.createAvp(DiameterGxAvpCodes.CHARGING_INFORMATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, null,
              ChargingInformationImpl.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingRuleDefinition createChargingRuleDefinition() {
        return (ChargingRuleDefinition) AvpUtilities.createAvp(DiameterGxAvpCodes.CHARGING_RULE_DEFINITION, DiameterGxAvpCodes.TGPP_VENDOR_ID, null,
              ChargingRuleDefinitionImpl.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingRuleInstall createChargingRuleInstall() {
        return (ChargingRuleInstall) AvpUtilities.createAvp(DiameterGxAvpCodes.CHARGING_RULE_INSTALL, DiameterGxAvpCodes.TGPP_VENDOR_ID, null,
              ChargingRuleInstallImpl.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingRuleRemove createChargingRuleRemove() {
        return (ChargingRuleRemove) AvpUtilities.createAvp(DiameterGxAvpCodes.CHARGING_RULE_REMOVE, DiameterGxAvpCodes.TGPP_VENDOR_ID, null,
              ChargingRuleRemoveImpl.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventTrigger createEventTrigger() {
        return (EventTrigger) AvpUtilities.createAvp(DiameterGxAvpCodes.EVENT_TRIGGER, DiameterGxAvpCodes.TGPP_VENDOR_ID, null, EventTrigger.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flows createFlows() {
        return (Flows) AvpUtilities.createAvp(DiameterGxAvpCodes.FLOWS, DiameterGxAvpCodes.TGPP_VENDOR_ID, null, FlowsImpl.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MeteringMethod createMeteringMethod() {
        return (MeteringMethod) AvpUtilities.createAvp(DiameterGxAvpCodes.METERING_METHOD, DiameterGxAvpCodes.TGPP_VENDOR_ID, null, MeteringMethod.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Offline createOffline() {
        return (Offline) AvpUtilities.createAvp(DiameterGxAvpCodes.OFFLINE, DiameterGxAvpCodes.TGPP_VENDOR_ID, null, Offline.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Online createOnline() {
        return (Online) AvpUtilities.createAvp(DiameterGxAvpCodes.ONLINE, DiameterGxAvpCodes.TGPP_VENDOR_ID, null, Online.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PDPSessionOperation createPDPSessionOperation() {
        return (PDPSessionOperation) AvpUtilities.createAvp(DiameterGxAvpCodes.PDP_SESSION_OPERATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, null,
              PDPSessionOperation.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportingLevel createReportingLevel() {
        return (ReportingLevel) AvpUtilities.createAvp(DiameterGxAvpCodes.REPORTING_LEVEL, DiameterGxAvpCodes.TGPP_VENDOR_ID, null, ReportingLevel.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TFTPacketFilterInformation createTFTPacketFilterInformation() {
        return (TFTPacketFilterInformation) AvpUtilities.createAvp(DiameterGxAvpCodes.TFT_PACKET_FILTER_INFORMATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, null,
              TFTPacketFilterInformationImpl.class);
    }
}
