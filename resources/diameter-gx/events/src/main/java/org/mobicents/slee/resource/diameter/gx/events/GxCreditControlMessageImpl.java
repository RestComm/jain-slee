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
        addAvp(CreditControlAVPCodes.CC_Request_Type, (long) ccRequestType.getValue());
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
