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

package org.mobicents.slee.resource.diameter.ro;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.ro.RoMessageFactory;
import net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer;
import net.java.slee.resource.diameter.ro.events.RoCreditControlMessage;
import net.java.slee.resource.diameter.ro.events.RoCreditControlRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.ro.events.RoCreditControlAnswerImpl;
import org.mobicents.slee.resource.diameter.ro.events.RoCreditControlRequestImpl;

/**
 * Implementation of {@link RoMessageFactory}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoMessageFactoryImpl implements RoMessageFactory {

  protected Logger logger = Logger.getLogger(RoMessageFactoryImpl.class);

  protected final static Set<Integer> ids;

  static {
    Set<Integer> _ids = new HashSet<Integer>();

    // SessionId
    // _ids.add(Avp.SESSION_ID);
    // Sub-Session-Id
    // _ids.add(CreditControlAVPCodes.CC_Sub_Session_Id);
    // { Origin-Host }
    // _ids.add(Avp.ORIGIN_HOST);
    // { Origin-Realm }
    // _ids.add(Avp.ORIGIN_REALM);
    // { Destination-Realm }
    // _ids.add(Avp.DESTINATION_REALM);
    // _ids.add(Avp.DESTINATION_HOST);
    // { Auth-Application-Id }
    // _ids.add(Avp.AUTH_APPLICATION_ID);
    // { Service-Context-Id }
    // _ids.add(CreditControlAVPCodes.Service_Context_Id);
    // { CC-Request-Type }
    _ids.add(CreditControlAVPCodes.CC_Request_Type);
    // { CC-Request-Number }
    _ids.add(CreditControlAVPCodes.CC_Request_Number);
    // [ Acct-Multi-Session-Id ]
    // _ids.add(Avp.ACC_MULTI_SESSION_ID);
    // [ Origin-State-Id ]
    // _ids.add(Avp.ORIGIN_STATE_ID);
    // [ Event-Timestamp ]
    // _ids.add(Avp.EVENT_TIMESTAMP);
    // xx*[ Proxy-Info ]
    // xx*[ Route-Record ]

    ids = Collections.unmodifiableSet(_ids);
  }
  protected DiameterMessageFactory baseFactory = null;

  protected String sessionId;
  protected Stack stack;

  private ApplicationId roAppId = ApplicationId.createByAuthAppId(0, _RO_AUTH_APP_ID);

  // protected RfAVPFactory rfAvpFactory = null;
  public RoMessageFactoryImpl(DiameterMessageFactory baseFactory, String sessionId, Stack stack/*
   * ,
   * RfAVPFactory
   * creditControlAvpFactory
   */) {
    super();

    this.baseFactory = baseFactory;
    this.sessionId = sessionId;
    this.stack = stack;
  }

  public void setApplicationId(long vendorId, long applicationId) {
    this.roAppId = ApplicationId.createByAuthAppId(vendorId, applicationId);
  }
  
  public ApplicationId getApplicationId() {
    return this.roAppId;      
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.ro.RoMessageFactory#
   * createRoCreditControlRequest(AccountingRecordType accountingrecordtype)
   */
  public RoCreditControlRequest createRoCreditControlRequest(/*CcRequestType type*/) {
    RoCreditControlRequest ro = (RoCreditControlRequest) createRoCreditControlRequest(null, new DiameterAvp[] {});
    if (sessionId != null) {
      ro.setSessionId(sessionId);
    }

    //ro.setCcRequestType(type);

    return ro;
  }

  public RoCreditControlRequest createRoCreditControlRequest(String sessionId/*, CcRequestType type*/) {
    RoCreditControlRequest ro = this.createRoCreditControlRequest(/*type*/);
    ro.setSessionId(sessionId);
    //ro.setCcRequestType(type);
    return ro;
  }

  public RoCreditControlAnswer createRoCreditControlAnswer(RoCreditControlRequest request) {

    // Create the answer from the request
    RoCreditControlRequestImpl ccr = (RoCreditControlRequestImpl) request;

    // DiameterAvp sessionIdAvp = null;
    // try {
    // sessionIdAvp = creditControlAvpFactory.getBaseFactory().createAvp(0,
    // DiameterAvpCodes.SESSION_ID, this.session.getSessionId());
    // }
    // catch (NoSuchAvpException e1) {
    // logger.error("Session-Id AVP not found in message", e1);
    // }
    RoCreditControlAnswerImpl msg = new RoCreditControlAnswerImpl(createMessage(ccr.getHeader(), new DiameterAvp[] {}));

    // FIXME: ammendonca: go through proper methods...
    Message raw = msg.getGenericData();
    raw.setRequest(false);
    raw.setReTransmitted(false); // just in case. answers never have T flag set

    raw.getAvps().removeAvp(DiameterAvpCodes.DESTINATION_HOST);
    raw.getAvps().removeAvp(DiameterAvpCodes.DESTINATION_REALM);
    raw.getAvps().removeAvp(DiameterAvpCodes.ORIGIN_HOST);
    raw.getAvps().removeAvp(DiameterAvpCodes.ORIGIN_REALM);
    msg.setSessionId(request.getSessionId());
    // Now copy the needed AVPs

    DiameterAvp[] messageAvps = request.getAvps();
    if (messageAvps != null) {
      for (DiameterAvp a : messageAvps) {
        try {
          if (ids.contains(a.getCode())) {
            msg.addAvp(a);
          }
        } catch (Exception e) {
          logger.error("Failed to add AVP to answer. Code[" + a.getCode() + "]", e);
        }
      }
    }
    addOrigin(msg);
    return msg;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * net.java.slee.resource.diameter.ro.RoMessageFactory#getBaseMessageFactory
   * ()
   */
  public DiameterMessageFactory getBaseMessageFactory() {
    return this.baseFactory;
  }

  private RoCreditControlMessage createRoCreditControlRequest(DiameterHeader diameterHeader, DiameterAvp[] avps) throws IllegalArgumentException {
    // List<DiameterAvp> list = (List<DiameterAvp>) this.avpList.clone();
    boolean isRequest = diameterHeader == null;
    RoCreditControlMessage msg = null;
    if (!isRequest) {
      Message raw = createMessage(diameterHeader, avps);
      raw.setProxiable(diameterHeader.isProxiable());
      raw.setRequest(false);
      raw.setReTransmitted(false); // just in case. answers never have T flag set
      msg = new RoCreditControlAnswerImpl(raw);
    } else {
      Message raw = createMessage(null, avps);
      raw.setProxiable(true);
      raw.setRequest(true);
      msg = new RoCreditControlRequestImpl(raw);
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

    boolean isRequest = true;
    boolean isProxiable = true;
    boolean isError = false;
    boolean isPotentiallyRetransmitted = false;

    if (header != null) {
      // Answer
      commandCode = header.getCommandCode();
      endToEndId = header.getEndToEndId();
      hopByHopId = header.getHopByHopId();

      isRequest = header.isRequest();
      isProxiable = header.isProxiable();
      isError = header.isError();
      isPotentiallyRetransmitted = header.isPotentiallyRetransmitted();

      // aid = ApplicationId.createByAuthAppId(header.getApplicationId());
    } else {
      commandCode = RoCreditControlRequest.commandCode;
      // endToEndId = (long) (Math.random()*1000000);
      // hopByHopId = (long) (Math.random()*1000000)+1;
    }
    Message msg = null;
    try {
      if (header != null) {
        msg = stack.getSessionFactory().getNewRawSession().createMessage(commandCode, roAppId, hopByHopId, endToEndId);
      } else {
        msg = stack.getSessionFactory().getNewRawSession().createMessage(commandCode, roAppId);
      }
    } catch (IllegalDiameterStateException e) {
      logger.error("Failed to get session factory for message creation.", e);
    } catch (InternalException e) {
      logger.error("Failed to create new raw session for message creation.", e);
    }

    // Set the message flags from header (or default)
    msg.setRequest(isRequest);
    msg.setProxiable(isProxiable);
    msg.setError(isError);
    msg.setReTransmitted(isRequest && isPotentiallyRetransmitted);

    return msg;
  }

  protected void addAvp(DiameterAvp avp, AvpSet set) {
    // FIXME: alexandre: Should we look at the types and add them with
    // proper function?
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

  private void addOrigin(DiameterMessage msg) {
    if (!msg.hasOriginHost()) {
      msg.setOriginHost(new DiameterIdentity(stack.getMetaData().getLocalPeer().getUri().getFQDN().toString()));
    }
    if (!msg.hasOriginRealm()) {
      msg.setOriginRealm(new DiameterIdentity(stack.getMetaData().getLocalPeer().getRealmName()));
    }
  }

}
