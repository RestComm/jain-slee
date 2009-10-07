package org.mobicents.slee.resource.diameter.base;

import java.io.IOException;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.AuthClientSessionActivity;
import net.java.slee.resource.diameter.base.AuthSessionState;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.common.api.app.auth.ClientAuthSessionState;
import org.jdiameter.common.impl.app.auth.AbortSessionAnswerImpl;
import org.jdiameter.common.impl.app.auth.ReAuthAnswerImpl;
import org.jdiameter.common.impl.app.auth.SessionTermRequestImpl;
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * Implementation of {@link AuthClientSessionActivity}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AuthClientSessionActivityImpl extends AuthSessionActivityImpl implements AuthClientSessionActivity {

  protected ClientAuthSession clientSession = null;

  public AuthClientSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ClientAuthSession clientSession, DiameterIdentity destinationHost, DiameterIdentity destinationRealm,SleeEndpoint endpoint)
  {
    super(messageFactory, avpFactory, null, (EventListener<Request, Answer>) clientSession, destinationHost, destinationRealm,endpoint);

    this.clientSession = clientSession;
    //this.clientSession.addStateChangeNotification(this);
    super.setCurrentWorkingSession(clientSession.getSessions().get(0));
  }

  public AbortSessionAnswer createAbortSessionAnswer() {
    // Create the request. // TODO Use request used to create this activity ?
    AbortSessionRequest asr = messageFactory.createAbortSessionRequest();
    asr.setAuthApplicationId(0L);

    return createAbortSessionAnswer(asr);
  }

  public AbortSessionAnswer createAbortSessionAnswer(AbortSessionRequest abortSessionRequest) {
    return messageFactory.createAbortSessionAnswer(abortSessionRequest);
  }

  public void sendAbortSessionAnswer(AbortSessionAnswer answer) throws IOException {
    try {
      // super.sendMessage(answer);
      DiameterMessageImpl asa = (DiameterMessageImpl) answer;
      this.clientSession.sendAbortSessionAnswer(new AbortSessionAnswerImpl((Answer) asa.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public ReAuthAnswer createReAuthAnswer() {
    // Create the request. // TODO Use request used to create this activity ?
    ReAuthRequest rar = messageFactory.createReAuthRequest();
    rar.setAuthApplicationId(0L);

    return createReAuthAnswer(rar);
  }

  public ReAuthAnswer createReAuthAnswer(ReAuthRequest reAuthRequest) {
    return messageFactory.createReAuthAnswer(reAuthRequest);
  }

  public void sendReAuthAnswer(ReAuthAnswer answer) throws IOException {
    try {
      //super.sendMessage(answer);
      DiameterMessageImpl msg = (DiameterMessageImpl) answer;
      this.clientSession.sendReAuthAnswer(new ReAuthAnswerImpl(msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public SessionTerminationRequest createSessionTerminationRequest(TerminationCauseType terminationCause) {
    SessionTerminationRequest str = messageFactory.createSessionTerminationRequest();

    // Set Auth-Application-Id to 0 as specified
    str.setAuthApplicationId(0L);
    str.setTerminationCause(terminationCause);

    return str;
  }

  public void sendSessionTerminationRequest(SessionTerminationRequest request) throws IOException {
    try {
      //super.sendMessage(request);
      DiameterMessageImpl msg = (DiameterMessageImpl) request;
      this.clientSession.sendSessionTerminationRequest(new SessionTermRequestImpl(msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getMessage());
    }
  }

  public void stateChanged(Enum oldState, Enum newState) {

    ClientAuthSessionState state=(ClientAuthSessionState) newState;
    switch(state)
    {
      case IDLE:
        super.state=AuthSessionState.Idle;
        break;
      case OPEN:
        super.state=AuthSessionState.Open;
        break;
      case  PENDING:
        super.state=AuthSessionState.Pending;
        break;
      case DISCONNECTED:
        super.state = AuthSessionState.Disconnected;
        String sessionId = this.clientSession.getSessions().get(0).getSessionId();
        this.clientSession.release();
        this.baseListener.sessionDestroyed(sessionId, this.clientSession);
        break;
    }
  }

  public ClientAuthSession getSession() {
    return this.clientSession;
  }

}
