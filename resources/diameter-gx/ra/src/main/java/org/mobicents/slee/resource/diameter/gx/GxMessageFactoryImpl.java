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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.gx.GxMessageFactory;
import net.java.slee.resource.diameter.gx.events.GxCreditControlAnswer;
import net.java.slee.resource.diameter.gx.events.GxCreditControlMessage;
import net.java.slee.resource.diameter.gx.events.GxCreditControlRequest;

import net.java.slee.resource.diameter.gx.events.GxReAuthAnswer;
import net.java.slee.resource.diameter.gx.events.GxReAuthMessage;
import net.java.slee.resource.diameter.gx.events.GxReAuthRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.gx.events.GxCreditControlAnswerImpl;
import org.mobicents.slee.resource.diameter.gx.events.GxCreditControlRequestImpl;

import org.mobicents.slee.resource.diameter.gx.events.GxReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.gx.events.GxReAuthRequestImpl;

/**
 * Implementation of {@link GxMessageFactory}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public class GxMessageFactoryImpl implements GxMessageFactory {

    protected Logger logger = Logger.getLogger(this.getClass());

    protected final static Set<Integer> ids;

    static {
        final Set<Integer> _ids = new HashSet<Integer>();

        _ids.add(CreditControlAVPCodes.CC_Request_Type);
        _ids.add(CreditControlAVPCodes.CC_Request_Number);

        ids = Collections.unmodifiableSet(_ids);
    }
    protected DiameterMessageFactory baseFactory = null;
    protected String sessionId;
    protected Stack stack;

    private ApplicationId gxAppId = ApplicationId.createByAuthAppId(0L, _GX_AUTH_APP_ID);

    // protected RfAVPFactory rfAvpFactory = null;
    public GxMessageFactoryImpl(final DiameterMessageFactory baseFactory, final String sessionId, final Stack stack) {
        super();

        this.baseFactory = baseFactory;
        this.sessionId = sessionId;
        this.stack = stack;
    }

    public void setApplicationId(long vendorId, long applicationId) {
      this.gxAppId = ApplicationId.createByAuthAppId(vendorId, applicationId);      
    }
    
    public ApplicationId getApplicationId() {
      return this.gxAppId;      
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public GxCreditControlRequest createGxCreditControlRequest() {
        final GxCreditControlRequest gx = (GxCreditControlRequest) createGxCreditControlRequest(null, new DiameterAvp[]{});
        if (sessionId != null) {
            gx.setSessionId(sessionId);
        }

        return gx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GxReAuthRequest createGxReAuthRequest() {
        final GxReAuthRequest gxRAR = (GxReAuthRequest) createGxReAuthRequest(null, new DiameterAvp[]{});
        if (sessionId != null) {
            gxRAR.setSessionId(sessionId);
        }

        return gxRAR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GxCreditControlRequest createGxCreditControlRequest(String sessionId) {
        final GxCreditControlRequest gx = this.createGxCreditControlRequest();
        gx.setSessionId(sessionId);
        return gx;
    }

    @Override
    public GxReAuthRequest createGxReAuthRequest(String sessionId) {
        final GxReAuthRequest gxRAR = this.createGxReAuthRequest();
        gxRAR.setSessionId(sessionId);
        return gxRAR;
    }

    public GxCreditControlAnswer createGxCreditControlAnswer(final GxCreditControlRequest request) {
        // Create the answer from the request
        final GxCreditControlRequestImpl ccr = (GxCreditControlRequestImpl) request;

        //final GxCreditControlAnswerImpl msg = new GxCreditControlAnswerImpl(createMessage(ccr.getHeader(), new DiameterAvp[]{}));
        final Message raw = createGxMessage(ccr.getHeader(), new DiameterAvp[]{}, GxCreditControlRequest.commandCode);
        raw.setProxiable(ccr.getHeader().isProxiable());
        raw.setRequest(false);
        raw.setReTransmitted(false); // just in case. answers never have T flag set
        final GxCreditControlAnswerImpl msg = new GxCreditControlAnswerImpl(raw);
        

        msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_HOST);
        msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_REALM);
        msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_HOST);
        msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_REALM);
        msg.setSessionId(request.getSessionId());
        // Now copy the needed AVPs

        final DiameterAvp[] messageAvps = request.getAvps();
        if (messageAvps != null) {
            for (DiameterAvp a : messageAvps) {
                try {
                    if (ids.contains(a.getCode())) {
                        msg.addAvp(a);
                    }
                } catch (Exception e) {
                    logger.error("Failed to add AVP to answer. Code[" + a.getCode() + "]", e);
                }
            }
        }
        addOrigin(msg);
        return msg;
    }

    public GxReAuthAnswer createGxReAuthAnswer(final GxReAuthRequest request) {
        // Create the answer from the request
        final GxReAuthRequestImpl rar = (GxReAuthRequestImpl) request;

        //final GxReAuthAnswerImpl msg = new GxReAuthAnswerImpl(createMessage(rar.getHeader(), new DiameterAvp[]{}));
        
        final Message raw = this.createGxMessage(rar.getHeader(), new DiameterAvp[]{}, GxReAuthAnswer.commandCode);
        raw.setProxiable(rar.getHeader().isProxiable());
        raw.setRequest(false);
        raw.setReTransmitted(false); // just in case. answers never have T flag set
        final GxReAuthAnswerImpl msg = new GxReAuthAnswerImpl(raw);
        

        msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_HOST);
        msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_REALM);
        msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_HOST);
        msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_REALM);
        msg.setSessionId(request.getSessionId());
        // Now copy the needed AVPs

        final DiameterAvp[] messageAvps = request.getAvps();
        if (messageAvps != null) {
            for (DiameterAvp a : messageAvps) {
                try {
                    if (ids.contains(a.getCode())) {
                        msg.addAvp(a);
                    }
                } catch (Exception e) {
                    logger.error("Failed to add AVP to answer. Code[" + a.getCode() + "]", e);
                }
            }
        }
        addOrigin(msg);
        return msg;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public DiameterMessageFactory getBaseMessageFactory() {
        return this.baseFactory;
    }

    private GxCreditControlMessage createGxCreditControlRequest(final DiameterHeader diameterHeader, final DiameterAvp[] avps) throws IllegalArgumentException {

        boolean isRequest = false;
        if (diameterHeader == null) {
            isRequest = true;
        }

        GxCreditControlMessage msg = null;
        if (!isRequest) {
            final Message raw = createGxMessage(diameterHeader, avps, GxCreditControlRequest.commandCode);
            raw.setProxiable(true);
            raw.setRequest(false);
            raw.setReTransmitted(false); // just in case. answers never have T flag set
            msg = new GxCreditControlAnswerImpl(raw);
        } else {
            final Message raw = createGxMessage(null, avps, GxCreditControlRequest.commandCode);
            raw.setProxiable(true);
            raw.setRequest(true);
            msg = new GxCreditControlRequestImpl(raw);
        }

        return msg;
    }

    private GxReAuthMessage createGxReAuthRequest(final DiameterHeader diameterHeader, final DiameterAvp[] avps) throws IllegalArgumentException {

        boolean isRequest = false;
        if (diameterHeader == null) {
            isRequest = true;
        }

        GxReAuthMessage msg = null;
        if (!isRequest) {
            final Message raw = createGxMessage(diameterHeader, avps, GxReAuthAnswer.commandCode);
            raw.setProxiable(true);
            raw.setRequest(false);
            raw.setReTransmitted(false); // just in case. answers never have T flag set
            msg = new GxReAuthAnswerImpl(raw);
        } else {
            final Message raw = createGxMessage(null, avps, GxReAuthRequest.commandCode);
            raw.setProxiable(true);
            raw.setRequest(true);
            msg = new GxReAuthRequestImpl(raw);
        }

        return msg;
    }
    
    public Message createGxMessage(final DiameterHeader header, final DiameterAvp[] avps, int commandCode) throws AvpNotAllowedException {
        final Message msg = createRawMessage(header, commandCode);

        final AvpSet set = msg.getAvps();
        for (DiameterAvp avp : avps) {
            addAvp(avp, set);
        }

        return msg;
    }

    protected Message createRawMessage(final DiameterHeader header, int commandCode) {
        long endToEndId = 0;
        long hopByHopId = 0;

        if (header != null) {
            // Answer
            commandCode = header.getCommandCode();
            endToEndId = header.getEndToEndId();
            hopByHopId = header.getHopByHopId();
        }

        try {
            if (header != null) {
                return stack.getSessionFactory().getNewRawSession().createMessage(commandCode, this.gxAppId, hopByHopId, endToEndId);
            } else {
                return stack.getSessionFactory().getNewRawSession().createMessage(commandCode, this.gxAppId);
            }
        } catch (IllegalDiameterStateException e) {
            logger.error("Failed to get session factory for message creation.", e);
        } catch (InternalException e) {
            logger.error("Failed to create new raw session for message creation.", e);
        }

        return null;
    }

    protected void addAvp(final DiameterAvp avp, final AvpSet set) {
        // FIXME: alexandre: Should we look at the types and add them with proper function?
        if (avp instanceof GroupedAvp) {
            final AvpSet avpSet = set.addGroupedAvp(avp.getCode(), avp.getVendorId(), avp.getMandatoryRule() == 1, avp.getProtectedRule() == 1);

            final DiameterAvp[] groupedAVPs = ((GroupedAvp) avp).getExtensionAvps();
            for (DiameterAvp avpFromGroup : groupedAVPs) {
                addAvp(avpFromGroup, avpSet);
            }
        } else if (avp != null) {
            set.addAvp(avp.getCode(), avp.byteArrayValue(), avp.getVendorId(), avp.getMandatoryRule() == 1, avp.getProtectedRule() == 1);
        }
    }

    private void addOrigin(final DiameterMessage msg) {
        if (!msg.hasOriginHost()) {
            msg.setOriginHost(new DiameterIdentity(stack.getMetaData().getLocalPeer().getUri().getFQDN().toString()));
        }
        if (!msg.hasOriginRealm()) {
            msg.setOriginRealm(new DiameterIdentity(stack.getMetaData().getLocalPeer().getRealmName()));
        }
    }
}
