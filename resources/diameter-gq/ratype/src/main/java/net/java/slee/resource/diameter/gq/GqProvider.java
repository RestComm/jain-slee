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

package net.java.slee.resource.diameter.gq;

import java.io.IOException;


import net.java.slee.resource.diameter.Validator;
import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.gq.events.GqAAAnswer;
import net.java.slee.resource.diameter.gq.events.GqAARequest;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest;
import net.java.slee.resource.diameter.gq.events.GqReAuthAnswer;
import net.java.slee.resource.diameter.gq.events.GqReAuthRequest;

/**
 * The SBB interface for the Diameter Gq Resource Adaptor.
 * 
 * This API can be used in either an asynchronous or synchronous manner.
 * 
 * To send messages asynchronously, create a GqClientSessionActivity using one of the createGqClientSessionActivity() methods.
 * 
 * The Gq interface messages must be created using the GqMessageFactory returned from getGqMessageFactory().
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GqProvider {

  /**
   * Return a message factory to be used to create concrete implementations of credit control messages.
   * 
   * @return
   */
  public GqMessageFactory getGqMessageFactory();

  /**
   * Return a avp factory to be used to create concrete implementations of credit control AVPs.
   * 
   * @return
   */
  public GqAvpFactory getGqAvpFactory();

  /**
   * gets existing activity by session ID , returns null if does not exists
   */
  public GqClientSessionActivity getActivityBySessionID(String sessionID);

  /**
   * Create a new activity to send and receive Diameter messages.
   * 
   * @return a DiameterActivity
   * @throws CreateActivityException
   *           if the RA could not create the activity for any reason
   */
  public GqClientSessionActivity createGqClientSessionActivity() throws CreateActivityException;

  /**
   * Create a new activity to send and receive Diameter messages.
   * 
   * @param destinationHost
   *          a destination host to automatically put in all messages
   * @param destinationRealm
   *          a destination realm to automatically put in all messages
   * @return a DiameterActivity
   * @throws CreateActivityException
   *           if the RA could not create the activity for any reason
   */
  public GqClientSessionActivity createGqClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm)
      throws CreateActivityException;

  /**
   * Send a AA-Request message to the appropriate peers, and block until the response is received then return it.
   * 
   * @param aar
   *          the AARequest to send
   * @return the answer received
   * @throws IOException
   *           if an error occurred sending the request to the peer
   */
  public GqAAAnswer sendGqAARequest(GqAARequest aar) throws IOException;

  /**
   * Send a Abort-Session-Request message to the appropriate peers, and block until the response is received then return it.
   * 
   * @param asr
   *          the Abort-Session-Request to send
   * @return the answer received
   * @throws IOException
   *           if an error occurred sending the request to the peer
   */
  public GqAbortSessionAnswer sendGqAbortSessionRequest(GqAbortSessionRequest asr) throws IOException;

  /**
   * Send a Re-Auth-Request message to the appropriate peers, and block until the response is received then return it.
   * 
   * @param rar
   *          the Re-Auth-Request to send
   * @return the answer received
   * @throws IOException
   *           if an error occurred sending the request to the peer
   */
  public GqReAuthAnswer sendGqReAuthRequest(GqReAuthRequest rar) throws IOException;

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

  Validator getValidator();
}
