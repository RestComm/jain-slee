package org.mobicents.slee.resource.diameter.rf;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;
import net.java.slee.resource.diameter.rf.RfMessageFactory;

import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;

/**
 * Implementation of {@link RfMessageFactory}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RfMessageFactoryImpl extends DiameterMessageFactoryImpl implements RfMessageFactory {

  private DiameterMessageFactoryImpl baseMessageFactory;

  public RfMessageFactoryImpl(DiameterMessageFactoryImpl baseMessageFactory, Stack stack) {
    super(stack);

    this.baseMessageFactory = baseMessageFactory;
  }

  public AccountingRequest createRfAccountingRequest( AccountingRecordType accountingrecordtype ) {
    AccountingRequest acr = super.createAccountingRequest();
    acr.setAcctApplicationId( _RF_ACC_APP_ID );

    acr.setAccountingRecordType( accountingrecordtype );

    return acr;
  }

  public DiameterMessageFactory getBaseMessageFactory() {
    return this.baseMessageFactory;
  }

}
