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
 * AVP representation of One-Time-Notification AVP. Defined in 3GPP TS 29.329 section 6.3.22.<br>
 * 
 * 6.3.22 One-Time-Notification AVP
 * The One-Time-Notification AVP is of type Enumerated. If present it indicates that
 * the sender requests to be notified only one time. The following values are defined:
 * <ul>
 * <li>ONE_TIME_NOTIFICATION_REQUESTED (0)</li>
 * </ul>
 * This AVP is only applicable to UE reachability for IP (23)
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class OneTimeNotificationType implements Serializable, Enumerated {

  private static final long serialVersionUID = 8726237939719241849L;

  public static final int _ONE_TIME_NOTIFICATION_REQUESTED = 0;

  /**
   * Singleton representation of {@link _ONE_TIME_NOTIFICATION_REQUESTED}
   */
  public static final OneTimeNotificationType ONE_TIME_NOTIFICATION_REQUESTED = new OneTimeNotificationType(_ONE_TIME_NOTIFICATION_REQUESTED);

  private OneTimeNotificationType(int value) {
    this.value = value;
  }

  public static OneTimeNotificationType fromInt(int type) {
    switch (type) {
      case _ONE_TIME_NOTIFICATION_REQUESTED:
        return ONE_TIME_NOTIFICATION_REQUESTED;
      default:
        throw new IllegalArgumentException("Invalid One-Time-Notification value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
      case _ONE_TIME_NOTIFICATION_REQUESTED:
        return "ONE_TIME_NOTIFICATION_REQUESTED";
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

  private int value;
}
