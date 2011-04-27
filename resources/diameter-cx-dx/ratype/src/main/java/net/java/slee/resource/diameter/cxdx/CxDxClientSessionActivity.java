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

import java.io.IOException;

import net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest;
import net.java.slee.resource.diameter.cxdx.events.MultimediaAuthenticationRequest;
import net.java.slee.resource.diameter.cxdx.events.PushProfileAnswer;
import net.java.slee.resource.diameter.cxdx.events.RegistrationTerminationAnswer;
import net.java.slee.resource.diameter.cxdx.events.ServerAssignmentRequest;
import net.java.slee.resource.diameter.cxdx.events.UserAuthorizationRequest;

/**
 * 
 * Represents a CxDxClientSession session for Cx/Dx clients.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface CxDxClientSessionActivity extends CxDxSessionActivity {

  /**
   * Create a User-Authorization-Request message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new UserAuthorizationRequest
   */
  UserAuthorizationRequest createUserAuthorizationRequest();

  /**
   * Send an event User-Authorization-Request. An event containing the answer will be fired on this activity.
   * 
   * @param userAuthorizationRequest the User-Authorization-Request message to send
   * @throws IOException
   */
  void sendUserAuthorizationRequest(UserAuthorizationRequest userAuthorizationRequest) throws IOException;

  /**
   * Create a ServerAssignmentRequest message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new ServerAssignmentRequest
   */
  ServerAssignmentRequest createServerAssignmentRequest();

  /**
   * Send an event Registration-Termination-Request. An event containing the answer will be fired on this activity.
   * 
   * @param serverAssignmentRequest the Registration-Termination-Request message to send
   * @throws IOException
   */
  void sendServerAssignmentRequest(ServerAssignmentRequest serverAssignmentRequest) throws IOException;

  /**
   * Create a LocationInfoRequest message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new LocationInfoRequest
   */
  LocationInfoRequest createLocationInfoRequest();

  /**
   * Send an event Location-Info-Request. An event containing the answer will be fired on this activity.
   * 
   * @param locationInfoRequest the Location-Info-Request message to send
   * @throws IOException
   */
  void sendLocationInfoRequest(LocationInfoRequest locationInfoRequest) throws IOException;

  /**
   * Create a MultimediaAuthenticationRequest message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new MultimediaAuthenticationRequest
   */
  MultimediaAuthenticationRequest createMultimediaAuthenticationRequest();

  /**
   * Send an event Multimedia-Authentication-Request. An event containing the answer will be fired on this activity.
   * 
   * @param multimediaAuthenticationRequest the Multimedia-Authentication-Request message to send
   * @throws IOException
   */
  void sendMultimediaAuthenticationRequest(MultimediaAuthenticationRequest multimediaAuthenticationRequest) throws IOException;

  /**
   * Create a RegistrationTerminationRequest message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new RegistrationTerminationRequest
   */
  RegistrationTerminationAnswer createRegistrationTerminationAnswer();

  /**
   * Send an event Registration-Termination-Request. An event containing the answer will be fired on this activity.
   * 
   * @param registrationTerminationRequest the Registration-Termination-Request message to send
   * @throws IOException
   */
  void sendRegistrationTerminationAnswer(RegistrationTerminationAnswer registrationTerminationAnswer) throws IOException;

  /**
   * Create a Push-Profile-Answer populated with the AVPs appropriate for this session.
   * 
   * @return a new PushProfileAnswer
   */
  PushProfileAnswer createPushProfileAnswer();

  /**
   * Send an event Push-Profile-Answer in response to a Push-Profile-Request received on this activity.
   * 
   * @param PushProfileAnswer the Push-Profile-Answer message to send
   * @throws IOException
   */
  void sendPushProfileAnswer(PushProfileAnswer pushProfileAnswer) throws IOException;

}
