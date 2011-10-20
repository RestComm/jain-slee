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

package net.java.slee.resource.diameter.s6a;

import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest;
import net.java.slee.resource.diameter.s6a.events.CancelLocationRequest;
import net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.NotifyRequest;
import net.java.slee.resource.diameter.s6a.events.ResetRequest;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationRequest;
import net.java.slee.resource.diameter.s6a.events.PurgeUERequest;

/**
 * Factory to support the creation of Diameter S6a messages.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface S6aMessageFactory {

  /**
   * The S6a interface protocol is defined as an IETF vendor specific Diameter application, where
   * the vendor is 3GPP. The vendor identifier assigned by IANA to 3GPP 
   * (http://www.iana.org/assignments/enterprise-numbers) is 10415.
   */
  public static final long _S6A_VENDOR = 10415L;

  /**
   * The Diameter application identifier assigned to the S6a interface application is 16777251 
   * (allocated by IANA).
   */
  public static final long _S6A_AUTH_APP_ID = 16777251L;

  UpdateLocationRequest createUpdateLocationRequest();
  UpdateLocationRequest createUpdateLocationRequest(String sessionId) throws IllegalArgumentException;

  AuthenticationInformationRequest createAuthenticationInformationRequest();
  AuthenticationInformationRequest createAuthenticationInformationRequest(String sessionId) throws IllegalArgumentException;

  CancelLocationRequest createCancelLocationRequest();
  CancelLocationRequest createCancelLocationRequest(String sessionId) throws IllegalArgumentException;

  InsertSubscriberDataRequest createInsertSubscriberDataRequest();
  InsertSubscriberDataRequest createInsertSubscriberDataRequest(String sessionId) throws IllegalArgumentException;

  DeleteSubscriberDataRequest createDeleteSubscriberDataRequest();
  DeleteSubscriberDataRequest createDeleteSubscriberDataRequest(String sessionId) throws IllegalArgumentException;

  PurgeUERequest createPurgeUERequest();
  PurgeUERequest createPurgeUERequest(String sessionId) throws IllegalArgumentException;

  ResetRequest createResetRequest();
  ResetRequest createResetRequest(String sessionId) throws IllegalArgumentException;

  NotifyRequest createNotifyRequest();
  NotifyRequest createNotifyRequest(String sessionId) throws IllegalArgumentException;

}
