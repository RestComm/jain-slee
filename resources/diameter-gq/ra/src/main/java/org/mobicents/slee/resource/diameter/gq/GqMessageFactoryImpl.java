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

package org.mobicents.slee.resource.diameter.gq;

import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.gq.events.GqAAAnswerImpl;
import org.mobicents.slee.resource.diameter.gq.events.GqAARequestImpl;
import org.mobicents.slee.resource.diameter.gq.events.GqAbortSessionAnswerImpl;
import org.mobicents.slee.resource.diameter.gq.events.GqAbortSessionRequestImpl;
import org.mobicents.slee.resource.diameter.gq.events.GqReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.gq.events.GqReAuthRequestImpl;
import org.mobicents.slee.resource.diameter.gq.events.GqSessionTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.gq.events.GqSessionTerminationRequestImpl;
import org.apache.log4j.Logger;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.gq.GqMessageFactory;
import net.java.slee.resource.diameter.gq.events.GqAAAnswer;
import net.java.slee.resource.diameter.gq.events.GqAARequest;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest;
import net.java.slee.resource.diameter.gq.events.GqReAuthAnswer;
import net.java.slee.resource.diameter.gq.events.GqReAuthRequest;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationAnswer;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest;


/**
 * Implementation of {@link GqMessageFactory}.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa</a>
 */
public class GqMessageFactoryImpl implements GqMessageFactory {

  protected DiameterMessageFactory baseFactory = null;

  protected String sessionId;
  protected Stack stack;
  protected Logger logger = Logger.getLogger(GqMessageFactoryImpl.class);

  private ApplicationId gqAppId = ApplicationId.createByAuthAppId(0L, _GQ_AUTH_APP_ID);

  public GqMessageFactoryImpl(DiameterMessageFactory baseFactory, String sessionId, Stack stack) {
    super();

    this.baseFactory = baseFactory;
    this.sessionId = sessionId;
    this.stack = stack;
  }

  public void setApplicationId(long vendorId, long applicationId) {
    this.gqAppId = ApplicationId.createByAuthAppId(vendorId, applicationId);      
  }
  
  public ApplicationId getApplicationId() {
    return this.gqAppId;      
  }
  
  @Override
  public GqAARequest createGqAARequest() {
    Message raw = createRequest(GqAARequest.COMMAND_CODE, new DiameterAvp[] {});
    GqAARequest aar = new GqAARequestImpl(raw);

    if (sessionId != null) {
      aar.setSessionId(sessionId);
    }

    addOrigin(aar);
    return aar;
  }

  @Override
  public GqAARequest createGqAARequest(String sessionId) {
    Message raw = createRequest(GqAARequest.COMMAND_CODE, new DiameterAvp[] {});
    GqAARequest aar = new GqAARequestImpl(raw);
    aar.setSessionId(sessionId);

    addOrigin(aar);
    return aar;
  }

  @Override
  public GqAAAnswer createGqAAAnswer(GqAARequest aar) {
    Message raw = createMessage(aar.getHeader(), new DiameterAvp[] {});
    raw.setRequest(false); // this should be different ...
    raw.setReTransmitted(false); // just in case. answers never have T flag set
    GqAAAnswerImpl aaa = new GqAAAnswerImpl(raw);
    aaa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_HOST);
    aaa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_REALM);
    aaa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_HOST);
    aaa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_REALM);
    aaa.setSessionId(aar.getSessionId());

    addOrigin(aaa);
    return aaa;
  }

  @Override
  public GqAbortSessionRequest createGqAbortSessionRequest() {
    Message raw = createRequest(GqAbortSessionRequest.COMMAND_CODE, new DiameterAvp[] {});
    GqAbortSessionRequest asr = new GqAbortSessionRequestImpl(raw);

    if (sessionId != null) {
      asr.setSessionId(sessionId);
    }

    addOrigin(asr);
    return asr;
  }

  @Override
  public GqAbortSessionRequest createGqAbortSessionRequest(String sessionId) {
    Message raw = createRequest(GqAbortSessionRequest.COMMAND_CODE, new DiameterAvp[] {});
    GqAbortSessionRequest asr = new GqAbortSessionRequestImpl(raw);
    asr.setSessionId(sessionId);
    addOrigin(asr);
    return asr;
  }

  @Override
  public GqAbortSessionAnswer createGqAbortSessionAnswer(GqAbortSessionRequest asr) {
    Message raw = createMessage(asr.getHeader(), new DiameterAvp[] {});
    raw.setRequest(false); // this should be different ...
    raw.setReTransmitted(false); // just in case. answers never have T flag set
    GqAbortSessionAnswerImpl asa = new GqAbortSessionAnswerImpl(raw);
    asa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_HOST);
    asa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_REALM);
    asa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_HOST);
    asa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_REALM);
    asa.setSessionId(asr.getSessionId());

    addOrigin(asa);
    return asa;
  }

  @Override
  public GqReAuthRequest createGqReAuthRequest() {
    Message raw = createRequest(GqReAuthRequest.COMMAND_CODE, new DiameterAvp[] {});
    GqReAuthRequest rar = new GqReAuthRequestImpl(raw);

    if (sessionId != null) {
      rar.setSessionId(sessionId);
    }

    addOrigin(rar);
    return rar;
  }

  @Override
  public GqReAuthRequest createGqReAuthRequest(String sessionId) {
    Message raw = createRequest(GqReAuthRequest.COMMAND_CODE, new DiameterAvp[] {});
    GqReAuthRequest rar = new GqReAuthRequestImpl(raw);
    rar.setSessionId(sessionId);

    addOrigin(rar);
    return rar;
  }

  @Override
  public GqReAuthAnswer createGqReAuthAnswer(GqReAuthRequest rar) {
    Message raw = createMessage(rar.getHeader(), new DiameterAvp[] {});
    raw.setRequest(false); // this should be different ...
    raw.setReTransmitted(false); // just in case. answers never have T flag set
    GqReAuthAnswerImpl raa = new GqReAuthAnswerImpl(raw);
    raa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_HOST);
    raa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_REALM);
    raa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_HOST);
    raa.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_REALM);
    raa.setSessionId(rar.getSessionId());

    addOrigin(raa);
    return raa;
  }

  @Override
  public GqSessionTerminationRequest createGqSessionTerminationRequest() {
    Message raw = createRequest(GqSessionTerminationRequest.COMMAND_CODE, new DiameterAvp[] {});
    GqSessionTerminationRequest str = new GqSessionTerminationRequestImpl(raw);

    if (sessionId != null) {
      str.setSessionId(sessionId);
    }

    addOrigin(str);
    return str;
  }

  @Override
  public GqSessionTerminationRequest createGqSessionTerminationRequest(String sessionId) {
    Message raw = createRequest(GqSessionTerminationRequest.COMMAND_CODE, new DiameterAvp[] {});
    GqSessionTerminationRequest str = new GqSessionTerminationRequestImpl(raw);
    str.setSessionId(sessionId);

    addOrigin(str);
    return str;
  }

  @Override
  public GqSessionTerminationAnswer createGqSessionTerminationAnswer(GqSessionTerminationRequest str) {
    Message raw = createMessage(str.getHeader(), new DiameterAvp[] {});
    raw.setRequest(false); // this should be different ...
    raw.setReTransmitted(false); // just in case. answers never have T flag set
    GqSessionTerminationAnswerImpl sta = new GqSessionTerminationAnswerImpl(raw);
    sta.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_HOST);
    sta.getGenericData().getAvps().removeAvp(DiameterAvpCodes.DESTINATION_REALM);
    sta.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_HOST);
    sta.getGenericData().getAvps().removeAvp(DiameterAvpCodes.ORIGIN_REALM);
    sta.setSessionId(str.getSessionId());

    addOrigin(sta);
    return sta;
  }

  @Override
  public DiameterMessageFactory getBaseMessageFactory() {
    return baseFactory;
  }

  public Message createRequest(int code, DiameterAvp[] avps) {
    Message raw = createMessage(code, new DiameterAvp[] {});
    raw.setProxiable(true);
    raw.setRequest(true);
    return raw;
  }

  public Message createMessage(DiameterHeader header, DiameterAvp[] avps) throws AvpNotAllowedException {
    Message msg = createRawMessage(header);

    AvpSet set = msg.getAvps();
    for (DiameterAvp avp : avps) {
      addAvp(avp, set);
    }

    return msg;
  }

  public Message createMessage(int commandCode, DiameterAvp[] avps) throws AvpNotAllowedException {
    Message msg = createRawMessage(commandCode);

    AvpSet set = msg.getAvps();
    for (DiameterAvp avp : avps) {
      addAvp(avp, set);
    }

    return msg;
  }

  protected Message createRawMessage(DiameterHeader header) {
    if (header == null) {
      return null;
    }

    int commandCode = 0;
    long endToEndId = 0;
    long hopByHopId = 0;

    boolean isRequest = true;
    boolean isProxiable = true;
    boolean isError = false;
    boolean isPotentiallyRetransmitted = false;

    commandCode = header.getCommandCode();
    endToEndId = header.getEndToEndId();
    hopByHopId = header.getHopByHopId();

    isRequest = header.isRequest();
    isProxiable = header.isProxiable();
    isError = header.isError();
    isPotentiallyRetransmitted = header.isPotentiallyRetransmitted();

    Message msg = null;

    try {
      msg = stack.getSessionFactory().getNewRawSession().createMessage(commandCode, gqAppId, hopByHopId, endToEndId);

      // Set the message flags from header (or default)
      msg.setRequest(isRequest);
      msg.setProxiable(isProxiable);
      msg.setError(isError);
      msg.setReTransmitted(isRequest && isPotentiallyRetransmitted);
    }
    catch (IllegalDiameterStateException e) {
      logger.error("Failed to get session factory for message creation.", e);
    }
    catch (InternalException e) {
      logger.error("Failed to create new raw session for message creation.", e);
    }

    return msg;
  }

  protected Message createRawMessage(int commandCode) {
    try {
      return stack.getSessionFactory().getNewRawSession().createMessage(commandCode, gqAppId);
    }
    catch (IllegalDiameterStateException e) {
      logger.error("Failed to get session factory for message creation.", e);
    }
    catch (InternalException e) {
      logger.error("Failed to create new raw session for message creation.", e);
    }
    return null;
  }

  protected void addAvp(DiameterAvp avp, AvpSet set) {
    if (avp instanceof GroupedAvp) {
      AvpSet avpSet = set.addGroupedAvp(avp.getCode(), avp.getVendorId(), avp.getMandatoryRule() == 1, avp.getProtectedRule() == 1);

      DiameterAvp[] groupedAVPs = ((GroupedAvp) avp).getExtensionAvps();
      for (DiameterAvp avpFromGroup : groupedAVPs) {
        addAvp(avpFromGroup, avpSet);
      }
    }
    else if (avp != null) {
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
