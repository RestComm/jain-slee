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

package net.java.slee.resource.diameter.ro;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.cca.events.avp.CcRequestType;
import net.java.slee.resource.diameter.ro.events.RoCreditControlRequest;

/**
 * An RoClientSessionActivity represents a charging control session for Credit
 * Control clients.
 * 
 * All requests for the session must be sent via the same
 * RoClientSessionActivity.
 * 
 * All responses related to the session will be received as events fired on the
 * same RoClientSessionActivity.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RoClientSessionActivity extends RoSessionActivity {

  /**
   * Send an event Credit-Control-Request.
   * 
   * @param ccr
   *            the CreditControlRequest to send
   * @throws IOException
   *             if an error occurred sending the request to the peer
   */
  public void sendEventRoCreditControlRequest(RoCreditControlRequest ccr) throws IOException;

  /**
   * Send an initial Credit-Control-Request.
   * 
   * @param ccr
   *            the RoCreditControlRequest to send
   * @throws IOException
   *             if an error occurred sending the request to the peer
   */
  public void sendInitialRoCreditControlRequest(RoCreditControlRequest ccr) throws IOException;

  /**
   * Send an update (intermediate) Credit-Control-Request.
   * 
   * @param ccr
   *            the RoCreditControlRequest to send
   * @throws IOException
   *             if an error occurred sending the request to the peer
   */
  public void sendUpdateRoCreditControlRequest(RoCreditControlRequest ccr) throws IOException;

  /**
   * Send a termination Credit-Control-Request.
   * 
   * @param ccr
   *            the RoCreditControlRequest to send
   * @throws IOException
   *             if an error occurred sending the request to the peer
   */
  public void sendTerminationRoCreditControlRequest(RoCreditControlRequest ccr) throws IOException;

  public abstract void sendReAuthAnswer(ReAuthAnswer raa) throws java.io.IOException;

  public RoCreditControlRequest createRoCreditControlRequest(CcRequestType type);
}
