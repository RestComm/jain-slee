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
package org.mobicents.slee.resource.diameter.ro;

import java.io.IOException;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterException;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.events.avp.CcRequestType;
import net.java.slee.resource.diameter.ro.RoClientSessionActivity;
import net.java.slee.resource.diameter.ro.RoMessageFactory;
import net.java.slee.resource.diameter.ro.RoSessionState;
import net.java.slee.resource.diameter.ro.events.RoCreditControlRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.ro.ClientRoSession;
import org.jdiameter.common.api.app.ro.ClientRoSessionState;
import org.jdiameter.common.impl.app.auth.ReAuthAnswerImpl;
import org.jdiameter.common.impl.app.ro.RoCreditControlRequestImpl;
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.cca.CreditControlClientSessionImpl;

/**
 * Implementation of {@link RoClientSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoClientSessionActivityImpl extends RoSessionActivityImpl implements RoClientSessionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 6391181777305378777L;

  protected transient RoMessageFactory roMessageFactory = null;
  protected transient ClientRoSession session;

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
  public RoClientSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ClientRoSession session, DiameterIdentity destinationHost,
      DiameterIdentity destinationRealm, Stack stack) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
    super.setRoMessageFactory(new RoMessageFactoryImpl(messageFactory, session.getSessionId(), stack));
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoClientSessionActivity#createRoCreditControlRequest(net.java.slee.resource.diameter.cca.events.avp.CcRequestType)
   */
  public RoCreditControlRequest createRoCreditControlRequest(CcRequestType type) {
    // Create the request
    RoCreditControlRequest request = super.getRoMessageFactory().createRoCreditControlRequest(super.getSessionId()/*,type*/);
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
   * @see net.java.slee.resource.diameter.ro.RoClientSessionActivity#sendEventRoCreditControlRequest(net.java.slee.resource.diameter.ro.events.RoCreditControlRequest)
   */
  public void sendEventRoCreditControlRequest(RoCreditControlRequest ccr) throws IOException {
    // fetchCurrentState(ccr);

    DiameterMessageImpl msg = (DiameterMessageImpl) ccr;
    ccr.setCcRequestType(CcRequestType.EVENT_REQUEST);
    validateState(ccr);
    try {
      session.sendCreditControlRequest(new RoCreditControlRequestImpl((Request) msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
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
   * @see net.java.slee.resource.diameter.ro.RoClientSessionActivity#sendInitialRoCreditControlRequest(net.java.slee.resource.diameter.ro.events.RoCreditControlRequest)
   */
  public void sendInitialRoCreditControlRequest(RoCreditControlRequest ccr) throws IOException {
    // FIXME: should this affect FSM ?
    ccr.setCcRequestType(CcRequestType.INITIAL_REQUEST);

    validateState(ccr);

    DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new RoCreditControlRequestImpl((Request) msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoClientSessionActivity#sendUpdateRoCreditControlRequest(net.java.slee.resource.diameter.ro.events.RoCreditControlRequest)
   */
  public void sendUpdateRoCreditControlRequest(RoCreditControlRequest ccr) throws IOException {
    // FIXME: Should this come already in the CCR?
    ccr.setCcRequestType(CcRequestType.UPDATE_REQUEST);

    validateState(ccr);

    DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new RoCreditControlRequestImpl((Request) msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoClientSessionActivity#sendTerminationRoCreditControlRequest(net.java.slee.resource.diameter.ro.events.RoCreditControlRequest)
   */
  public void sendTerminationRoCreditControlRequest(RoCreditControlRequest ccr) throws IOException {
    // This should not be used to terminate sub-sessions!

    // FIXME: Should this come already in the CCR?
    ccr.setCcRequestType(CcRequestType.TERMINATION_REQUEST);

    validateState(ccr);

    DiameterMessageImpl msg = (DiameterMessageImpl) ccr;

    try {
      session.sendCreditControlRequest(new RoCreditControlRequestImpl((Request) msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoClientSessionActivity#sendReAuthAnswer(net.java.slee.resource.diameter.base.events.ReAuthAnswer)
   */
  public void sendReAuthAnswer(ReAuthAnswer rar) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) rar;

    try {
      session.sendReAuthAnswer(new ReAuthAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
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
    ClientRoSessionState s = (ClientRoSessionState) newState;

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
      ClientRoSessionState old = (ClientRoSessionState) oldState;
      if (old == ClientRoSessionState.PENDING_EVENT) {
        super.setTerminateAfterProcessing(true);

      }
      else {
        //((CCASessionCreationListener) this.getSessionListener()).sessionDestroyed(sessionId, this);
        //this.session.release();
        endActivity();
      }
      break;

    default:
      logger.error("Unexpected state in Credit-Control Client FSM: " + s);
    }
  }

  private void validateState(RoCreditControlRequest ccr) {
    //this is used for methods that send specific messages. should be done in jdiam, but there is not hook for it now.
    if(ccr.getCcRequestType() == null) {
      throw new DiameterException("No request type is present!!");
    }
    int t = ccr.getCcRequestType().getValue();

    RoSessionState currentState = this.getState();
    if(t == CcRequestType._INITIAL_REQUEST) {
      if(currentState!=RoSessionState.IDLE) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: "+currentState);
      }
    }
    else if(t == CcRequestType._UPDATE_REQUEST) {
      if(currentState!=RoSessionState.OPEN) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: "+currentState);
      }
    }
    else if(t == CcRequestType._TERMINATION_REQUEST) {
      if(currentState!=RoSessionState.OPEN) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: "+currentState);
      }
    }
    else if(t == CcRequestType._EVENT_REQUEST) {
      if(currentState!=RoSessionState.IDLE) {
        //FIXME: change all exception to DiameterException
        throw new DiameterException("Failed to validate, intial event, wrong state: "+currentState);
      }
    }
  }

  public void setSession(ClientRoSession session2) {
    this.session = session2;
    this.session.addStateChangeNotification(this);
  }

  public RoSessionState getState() {
    ClientRoSessionState s = this.session.getState(ClientRoSessionState.class);

    // IDLE(0), PENDING_EVENT(1), PENDING_INITIAL(2), PENDING_UPDATE(3),
    // PENDING_TERMINATION(4), PENDING_BUFFERED(5), OPEN(6);
    switch (s) {
    case PENDING_EVENT:
      return RoSessionState.PENDING_EVENT;
    case PENDING_BUFFERED:
      return RoSessionState.PENDING_BUFFERED;
    case PENDING_TERMINATION:
      return RoSessionState.PENDING_TERMINATION;
    case PENDING_UPDATE:
      return RoSessionState.PENDING_UPDATE;
    case OPEN:
      return RoSessionState.OPEN;
    case PENDING_INITIAL:
      return RoSessionState.PENDING_INITIAL;
    case IDLE:
      return RoSessionState.IDLE;
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
    if (terminateAfterProcessing != other.isTerminateAfterProcessing()) {
      return false;
    }

    return true;
  }

  @Override
  public void endActivity() {
    this.session.release();
    super.baseListener.endActivity(getActivityHandle());
  }

}
