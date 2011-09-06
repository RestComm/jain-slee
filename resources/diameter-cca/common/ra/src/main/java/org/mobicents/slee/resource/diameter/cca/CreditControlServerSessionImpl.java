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

package org.mobicents.slee.resource.diameter.cca;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.cca.CreditControlServerSession;
import net.java.slee.resource.diameter.cca.CreditControlSessionState;
import net.java.slee.resource.diameter.cca.events.CreditControlAnswer;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.cca.ServerCCASession;
import org.jdiameter.common.api.app.cca.ServerCCASessionState;
import org.jdiameter.common.impl.app.auth.ReAuthRequestImpl;
import org.jdiameter.common.impl.app.cca.JCreditControlAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Start time:15:26:12 2008-12-08<br>
 * Project: mobicents-diameter-parent<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class CreditControlServerSessionImpl extends CreditControlSessionImpl implements CreditControlServerSession {

  private static final long serialVersionUID = 779792504693775120L;

  private static Logger logger = Logger.getLogger(CreditControlServerSessionImpl.class);

  protected transient ServerCCASession session = null;
  protected transient CreditControlRequest lastRequest = null;

  /**
   * 
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param timeout
   * @param destinationHost
   * @param destinationRealm
   * @param endpoint
   */
  public CreditControlServerSessionImpl(CreditControlMessageFactory messageFactory, CreditControlAVPFactory avpFactory, ServerCCASession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) session, destinationRealm, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(this.session.getSessions().get(0));
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.CreditControlServerSession#createCreditControlAnswer()
   */
  public CreditControlAnswer createCreditControlAnswer()
  {
    if(lastRequest == null) {
      if(logger.isInfoEnabled()) {
        logger.info("No request received, cant create answer.");
      }
      return null;
    }
    CreditControlAnswer answer = getCCAMessageFactory().createCreditControlAnswer(lastRequest);

    return answer;
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.CreditControlServerSession#sendCreditControlAnswer(net.java.slee.resource.diameter.cca.events.CreditControlAnswer)
   */
  public void sendCreditControlAnswer(CreditControlAnswer cca) throws IOException {
    fetchCurrentState(cca);

    DiameterMessageImpl msg = (DiameterMessageImpl)cca;

    try {
      session.sendCreditControlAnswer(new JCreditControlAnswerImpl((Answer) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
      throw anae;
    }
    catch (Exception e) {
      e.printStackTrace();
      IOException ioe = new IOException("Failed to send message, due to: " + e);
      throw ioe;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cca.CreditControlServerSession#sendReAuthRequest(net.java.slee.resource.diameter.base.events.ReAuthRequest)
   */
  public void sendReAuthRequest(ReAuthRequest rar) throws IOException {
    //RFC 4006 5.5
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
      AvpNotAllowedException anae = new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
      throw anae;
    }
    catch (Exception e) {
      e.printStackTrace();
      IOException ioe = new IOException("Failed to send message, due to: " + e);
      throw ioe;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Object, java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    this.stateChanged(oldState, newState);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(Enum oldState, Enum newState) {
    if (logger.isInfoEnabled()) {
      logger.info( "Credit-Control Server FSM State Changed: " + oldState + " => " + newState );
    }

    ServerCCASessionState s = (ServerCCASessionState) newState;

    // IDLE(0), OPEN(1);    
    switch (s)
    {
    case OPEN:
      // FIXME: this should not happen?
      //this.state = CreditControlSessionState.OPEN;
      break;

    case IDLE:
      //this.state = CreditControlSessionState.IDLE;
      // Destroy and release session
      //((CCASessionCreationListener) this.getSessionListener()).sessionDestroyed(sessionId, this);
      //this.session.release();
      //TODO: possibly endActivty(); is ok, since it happens after answer is sent.
      this.setTerminateAfterProcessing(true);
      super.baseListener.startActivityRemoveTimer(getActivityHandle());
      break;

    default:
      logger.error("Unexpected state in Credit-Control Server FSM: " + s);
    }
  }

  public void fetchCurrentState(CreditControlRequest ccr) {
    this.lastRequest = ccr;
    // TODO: Complete this method.
  }

  public void fetchCurrentState(CreditControlAnswer cca) {
    // TODO: Complete this method.
  }

  public ServerCCASession getSession() {
    return this.session;
  }

  public void setSession(ServerCCASession session2) {
    this.session = session2;
    this.session.addStateChangeNotification(this);
  }

  public CreditControlSessionState getState() {
    ServerCCASessionState sessionState = session.getState(ServerCCASessionState.class);
    switch (sessionState)
    {
    case OPEN:
      return CreditControlSessionState.OPEN;
    case IDLE:
      return CreditControlSessionState.IDLE;
    default:
      logger.error("Unexpected state in Credit-Control Server FSM: " + sessionState);
      return null;
    }
  }

  public String toString() {
    return super.toString() + " -- Event[ " + (lastRequest != null) + " ] Session[ " + session + " ] State[ " + getState() +" ]";
  }

  @Override
  public void endActivity() {
    this.session.release();
    super.endActivity();
  }

}
