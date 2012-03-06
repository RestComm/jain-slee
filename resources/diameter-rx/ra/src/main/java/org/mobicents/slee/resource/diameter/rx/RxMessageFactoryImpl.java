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

package org.mobicents.slee.resource.diameter.rx;

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
import net.java.slee.resource.diameter.rx.RxMessageFactory;
import net.java.slee.resource.diameter.rx.events.AAAnswer;
import net.java.slee.resource.diameter.rx.events.AARequest;
import net.java.slee.resource.diameter.rx.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.rx.events.AbortSessionRequest;
import net.java.slee.resource.diameter.rx.events.ReAuthAnswer;
import net.java.slee.resource.diameter.rx.events.ReAuthRequest;
import net.java.slee.resource.diameter.rx.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.rx.events.SessionTerminationRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.Message;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.rx.events.AAAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.AARequestImpl;
import org.mobicents.slee.resource.diameter.rx.events.AbortSessionAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.AbortSessionRequestImpl;
import org.mobicents.slee.resource.diameter.rx.events.ReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.ReAuthRequestImpl;
import org.mobicents.slee.resource.diameter.rx.events.SessionTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.SessionTerminationRequestImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.DiameterRxAvpCodes;

/**
 * Implementation of {@link RxMessageFactory}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class RxMessageFactoryImpl implements RxMessageFactory {

  protected Logger logger = Logger.getLogger(RxMessageFactoryImpl.class);

  private static final DiameterAvp[] EMPTY_AVP_ARRAY = new DiameterAvp[]{};
  protected final static Set<Integer> ids;

  static {
    final Set<Integer> _ids = new HashSet<Integer>();
    //_ids.add(Avp.SESSION_ID);
    //_ids.add(Avp.AUTH_APPLICATION_ID);
    //_ids.add(Avp.VENDOR_SPECIFIC_APPLICATION_ID);

    ids = Collections.unmodifiableSet(_ids);
  }

  protected DiameterMessageFactory baseFactory = null;
  protected String sessionId;
  protected Stack stack;

  private ApplicationId rxAppId = ApplicationId.createByAuthAppId(DiameterRxAvpCodes.RX_APPLICATION_ID);

  public RxMessageFactoryImpl(final DiameterMessageFactory baseFactory, final String sessionId, final Stack stack) {
    super();

    this.baseFactory = baseFactory;
    this.sessionId = sessionId;
    this.stack = stack;
  }

  public void setApplicationId(long vendorId, long applicationId) {
    this.rxAppId = ApplicationId.createByAuthAppId(vendorId, applicationId);      
  }
  
  public ApplicationId getApplicationId() {
    return this.rxAppId;      
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AARequest createAARequest() {
    final AARequest aar = (AARequest) createDiameterMessage(null,new DiameterAvp[]{},AARequest.commandCode, rxAppId);
    if (sessionId != null) {
      aar.setSessionId(sessionId);
    }
    return aar;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AARequest createAARequest(String sessionId) {
    final AARequest aar = this.createAARequest();
    aar.setSessionId(sessionId);
    return aar;
  }

  public AAAnswer createAAAnswer(final AARequest request) {
    // Create the answer from the request
    final AAAnswerImpl msg = (AAAnswerImpl) createDiameterMessage(request.getHeader(), EMPTY_AVP_ARRAY, 0, rxAppId);

    msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_HOST);
    msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_REALM);
    msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_HOST);
    msg.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_REALM);
    msg.setSessionId(request.getSessionId());

    // Now copy the needed AVPs
    final DiameterAvp[] messageAvps = request.getAvps();
    if (messageAvps != null) {
      for (DiameterAvp a : messageAvps) {
        try {
          if (ids.contains(a.getCode())) {
            msg.addAvp(a);
          }
        }
        catch (Exception e) {
          logger.error("Failed to add AVP to answer. Code[" + a.getCode() + "]", e);
        }
      }
    }

    return msg;
  }

  public AbortSessionAnswer createAbortSessionAnswer(AbortSessionRequest request, DiameterAvp[] avps) throws AvpNotAllowedException {
    AbortSessionAnswer msg = (AbortSessionAnswer) this.createDiameterMessage(request.getHeader(), avps, Message.ABORT_SESSION_ANSWER, getApplicationId(request));

    // Add Session-Id AVP if not present
    if (msg.getSessionId() == null) {
      String reqSessionId = request.getSessionId();
      if (reqSessionId != null) {
        msg.setSessionId(reqSessionId);
      }
      else if (sessionId != null) {
        msg.setSessionId(sessionId);
      }
    }

    return msg;
  }

  public AbortSessionAnswer createAbortSessionAnswer(AbortSessionRequest request) {
    try {
      return createAbortSessionAnswer(request, new DiameterAvp[0]);
    }
    catch (AvpNotAllowedException e) {
      logger.error("Unexpected failure while trying to create ASA message.", e);
    }

    return null;
  }

  public AbortSessionRequest createAbortSessionRequest(DiameterAvp[] avps) throws AvpNotAllowedException {
    AbortSessionRequest msg = (AbortSessionRequest) this.createDiameterMessage(null, avps, Message.ABORT_SESSION_REQUEST, rxAppId);

    // Add Session-Id AVP if not present
    if (msg.getSessionId() == null) {
      if (sessionId != null) {
        msg.setSessionId(sessionId);
      }
    }

    return msg;
  }

  public AbortSessionRequest createAbortSessionRequest() {
    try {
      return createAbortSessionRequest(new DiameterAvp[0]);
    }
    catch (AvpNotAllowedException e) {
      logger.error("Unexpected failure while trying to create ASR message.", e);
    }

    return null;
  }
  public ReAuthAnswer createReAuthAnswer(ReAuthRequest request, DiameterAvp[] avps) throws AvpNotAllowedException {
    ReAuthAnswer msg = (ReAuthAnswer) this.createDiameterMessage(request.getHeader(), avps, Message.RE_AUTH_ANSWER, getApplicationId(request));

    // Add Session-Id AVP if not present
    if (msg.getSessionId() == null) {
      String reqSessionId = request.getSessionId();
      if (reqSessionId != null) {
        msg.setSessionId(reqSessionId);
      }
      else if (sessionId != null) {
        msg.setSessionId(sessionId);
      }
    }

    return msg;
  }

  public ReAuthAnswer createReAuthAnswer(ReAuthRequest request) {
    try {
      return createReAuthAnswer(request, new DiameterAvp[0]);
    }
    catch (AvpNotAllowedException e) {
      logger.error("Unexpected failure while trying to create RAA message.", e);
    }

    return null;
  }

  public ReAuthRequest createReAuthRequest(DiameterAvp[] avps) throws AvpNotAllowedException {
    ReAuthRequest msg = (ReAuthRequest) this.createDiameterMessage(null, avps, Message.RE_AUTH_REQUEST, rxAppId);

    // Add Session-Id AVP if not present
    if (msg.getSessionId() == null) {
      if (sessionId != null) {
        msg.setSessionId(sessionId);
      }
    }

    return msg;
  }

  public ReAuthRequest createReAuthRequest() {
    try {
      return createReAuthRequest(new DiameterAvp[0]);
    }
    catch (AvpNotAllowedException e) {
      logger.error("Unexpected failure while trying to create RAR message.", e);
    }

    return null;
  }

  public SessionTerminationAnswer createSessionTerminationAnswer(SessionTerminationRequest request, DiameterAvp[] avps) throws AvpNotAllowedException {
    SessionTerminationAnswer msg = (SessionTerminationAnswer) this.createDiameterMessage(request.getHeader(), avps, Message.SESSION_TERMINATION_REQUEST, rxAppId);

    // Add Session-Id AVP if not present
    if (msg.getSessionId() == null) {
      String reqSessionId = request.getSessionId();
      if (reqSessionId != null) {
        msg.setSessionId(reqSessionId);
      }
      else if (sessionId != null) {
        msg.setSessionId(sessionId);
      }
    }

    return msg;
  }

  public SessionTerminationAnswer createSessionTerminationAnswer(SessionTerminationRequest request) {
    try {
      return createSessionTerminationAnswer(request, EMPTY_AVP_ARRAY);
     }
    catch (AvpNotAllowedException e) {
      logger.error("Unexpected failure while trying to create STA message.", e);
    }

    return null;
  }

  public SessionTerminationRequest createSessionTerminationRequest(DiameterAvp[] avps) throws AvpNotAllowedException {
    SessionTerminationRequest msg = (SessionTerminationRequest) this.createDiameterMessage(null, avps, Message.SESSION_TERMINATION_REQUEST, rxAppId);

    // Add Session-Id AVP if not present
    if (msg.getSessionId() == null) {
      if (sessionId != null) {
        msg.setSessionId(sessionId);
      }
    }

    return msg;
  }

  public SessionTerminationRequest createSessionTerminationRequest() {
    try {
      return createSessionTerminationRequest(new DiameterAvp[0]);
    }
    catch (AvpNotAllowedException e) {
      logger.error("Unexpected failure while trying to create STR message.", e);
      return null;
    }
  }

  protected DiameterMessage createDiameterMessage(DiameterHeader diameterHeader, DiameterAvp[] avps, int _commandCode, ApplicationId appId) throws IllegalArgumentException {

    boolean creatingRequest = diameterHeader == null;
    Message msg = null;

    if (!creatingRequest) {
      Message raw = createMessage(diameterHeader, avps, 0, appId);
      raw.setRequest(false);
      raw.setReTransmitted(false); // just in case. answers never have T flag set
      msg = raw;
    }
    else {
      Message raw = createMessage(diameterHeader, avps, _commandCode, appId);
      raw.setRequest(true);
      msg = raw;
    }

    int commandCode = creatingRequest ? _commandCode : diameterHeader.getCommandCode();
    DiameterMessage diamMessage = null;

    switch (commandCode) {
      case AARequest.commandCode:
        diamMessage = creatingRequest ? new AARequestImpl(msg) : new AAAnswerImpl(msg);
        break;
      case Message.ABORT_SESSION_REQUEST:
        diamMessage = creatingRequest ? new AbortSessionRequestImpl(msg) : new AbortSessionAnswerImpl(msg);
        break;
      case Message.RE_AUTH_REQUEST:
        diamMessage = creatingRequest ? new ReAuthRequestImpl(msg) : new ReAuthAnswerImpl(msg);
        break;
      case Message.SESSION_TERMINATION_REQUEST:
        diamMessage = creatingRequest ? new SessionTerminationRequestImpl(msg) : new SessionTerminationAnswerImpl(msg);
        break;
      default:
        throw new IllegalArgumentException();
    }

    // Finally, add Origin-Host and Origin-Realm, if not present.
    addOriginHostAndRealm(diamMessage);

    return diamMessage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DiameterMessageFactory getBaseMessageFactory() {
    return this.baseFactory;
  }

  protected Message createMessage(DiameterHeader header, DiameterAvp[] avps,int _commandCode, ApplicationId appId) throws AvpNotAllowedException {
    try {
      Message msg = createRawMessage(header, _commandCode, appId);

      if(avps != null && avps.length > 0) {
        AvpSet set = msg.getAvps();
        for (DiameterAvp avp : avps) {
          if(appId != null && (avp.getCode() == DiameterAvpCodes.VENDOR_SPECIFIC_APPLICATION_ID || avp.getCode() == DiameterAvpCodes.ACCT_APPLICATION_ID || avp.getCode() == DiameterAvpCodes.AUTH_APPLICATION_ID)) {
            continue;
          }
          addAvp(avp, set);
        }
      }

      return msg;
    }
    catch (Exception e) {
      logger.error("Failure trying to create Diameter message.", e);
    }

    return null;
  }

  protected Message createRawMessage(DiameterHeader header, int _commandCode, ApplicationId appId) {

    int commandCode = 0;
    long endToEndId = 0;
    long hopByHopId = 0;

    boolean isRequest = true;
    boolean isProxiable = true;
    boolean isError = false;
    boolean isPotentiallyRetransmitted = false;

    if (header != null) {
      commandCode = header.getCommandCode();
      endToEndId = header.getEndToEndId();
      hopByHopId = header.getHopByHopId();

      isRequest = header.isRequest();
      isProxiable = header.isProxiable();
      isError = header.isError();
      isPotentiallyRetransmitted = header.isPotentiallyRetransmitted();
    }
    else {
      commandCode = _commandCode;
    }
    try {
      Message msg = stack.getSessionFactory().getNewRawSession().createMessage(commandCode, appId != null ? appId : rxAppId, hopByHopId, endToEndId);

      // Set the message flags from header (or default)
      msg.setRequest(isRequest);
      msg.setProxiable(isProxiable);
      msg.setError(isError);
      msg.setReTransmitted(isRequest && isPotentiallyRetransmitted);

      return msg;
    }
    catch (Exception e) {
      logger.error( "Failure while trying to create raw message.", e );
    }

    return null;
  }

  protected void addAvp(DiameterAvp avp, AvpSet set) {
    if (avp instanceof GroupedAvp) {
      AvpSet avpSet = set.addGroupedAvp(avp.getCode(), avp.getVendorId(), avp.getMandatoryRule() != DiameterAvp.FLAG_RULE_MUSTNOT, avp.getProtectedRule() == DiameterAvp.FLAG_RULE_MUST);

      DiameterAvp[] groupedAVPs = ((GroupedAvp) avp).getExtensionAvps();
      for (DiameterAvp avpFromGroup : groupedAVPs) {
        addAvp(avpFromGroup, avpSet);
      }
    }
    else if (avp != null) {
      set.addAvp(avp.getCode(), avp.byteArrayValue(), avp.getVendorId(), avp.getMandatoryRule() != DiameterAvp.FLAG_RULE_MUSTNOT, avp.getProtectedRule() == DiameterAvp.FLAG_RULE_MUST);
    }
  }

  /**
   * 
   */
  public void clean() {

  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.DiameterMessageFactory#createMessage(net.java.slee.resource.diameter.base.events.DiameterHeader, net.java.slee.resource.diameter.base.events.avp.DiameterAvp[])
   */
  public DiameterMessage createMessage(DiameterHeader header, DiameterAvp[] avps) throws AvpNotAllowedException {
    return this.createDiameterMessage(header, avps, header.getCommandCode(), rxAppId);
  }

  private void addOriginHostAndRealm(DiameterMessage msg) {
    if(!msg.hasOriginHost()) {
      msg.setOriginHost(new DiameterIdentity(stack.getMetaData().getLocalPeer().getUri().getFQDN().toString()));
    }
    if(!msg.hasOriginRealm()) {
      msg.setOriginRealm(new DiameterIdentity(stack.getMetaData().getLocalPeer().getRealmName()));
    }
  }

  private ApplicationId getApplicationId(DiameterMessage msg) {
    ApplicationId applicationId = getApplicationId(msg.getAvps());

    if (applicationId == null) {
      applicationId = rxAppId;
    }

    return applicationId;
  }

  private ApplicationId getApplicationId(DiameterAvp[] avps) {
    ApplicationId applicationId = null;

    long vendorId = 0L;

    // Try to get Application-Id from Message AVPs
    if (avps != null) {
      for (DiameterAvp avp : avps) {
        if(avp.getCode() == DiameterAvpCodes.VENDOR_ID) {
          vendorId = avp.intValue();
        }
        if(avp.getCode() == DiameterAvpCodes.VENDOR_SPECIFIC_APPLICATION_ID) {
          applicationId = getApplicationId(((GroupedAvp)avp).getExtensionAvps());
          break;
        }
        if (avp.getCode() == DiameterAvpCodes.ACCT_APPLICATION_ID) {
          applicationId = ApplicationId.createByAccAppId(vendorId, avp.intValue());
          break;
        }
        else if (avp.getCode() == DiameterAvpCodes.AUTH_APPLICATION_ID) {
          applicationId = ApplicationId.createByAuthAppId(vendorId, avp.intValue());
          break;
        }
      }
    }

    return applicationId;
  }

}
