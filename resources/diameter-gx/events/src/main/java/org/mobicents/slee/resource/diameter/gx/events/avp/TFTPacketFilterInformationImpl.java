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
import net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class TFTPacketFilterInformationImpl extends GroupedAvpImpl implements TFTPacketFilterInformation {

     public TFTPacketFilterInformationImpl() {
         super();
    }

    public TFTPacketFilterInformationImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#getPrecedence()
     */
    public long getPrecedence() {
        return getAvpAsUnsigned32(DiameterGxAvpCodes.PRECEDENCE, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#getTFTFilter()
     */
    public IPFilterRule getTFTFilter() {
        return (IPFilterRule) getAvpAsEnumerated(DiameterGxAvpCodes.TFT_FILTER, DiameterGxAvpCodes.TGPP_VENDOR_ID, IPFilterRule.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#getTosTrafficClass()
     */
    public byte[] getTosTrafficClass() {
        return getAvpAsOctetString(DiameterGxAvpCodes.ToS_TRAFFIC_CLASS, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#hasPrecedence()
     */
    public boolean hasPrecedence() {
        return hasAvp(DiameterGxAvpCodes.PRECEDENCE, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#hasTFTFilter()
     */
    public boolean hasTFTFilter() {
        return hasAvp(DiameterGxAvpCodes.TFT_FILTER, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#hasTosTrafficClass()
     */
    public boolean hasTosTrafficClass() {
        return hasAvp(DiameterGxAvpCodes.ToS_TRAFFIC_CLASS, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#setPrecedence(long)
     */
    public void setPrecedence(long precedence) {
        addAvp(DiameterGxAvpCodes.PRECEDENCE, DiameterGxAvpCodes.TGPP_VENDOR_ID, precedence);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#setTFTFilter(net.java.slee.resource.diameter.base.events.avp.IPFilterRule)
     */
    public void setTFTFilter(IPFilterRule tftFilter) {
        addAvp(DiameterGxAvpCodes.TFT_FILTER, DiameterGxAvpCodes.TGPP_VENDOR_ID, tftFilter.toString());
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation#setTosTrafficClass(String)
     */
    public void setTosTrafficClass(byte[] tosTrafficClass) {
        addAvp(DiameterGxAvpCodes.ToS_TRAFFIC_CLASS, DiameterGxAvpCodes.TGPP_VENDOR_ID, tosTrafficClass);
    }

}
