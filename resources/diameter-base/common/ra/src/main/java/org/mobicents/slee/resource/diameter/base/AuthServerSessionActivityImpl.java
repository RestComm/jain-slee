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
package org.mobicents.slee.resource.diameter.base;

import java.io.IOException;

import net.java.slee.resource.diameter.base.AuthServerSessionActivity;
import net.java.slee.resource.diameter.base.AuthSessionState;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.auth.ServerAuthSession;
import org.jdiameter.common.api.app.auth.ServerAuthSessionState;
import org.jdiameter.common.impl.app.auth.AbortSessionRequestImpl;
import org.jdiameter.common.impl.app.auth.ReAuthRequestImpl;
import org.jdiameter.common.impl.app.auth.SessionTermAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * Implementation of {@link AuthServerSessionActivity}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AuthServerSessionActivityImpl extends AuthSessionActivityImpl implements AuthServerSessionActivity {

  private static final long serialVersionUID = -3695874024822124799L;

  protected transient ServerAuthSession serverSession = null;

  public AuthServerSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ServerAuthSession serverSession, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) serverSession, destinationHost, destinationRealm);

    super.setCurrentWorkingSession(this.serverSession.getSessions().get(0));
    setSession(serverSession);
  }

  public AbortSessionRequest createAbortSessionRequest() {
    AbortSessionRequest asr = messageFactory.createAbortSessionRequest();

    // Set Auth-Application-Id to 0 as specified
    asr.setAuthApplicationId(0L);

    return asr;
  }

  public void sendAbortSessionRequest(AbortSessionRequest request) throws IOException {
    try {
      //super.sendMessage(request);
      DiameterMessageImpl msg = (DiameterMessageImpl) request;
      this.serverSession.sendAbortSessionRequest(new AbortSessionRequestImpl(msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public ReAuthRequest createReAuthRequest(ReAuthRequestType reAuthRequestType) {
    ReAuthRequest rar = messageFactory.createReAuthRequest();

    // Set Auth-Application-Id to 0 as specified
    rar.setAuthApplicationId(0L);
    rar.setReAuthRequestType(reAuthRequestType);

    return rar;
  }

  public void sendReAuthRequest(ReAuthRequest request) throws IOException {
    try {
      //super.sendMessage(request);
      DiameterMessageImpl msg = (DiameterMessageImpl) request;
      this.serverSession.sendReAuthRequest(new ReAuthRequestImpl(msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public SessionTerminationAnswer createSessionTerminationAnswer() {
    // Create the request. // TODO Use request used to create this activity ?
    SessionTerminationRequest str = messageFactory.createSessionTerminationRequest();
    str.setAuthApplicationId(0L);

    return createSessionTerminationAnswer(str);
  }

  public SessionTerminationAnswer createSessionTerminationAnswer(SessionTerminationRequest sessionTerminationRequest) {
    return messageFactory.createSessionTerminationAnswer(sessionTerminationRequest);
  }

  public void sendSessionTerminationAnswer(SessionTerminationAnswer request) throws IOException {
    try {

      //super.sendMessage(request);
      DiameterMessageImpl msg = (DiameterMessageImpl) request;
      this.serverSession.sendSessionTerminationAnswer(new SessionTermAnswerImpl(msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public ServerAuthSession getSession() {
    return serverSession;
  }

  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    stateChanged(oldState, newState);
  }

  public void stateChanged(Enum oldState, Enum newState) {
    ServerAuthSessionState state = (ServerAuthSessionState) newState;

    switch(state)
    {
    case IDLE:
      //this.state = AuthSessionState.Idle;
      break;
    case OPEN:
      //this.state = AuthSessionState.Open;
      break;
    case DISCONNECTED:
      //super.state = AuthSessionState.Disconnected;
      //String sessionId = this.serverSession.getSessions().get(0).getSessionId();
      //this.serverSession.release();
      //this.baseListener.sessionDestroyed(sessionId, this.serverSession);
      endActivity();
      break;
    }
  }

  @Override
  public AuthSessionState getSessionState() {
    ServerAuthSessionState state = (ServerAuthSessionState) this.serverSession.getState(ServerAuthSessionState.class);

    switch (state) {
    case IDLE:
      return AuthSessionState.Idle;
    case OPEN:
      return AuthSessionState.Open;
    case DISCONNECTED:
      return AuthSessionState.Disconnected;
    default:
      logger.error("Unexpected state in Auth Server FSM: " + state);
      return null;
    }
  }

  public void setSession(ServerAuthSession appSession) {
    this.serverSession = appSession;
    this.serverSession.addStateChangeNotification(this);
    super.eventListener = (EventListener<Request, Answer>) appSession;
  }

  @Override
  public void endActivity() {
    this.serverSession.release();
    super.baseListener.endActivity(getActivityHandle());
  }
}
