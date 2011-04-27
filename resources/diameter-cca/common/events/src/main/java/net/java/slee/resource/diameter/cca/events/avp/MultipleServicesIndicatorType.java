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

package net.java.slee.resource.diameter.cca.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 *<pre> <b>8.40. Multiple-Services-Indicator AVP</b>
 *
 *
 *   The Multiple-Services-Indicator AVP (AVP Code 455) is of type
 *   Enumerated and indicates whether the Diameter credit-control client
 *   is capable of handling multiple services independently within a
 *   (sub-) session.  The absence of this AVP means that independent
 *   credit-control of multiple services is not supported.
 *
 *   A server not implementing the independent credit-control of multiple
 *   services MUST treat the Multiple-Services-Indicator AVP as an invalid
 *   AVP.
 *
 *   The following values are defined for the Multiple-Services-Indicator
 *   AVP:
 *
 *   MULTIPLE_SERVICES_NOT_SUPPORTED 0
 *      Client does not support independent credit-control of multiple
 *      services within a (sub-)session.
 *
 *   MULTIPLE_SERVICES_SUPPORTED     1
 *      Client supports independent credit-control of multiple services
 *      within a (sub-)session.
 *      
 *      </pre>
 *  
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public enum MultipleServicesIndicatorType implements Enumerated {

  MULTIPLE_SERVICES_NOT_SUPPORTED(0), MULTIPLE_SERVICES_SUPPORTED(1);

  private int value = -1;

  private MultipleServicesIndicatorType(int value) {
    this.value = value;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static MultipleServicesIndicatorType fromInt(int type) throws IllegalArgumentException
  {
    switch (type) {
    case 0:
      return MULTIPLE_SERVICES_NOT_SUPPORTED;
    case 1:
      return MULTIPLE_SERVICES_SUPPORTED;

    default:
      throw new IllegalArgumentException();
    }
  }

  public int getValue() {
    return this.value;
  }

}
