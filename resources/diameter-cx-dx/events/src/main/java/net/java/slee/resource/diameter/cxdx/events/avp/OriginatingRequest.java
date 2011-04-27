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

package net.java.slee.resource.diameter.cxdx.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * <pre>
 * <b>6.3.34 Originating-Request AVP</b>
 * The Originating-Request AVP is of type Enumerated and indicates to the HSS that the request is 
 * related to an AS originating SIP request in the Location-Information-Request operation. The 
 * following value is defined:
 * 
 *  ORIGINATING (0)
 *    This value informs the HSS that it should check originating unregistered services for the public identity.
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class OriginatingRequest implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _ORIGINATING = 0;

  public static final OriginatingRequest ORIGINATING = new OriginatingRequest(_ORIGINATING);

  private int value = 0;

  private OriginatingRequest(int value) {
    this.value = value;
  }

  public static OriginatingRequest fromInt(int type) {
    switch(type) {
    case _ORIGINATING: 
      return ORIGINATING;
    default: 
      throw new IllegalArgumentException("Invalid Originating-Request value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _ORIGINATING: 
      return "ORIGINATING";
    default: 
      return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

}
