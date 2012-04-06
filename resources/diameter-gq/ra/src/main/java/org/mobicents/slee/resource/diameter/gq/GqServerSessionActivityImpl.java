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
import net.java.slee.resource.diameter.gq.GqServerSessionActivity;
import net.java.slee.resource.diameter.gq.GqSessionState;
import net.java.slee.resource.diameter.gq.events.GqAAAnswer;
import net.java.slee.resource.diameter.gq.events.GqAARequest;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest;
import net.java.slee.resource.diameter.gq.events.GqReAuthRequest;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationAnswer;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.auth.ServerAuthSession;
import org.jdiameter.common.impl.app.AppAnswerEventImpl;
import org.jdiameter.common.api.app.auth.ServerAuthSessionState;
import org.jdiameter.common.impl.app.auth.ReAuthRequestImpl;
import org.jdiameter.common.impl.app.auth.SessionTermAnswerImpl;
import org.jdiameter.common.impl.app.auth.AbortSessionRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;


/**
 * Implementation of {@link GqServerSessionActivity}.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa</a>
 */
public class GqServerSessionActivityImpl extends GqSessionActivityImpl implements GqServerSessionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 1L;

  private static Logger logger = Logger.getLogger(GqServerSessionActivityImpl.class);

  private GqSessionState state = GqSessionState.IDLE;
  protected transient ServerAuthSession session = null;

  public GqServerSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ServerAuthSession session,
      DiameterIdentity destinationHost, DiameterIdentity destinationRealm, Stack stack) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));

    super.setGqMessageFactory(new GqMessageFactoryImpl(messageFactory, session.getSessionId(), stack));
  }

  public ServerAuthSession getSession() {
    return this.session;
  }

  public void setSession(ServerAuthSession session2) {
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
      logger.info("Gq Server FSM State Changed: " + oldState + " => " + newState);

    ServerAuthSessionState s = (ServerAuthSessionState) newState;

    switch (s) {
      case OPEN:
        this.state = GqSessionState.OPEN;
        break;
      case IDLE:
        this.state = GqSessionState.IDLE;
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
        break;
      case DISCONNECTED:
        this.state = GqSessionState.DISCONNECTED;
        //TODO: check if this also terminates?
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
        break;
      default:
        logger.error("Unexpected state in Gq Server FSM: " + s);
    }
  }

  @Override
  public GqAAAnswer createGqAAAnswer(GqAARequest aar) {
    return getGqMessageFactory().createGqAAAnswer(aar);
  }

  @Override
  public void sendGqAAAnswer(GqAAAnswer aaa) throws IOException {
    if (this.state == GqSessionState.DISCONNECTED)
      throw new DiameterException("Failed to validate, wrong state: " + this.state);

    DiameterMessageImpl msg = (DiameterMessageImpl) aaa;

    try {
      session.sendAuthAnswer(new AppAnswerEventImpl((Answer) msg.getGenericData()));
    }
    catch (AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  @Override
  public void sendReAuthRequest(GqReAuthRequest rar) throws IOException {
    if (this.state != GqSessionState.OPEN)
      throw new DiameterException("Failed to validate, wrong state: " + this.state);

    DiameterMessageImpl msg = (DiameterMessageImpl) rar;

    try {
      session.sendReAuthRequest(new ReAuthRequestImpl((Request) msg.getGenericData()));
    }
    catch (AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  @Override
  public GqSessionTerminationAnswer createGqSessionTerminationAnswer(GqSessionTerminationRequest str) {
    return getGqMessageFactory().createGqSessionTerminationAnswer(str);
  }

  @Override
  public void sendGqSessionTerminationAnswer(GqSessionTerminationAnswer sta) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) sta;

    try {
      session.sendSessionTerminationAnswer(new SessionTermAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  @Override
  public void sendAbortSessionRequest(GqAbortSessionRequest asr) throws IOException {
    if (this.state == GqSessionState.IDLE)
      throw new DiameterException("Failed to validate, wrong state: " + this.state);

    DiameterMessageImpl msg = (DiameterMessageImpl) asr;

    try {
      session.sendAbortSessionRequest(new AbortSessionRequestImpl((Request) msg.getGenericData()));
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
