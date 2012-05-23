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

package org.mobicents.slee.resource.diameter.cca.handlers;

import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.auth.events.ReAuthAnswer;
import org.jdiameter.api.auth.events.ReAuthRequest;
import org.jdiameter.api.cca.ClientCCASession;
import org.jdiameter.api.cca.ServerCCASession;
import org.jdiameter.api.cca.events.JCreditControlAnswer;
import org.jdiameter.api.cca.events.JCreditControlRequest;
import org.jdiameter.common.impl.app.auth.ReAuthAnswerImpl;
import org.jdiameter.common.impl.app.auth.ReAuthRequestImpl;
import org.jdiameter.common.impl.app.cca.CCASessionFactoryImpl;
import org.jdiameter.common.impl.app.cca.JCreditControlAnswerImpl;
import org.jdiameter.common.impl.app.cca.JCreditControlRequestImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;

/**
 * 
 * CreditControlSessionFactory.java
 * 
 * <br>
 * Super project: mobicents <br>
 * 3:19:55 AM Dec 30, 2008 <br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class CreditControlSessionFactory  extends  CCASessionFactoryImpl{

  protected SessionFactory sessionFactory = null;
  protected DiameterRAInterface resourceAdaptor = null;

  // Message timeout value (in milliseconds)

  protected int defaultDirectDebitingFailureHandling = 0;
  protected int defaultCreditControlFailureHandling = 0;

  // its seconds
  protected long defaultValidityTime = 30;
  protected long defaultTxTimerValue = 10;
  protected Logger logger = Logger.getLogger(CreditControlSessionFactory.class);

  public CreditControlSessionFactory(SessionFactory sessionFactory, DiameterRAInterface resourceAdaptor) {
    super(sessionFactory);

    this.sessionFactory = sessionFactory;
    this.resourceAdaptor = resourceAdaptor;
  }

  public CreditControlSessionFactory(SessionFactory sessionFactory, DiameterRAInterface resourceAdaptor, int defaultDirectDebitingFailureHandling, int defaultCreditControlFailureHandling, long defaultValidityTime, long defaultTxTimerValue) {
    this(sessionFactory, resourceAdaptor);

    this.defaultDirectDebitingFailureHandling = defaultDirectDebitingFailureHandling;
    this.defaultCreditControlFailureHandling = defaultCreditControlFailureHandling;
    this.defaultValidityTime = defaultValidityTime;
    this.defaultTxTimerValue = defaultTxTimerValue;
  }

  public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args) {

    AppSession appSession = super.getNewSession(sessionId, aClass, applicationId, args);

    try {   
      if(appSession instanceof ClientCCASession) {
        ((ClientCCASession)appSession).addStateChangeNotification(this);
      }
      else if(appSession instanceof ServerCCASession)	{
        ((ServerCCASession)appSession).addStateChangeNotification(this);
      }
      else {
        //?
      }
    }
    catch (Exception e) {
      logger.error("Failure to obtain new Credit-Control Session.", e);
    }

    return appSession;
  }

  // ////////////////////
  // Message Handlers //
  // ////////////////////

  private void doMessage(AppSession appSession, AppEvent appEvent) throws InternalException {
    this.resourceAdaptor.fireEvent(appSession.getSessions().get(0).getSessionId(), appEvent.getMessage());
  }

  public void doCreditControlRequest(ServerCCASession session, JCreditControlRequest request) throws InternalException {
    doMessage(session, request);
  }

  public void doCreditControlAnswer(ClientCCASession session, JCreditControlRequest request, JCreditControlAnswer answer) throws InternalException {
    doMessage(session, answer);
  }

  public void doReAuthRequest(ClientCCASession session, ReAuthRequest request) throws InternalException {
    doMessage(session, request);
  }

  public void doReAuthAnswer(ServerCCASession session, ReAuthRequest request, ReAuthAnswer answer) throws InternalException {
    doMessage(session, answer);
  }

  public void doOtherEvent(AppSession session, AppRequestEvent request, AppAnswerEvent answer) throws InternalException {
    // Here we get something weird, lets do extension
    // Still we rely on CCA termination mechanisms, those message are sent via generic send, which does not trigger FSM
    if(logger.isInfoEnabled()) {
      logger.info("Diameter CCA RA :: doOtherEvent :: appSession[" + session + "], Request[" + request + "], Answer[" + answer + "]");
    }

    doMessage(session, answer != null ? answer : request);
  }

  // ///////////////////////////
  // Message Factory Methods //
  // ///////////////////////////

  public JCreditControlAnswer createCreditControlAnswer(Answer answer) {
    return new JCreditControlAnswerImpl(answer);
  }

  public JCreditControlRequest createCreditControlRequest(Request req) {
    return new JCreditControlRequestImpl(req);
  }

  public ReAuthAnswer createReAuthAnswer(Answer answer) {
    return new ReAuthAnswerImpl(answer);
  }

  public ReAuthRequest createReAuthRequest(Request req) {
    return new ReAuthRequestImpl(req);
  }

  // ///////////////////
  // Context Methods //
  // ///////////////////

  public void stateChanged(Enum oldState, Enum newState) {
    if (logger.isInfoEnabled()) {
      logger.info("Diameter CCA SessionFactory :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jdiameter.common.impl.app.cca.CCASessionFactoryImpl#stateChanged(
   * org.jdiameter.api.app.AppSession, java.lang.Enum, java.lang.Enum)
   */
  @Override
  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    if (logger.isInfoEnabled()) {
      logger.info("Diameter CCA SessionFactory :: stateChanged :: source[" + source + "] :: oldState[" + oldState + "], newState[" + newState + "]");
    }
  }

  public void sessionSupervisionTimerExpired(ServerCCASession session) {
    // this.resourceAdaptor.sessionDestroyed(session.getSessions().get(0).getSessionId(), session);
    session.release();
  }

  public void sessionSupervisionTimerReStarted(ServerCCASession session, ScheduledFuture future) {
    // TODO Complete this method.
  }

  public void sessionSupervisionTimerStarted(ServerCCASession session, ScheduledFuture future) {
    // TODO Complete this method.
  }

  public void sessionSupervisionTimerStopped(ServerCCASession session, ScheduledFuture future) {
    // TODO Complete this method.
  }

  public void timeoutExpired(Request request) {
    // FIXME What should we do when there's a timeout?
  }

  public void denyAccessOnDeliverFailure(ClientCCASession clientCCASessionImpl, Message request) {
    // TODO Complete this method.
  }

  public void denyAccessOnFailureMessage(ClientCCASession clientCCASessionImpl) {
    // TODO Complete this method.
  }

  public void denyAccessOnTxExpire(ClientCCASession clientCCASessionImpl) {
    //this.resourceAdaptor.sessionDestroyed(clientCCASessionImpl.getSessions().get(0).getSessionId(), clientCCASessionImpl);
    clientCCASessionImpl.release();
  }

  public int getDefaultCCFHValue() {
    return defaultCreditControlFailureHandling;
  }

  public int getDefaultDDFHValue() {
    return defaultDirectDebitingFailureHandling;
  }

  public long getDefaultTxTimerValue() {
    return defaultTxTimerValue;
  }

  public void grantAccessOnDeliverFailure(ClientCCASession clientCCASessionImpl, Message request) {
    // TODO Auto-generated method stub
  }

  public void grantAccessOnFailureMessage(ClientCCASession clientCCASessionImpl) {
    // TODO Auto-generated method stub
  }

  public void grantAccessOnTxExpire(ClientCCASession clientCCASessionImpl) {
    // TODO Auto-generated method stub
  }

  public void indicateServiceError(ClientCCASession clientCCASessionImpl) {
    // TODO Auto-generated method stub
  }

  public void txTimerExpired(ClientCCASession session) {
    //this.resourceAdaptor.sessionDestroyed(session.getSessions().get(0).getSessionId(), session);
    session.release();
  }

  public long[] getApplicationIds() {
    // FIXME: What should we do here?
    return new long[] { 4 };
  }

  public long getDefaultValidityTime() {
    return this.defaultValidityTime;
  }

}
