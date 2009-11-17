package org.mobicents.slee.resource.diameter.base.handlers;

import org.jdiameter.api.Answer;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.acc.ClientAccSession;
import org.jdiameter.api.acc.ServerAccSession;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.api.auth.ServerAuthSession;

/**
 * This should be implemented by RA. It defines some static values equal to event 
 * name part from event definition. This should be passed as arg to
 * {@link #fireEvent(String sessionId, String name, Request request, Answer answer)}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface BaseSessionCreationListener {

  /**
   * 
   * @param sessionId
   * @param appSession
   */
  public void sessionDestroyed(String sessionId, Object appSession);

  /**
   * 
   * @param session
   */
  public void sessionCreated(ServerAccSession session);

  /**
   * 
   * @param session
   */
  public void sessionCreated(ServerAuthSession session);

  /**
   * 
   * @param session
   */
  public void sessionCreated(ClientAuthSession session);

  /**
   * 
   * @param session
   */
  public void sessionCreated(ClientAccSession session);

  /**
   * 
   * @param session
   */
  public void sessionCreated(Session session);

  /**
   * 
   * @param sessionId
   * @return
   */
  public boolean sessionExists(String sessionId);

  /**
   * Makes RA fire event with certain name.
   * 
   * @param sessionId
   * @param message
   */
  public void fireEvent(String sessionId, Message message);

  /**
   * 
   * @return
   */
  public ApplicationId[] getSupportedApplications();
}
