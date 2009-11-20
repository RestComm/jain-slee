package org.mobicents.slee.resource.diameter.sh.server;

import java.io.IOException;
import java.util.ArrayList;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.ShSessionState;
import net.java.slee.resource.diameter.sh.events.DiameterShMessage;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataAnswer;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;
import net.java.slee.resource.diameter.sh.server.ShServerActivity;
import net.java.slee.resource.diameter.sh.server.ShServerMessageFactory;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.sh.ServerShSession;
import org.jdiameter.common.impl.app.sh.ProfileUpdateAnswerImpl;
import org.jdiameter.common.impl.app.sh.SubscribeNotificationsAnswerImpl;
import org.jdiameter.common.impl.app.sh.UserDataAnswerImpl;
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.mobicents.diameter.dictionary.AvpDictionary;
import org.mobicents.diameter.dictionary.AvpRepresentation;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.DiameterShMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.UserIdentityAvpImpl;
import org.mobicents.slee.resource.diameter.sh.server.handlers.ShServerSessionListener;

/**
 * Implementation of stateles Sh Server activity whihc recieves. It ends after resposne is sent.
 * 
 * @author <a href = "mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href = "mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see ShServerActivity
 */
public class ShServerActivityImpl extends DiameterActivityImpl implements ShServerActivity, StateChangeListener {

  protected ServerShSession serverSession = null;
  protected ShSessionState state = ShSessionState.NOTSUBSCRIBED;
  protected ShServerSessionListener listener = null;

  // Factories
  protected DiameterShAvpFactory shAvpFactory = null;
  protected ShServerMessageFactoryImpl messageFactory = null;

  //FIXME: add more
  protected UserIdentityAvp userIdentity;
  protected DataReferenceType[] dataReferenceType;
  protected AuthSessionStateType authSessionState;
  protected DiameterIdentity remoteRealm;
  protected DiameterIdentity remoteHost;

  // THIS IS BAD, we need to come up with something.
  /**
   * Should contain requests, so we can create answer.
   */
  protected ArrayList<DiameterMessage> stateMessages = new ArrayList<DiameterMessage>();

  public ShServerActivityImpl(ShServerMessageFactory shServerMessageFactory, DiameterShAvpFactory diameterShAvpFactory, ServerShSession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint) {
    super(null, null, null, (EventListener<Request, Answer>) session, destinationHost, destinationRealm, endpoint);

    this.serverSession = session;
    this.serverSession.addStateChangeNotification(this);
    super.setCurrentWorkingSession(this.serverSession.getSessions().get(0));
    this.shAvpFactory = diameterShAvpFactory;
    this.messageFactory = (ShServerMessageFactoryImpl) shServerMessageFactory;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#createProfileUpdateAnswer(long, boolean)
   */
  public ProfileUpdateAnswer createProfileUpdateAnswer(long resultCode, boolean isExperimentalResult) {
    // Fetch the session stored request
    ProfileUpdateRequest req = (ProfileUpdateRequest) getSessionMessage(ProfileUpdateRequest.commandCode);

    ProfileUpdateAnswer answer = null;

    if(req != null) {
      // Create answer from it
      answer = this.messageFactory.createProfileUpdateAnswer(req, resultCode, isExperimentalResult);

      // Add any extra session data
      addSessionData(answer);

      // Store request data FIXME: Commented by Alex. Is this needed?
      // ((DiameterShMessageImpl)answer).setData(req);
    }

    return answer;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#createProfileUpdateAnswer()
   */
  public ProfileUpdateAnswer createProfileUpdateAnswer() {
    // Fetch the session stored request
    ProfileUpdateRequest req = (ProfileUpdateRequest) getSessionMessage(ProfileUpdateRequest.commandCode);

    ProfileUpdateAnswer answer = null;

    if(req != null) {
      // Create answer from it
      answer = this.messageFactory.createProfileUpdateAnswer(req);

      // Add any extra session data
      addSessionData(answer);

      // Store request data FIXME: Commented by Alex. Is this needed?
      // ((DiameterShMessageImpl)answer).setData(req);
    }

    return answer;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#createUserDataAnswer(byte[])
   */
  public UserDataAnswer createUserDataAnswer(byte[] userData) {
    // Fetch the session stored request
    UserDataRequest req = (UserDataRequest) getSessionMessage(ProfileUpdateRequest.commandCode);

    UserDataAnswer answer = null;

    if(req != null) {
      // Create answer from it
      answer = this.messageFactory.createUserDataAnswer(req, userData);

      // Add any extra session data
      addSessionData(answer);

      // Store request data FIXME: Commented by Alex. Is this needed?
      // ((DiameterShMessageImpl)answer).setData(req);
    }

    return answer;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#createUserDataAnswer(long, boolean)
   */
  public UserDataAnswer createUserDataAnswer(long resultCode, boolean isExperimentalResult) {
    // Fetch the session stored request
    UserDataRequest req = (UserDataRequest) getSessionMessage(ProfileUpdateRequest.commandCode);

    UserDataAnswer answer = null;

    if(req != null) {
      // Create answer from it
      answer = this.messageFactory.createUserDataAnswer(req, resultCode, isExperimentalResult);

      // Add any extra session data
      addSessionData(answer);

      // Store request data FIXME: Commented by Alex. Is this needed?
      // ((DiameterShMessageImpl)answer).setData(req);
    }

    return answer;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#createUserDataAnswer()
   */
  public UserDataAnswer createUserDataAnswer() {
    // Fetch the session stored request
    UserDataRequest req = (UserDataRequest) getSessionMessage(ProfileUpdateRequest.commandCode);

    UserDataAnswer answer = null;

    if(req != null) {
      // Create answer from it
      answer = this.messageFactory.createUserDataAnswer(req);

      // Add any extra session data
      addSessionData(answer);

      // Store request data FIXME: Commented by Alex. Is this needed?
      // ((DiameterShMessageImpl)answer).setData(req);
    }

    return answer;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#createSubscribeNotificationsAnswer(long, boolean)
   */
  public SubscribeNotificationsAnswer createSubscribeNotificationsAnswer(long resultCode, boolean isExperimentalResult) {
    // Fetch the session stored request
    SubscribeNotificationsRequest req = (SubscribeNotificationsRequest) getSessionMessage(ProfileUpdateRequest.commandCode);

    SubscribeNotificationsAnswer answer = null;

    if(req != null) {
      // Create answer from it
      answer = this.messageFactory.createSubscribeNotificationsAnswer(req, resultCode, isExperimentalResult);

      // Add any extra session data
      addSessionData(answer);

      // Store request data FIXME: Commented by Alex. Is this needed?
      // ((DiameterShMessageImpl)answer).setData(req);
    }

    return answer;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#createSubscribeNotificationsAnswer()
   */
  public SubscribeNotificationsAnswer createSubscribeNotificationsAnswer() {
    // Fetch the session stored request
    SubscribeNotificationsRequest req = (SubscribeNotificationsRequest) getSessionMessage(ProfileUpdateRequest.commandCode);

    SubscribeNotificationsAnswer answer = null;

    if(req != null) {
      // Create answer from it
      answer = this.messageFactory.createSubscribeNotificationsAnswer(req);

      // Add any extra session data
      addSessionData(answer);

      // Store request data FIXME: Commented by Alex. Is this needed?
      // ((DiameterShMessageImpl)answer).setData(req);
    }

    return answer;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#sendSubscribeNotificationsAnswer(net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer)
   */
  public void sendSubscribeNotificationsAnswer(SubscribeNotificationsAnswer message) throws IOException {
    try {
      DiameterShMessageImpl msg = (DiameterShMessageImpl) message;
      fetchSessionData(msg, false);
      this.serverSession.sendSubscribeNotificationsAnswer(new SubscribeNotificationsAnswerImpl((Answer) msg.getGenericData()));
      clean(msg);
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#sendUserDataAnswer(net.java.slee.resource.diameter.sh.events.UserDataAnswer)
   */
  public void sendUserDataAnswer(UserDataAnswer message) throws IOException {
    try {
      DiameterShMessageImpl msg = (DiameterShMessageImpl) message;
      this.serverSession.sendUserDataAnswer(new UserDataAnswerImpl((Answer) msg.getGenericData()));
      clean(msg);
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerActivity#sendProfileUpdateAnswer(net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer)
   */
  public void sendProfileUpdateAnswer(ProfileUpdateAnswer message) throws IOException {
    try {
      DiameterShMessageImpl msg = (DiameterShMessageImpl) message;
      fetchSessionData(msg, false);
      this.serverSession.sendProfileUpdateAnswer(new ProfileUpdateAnswerImpl((Answer) msg.getGenericData()));
      clean(msg);
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  // #########################
  // # StateChangeListener
  // #########################

  public void stateChanged(Enum oldState, Enum newState) {
    org.jdiameter.common.api.app.sh.ShSessionState _state = (org.jdiameter.common.api.app.sh.ShSessionState) newState;

    switch(_state)
    {
    case NOTSUBSCRIBED:
      break;
    case SUBSCRIBED:
      //FIXME: error?
      //This should not happen!!!
      break;
    case TERMINATED:
      state = ShSessionState.TERMINATED;
      this.listener.sessionDestroyed(getSessionId(), serverSession);
      this.serverSession.removeStateChangeNotification(this);
      this.messageFactory.clean();
      this.messageFactory = null;
      this.serverSession = null;
      super.session =  null;

      this.shAvpFactory = null;

      this.stateMessages.clear();
      this.listener = null;
      super.clean();
      break;
    }
  }

  // #########################
  // # DiameterActivityImpl
  // #########################

  @Override
  public Object getSessionListener() {
    return this.listener;
  }

  @Override
  public void setSessionListener(Object ra) {
    this.listener = (ShServerSessionListener) ra;
  }

  public void fetchSessionData(DiameterMessage msg, boolean incoming) {
    if(msg.getHeader().isRequest()) {
      // Well it should always be getting this on request and only once ?
      if(incoming) {
        if(remoteRealm == null ) {
          remoteRealm = msg.getOriginRealm();
        }
        if(remoteHost == null) {
          remoteHost = msg.getOriginHost();
        }

        if(this.userIdentity == null) {
          AvpRepresentation rep = AvpDictionary.INSTANCE.getAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID);
          this.userIdentity = new UserIdentityAvpImpl(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID,rep.getRuleMandatoryAsInt(),rep.getRuleProtectedAsInt(),AvpUtilities.getAvpAsGrouped(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, ((DiameterMessageImpl)msg).getGenericData().getAvps()));
        }

        if(this.authSessionState == null) {
          this.authSessionState = AuthSessionStateType.fromInt(AvpUtilities.getAvpAsInteger32(277, ((DiameterMessageImpl)msg).getGenericData().getAvps()));
        }

        stateMessages.add((DiameterMessageImpl) msg);
      }
      else {
        //FIXME, do more :)
      }
    }
  }

  // Aux Methods ---------------------------------------------------------

  private void clean(DiameterShMessageImpl msg) {
    if(msg.getData() != null) {
      this.stateMessages.remove(msg.removeData());
    }
  }

  private DiameterMessage getSessionMessage(int code) {
    for(int index = 0; index < stateMessages.size(); index++) {
      DiameterMessage msg = stateMessages.get(index);

      if(msg.getCommand().getCode() == code) {
        return msg;
      }
    }

    return null;
  }

  /**
   * Adds current session data to answer, if needed and present.
   * 
   * @param shMessage the message to be injected with data
   */
  private void addSessionData(DiameterShMessage shMessage) {
    if(shMessage.getAuthSessionState() == null && this.authSessionState != null) {
      shMessage.setAuthSessionState(this.authSessionState);
    }
  }

}
