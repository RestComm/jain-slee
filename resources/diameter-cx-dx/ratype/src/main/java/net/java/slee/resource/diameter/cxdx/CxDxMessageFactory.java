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

package net.java.slee.resource.diameter.cxdx;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;

import net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest;
import net.java.slee.resource.diameter.cxdx.events.MultimediaAuthenticationRequest;
import net.java.slee.resource.diameter.cxdx.events.PushProfileRequest;
import net.java.slee.resource.diameter.cxdx.events.RegistrationTerminationRequest;
import net.java.slee.resource.diameter.cxdx.events.ServerAssignmentRequest;
import net.java.slee.resource.diameter.cxdx.events.UserAuthorizationRequest;

/**
 *
 * Factory to support the creation of Diameter Cx/Dx messages.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface CxDxMessageFactory {

  /**
   * Create a UserAuthorizationRequest instance, populating it with the internal
   * AVPs not known or needed by the application.
   * 
   * @return a new UserAuthorizationRequest
   */
  UserAuthorizationRequest createUserAuthorizationRequest();

  /**
   * Create a UserAuthorizationRequest instance, populating it with the internal AVPs not known or
   * needed by the application. Use the session ID provided to find the Diameter session. This
   * should be used when the requests are being made synchronously and there is no 
   * CxDxClientSession available. 
   * 
   * @param sessionId the Session-Id
   * @return a new UserAuthorizationRequest
   * @throws IllegalArgumentException if sessionId is not a valid SessionID
   */
  UserAuthorizationRequest createUserAuthorizationRequest(String sessionId) throws IllegalArgumentException;

  /**
   * Create a ServerAssignmentRequest instance, populating it with the internal
   * AVPs not known or needed by the application.
   * 
   * @return a new ServerAssignmentRequest
   */
  ServerAssignmentRequest createServerAssignmentRequest();

  /**
   * Create a ServerAssignmentRequest instance, populating it with the internal AVPs not known or
   * needed by the application. Use the session ID provided to find the Diameter session. This
   * should be used when the requests are being made synchronously and there is no 
   * CxDxClientSession available. 
   * 
   * @param sessionId the Session-Id
   * @return a new ServerAssignmentRequest
   * @throws IllegalArgumentException if sessionId is not a valid SessionID
   */
  ServerAssignmentRequest createServerAssignmentRequest(String sessionId) throws IllegalArgumentException;

  /**
   * Create a LocationInfoRequest instance, populating it with the internal
   * AVPs not known or needed by the application.
   * 
   * @return a new LocationInfoRequest
   */
  LocationInfoRequest createLocationInfoRequest();

  /**
   * Create a LocationInfoRequest instance, populating it with the internal AVPs not known or
   * needed by the application. Use the session ID provided to find the Diameter session. This
   * should be used when the requests are being made synchronously and there is no 
   * CxDxClientSession available. 
   * 
   * @param sessionId the Session-Id
   * @return a new LocationInfoRequest
   * @throws IllegalArgumentException if sessionId is not a valid SessionID
   */
  LocationInfoRequest createLocationInfoRequest(String sessionId) throws IllegalArgumentException;

  /**
   * Create a MultimediaAuthenticationRequest instance, populating it with the internal
   * AVPs not known or needed by the application.
   * 
   * @return a new MultimediaAuthenticationRequest
   */
  MultimediaAuthenticationRequest createMultimediaAuthenticationRequest();

  /**
   * Create a MultimediaAuthenticationRequest instance, populating it with the internal AVPs not known or
   * needed by the application. Use the session ID provided to find the Diameter session. This
   * should be used when the requests are being made synchronously and there is no 
   * CxDxClientSession available. 
   * 
   * @param sessionId the Session-Id
   * @return a new MultimediaAuthenticationRequest
   * @throws IllegalArgumentException if sessionId is not a valid SessionID
   */
  MultimediaAuthenticationRequest createMultimediaAuthenticationRequest(String sessionId) throws IllegalArgumentException;

  /**
   * Create a RegistrationTerminationRequest instance, populating it with the internal
   * AVPs not known or needed by the application.
   * 
   * @return a new RegistrationTerminationRequest
   */
  RegistrationTerminationRequest createRegistrationTerminationRequest();

  /**
   * Create a RegistrationTerminationRequest instance, populating it with the internal AVPs not known or
   * needed by the application. Use the session ID provided to find the Diameter session. This
   * should be used when the requests are being made synchronously and there is no 
   * CxDxClientSession available. 
   * 
   * @param sessionId the Session-Id
   * @return a new RegistrationTerminationRequest
   * @throws IllegalArgumentException if sessionId is not a valid SessionID
   */
  RegistrationTerminationRequest createRegistrationTerminationRequest(String sessionId) throws IllegalArgumentException;

  /**
   * Create a PushProfileRequest instance, populating it with the internal
   * AVPs not known or needed by the application.
   * 
   * @return a new PushProfileRequest
   */
  PushProfileRequest createPushProfileRequest();

  /**
   * Create a PushProfileRequest instance, populating it with the internal AVPs not known or
   * needed by the application. Use the session ID provided to find the Diameter session. This
   * should be used when the requests are being made synchronously and there is no 
   * CxDxClientSession available. 
   * 
   * @param sessionId the Session-Id
   * @return a new PushProfileRequest
   * @throws IllegalArgumentException if sessionId is not a valid SessionID
   */
  PushProfileRequest createPushProfileRequest(String sessionId) throws IllegalArgumentException;

  /**
   * 
   * @return Base Diameter message factory
   */
  public DiameterMessageFactory getBaseMessageFactory();
}
