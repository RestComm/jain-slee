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
import net.java.slee.resource.diameter.rx.RxMessageFactory;
import net.java.slee.resource.diameter.rx.RxServerSessionActivity;
import net.java.slee.resource.diameter.rx.RxSessionState;
import net.java.slee.resource.diameter.rx.events.AAAnswer;
import net.java.slee.resource.diameter.rx.events.AARequest;
import net.java.slee.resource.diameter.rx.events.AbortSessionRequest;
import net.java.slee.resource.diameter.rx.events.ReAuthRequest;
import net.java.slee.resource.diameter.rx.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.rx.events.SessionTerminationRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.rx.ServerRxSession;
import org.jdiameter.common.api.app.rx.ServerRxSessionState;
import org.jdiameter.common.impl.app.rx.RxAAAnswerImpl;
import org.jdiameter.common.impl.app.rx.RxAbortSessionRequestImpl;
import org.jdiameter.common.impl.app.rx.RxReAuthRequestImpl;
import org.jdiameter.common.impl.app.rx.RxSessionTermAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Implementation of {@link RxServerSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class RxServerSessionActivityImpl extends RxSessionActivityImpl implements RxServerSessionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 5230054776594429948L;

  private static Logger logger = Logger.getLogger(RxServerSessionActivityImpl.class);

  protected transient ServerRxSession session = null;
  protected transient AARequest lastAARequest = null;
  protected transient SessionTerminationRequest lastSessionTermRequest = null;

  public RxServerSessionActivityImpl(final RxMessageFactory rxMessageFactory, final RxAvpFactory rxAvpFactory, final ServerRxSession session,
      final DiameterIdentity destinationHost, final DiameterIdentity destinationRealm, final Stack stack) {
    super(rxMessageFactory, rxAvpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AAAnswer createAAAnswer() {
    if (lastAARequest == null) {
      if (logger.isInfoEnabled()) {
        logger.info("No request received, cant create answer.");
      }
      return null;
    }

    final AAAnswer answer = ((RxMessageFactoryImpl) getRxMessageFactory()).createAAAnswer(lastAARequest);

    return answer;
  }
  /**
   * {@inheritDoc}
   */
  @Override
  public SessionTerminationAnswer createSessionTermAnswer() {
    if (lastSessionTermRequest == null) {
      if (logger.isInfoEnabled()) {
        logger.info("No request received, cant create answer.");
      }
      return null;
    }

    SessionTerminationAnswer answer = null;
    answer = ((RxMessageFactoryImpl) getRxMessageFactory()).createSessionTerminationAnswer(lastSessionTermRequest);
    return answer;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendAAAnswer(final AAAnswer aaa) throws IOException {
    fetchCurrentState(aaa);

    final DiameterMessageImpl msg = (DiameterMessageImpl) aaa;

    try {
      session.sendAAAnswer(new RxAAAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      final AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
      throw anae;
    }
    catch (Exception e) {
      throw new IOException("Failed to send message.", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendSessionTermAnswer(final SessionTerminationAnswer sta) throws IOException {
    fetchCurrentState(sta);

    final DiameterMessageImpl msg = (DiameterMessageImpl) sta;

    try {
      session.sendSessionTermAnswer(new RxSessionTermAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      final AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
      throw anae;
    }
    catch (Exception e) {
      throw new IOException("Failed to send message.", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendReAuthRequest(final ReAuthRequest rar) throws IOException {
    // RFC 4006 5.5

    //set Auth application ID
    //set Auth request type

    final DiameterMessageImpl msg = (DiameterMessageImpl) rar;

    try {
      session.sendReAuthRequest(new RxReAuthRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      final AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
      throw anae;
    }
    catch (Exception e) {
      throw new IOException("Failed to send message.", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendAbortSessionRequest(final AbortSessionRequest asr) throws IOException {
    // RFC 4006 5.5

    //set Auth application ID
    //set Auth request type

    final DiameterMessageImpl msg = (DiameterMessageImpl) asr;

    try {
      session.sendAbortSessionRequest(new RxAbortSessionRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      final AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
      throw anae;
    }
    catch (Exception e) {
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
      logger.info("AA Server FSM State Changed: " + oldState + " => " + newState);
    }

    final ServerRxSessionState s = (ServerRxSessionState) newState;

    // IDLE(0), OPEN(1);
    switch (s) {
      case OPEN:
        // FIXME: this should not happen?
        break;

      case IDLE:
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
        break;

      default:
        logger.error("Unexpected state in AA Server FSM: " + s);
    }
  }

  public void fetchCurrentState(final AARequest aar) {
    this.lastAARequest = aar;
    // TODO: Complete this method.
  }

  public void fetchCurrentState(final SessionTerminationRequest str) {
    this.lastSessionTermRequest = str;
    // TODO: Complete this method.
  }

  public void fetchCurrentState(final AAAnswer aaa) {
    // TODO: Complete this method.
  }

  public void fetchCurrentState(final SessionTerminationAnswer sta) {
    // TODO: Complete this method.
  }

  public ServerRxSession getSession() {
    return this.session;
  }

  public void setSession(final ServerRxSession session2) {
    this.session = session2;
    this.session.addStateChangeNotification(this);
  }

  public RxSessionState getState() {
    final ServerRxSessionState sessionState = session.getState(ServerRxSessionState.class);
    switch (sessionState) {
      case OPEN:
        return RxSessionState.OPEN;
      case IDLE:
        return RxSessionState.IDLE;
      default:
        logger.error("Unexpected state in AA Server FSM: " + sessionState);
        return null;
    }
  }

  public String toString() {
    return super.toString() + " -- Event[ " + (lastAARequest != null) + " ] Session[ " + session + " ] State[ " + getState() + " ]";
  }

  @Override
  public void endActivity() {
    this.session.release();
    super.endActivity();
  }
}
