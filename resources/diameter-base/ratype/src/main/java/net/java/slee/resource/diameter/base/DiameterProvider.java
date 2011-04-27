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

import net.java.slee.resource.diameter.Validator;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;

import java.io.IOException;

/**
 * 
 * The interface used by an SBB to interact with the Diameter RA.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface DiameterProvider {

  /**
   * Return a DiameterMessageFactory implementation to be used to create
   * {@link org.mobicents.slee.resource.diameter.base.DiameterMessage} objects.
   * 
   * @return a DiameterMessageFactory implementation
   */
  DiameterMessageFactory getDiameterMessageFactory();

  /**
   * Returns avp factory for base avp types and common types, like UNSIGNED_32
   * type and similar.
   * 
   * @return
   */
  DiameterAvpFactory getDiameterAvpFactory();

  /**
   * Create a new activity to send and receive Diameter messages.
   * 
   * @return a DiameterActivity
   * @throws CreateActivityException
   *             if the RA could not create the activity for any reason
   */
  DiameterActivity createActivity() throws CreateActivityException;

  /**
   * Create a new activity to send and receive Diameter messages.
   * 
   * @param destinationHost
   *            a destination host to automatically put in all messages
   * @param destinationRealm
   *            a destination realm to automatically put in all messages
   * @return a DiameterActivity
   * @throws CreateActivityException
   *             if the RA could not create the activity for any reason
   */
  DiameterActivity createActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException;

  /**
   * Create a new activity to send accounting request messages.
   * 
   * @return a AccountingClientSessionActivity 
   * @throws CreateActivityException if the RA could not create the activity for any reason
   */
  AccountingClientSessionActivity createAccountingClientSessionActivity() throws CreateActivityException;

  /**
   * Create a new activity to send accounting request messages.
   * 
   * @param destinationHost a destination host to automatically put in all messages
   * @param destinationRealm a destination realm to automatically put in all messages
   * @return a AccountingClientSessionActivity 
   * @throws CreateActivityException if the RA could not create the activity for any reason
   */
  AccountingClientSessionActivity createAccountingClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException ;

  /**
   * Synchronously send a Diameter request and block until a response is
   * received.
   * 
   * @param message
   *            the Diameter message to send
   * @return the Diameter message containing the response
   */
  DiameterMessage sendSyncRequest(DiameterMessage message) throws IOException;

  /**
   * Return the number of peers this Diameter resource adaptor is connected
   * to.
   * 
   * @return connected peer count
   */
  int getPeerCount();

  /**
   * Create a new activity to send authorization request messages.
   * 
   * @return a AccountingClientSessionActivity 
   * @throws CreateActivityException if the RA could not create the activity for any reason
   */
  AuthClientSessionActivity createAuthenticationClientSessionActivity() throws CreateActivityException;

  /**
   * Create a new activity to send authorization request messages.
   * 
   * @param destinationHost a destination host to automatically put in all messages
   * @param destinationRealm a destination realm to automatically put in all messages
   * @return a AccountingClientSessionActivity 
   * @throws CreateActivityException if the RA could not create the activity for any reason
   */
  AuthClientSessionActivity createAuthenticationClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm)throws CreateActivityException;

  /**
   * Returns array containing identities of connected peers 
   * 
   * @return
   */
  DiameterIdentity[] getConnectedPeers();

  public Validator getValidator();
}
