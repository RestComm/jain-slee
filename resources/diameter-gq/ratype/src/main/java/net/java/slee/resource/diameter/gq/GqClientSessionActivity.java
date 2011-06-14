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

import net.java.slee.resource.diameter.gq.events.GqAARequest;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest;
import net.java.slee.resource.diameter.gq.events.GqReAuthAnswer;
import net.java.slee.resource.diameter.gq.events.GqReAuthRequest;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest;


/**
 * An GqClientSessionActivity represents a GQ session for GQ clients
 * 
 * A single GqClientSessionActivity will be created for the Diameter session. All requests received for the session will be fired as events
 * on the same GqClientSessionActivity. All responses related to the session will be received as events fired on the same
 * RoClientSessionActivity.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GqClientSessionActivity extends GqSessionActivity {

  /**
   * Sends an AA-Request message to the peer.
   * 
   * @param aar
   *          the GqAARequest to send
   * @throws IOException
   *           if an error occurred sending the request to the peer
   */
  public void sendGqAARequest(GqAARequest aar) throws IOException;

  /**
   * Create a Gq-specific Re-Auth-Answer message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new GqReAuthAnswer
   */
  public GqReAuthAnswer createGqReAuthAnswer(GqReAuthRequest rar);

  /**
   * Sends a Re-Auth-Answer message to the peer.
   * 
   * @param raa
   *          the GqReAuthAnswer to send
   * @throws IOException
   *           if an error occurred sending the request to the peer
   */
  public void sendGqReAuthAnswer(GqReAuthAnswer raa) throws IOException;

  /**
   * Create a Gq-specific Abort-Session-Answer message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new GqAbortSessionAnswer
   */
  public GqAbortSessionAnswer createGqAbortSessionAnswer(GqAbortSessionRequest asr);

  /**
   * Sends a Abort-Session-Answer message to the peer.
   * 
   * @param asa
   *          the GqAbortSessionAnswer to send
   * @throws IOException
   *           if an error occurred sending the request to the peer
   */
  public void sendGqAbortSessionAnswer(GqAbortSessionAnswer asa) throws IOException;

  public void sendGqSessionTerminationRequest(GqSessionTerminationRequest str) throws IOException;
}
