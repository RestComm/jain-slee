package org.mobicents.slee.resource.diameter.ro;

import java.io.IOException;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;
import net.java.slee.resource.diameter.ro.RoClientSessionActivity;
import net.java.slee.resource.diameter.ro.RoMessageFactory;

import org.jdiameter.api.Stack;
import org.jdiameter.api.cca.ClientCCASession;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.cca.CreditControlClientSessionImpl;

/**
 * Implementation of {@link RoClientSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoClientSessionActivityImpl extends CreditControlClientSessionImpl implements RoClientSessionActivity {

  RoMessageFactory roMessageFactory = null;

  /**
   * 
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param destinationHost
   * @param destinationRealm
   * @param endpoint
   * @param stack
   */
  public RoClientSessionActivityImpl(CreditControlMessageFactory messageFactory, CreditControlAVPFactory avpFactory, ClientCCASession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint, Stack stack) {
    super(messageFactory, avpFactory, session, destinationHost, destinationRealm, endpoint);
    
    this.roMessageFactory = new RoMessageFactoryImpl((DiameterMessageFactoryImpl) messageFactory.getBaseMessageFactory(), session.getSessions().get(0), stack, avpFactory);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoClientSession#sendEventCreditControlRequest(net.java.slee.resource.diameter.cca.events.CreditControlRequest)
   */
  public void sendEventCreditControlRequest(CreditControlRequest ccr) throws IOException {
    super.sendCreditControlRequest(ccr);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoSession#getRoMessageFactory()
   */
  public RoMessageFactory getRoMessageFactory() {
    return this.roMessageFactory;
  }

}
