package org.mobicents.slee.resource.diameter.cxdx.handlers;

import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Message;
import org.jdiameter.api.Session;
import org.jdiameter.api.cxdx.ClientCxDxSession;
import org.jdiameter.api.cxdx.ServerCxDxSession;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface CxDxSessionCreationListener {

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
  public void sessionCreated(ServerCxDxSession session);

  /**
   * 
   * @param session
   */
  public void sessionCreated(ClientCxDxSession session);

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
   * Makes RA fire event within certain session.
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
