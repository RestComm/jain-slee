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

package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.AMBRAvp;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link AMBRAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class AMBRAvpImpl extends GroupedAvpImpl implements AMBRAvp {

    public AMBRAvpImpl() {
        super();
    }

    public AMBRAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    public boolean hasMaxRequestedBandwidthUL() {
        return hasAvp(DiameterS6aAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public long getMaxRequestedBandwidthUL() {
         return getAvpAsUnsigned32(DiameterS6aAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public void setMaxRequestedBandwidthUL(long maxRequestedBandwidthUL) {
        addAvp(DiameterS6aAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterS6aAvpCodes.S6A_VENDOR_ID, maxRequestedBandwidthUL);
    }

    public boolean hasMaxRequestedBandwidthDL() {
        return hasAvp(DiameterS6aAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public long getMaxRequestedBandwidthDL() {
         return getAvpAsUnsigned32(DiameterS6aAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterS6aAvpCodes.S6A_VENDOR_ID);
    }

    public void setMaxRequestedBandwidthDL(long maxRequestedBandwidthDL) {
        addAvp(DiameterS6aAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterS6aAvpCodes.S6A_VENDOR_ID, maxRequestedBandwidthDL);
    }
}
