package org.mobicents.slee.resource.diameter.sh.server.handlers;

import javax.slee.facilities.Tracer;

import net.java.slee.resource.diameter.sh.MessageFactory;

import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.Request;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.sh.ServerShSession;
import org.jdiameter.api.sh.ServerShSessionListener;
import org.jdiameter.api.sh.events.ProfileUpdateRequest;
import org.jdiameter.api.sh.events.PushNotificationAnswer;
import org.jdiameter.api.sh.events.PushNotificationRequest;
import org.jdiameter.api.sh.events.SubscribeNotificationsRequest;
import org.jdiameter.api.sh.events.UserDataRequest;
import org.jdiameter.common.api.app.IAppSessionFactory;
import org.jdiameter.common.api.app.sh.IShMessageFactory;
import org.jdiameter.common.impl.app.sh.ProfileUpdateAnswerImpl;
import org.jdiameter.common.impl.app.sh.ProfileUpdateRequestImpl;
import org.jdiameter.common.impl.app.sh.PushNotificationAnswerImpl;
import org.jdiameter.common.impl.app.sh.PushNotificationRequestImpl;
import org.jdiameter.common.impl.app.sh.SubscribeNotificationsAnswerImpl;
import org.jdiameter.common.impl.app.sh.SubscribeNotificationsRequestImpl;
import org.jdiameter.common.impl.app.sh.UserDataAnswerImpl;
import org.jdiameter.common.impl.app.sh.UserDataRequestImpl;
import org.jdiameter.server.impl.app.sh.ShServerSessionImpl;
import org.mobicents.slee.resource.diameter.sh.server.DiameterShServerResourceAdaptor;

/**
 * Start time:18:16:01 2009-01-06<br>
 * Project: mobicents-diameter-parent<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ShServerSessionFactory implements IAppSessionFactory, ServerShSessionListener, StateChangeListener, IShMessageFactory {

  protected SessionFactory sessionFactory = null;
  protected DiameterShServerResourceAdaptor ra = null;

  private Tracer tracer;

  public ShServerSessionFactory(SessionFactory sessionFactory, DiameterShServerResourceAdaptor diameterShServerResourceAdaptor) {
    super();

    this.sessionFactory = sessionFactory;
    this.ra = diameterShServerResourceAdaptor;
    
    this.tracer = ra.getRaContext().getTracer("ShServerSessionFactory");
  }

  /*
   * (non-Javadoc)
   * @see org.jdiameter.common.api.app.IAppSessionFactory#getNewSession(java.lang.String, java.lang.Class, org.jdiameter.api.ApplicationId, java.lang.Object[])
   */
  public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args)
  {
    if (aClass == ServerShSession.class) {
      ShServerSessionImpl serverSession = null;

      if (args != null && args.length > 0 && args[0] instanceof Request) {
        // This shouldnt happen but just in case
        Request request = (Request) args[0];
        serverSession = new ShServerSessionImpl(sessionId, this, sessionFactory, this);
        serverSession.addStateChangeNotification(this);

        //Notify SLEE
        this.ra.sessionCreated(serverSession, request.getCommandCode() == SubscribeNotificationsRequest.code);
      }
      else {
        throw new IllegalArgumentException("Can't create Sh-Server Session: Unknown request type.");
      }

      return serverSession;
    }
    else {
      throw new IllegalArgumentException("Wrong session class. Class[" + aClass + "]. Supported[" + ServerShSession.class + "]");
    }
  }

  //////////////////////
  // Message Handlers //
  //////////////////////

  public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShServer RA :: doOtherEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    }

    this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), answer != null ? answer.getMessage() : request.getMessage());
  }

  public void doProfileUpdateRequestEvent(ServerShSession appSession, ProfileUpdateRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShServer RA :: doProfileUpdateRequestEvent :: appSession[" + appSession + "], Request[" + request + "]");
    }

    this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), request.getMessage());
  }

  public void doPushNotificationAnswerEvent(ServerShSession appSession, PushNotificationRequest request, PushNotificationAnswer answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShServer RA :: doPushNotificationAnswerEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");
    }

    this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), answer.getMessage());
  }

  public void doSubscribeNotificationsRequestEvent(ServerShSession appSession, SubscribeNotificationsRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShServer RA :: doSubscribeNotificationsRequestEvent :: appSession[" + appSession + "], Request[" + request + "]");
    }

    this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), request.getMessage());
  }

  public void doUserDataRequestEvent(ServerShSession appSession, UserDataRequest request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    if(tracer.isFineEnabled()) {
      tracer.fine("Diameter ShServer RA :: doUserDataRequestEvent :: appSession[" + appSession + "], Request[" + request + "]");
    }

    this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), request.getMessage());
  }

  public void stateChanged(Enum oldState, Enum newState) {
    if(tracer.isInfoEnabled()) {
      tracer.info("Diameter ShServerSessionFactory :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
    }
  }

  public AppAnswerEvent createProfileUpdateAnswer(Answer answer) {
    return new ProfileUpdateAnswerImpl(answer);
  }

  public AppRequestEvent createProfileUpdateRequest(Request request) {
    return new ProfileUpdateRequestImpl(request);
  }

  public AppAnswerEvent createPushNotificationAnswer(Answer answer) {
    return new PushNotificationAnswerImpl(answer);
  }

  public AppRequestEvent createPushNotificationRequest(Request request) {
    return new PushNotificationRequestImpl(request);
  }

  public AppAnswerEvent createSubscribeNotificationsAnswer(Answer answer) {
    return new SubscribeNotificationsAnswerImpl(answer);
  }

  public AppRequestEvent createSubscribeNotificationsRequest(Request request) {
    return new SubscribeNotificationsRequestImpl(request);
  }

  public AppAnswerEvent createUserDataAnswer(Answer answer) {
    return new UserDataAnswerImpl(answer);
  }

  public AppRequestEvent createUserDataRequest(Request request) {
    return new UserDataRequestImpl(request);
  }

  public long getApplicationId() {
    return MessageFactory._SH_APP_ID;
  }

  public long getMessageTimeout() {
    return this.ra.getMessageTimeout();
  }

}
