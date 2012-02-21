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

package org.mobicents.slee.resource.diameter.cxdx;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;

import net.java.slee.resource.diameter.base.NoSuchAvpException;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cxdx.CxDxMessageFactory;
import net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest;
import net.java.slee.resource.diameter.cxdx.events.MultimediaAuthenticationRequest;
import net.java.slee.resource.diameter.cxdx.events.PushProfileRequest;
import net.java.slee.resource.diameter.cxdx.events.RegistrationTerminationRequest;
import net.java.slee.resource.diameter.cxdx.events.ServerAssignmentRequest;
import net.java.slee.resource.diameter.cxdx.events.UserAuthorizationRequest;
import net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes;

import org.apache.log4j.Logger;
import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Avp;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Message;
import org.jdiameter.api.Session;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.LocationInfoAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.LocationInfoRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.MultimediaAuthenticationAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.MultimediaAuthenticationRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.PushProfileAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.PushProfileRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.RegistrationTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.RegistrationTerminationRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.ServerAssignmentAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.ServerAssignmentRequestImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.UserAuthorizationAnswerImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.UserAuthorizationRequestImpl;

/**
 *
 * CxDxMessageFactoryImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CxDxMessageFactoryImpl extends DiameterMessageFactoryImpl implements CxDxMessageFactory {

  private static Logger logger = Logger.getLogger(CxDxMessageFactoryImpl.class);

  private DiameterAvpFactory baseAvpFactory = null;
  protected DiameterMessageFactory baseFactory = null;
  
  private ApplicationId cxdxAppId = ApplicationId.createByAuthAppId(DiameterCxDxAvpCodes.CXDX_VENDOR_ID, DiameterCxDxAvpCodes.CXDX_AUTH_APP_ID);

  private DiameterAvp[] EMPTY_AVP_ARRAY = new DiameterAvp[]{}; 

  /**
   * @param session
   * @param stack
   * @param avps
   */
  public CxDxMessageFactoryImpl(DiameterMessageFactory baseFactory,Session session, Stack stack, DiameterIdentity... avps) {
    super(session, stack, avps);

    this.baseAvpFactory = new DiameterAvpFactoryImpl();
    this.baseFactory = baseFactory;
  }

  /**
   * @param stack
   */
  public CxDxMessageFactoryImpl(DiameterMessageFactory baseFactory,Stack stack) {
    super(stack);   
    
    this.baseAvpFactory = new DiameterAvpFactoryImpl();
    this.baseFactory = baseFactory;
  }

  public void setApplicationId(long vendorId, long applicationId) {
    this.cxdxAppId = ApplicationId.createByAuthAppId(vendorId, applicationId);      
  }
  
  public ApplicationId getApplicationId() {
    return this.cxdxAppId;      
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createLocationInfoRequest()
   */
  public LocationInfoRequest createLocationInfoRequest() {
    try {
      return (LocationInfoRequest) createCxDxMessage(null, EMPTY_AVP_ARRAY, LocationInfoRequest.COMMAND_CODE, cxdxAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Location-Info-Request", e);
    }

    return null;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createLocationInfoRequest(java.lang.String)
   */
  public LocationInfoRequest createLocationInfoRequest(String sessionId) throws IllegalArgumentException {
    LocationInfoRequest lir = createLocationInfoRequest();
    lir.setSessionId(sessionId);

    return lir;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createMultimediaAuthenticationRequest()
   */
  public MultimediaAuthenticationRequest createMultimediaAuthenticationRequest() {
    try {
      return (MultimediaAuthenticationRequest) createCxDxMessage(null, EMPTY_AVP_ARRAY, MultimediaAuthenticationRequest.COMMAND_CODE, cxdxAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Multimedia-Authentication-Request", e);
    }

    return null;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createMultimediaAuthenticationRequest(java.lang.String)
   */
  public MultimediaAuthenticationRequest createMultimediaAuthenticationRequest(String sessionId) throws IllegalArgumentException {
    MultimediaAuthenticationRequest mar = createMultimediaAuthenticationRequest();
    mar.setSessionId(sessionId);

    return mar;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createPushProfileRequest()
   */
  public PushProfileRequest createPushProfileRequest() {
    try {
      return (PushProfileRequest) createCxDxMessage(null, EMPTY_AVP_ARRAY, PushProfileRequest.COMMAND_CODE, cxdxAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Push-Profile-Request", e);
    }

    return null;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createPushProfileRequest(java.lang.String)
   */
  public PushProfileRequest createPushProfileRequest(String sessionId) throws IllegalArgumentException {
    PushProfileRequest ppr = createPushProfileRequest();
    ppr.setSessionId(sessionId);

    return ppr;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createRegistrationTerminationRequest()
   */
  public RegistrationTerminationRequest createRegistrationTerminationRequest() {
    try {
      return (RegistrationTerminationRequest) createCxDxMessage(null, EMPTY_AVP_ARRAY, RegistrationTerminationRequest.COMMAND_CODE, cxdxAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Registration-Termination-Request", e);
    }

    return null;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createRegistrationTerminationRequest(java.lang.String)
   */
  public RegistrationTerminationRequest createRegistrationTerminationRequest(String sessionId) throws IllegalArgumentException {
    RegistrationTerminationRequest rtr = createRegistrationTerminationRequest();
    rtr.setSessionId(sessionId);

    return rtr;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createServerAssignmentRequest()
   */
  public ServerAssignmentRequest createServerAssignmentRequest() {
    try {
      return (ServerAssignmentRequest) createCxDxMessage(null, EMPTY_AVP_ARRAY, ServerAssignmentRequest.COMMAND_CODE, cxdxAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create Server-Assignment-Request", e);
    }

    return null;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createServerAssignmentRequest(java.lang.String)
   */
  public ServerAssignmentRequest createServerAssignmentRequest(String sessionId) throws IllegalArgumentException {
    ServerAssignmentRequest sar = createServerAssignmentRequest();
    sar.setSessionId(sessionId);

    return sar;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createUserAuthorizationRequest()
   */
  public UserAuthorizationRequest createUserAuthorizationRequest() {
    try {
      return (UserAuthorizationRequest) createCxDxMessage(null, EMPTY_AVP_ARRAY, UserAuthorizationRequest.COMMAND_CODE, cxdxAppId);
    }
    catch (InternalException e) {
      logger.error("Failed to create User-Authorization-Request", e);
    }

    return null;
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxMessageFactory#createUserAuthorizationRequest(java.lang.String)
   */
  public UserAuthorizationRequest createUserAuthorizationRequest(String sessionId) throws IllegalArgumentException {
    UserAuthorizationRequest uar = createUserAuthorizationRequest();
    uar.setSessionId(sessionId);

    return uar;
  }

  @Override
  public DiameterMessageFactory getBaseMessageFactory() 
  {
		return baseFactory;
  }
  
  /**
   * Creates a CxDx Message with specified command-code and avps filled. If a header is present an answer will be created, if not
   * it will generate a request.
   * 
   * @param diameterHeader
   * @param avps
   * @param _commandCode
   * @param appId
   * @return
   * @throws InternalException
   */
  DiameterMessage createCxDxMessage(DiameterHeader diameterHeader, DiameterAvp[] avps, int _commandCode, ApplicationId appId) throws  InternalException {

    boolean creatingRequest = diameterHeader == null;
    Message msg = null;

    if (!creatingRequest) {
      Message raw = createMessage(diameterHeader, avps, 0, appId);
      raw.setProxiable(diameterHeader.isProxiable());
      raw.setRequest(false);
      raw.setReTransmitted(false); // just in case. answers never have T flag set
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
    case LocationInfoRequest.COMMAND_CODE:
      diamMessage = creatingRequest ? new LocationInfoRequestImpl(msg) : new LocationInfoAnswerImpl(msg);
      break;
    case MultimediaAuthenticationRequest.COMMAND_CODE:
      diamMessage = creatingRequest ? new MultimediaAuthenticationRequestImpl(msg) : new MultimediaAuthenticationAnswerImpl(msg);
      break;
    case PushProfileRequest.COMMAND_CODE:
      diamMessage = creatingRequest ? new PushProfileRequestImpl(msg) : new PushProfileAnswerImpl(msg);
      break;
    case RegistrationTerminationRequest.COMMAND_CODE:
      diamMessage = creatingRequest ? new RegistrationTerminationRequestImpl(msg) : new RegistrationTerminationAnswerImpl(msg);
      break;
    case ServerAssignmentRequest.COMMAND_CODE:
      diamMessage = creatingRequest ? new ServerAssignmentRequestImpl(msg) : new ServerAssignmentAnswerImpl(msg);
      break;
    case UserAuthorizationRequest.COMMAND_CODE:
      diamMessage = creatingRequest ? new UserAuthorizationRequestImpl(msg) : new UserAuthorizationAnswerImpl(msg);
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

}
