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

package org.mobicents.slee.resource.diameter.sh.server;

import java.io.IOException;
import java.util.ArrayList;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
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
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.sh.ServerShSession;
import org.jdiameter.common.impl.app.sh.ProfileUpdateAnswerImpl;
import org.jdiameter.common.impl.app.sh.SubscribeNotificationsAnswerImpl;
import org.jdiameter.common.impl.app.sh.UserDataAnswerImpl;
import org.mobicents.diameter.dictionary.AvpDictionary;
import org.mobicents.diameter.dictionary.AvpRepresentation;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.DiameterShMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.avp.UserIdentityAvpImpl;

/**
 * Implementation of stateles Sh Server activity whihc recieves. It ends after resposne is sent.
 * 
 * @author <a href = "mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href = "mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see ShServerActivity
 */
public class ShServerActivityImpl extends DiameterActivityImpl implements ShServerActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = -5297270149541413224L;

  protected transient ServerShSession serverSession = null;

  // Factories
  protected transient DiameterShAvpFactory shAvpFactory = null;
  protected transient ShServerMessageFactoryImpl messageFactory = null;

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
  protected transient ArrayList<DiameterMessage> stateMessages = new ArrayList<DiameterMessage>();

  public ShServerActivityImpl(ShServerMessageFactory shServerMessageFactory, DiameterShAvpFactory diameterShAvpFactory, ServerShSession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(null, null, null, (EventListener<Request, Answer>) session, destinationHost, destinationRealm);

    setSession(session);
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
    UserDataRequest req = (UserDataRequest) getSessionMessage(UserDataRequest.commandCode);

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
    UserDataRequest req = (UserDataRequest) getSessionMessage(UserDataRequest.commandCode);

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
    UserDataRequest req = (UserDataRequest) getSessionMessage(UserDataAnswer.commandCode);

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
    SubscribeNotificationsRequest req = (SubscribeNotificationsRequest) getSessionMessage(SubscribeNotificationsRequest.commandCode);

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
    SubscribeNotificationsRequest req = (SubscribeNotificationsRequest) getSessionMessage(SubscribeNotificationsRequest.commandCode);

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
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
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
      fetchSessionData(msg, false);
      this.serverSession.sendUserDataAnswer(new UserDataAnswerImpl((Answer) msg.getGenericData()));
      clean(msg);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
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
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  // #########################
  // # StateChangeListener
  // #########################

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Object,
   * java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(AppSession arg0, Enum oldState, Enum newState) {
    this.stateChanged(oldState, newState);

  }


  public void stateChanged(Enum oldState, Enum newState) {
    // NOP
  }

  // #########################
  // # DiameterActivityImpl
  // #########################

  public void fetchSessionData(DiameterMessage msg, boolean incoming) {
    if(msg.getHeader().isRequest()) {
      // Well it should always be getting this on request and only once ?
      if(incoming) {
        boolean changed = false;
        if(remoteRealm == null ) {
          remoteRealm = msg.getOriginRealm();
          changed = true;
        }
        if(remoteHost == null) {
          changed = true;
          remoteHost = msg.getOriginHost();
        }

        if(this.userIdentity == null) {
          changed = true;
          AvpRepresentation rep = AvpDictionary.INSTANCE.getAvp(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID);
          this.userIdentity = new UserIdentityAvpImpl(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID,rep.getRuleMandatoryAsInt(),rep.getRuleProtectedAsInt(),AvpUtilities.getAvpAsGrouped(DiameterShAvpCodes.USER_IDENTITY, DiameterShAvpCodes.SH_VENDOR_ID, ((DiameterMessageImpl)msg).getGenericData().getAvps()));
        }
        //FIXME: is this true?
        if(this.authSessionState == null) {
          changed = true;
          this.authSessionState = AuthSessionStateType.fromInt(AvpUtilities.getAvpAsInteger32(277, ((DiameterMessageImpl)msg).getGenericData().getAvps()));
        }

        stateMessages.add((DiameterMessageImpl) msg);
        if(changed) {
          super.baseListener.update(getActivityHandle(), this);
        }
      }
      else {
        //FIXME, do more :)
      }
    }
  }

  @Override
  public void endActivity() {
    this.serverSession.release();
    this.serverSession.removeStateChangeNotification(this);
    super.endActivity();
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

  //  Setters & Getters --------------------------------------------------

  public void setSession(ServerShSession session) {
    stateMessages = new ArrayList<DiameterMessage>();
    this.serverSession = session;
    this.serverSession.addStateChangeNotification(this);
  }

  @Override
  public DiameterShAvpFactory getServerAvpFactory() {
    return this.shAvpFactory;
  }

  @Override
  public ShServerMessageFactory getServerMessageFactory() {
    return this.messageFactory;
  }

  public void setServerAvpFactory(DiameterShAvpFactory shAvpFactory) {
    this.shAvpFactory = shAvpFactory;
  }

  public void setServerMessageFactory(ShServerMessageFactory messageFactory) {
    this.messageFactory = (ShServerMessageFactoryImpl) messageFactory;
  }

}
