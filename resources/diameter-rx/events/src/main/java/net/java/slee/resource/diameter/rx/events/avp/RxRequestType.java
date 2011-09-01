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

package net.java.slee.resource.diameter.rx.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * The Rx-Request-Type AVP (AVP code 533) is of type Enumerated, and contains
 * the reason for sending the AA-Request message. The following values are
 * defined:
 * 
 * <pre>
 * INITIAL_REQUEST (0)
 *     An initial request is used to initiate an Rx session and contains information that is relevant to initiation.
 * UPDATE_REQUEST (1)
 *     An update request is used to update an existing Rx session.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum RxRequestType implements Enumerated{

  /**
   * An initial request is used to initiate an Rx session and contains information that is relevant to initiation.
   */
  INITIAL_REQUEST(0),

  /**
   * An update request is used to update an existing Rx session.
   */
  UPDATE_REQUEST(1);

  public static final int _INITIAL_REQUEST = INITIAL_REQUEST.getValue();
  public static final int _UPDATE_REQUEST = UPDATE_REQUEST.getValue();

  private int value = -1;

  private RxRequestType(int v) {
    this.value=v;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static RxRequestType fromInt(int type) throws IllegalArgumentException {
    switch (type) {
      case 0:
        return INITIAL_REQUEST;
      case 1:
        return UPDATE_REQUEST;

      default:
        throw new IllegalArgumentException();
    }
  }

  public int getValue() {
    return this.value;
  }

}
