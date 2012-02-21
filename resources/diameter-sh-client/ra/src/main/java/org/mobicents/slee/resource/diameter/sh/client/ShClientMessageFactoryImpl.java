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

package org.mobicents.slee.resource.diameter.sh.client;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.NoSuchAvpException;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.sh.client.ShClientMessageFactory;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.PushNotificationAnswer;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.DataReferenceType;
import net.java.slee.resource.diameter.sh.events.avp.SubsReqType;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Session;
import org.jdiameter.api.Stack;
import org.jdiameter.client.impl.helpers.UIDGenerator;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.sh.events.ProfileUpdateRequestImpl;
import org.mobicents.slee.resource.diameter.sh.events.PushNotificationAnswerImpl;
import org.mobicents.slee.resource.diameter.sh.events.SubscribeNotificationsRequestImpl;
import org.mobicents.slee.resource.diameter.sh.events.UserDataRequestImpl;

/**
 * Test class for JAIN SLEE Diameter Sh (Client) RA Message and AVP Factories
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ShClientMessageFactoryImpl implements ShClientMessageFactory {

  private static Logger logger = Logger.getLogger(ShClientMessageFactoryImpl.class);

  protected Session session;
  protected Stack stack;
  protected DiameterMessageFactoryImpl baseFactory = null;
  protected DiameterAvpFactory baseAvpFactory = null;

  // Sh: Vendor-Specific-Application-Id is mandatory;
  private ApplicationId shAppId = ApplicationId.createByAuthAppId(_SH_VENDOR_ID, _SH_APP_ID);

  // Used for generating session id's
  protected static UIDGenerator uid = new UIDGenerator();

  public ShClientMessageFactoryImpl(Session session, Stack stack) {
    super();
    this.session = session;
    this.stack = stack;
    this.baseFactory = new DiameterMessageFactoryImpl(this.session, this.stack);
    this.baseAvpFactory = new DiameterAvpFactoryImpl();
  }

  public ShClientMessageFactoryImpl(Stack stack) {
    super();
    this.stack = stack;
    this.baseFactory = new DiameterMessageFactoryImpl(this.stack);
    this.baseAvpFactory = new DiameterAvpFactoryImpl();
  }

  public void setApplicationId(long vendorId, long applicationId) {
    this.shAppId = ApplicationId.createByAuthAppId(vendorId, applicationId);      
  }
  
  public ApplicationId getApplicationId() {
    return this.shAppId;      
  }
  
  public ProfileUpdateRequest createProfileUpdateRequest(UserIdentityAvp userIdentity, DataReferenceType reference, byte[] userData) {
    ProfileUpdateRequest pur = this.createProfileUpdateRequest();

    pur.setUserIdentity(userIdentity);
    pur.setDataReference(reference);
    pur.setUserData(userData);
    
    return pur;
  }

  public ProfileUpdateRequest createProfileUpdateRequest() {
    DiameterAvp[] avps = new DiameterAvp[0];

    if(session != null) {
      try {
        DiameterAvp sessionIdAvp = null;
        sessionIdAvp = baseAvpFactory.createAvp(0, DiameterAvpCodes.SESSION_ID, session.getSessionId());
        avps = new DiameterAvp[]{sessionIdAvp};
      }
      catch (NoSuchAvpException e) {
        logger.error( "Unexpected failure trying to create Session-Id AVP.", e );
      }
    }

    Message msg = createShMessage(null, avps, ProfileUpdateRequest.commandCode);
    ProfileUpdateRequestImpl pur = new ProfileUpdateRequestImpl(msg);
    addOrigin(pur);

    return pur;
  }

  public PushNotificationAnswer createPushNotificationAnswer(PushNotificationRequest request,long resultCode, boolean isExperimentalResultCode) {
    PushNotificationAnswer pna = this.createPushNotificationAnswer(request);

    if (isExperimentalResultCode) {
      pna.setExperimentalResult(this.baseAvpFactory.createExperimentalResult(0, resultCode));
    }
    else {
      pna.setResultCode(resultCode);
    }

    return pna;
  }

  public PushNotificationAnswer createPushNotificationAnswer(PushNotificationRequest request) {
    DiameterAvp[] avps = new DiameterAvp[0];

    try {
      DiameterAvp sessionIdAvp = null;
      sessionIdAvp = baseAvpFactory.createAvp(0, DiameterAvpCodes.SESSION_ID, request.getSessionId());
      avps = new DiameterAvp[]{sessionIdAvp};
    }
    catch (NoSuchAvpException e) {
      logger.error( "Unexpected failure trying to create Session-Id AVP.", e );
    }

    Message msg = createShMessage(request.getHeader(), avps, PushNotificationAnswer.commandCode);
    PushNotificationAnswerImpl pna = new PushNotificationAnswerImpl(msg);
    addOrigin(pna);

    return pna;
  }

  public SubscribeNotificationsRequest createSubscribeNotificationsRequest(UserIdentityAvp userIdentity, DataReferenceType reference, SubsReqType subscriptionType) {
    SubscribeNotificationsRequest snr = this.createSubscribeNotificationsRequest();
    
    snr.setUserIdentity(userIdentity);
    snr.setDataReference(reference);
    snr.setSubsReqType(subscriptionType);

    return snr;
  }

  public SubscribeNotificationsRequest createSubscribeNotificationsRequest() {
    DiameterAvp[] avps = new DiameterAvp[0];
    if(session != null) {
      try {
        DiameterAvp sessionIdAvp = null;
        sessionIdAvp = baseAvpFactory.createAvp(0, DiameterAvpCodes.SESSION_ID, session.getSessionId());
        avps = new DiameterAvp[]{sessionIdAvp};
      }
      catch (NoSuchAvpException e) {
        logger.error( "Unexpected failure trying to create Session-Id AVP.", e );
      }
    }

    Message msg = createShMessage(null, avps, SubscribeNotificationsRequest.commandCode);
    SubscribeNotificationsRequestImpl snr = new SubscribeNotificationsRequestImpl(msg);
    addOrigin(snr);

    return snr;
  }

  public UserDataRequest createUserDataRequest(UserIdentityAvp userIdentity, DataReferenceType reference) {
    UserDataRequest udr = this.createUserDataRequest();
    udr.setUserIdentity(userIdentity);
    udr.setDataReference(reference);
    return udr;
  }

  public UserDataRequest createUserDataRequest() {
    DiameterAvp[] avps = new DiameterAvp[0];

    if(session != null) {
      try {
        DiameterAvp sessionIdAvp = null;
        sessionIdAvp = baseAvpFactory.createAvp(0, DiameterAvpCodes.SESSION_ID, session.getSessionId());
        avps = new DiameterAvp[]{sessionIdAvp};
      }
      catch (NoSuchAvpException e) {
        logger.error( "Unexpected failure trying to create Session-Id AVP.", e );
      }
    }

    Message msg = createShMessage(null, avps, UserDataRequest.commandCode);
    UserDataRequestImpl udr = new UserDataRequestImpl(msg);
    addOrigin(udr);

    return udr;
  }

  private Message createShMessage(DiameterHeader diameterHeader, DiameterAvp[] avps, int _commandCode) throws IllegalArgumentException {

    // List<DiameterAvp> list = (List<DiameterAvp>) this.avpList.clone();
    boolean isRequest = diameterHeader == null;
    Message msg = null;

    if (!isRequest) {
      Message raw = createMessage(diameterHeader, avps,0);
      raw.setProxiable(diameterHeader.isProxiable());
      raw.setRequest(false);
      raw.setReTransmitted(false); // just in case. answers never have T flag set
      msg = raw;
    }
    else {
      Message raw = createMessage(null, avps,_commandCode);
      raw.setProxiable(true);
      raw.setRequest(true);
      msg = raw;
    }

    return msg;
  }

  protected Message createMessage(DiameterHeader header, DiameterAvp[] avps, int commandCode) throws AvpNotAllowedException {

    try {
      Message msg = createRawMessage(header,commandCode);
      AvpSet set = msg.getAvps();
      for (DiameterAvp avp : avps) {
        addAvp(avp, set);
      }

      return msg;
    }
    catch (Exception e) {
      logger.error("Unexpected failure trying to create Sh Message.", e);
    }
    return null;
  }

  protected Message createRawMessage(DiameterHeader header, int _commandCode) {

    int commandCode = 0;
    long endToEndId = 0;
    long hopByHopId = 0;

    if (header != null) {
      commandCode = header.getCommandCode();
      endToEndId = header.getEndToEndId();
      hopByHopId = header.getHopByHopId();
    }
    else {
      commandCode = _commandCode;
    }
    try {
      return header != null ? stack.getSessionFactory().getNewRawSession().createMessage(commandCode, shAppId, hopByHopId, endToEndId) : 
        stack.getSessionFactory().getNewRawSession().createMessage(commandCode, shAppId);
    }
    catch (InternalException e) {
      logger.error("Unexpected failure trying to create Raw Message.", e);
    }
    catch (IllegalDiameterStateException e) {
      logger.error("Unexpected failure trying to create Raw Message.", e);
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
    } else if (avp != null)
      set.addAvp(avp.getCode(), avp.byteArrayValue(), avp.getVendorId(), avp.getMandatoryRule() == 1, avp.getProtectedRule() == 1);
  }

  private void addOrigin(DiameterMessage msg) {
    if(!msg.hasOriginHost()) {
      msg.setOriginHost(new DiameterIdentity(stack.getMetaData().getLocalPeer().getUri().getFQDN().toString()));
    }
    if(!msg.hasOriginRealm()) {
      msg.setOriginRealm(new DiameterIdentity(stack.getMetaData().getLocalPeer().getRealmName()));
    }
  }

  public DiameterMessageFactory getBaseMessageFactory() {
    return this.baseFactory;
  }
}
