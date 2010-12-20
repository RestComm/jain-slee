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
package org.mobicents.slee.resource.diameter.gx;

import java.io.IOException;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.gx.GxServerSessionActivity;
import net.java.slee.resource.diameter.gx.GxSessionState;
import net.java.slee.resource.diameter.gx.events.GxCreditControlAnswer;
import net.java.slee.resource.diameter.gx.events.GxCreditControlRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.gx.ServerGxSession;
import org.jdiameter.common.api.app.gx.ServerGxSessionState;
import org.jdiameter.common.impl.app.auth.ReAuthRequestImpl;
import org.jdiameter.common.impl.app.gx.GxCreditControlAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Implementation of {@link GxServerSessionActivity}.
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public class GxServerSessionActivityImpl extends GxSessionActivityImpl implements GxServerSessionActivity, StateChangeListener<AppSession> {

    private static final long serialVersionUID = 5230054776594429948L;
    private static Logger logger = Logger.getLogger(GxServerSessionActivityImpl.class);
    protected transient ServerGxSession session = null;
    protected transient GxCreditControlRequest lastRequest = null;

    public GxServerSessionActivityImpl(final DiameterMessageFactory messageFactory, final DiameterAvpFactory avpFactory, final ServerGxSession session,
                                       final DiameterIdentity destinationHost, final DiameterIdentity destinationRealm, final Stack stack) {
        super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

        setSession(session);
        super.setCurrentWorkingSession(this.session.getSessions().get(0));
        super.setGxMessageFactory(new GxMessageFactoryImpl(messageFactory, session.getSessionId(), stack));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GxCreditControlAnswer createGxCreditControlAnswer() {
        if (lastRequest == null) {
            if (logger.isInfoEnabled()) {
                logger.info("No request received, cant create answer.");
            }
            return null;
        }

        final GxCreditControlAnswer answer = ((GxMessageFactoryImpl) getGxMessageFactory()).createGxCreditControlAnswer(lastRequest);

        return answer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendGxCreditControlAnswer(final GxCreditControlAnswer cca) throws IOException {
        fetchCurrentState(cca);

        final DiameterMessageImpl msg = (DiameterMessageImpl) cca;

        try {
            session.sendCreditControlAnswer(new GxCreditControlAnswerImpl((Answer) msg.getGenericData()));
        } catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
            final AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
            throw anae;
        } catch (Exception e) {
            throw new IOException("Failed to send message.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendReAuthRequest(final ReAuthRequest rar) throws IOException {
        // RFC 4006 5.5
        rar.setReAuthRequestType(ReAuthRequestType.AUTHORIZE_ONLY);
        rar.setAuthApplicationId(CreditControlMessageFactory._CCA_AUTH_APP_ID);

        final DiameterMessageImpl msg = (DiameterMessageImpl) rar;

        try {
            session.sendReAuthRequest(new ReAuthRequestImpl((Request) msg.getGenericData()));
        } catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
            final AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
            throw anae;
        } catch (Exception e) {
            throw new IOException("Failed to send message.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateChanged(final AppSession source, final Enum oldState, final Enum newState) {
        this.stateChanged(oldState, newState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateChanged(final Enum oldState, final Enum newState) {
        if (logger.isInfoEnabled()) {
            logger.info("Credit-Control Server FSM State Changed: " + oldState + " => " + newState);
        }

        final ServerGxSessionState s = (ServerGxSessionState) newState;

        // IDLE(0), OPEN(1);
        switch (s) {
            case OPEN:
                // FIXME: this should not happen?
                break;

            case IDLE:
                endActivity();
                break;

            default:
                logger.error("Unexpected state in Credit-Control Server FSM: " + s);
        }
    }

    public void fetchCurrentState(final GxCreditControlRequest ccr) {
        this.lastRequest = ccr;
        // TODO: Complete this method.
    }

    public void fetchCurrentState(final GxCreditControlAnswer cca) {
        // TODO: Complete this method.
    }

    public ServerGxSession getSession() {
        return this.session;
    }

    public void setSession(final ServerGxSession session2) {
        this.session = session2;
        this.session.addStateChangeNotification(this);
    }

    public GxSessionState getState() {
        final ServerGxSessionState sessionState = session.getState(ServerGxSessionState.class);
        switch (sessionState) {
            case OPEN:
                return GxSessionState.OPEN;
            case IDLE:
                return GxSessionState.IDLE;
            default:
                logger.error("Unexpected state in Credit-Control Server FSM: " + sessionState);
                return null;
        }
    }

    public String toString() {
        return super.toString() + " -- Event[ " + (lastRequest != null) + " ] Session[ " + session + " ] State[ " + getState() + " ]";
    }

    @Override
    public void endActivity() {
        this.session.release();
        super.baseListener.endActivity(getActivityHandle());
    }
    }
