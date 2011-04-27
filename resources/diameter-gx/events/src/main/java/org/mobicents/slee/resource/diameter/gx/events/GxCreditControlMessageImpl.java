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

import net.java.slee.resource.diameter.cca.events.avp.CcRequestType;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.gx.events.GxCreditControlMessage;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public abstract class GxCreditControlMessageImpl extends DiameterMessageImpl implements GxCreditControlMessage {

    /**
     * @param message
     */
    public GxCreditControlMessageImpl(Message message) {
        super(message);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlMessage#getCcRequestNumber()
     */
    @Override
    public long getCcRequestNumber() {
        return getAvpAsUnsigned32(CreditControlAVPCodes.CC_Request_Number);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlMessage#setCcRequestNumber(long)
     */
    @Override
    public void setCcRequestNumber(long ccRequestNumber) throws IllegalStateException {
        addAvp(CreditControlAVPCodes.CC_Request_Number, ccRequestNumber);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlMessage#hasCcRequestNumber()
     */
    @Override
    public boolean hasCcRequestNumber() {
        return hasAvp(CreditControlAVPCodes.CC_Request_Number);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlMessage#getCcRequestType()
     */
    @Override
    public CcRequestType getCcRequestType() {
        return (CcRequestType) getAvpAsEnumerated(CreditControlAVPCodes.CC_Request_Type, CcRequestType.class);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlMessage#setCcRequestType(net.java.slee.resource.diameter.cca.events.avp.CcRequestType)
     */
    @Override
    public void setCcRequestType(CcRequestType ccRequestType) throws IllegalStateException {
        addAvp(CreditControlAVPCodes.CC_Request_Type, ccRequestType.getValue());
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlMessage#hasCcRequestType()
     */
    @Override
    public boolean hasCcRequestType() {
        return hasAvp(CreditControlAVPCodes.CC_Request_Type);
    }


}
