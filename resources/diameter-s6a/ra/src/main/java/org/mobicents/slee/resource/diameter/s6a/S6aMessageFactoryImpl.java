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

package org.mobicents.slee.resource.diameter.s6a;

import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.s6a.S6aMessageFactory;
import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest;
import net.java.slee.resource.diameter.s6a.events.CancelLocationRequest;
import net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.NotifyRequest;
import net.java.slee.resource.diameter.s6a.events.PurgeUERequest;
import net.java.slee.resource.diameter.s6a.events.ResetRequest;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationRequest;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Session;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.s6a.events.AuthenticationInformationAnswerImpl;
import org.mobicents.slee.resource.diameter.s6a.events.AuthenticationInformationRequestImpl;
import org.mobicents.slee.resource.diameter.s6a.events.CancelLocationAnswerImpl;
import org.mobicents.slee.resource.diameter.s6a.events.CancelLocationRequestImpl;
import org.mobicents.slee.resource.diameter.s6a.events.DeleteSubscriberDataAnswerImpl;
import org.mobicents.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequestImpl;
import org.mobicents.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswerImpl;
import org.mobicents.slee.resource.diameter.s6a.events.InsertSubscriberDataRequestImpl;
import org.mobicents.slee.resource.diameter.s6a.events.NotifyAnswerImpl;
import org.mobicents.slee.resource.diameter.s6a.events.NotifyRequestImpl;
import org.mobicents.slee.resource.diameter.s6a.events.PurgeUEAnswerImpl;
import org.mobicents.slee.resource.diameter.s6a.events.PurgeUERequestImpl;
import org.mobicents.slee.resource.diameter.s6a.events.ResetAnswerImpl;
import org.mobicents.slee.resource.diameter.s6a.events.ResetRequestImpl;
import org.mobicents.slee.resource.diameter.s6a.events.UpdateLocationAnswerImpl;
import org.mobicents.slee.resource.diameter.s6a.events.UpdateLocationRequestImpl;

/**
 * Diameter S6a Reference Point Message Factory. Implementation for {@link S6aMessageFactory}
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class S6aMessageFactoryImpl extends DiameterMessageFactoryImpl implements S6aMessageFactory {

  private static Logger logger = Logger.getLogger(S6aMessageFactoryImpl.class);

  private DiameterAvp[] EMPTY_AVP_ARRAY = new DiameterAvp[]{};

  // S6a: Vendor-Specific-Application-Id is set as optional and may be discarded in future releases; No Auth-Application-Id either;
  private ApplicationId s6aAppId = ApplicationId.createByAuthAppId(_S6A_VENDOR, _S6A_AUTH_APP_ID);

  /**
   * @param session
   * @param stack
   * @param avps
   */
  public S6aMessageFactoryImpl(Session session, Stack stack, DiameterIdentity... avps) {
    super(session, stack, avps);
  }

  /**
   * @param stack
   */
  public S6aMessageFactoryImpl(Stack stack) {
    super(stack);
  }

  public void setApplicationId(long vendorId, long applicationId) {
    this.s6aAppId = ApplicationId.createByAuthAppId(vendorId, applicationId);      
  }
  
  public ApplicationId getApplicationId() {
    return this.s6aAppId;      
  }
  
  /**
   * Creates a S6a Message with specified command-code and avps filled. If a header is present an answer will be created, if not
   * it will generate a request.
   * 
   * @param diameterHeader
   * @param avps
   * @param _commandCode
   * @param appId
   * @return
   * @throws InternalException
   */
  DiameterMessage createS6aMessage(DiameterHeader diameterHeader, DiameterAvp[] avps, int _commandCode, ApplicationId appId) throws InternalException {

    boolean creatingRequest = diameterHeader == null;
    Message msg = null;

    if (!creatingRequest) {
      Message raw = createMessage(diameterHeader, avps, 0, appId);
      raw.setProxiable(diameterHeader.isProxiable());
      raw.setRequest(false);
      raw.setReTransmitted(false); // just in case. answers never have T flag set
      // FIXME ? raw.getAvps().removeAvp(Avp.AUTH_APPLICATION_ID);
      msg = raw;
    }
    else {
      Message raw = createMessage(diameterHeader, avps, _commandCode, appId);
      raw.setProxiable(true);
      raw.setRequest(true);
      msg = raw;
    }

    int commandCode = creatingRequest ? _commandCode : diameterHeader.getCommandCode();
    DiameterMessage diamMessage = null;

    switch (commandCode) {
      case UpdateLocationRequest.COMMAND_CODE:
        diamMessage = creatingRequest ? new UpdateLocationRequestImpl(msg) : new UpdateLocationAnswerImpl(msg);
        break;
      case AuthenticationInformationRequest.COMMAND_CODE:
        diamMessage = creatingRequest ? new AuthenticationInformationRequestImpl(msg) : new AuthenticationInformationAnswerImpl(msg);
        break;
      case CancelLocationRequest.COMMAND_CODE:
        diamMessage = creatingRequest ? new CancelLocationRequestImpl(msg) : new CancelLocationAnswerImpl(msg);
        break;
      case InsertSubscriberDataRequest.COMMAND_CODE:
        diamMessage = creatingRequest ? new InsertSubscriberDataRequestImpl(msg) : new InsertSubscriberDataAnswerImpl(msg);
        break;
      case DeleteSubscriberDataRequest.COMMAND_CODE:
        diamMessage = creatingRequest ? new DeleteSubscriberDataRequestImpl(msg) : new DeleteSubscriberDataAnswerImpl(msg);
        break;
      case PurgeUERequest.COMMAND_CODE:
        diamMessage = creatingRequest ? new PurgeUERequestImpl(msg) : new PurgeUEAnswerImpl(msg);
        break;
      case ResetRequest.COMMAND_CODE:
        diamMessage = creatingRequest ? new ResetRequestImpl(msg) : new ResetAnswerImpl(msg);
        break;
      case NotifyRequest.COMMAND_CODE:
        diamMessage = creatingRequest ? new NotifyRequestImpl(msg) : new NotifyAnswerImpl(msg);
        break;
      default:
        diamMessage = new ExtensionDiameterMessageImpl(msg);
    }

    // Finally, add Origin-Host and Origin-Realm, if not present.
    // FIXME: Alex: needed? addOriginHostAndRealm(diamMessage);

    if (!diamMessage.hasSessionId() && session != null) {
      diamMessage.setSessionId(session.getSessionId());
    }

    return diamMessage;
  }

  public AuthenticationInformationRequest createAuthenticationInformationRequest() {
    try {
      return (AuthenticationInformationRequest) createS6aMessage(null, EMPTY_AVP_ARRAY, AuthenticationInformationRequest.COMMAND_CODE, s6aAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Authentication-Information-Request", e);
    }

    return null;
  }

  public AuthenticationInformationRequest createAuthenticationInformationRequest(String sessionId) throws IllegalArgumentException {
    AuthenticationInformationRequest uar = createAuthenticationInformationRequest();
    uar.setSessionId(sessionId);
    return uar;
  }

  public CancelLocationRequest createCancelLocationRequest() {
    try {
      return (CancelLocationRequest) createS6aMessage(null, EMPTY_AVP_ARRAY, CancelLocationRequest.COMMAND_CODE, s6aAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Cancel-Location-Request", e);
    }

    return null;
  }

  public CancelLocationRequest createCancelLocationRequest(String sessionId) throws IllegalArgumentException {
    CancelLocationRequest clr = createCancelLocationRequest();
    clr.setSessionId(sessionId);
    return clr;
  }

  public PurgeUERequest createPurgeUERequest() {
    try {
      return (PurgeUERequest) createS6aMessage(null, EMPTY_AVP_ARRAY, PurgeUERequest.COMMAND_CODE, s6aAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Purge-UE-Request", e);
    }
    return null;
  }

  public PurgeUERequest createPurgeUERequest(String sessionId) throws IllegalArgumentException {
    PurgeUERequest uar = createPurgeUERequest();
    uar.setSessionId(sessionId);
    return uar;
  }

  public UpdateLocationRequest createUpdateLocationRequest() {
    try {
      return (UpdateLocationRequest) createS6aMessage(null, EMPTY_AVP_ARRAY, UpdateLocationRequest.COMMAND_CODE, s6aAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Update-Location-Request", e);
    }
    return null;
  }

  public UpdateLocationRequest createUpdateLocationRequest(String sessionId) throws IllegalArgumentException {
    UpdateLocationRequest uar = createUpdateLocationRequest();
    uar.setSessionId(sessionId);
    return uar;
  }

  public InsertSubscriberDataRequest createInsertSubscriberDataRequest() {
    try {
      return (InsertSubscriberDataRequest) createS6aMessage(null, EMPTY_AVP_ARRAY, InsertSubscriberDataRequest.COMMAND_CODE, s6aAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Insert-Subscriber-Data-Request", e);
    }
    return null;
  }

  public InsertSubscriberDataRequest createInsertSubscriberDataRequest(String sessionId) throws IllegalArgumentException {
    InsertSubscriberDataRequest idr = createInsertSubscriberDataRequest();
    idr.setSessionId(sessionId);
    return idr;
  }

  public DeleteSubscriberDataRequest createDeleteSubscriberDataRequest() {
    try {
      return (DeleteSubscriberDataRequest) createS6aMessage(null, EMPTY_AVP_ARRAY, DeleteSubscriberDataRequest.COMMAND_CODE, s6aAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Delete-Subscriber-Data-Request", e);
    }
    return null;
  }

  public DeleteSubscriberDataRequest createDeleteSubscriberDataRequest(String sessionId) throws IllegalArgumentException {
    DeleteSubscriberDataRequest dsr = createDeleteSubscriberDataRequest();
    dsr.setSessionId(sessionId);
    return dsr;
  }

  public ResetRequest createResetRequest() {
    try {
      return (ResetRequest) createS6aMessage(null, EMPTY_AVP_ARRAY, ResetRequest.COMMAND_CODE, s6aAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Reset-Request", e);
    }
    return null;
  }

  public ResetRequest createResetRequest(String sessionId) throws IllegalArgumentException {
    ResetRequest rsr = createResetRequest();
    rsr.setSessionId(sessionId);
    return rsr;
  }

  public NotifyRequest createNotifyRequest() {
    try {
      return (NotifyRequest) createS6aMessage(null, EMPTY_AVP_ARRAY, NotifyRequest.COMMAND_CODE, s6aAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Notify-Request", e);
    }
    return null;
  }

  public NotifyRequest createNotifyRequest(String sessionId) throws IllegalArgumentException {
    NotifyRequest nor = createNotifyRequest();
    nor.setSessionId(sessionId);
    return nor;
  }

}
