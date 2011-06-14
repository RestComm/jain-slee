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

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.gq.events.GqAAAnswer;
import net.java.slee.resource.diameter.gq.events.GqAARequest;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionAnswer;
import net.java.slee.resource.diameter.gq.events.GqAbortSessionRequest;
import net.java.slee.resource.diameter.gq.events.GqReAuthAnswer;
import net.java.slee.resource.diameter.gq.events.GqReAuthRequest;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationAnswer;
import net.java.slee.resource.diameter.gq.events.GqSessionTerminationRequest;


/**
 * Used by applications to create Diameter Gq messages.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Yulian Oifa </a>
 */
public interface GqMessageFactory {

  public static final long _GQ_TGPP_VENDOR_ID = 10415L;
  public static final int _GQ_AUTH_APP_ID = 16777222;

  /**
   * Creates a Gq AA Request message.
   * 
   * @return
   */
  public GqAARequest createGqAARequest();

  /**
   * Creates a Gq AA Request message with the Session-Id AVP populated with the sessionId parameter.
   * 
   * @param sessionId
   * @return
   */
  public GqAARequest createGqAARequest(String sessionId);

  /**
   * Creates a Gq AA Answer message based on request
   * 
   * @param sessionId
   * @return
   */
  public GqAAAnswer createGqAAAnswer(GqAARequest aar);

  /**
   * Creates a Gq Abort Session Request message.
   * 
   * @return
   */
  public GqAbortSessionRequest createGqAbortSessionRequest();

  /**
   * Creates a Gq Abort Session message with the Session-Id AVP populated with the sessionId parameter.
   * 
   * @param sessionId
   * @return
   */
  public GqAbortSessionRequest createGqAbortSessionRequest(String sessionId);

  /**
   * Creates a Gq Abort Session Answer message based on request
   * 
   * @param sessionId
   * @return
   */
  public GqAbortSessionAnswer createGqAbortSessionAnswer(GqAbortSessionRequest asr);

  /**
   * Creates a Gq Re Auth Request message.
   * 
   * @return
   */
  public GqReAuthRequest createGqReAuthRequest();

  /**
   * Creates a Gq Re Auth Request message with the Session-Id AVP populated with the sessionId parameter.
   * 
   * @param sessionId
   * @return
   */
  public GqReAuthRequest createGqReAuthRequest(String sessionId);

  /**
   * Creates a Gq Re Auth Answer message based on request
   * 
   * @param sessionId
   * @return
   */
  public GqReAuthAnswer createGqReAuthAnswer(GqReAuthRequest rar);

  /**
   * Creates a Gq Session Termination Request message.
   * 
   * @return
   */
  public GqSessionTerminationRequest createGqSessionTerminationRequest();

  /**
   * Creates a Gq Session Termination Request message with the Session-Id AVP populated with the sessionId parameter.
   * 
   * @param sessionId
   * @return
   */
  public GqSessionTerminationRequest createGqSessionTerminationRequest(String sessionId);

  /**
   * Creates a Gq Session Termination Answer message based on request
   * 
   * @param sessionId
   * @return
   */
  public GqSessionTerminationAnswer createGqSessionTerminationAnswer(GqSessionTerminationRequest str);

  /**
   * 
   * @return Base Diameter message factory
   */
  public DiameterMessageFactory getBaseMessageFactory();

}
