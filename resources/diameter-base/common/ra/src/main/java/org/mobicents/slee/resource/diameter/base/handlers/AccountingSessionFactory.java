package org.mobicents.slee.resource.diameter.base.handlers;

import java.util.HashMap;
import java.util.HashSet;

import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.OverloadException;
import org.jdiameter.api.Request;
import org.jdiameter.api.RouteException;
import org.jdiameter.api.SessionFactory;
import org.jdiameter.api.acc.ClientAccSession;
import org.jdiameter.api.acc.ClientAccSessionListener;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.api.acc.ServerAccSessionListener;
import org.jdiameter.api.acc.events.AccountAnswer;
import org.jdiameter.api.acc.events.AccountRequest;
import org.jdiameter.api.app.AppAnswerEvent;
import org.jdiameter.api.app.AppRequestEvent;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.client.impl.app.acc.ClientAccSessionImpl;
import org.jdiameter.common.api.app.IAppSessionFactory;
import org.jdiameter.server.impl.app.acc.ServerAccSessionImpl;

/**
 * 
 * AccountingSessionFactory.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AccountingSessionFactory implements IAppSessionFactory, ServerAccSessionListener, StateChangeListener, ClientAccSessionListener {

  private static HashSet<Integer> accEventCodes = new HashSet<Integer>();
  private static HashSet<Integer> authEventCodes = new HashSet<Integer>();

  static {
    authEventCodes.add(AbortSessionAnswer.commandCode);
    authEventCodes.add(ReAuthAnswer.commandCode);
    authEventCodes.add(SessionTerminationAnswer.commandCode);

    accEventCodes.add(AccountingAnswer.commandCode);
  }

  protected HashMap<ApplicationId, BaseSessionCreationListener> ras;
  protected long messageTimeout = 5000;
  protected SessionFactory sessionFactory = null;
  protected final static Logger logger = Logger.getLogger(AccountingSessionFactory.class);

  public static AccountingSessionFactory INSTANCE = new AccountingSessionFactory();

  /*
   * public AccountingSessionFactory(BaseSessionCreationListener ra, long
   * messageTimeout, SessionFactory sessionFactory, ApplicationId appId) {
   * super(); this.ras.put(appId, ra); this.messageTimeout = messageTimeout;
   * this.sessionFactory = sessionFactory; }
   */

  private AccountingSessionFactory() {
    this.ras = new HashMap<ApplicationId, BaseSessionCreationListener>();
  }

  public void registerListener(BaseSessionCreationListener ra, long messageTimeout, SessionFactory sessionFactory) {
    for (ApplicationId appId : ra.getSupportedApplications()) {
      this.ras.put(appId, ra);
    }
    this.messageTimeout = messageTimeout;
    this.sessionFactory = sessionFactory;
  }

  public AppSession getNewSession(String sessionId, Class<? extends AppSession> aClass, ApplicationId applicationId, Object[] args) {
    try {
      if (aClass == ServerAccSession.class) {
        Request request = (Request) args[0];

        ServerAccSessionImpl session = new ServerAccSessionImpl(sessionFactory.getNewSession(request.getSessionId()), sessionFactory, request, this, messageTimeout, true, new StateChangeListener[] { this });
        BaseSessionCreationListener ra = this.ras.get(applicationId) != null ? this.ras.get(applicationId) : this.ras.values().iterator().next();
        ra.sessionCreated(session);
        return session;
      }
      else if (aClass == ClientAccSession.class) {
        ClientAccSessionImpl session = sessionId == null ? new ClientAccSessionImpl(sessionFactory, this, applicationId) : new ClientAccSessionImpl(sessionFactory, sessionId, this, applicationId);
        session.addStateChangeNotification(this);
        BaseSessionCreationListener ra = this.ras.get(applicationId) != null ? this.ras.get(applicationId) : this.ras.values().iterator().next();
        ra.sessionCreated(session);
        return session;
      }
    }
    catch (Exception e) {
      logger.error("Failure to obtain new Accounting Session.", e);
    }

    return null;
  }

  public void doAccRequestEvent(ServerAccSession appSession, AccountRequest acr) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AccountingSessionFactory :: doAccRequestEvent :: appSession[" + appSession + "], Request[" + acr + "]");

    doFireEvent(appSession, acr.getMessage());
  }

  public void doAccAnswerEvent(ClientAccSession appSession, AccountRequest acr, AccountAnswer aca) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("doAccAnswerEvent :: appSession[" + appSession + "], Request[" + acr + "], Answer[" + aca + "]");

    doFireEvent(appSession, aca.getMessage());
  }

  public void doOtherEvent(AppSession appSession, AppRequestEvent request, AppAnswerEvent answer) throws InternalException, IllegalDiameterStateException, RouteException, OverloadException {
    logger.info("Diameter Base AccountingSessionFactory :: doOtherEvent :: appSession[" + appSession + "], Request[" + request + "], Answer[" + answer + "]");

    doFireEvent(appSession, request != null ? request.getMessage() : answer.getMessage());
  }

  public void stateChanged(Enum oldState, Enum newState) {
    logger.info("Diameter Base AccountingSessionFactory :: stateChanged :: oldState[" + oldState + "], newState[" + newState + "]");
    //FIXME: add code here.
  }

  private void doFireEvent(AppSession appSession, Message message) {
    ApplicationId appId = null;
    for(ApplicationId curAppId : message.getApplicationIdAvps()) {
      if(curAppId.getAcctAppId() != ApplicationId.UNDEFINED_VALUE && this.ras.containsKey(curAppId)) {
        appId = curAppId;
        break;
      }
    }

    BaseSessionCreationListener ra = appId != null ? this.ras.get(appId) : this.ras.values().iterator().next();

    if(ra != null) {
      ra.fireEvent(appSession.getSessions().get(0).getSessionId(), message);
    }
    else {
      // TODO: tracer
    }
  }
}
