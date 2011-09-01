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

package net.java.slee.resource.diameter.rx;

import java.io.IOException;

import net.java.slee.resource.diameter.Validator;
import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rx.events.*;

/**
 * The SBB interface for the Diameter Rx Resource Adaptor.
 *
 * This API can be used in either an asynchronous or synchronous manner.
 *
 * To send messages asynchronously, create a RxClientSessionActivity using one of the createRxClientSessionActivity() methods.
 *
 * The Rx request messages must be created using the RxMessageFactory returned from getRxMessageFactory().
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface RxProvider {

  /**
   * Return a message factory to be used to create concrete implementations of Rx messages.
   *
   * @return an instance of a RxMessageFactory.
   */
  RxMessageFactory getRxMessageFactory();

  /**
   * Return an AVP factory to be used to create concrete implementations of Rx AVPs.
   *
   * @return an AVP factory instance.
   */
  RxAvpFactory getRxAvpFactory();

  /**
   * Create a new activity to send and receive Diameter messages.
   *
   * @return a DiameterActivity
   * @throws CreateActivityException if the RA could not create the activity for any reason
   */
  RxClientSessionActivity createRxClientSessionActivity() throws CreateActivityException;

  /**
   * Create a new activity to send and receive Diameter messages.
   *
   * @param destinationHost a destination host to automatically put in all messages
   * @param destinationRealm a destination realm to automatically put in all messages
   * @return a DiameterActivity
   * @throws CreateActivityException if the RA could not create the activity for any reason
   */
  RxClientSessionActivity createRxClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException;

  /**
   * Send a AA-Request message to the appropriate peers, and block until the response is received then return it.
   *
   * @param aar the AARequest to send
   * @return the answer received
   * @throws IOException if an error occurred sending the request to the peer
   */
  AAAnswer sendAARequest(AARequest aar) throws IOException;

  /**
   * Send a AbortSessionRequest message to the appropriate peers, and block until the response is received then return it.
   *
   * @param asr the AbortSessionRequest to send
   * @return the answer received
   * @throws IOException if an error occurred sending the request to the peer
   */
  AbortSessionAnswer sendAbortSessionRequest(AbortSessionRequest asr) throws IOException;

  /**
   * Send a SessionTerminationRequest message to the appropriate peers, and block until the response is received then return it.
   *
   * @param str the SessionTerminationRequest to send
   * @return the answer received
   * @throws IOException if an error occurred sending the request to the peer
   */
  SessionTerminationAnswer sendSessionTerminationRequest(SessionTerminationRequest str) throws IOException;

  /**
   * Send a ReAuthRequest message to the appropriate peers, and block until the response is received then return it.
   *
   * @param rar the ReAuthRequest to send
   * @return the answer received
   * @throws IOException if an error occurred sending the request to the peer
   */
  ReAuthAnswer sendReAuthRequest(ReAuthRequest rar) throws IOException;

  /**
   * Return the number of peers this Diameter resource adaptor is connected to.
   *
   * @return connected peer count
   */
  int getPeerCount();

  /**
   * Returns array containing identities of connected peers.
   *
   * @return
   */
  DiameterIdentity[] getConnectedPeers();

  /**
   * Returns the Diameter Validator
   * 
   * @return
   */
  Validator getValidator();

}
