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

import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.ro.events.RoCreditControlAnswer;

/**
 * An RoServerSessionActivity represents a charging control session for Credit
 * Control servers.
 * 
 * A single RoServerSessionActivity will be created for the Diameter session.
 * All requests received for the session will be fired as events on the same
 * RoServerSessionActivity.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RoServerSessionActivity extends RoSessionActivity {

  /**
   * Create a Ro-specific Credit-Control-Answer message pre-populated with the
   * AVPs appropriate for this session.
   * 
   * @return a new CreditControlAnswer
   */
  public RoCreditControlAnswer createRoCreditControlAnswer();

  /**
   * Sends a Credit-Control-Answer message to the peer.
   * 
   * @param cca
   *            the CreditControlAnswer to send
   * @throws IOException
   *             if an error occured sending the request to the peer
   */
  public void sendRoCreditControlAnswer(RoCreditControlAnswer cca) throws IOException;

  public void sendReAuthRequest(ReAuthRequest rar) throws java.io.IOException;

}
