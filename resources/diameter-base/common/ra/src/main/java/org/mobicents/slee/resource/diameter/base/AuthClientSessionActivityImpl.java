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

import net.java.slee.resource.diameter.base.AuthClientSessionActivity;
import net.java.slee.resource.diameter.base.AuthSessionState;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.common.api.app.auth.ClientAuthSessionState;
import org.jdiameter.common.impl.app.auth.AbortSessionAnswerImpl;
import org.jdiameter.common.impl.app.auth.ReAuthAnswerImpl;
import org.jdiameter.common.impl.app.auth.SessionTermRequestImpl;
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * Implementation of {@link AuthClientSessionActivity}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AuthClientSessionActivityImpl extends AuthSessionActivityImpl implements AuthClientSessionActivity {

  private static final long serialVersionUID = -7354479964717373558L;

  protected transient ClientAuthSession clientSession = null;

  public AuthClientSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ClientAuthSession clientSession, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) clientSession, destinationHost, destinationRealm);

    setSession(clientSession);
    super.setCurrentWorkingSession(clientSession.getSessions().get(0));
  }

  public AbortSessionAnswer createAbortSessionAnswer() {
    // Create the request. // TODO Use request used to create this activity ?
    AbortSessionRequest asr = messageFactory.createAbortSessionRequest();
    asr.setAuthApplicationId(0L);

    return createAbortSessionAnswer(asr);
  }

  public AbortSessionAnswer createAbortSessionAnswer(AbortSessionRequest abortSessionRequest) {
    return messageFactory.createAbortSessionAnswer(abortSessionRequest);
  }

  public void sendAbortSessionAnswer(AbortSessionAnswer answer) throws IOException {
    try {
      // super.sendMessage(answer);
      DiameterMessageImpl asa = (DiameterMessageImpl) answer;
      this.clientSession.sendAbortSessionAnswer(new AbortSessionAnswerImpl((Answer) asa.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public ReAuthAnswer createReAuthAnswer() {
    // Create the request. // TODO Use request used to create this activity ?
    ReAuthRequest rar = messageFactory.createReAuthRequest();
    rar.setAuthApplicationId(0L);

    return createReAuthAnswer(rar);
  }

  public ReAuthAnswer createReAuthAnswer(ReAuthRequest reAuthRequest) {
    return messageFactory.createReAuthAnswer(reAuthRequest);
  }

  public void sendReAuthAnswer(ReAuthAnswer answer) throws IOException {
    try {
      //super.sendMessage(answer);
      DiameterMessageImpl msg = (DiameterMessageImpl) answer;
      this.clientSession.sendReAuthAnswer(new ReAuthAnswerImpl(msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public SessionTerminationRequest createSessionTerminationRequest(TerminationCauseType terminationCause) {
    SessionTerminationRequest str = messageFactory.createSessionTerminationRequest();

    // Set Auth-Application-Id to 0 as specified
    str.setAuthApplicationId(0L);
    str.setTerminationCause(terminationCause);

    return str;
  }

  public void sendSessionTerminationRequest(SessionTerminationRequest request) throws IOException {
    try {
      //super.sendMessage(request);
      DiameterMessageImpl msg = (DiameterMessageImpl) request;
      this.clientSession.sendSessionTerminationRequest(new SessionTermRequestImpl(msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    stateChanged(oldState, newState);
  }

  public void stateChanged(Enum oldState, Enum newState) {

    ClientAuthSessionState state=(ClientAuthSessionState) newState;
    switch(state)
    {
    case IDLE:
      //super.state=AuthSessionState.Idle;
      break;
    case OPEN:
      // super.state=AuthSessionState.Open;
      break;
    case  PENDING:
      //super.state=AuthSessionState.Pending;
      break;
    case DISCONNECTED:
      //super.state = AuthSessionState.Disconnected;
      //String sessionId = this.clientSession.getSessions().get(0).getSessionId();
      //this.clientSession.release();
      //this.baseListener.sessionDestroyed(sessionId, this.clientSession);
      endActivity();
      break;
    }
  }

  public AuthSessionState getSessionState() {
    ClientAuthSessionState state = (ClientAuthSessionState) this.clientSession
    .getState(ClientAuthSessionState.class);
    switch (state) {
    case IDLE:
      return AuthSessionState.Idle;
    case OPEN:
      return AuthSessionState.Open;
    case PENDING:
      return AuthSessionState.Pending;
    case DISCONNECTED:
      return AuthSessionState.Disconnected;
    default:
      logger.error("Unexpected state in Auth Client FSM: " + state);
      return null;
    }
  }

  public ClientAuthSession getSession() {
    return this.clientSession;
  }

  public void setSession(ClientAuthSession appSession) {
    this.clientSession = appSession;
    this.clientSession.addStateChangeNotification(this);
    super.eventListener = (EventListener<Request, Answer>) appSession;
  }

  @Override
  public void endActivity() {
    this.clientSession.release();
    super.baseListener.endActivity(getActivityHandle());
  }
}
