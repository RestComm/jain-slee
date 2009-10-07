package org.mobicents.slee.resource.diameter.base;

import java.io.IOException;

import javax.slee.resource.SleeEndpoint;

import net.java.slee.resource.diameter.base.AccountingClientSessionActivity;
import net.java.slee.resource.diameter.base.AccountingSessionState;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.acc.ClientAccSession;
import org.jdiameter.common.api.app.acc.ClientAccSessionState;
import org.jdiameter.common.impl.app.acc.AccountRequestImpl;
import org.jdiameter.common.impl.validation.JAvpNotAllowedException;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * Implementation of {@link AccountingClientSessionActivity}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class AccountingClientSessionActivityImpl extends AccountingSessionActivityImpl implements AccountingClientSessionActivity {

  protected ClientAccSession clientSession = null;

  public AccountingClientSessionActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, ClientAccSession clientSession, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, SleeEndpoint endpoint) {
    super(messageFactory, avpFactory, null,(EventListener<Request, Answer>) clientSession, destinationHost, destinationRealm, endpoint);

    this.clientSession = clientSession;
    this.clientSession.addStateChangeNotification(this);
    this.state = AccountingSessionState.Idle;

    super.setCurrentWorkingSession(this.clientSession.getSessions().get(0));
  }

  public AccountingRequest createAccountingRequest(AccountingRecordType accountingRecordType) {
    AccountingRequest acr = messageFactory.createAccountingRequest();

    // Set Acct-Application-Id to 3 as specified
    acr.setAcctApplicationId(3L);
    acr.setAccountingRecordType(accountingRecordType);

    return acr;
  }

  public void sendAccountRequest(AccountingRequest request) throws IOException {
    // FIXME: baranowb - add here magic to pick up and set in super correct Session in case of Relly agent
    DiameterMessageImpl msg = (DiameterMessageImpl) request;
    try {
      this.clientSession.sendAccountRequest(new AccountRequestImpl((Request) msg.getGenericData()));
    }
    catch (JAvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public void stateChanged(Enum oldState, Enum newState) {

    ClientAccSessionState state = (ClientAccSessionState) newState;

    //FIXME: baranowb: PendingL - where does this fit?
    switch (state) {
    case IDLE:
      if(oldState != state)
      {
        String sessionId = this.clientSession.getSessions().get(0).getSessionId();
        this.state = AccountingSessionState.Idle;
        //this.clientSession.release();
        this.baseListener.sessionDestroyed(sessionId, this.clientSession);
      }
      break;
    case OPEN:
      this.state = AccountingSessionState.Open;
      break;
    case PENDING_EVENT:
      this.state = AccountingSessionState.PendingE;
      break;
    case PENDING_START:
      this.state = AccountingSessionState.PendingS;
      break;
    case PENDING_INTERIM:
      this.state = AccountingSessionState.PendingI;
      break;
    case PENDING_CLOSE:
      this.state = AccountingSessionState.PendingS;
      break;
    case PENDING_BUFFERED:
      this.state = AccountingSessionState.PendingB;
      break;
    }
  }

  public ClientAccSession getSession() {
    return this.clientSession;
  }

}
