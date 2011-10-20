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

package org.mobicents.slee.resource.diameter.s6a.handlers;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.s6a.ClientS6aSession;
import org.jdiameter.api.s6a.ServerS6aSession;
import org.jdiameter.api.s6a.events.JAuthenticationInformationAnswer;
import org.jdiameter.api.s6a.events.JAuthenticationInformationRequest;
import org.jdiameter.api.s6a.events.JCancelLocationAnswer;
import org.jdiameter.api.s6a.events.JCancelLocationRequest;
import org.jdiameter.api.s6a.events.JDeleteSubscriberDataAnswer;
import org.jdiameter.api.s6a.events.JDeleteSubscriberDataRequest;
import org.jdiameter.api.s6a.events.JInsertSubscriberDataAnswer;
import org.jdiameter.api.s6a.events.JInsertSubscriberDataRequest;
import org.jdiameter.api.s6a.events.JNotifyAnswer;
import org.jdiameter.api.s6a.events.JNotifyRequest;
import org.jdiameter.api.s6a.events.JPurgeUEAnswer;
import org.jdiameter.api.s6a.events.JPurgeUERequest;
import org.jdiameter.api.s6a.events.JResetAnswer;
import org.jdiameter.api.s6a.events.JResetRequest;
import org.jdiameter.api.s6a.events.JUpdateLocationAnswer;
import org.jdiameter.api.s6a.events.JUpdateLocationRequest;
import org.jdiameter.client.impl.app.s6a.S6aClientSessionImpl;
import org.jdiameter.common.impl.app.s6a.S6aSessionFactoryImpl;
import org.jdiameter.server.impl.app.s6a.S6aServerSessionImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class S6aSessionFactory extends S6aSessionFactoryImpl {

  private DiameterRAInterface s6aResourceAdaptor;
  private static final Logger logger = Logger.getLogger(S6aSessionFactory.class);

  public S6aSessionFactory(DiameterRAInterface s6aResourceAdaptor, long messageTimeout, SessionFactory sessionFactory) {
    super(sessionFactory);
    this.s6aResourceAdaptor = s6aResourceAdaptor;
  }

  /* (non-Javadoc)
   * @see org.jdiameter.common.api.app.IAppSessionFactory#getNewSession(java.lang.String, java.lang.Class, org.jdiameter.api.ApplicationId, java.lang.Object[])
   */
  @Override
  public AppSession getNewSession(String sessionId, Class<? extends AppSession> appSessionClass, ApplicationId applicationId, Object[] args) {
    AppSession appSession = null;

    if(appSessionClass == ServerS6aSession.class) {
      S6aServerSessionImpl serverSession = null;
      serverSession = (S6aServerSessionImpl) super.getNewSession(sessionId, appSessionClass, applicationId, args);
      appSession = serverSession;
    }
    else if(appSessionClass == ClientS6aSession.class){
      S6aClientSessionImpl clientSession = null;
      clientSession = (S6aClientSessionImpl) super.getNewSession(sessionId, appSessionClass, applicationId, args);
      appSession = clientSession;
    }
    else {
      throw new IllegalArgumentException("Wrong session class: [" + appSessionClass + "]. Supported[" + ServerS6aSession.class + "," + ClientS6aSession.class + "]");
    }

    return appSession;
  }

  /* (non-Javadoc)
   * @see org.jdiameter.api.s6a.Servers6aSessionListener#doOtherEvent(org.jdiameter.api.app.AppSession, org.jdiameter.api.app.AppRequestEvent, org.jdiameter.api.app.AppAnswerEvent)
   */
  @Override
  public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doOtherEvent :: appSession[" + appSession + "], Request[" + request + "]");

    doFireEvent(appSession, answer != null ? answer.getMessage() : request.getMessage());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jdiameter.common.impl.app.sh.ShSessionFactoryImpl#stateChanged(org
   * .jdiameter.api.app.AppSession, java.lang.Enum, java.lang.Enum)
   */
  @Override
  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    //s6aResourceAdaptor.stateChanged(source, oldState, newState);
    logger.info("Diameter S6a Session Factory :: stateChanged :: source[" + source + "] :: oldState[" + oldState + "], newState[" + newState + "]");
  }

  /* (non-Javadoc)
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Enum, java.lang.Enum)
   */
  @Override
  public void stateChanged(Enum oldState, Enum newState) {
    logger.info("Diameter S6a Session Factory :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
  }

  /* (non-Javadoc)
   * @see org.jdiameter.common.api.app.s6a.Is6aMessageFactory#getApplicationId()
   */
  @Override
  public long getApplicationId() {
    // FIXME: Not needed?
    return 16777251;
  }

  private void doFireEvent(AppSession appSession, Message message) {
    this.s6aResourceAdaptor.fireEvent(appSession.getSessions().get(0).getSessionId(), message);
  }

  @Override
  public void doUpdateLocationRequestEvent(ServerS6aSession appSession, JUpdateLocationRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doUpdateLocationRequest :: appSession[" + appSession + "], Request[" + request + "]");
    doFireEvent(appSession, request.getMessage());
  }

  @Override
  public void doUpdateLocationAnswerEvent(ClientS6aSession appSession, JUpdateLocationRequest request, JUpdateLocationAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doUpdateLocationAnswer :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    doFireEvent(appSession, answer.getMessage());
  }

  @Override
  public void doAuthenticationInformationRequestEvent(ServerS6aSession appSession, JAuthenticationInformationRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doAuthenticationInformationRequest :: appSession[" + appSession + "], Request[" + request + "]");
    doFireEvent(appSession, request.getMessage());
  }

  @Override
  public void doAuthenticationInformationAnswerEvent(ClientS6aSession appSession, JAuthenticationInformationRequest request, JAuthenticationInformationAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doAuthenticationInformationAnswer :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    doFireEvent(appSession, answer.getMessage());
  }

  @Override
  public void doCancelLocationRequestEvent(ClientS6aSession appSession, JCancelLocationRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doCancelLocationRequest :: appSession[" + appSession + "], Request[" + request + "]");
    doFireEvent(appSession, request.getMessage());
  }

  @Override
  public void doCancelLocationAnswerEvent(ServerS6aSession appSession, JCancelLocationRequest request, JCancelLocationAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doCancelLocationAnswer :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    doFireEvent(appSession, answer.getMessage());
  }

  @Override
  public void doInsertSubscriberDataRequestEvent(ClientS6aSession appSession, JInsertSubscriberDataRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doInsertSubscriberDataRequest :: appSession[" + appSession + "], Request[" + request + "]");
    doFireEvent(appSession, request.getMessage());
  }

  @Override
  public void doInsertSubscriberDataAnswerEvent(ServerS6aSession appSession, JInsertSubscriberDataRequest request, JInsertSubscriberDataAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doInsertSubscriberDataAnswer :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    doFireEvent(appSession, answer.getMessage());
  }

  @Override
  public void doDeleteSubscriberDataRequestEvent(ClientS6aSession appSession, JDeleteSubscriberDataRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doDeleteSubscriberDataRequest :: appSession[" + appSession + "], Request[" + request + "]");
    doFireEvent(appSession, request.getMessage());
  }

  @Override
  public void doDeleteSubscriberDataAnswerEvent(ServerS6aSession appSession, JDeleteSubscriberDataRequest request, JDeleteSubscriberDataAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doDeleteSubscriberDataAnswer :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    doFireEvent(appSession, answer.getMessage());
  }

  @Override
  public void doPurgeUERequestEvent(ServerS6aSession appSession, JPurgeUERequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doPurgeUERequest :: appSession[" + appSession + "], Request[" + request + "]");
    doFireEvent(appSession, request.getMessage());
  }

  @Override
  public void doPurgeUEAnswerEvent(ClientS6aSession appSession, JPurgeUERequest request, JPurgeUEAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doPurgeUEAnswer :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    doFireEvent(appSession, answer.getMessage());
  }

  @Override
  public void doResetRequestEvent(ClientS6aSession appSession, JResetRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doResetRequest :: appSession[" + appSession + "], Request[" + request + "]");
    doFireEvent(appSession, request.getMessage());
  }

  @Override
  public void doResetAnswerEvent(ServerS6aSession appSession, JResetRequest request, JResetAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doResetAnswer :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    doFireEvent(appSession, answer.getMessage());
  }

  @Override
  public void doNotifyRequestEvent(ServerS6aSession appSession, JNotifyRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doNotifyRequest :: appSession[" + appSession + "], Request[" + request + "]");
    doFireEvent(appSession, request.getMessage());
  }

  @Override
  public void doNotifyAnswerEvent(ClientS6aSession appSession, JNotifyRequest request, JNotifyAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter S6a Session Factory :: doNotifyAnswer :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    doFireEvent(appSession, answer.getMessage());
  }

}
