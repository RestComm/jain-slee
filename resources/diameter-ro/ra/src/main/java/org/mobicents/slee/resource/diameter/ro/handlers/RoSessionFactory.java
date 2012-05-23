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

package org.mobicents.slee.resource.diameter.ro.handlers;

import org.jdiameter.api.InternalException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.auth.events.ReAuthAnswer;
import org.jdiameter.api.auth.events.ReAuthRequest;
import org.jdiameter.api.ro.ClientRoSession;
import org.jdiameter.api.ro.ServerRoSession;
import org.jdiameter.api.ro.events.RoCreditControlAnswer;
import org.jdiameter.api.ro.events.RoCreditControlRequest;
import org.jdiameter.common.impl.app.ro.RoSessionFactoryImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoSessionFactory extends RoSessionFactoryImpl {

  public DiameterRAInterface ra;

  /**
   * @param sessionFactory
   */
  public RoSessionFactory(DiameterRAInterface ra, SessionFactory sessionFactory, int defaultDirectDebitingFailureHandling, int defaultCreditControlFailureHandling, long defaultValidityTime, long defaultTxTimerValue) {
    super(sessionFactory);

    super.defaultDirectDebitingFailureHandling = defaultDirectDebitingFailureHandling;
    super.defaultCreditControlFailureHandling = defaultCreditControlFailureHandling;
    super.defaultValidityTime = defaultValidityTime;
    super.defaultTxTimerValue = defaultTxTimerValue;
    
    this.ra = ra;
  }

  @Override
  public void doCreditControlAnswer(ClientRoSession session, RoCreditControlRequest request, RoCreditControlAnswer answer) throws InternalException {
    ra.fireEvent(session.getSessionId(), answer.getMessage());
  }

  @Override
  public void doCreditControlRequest(ServerRoSession session, RoCreditControlRequest request) throws InternalException {
    ra.fireEvent(session.getSessionId(), request.getMessage());
  }

  @Override
  public void doOtherEvent(AppSession session, AppRequestEvent request, AppAnswerEvent answer) throws InternalException {
    ra.fireEvent(session.getSessionId(), answer != null ? answer.getMessage() : request.getMessage());
  }

  @Override
  public void doReAuthAnswer(ServerRoSession session, ReAuthRequest request, ReAuthAnswer answer) throws InternalException {
    ra.fireEvent(session.getSessionId(), answer.getMessage());
  }

  @Override
  public void doReAuthRequest(ClientRoSession session, ReAuthRequest request) throws InternalException {
    ra.fireEvent(session.getSessionId(), request.getMessage());
  }

}
