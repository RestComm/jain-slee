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

import net.java.slee.resource.diameter.gq.events.GqAAAnswer;
import net.java.slee.resource.diameter.gq.events.GqAARequest;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest;
import net.java.slee.resource.diameter.gq.events.GqReAuthRequest;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationAnswer;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest;


/**
 * An GqServerSessionActivity represents a GQ session for GQ servers
 * 
 * A single GqServerSessionActivity will be created for the Diameter session. All requests received for the session will be fired as events
 * on the same GqServerSessionActivity.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GqServerSessionActivity extends GqSessionActivity {

  /**
   * Create a Gq-specific AA-Answer message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new GqAAAnswer
   */
  public GqAAAnswer createGqAAAnswer(GqAARequest aar);

  /**
   * Sends a AA-Answer message to the peer.
   * 
   * @param aaa
   *          the GqAAAnswer to send
   * @throws IOException
   *           if an error occured sending the request to the peer
   */
  public void sendGqAAAnswer(GqAAAnswer aaa) throws IOException;

  public void sendReAuthRequest(GqReAuthRequest rar) throws java.io.IOException;

  /**
   * Create a Gq-specific Session-Termination-Answer message pre-populated with the AVPs appropriate for this session.
   * 
   * @return a new GqSessionTerminationAnswer
   */
  public GqSessionTerminationAnswer createGqSessionTerminationAnswer(GqSessionTerminationRequest str);

  /**
   * Sends a Session-Termination-Answer message to the peer.
   * 
   * @param sta
   *          the GqSessionTerminationAnswer to send
   * @throws IOException
   *           if an error occured sending the request to the peer
   */
  public void sendGqSessionTerminationAnswer(GqSessionTerminationAnswer sta) throws IOException;

  public void sendAbortSessionRequest(GqAbortSessionRequest asr) throws java.io.IOException;
}
