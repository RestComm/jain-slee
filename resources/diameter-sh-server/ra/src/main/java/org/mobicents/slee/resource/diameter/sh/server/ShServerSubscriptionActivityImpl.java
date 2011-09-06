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
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataAnswer;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;
import net.java.slee.resource.diameter.sh.server.ShServerMessageFactory;
import net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.sh.ServerShSession;
import org.jdiameter.common.impl.app.sh.ProfileUpdateAnswerImpl;
import org.jdiameter.common.impl.app.sh.PushNotificationRequestImpl;
import org.jdiameter.common.impl.app.sh.SubscribeNotificationsAnswerImpl;
import org.jdiameter.common.impl.app.sh.UserDataAnswerImpl;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.DiameterShMessageImpl;
import org.mobicents.slee.resource.diameter.sh.events.SubscribeNotificationsRequestImpl;

/**
 * Implementation of stateful activity.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see ShServerSubscriptionActivity
 */
public class ShServerSubscriptionActivityImpl extends DiameterActivityImpl implements ShServerSubscriptionActivity, StateChangeListener<AppSession> {

  private static final long serialVersionUID = 1245108589521694221L;

  protected transient ServerShSession serverSession = null;
  protected transient DiameterShAvpFactory shAvpFactory = null;
  protected transient ShServerMessageFactoryImpl messageFactory = null;

  //FIXME: add more
  protected UserIdentityAvp userIdentity;
  protected DataReferenceType[] dataReferenceType;
  protected AuthSessionStateType authSessionState;
  protected DiameterIdentity remoteRealm;
  protected DiameterIdentity remoteHost;

  /**
   * Should contain requests, so we can create answer.
   */
  protected ArrayList<DiameterMessageImpl> stateMessages = new ArrayList<DiameterMessageImpl>();

  public ShServerSubscriptionActivityImpl(ShServerMessageFactory shServerMessageFactory, DiameterShAvpFactory diameterShAvpFactory, ServerShSession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(null, null, null, (EventListener<Request, Answer>) session, destinationHost, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(this.serverSession.getSessions().get(0));
    this.shAvpFactory = diameterShAvpFactory;
    this.messageFactory = (ShServerMessageFactoryImpl) shServerMessageFactory;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#createPushNotificationRequest()
   */
  public PushNotificationRequest createPushNotificationRequest() {
    PushNotificationRequest request = this.messageFactory.createPushNotificationRequest();

    if(!request.hasDestinationHost() && remoteHost != null) {
      request.setDestinationHost(remoteHost);
    }

    if(!request.hasDestinationRealm() && remoteRealm != null) {
      request.setDestinationRealm(remoteRealm);
    }

    if(!request.hasUserIdentity() && this.userIdentity != null) {
      request.setExtensionAvps(this.userIdentity);
    }

    if(!request.hasAuthSessionState() && this.authSessionState != null) {
      request.setAuthSessionState(this.authSessionState);
    }

    return request; 
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#createSubscribeNotificationsAnswer(long, boolean)
   */
  public SubscribeNotificationsAnswer createSubscribeNotificationsAnswer(long resultCode, boolean isExperimentalResult) {
    SubscribeNotificationsAnswer answer = null;

    for(int index = 0; index < stateMessages.size(); index++) {
      if(stateMessages.get(index).getCommand().getCode() == SubscribeNotificationsRequest.commandCode) {
        SubscribeNotificationsRequest msg = (SubscribeNotificationsRequest) stateMessages.get(index);

        answer = this.messageFactory.createSubscribeNotificationsAnswer(msg, resultCode, isExperimentalResult);

        if(answer.getAuthSessionState() == null && this.authSessionState != null) {
          answer.setAuthSessionState(this.authSessionState);
        }

        ((DiameterShMessageImpl)answer).setData(msg);
        break;
      }
    }

    //answer.setSessionId(super.session.getSessionId());
    return answer; 
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#createSubscribeNotificationsAnswer()
   */
  public SubscribeNotificationsAnswer createSubscribeNotificationsAnswer() {
    SubscribeNotificationsAnswer answer = null;

    for(int index = 0; index < stateMessages.size(); index++) {
      if(stateMessages.get(index).getCommand().getCode() == SubscribeNotificationsRequest.commandCode) {
        SubscribeNotificationsRequest msg = (SubscribeNotificationsRequest) stateMessages.get(index);

        answer = this.messageFactory.createSubscribeNotificationsAnswer(msg);
        if(answer.getAuthSessionState() == null && this.authSessionState != null) {
          answer.setAuthSessionState(this.authSessionState);
        }

        ((DiameterShMessageImpl)answer).setData(msg);
        break;
      }
    }

    //answer.setSessionId(super.session.getSessionId());
    return answer; 
  }

  public ProfileUpdateAnswer createProfileUpdateAnswer(long resultCode, boolean isExperimentalResult) {
    ProfileUpdateAnswer answer = null;

    for(int index = 0; index < stateMessages.size(); index++) {
      if(stateMessages.get(index).getCommand().getCode() == ProfileUpdateRequest.commandCode) {
        ProfileUpdateRequest msg = (ProfileUpdateRequest) stateMessages.get(index);

        answer = this.messageFactory.createProfileUpdateAnswer(msg, resultCode, isExperimentalResult);

        if(answer.getAuthSessionState() == null && this.authSessionState != null) {
          answer.setAuthSessionState(this.authSessionState);
        }

        ((DiameterShMessageImpl)answer).setData(msg);
        break;
      }
    }

    //answer.setSessionId(super.session.getSessionId());
    return answer; 
  }

  public UserDataAnswer createUserDataAnswer(byte[] userData) {
    UserDataAnswer answer = null;
    for(int index = 0; index < stateMessages.size(); index++) {
      if(stateMessages.get(index).getCommand().getCode() == UserDataRequest.commandCode) {
        UserDataRequest msg = (UserDataRequest) stateMessages.get(index);

        answer = this.messageFactory.createUserDataAnswer(msg,userData);
        if(answer.getAuthSessionState() == null && this.authSessionState != null) {
          answer.setAuthSessionState(this.authSessionState);
        }

        ((DiameterShMessageImpl)answer).setData(msg);
        break;
      }
    }

    //answer.setSessionId(super.session.getSessionId());
    return answer; 
  }

  public UserDataAnswer createUserDataAnswer(long resultCode, boolean isExperimentalResult) {
    UserDataAnswer answer = null;

    for(int index = 0; index < stateMessages.size(); index++) {
      if(stateMessages.get(index).getCommand().getCode() == UserDataRequest.commandCode) {
        UserDataRequest msg = (UserDataRequest) stateMessages.get(index);

        answer = this.messageFactory.createUserDataAnswer(msg, resultCode, isExperimentalResult);

        if(answer.getAuthSessionState() == null && this.authSessionState != null) {
          answer.setAuthSessionState(this.authSessionState);
        }

        ((DiameterShMessageImpl)answer).setData(msg);
        break;
      }
    }

    //answer.setSessionId(super.session.getSessionId());
    return answer; 
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#createUserDataAnswer()
   */
  public UserDataAnswer createUserDataAnswer() {
    UserDataAnswer answer = null;

    for(int index = 0; index < stateMessages.size(); index++) {
      if(stateMessages.get(index).getCommand().getCode() == UserDataRequest.commandCode) {
        UserDataRequest msg = (UserDataRequest) stateMessages.get(index);

        answer = this.messageFactory.createUserDataAnswer(msg);

        if(answer.getAuthSessionState() == null && this.authSessionState != null) {
          answer.setAuthSessionState(this.authSessionState);
        }

        ((DiameterShMessageImpl)answer).setData(msg);
        break;
      }
    }

    //answer.setSessionId(super.session.getSessionId());
    return answer; 
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#createProfileUpdateAnswer()
   */
  public ProfileUpdateAnswer createProfileUpdateAnswer() {
    ProfileUpdateAnswer answer = null;

    for(int index = 0; index < stateMessages.size(); index++) {
      if(stateMessages.get(index).getCommand().getCode() == ProfileUpdateRequest.commandCode) {
        ProfileUpdateRequest msg = (ProfileUpdateRequest) stateMessages.get(index);

        answer = this.messageFactory.createProfileUpdateAnswer(msg);

        if(answer.getAuthSessionState() == null && this.authSessionState != null) {
          answer.setAuthSessionState(this.authSessionState);
        }

        ((DiameterShMessageImpl)answer).setData(msg);
        break;
      }
    }

    //answer.setSessionId(super.session.getSessionId());
    return answer; 
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#sendPushNotificationRequest(net.java.slee.resource.diameter.sh.events.PushNotificationRequest)
   */
  public void sendPushNotificationRequest(PushNotificationRequest message) throws IOException {
    try {
      DiameterShMessageImpl msg = (DiameterShMessageImpl) message;
      fetchSessionData(msg, false);
      this.serverSession.sendPushNotificationRequest(new PushNotificationRequestImpl((Request) msg.getGenericData()));
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
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#sendUserDataAnswer(net.java.slee.resource.diameter.sh.events.UserDataAnswer)
   */
  public void sendUserDataAnswer(UserDataAnswer message) throws IOException {
    try {
      DiameterShMessageImpl msg = (DiameterShMessageImpl) message;
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
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#sendProfileUpdateAnswer(net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer)
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
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.ShServerSubscriptionActivity#sendSubscribeNotificationsAnswer(net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer)
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
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

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

  public void fetchSessionData(DiameterMessage msg, boolean incoming) {
    if(msg.getHeader().isRequest()) {
      // Well it should always be getting this on request and only once ?
      if(incoming) {
        //FIXME: add more ?
        if(this.remoteRealm == null) {
          this.remoteRealm = msg.getOriginRealm();
        }
        if(this.remoteHost == null) {
          this.remoteHost = msg.getOriginHost();
        }
        if(msg instanceof SubscribeNotificationsRequest) {
          SubscribeNotificationsRequestImpl msgImpl = (SubscribeNotificationsRequestImpl) msg;

          if(authSessionState == null && msgImpl.hasAuthSessionState()) {
            this.authSessionState = msgImpl.getAuthSessionState();
          }
          if(dataReferenceType == null || dataReferenceType.length == 0) {
            this.dataReferenceType = msgImpl.getDataReferences();
          }
        }

        stateMessages.add((DiameterMessageImpl) msg);
      }
      else {
        //FIXME, do more :)
      }
    }
  }

  private void clean(DiameterShMessageImpl msg) {
    if(msg.getData() != null) {
      this.stateMessages.remove(msg.removeData());
    }
  }

  @Override
  public void endActivity() {
    this.serverSession.release();
    this.serverSession.removeStateChangeNotification(this);
    super.endActivity();
  }

  // Setters & Getters ---------------------------------------

  public void setSession(ServerShSession session) {
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
