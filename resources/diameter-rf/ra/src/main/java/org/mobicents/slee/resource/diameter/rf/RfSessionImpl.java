package org.mobicents.slee.resource.diameter.rf;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rf.RfMessageFactory;
import net.java.slee.resource.diameter.rf.RfSession;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.mobicents.slee.resource.diameter.base.AccountingSessionActivityImpl;

/**
 * Implementation of {@link RfSession}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class RfSessionImpl extends AccountingSessionActivityImpl implements RfSession {

  RfMessageFactory rfMessageFactory;
  DiameterAvpFactory baseAvpFactory;

  /**
   * 
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param raEventListener
   * @param destinationHost
   * @param destinationRealm
   * @param endpoint
   */
  public RfSessionImpl(RfMessageFactory messageFactory, DiameterAvpFactory avpFactory, Session session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint) {
    super(null, null, session, raEventListener, destinationHost, destinationRealm, endpoint);

    this.rfMessageFactory = messageFactory;
    this.baseAvpFactory = avpFactory;
  }

}
