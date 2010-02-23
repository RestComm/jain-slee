package org.mobicents.slee.resource.diameter.base.handlers;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.Request;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.api.auth.ClientAuthSessionListener;
import org.jdiameter.api.auth.ServerAuthSession;
import org.jdiameter.api.auth.ServerAuthSessionListener;
import org.jdiameter.api.auth.events.AbortSessionAnswer;
import org.jdiameter.api.auth.events.AbortSessionRequest;
import org.jdiameter.api.auth.events.ReAuthRequest;
import org.jdiameter.api.auth.events.SessionTermAnswer;
import org.jdiameter.api.auth.events.SessionTermRequest;
import org.jdiameter.client.impl.app.auth.ClientAuthSessionImpl;
import org.jdiameter.common.api.app.IAppSessionFactory;
import org.jdiameter.common.api.app.auth.IAuthMessageFactory;
import org.jdiameter.common.impl.app.AppAnswerEventImpl;
import org.jdiameter.common.impl.app.AppRequestEventImpl;
import org.jdiameter.server.impl.app.auth.ServerAuthSessionImpl;

/**
 * 
 * AuthorizationSessionFactory.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AuthorizationSessionFactory implements IAppSessionFactory, IAuthMessageFactory, ServerAuthSessionListener, StateChangeListener, ClientAuthSessionListener {

  private long authAppId = 19301L;

  protected BaseSessionCreationListener ra;
  protected long messageTimeout = 5000;
  protected SessionFactory sessionFactory = null;
  protected final static Logger logger = Logger.getLogger(AccountingSessionFactory.class);

  private boolean stateless = true;

  public AuthorizationSessionFactory(BaseSessionCreationListener ra, long messageTimeout, SessionFactory sessionFactory) {
    super();
    this.ra = ra;
    this.messageTimeout = messageTimeout;
    this.sessionFactory = sessionFactory;
  }

  public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args) {
    try {
      if (aClass == ServerAuthSession.class) {
        Request request = (Request) args[0];

        ServerAuthSessionImpl session = new ServerAuthSessionImpl(sessionFactory.getNewSession(request.getSessionId()), sessionFactory, request, this, this, messageTimeout, stateless, this);
        this.ra.sessionCreated(session);

        return session;
      }
      else {
        if (aClass == ClientAuthSession.class) {
          ClientAuthSessionImpl session = sessionId == null ? new ClientAuthSessionImpl(stateless, this, sessionFactory, this) : new ClientAuthSessionImpl(stateless, sessionId, this, sessionFactory, this);
          session.addStateChangeNotification(this);
          this.ra.sessionCreated(session);
          return session;
        }
      }
    }
    catch (Exception e) {
      logger.error("", e);
    }

    return null;
  }

  public void stateChanged(Enum oldState, Enum newState) {
    logger.info("Diameter Base AuthorizationSessionFactory :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
  }

  public void doAbortSessionRequestEvent(ClientAuthSession appSession, AbortSessionRequest asr) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doAbortSessionRequestEvent :: appSession[" + appSession + "], ASR[" + asr + "]");

    doFireEvent(appSession, asr.getMessage());
  }

  public void doAbortSessionAnswerEvent(ServerAuthSession appSession, AbortSessionAnswer asa) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doAbortSessionAnswerEvent :: appSession[" + appSession + "], ASA[" + asa + "]");

    doFireEvent(appSession, asa.getMessage());
  }

  public void doSessionTerminationRequestEvent(ServerAuthSession appSession, SessionTermRequest str) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doSessionTerminationRequestEvent :: appSession[" + appSession + "], STA[" + str + "]");

    doFireEvent(appSession, str.getMessage());
  }

  public void doSessionTerminationAnswerEvent(ClientAuthSession appSession, SessionTermAnswer sta) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doSessionTerminationAnswerEvent :: appSession[" + appSession + "], STA[" + sta + "]");

    doFireEvent(appSession, sta.getMessage());
  }

  public void doAuthRequestEvent(ServerAuthSession appSession, AppRequestEvent request) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doAuthRequestEvent :: appSession[" + appSession + "], Request[" + request + "]");

    doFireEvent(appSession, request.getMessage());
  }

  public void doAuthAnswerEvent(ClientAuthSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doAuthAnswerEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");

    doFireEvent(appSession, answer.getMessage());
  }

  public void doReAuthRequestEvent(ClientAuthSession appSession, ReAuthRequest rar) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doReAuthRequestEvent :: appSession[" + appSession + "], RAR[" + rar + "]");

    doFireEvent(appSession, rar.getMessage());
  }

  public void doReAuthAnswerEvent(ServerAuthSession appSession, ReAuthRequest rar, org.jdiameter.api.auth.events.ReAuthAnswer raa) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doReAuthAnswerEvent :: appSession[" + appSession + "], RAR[" + rar + "], RAA[" + raa + "]");

    doFireEvent(appSession, raa.getMessage());
  }

  public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AuthorizationSessionFactory :: doOtherEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");

    doFireEvent(appSession, request != null ? request.getMessage() : answer.getMessage());
  }

  public AppAnswerEvent createAuthAnswer(Answer answer) {
    return new AppAnswerEventImpl(answer);
  }

  public AppRequestEvent createAuthRequest(Request request) {
    return new AppRequestEventImpl(request);
  }

  public ApplicationId getApplicationId() {
    return ApplicationId.createByAuthAppId(authAppId);
  }

  public int getAuthMessageCommandCode() {
    // FIXME: alexandre: what to use here?
    return 0;
  }

  public void setStateless(boolean stateless) {
    this.stateless = stateless;
  }

  public boolean getStateless() {
    return this.stateless;
  }

  private void doFireEvent(AppSession appSession, Message message) {
    this.ra.fireEvent(appSession.getSessions().get(0).getSessionId(), message);
  }
}
