package org.mobicents.slee.resource.diameter.cca.handlers;

import javax.slee.resource.ActivityHandle;

import org.jdiameter.api.Answer;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.cca.ClientCCASession;
import org.jdiameter.api.cca.ServerCCASession;

/**
 * 
 * CCASessionCreationListener.java
 * 
 * <br>
 * Super project: mobicents <br>
 * 11:18:42 AM Dec 30, 2008 <br>
 * This should be implemented by RA. It defines some static values equal to
 * event name part from event definition. This should be passed as arg to
 * {@link #fireEvent(String sessionId, String name, Request request, Answer answer)}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface CCASessionCreationListener {

  /**
   * Listener for Client Session creation.
   * 
   * @param ccClientSession
   *            the newly created Client Session
   */
  public void sessionCreated(ClientCCASession ccClientSession);

  /**
   * Listener for Server Session creation.
   * 
   * @param ccServerSession
   *            the newly created Client Session
   */
  public void sessionCreated(ServerCCASession ccServerSession);

  /**
   * Method for verifying if some session (with a given Session-Id) exists.
   * 
   * @param sessionId
   *            the Id to verify
   * @return false if there's no session with the id, true otherwise
   */
  public boolean sessionExists(String sessionId);

  /**
   * Listener for Server Session destruction.
   * 
   * @param sessionId
   *            the id of the session destroyed
   * @param appSession
   *            the session object itself
   */
  public void sessionDestroyed(String sessionId, Object appSession);

  /**
   * Helper method to fire events to SLEE.
   * 
   * @param sessionId
   *            the id of the session for this event
   * @param message
   *            the message object
   */
  public void fireEvent(String sessionId, Message message);

}
