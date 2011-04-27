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

package net.java.slee.resource.diameter.gx;

import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
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

/**
 * Used by applications to create Diameter Gx request messages.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxAvpFactory extends CreditControlAVPFactory {

    CreditControlAVPFactory getCreditControlAVPFactory();

    /**
     * Create an empty BearerUsage (Enumerated AVP) instance.
     * @return
     */
    BearerUsage createBearerUsage();

    /**
     * Create an empty ChargingInformation (Enumerated AVP) instance.
     * @return
     */
    ChargingInformation createChargingInformation();

    /**
     * Create an empty ChargingRuleDefinition (Grouped AVP) instance.
     * @return
     */
    ChargingRuleDefinition createChargingRuleDefinition();

    /**
     * Create an empty ChargingRuleInstall (Grouped AVP) instance.
     * @return
     */
    ChargingRuleInstall createChargingRuleInstall();

    /**
     * Create an empty ChargingRuleRemove (Grouped AVP) instance.
     * @return
     */
    ChargingRuleRemove createChargingRuleRemove();

    /**
     * Create an empty EventTrigger (Enumerated AVP) instance.
     * @return
     */
    EventTrigger createEventTrigger();

    /**
     * Create an empty Flows (Grouped AVP) instance.
     * @return
     */
    Flows createFlows();

    /**
     * Create an empty MeteringMethod (Enumerated AVP) instance.
     * @return
     */
    MeteringMethod createMeteringMethod();

    /**
     * Create an empty Offline (Enumerated AVP) instance.
     * @return
     */
    Offline createOffline();

    /**
     * Create an empty Online (Enumerated AVP) instance.
     * @return
     */
    Online createOnline();

    /**
     * Create an empty PDPSessionOperation (Enumerated AVP) instance.
     * @return
     */
    PDPSessionOperation createPDPSessionOperation();

    /**
     * Create an empty ReportingLevel (Enumerated AVP) instance.
     * @return
     */
    ReportingLevel createReportingLevel();

    /**
     * Create an empty TFTPacketFilterInformation (Grouped AVP) instance.
     * @return
     */
    TFTPacketFilterInformation createTFTPacketFilterInformation();
}
