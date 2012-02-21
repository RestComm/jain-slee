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

package org.mobicents.slee.resource.diameter.sh.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.NoSuchAvpException;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataAnswer;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;
import net.java.slee.resource.diameter.sh.server.ShServerMessageFactory;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.AvpSet;
import org.jdiameter.api.IllegalDiameterStateException;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Session;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.sh.DiameterShAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.sh.events.ProfileUpdateAnswerImpl;
import org.mobicents.slee.resource.diameter.sh.events.PushNotificationRequestImpl;
import org.mobicents.slee.resource.diameter.sh.events.SubscribeNotificationsAnswerImpl;
import org.mobicents.slee.resource.diameter.sh.events.UserDataAnswerImpl;

/**
 * Implementation of Sh Server Message factory.
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see ShServerMessageFactory
 */
public class ShServerMessageFactoryImpl implements ShServerMessageFactory {

  protected Session session;
  protected Stack stack;
  protected DiameterMessageFactoryImpl baseFactory = null;
  protected DiameterShAvpFactoryImpl localFactory = null;

  private static Logger logger = Logger.getLogger(ShServerMessageFactoryImpl.class);

  protected ArrayList<DiameterAvp> avpList = new ArrayList<DiameterAvp>();

  // Sh: Vendor-Specific-Application-Id is mandatory;
  private ApplicationId shAppId = ApplicationId.createByAuthAppId(_SH_VENDOR_ID, _SH_APP_ID);

  public ShServerMessageFactoryImpl(Session session, Stack stack) {
    super();
    this.session = session;
    this.stack = stack;
    this.baseFactory = new DiameterMessageFactoryImpl(this.session, this.stack);
    this.localFactory = new DiameterShAvpFactoryImpl(new DiameterAvpFactoryImpl());
  }

  public ShServerMessageFactoryImpl(Stack stack) {
    super();
    this.stack = stack;
    this.baseFactory = new DiameterMessageFactoryImpl(this.stack);
    this.localFactory = new DiameterShAvpFactoryImpl(new DiameterAvpFactoryImpl());
  }

  public ShServerMessageFactoryImpl(DiameterMessageFactoryImpl baseMsgFactory, Session session, Stack stack, DiameterShAvpFactory localFactory) {
    this.session = session;
    this.stack = stack;
    this.baseFactory = baseMsgFactory;
    this.localFactory = (DiameterShAvpFactoryImpl) localFactory;
  }

  public void setApplicationId(long vendorId, long applicationId) {
    this.shAppId = ApplicationId.createByAuthAppId(vendorId, applicationId);      
  }
  
  public ApplicationId getApplicationId() {
    return this.shAppId;      
  }
  
  public ProfileUpdateAnswer createProfileUpdateAnswer(ProfileUpdateRequest request, long resultCode, boolean isExperimentalResult) {
    ProfileUpdateAnswer pua = this.createProfileUpdateAnswer(request);

    if (isExperimentalResult) {
      pua.setExperimentalResult(this.localFactory.getBaseFactory().createExperimentalResult(0, resultCode));
    }
    else {
      pua.setResultCode(resultCode);
    }

    return pua;
  }

  public ProfileUpdateAnswer createProfileUpdateAnswer(ProfileUpdateRequest request) {
    List<DiameterAvp> avps = new ArrayList<DiameterAvp>();

    DiameterAvp sessionIdAvp = null;
    try {
      sessionIdAvp = localFactory.getBaseFactory().createAvp(0, DiameterAvpCodes.SESSION_ID, request.getSessionId());
      avps.add(sessionIdAvp);
    }
    catch (NoSuchAvpException e) {
      logger.error("Unable to create Session-Id AVP.", e);
    }

    if (request.getUserIdentity() != null) {
      avps.add(request.getUserIdentity());
    }

    Message msg = createShMessage(request.getHeader(), avps.toArray(new DiameterAvp[avps.size()]));
    ProfileUpdateAnswerImpl answer = new ProfileUpdateAnswerImpl(msg);

    answer.setRouteRecords(request.getRouteRecords());

    addOrigin(answer);

    return answer;
  }

  public PushNotificationRequest createPushNotificationRequest(UserIdentityAvp userIdentity, byte[] userData) {
    PushNotificationRequest pnr = this.createPushNotificationRequest();

    pnr.setUserIdentity(userIdentity);
    pnr.setUserData(userData);

    return pnr;
  }

  public PushNotificationRequest createPushNotificationRequest() {
    List<DiameterAvp> avps = new ArrayList<DiameterAvp>();

    if (session != null)
      try {
        DiameterAvp sessionIdAvp = null;
        sessionIdAvp = localFactory.getBaseFactory().createAvp(0, DiameterAvpCodes.SESSION_ID, session.getSessionId());
        avps.add(sessionIdAvp);
      }
    catch (NoSuchAvpException e) {
      logger.error("Unable to create Session-Id AVP.", e);
    }

    Message msg = createShMessage(null, avps.toArray(new DiameterAvp[avps.size()]));
    PushNotificationRequestImpl request = new PushNotificationRequestImpl(msg);
    addOrigin(request);

    return request;
  }

  public SubscribeNotificationsAnswer createSubscribeNotificationsAnswer(SubscribeNotificationsRequest request, long resultCode, boolean isExperimentalResult) {
    SubscribeNotificationsAnswer sna = this.createSubscribeNotificationsAnswer(request);

    if (isExperimentalResult) {
      sna.setExperimentalResult(this.localFactory.getBaseFactory().createExperimentalResult(0, resultCode));
    }
    else {
      sna.setResultCode(resultCode);
    }

    return sna;
  }

  public SubscribeNotificationsAnswer createSubscribeNotificationsAnswer(SubscribeNotificationsRequest request) {
    // Message msg = createShMessage(UserDataAnswer.commandCode, session !=
    // null ? session.getSessionId() : null, false);

    List<DiameterAvp> avps = new ArrayList<DiameterAvp>();

    DiameterAvp sessionIdAvp = null;
    try {
      sessionIdAvp = localFactory.getBaseFactory().createAvp(0, DiameterAvpCodes.SESSION_ID, request.getSessionId());
      avps.add(sessionIdAvp);
    }
    catch (NoSuchAvpException e) {
      logger.error("Unable to create Session-Id AVP.", e);
    }

    if (request.getUserIdentity() != null) {
      avps.add(request.getUserIdentity());
    }

    Message msg = createShMessage(request.getHeader(), avps.toArray(new DiameterAvp[avps.size()]));
    SubscribeNotificationsAnswerImpl answer = new SubscribeNotificationsAnswerImpl(msg);

    answer.setRouteRecords(request.getRouteRecords());
    // add more :) ?

    addOrigin(answer);

    return answer;
  }

  public UserDataAnswer createUserDataAnswer(UserDataRequest request, byte[] userData) {
    UserDataAnswer uda = this.createUserDataAnswer(request);
    uda.setUserData(userData);

    return uda;
  }

  public UserDataAnswer createUserDataAnswer(UserDataRequest request, long resultCode, boolean isExperimentalResult) {
    UserDataAnswer uda = this.createUserDataAnswer(request);

    if (isExperimentalResult) {
      uda.setExperimentalResult(this.localFactory.getBaseFactory().createExperimentalResult(0, resultCode));
    }
    else {
      uda.setResultCode(resultCode);
    }

    return uda;
  }

  public UserDataAnswer createUserDataAnswer(UserDataRequest request) {
    // Message msg = createShMessage(UserDataAnswer.commandCode, session !=
    // null ? session.getSessionId() : null, false);

    List<DiameterAvp> avps = new ArrayList<DiameterAvp>();

    DiameterAvp sessionIdAvp = null;
    try {
      sessionIdAvp = localFactory.getBaseFactory().createAvp(0, DiameterAvpCodes.SESSION_ID, request.getSessionId());
      avps.add(sessionIdAvp);
    }
    catch (NoSuchAvpException e) {
      logger.error("Unable to create Session-Id AVP.", e);
    }

    if (request.getUserIdentity() != null)
      avps.add(request.getUserIdentity());

    Message msg = createShMessage(request.getHeader(), avps.toArray(new DiameterAvp[avps.size()]));
    UserDataAnswerImpl answer = new UserDataAnswerImpl(msg);

    answer.setRouteRecords(request.getRouteRecords());
    // add more :) ?

    addOrigin(answer);
    return answer;
  }

  public DiameterMessageFactory getBaseMessageFactory() {
    return this.baseFactory;
  }

  public List<DiameterAvp> getInnerAvps() {
    return this.avpList;
  }

  public void addAvpToInnerList(DiameterAvp avp) {
    // Remove existing occurences...
    removeAvpFromInnerList(avp.getCode(),avp.getVendorId());

    this.avpList.add(avp);
  }

  public void removeAvpFromInnerList(int code, long vendorId) {
    Iterator<DiameterAvp> it = this.avpList.iterator();

    while (it.hasNext()) {
    	DiameterAvp a = it.next();
      if (a.getCode() == code && a.getVendorId() == vendorId) {
        it.remove();
      }
    }
  }

  // Private Methods ----------------------------------------------------- 

  private Message createShMessage(DiameterHeader diameterHeader, DiameterAvp[] avps) throws IllegalArgumentException {

    // List<DiameterAvp> list = (List<DiameterAvp>) this.avpList.clone();
    boolean isRequest = diameterHeader == null;
    Message msg = null;
    if (!isRequest) {
      Message raw = createMessage(diameterHeader, avps);
      raw.setProxiable(diameterHeader.isProxiable());
      raw.setRequest(false);
      raw.setReTransmitted(false); // just in case. answers never have T flag set
      msg = raw;
    }
    else {
      Message raw = createMessage(null, avps);
      raw.setProxiable(true);
      raw.setRequest(true);
      msg = raw;
    }
    // now now we msut add VendorSpecific?

    return msg;
  }

  protected Message createMessage(DiameterHeader header, DiameterAvp[] avps) throws AvpNotAllowedException {

    try {
      Message msg = createRawMessage(header);
      AvpSet set = msg.getAvps();

      for (DiameterAvp avp : avps) {
        addAvp(avp, set);
      }

      return msg;
    }
    catch (Exception e) {
      logger.error("Failed to create message.", e);
    }
    return null;
  }

  protected Message createRawMessage(DiameterHeader header) {

    int commandCode = 0;
    long endToEndId = 0;
    long hopByHopId = 0;
    if (header != null) {
      commandCode = header.getCommandCode();
      endToEndId = header.getEndToEndId();
      hopByHopId = header.getHopByHopId();
    }
    else {
      //FIXME: This is the only one ;[
      commandCode = PushNotificationRequest.commandCode;
      endToEndId = (long) (Math.random()*1000000);
      hopByHopId = (long) (Math.random()*1000000)+1;
    }
    try {
      return stack.getSessionFactory().getNewRawSession().createMessage(commandCode, shAppId, hopByHopId, endToEndId);
    }
    catch (InternalException e) {
      logger.error("Unable to create Raw Message.", e);
    }
    catch (IllegalDiameterStateException e) {
      logger.error("Unable to create Raw Message.", e);
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
    }
    else if (avp != null) {
      set.addAvp(avp.getCode(), avp.byteArrayValue(), avp.getVendorId(), avp.getMandatoryRule() == 1, avp.getProtectedRule() == 1);
    }
  }

  public void clean() {
    session = null;
    if (this.baseFactory != null) {
      this.baseFactory.clean();
    }
  }

  private void addOrigin(DiameterMessage msg) {
    if(!msg.hasOriginHost()) {
      msg.setOriginHost(new DiameterIdentity(stack.getMetaData().getLocalPeer().getUri().getFQDN().toString()));
    }
    if(!msg.hasOriginRealm()) {
      msg.setOriginRealm(new DiameterIdentity(stack.getMetaData().getLocalPeer().getRealmName()));
    }
  }
}
