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
