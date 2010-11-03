/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.diameter.rf;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.rf.RfMessageFactory;
import net.java.slee.resource.diameter.rf.events.RfAccountingMessage;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.rf.events.RfAccountingAnswerImpl;
import org.mobicents.slee.resource.diameter.rf.events.RfAccountingRequestImpl;

/**
 * Implementation of {@link RfMessageFactory}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RfMessageFactoryImpl implements RfMessageFactory {

  protected DiameterMessageFactory baseFactory = null;

  protected String sessionId;
  protected Stack stack;
  protected Logger logger = Logger.getLogger(this.getClass());

  // protected RfAVPFactory rfAvpFactory = null;
  public RfMessageFactoryImpl(DiameterMessageFactory baseFactory, String sessionId, Stack stack/*, RfAVPFactory creditControlAvpFactory*/) {
    super();

    this.baseFactory = baseFactory;
    this.sessionId = sessionId;
    this.stack = stack;
  }

  public RfAccountingRequest createRfAccountingRequest(AccountingRecordType accountingrecordtype) {
    DiameterAvp[] avps = new DiameterAvp[] {};

    RfAccountingRequest acr = (RfAccountingRequest) createRfAccountingRequest(null, avps);
    if (this.sessionId != null) {
      acr.setSessionId(sessionId);
    }
    // acr.setAcctApplicationId(_RF_ACC_APP_ID);
    acr.setAccountingRecordType(accountingrecordtype);

    return acr;
  }

  private RfAccountingMessage createRfAccountingRequest(DiameterHeader diameterHeader, DiameterAvp[] avps) throws IllegalArgumentException {
    // List<DiameterAvp> list = (List<DiameterAvp>) this.avpList.clone();
    boolean isRequest = diameterHeader == null;
    RfAccountingMessage msg = null;
    if (!isRequest) {
      Message raw = createMessage(diameterHeader, avps);
      raw.setProxiable(true);
      raw.setRequest(false);
      msg = new RfAccountingAnswerImpl(raw);
    } else {
      Message raw = createMessage(null, avps);
      raw.setProxiable(true);
      raw.setRequest(true);
      msg = new RfAccountingRequestImpl(raw);
    }

    return msg;
  }

  public Message createMessage(DiameterHeader header, DiameterAvp[] avps) throws AvpNotAllowedException {
    Message msg = createRawMessage(header);

    AvpSet set = msg.getAvps();
    for (DiameterAvp avp : avps) {
      addAvp(avp, set);
    }

    return msg;
  }

  protected Message createRawMessage(DiameterHeader header) {
    int commandCode = 0;
    long endToEndId = 0;
    long hopByHopId = 0;

    ApplicationId aid = ApplicationId.createByAccAppId(0, _RF_ACC_APP_ID);
    if (header != null) {
      // Answer
      commandCode = header.getCommandCode();
      endToEndId = header.getEndToEndId();
      hopByHopId = header.getHopByHopId();
      // aid = ApplicationId.createByAuthAppId(header.getApplicationId());
    } else {
      commandCode = RfAccountingRequest.commandCode;
      // endToEndId = (long) (Math.random()*1000000);
      // hopByHopId = (long) (Math.random()*1000000)+1;
    }

    try {
      if (header != null) {
        return stack.getSessionFactory().getNewRawSession().createMessage(commandCode, aid, hopByHopId, endToEndId);
      } else {
        return stack.getSessionFactory().getNewRawSession().createMessage(commandCode, aid);
      }
    } catch (IllegalDiameterStateException e) {
      logger.error("Failed to get session factory for message creation.", e);
    } catch (InternalException e) {
      logger.error("Failed to create new raw session for message creation.", e);
    }

    return null;
  }

  protected void addAvp(DiameterAvp avp, AvpSet set) {
    // FIXME: alexandre: Should we look at the types and add them with proper function?
    if (avp instanceof GroupedAvp) {
      AvpSet avpSet = set.addGroupedAvp(avp.getCode(), avp.getVendorId(), avp.getMandatoryRule() == 1, avp.getProtectedRule() == 1);

      DiameterAvp[] groupedAVPs = ((GroupedAvp) avp).getExtensionAvps();
      for (DiameterAvp avpFromGroup : groupedAVPs) {
        addAvp(avpFromGroup, avpSet);
      }
    } else if (avp != null) {
      set.addAvp(avp.getCode(), avp.byteArrayValue(), avp.getVendorId(), avp.getMandatoryRule() == 1, avp.getProtectedRule() == 1);
    }
  }

  public DiameterMessageFactory getBaseMessageFactory() {
    return this.baseFactory;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.RfMessageFactory#createRfAccountingRequest(java.lang.String, net.java.slee.resource.diameter.base.events.avp.AccountingRecordType)
   */
  @Override
  public RfAccountingRequest createRfAccountingRequest(String sessionId, AccountingRecordType accountingRecordType) {
    RfAccountingRequest req = this.createRfAccountingRequest(accountingRecordType);
    if (sessionId != null) {
      req.setSessionId(sessionId);
    }

    return req;
  }
}
