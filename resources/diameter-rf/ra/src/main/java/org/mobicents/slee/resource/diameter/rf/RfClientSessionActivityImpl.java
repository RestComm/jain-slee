/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.resource.diameter.rf;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rf.RfAvpFactory;
import net.java.slee.resource.diameter.rf.RfClientSessionActivity;
import net.java.slee.resource.diameter.rf.RfMessageFactory;
import net.java.slee.resource.diameter.rf.RfSessionState;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.rf.ClientRfSession;
import org.jdiameter.common.api.app.rf.ClientRfSessionState;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * Implementation of {@link RfClientSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RfClientSessionActivityImpl extends RfSessionActivityImpl implements RfClientSessionActivity {

  private static final long serialVersionUID = -896041231173969408L;

  protected transient ClientRfSession clientSession = null;

  public RfClientSessionActivityImpl(RfMessageFactory rfMessageFactory, RfAvpFactory rfAvpFactory, ClientRfSession clientSession, DiameterIdentity destinationHost,
      DiameterIdentity destinationRealm, Stack stack) {
    super(rfMessageFactory, rfAvpFactory, null, (EventListener<Request, Answer>) clientSession, destinationHost, destinationRealm);

    setSession(clientSession);
    super.setCurrentWorkingSession(this.clientSession.getSessions().get(0));
  }



  public RfAccountingRequest createRfAccountingRequest(AccountingRecordType accountingRecordType) {
    RfAccountingRequest acr = rfMessageFactory.createRfAccountingRequest(accountingRecordType);

    // Set Acct-Application-Id to 3 as specified
    acr.setAcctApplicationId(3L);
    acr.setAccountingRecordType(accountingRecordType);

    return acr;
  }

  public void sendRfAccountingRequest(RfAccountingRequest request) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) request;
    try {
      this.clientSession.sendAccountRequest(new org.jdiameter.common.impl.app.rf.RfAccountingRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message, due to: ", e);
      }
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    stateChanged(oldState, newState);
  }

  public void stateChanged(Enum oldState, Enum newState) {
    ClientRfSessionState state = (ClientRfSessionState) newState;

    // FIXME: baranowb: PendingL - where does this fit?
    switch (state) {
    case IDLE:
      if (oldState != state) {
        // String sessionId =
        // this.clientSession.getSessions().get(0).getSessionId();
        // this.state = AccountingSessionState.Idle;
        // this.clientSession.release();
        // this.baseListener.sessionDestroyed(sessionId,
        // this.clientSession);
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
      }
      break;
    case OPEN:
      // this.state = AccountingSessionState.Open;
      break;
    case PENDING_EVENT:
      // this.state = AccountingSessionState.PendingE;
      break;
    case PENDING_START:
      // this.state = AccountingSessionState.PendingS;
      break;
    case PENDING_INTERIM:
      // this.state = AccountingSessionState.PendingI;
      break;
    case PENDING_CLOSE:
      // this.state = AccountingSessionState.PendingS;
      break;
    case PENDING_BUFFERED:
      // this.state = AccountingSessionState.PendingB;
      break;
    }
  }

  public ClientRfSession getSession() {
    return this.clientSession;
  }

  @Override
  public RfSessionState getRfSessionState() {
    ClientRfSessionState state = (ClientRfSessionState) clientSession.getState(ClientRfSessionState.class);

    // FIXME: baranowb: PendingL - where does this fit?
    switch (state) {
    case IDLE:
      return RfSessionState.Idle;
    case OPEN:
      return RfSessionState.Open;
    case PENDING_EVENT:
      return RfSessionState.PendingE;
    case PENDING_START:
      return RfSessionState.PendingS;
    case PENDING_INTERIM:
      return RfSessionState.PendingI;
    case PENDING_CLOSE:
      return RfSessionState.PendingC;
    case PENDING_BUFFERED:
      return RfSessionState.PendingB;
    default:
      logger.error("Unexpected state in Accounting Client FSM: " + state);
      return null;
    }
  }

  // used to recreate object.
  public void setSession(ClientRfSession clientSession) {
    this.clientSession = clientSession;
    this.clientSession.addStateChangeNotification(this);
    super.eventListener = (EventListener<Request, Answer>) clientSession;
  }

  @Override
  public void endActivity() {
    this.clientSession.release();
    super.endActivity();
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.RfSessionActivity#getRfMessageFactory()
   */
  public RfMessageFactory getRfMessageFactory() {
    return this.rfMessageFactory;
  }

  public void setRfMessageFactory(RfMessageFactory rfMessageFactory) {
    this.rfMessageFactory = rfMessageFactory;
  }

}
