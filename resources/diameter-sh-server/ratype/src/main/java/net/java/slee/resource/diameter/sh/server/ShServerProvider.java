package net.java.slee.resource.diameter.sh.server;

import java.io.IOException;

import net.java.slee.resource.diameter.Validator;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.events.PushNotificationAnswer;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;

/**
 * The ShServerProvider can be used by a SLEE service to provide HSS functions
 * on an IMS network.
 * 
 */
public interface ShServerProvider {

  /**
   * Provides the ServerMessageFactory
   * 
   * @return ServerMesageFactory
   */
  ShServerMessageFactory getServerMessageFactory();

  DiameterShAvpFactory getServerAvpFactory();

  /**
   * Create a new Sh server activity to send and receive Diameter Sh messages.
   * 
   * @return an ShServerSubscriptionActivity
   * @throws CreateActivityException
   *             if the RA could not create the activity for any reason
   */
  // ShServerSubscriptionActivity createShServerSubscriptionActivity() throws CreateActivityException;

  /**
   * Create a new Sh server activity to send and receive Diameter Sh messages.
   * 
   * @return an ShServerSubscriptionActivity
   * @throws CreateActivityException
   *             if the RA could not create the activity for any reason
   */
  // ShServerActivity createShServerActivity() throws CreateActivityException;

  /**
   * Sends a synchronous PushNotificationRequest which will block until an
   * answer is received from the peer.
   * 
   * @param message
   *            a PushNotificationRequest created by the ServerMessageFactory
   * @return Push-Notification-Answer message from AS
   * @throws IOException
   *             if there is an error sending the message
   */
  PushNotificationAnswer pushNotificationRequest(PushNotificationRequest message) throws IOException;

  /**
   * Return the number of peers this Diameter resource adaptor is connected
   * to.
   * 
   * @return connected peer count
   */
  int getPeerCount();

  /**
   * Returns array containing identities of connected peers FIXME: baranowb; -
   * should it be InetAddres, Port pair?
   * 
   * @return
   */
  DiameterIdentity[] getConnectedPeers();

  Validator getValidator();

}
