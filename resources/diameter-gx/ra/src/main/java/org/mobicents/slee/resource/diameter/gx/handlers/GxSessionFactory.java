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

package org.mobicents.slee.resource.diameter.gx.handlers;

import org.jdiameter.api.InternalException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.gx.events.GxReAuthAnswer;
import org.jdiameter.api.gx.events.GxReAuthRequest;
import org.jdiameter.api.gx.ClientGxSession;
import org.jdiameter.api.gx.ServerGxSession;
import org.jdiameter.api.gx.events.GxCreditControlAnswer;
import org.jdiameter.api.gx.events.GxCreditControlRequest;
import org.jdiameter.common.impl.app.gx.GxSessionFactoryImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;

/**
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public class GxSessionFactory extends GxSessionFactoryImpl {

    public DiameterRAInterface ra;

    /**
     * @param sessionFactory
     */
    public GxSessionFactory(DiameterRAInterface ra, SessionFactory sessionFactory, int defaultDirectDebitingFailureHandling,
                            int defaultCreditControlFailureHandling, long defaultValidityTime, long defaultTxTimerValue) {
        super(sessionFactory);

        super.defaultDirectDebitingFailureHandling = defaultDirectDebitingFailureHandling;
        super.defaultCreditControlFailureHandling = defaultCreditControlFailureHandling;
        super.defaultValidityTime = defaultValidityTime;
        super.defaultTxTimerValue = defaultTxTimerValue;

        this.ra = ra;
    }

    @Override
    public void doCreditControlAnswer(ClientGxSession session, GxCreditControlRequest request, GxCreditControlAnswer answer) throws InternalException {
        ra.fireEvent(session.getSessionId(), answer.getMessage());
    }

    @Override
    public void doCreditControlRequest(ServerGxSession session, GxCreditControlRequest request) throws InternalException {
        ra.fireEvent(session.getSessionId(), request.getMessage());
    }

    @Override
    public void doOtherEvent(AppSession session, AppRequestEvent request, AppAnswerEvent answer) throws InternalException {
        ra.fireEvent(session.getSessionId(), answer != null ? answer.getMessage() : request.getMessage());
    }

    @Override
    public void doGxReAuthAnswer(ServerGxSession session, GxReAuthRequest request, GxReAuthAnswer answer) throws InternalException {
        ra.fireEvent(session.getSessionId(), answer.getMessage());
    }

    @Override
    public void doGxReAuthRequest(ClientGxSession session, GxReAuthRequest request) throws InternalException {
        ra.fireEvent(session.getSessionId(), request.getMessage());
    }
}
