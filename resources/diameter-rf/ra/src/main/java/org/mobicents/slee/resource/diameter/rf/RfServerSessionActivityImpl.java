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

package org.mobicents.slee.resource.diameter.rf;

import java.io.IOException;
import java.util.ArrayList;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rf.RfAvpFactory;
import net.java.slee.resource.diameter.rf.RfMessageFactory;
import net.java.slee.resource.diameter.rf.RfServerSessionActivity;
import net.java.slee.resource.diameter.rf.RfSessionState;
import net.java.slee.resource.diameter.rf.events.RfAccountingAnswer;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.Avp;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.rf.ServerRfSession;
import org.jdiameter.common.api.app.rf.ServerRfSessionState;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.rf.events.RfAccountingAnswerImpl;

/**
 * Implementation of {@link RfServerSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RfServerSessionActivityImpl extends RfSessionActivityImpl implements RfServerSessionActivity {

  private static final long serialVersionUID = -4463687722140594904L;

  /**
   * Should contain requests, so we can create answer.
   */
  protected transient ArrayList<DiameterMessageImpl> stateMessages = new ArrayList<DiameterMessageImpl>();

  protected transient ServerRfSession serverSession = null;

  // FIXME: These are default values, should be overriden by stack.
  protected String originHost = "aaa://127.0.0.1:3868";
  protected String originRealm = "mobicents.org";

  // boolean destroyAfterSending = false;

  public RfServerSessionActivityImpl(RfMessageFactory rfMessageFactory, RfAvpFactory rfAvpFactory, ServerRfSession session, DiameterIdentity destinationHost,
      DiameterIdentity destinationRealm, Stack stack) {
    super(rfMessageFactory, rfAvpFactory, null, (EventListener<Request, Answer>) session, destinationHost, destinationRealm);

    this.originHost = stack.getMetaData().getLocalPeer().getUri().toString();
    this.originRealm = stack.getMetaData().getLocalPeer().getRealmName();

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
  }

  public RfAccountingAnswer createRfAccountingAnswer(RfAccountingRequest request) {
    try {
      // Extract interesting AVPs
      ArrayList<DiameterAvp> copyAvps = new ArrayList<DiameterAvp>();

      copyAvps.add(avpFactory.createAvp(Avp.SESSION_ID, serverSession.getSessions().get(0).getSessionId()));

      copyAvps.add(avpFactory.createAvp(Avp.ORIGIN_HOST, this.originHost.getBytes()));
      copyAvps.add(avpFactory.createAvp(Avp.ORIGIN_REALM, this.originRealm.getBytes()));

      for(DiameterAvp avp : request.getAvps()) {
        if(avp.getCode() == Avp.ACC_RECORD_NUMBER || avp.getCode() == Avp.ACC_RECORD_TYPE /* || 
            avp.getCode() == Avp.ACCT_APPLICATION_ID || avp.getCode() == Avp.VENDOR_SPECIFIC_APPLICATION_ID*/) {
          copyAvps.add((DiameterAvp) avp.clone());
        }
      }

      RfAccountingAnswerImpl answer = (RfAccountingAnswerImpl) ((RfMessageFactoryImpl)rfMessageFactory).createRfAccountingMessage(request.getHeader(), copyAvps.toArray(new DiameterAvp[copyAvps.size()]));

      // Get the raw Answer
      Message rawAnswer = answer.getGenericData();

      // This is an answer.
      rawAnswer.setRequest(false);
      rawAnswer.setReTransmitted(false); // just in case. answers never have T flag set

      answer.setData(request);

      return answer;
    }
    catch (Exception e) {
      logger.error("", e);
    }

    return null;
  }

  public RfAccountingAnswer createRfAccountingAnswer(RfAccountingRequest request, int resultCode) {
    RfAccountingAnswer answer = this.createRfAccountingAnswer(request);

    answer.setResultCode(resultCode);

    return answer;
  }

  public void sendRfAccountingAnswer(RfAccountingAnswer answer) throws IOException {

    try {
      RfAccountingAnswerImpl aca = (RfAccountingAnswerImpl) answer;
      this.serverSession.sendAccountAnswer(new org.jdiameter.common.impl.app.rf.RfAccountingAnswerImpl((Answer) aca.getGenericData()));

      // FIXME: check this?
      if (isTerminateAfterProcessing()) {
        endActivity();
        // this.serverSession.release();
        //
        // if(!serverSession.isValid()) {
        // String sessionId =
        // this.serverSession.getSessions().get(0).getSessionId();
        // this.baseListener.sessionDestroyed(sessionId,
        // this.serverSession);
        // }
      }
      //clean
      clean(aca);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message, due to: ", e);
      }
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public ServerRfSession getSession() {
    return this.serverSession;
  }

  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    stateChanged(oldState, newState);
  }

  public void stateChanged(Enum oldState, Enum newState) {
    if (newState == ServerRfSessionState.IDLE) {
      this.setTerminateAfterProcessing(true);
      super.baseListener.startActivityRemoveTimer(getActivityHandle());
    }
    else {
      // super.state = AccountingSessionState.Open;
    }
  }

  @Override
  public RfSessionState getRfSessionState() {
    ServerRfSessionState state = (ServerRfSessionState) this.serverSession.getState(ServerRfSessionState.class);

    // FIXME: baranowb: PendingL - where does this fit?
    switch (state) {
    case IDLE:
      return RfSessionState.Idle;
    case OPEN:
      return RfSessionState.Open;
    default:
      logger.error("Unexpected state in Accounting Server FSM: " + state);
      return null;
    }
  }

  public RfAccountingAnswer createRfAccountingAnswer() {
	  RfAccountingAnswer answer = null;

	    for(int index = 0; index < stateMessages.size(); index++) {
	      if(stateMessages.get(index).getCommand().getCode() == RfAccountingRequest.commandCode) {
	        RfAccountingRequest msg = (RfAccountingRequest) stateMessages.get(index);

	        answer = createRfAccountingAnswer(msg);

	        if(!answer.hasSessionId() && session != null) {
	          answer.setSessionId(session.getSessionId());
	        }

	        ((DiameterMessageImpl)answer).setData(msg);
	        break;
	      }
	    }

	    return answer;
  }
  
  public void fetchSessionData(DiameterMessage msg, boolean incoming) {
	    if(msg.getHeader().isRequest()) {
	      //Well it should always be getting this on request and only once ?
	      if(incoming) {
	        //FIXME: add more ?
	        //if(this.remoteRealm == null) {
	        //  this.remoteRealm = msg.getOriginRealm();
	        //}
	        stateMessages.add((DiameterMessageImpl) msg);
	      }
	      else {
	        //FIXME, do more :)
	      }
	    }
	  }

	  private void clean(DiameterMessageImpl msg) {
	    if(msg.getData() != null) {
	      this.stateMessages.remove(msg.removeData());
	    }
	  }

  public void setSession(ServerRfSession appSession) {
    this.serverSession = appSession;
    this.serverSession.addStateChangeNotification(this);
    super.eventListener = (EventListener<Request, Answer>) appSession;
  }

  public String toString() {
    return super.toString() + "['" + this.isTerminateAfterProcessing() + "']-" + this.serverSession + "-" + super.eventListener + "-" + super.session + "-"
    + super.baseListener;
  }

  @Override
  public void endActivity() {
    this.serverSession.release();
    super.endActivity();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.RfSessionActivity#getRfMessageFactory()
   */
  public RfMessageFactory getRfMessageFactory() {
    return this.rfMessageFactory;
  }

  public void setRfMessageFactory(RfMessageFactory rfMessageFactory) {
    this.rfMessageFactory = rfMessageFactory;
  }

}
