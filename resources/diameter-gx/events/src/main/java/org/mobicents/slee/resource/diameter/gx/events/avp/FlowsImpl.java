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
