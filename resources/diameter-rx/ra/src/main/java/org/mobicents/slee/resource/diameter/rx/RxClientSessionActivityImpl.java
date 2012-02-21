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

package org.mobicents.slee.resource.diameter.rx;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rx.RxAvpFactory;
import net.java.slee.resource.diameter.rx.RxClientSessionActivity;
import net.java.slee.resource.diameter.rx.RxMessageFactory;
import net.java.slee.resource.diameter.rx.RxSessionState;
import net.java.slee.resource.diameter.rx.events.AARequest;
import net.java.slee.resource.diameter.rx.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.rx.events.ReAuthAnswer;
import net.java.slee.resource.diameter.rx.events.SessionTerminationRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.rx.ClientRxSession;
import org.jdiameter.common.api.app.rx.ClientRxSessionState;
import org.jdiameter.common.impl.app.rx.RxAARequestImpl;
import org.jdiameter.common.impl.app.rx.RxAbortSessionAnswerImpl;
import org.jdiameter.common.impl.app.rx.RxReAuthAnswerImpl;
import org.jdiameter.common.impl.app.rx.RxSessionTermRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Implementation of {@link RxClientSessionActivity}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class RxClientSessionActivityImpl extends RxSessionActivityImpl implements RxClientSessionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 6391181777305378777L;

  protected transient ClientRxSession session;

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
  public RxClientSessionActivityImpl(final RxMessageFactory rxMessageFactory, final RxAvpFactory rxAvpFactory, final ClientRxSession session,
      final DiameterIdentity destinationHost, final DiameterIdentity destinationRealm, final Stack stack) {
    super(rxMessageFactory, rxAvpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
  }


  public AARequest createRxAARequest() {
    // Create the request
    final AARequest request = super.getRxMessageFactory().createAARequest(super.getSessionId()/*,type*/);

    // If there's a Destination-Host, add the AVP
    if (destinationHost != null) {
      request.setDestinationHost(destinationHost);
    }

    if (destinationRealm != null) {
      request.setDestinationRealm(destinationRealm);
    }

    return request;
  }

  public SessionTerminationRequest createSessionTermRequest() {
    // Create the request
    final SessionTerminationRequest request = super.getRxMessageFactory().createSessionTerminationRequest();

    // If there's a Destination-Host, add the AVP
    if (destinationHost != null) {
      request.setDestinationHost(destinationHost);
    }

    if (destinationRealm != null) {
      request.setDestinationRealm(destinationRealm);
    }

    return request;
  }

  public void sendRxAARequest(final AARequest aar) throws IOException {

    final DiameterMessageImpl msg = (DiameterMessageImpl) aar;
    try {
      session.sendAARequest(new RxAARequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public void sendSessionTermRequest(final SessionTerminationRequest str) throws IOException {
    final DiameterMessageImpl msg = (DiameterMessageImpl) str;
    try {
      session.sendSessionTermRequest(new RxSessionTermRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public void sendReAuthAnswer(final ReAuthAnswer raa) throws IOException {
    final DiameterMessageImpl msg = (DiameterMessageImpl) raa;

    try {
      session.sendReAuthAnswer(new RxReAuthAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public void sendAbortSessionAnswer(final AbortSessionAnswer asr) throws IOException {
    final DiameterMessageImpl msg = (DiameterMessageImpl) asr;

    try {
      session.sendAbortSessionAnswer(new RxAbortSessionAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }


  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Object, java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(AppSession arg0, Enum oldState, Enum newState) {
    this.stateChanged(oldState, newState);
  }

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(Enum oldState, Enum newState) {
    final ClientRxSessionState s = (ClientRxSessionState) newState;

    switch (s) {
      case IDLE:
        //this.state = CreditControlSessionState.IDLE;
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
        break;

      default:
        logger.error("Unexpected state in AA Client FSM: " + s);
    }
  }

  public void setSession(ClientRxSession session2) {
    this.session = session2;
    this.session.addStateChangeNotification(this);
  }

  public RxSessionState getState() {
    final ClientRxSessionState s = this.session.getState(ClientRxSessionState.class);

    // IDLE(0), PENDING_EVENT(1), PENDING_INITIAL(2), PENDING_UPDATE(3),
    // PENDING_TERMINATION(4), PENDING_BUFFERED(5), OPEN(6);
    switch (s) {
      case PENDING_EVENT:
        return RxSessionState.PENDING_EVENT;
      case PENDING_BUFFERED:
        return RxSessionState.PENDING_BUFFERED;
      case PENDING_STR:
        return RxSessionState.PENDING_STR;
      case PENDING_AAR:
        return RxSessionState.PENDING_AAR;
      case OPEN:
        return RxSessionState.OPEN;
      case IDLE:
        return RxSessionState.IDLE;
      default:
        logger.error("Unexpected state in AA Client FSM: " + s);
        return null;
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (isTerminateAfterProcessing() ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final RxClientSessionActivityImpl other = (RxClientSessionActivityImpl) obj;
    if (terminateAfterProcessing != other.isTerminateAfterProcessing()) {
      return false;
    }

    return true;
  }

  @Override
  public void endActivity() {
    this.session.release();
    super.endActivity();
  }

}
