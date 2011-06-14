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

import java.io.StreamCorruptedException;

/**
 * Enum representing Authorization FSM States.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public enum GqSessionState {

  IDLE(0), PENDING(1), OPEN(2), DISCONNECTED(3);

  private int value = -1;

  /**
   * Constructor.
   * 
   * @param val
   *          the value to be initialized with.
   */
  private GqSessionState(int val) {
    this.value = val;
  }

  /**
   * Getter for the current FSM state as int.
   * 
   * @return an int corresponding to the actual FSM state.
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Getter for the current FSM state as enum.
   * 
   * @return enum object corresponding to the actual FSM state.
   * @throws StreamCorruptedException
   */
  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  /**
   * Converts a given state value into it's equivalent.
   * 
   * @param value
   *          int to be converted
   * @return the corresponding RoSessionState enum value
   * @throws IllegalArgumentException
   *           if value is not applicable for this enum
   */
  public GqSessionState fromInt(int value) throws IllegalArgumentException {
    switch (value) {
      case 0:
        return IDLE;
      case 1:
        return PENDING;
      case 3:
        return OPEN;
      case 4:
        return DISCONNECTED;
      default:
        throw new IllegalArgumentException("Unknown value for Session State: " + value);
    }
  }
}
