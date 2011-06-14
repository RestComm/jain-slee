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

package net.java.slee.resource.diameter.gq.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the Flow Usage enumerated type.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class FlowUsage implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _NO_INFORMATION = 0;
  public static final int _RTCP = 1;

  public static final FlowUsage NO_INFORMATION = new FlowUsage(_NO_INFORMATION);
  public static final FlowUsage RTCP = new FlowUsage(_RTCP);

  private int value = 0;

  private FlowUsage(int v) {
    value = v;
  }

  @Override
  public int getValue() {
    return value;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static FlowUsage fromInt(int type) {
    switch (type) {
      case _NO_INFORMATION:
        return NO_INFORMATION;
      case _RTCP:
        return RTCP;
      default:
        throw new IllegalArgumentException("Invalid Flow usage value: " + type);
    }
  }

  public String toString() {
    switch (value) {
      case _NO_INFORMATION:
        return "NO_INFORMATION";
      case _RTCP:
        return "RTCP";
      default:
        return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid flow usage found: " + value);
    }
  }
}
