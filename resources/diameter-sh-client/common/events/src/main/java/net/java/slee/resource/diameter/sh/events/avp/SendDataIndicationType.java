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

package net.java.slee.resource.diameter.sh.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * 
 * AVP representation of Send-Data-Indication AVP. Defined in 3GPP TS 29.329
 * section 6.3.17.<br>
 * 
 * The Send-Data-Indication AVP is of type Enumerated. If present it indicates
 * that the sender requests the User-Data. The following values are defined:
 * <ul>
 * <li>USER_DATA_NOT_REQUESTED (0)</li>
 * <li>USER_DATA_REQUESTED (1)</li>
 * </ul>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class SendDataIndicationType implements Serializable, Enumerated {

  /**
   * 
   */
  private static final long serialVersionUID = 220559116111005191L;
  /**
   * Value indicating that user data has not been requested by sender
   */
  public static final int _USER_DATA_NOT_REQUESTED = 0;
  /**
   * Value indicating that user data has been requested by sender
   */
  public static final int _USER_DATA_REQUESTED = 1;

  /**
   * Static class representing 
   */
  public static final SendDataIndicationType USER_DATA_NOT_REQUESTED = new SendDataIndicationType(_USER_DATA_NOT_REQUESTED);

  /**
   * 
   */
  public static final SendDataIndicationType USER_DATA_REQUESTED = new SendDataIndicationType(_USER_DATA_REQUESTED);

  private SendDataIndicationType(int value) {
    this.value = value;
  }

  public static SendDataIndicationType fromInt(int type) {
    switch (type) {
      case _USER_DATA_NOT_REQUESTED:
        return USER_DATA_NOT_REQUESTED;
      case _USER_DATA_REQUESTED:
        return USER_DATA_REQUESTED;
      default:
        throw new IllegalArgumentException("Invalid SendDataIndication value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
      case _USER_DATA_NOT_REQUESTED:
        return "USER_DATA_NOT_REQUESTED";
      case _USER_DATA_REQUESTED:
        return "USER_DATA_REQUESTED";
      default:
        return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    } catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  private int value;
}
