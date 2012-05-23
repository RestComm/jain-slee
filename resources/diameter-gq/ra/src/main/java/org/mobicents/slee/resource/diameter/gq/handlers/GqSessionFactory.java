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

package org.mobicents.slee.resource.diameter.gq.handlers;

import org.apache.log4j.Logger;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.api.auth.ServerAuthSession;
import org.jdiameter.api.auth.events.AbortSessionAnswer;
import org.jdiameter.api.auth.events.AbortSessionRequest;
import org.jdiameter.api.auth.events.ReAuthAnswer;
import org.jdiameter.api.auth.events.ReAuthRequest;
import org.jdiameter.api.auth.events.SessionTermAnswer;
import org.jdiameter.api.auth.events.SessionTermRequest;
import org.jdiameter.common.impl.app.gq.GqSessionFactoryImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;

public class GqSessionFactory extends GqSessionFactoryImpl {

  protected final static Logger logger = Logger.getLogger(GqSessionFactory.class);

  public DiameterRAInterface ra;

  public GqSessionFactory(DiameterRAInterface ra, SessionFactory sessionFactory) {
    super(sessionFactory);
    this.ra = ra;
  }

  public void doAbortSessionRequestEvent(ClientAuthSession appSession, AbortSessionRequest asr) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), asr.getMessage());
  }

  public void doAbortSessionAnswerEvent(ServerAuthSession appSession, AbortSessionAnswer asa) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), asa.getMessage());
  }

  public void doSessionTerminationRequestEvent(ServerAuthSession appSession, SessionTermRequest str) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), str.getMessage());
  }

  public void doSessionTerminationAnswerEvent(ClientAuthSession appSession, SessionTermAnswer sta) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), sta.getMessage());
  }

  public void doAuthRequestEvent(ServerAuthSession appSession, AppRequestEvent request) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), request.getMessage());
  }

  public void doAuthAnswerEvent(ClientAuthSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), answer.getMessage());
  }

  public void doReAuthRequestEvent(ClientAuthSession appSession, ReAuthRequest rar) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), rar.getMessage());
  }

  public void doReAuthAnswerEvent(ServerAuthSession appSession, ReAuthRequest rar, ReAuthAnswer raa) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), raa.getMessage());
  }

  public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException,
  IllegalDiameterStateException, RouteException, OverloadException {
    ra.fireEvent(appSession.getSessionId(), answer != null ? answer.getMessage() : request.getMessage());
  }
}
