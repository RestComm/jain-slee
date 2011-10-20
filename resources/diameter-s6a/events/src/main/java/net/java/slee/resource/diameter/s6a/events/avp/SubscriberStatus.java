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

package net.java.slee.resource.diameter.s6a.events.avp;

import java.io.Serializable;
import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class representing the Subscriber-Status enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.29 Subscriber-Status
 * 
 * The 3GPP Subscriber Status AVP is of type Enumerated. It shall indicate if the service is barred
 * or granted. The following values are defined:
 * 
 *   SERVICE_GRANTED (0)
 *   
 *   OPERATOR_DETERMINED_BARRING (1)
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class SubscriberStatus implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _SERVICE_GRANTED = 0;
  public static final int _OPERATOR_DETERMINED_BARRING = 1;

  public static final SubscriberStatus SERVICE_GRANTED = new SubscriberStatus(_SERVICE_GRANTED);
  public static final SubscriberStatus OPERATOR_DETERMINED_BARRING = new SubscriberStatus(_OPERATOR_DETERMINED_BARRING);

  private int value = -1;

  private SubscriberStatus(int value) {
    this.value = value;
  }

  public static SubscriberStatus fromInt(int type) {
    switch (type) {
      case _SERVICE_GRANTED:
        return SERVICE_GRANTED;
      case _OPERATOR_DETERMINED_BARRING:
        return OPERATOR_DETERMINED_BARRING;
      default:
        throw new IllegalArgumentException("Invalid value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    switch (value) {
      case _SERVICE_GRANTED:
        return "SERVICE_GRANTED";
      case _OPERATOR_DETERMINED_BARRING:
        return "OPERATOR_DETERMINED_BARRING";
      default:
        return "<Invalid Value>";
    }
  }
}