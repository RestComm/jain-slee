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

package org.mobicents.slee.resource.diameter.cca;

import java.io.IOException;

import net.java.slee.resource.diameter.base.DiameterException;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.cca.CreditControlClientSession;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.cca.CreditControlSessionState;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;
import net.java.slee.resource.diameter.cca.events.avp.CcRequestType;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.cca.ClientCCASession;
import org.jdiameter.common.api.app.cca.ClientCCASessionState;
import org.jdiameter.common.impl.app.auth.ReAuthAnswerImpl;
import org.jdiameter.common.impl.app.cca.JCreditControlRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Start time:15:00:53 2008-12-08<br>
 * Project: mobicents-diameter-parent<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class CreditControlClientSessionImpl extends CreditControlSessionImpl implements CreditControlClientSession {

  private static final long serialVersionUID = -391269379875938636L;

  protected transient ClientCCASession session = null;

  /**
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param raEventListener
   * @param timeout
   * @param destinationHost
   * @param destinationRealm
   * @param endpoint
   */
  public CreditControlClientSessionImpl(CreditControlMessageFactory messageFactory, CreditControlAVPFactory avpFactory, ClientCCASession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) session, destinationHost, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(this.session.getSessions().get(0));
  }

  public CreditControlAVPFactory getCreditControlAvpFactory() {
    return this.ccaAvpFactory;
  }

  public CreditControlMessageFactory getCreditControlMessageFactory() {
    return this.ccaMessageFactory;
  }

  /*
   * (non-Javadoc)
   * 
   * @seenet.java.slee.resource.diameter.cca.CreditControlClientSession#
   * createCreditControlRequest()
   */
  public CreditControlRequest createCreditControlRequest() {
    // Create the request
    CreditControlRequest request = super.ccaMessageFactory.createCreditControlRequest(super.getSessionId());

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
   * 
   * @seenet.java.slee.resource.diameter.cca.CreditControlClientSession#
   * sendCreditControlRequest
   * (net.java.slee.resource.diameter.cca.events.CreditControlRequest)
   */
  public void sendCreditControlRequest(CreditControlRequest ccr) throws IOException {
    // fetchCurrentState(ccr);

    DiameterMessageImpl msg = (DiameterMessageImpl) ccr;
    validateState(ccr);
    try {
      session.sendCreditControlRequest(new JCreditControlRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @seenet.java.slee.resource.diameter.cca.CreditControlClientSession#
   * sendInitialCreditControlRequest
   * (net.java.slee.resource.diameter.cca.events.CreditControlRequest)
   */
  public void sendInitialCreditControlRequest(CreditControlRequest ccr) throws IOException {
    // FIXME: should this affect FSM ?
    ccr.setCcRequestType(CcRequestType.INITIAL_REQUEST);

    validateState(ccr);

    DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new JCreditControlRequestImpl((Request) msg.getGenericData()));
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
   * 
   * @seenet.java.slee.resource.diameter.cca.CreditControlClientSession#
   * sendUpdateCreditControlRequest
   * (net.java.slee.resource.diameter.cca.events.CreditControlRequest)
   */
  public void sendUpdateCreditControlRequest(CreditControlRequest ccr) throws IOException {
    // FIXME: Should this come already in the CCR?
    ccr.setCcRequestType(CcRequestType.UPDATE_REQUEST);

    validateState(ccr);

    DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new JCreditControlRequestImpl((Request) msg.getGenericData()));
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
   * 
   * @seenet.java.slee.resource.diameter.cca.CreditControlClientSession#
   * sendTerminationCreditControlRequest
   * (net.java.slee.resource.diameter.cca.events.CreditControlRequest)
   */
  public void sendTerminationCreditControlRequest(CreditControlRequest ccr) throws IOException {
    // This should not be used to terminate sub-sessions!

    // FIXME: Should this come already in the CCR?
    ccr.setCcRequestType(CcRequestType.TERMINATION_REQUEST);

    validateState(ccr);

    DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new JCreditControlRequestImpl((Request) msg.getGenericData()));
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
   * 
   * @seenet.java.slee.resource.diameter.cca.CreditControlClientSession#
   * sendReAuthAnswer
   * (net.java.slee.resource.diameter.base.events.ReAuthAnswer)
   */
  public void sendReAuthAnswer(ReAuthAnswer rar) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) rar;

    try {
      session.sendReAuthAnswer(new ReAuthAnswerImpl((Answer) msg.getGenericData()));
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
   * 
   * @see
   * org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Object,
   * java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(AppSession arg0, Enum oldState, Enum newState) {
    this.stateChanged(oldState, newState);

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Enum,
   * java.lang.Enum)
   */
  public void stateChanged(Enum oldState, Enum newState) {
    ClientCCASessionState s = (ClientCCASessionState) newState;

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

  private void validateState(CreditControlRequest ccr) {
    //this is used for methods that send specific messages. should be done in jdiam, but there is not hook for it now.
    if(ccr.getCcRequestType() == null) {
      throw new DiameterException("No request type is present!!");
    }
    int t = ccr.getCcRequestType().getValue();

    CreditControlSessionState currentState = this.getState();
    if(t == CcRequestType._INITIAL_REQUEST) {
      if(currentState!=CreditControlSessionState.IDLE) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: "+currentState);
      }
    }
    else if(t == CcRequestType._UPDATE_REQUEST) {
      if(currentState!=CreditControlSessionState.OPEN) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: "+currentState);
      }
    }
    else if(t == CcRequestType._TERMINATION_REQUEST) {
      if(currentState!=CreditControlSessionState.OPEN) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: "+currentState);
      }
    }
    else if(t == CcRequestType._EVENT_REQUEST) {
      if(currentState!=CreditControlSessionState.IDLE) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: "+currentState);
      }
    }
  }

  public void setSession(ClientCCASession session2) {
    this.session = session2;
    this.session.addStateChangeNotification(this);
  }

  public CreditControlSessionState getState() {
    ClientCCASessionState s = this.session.getState(ClientCCASessionState.class);

    // IDLE(0), PENDING_EVENT(1), PENDING_INITIAL(2), PENDING_UPDATE(3),
    // PENDING_TERMINATION(4), PENDING_BUFFERED(5), OPEN(6);
    switch (s) {
    case PENDING_EVENT:
      return CreditControlSessionState.PENDING_EVENT;
    case PENDING_BUFFERED:
      return CreditControlSessionState.PENDING_BUFFERED;
    case PENDING_TERMINATION:
      return CreditControlSessionState.PENDING_TERMINATION;
    case PENDING_UPDATE:
      return CreditControlSessionState.PENDING_UPDATE;
    case OPEN:
      return CreditControlSessionState.OPEN;
    case PENDING_INITIAL:
      return CreditControlSessionState.PENDING_INITIAL;
    case IDLE:
      return CreditControlSessionState.IDLE;
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
    CreditControlClientSessionImpl other = (CreditControlClientSessionImpl) obj;
    if (terminateAfterProcessing != other.terminateAfterProcessing) {
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
