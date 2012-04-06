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

package org.mobicents.slee.resource.diameter.ro;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.ro.RoAvpFactory;
import net.java.slee.resource.diameter.ro.RoMessageFactory;
import net.java.slee.resource.diameter.ro.RoServerSessionActivity;
import net.java.slee.resource.diameter.ro.RoSessionState;
import net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer;
import net.java.slee.resource.diameter.ro.events.RoCreditControlRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.ro.ServerRoSession;
import org.jdiameter.common.api.app.ro.ServerRoSessionState;
import org.jdiameter.common.impl.app.auth.ReAuthRequestImpl;
import org.jdiameter.common.impl.app.ro.RoCreditControlAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Implementation of {@link RoServerSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoServerSessionActivityImpl extends RoSessionActivityImpl implements RoServerSessionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 5230054776594429948L;

  private static Logger logger = Logger.getLogger(RoServerSessionActivityImpl.class);

  protected transient ServerRoSession session = null;
  protected transient RoCreditControlRequest lastRequest = null;

  public RoServerSessionActivityImpl(RoMessageFactory roMessageFactory, RoAvpFactory roAvpFactory, ServerRoSession session, DiameterIdentity destinationHost,
      DiameterIdentity destinationRealm, Stack stack) {
    super(roMessageFactory, roAvpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.CreditControlServerSession#
   * createCreditControlAnswer()
   */
  public RoCreditControlAnswer createRoCreditControlAnswer() {
    if (lastRequest == null) {
      if (logger.isInfoEnabled()) {
        logger.info("No request received, cant create answer.");
      }
      return null;
    }
    RoCreditControlAnswer answer = ((RoMessageFactoryImpl) getRoMessageFactory()).createRoCreditControlAnswer(lastRequest);

    return answer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.CreditControlServerSession#
   * sendCreditControlAnswer
   * (net.java.slee.resource.diameter.cca.events.CreditControlAnswer)
   */
  public void sendRoCreditControlAnswer(RoCreditControlAnswer cca) throws IOException {
    fetchCurrentState(cca);

    DiameterMessageImpl msg = (DiameterMessageImpl) cca;

    try {
      session.sendCreditControlAnswer(new RoCreditControlAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message, due to: ", e);
      }
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.CreditControlServerSession#
   * sendReAuthRequest
   * (net.java.slee.resource.diameter.base.events.ReAuthRequest)
   */
  public void sendReAuthRequest(ReAuthRequest rar) throws IOException {
    // RFC 4006 5.5
    if(!rar.hasReAuthRequestType()) {
      rar.setReAuthRequestType(ReAuthRequestType.AUTHORIZE_ONLY);
    }
    if(!rar.hasAuthApplicationId()) {
      rar.setAuthApplicationId(CreditControlMessageFactory._CCA_AUTH_APP_ID);
    }

    DiameterMessageImpl msg = (DiameterMessageImpl) rar;

    try {
      session.sendReAuthRequest(new ReAuthRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message, due to: ", e);
      }
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
  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
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
    if (logger.isInfoEnabled()) {
      logger.info("Credit-Control Server FSM State Changed: " + oldState + " => " + newState);
    }

    ServerRoSessionState s = (ServerRoSessionState) newState;

    // IDLE(0), OPEN(1);
    switch (s) {
    case OPEN:
      // FIXME: this should not happen?
      // this.state = CreditControlSessionState.OPEN;
      break;

    case IDLE:
      // this.state = CreditControlSessionState.IDLE;
      // Destroy and release session
      // ((CCASessionCreationListener)
      // this.getSessionListener()).sessionDestroyed(sessionId, this);
      // this.session.release();
      this.setTerminateAfterProcessing(true);
      super.baseListener.startActivityRemoveTimer(getActivityHandle());
      break;

    default:
      logger.error("Unexpected state in Credit-Control Server FSM: " + s);
    }
  }

  public void fetchCurrentState(RoCreditControlRequest ccr) {
    this.lastRequest = ccr;
    // TODO: Complete this method.
  }

  public void fetchCurrentState(RoCreditControlAnswer cca) {
    // TODO: Complete this method.
  }

  public ServerRoSession getSession() {
    return this.session;
  }

  public void setSession(ServerRoSession session2) {
    this.session = session2;
    this.session.addStateChangeNotification(this);
  }

  public RoSessionState getState() {
    ServerRoSessionState sessionState = session.getState(ServerRoSessionState.class);
    switch (sessionState) {
    case OPEN:
      return RoSessionState.OPEN;
    case IDLE:
      return RoSessionState.IDLE;
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
    super.endActivity();
  }

}
