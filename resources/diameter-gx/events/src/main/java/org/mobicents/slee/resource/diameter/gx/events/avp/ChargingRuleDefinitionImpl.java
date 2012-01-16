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

import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition;
import net.java.slee.resource.diameter.gx.events.avp.Flows;
import net.java.slee.resource.diameter.gx.events.avp.MeteringMethod;
import net.java.slee.resource.diameter.gx.events.avp.Offline;
import net.java.slee.resource.diameter.gx.events.avp.Online;
import net.java.slee.resource.diameter.gx.events.avp.ReportingLevel;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class ChargingRuleDefinitionImpl extends GroupedAvpImpl implements ChargingRuleDefinition{

    public ChargingRuleDefinitionImpl() {
         super();
    }

   
    public ChargingRuleDefinitionImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }


    /**
     * (non-Javadoc)
     * 
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getChargingRuleName()
     */
    public byte[] getChargingRuleName() {
        return getAvpAsOctetString(DiameterGxAvpCodes.CHARGING_RULE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getServiceIdentifier()
     */
    public long getServiceIdentifier() {
        return getAvpAsUnsigned32(CreditControlAVPCodes.Service_Identifier);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getRatingGroup()
     */
    public long getRatingGroup() {
        return getAvpAsUnsigned32(CreditControlAVPCodes.Rating_Group);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getFlowDescription()
     */
    //TODO check for array of IPRUle
    public IPFilterRule getFlowDescription() {
        return (IPFilterRule) getAvpAsEnumerated(DiameterGxAvpCodes.FLOW_DESCRIPTION, DiameterGxAvpCodes.TGPP_VENDOR_ID, IPFilterRule.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getReportingLevel()
     */
    public ReportingLevel getReportingLevel() {
        return (ReportingLevel)getAvpAsEnumerated(DiameterGxAvpCodes.REPORTING_LEVEL, DiameterGxAvpCodes.TGPP_VENDOR_ID, ReportingLevel.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getOnline()
     */
    public Online getOnline() {
        return (Online)getAvpAsEnumerated(DiameterGxAvpCodes.ONLINE, DiameterGxAvpCodes.TGPP_VENDOR_ID, Online.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getOffline()
     */
    public Offline getOffline() {
        return (Offline)getAvpAsEnumerated(DiameterGxAvpCodes.OFFLINE, DiameterGxAvpCodes.TGPP_VENDOR_ID, Offline.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getMeteringMethod()
     */
    public MeteringMethod getMeteringMethod() {
        return (MeteringMethod)getAvpAsEnumerated(DiameterGxAvpCodes.METERING_METHOD, DiameterGxAvpCodes.TGPP_VENDOR_ID, MeteringMethod.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getPrecedence()
     */
    public long getPrecedence() {
        return getAvpAsUnsigned32(DiameterGxAvpCodes.PRECEDENCE, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getAFChargingIdentifier()
     */
    public byte[] getAFChargingIdentifier() {
        return getAvpAsOctetString(DiameterGxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#getFlows()
     */
    public Flows getFlows() {
        return (Flows) getAvpAsCustom(DiameterGxAvpCodes.FLOWS, DiameterGxAvpCodes.TGPP_VENDOR_ID, Flows.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasChargingRuleName()
     */
    public boolean hasChargingRuleName() {
        return hasAvp( DiameterGxAvpCodes.CHARGING_RULE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasServiceIdentifier()
     */
    public boolean hasServiceIdentifier() {
        return hasAvp( CreditControlAVPCodes.Service_Identifier);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasRatingGroup()
     */
    public boolean hasRatingGroup() {
        return hasAvp( CreditControlAVPCodes.Rating_Group );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasFlowDescription()
     */
    public boolean hasFlowDescription() {
        return hasAvp(DiameterGxAvpCodes.FLOW_DESCRIPTION, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasReportingLevel()
     */
    public boolean hasReportingLevel() {
        return hasAvp(DiameterGxAvpCodes.REPORTING_LEVEL, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasOnline()
     */
    public boolean hasOnline() {
        return hasAvp(DiameterGxAvpCodes.ONLINE, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasOffline()
     */
    public boolean hasOffline() {
        return hasAvp(DiameterGxAvpCodes.OFFLINE, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasMeteringMethod()
     */
    public boolean hasMeteringMethod() {
        return hasAvp(DiameterGxAvpCodes.METERING_METHOD, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasPrecedence()
     */
    public boolean hasPrecedence() {
        return hasAvp(DiameterGxAvpCodes.PRECEDENCE, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasAFChargingIdentifier()
     */
    public boolean hasAFChargingIdentifier() {
        return hasAvp(DiameterGxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#hasFlows()
     */
    public boolean hasFlows() {
        return hasAvp(DiameterGxAvpCodes.FLOWS, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setChargingRuleName(String)
     */
    public void setChargingRuleName(byte[] chargingRuleName) {
        addAvp(DiameterGxAvpCodes.CHARGING_RULE_NAME, DiameterGxAvpCodes.TGPP_VENDOR_ID, chargingRuleName);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setServiceIdentifier(long)
     */
    public void setServiceIdentifier(long serviceIdentifier) {
        addAvp(CreditControlAVPCodes.Service_Identifier, serviceIdentifier);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setRatingGroup(long)
     */
    public void setRatingGroup(long ratingGroup) {
        addAvp(CreditControlAVPCodes.Rating_Group, ratingGroup);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setFlowDescription(net.java.slee.resource.diameter.base.events.avp.IPFilterRule)
     */
    public void setFlowDescription(IPFilterRule flowDescription) {
        addAvp(DiameterGxAvpCodes.FLOW_DESCRIPTION, DiameterGxAvpCodes.TGPP_VENDOR_ID, flowDescription.toString());
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setReportingLevel(net.java.slee.resource.diameter.gx.events.avp.ReportingLevel)
     */
    public void setReportingLevel(ReportingLevel reportingLevel) {
         addAvp(DiameterGxAvpCodes.REPORTING_LEVEL, DiameterGxAvpCodes.TGPP_VENDOR_ID, reportingLevel.getValue());
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setOnline(net.java.slee.resource.diameter.gx.events.avp.Online)
     */
    public void setOnline(Online online) {
        addAvp(DiameterGxAvpCodes.ONLINE, DiameterGxAvpCodes.TGPP_VENDOR_ID, online.getValue());
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setOffline(net.java.slee.resource.diameter.gx.events.avp.Offline)
     */
    public void setOffline(Offline offline) {
        addAvp(DiameterGxAvpCodes.OFFLINE, DiameterGxAvpCodes.TGPP_VENDOR_ID, offline.getValue());
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setMeteringMethod(net.java.slee.resource.diameter.gx.events.avp.MeteringMethod)
     */
    public void setMeteringMethod(MeteringMethod meteringMethod) {
        addAvp(DiameterGxAvpCodes.METERING_METHOD, DiameterGxAvpCodes.TGPP_VENDOR_ID, meteringMethod.getValue());
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setPrecedence(long)
     */
    public void setPrecedence(long precedence) {
        addAvp(DiameterGxAvpCodes.PRECEDENCE, DiameterGxAvpCodes.TGPP_VENDOR_ID, precedence);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setAFChargingIdentifier(String)
     */
    public void setAFChargingIdentifier(byte[] afChargingIdentifier) {
        addAvp(DiameterGxAvpCodes.AF_CHARGING_IDENTIFIER, DiameterGxAvpCodes.TGPP_VENDOR_ID, afChargingIdentifier);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.ChargingRuleDefinition#setFlows(net.java.slee.resource.diameter.gx.events.avp.Flows)
     */
    public void setFlows(Flows flows) {
        addAvp(DiameterGxAvpCodes.FLOWS, DiameterGxAvpCodes.TGPP_VENDOR_ID, flows.byteArrayValue());
    }


}
