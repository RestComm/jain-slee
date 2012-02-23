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

import java.io.IOException;

import net.java.slee.resource.diameter.base.DiameterException;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.events.avp.CcRequestType;
import net.java.slee.resource.diameter.gx.GxAvpFactory;
import net.java.slee.resource.diameter.gx.GxClientSessionActivity;
import net.java.slee.resource.diameter.gx.GxMessageFactory;
import net.java.slee.resource.diameter.gx.GxSessionState;
import net.java.slee.resource.diameter.gx.events.GxCreditControlRequest;
import net.java.slee.resource.diameter.gx.events.GxReAuthAnswer;
import net.java.slee.resource.diameter.gx.events.GxReAuthRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.gx.ClientGxSession;
import org.jdiameter.common.api.app.gx.ClientGxSessionState;
import org.jdiameter.common.impl.app.gx.GxCreditControlRequestImpl;
import org.jdiameter.common.impl.app.gx.GxReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Implementation of {@link GxClientSessionActivity}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public class GxClientSessionActivityImpl extends GxSessionActivityImpl implements GxClientSessionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 6391181777305378777L;
  protected transient GxMessageFactory roMessageFactory = null;
  protected transient ClientGxSession session;

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
  public GxClientSessionActivityImpl(final GxMessageFactory gxMessageFactory, final GxAvpFactory gxAvpFactory, final ClientGxSession session,
      final DiameterIdentity destinationHost, final DiameterIdentity destinationRealm, final Stack stack) {
    super(gxMessageFactory, gxAvpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diametergx.GxClientSessionActivity#createGxCreditControlRequest(net.java.slee.resource.diameter.cca.events.avp.CcRequestType)
   */
  public GxCreditControlRequest createGxCreditControlRequest(final CcRequestType type) {
    // Create the request
    final GxCreditControlRequest request = super.getGxMessageFactory().createGxCreditControlRequest(super.getSessionId()/*,type*/);
    request.setCcRequestType(type);

    // If there's a Destination-Host, add the AVP
    if (destinationHost != null) {
      request.setDestinationHost(destinationHost);
    }

    if (destinationRealm != null) {
      request.setDestinationRealm(destinationRealm);
    }

    return request;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diametergx.GxClientSessionActivity#sendEventGxCreditControlRequest(net.java.slee.resource.diameter.gx.events.GxCreditControlRequest)
   */
  public void sendEventGxCreditControlRequest(final GxCreditControlRequest ccr) throws IOException {
    // fetchCurrentState(ccr);

    final DiameterMessageImpl msg = (DiameterMessageImpl) ccr;
    validateState(ccr);
    try {
      session.sendCreditControlRequest(new GxCreditControlRequestImpl((Request) msg.getGenericData()));
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

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diametergx.GxClientSessionActivity#sendInitialRoCreditControlRequest(net.java.slee.resource.diameter.gx.events.GxCreditControlRequest)
   */
  public void sendInitialGxCreditControlRequest(final GxCreditControlRequest ccr) throws IOException {
    // FIXME: should this affect FSM ?
    validateState(ccr);

    final DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new GxCreditControlRequestImpl((Request) msg.getGenericData()));
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
   * @see net.java.slee.resource.diametergx.GxClientSessionActivity#sendUpdateRoCreditControlRequest(net.java.slee.resource.diameter.gx.events.GxCreditControlRequest)
   */
  public void sendUpdateGxCreditControlRequest(final GxCreditControlRequest ccr) throws IOException {
    // FIXME: Should this come already in the CCR?
    validateState(ccr);

    final DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new GxCreditControlRequestImpl((Request) msg.getGenericData()));
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
   * @see net.java.slee.resource.diametergx.GxClientSessionActivity#sendTerminationRoCreditControlRequest(net.java.slee.resource.diameter.gx.events.GxCreditControlRequest)
   */
  public void sendTerminationGxCreditControlRequest(final GxCreditControlRequest ccr) throws IOException {
    // This should not be used to terminate sub-sessions!
    validateState(ccr);

    final DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new GxCreditControlRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  @Override
  public GxReAuthAnswer createGxReAuthAnswer(GxReAuthRequest rar) {
    final GxReAuthAnswer answer = ((GxMessageFactoryImpl) getGxMessageFactory()).createGxReAuthAnswer(rar);
    return answer;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diametergx.GxClientSessionActivity#sendReAuthAnswer(net.java.slee.resource.diameter.base.events.ReAuthAnswer)
   */
  public void sendGxReAuthAnswer(final GxReAuthAnswer rar) throws IOException {
    final DiameterMessageImpl msg = (DiameterMessageImpl) rar;

    try {
      session.sendGxReAuthAnswer(new GxReAuthAnswerImpl((Answer) msg.getGenericData()));
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
    final ClientGxSessionState s = (ClientGxSessionState) newState;

    // IDLE(0), PENDING_EVENT(1), PENDING_INITIAL(2), PENDING_UPDATE(3),
    // PENDING_TERMINATION(4), PENDING_BUFFERED(5), OPEN(6);
    switch (s) {
      case PENDING_EVENT:
        //this.state = CreditControlSessionState.PENDING_EVENT;
        break;
      case PENDING_BUFFERED:
        //this.state = CreditControlSessionState.PENDING_BUFFERED;
        break;
      case PENDING_TERMINATION:
        //this.state = CreditControlSessionState.PENDING_TERMINATION;
        break;
      case PENDING_UPDATE:
        //this.state = CreditControlSessionState.PENDING_UPDATE;
        break;
      case OPEN:
        // FIXME: this should not happen?
        //this.state = CreditControlSessionState.OPEN;
        break;
      case PENDING_INITIAL:
        //this.state = CreditControlSessionState.PENDING_INITIAL;
        break;
      case IDLE:
        //this.state = CreditControlSessionState.IDLE;
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
        break;

      default:
        logger.error("Unexpected state in Credit-Control Client FSM: " + s);
    }
  }

  private void validateState(GxCreditControlRequest ccr) {
    //this is used for methods that send specific messages. should be done in jdiam, but there is not hook for it now.
    if (ccr.getCcRequestType() == null) {
      throw new DiameterException("No request type is present!!");
    }
    final int t = ccr.getCcRequestType().getValue();

    GxSessionState currentState = this.getState();
    if (t == CcRequestType._INITIAL_REQUEST) {
      if (currentState != GxSessionState.IDLE) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: " + currentState);
      }
    }
    else if (t == CcRequestType._UPDATE_REQUEST) {
      if (currentState != GxSessionState.OPEN) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: " + currentState);
      }
    }
    else if (t == CcRequestType._TERMINATION_REQUEST) {
      if (currentState != GxSessionState.OPEN) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: " + currentState);
      }
    }
    else if (t == CcRequestType._EVENT_REQUEST) {
      if (currentState != GxSessionState.IDLE) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: " + currentState);
      }
    }
  }

  public void setSession(ClientGxSession session2) {
    this.session = session2;
    this.session.addStateChangeNotification(this);
  }

  public GxSessionState getState() {
    final ClientGxSessionState s = this.session.getState(ClientGxSessionState.class);

    // IDLE(0), PENDING_EVENT(1), PENDING_INITIAL(2), PENDING_UPDATE(3),
    // PENDING_TERMINATION(4), PENDING_BUFFERED(5), OPEN(6);
    switch (s) {
      case PENDING_EVENT:
        return GxSessionState.PENDING_EVENT;
      case PENDING_BUFFERED:
        return GxSessionState.PENDING_BUFFERED;
      case PENDING_TERMINATION:
        return GxSessionState.PENDING_TERMINATION;
      case PENDING_UPDATE:
        return GxSessionState.PENDING_UPDATE;
      case OPEN:
        return GxSessionState.OPEN;
      case PENDING_INITIAL:
        return GxSessionState.PENDING_INITIAL;
      case IDLE:
        return GxSessionState.IDLE;
      default:
        logger.error("Unexpected state in Credit-Control Client FSM: " + s);
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
    final GxClientSessionActivityImpl other = (GxClientSessionActivityImpl) obj;
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