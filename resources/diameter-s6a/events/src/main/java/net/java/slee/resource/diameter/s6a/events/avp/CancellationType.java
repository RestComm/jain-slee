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
 * Java class representing the Cancellation-Type enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.24 Cancellation-Type
 * The Cancellation-Type AVP is of type Enumerated and indicates the type of cancellation. The following values are defined:
 * 
 *   ATTACHED_NOT_REACHABLE_FOR_PAGING (0)
 * This value is used when the Cancel Location is sent to the previous MME due to a received Update Location message from a new MME.
 * 
 *   ATTACHED_REACHABLE_FOR_PAGING (1)
 * This value is used when the Cancel Location is sent to the previous SGSN due to a received Update Location message from a new SGSN.
 * 
 *   CONNECTED_NOT_REACHABLE_FOR_PAGING (2)
 * This value is used when the Cancel Location is sent to the current MME or SGSN due to withdrawal of the user’s subscription by the HSS operator.
 * 
 *   CONNECTED_REACHABLE_FOR_PAGING (3)
 * This value is used by an IWF when interworking with a pre-Rel-8 HSS.
 * 
 *   NETWORK_DETERMINED_NOT_REACHABLE (4)
 * This value is used when the Cancel Location is sent to the MME or SGSN due to a received Update Location message during initial attach procedure from an SGSN or MME respectively.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class CancellationType implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _ATTACHED_NOT_REACHABLE_FOR_PAGING     = 0;
  public static final int _ATTACHED_REACHABLE_FOR_PAGING    = 1;
  public static final int _CONNECTED_NOT_REACHABLE_FOR_PAGING  = 2;
  public static final int _CONNECTED_REACHABLE_FOR_PAGING     = 3;
  public static final int _NETWORK_DETERMINED_NOT_REACHABLE = 4;

  public static final CancellationType ATTACHED_NOT_REACHABLE_FOR_PAGING = new CancellationType(_ATTACHED_NOT_REACHABLE_FOR_PAGING);
  public static final CancellationType ATTACHED_REACHABLE_FOR_PAGING = new CancellationType(_ATTACHED_REACHABLE_FOR_PAGING);
  public static final CancellationType CONNECTED_NOT_REACHABLE_FOR_PAGING = new CancellationType(_CONNECTED_NOT_REACHABLE_FOR_PAGING);
  public static final CancellationType CONNECTED_REACHABLE_FOR_PAGING = new CancellationType(_CONNECTED_REACHABLE_FOR_PAGING);
  public static final CancellationType NETWORK_DETERMINED_NOT_REACHABLE = new CancellationType(_NETWORK_DETERMINED_NOT_REACHABLE);

  private int value = -1;

  private CancellationType(int value) {
    this.value = value;
  }

  public static CancellationType fromInt(int type) {
    switch (type) {
      case _ATTACHED_NOT_REACHABLE_FOR_PAGING:
        return ATTACHED_NOT_REACHABLE_FOR_PAGING;
      case _ATTACHED_REACHABLE_FOR_PAGING:
        return ATTACHED_REACHABLE_FOR_PAGING;
      case _CONNECTED_NOT_REACHABLE_FOR_PAGING:
        return CONNECTED_NOT_REACHABLE_FOR_PAGING;
      case _CONNECTED_REACHABLE_FOR_PAGING:
        return CONNECTED_REACHABLE_FOR_PAGING;
      case _NETWORK_DETERMINED_NOT_REACHABLE:
        return NETWORK_DETERMINED_NOT_REACHABLE;
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
      case _ATTACHED_NOT_REACHABLE_FOR_PAGING:
        return "ATTACHED_NOT_REACHABLE_FOR_PAGING";
      case _ATTACHED_REACHABLE_FOR_PAGING:
        return "ATTACHED_REACHABLE_FOR_PAGING";
      case _CONNECTED_NOT_REACHABLE_FOR_PAGING:
        return "CONNECTED_NOT_REACHABLE_FOR_PAGING";
      case _CONNECTED_REACHABLE_FOR_PAGING:
        return "CONNECTED_REACHABLE_FOR_PAGING";
      case _NETWORK_DETERMINED_NOT_REACHABLE:
        return "NETWORK_DETERMINED_NOT_REACHABLE";
      default:
        return "<Invalid Value>";
    }
  }

}