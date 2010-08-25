package org.mobicents.slee.resource.diameter.rf;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rf.RfClientSession;
import net.java.slee.resource.diameter.rf.RfMessageFactory;

import org.jdiameter.api.Stack;
import org.jdiameter.api.acc.ClientAccSession;
import org.mobicents.slee.resource.diameter.base.AccountingClientSessionActivityImpl;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;

/**
 * Implementation of {@link RfClientSession}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RfClientSessionImpl extends AccountingClientSessionActivityImpl implements RfClientSession {

  private static final long serialVersionUID = -896041231173969408L;

  protected transient RfMessageFactory rfMessageFactory = null;

  /**
   * 
   * @param messageFactory
   * @param avpFactory
   * @param clientSession
   * @param destinationHost
   * @param destinationRealm
   * @param endpoint
   * @param stack
   */
  public RfClientSessionImpl(DiameterMessageFactoryImpl messageFactory, DiameterAvpFactoryImpl avpFactory, ClientAccSession clientSession, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, Stack stack) {
    super(messageFactory, avpFactory, clientSession, destinationHost, destinationRealm);

    this.rfMessageFactory = new RfMessageFactoryImpl(messageFactory, stack);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.RfClientSession#sendAccountingRequest(net.java.slee.resource.diameter.base.events.AccountingRequest)
   */
  public void sendAccountingRequest(AccountingRequest accountingRequest) throws IOException, IllegalArgumentException {
    super.sendAccountRequest(accountingRequest);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.RfSession#getRfMessageFactory()
   */
  public RfMessageFactory getRfMessageFactory() {
    return this.rfMessageFactory;
  }

  public void setRfMessageFactory(RfMessageFactory rfMessageFactory) {
    this.rfMessageFactory = rfMessageFactory;
  }

}
