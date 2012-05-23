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

package org.mobicents.slee.resource.diameter.base.handlers;

import java.util.HashMap;
import java.util.HashSet;

import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.acc.ClientAccSession;
import org.jdiameter.api.acc.ClientAccSessionListener;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.api.acc.ServerAccSessionListener;
import org.jdiameter.api.acc.events.AccountAnswer;
import org.jdiameter.api.acc.events.AccountRequest;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.client.api.ISessionFactory;
import org.jdiameter.common.api.app.IAppSessionFactory;
import org.jdiameter.common.impl.app.acc.AccSessionFactoryImpl;

/**
 * 
 * AccountingSessionFactory.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AccountingSessionFactory extends AccSessionFactoryImpl implements IAppSessionFactory, ServerAccSessionListener, StateChangeListener<AppSession>, ClientAccSessionListener {

  private static HashSet<Integer> accEventCodes = new HashSet<Integer>();
  private static HashSet<Integer> authEventCodes = new HashSet<Integer>();

  static {
    authEventCodes.add(AbortSessionAnswer.commandCode);
    authEventCodes.add(ReAuthAnswer.commandCode);
    authEventCodes.add(SessionTerminationAnswer.commandCode);

    accEventCodes.add(AccountingAnswer.commandCode);
  }

  protected HashMap<ApplicationId, DiameterRAInterface> ras;
  protected long messageTimeout = 5000;
  protected SessionFactory sessionFactory = null;
  protected final static Logger logger = Logger.getLogger(AccountingSessionFactory.class);

  public static AccountingSessionFactory INSTANCE = new AccountingSessionFactory();

  /*
   * public AccountingSessionFactory(BaseSessionCreationListener ra, long
   * messageTimeout, SessionFactory sessionFactory, ApplicationId appId) {
   * super(); this.ras.put(appId, ra); this.messageTimeout = messageTimeout;
   * this.sessionFactory = sessionFactory; }
   */

  private AccountingSessionFactory() {
    this.ras = new HashMap<ApplicationId, DiameterRAInterface>();
  }

  public void registerListener(DiameterRAInterface ra, long messageTimeout, SessionFactory sessionFactory) {
    for (ApplicationId appId : ra.getSupportedApplications()) {
      this.ras.put(appId, ra);
    }
    this.messageTimeout = messageTimeout;
    this.sessionFactory = sessionFactory;
    super.setSessionFactory((ISessionFactory) sessionFactory);
  }

  public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args) {
    AppSession session = super.getNewSession(sessionId, aClass, applicationId, args);
    if (aClass == ServerAccSession.class) {
      //BaseSessionCreationListener ra = this.ras.get(applicationId) != null ? this.ras.get(applicationId) : this.ras.values().iterator().next();
      //ra.sessionCreated((ServerAccSession)session);
      return session;
    }
    else if (aClass == ClientAccSession.class) {
      //BaseSessionCreationListener ra = this.ras.get(applicationId) != null ? this.ras.get(applicationId) : this.ras.values().iterator().next();
      //ra.sessionCreated((ClientAccSession)session);
      return session;
    }
    
    return null;
  }

  public void doAccRequestEvent(ServerAccSession appSession, AccountRequest acr) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AccountingSessionFactory :: doAccRequestEvent :: appSession[" + appSession + "], Request[" + acr + "]");

    doFireEvent(appSession, acr.getMessage());
  }

  public void doAccAnswerEvent(ClientAccSession appSession, AccountRequest acr, AccountAnswer aca) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("doAccAnswerEvent :: appSession[" + appSession + "], Request[" + acr + "], Answer[" + aca + "]");

    doFireEvent(appSession, aca.getMessage());
  }

  public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AccountingSessionFactory :: doOtherEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");

    doFireEvent(appSession, answer != null ? answer.getMessage() : request.getMessage());
  }

  public void stateChanged(Enum oldState, Enum newState) {
    logger.info("Diameter Base AccountingSessionFactory :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
    //FIXME: add code here.
  }

  /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jdiameter.common.impl.app.auth.AuthSessionFactoryImpl#stateChanged
	 * (org.jdiameter.api.app.AppSession, java.lang.Enum, java.lang.Enum)
	 */
	@Override
	public void stateChanged(AppSession source, Enum oldState, Enum newState) {
		//inform one who might need that
		//BaseSessionCreationListener ra = this.ras.get(source.getSessionAppId());
		//FIXME: Alex?
		//ra.stateChanged(source,oldState,newState);
		if(logger.isInfoEnabled())
		{
			logger.info("Diameter Base AccountingSessionFactory :: stateChanged :: source["+source+"] :: oldState[" + oldState + "], newState[" + newState + "]");
		}
	}
  
  private void doFireEvent(AppSession appSession, Message message) {
    ApplicationId appId = null;
    for(ApplicationId curAppId : message.getApplicationIdAvps()) {
      if(curAppId.getAcctAppId() != ApplicationId.UNDEFINED_VALUE && this.ras.containsKey(curAppId)) {
        appId = curAppId;
        break;
      }
    }

    DiameterRAInterface ra = appId != null ? this.ras.get(appId) : this.ras.values().iterator().next();

    if(ra != null) {
      ra.fireEvent(appSession.getSessions().get(0).getSessionId(), message);
    }
    else {
      // TODO: tracer
    }
  }
}
