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

package net.java.slee.resource.diameter.base;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;

public interface AuthServerSessionActivity extends AuthSessionActivity {

  /**
   * Create an Abort-Session-Request message populated with the following AVPs:
   * 
   *  * Auth-Application-Id: the value 0 as specified by RFC3588 
   * 
   * @return a new AbortSessionRequest
   */
  AbortSessionRequest createAbortSessionRequest();

  /**
   * Send session abort session request to client
   * 
   * @param request
   * @throws IOException 
   */
  void sendAbortSessionRequest(AbortSessionRequest request) throws IOException;

  /**
   * Create an Re-Auth-Request message populated with the following AVPs:
   * 
   *  * Auth-Application-Id: the value 0 as specified by RFC3588
   *  * Re-Auth-Request-Type: as per reAuthRequestType parameter
   * 
   * @param terminationCause
   * @return a new ReAuthRequest
   */
  ReAuthRequest createReAuthRequest(ReAuthRequestType reAuthRequestType);

  /**
   * Send Re-Auth-Request
   * 
   * @param request
   * @throws IOException 
   */
  void sendReAuthRequest(ReAuthRequest request) throws IOException;

  /**
   * Create an Session-Termination-Answer with the Auth-Application-Id set to 0.
   * 
   * @return
   */
  SessionTerminationAnswer createSessionTerminationAnswer();

  /**
   * Create an Session-Termination-Answer with some AVPs populated from the provided Session-Termination-Request.
   * 
   * @param sessionTerminationRequest
   * @return
   */
  SessionTerminationAnswer createSessionTerminationAnswer(SessionTerminationRequest sessionTerminationRequest);

  /**
   * Send session termination answer to client
   * 
   * @param request
   * @throws IOException 
   */
  void sendSessionTerminationAnswer(SessionTerminationAnswer request) throws IOException;

}
