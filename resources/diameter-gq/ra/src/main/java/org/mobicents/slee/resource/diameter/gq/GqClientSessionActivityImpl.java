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

package org.mobicents.slee.resource.diameter.gq;

import java.io.IOException;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterException;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.gq.GqClientSessionActivity;
import net.java.slee.resource.diameter.gq.GqSessionState;
import net.java.slee.resource.diameter.gq.events.GqAARequest;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest;
import net.java.slee.resource.diameter.gq.events.GqReAuthAnswer;
import net.java.slee.resource.diameter.gq.events.GqReAuthRequest;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.common.api.app.auth.ClientAuthSessionState;
import org.jdiameter.common.impl.app.AppRequestEventImpl;
import org.jdiameter.common.impl.app.auth.AbortSessionAnswerImpl;
import org.jdiameter.common.impl.app.auth.ReAuthAnswerImpl;
import org.jdiameter.common.impl.app.auth.SessionTermRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;


/**
 * Implementation of {@link GqClientSessionActivity}.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa</a>
 */
public class GqClientSessionActivityImpl extends GqSessionActivityImpl implements GqClientSessionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 1L;

  protected transient ClientAuthSession session;
  private GqSessionState state = GqSessionState.IDLE;

  /**
   * 
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param destinationHost
   * @param destinationRealm
   * @param endpoint
   * @param stack
   */
  public GqClientSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ClientAuthSession session,
      DiameterIdentity destinationHost, DiameterIdentity destinationRealm, Stack stack) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);
    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
    super.setGqMessageFactory(new GqMessageFactoryImpl(messageFactory, session.getSessionId(), stack));
  }

  public void setSession(ClientAuthSession session2) {
    this.session = session2;
    this.session.addStateChangeNotification(this);
  }

  @Override
  public GqSessionState getState() {
    return state;
  }

  @Override
  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    this.stateChanged(oldState, newState);
  }

  public void stateChanged(Enum oldState, Enum newState) {
    if (logger.isInfoEnabled())
      logger.info("Gq Client FSM State Changed: " + oldState + " => " + newState);

    ClientAuthSessionState s = (ClientAuthSessionState) newState;

    switch (s) {
      case OPEN:
        this.state = GqSessionState.OPEN;
        break;
      case PENDING:
        this.state = GqSessionState.PENDING;
        break;
      case IDLE:
        this.state = GqSessionState.IDLE;
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
        break;
      case DISCONNECTED:
        this.state = GqSessionState.DISCONNECTED;
        //TODO: check if this also should term activity.
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
        break;
      default:
        logger.error("Unexpected state in Gq Client FSM: " + s);
    }
  }

  @Override
  public void sendGqAARequest(GqAARequest aar) throws IOException {
    if (this.state != GqSessionState.IDLE && this.state != GqSessionState.OPEN)
      throw new DiameterException("Failed to validate, wrong state: " + this.state);

    DiameterMessageImpl msg = (DiameterMessageImpl) aar;

    try {
      session.sendAuthRequest(new AppRequestEventImpl((Request) msg.getGenericData()));
    }
    catch (AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  @Override
  public GqReAuthAnswer createGqReAuthAnswer(GqReAuthRequest rar) {
    return getGqMessageFactory().createGqReAuthAnswer(rar);
  }

  @Override
  public void sendGqReAuthAnswer(GqReAuthAnswer raa) throws IOException {
    if (this.state != GqSessionState.OPEN)
      throw new DiameterException("Failed to validate, wrong state: " + this.state);

    DiameterMessageImpl msg = (DiameterMessageImpl) raa;

    try {
      session.sendReAuthAnswer(new ReAuthAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  @Override
  public GqAbortSessionAnswer createGqAbortSessionAnswer(GqAbortSessionRequest asr) {
    return getGqMessageFactory().createGqAbortSessionAnswer(asr);
  }

  @Override
  public void sendGqAbortSessionAnswer(GqAbortSessionAnswer asa) throws IOException {
    if (this.state == GqSessionState.PENDING)
      throw new DiameterException("Failed to validate, wrong state: " + this.state);

    DiameterMessageImpl msg = (DiameterMessageImpl) asa;

    try {
      session.sendAbortSessionAnswer(new AbortSessionAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  @Override
  public void sendGqSessionTerminationRequest(GqSessionTerminationRequest str) throws IOException {
    if (this.state != GqSessionState.OPEN && this.state != GqSessionState.PENDING)
      throw new DiameterException("Failed to validate, wrong state: " + this.state);

    DiameterMessageImpl msg = (DiameterMessageImpl) str;

    try {
      session.sendSessionTerminationRequest(new SessionTermRequestImpl((Request) msg.getGenericData()));
    }
    catch (AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  @Override
  public void endActivity() {
    this.session.release();
    super.endActivity();
  }
}
