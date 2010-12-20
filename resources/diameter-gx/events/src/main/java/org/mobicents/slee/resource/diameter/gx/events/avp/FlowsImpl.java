/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.diameter.gx.events.avp;

import net.java.slee.resource.diameter.gx.events.avp.Flows;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class FlowsImpl extends GroupedAvpImpl implements Flows{

     public FlowsImpl() {
         super();
    }

    public FlowsImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
        super(code, vendorId, mnd, prt, value);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.Flows#getMediaComponentNumber()
     */
    public long getMediaComponentNumber() {
        return getAvpAsUnsigned32(DiameterGxAvpCodes.MEDIA_COMPONENT_NUMBER, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.Flows#getFlowNumber()
     */
    public long getFlowNumber() {
        return getAvpAsUnsigned32(DiameterGxAvpCodes.FLOW_NUMBER, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.Flows#hasMediaComponentNumber()
     */
    public boolean hasMediaComponentNumber() {
        return hasAvp( DiameterGxAvpCodes.MEDIA_COMPONENT_NUMBER, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.Flows#hasFlowNumber()
     */
    public boolean hasFlowNumber() {
        return hasAvp( DiameterGxAvpCodes.FLOW_NUMBER, DiameterGxAvpCodes.TGPP_VENDOR_ID );
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.Flows#setMediaComponentNumber(lomg)
     */
    public void setMediaComponentNumber(long mediaComponentNumber) {
        addAvp( DiameterGxAvpCodes.MEDIA_COMPONENT_NUMBER, DiameterGxAvpCodes.TGPP_VENDOR_ID, mediaComponentNumber);
    }

    /**
     * (non-Javadoc)
     *
     * @see net.java.slee.resource.diameter.gx.events.avp.Flows#setFlowNumber(long)
     */
    public void setFlowNumber(long flowNumber) {
        addAvp( DiameterGxAvpCodes.FLOW_NUMBER, DiameterGxAvpCodes.TGPP_VENDOR_ID, flowNumber);
    }

}
