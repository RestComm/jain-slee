/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package net.java.slee.resource.diameter.rf;

import java.io.IOException;

import net.java.slee.resource.diameter.base.CreateActivityException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rf.events.RfAccountingAnswer;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

/**
 * The SBB interface for the Diameter Rf Resource Adaptor.
 * 
 * This API can be used in either an asynchronous or synchronous manner.
 * 
 * To send messages asynchronously, create a RfClientSessionActivity using one of the createRfClientSessionActivity() methods.
 * 
 * To send messages synchronously, use the sendRfAccountingRequest(RfAccountingRequest) method.
 * 
 * The Accounting-Request messages must be created using the RfMessageFactory returned from getRfMessageFactory().
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RfProvider {

  /**
   * Return a message factory to be used to create concrete implementations of accounting messages.
   * 
   * @return a DiameterActivity 
   */
  public RfMessageFactory getRfMessageFactory();

  /**
   * Return an AVP factory to be used to create concrete implementations of accounting AVPs.
   * 
   * @return a DiameterActivity 
   */
  public RfAvpFactory getRfAvpFactory();

  /**
   * Create a new activity to send and receive Diameter messages.
   * 
   * @return
   * @throws CreateActivityException if the RA could not create the activity for any reason
   */
  public RfClientSessionActivity createRfClientSessionActivity() throws CreateActivityException;

  /**
   * Create a new activity to send and receive Diameter messages.
   * 
   * @param destinationHost a destination host to automatically put in all messages
   * @param destinationRealm a destination realm to automatically put in all messages 
   * @return
   * @throws CreateActivityException if the RA could not create the activity for any reason
   */
  public RfClientSessionActivity createRfClientSessionActivity(DiameterIdentity destinationHost, DiameterIdentity destinationRealm) throws CreateActivityException;

  /**
   * Send an Accounting Request.
   * 
   * @param accountingRequest the Accounting-Request message to send 
   * @return
   * @throws IllegalArgumentException if accountingRequest is missing any required AVPs
   * @throws IOException if the message could not be sent 
   */
  public RfAccountingAnswer sendRfAccountingRequest(RfAccountingRequest accountingRequest) throws IllegalArgumentException, IOException;

  /**
   * Return the number of peers this Diameter resource adaptor is connected
   * to.
   * 
   * @return connected peer count
   */
  int getPeerCount();

  /**
   * Returns array containing identities of connected peers
   * 
   * @return
   */
  DiameterIdentity[] getConnectedPeers();
}
