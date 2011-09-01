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

package org.mobicents.slee.resource.diameter.rx.handlers;

import org.jdiameter.api.InternalException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;

import org.jdiameter.api.auth.events.ReAuthAnswer;
import org.jdiameter.api.auth.events.ReAuthRequest;
import org.jdiameter.api.auth.events.SessionTermAnswer;
import org.jdiameter.api.auth.events.SessionTermRequest;
import org.jdiameter.api.auth.events.AbortSessionAnswer;
import org.jdiameter.api.auth.events.AbortSessionRequest;

import org.jdiameter.api.rx.ClientRxSession;
import org.jdiameter.api.rx.ServerRxSession;
import org.jdiameter.api.rx.events.RxAAAnswer;
import org.jdiameter.api.rx.events.RxAARequest;
import org.jdiameter.common.impl.app.rx.RxSessionFactoryImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;

/**
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class RxSessionFactory extends RxSessionFactoryImpl {

  public DiameterRAInterface ra;

  /**
   * @param sessionFactory
   */
  public RxSessionFactory(DiameterRAInterface ra, SessionFactory sessionFactory) {
    super(sessionFactory);

    this.ra = ra;
  }

  @Override
  public void doAAAnswer(ClientRxSession session, RxAARequest request, RxAAAnswer answer) throws InternalException {
    ra.fireEvent(session.getSessionId(), answer.getMessage());
  }

  @Override
  public void doAARequest(ServerRxSession session, RxAARequest request) throws InternalException {
    ra.fireEvent(session.getSessionId(), request.getMessage());
  }

  @Override
  public void doSessionTermAnswer(ClientRxSession session, SessionTermRequest request, SessionTermAnswer answer) throws InternalException {
    ra.fireEvent(session.getSessionId(), answer.getMessage());
  }

  @Override
  public void doSessionTermRequest(ServerRxSession session, SessionTermRequest request) throws InternalException {
    ra.fireEvent(session.getSessionId(), request.getMessage());
  }

  @Override
  public void doOtherEvent(AppSession session, AppRequestEvent request, AppAnswerEvent answer) throws InternalException {
    if (request != null) {
      ra.fireEvent(session.getSessionId(), request.getMessage());
    }
    else {
      ra.fireEvent(session.getSessionId(), answer.getMessage());
    }
  }

  @Override
  public void doReAuthAnswer(ServerRxSession session, ReAuthRequest request, ReAuthAnswer answer) throws InternalException {
    ra.fireEvent(session.getSessionId(), answer.getMessage());
  }

  @Override
  public void doReAuthRequest(ClientRxSession session, ReAuthRequest request) throws InternalException {
    ra.fireEvent(session.getSessionId(), request.getMessage());
  }

  @Override
  public void doAbortSessionAnswer(ServerRxSession session, AbortSessionRequest request, AbortSessionAnswer answer) throws InternalException {
    ra.fireEvent(session.getSessionId(), answer.getMessage());
  }

  @Override
  public void doAbortSessionRequest(ClientRxSession session, AbortSessionRequest request) throws InternalException {
    ra.fireEvent(session.getSessionId(), request.getMessage());
  }

}
