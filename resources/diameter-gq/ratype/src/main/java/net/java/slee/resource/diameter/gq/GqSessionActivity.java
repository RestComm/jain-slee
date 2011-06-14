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

/**
 * GqSessionActivity.java
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GqSessionActivity {

  /**
   * Provides session state information. Gq session must conform to Authorization FSM as described in <a
   * href="link http://rfc.net/rfc3588.html">section 8.1 of rfc3588</a>
   * 
   * @return instance of {@link GqSessionState}
   */
  public GqSessionState getState();

  /**
   * Return a message factory to be used to create concrete implementations of gq messages.
   * 
   * @return
   */
  public GqMessageFactory getGqMessageFactory();

  /**
   * Returns the session ID of the authorization session, which uniquely identifies the session.
   * 
   * @return the session ID
   */
  public String getSessionId();
}
