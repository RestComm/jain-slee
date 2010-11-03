/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.diameter.rf;

import java.io.IOException;
import java.util.ArrayList;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
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
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
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

  protected transient RfMessageFactory rfMessageFactory = null;
  // protected DiameterIdentity remoteRealm;

  /**
   * Should contain requests, so we can create answer.
   */
  protected transient ArrayList<DiameterMessageImpl> stateMessages = new ArrayList<DiameterMessageImpl>();

  protected transient ServerRfSession serverSession = null;

  // FIXME: These are default values, should be overriden by stack.
  protected String originHost = "aaa://127.0.0.1:3868";
  protected String originRealm = "mobicents.org";

  // boolean destroyAfterSending = false;

  public RfServerSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ServerRfSession serverSession, DiameterIdentity destinationHost,
      DiameterIdentity destinationRealm, Stack stack) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) serverSession, destinationHost, destinationRealm);

    this.originHost = stack.getMetaData().getLocalPeer().getUri().toString();
    this.originRealm = stack.getMetaData().getLocalPeer().getRealmName();
    setSession(serverSession);
    super.setCurrentWorkingSession(this.serverSession.getSessions().get(0));
    this.rfMessageFactory = new RfMessageFactoryImpl((DiameterMessageFactoryImpl) messageFactory,serverSession.getSessionId(),  stack);
  }

  public RfAccountingAnswer createRfAccountingAnswer(RfAccountingRequest request) {
    try {
      // Get the impl
      DiameterMessageImpl implRequest = (DiameterMessageImpl) request;

      // Get raw message from impl
      Message rawMessage = implRequest.getGenericData();

      // Extract interesting AVPs
      DiameterAvp accRecordNumber = avpFactory.createAvp(Avp.ACC_RECORD_NUMBER, rawMessage.getAvps().getAvp(Avp.ACC_RECORD_NUMBER).getRaw());
      DiameterAvp accRecordType = avpFactory.createAvp(Avp.ACC_RECORD_TYPE, rawMessage.getAvps().getAvp(Avp.ACC_RECORD_TYPE).getRaw());

      DiameterAvp originHost = avpFactory.createAvp(Avp.ORIGIN_HOST, this.originHost.getBytes());
      DiameterAvp originRealm = avpFactory.createAvp(Avp.ORIGIN_REALM, this.originRealm.getBytes());

      DiameterAvp sessionId = avpFactory.createAvp(Avp.SESSION_ID, serverSession.getSessions().get(0).getSessionId());

      DiameterMessageImpl answer = (DiameterMessageImpl) messageFactory.createMessage(implRequest.getHeader(), new DiameterAvp[] { accRecordNumber, accRecordType,
        originHost, originRealm, sessionId });

      // RFC3588, Page 119-120
      // One of Acct-Application-Id and Vendor-Specific-Application-Id AVPs
      // MUST be present. If the Vendor-Specific-Application-Id grouped AVP
      // is present, it must have an Acct-Application-Id inside.

      if (request.hasAcctApplicationId()) {
        answer.addAvp(avpFactory.createAvp(Avp.ACCT_APPLICATION_ID, request.getAcctApplicationId()));
      }

      // Get the raw Answer
      Message rawAnswer = answer.getGenericData();

      // This is an answer.
      rawAnswer.setRequest(false);
      RfAccountingAnswerImpl ans = new RfAccountingAnswerImpl(rawAnswer);
      ans.setData(request);
      return ans;
    } catch (Exception e) {
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
    } catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    } catch (Exception e) {
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
      // super.state = AccountingSessionState.Idle;
      // destroyAfterSending = true;
      setTerminateAfterProcessing(true);
    } else {
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
	        answer.setAcctApplicationId(3L);

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
    super.baseListener.endActivity(getActivityHandle());
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
