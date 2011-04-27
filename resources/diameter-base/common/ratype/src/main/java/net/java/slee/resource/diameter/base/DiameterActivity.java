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

import java.io.IOException;
import java.io.Serializable;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;

/**
 * 
 * Represents a session with a Diameter peer. DiameterMessages (both requests
 * and responses) are received as events fired on DiameterActivity objects. 
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface DiameterActivity extends Serializable {

  /**
   * Return a DiameterMessageFactory implementation to be used to create
   * instances of {@link DiameterMessage} object to be fired on this
   * Activity. <br>
   * 
   * @return a DiameterMessageFactory implementation
   */
  DiameterMessageFactory getDiameterMessageFactory();

  /**
   * Returns a DiameterAvpFactory which can be used to create instances of  {@link DiameterAvp}.
   * 
   * @return a DiameterAvpFactory implementation
   */
  DiameterAvpFactory getDiameterAvpFactory();

  /**
   * Sends the given DiameterMessage on the DiameterActivity. The response to
   * the message (if any) will be fired on this activity. This method should
   * be used to send custom messages. Its application responsibility to
   * maintain FSM for those events.
   * 
   * @param message the Diameter message to send
   */
  void sendMessage(DiameterMessage message) throws IOException;

  /**
   * Return the Session ID for this activity.
   * 
   * @return the Session ID for this activity
   */
  String getSessionId();

  /**
   * Terminates underlying session
   */
  void endActivity();

}
