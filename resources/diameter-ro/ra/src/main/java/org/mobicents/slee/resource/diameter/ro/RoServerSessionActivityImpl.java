package org.mobicents.slee.resource.diameter.ro;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.cca.events.CreditControlAnswer;
import net.java.slee.resource.diameter.ro.RoMessageFactory;
import net.java.slee.resource.diameter.ro.RoServerSessionActivity;

import org.jdiameter.api.Stack;
import org.jdiameter.api.cca.ServerCCASession;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.cca.CreditControlServerSessionImpl;

/**
 * Implementation of {@link RoServerSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoServerSessionActivityImpl extends CreditControlServerSessionImpl implements RoServerSessionActivity {

  RoMessageFactory roMessageFactory = null;

  /**
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param destinationHost
   * @param destinationRealm
   * @param endpoint
   */
  public RoServerSessionActivityImpl( CreditControlMessageFactory messageFactory, CreditControlAVPFactory avpFactory, ServerCCASession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint, Stack stack ) {
    super( messageFactory, avpFactory, session, destinationHost, destinationRealm, endpoint );

    this.roMessageFactory = new RoMessageFactoryImpl((DiameterMessageFactoryImpl) messageFactory.getBaseMessageFactory(), session.getSessions().get(0), stack, avpFactory);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoServerSession#createRoCreditControlAnswer()
   */
  public CreditControlAnswer createRoCreditControlAnswer() {
    return this.createCreditControlAnswer();
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoSession#getRoMessageFactory()
   */
  public RoMessageFactory getRoMessageFactory() {
    return this.roMessageFactory;
  }

}
