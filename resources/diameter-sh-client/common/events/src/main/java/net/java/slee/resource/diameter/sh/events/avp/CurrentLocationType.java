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
 * AVP representation of Current-Location AVP. Defined in 3GPP TS 29.329 section
 * 6.3.8.<br>
 * The Current-Location AVP is of type Enumerated, and indicates whether an
 * active location retrieval has to be initiated or not: 
 * <pre>
 *      <b>DoNotNeedInitiateActiveLocationRetrieval (0)</b> : 
 *          The request indicates that the initiation of an active location retrieval is not required. 
 *      <b>InitiateActiveLocationRetrieval (1)</b> : 
 *          It is requested that an active location retrieval is initiated.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CurrentLocationType implements Serializable, Enumerated {

  /**
   * 
   */
  private static final long serialVersionUID = 1583473527497727782L;
  public static final int _DO_NOT_NEED_INITIATE_ACTIVE_LOCATION_RETRIEVAL = 0;
  public static final int _INITIATE_ACTIVE_LOCATION_RETRIEVAL = 1;

  /**
   * The request indicates that the initiation of an active location retrieval
   * is not required.
   */
  public static final CurrentLocationType DO_NOT_NEED_INITIATE_ACTIVE_LOCATION_RETRIEVAL = new CurrentLocationType(_DO_NOT_NEED_INITIATE_ACTIVE_LOCATION_RETRIEVAL);

  /**
   * It is requested that an active location retrieval is initiated.
   */
  public static final CurrentLocationType INITIATE_ACTIVE_LOCATION_RETRIEVAL = new CurrentLocationType(_INITIATE_ACTIVE_LOCATION_RETRIEVAL);

  private CurrentLocationType(int value) {
    this.value = value;
  }

  public static CurrentLocationType fromInt(int type) {
    switch (type) {
    case _DO_NOT_NEED_INITIATE_ACTIVE_LOCATION_RETRIEVAL:
      return DO_NOT_NEED_INITIATE_ACTIVE_LOCATION_RETRIEVAL;
    case _INITIATE_ACTIVE_LOCATION_RETRIEVAL:
      return INITIATE_ACTIVE_LOCATION_RETRIEVAL;
    default:
      throw new IllegalArgumentException("Invalid CurrentLocation value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
    case _DO_NOT_NEED_INITIATE_ACTIVE_LOCATION_RETRIEVAL:
      return "DO_NOT_NEED_INITIATE_ACTIVE_LOCATION_RETRIEVAL";
    case _INITIATE_ACTIVE_LOCATION_RETRIEVAL:
      return "INITIATE_ACTIVE_LOCATION_RETRIEVAL";
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
