package org.mobicents.slee.resource.diameter.sh.server.handlers;

import org.jdiameter.api.Message;
import org.jdiameter.api.sh.ServerShSession;

/**
 * ShServerSession listener - class that is used to inform entities outise
 * AppSession factories/stack session operations has been performed.
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ShServerSessionListener {

  public void sessionDestroyed(String sessionId, ServerShSession session);

  /**
   * Listener for Server Session creation.
   */
  public void sessionCreated(ServerShSession session, boolean isSubscription);

  /**
   * Helper method to fire events to SLEE.
   * 
   * @param sessionId
   *            the id of the session for this event
   * @param message
   *            the message (request or answer) object
   */
  public void fireEvent(String sessionId, Message message);
}
