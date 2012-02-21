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

package org.mobicents.slee.resource.diameter.gx;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;
import net.java.slee.resource.diameter.gx.GxAvpFactory;
import net.java.slee.resource.diameter.gx.GxMessageFactory;
import net.java.slee.resource.diameter.gx.GxServerSessionActivity;
import net.java.slee.resource.diameter.gx.GxSessionState;
import net.java.slee.resource.diameter.gx.events.GxCreditControlAnswer;
import net.java.slee.resource.diameter.gx.events.GxCreditControlRequest;
import net.java.slee.resource.diameter.gx.events.GxReAuthRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.gx.ServerGxSession;
import org.jdiameter.common.api.app.gx.ServerGxSessionState;
import org.jdiameter.common.impl.app.gx.GxCreditControlAnswerImpl;
import org.jdiameter.common.impl.app.gx.GxReAuthRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Implementation of {@link GxServerSessionActivity}.
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public class GxServerSessionActivityImpl extends GxSessionActivityImpl implements GxServerSessionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 5230054776594429948L;
  private static Logger logger = Logger.getLogger(GxServerSessionActivityImpl.class);
  protected transient ServerGxSession session = null;
  protected transient GxCreditControlRequest lastRequest = null;

  public GxServerSessionActivityImpl(final GxMessageFactory gxMessageFactory, final GxAvpFactory gxAvpFactory, final ServerGxSession session,
      final DiameterIdentity destinationHost, final DiameterIdentity destinationRealm, final Stack stack) {
    super(gxMessageFactory, gxAvpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(this.session.getSessions().get(0));
  }

  /**
   * {@inheritDoc}
   */
   @Override
   public GxCreditControlAnswer createGxCreditControlAnswer() {
     if (lastRequest == null) {
       if (logger.isInfoEnabled()) {
         logger.info("No request received, cant create answer.");
       }
       return null;
     }

     final GxCreditControlAnswer answer = ((GxMessageFactoryImpl) getGxMessageFactory()).createGxCreditControlAnswer(lastRequest);

     return answer;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void sendGxCreditControlAnswer(final GxCreditControlAnswer cca) throws IOException {
     fetchCurrentState(cca);

     final DiameterMessageImpl msg = (DiameterMessageImpl) cca;

     try {
       session.sendCreditControlAnswer(new GxCreditControlAnswerImpl((Answer) msg.getGenericData()));
     }
     catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
       final AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
       throw anae;
     }
     catch (Exception e) {
       throw new IOException("Failed to send message.", e);
     }
   }

   public GxReAuthRequest createGxReAuthRequest() {
     // Create the request
     final GxReAuthRequest request = super.getGxMessageFactory().createGxReAuthRequest(super.getSessionId()/*,type*/);

     // If there's a Destination-Host, add the AVP
     if (destinationHost != null) {
       request.setDestinationHost(destinationHost);
     }

     if (destinationRealm != null) {
       request.setDestinationRealm(destinationRealm);
     }

     return request;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void sendGxReAuthRequest(final GxReAuthRequest rar) throws IOException {
     // RFC 4006 5.5
     if(!rar.hasReAuthRequestType()) {
       rar.setReAuthRequestType(ReAuthRequestType.AUTHORIZE_ONLY);
     }
     //*****
     //if(!rar.hasAuthApplicationId()) {
     //  rar.setAuthApplicationId(((Gxmthis.messageFactoryGxMessageFactory._GX_AUTH_APP_ID);
     //}
     //************

     final DiameterMessageImpl msg = (DiameterMessageImpl) rar;

     try {
       session.sendGxReAuthRequest(new GxReAuthRequestImpl((Request) msg.getGenericData()));
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
       logger.info("Credit-Control Server FSM State Changed: " + oldState + " => " + newState);
     }

     final ServerGxSessionState s = (ServerGxSessionState) newState;

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
         logger.error("Unexpected state in Credit-Control Server FSM: " + s);
     }
   }

   public void fetchCurrentState(final GxCreditControlRequest ccr) {
     this.lastRequest = ccr;
     // TODO: Complete this method.
   }

   public void fetchCurrentState(final GxCreditControlAnswer cca) {
     // TODO: Complete this method.
   }

   public ServerGxSession getSession() {
     return this.session;
   }

   public void setSession(final ServerGxSession session2) {
     this.session = session2;
     this.session.addStateChangeNotification(this);
   }

   public GxSessionState getState() {
     final ServerGxSessionState sessionState = session.getState(ServerGxSessionState.class);
     switch (sessionState) {
       case OPEN:
         return GxSessionState.OPEN;
       case IDLE:
         return GxSessionState.IDLE;
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