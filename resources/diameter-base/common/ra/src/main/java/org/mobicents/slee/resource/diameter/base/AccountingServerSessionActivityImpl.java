package org.mobicents.slee.resource.diameter.base;

import java.io.IOException;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.AccountingServerSessionActivity;
import net.java.slee.resource.diameter.base.AccountingSessionState;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;

import org.jdiameter.api.Answer;
import org.jdiameter.api.Avp;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.common.api.app.acc.ServerAccSessionState;
import org.jdiameter.common.impl.app.acc.AccountAnswerImpl;
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.mobicents.slee.resource.diameter.base.events.AccountingAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * Implementation of {@link AccountingServerSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AccountingServerSessionActivityImpl extends AccountingSessionActivityImpl implements AccountingServerSessionActivity {

  protected ServerAccSession serverSession = null;

  // These are default values, should be overriden by stack.
  protected String originHost = "aaa://127.0.0.1:3868";
  protected String originRealm = "mobicents.org";

  boolean destroyAfterSending = false;

  public AccountingServerSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ServerAccSession serverSession, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint, Stack stack) {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) serverSession, destinationHost, destinationRealm, endpoint);

    this.serverSession = serverSession;

    this.state = AccountingSessionState.Idle;
    this.originHost = stack.getMetaData().getLocalPeer().getUri().toString();
    this.originRealm = stack.getMetaData().getLocalPeer().getRealmName();

    super.setCurrentWorkingSession(this.serverSession.getSessions().get(0));
  }

  public AccountingAnswer createAccountingAnswer(AccountingRequest request) {
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

      DiameterMessageImpl answer = (DiameterMessageImpl) messageFactory.createMessage(implRequest.getHeader(), new DiameterAvp[] { accRecordNumber, accRecordType, originHost, originRealm, sessionId });

      // RFC3588, Page 119-120
      // One of Acct-Application-Id and Vendor-Specific-Application-Id AVPs
      // MUST be present. If the Vendor-Specific-Application-Id grouped AVP
      // is present, it must have an Acct-Application-Id inside.

      if (request.hasAcctApplicationId()) {
        answer.addAvp(avpFactory.createAvp(Avp.ACCT_APPLICATION_ID, request.getAcctApplicationId()));
      }
      else {
        // We should have an Vendor-Specific-Application-Id grouped AVP
        answer.addAvp(request.getVendorSpecificApplicationId());
      }

      // Get the raw Answer
      Message rawAnswer = answer.getGenericData();

      // This is an answer.
      rawAnswer.setRequest(false);

      return new AccountingAnswerImpl(rawAnswer);
    }
    catch (Exception e) {
      logger.error("", e);
    }

    return null;
  }

  public AccountingAnswer createAccountAnswer(AccountingRequest request, int resultCode) {
    AccountingAnswer answer = this.createAccountingAnswer(request);

    answer.setResultCode(resultCode);

    return answer;
  }

  public void sendAccountingAnswer(AccountingAnswer answer) throws IOException {

    try {
      AccountingAnswerImpl aca = (AccountingAnswerImpl) answer;

      this.serverSession.sendAccountAnswer(new AccountAnswerImpl((Answer) aca.getGenericData()));

      // FIXME: check this?
      if (destroyAfterSending) {
        String sessionId = this.serverSession.getSessions().get(0).getSessionId();
        this.serverSession.release();
        this.baseListener.sessionDestroyed(sessionId, this.serverSession);
      }
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public ServerAccSession getSession() {
    return this.serverSession;
  }

  public void stateChanged(Enum oldState, Enum newState) {

    if (newState == ServerAccSessionState.IDLE) {
      super.state = AccountingSessionState.Idle;
      destroyAfterSending = true;
    }
    else {
      super.state = AccountingSessionState.Open;
    }
  }

  public AccountingAnswer createAccountingAnswer() {
    return null;
  }

}
