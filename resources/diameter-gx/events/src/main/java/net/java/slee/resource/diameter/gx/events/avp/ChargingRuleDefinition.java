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
import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;

/**
 * Defines an interface representing the Charging-Rule-Definition grouped AVP type.<br>
 * <br>
 * Charging rule provisioning over Gx interface(3GPP TS 29.210 V6.7.0) specification:
 * <pre>
 *  5.2.4 Charging-Rule-Definition AVP
 *  The Charging-Rule-Definition AVP (AVP code 1003) is of type Grouped, and it defines the charging rule for a service flow
 *  sent by the CRF to the TPF. The Charging-Rule-Name AVP uniquely identifies the charging rule for a bearer and
 *  it is used to reference to a charging rule in communication between the TPF and the CRF. The Flow-Description AVP(s)
 *  determines the traffic that belongs to the service flow.
    If optional AVP(s) within a Charging-Rule-Definition AVP are omitted, but corresponding information has been provided in
 *  previous Gx messages, the previous information remains valid. If Flow-Description AVP(s) are supplied, they replace all
 *   previous Flow-Description AVP(s). If Flows AVP(s) are supplied, they replace all previous Flows AVP(s).
 *   Flows AVP may appear if and only if AF-Charging-Identifier AVP is also present.
 *   AVP Format:
 *   Charging-Rule-Definition ::= < AVP Header: 1003 >
 *                               { Charging-Rule-Name }
 *                               [ Service-Identifier ]
 *                               [ Rating-Group ]
 *                               *[ Flow-Description ]
 *                               [ Reporting-Level ]
 *                               [ Online ]
 *                               [ Offline ]
 *                               [ Metering-Method ]
 *                               [ Precedence ]
 *                               [ AF-Charging-Identifier ]
 *                               *[ Flows ]
 *                               *[ AVP ]
 * </pre>
 *
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public interface ChargingRuleDefinition extends GroupedAvp {

    /**
     * Returns the value of the Charging-Rule-Name AVP, of type Octet String.
     * @return String
     */
    abstract byte[] getChargingRuleName();

    /**
     * Returns the value of the Service-Identifier AVP, of type long.
     * @return long
     */
    abstract long getServiceIdentifier();

    /**
     * Returns the value of the Rating-Group AVP, of type long.
     * @return long
     */
    abstract long getRatingGroup();

    /**
     * Returns the value of the Flow-Description AVP, of type IPFilterRule.
     * @return IPFilterRule
     */
    abstract IPFilterRule getFlowDescription();

    /**
     * Returns the value of the Flow-Description AVP, of type Enumerated.
     * @return ReportingLevel
     */
    abstract ReportingLevel getReportingLevel();

    /**
     * Returns the value of the Online AVP, of type Enumerated.
     * @return Online
     */
    abstract Online getOnline();

    /**
     * Returns the value of the Offline AVP, of type Enumerated.
     * @return offline
     */
    abstract Offline getOffline();

    /**
     * Returns the value of the MeteringMethod AVP, of type Enumerated.
     * @return MeteringMethod
     */
    abstract MeteringMethod getMeteringMethod();

    /**
     * Returns the value of the Precedence AVP, of type Unsigned32.
     * @return long
     */
    abstract long getPrecedence();

    /**
     * Returns the value of the AF-ChargingIdentifier AVP, of type OctetString.
     * @return String
     */
    abstract byte[] getAFChargingIdentifier();

    /**
     * Returns the value of the Flows AVP, of type Grouped.
     * @return Flows
     */
    abstract Flows getFlows();

    /**
     * Returns true if the Charging-Rule-Name AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasChargingRuleName();

    /**
     * Returns true if the Service-Identifier AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasServiceIdentifier();

    /**
     * Returns true if the Rating-Group AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasRatingGroup();

    /**
     * Returns true if the Flow-Description AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasFlowDescription();

    /**
     * Returns true if the Reporting-Level AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasReportingLevel();

    /**
     * Returns true if the Online AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasOnline();

     /**
     * Returns true if the offline AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasOffline();

    /**
     * Returns true if the Metering-Method AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasMeteringMethod();

    /**
     * Returns true if the Precedence AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasPrecedence();

    /**
     * Returns true if the AF-ChargingIdentifier AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasAFChargingIdentifier();

    /**
     * Returns true if the Flows AVP is present in the message.
     * @return boolean
     */
    abstract boolean hasFlows();

    /**
     * Sets the value of the charging-Rule-Name AVP, of type Octet String.
     * @param chargingRuleName
     */
    abstract void setChargingRuleName(byte[] chargingRuleName);

    /**
     * Sets the value of the Service-Identifier AVP, of type long.
     * @param serviceIdentifier
     */
    abstract void setServiceIdentifier(long serviceIdentifier);

    /**
     * Sets the value of the Rating-Group AVP, of type long.
     * @param ratingGroup
     */
    abstract void setRatingGroup(long ratingGroup);

    /**
     * Sets the value of the Flow-Description AVP, of type IPFilterRule.
     * @param flowDescription
     */
    abstract void setFlowDescription(IPFilterRule flowDescription);

    /**
     * Sets the value of the Reporting-Level AVP, of type Enumerated.
     * @param reportingLevel
     */
    abstract void setReportingLevel(ReportingLevel reportingLevel);

    /**
     * Sets the value of the Online AVP, of type Enumerated.
     * @param online
     */
    abstract void setOnline(Online online);

    /**
     * Sets the value of the Offline AVP, of type Enumerated.
     * @param offline
     */
    abstract void setOffline(Offline offline);

    /**
     * Sets the value of the Metering-Method AVP, of type Enumerated.
     * @param meteringMethod
     */
    abstract void setMeteringMethod(MeteringMethod meteringMethod);

    /**
     * Sets the value of the Precedence AVP, of type Unsigned32.
     * @param precedence
     */
    abstract void setPrecedence(long precedence);

    /**
     * Sets the value of the AF-Charging-Identifier AVP, of type Octet String.
     * @param afChargingIdentifier
     */
    abstract void setAFChargingIdentifier(byte[] afChargingIdentifier);

    /**
     * Sets the value of the Flows AVP, of type Grouped.
     * @param flows
     */
    abstract void setFlows(Flows flows);
}
