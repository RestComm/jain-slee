package org.mobicents.slee.resource.diameter.ro;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;
import net.java.slee.resource.diameter.ro.RoMessageFactory;

import org.jdiameter.api.Session;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.cca.CreditControlMessageFactoryImpl;

/**
 * Implementation of {@link RoMessageFactory}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoMessageFactoryImpl extends CreditControlMessageFactoryImpl implements RoMessageFactory {

  public RoMessageFactoryImpl(DiameterMessageFactoryImpl baseFactory, Session session, Stack stack, CreditControlAVPFactory localFactory) {
    super(baseFactory, session, stack, localFactory);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoMessageFactory#createRoCreditControlRequest()
   */
  public CreditControlRequest createRoCreditControlRequest() {
    return super.createCreditControlRequest();
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoMessageFactory#createRoCreditControlRequest(String)
   */
  public CreditControlRequest createRoCreditControlRequest(String sessionId) {
    return this.createCreditControlRequest(sessionId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoMessageFactory#getBaseMessageFactory()
   */
  public DiameterMessageFactory getBaseMessageFactory() {
    return this.baseFactory;
  }

}
