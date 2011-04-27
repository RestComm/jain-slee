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
 * AVP representation of Requested-Domain AVP. Defined in 3GPP TS 29.329 section
 * 6.3.7.<br>
 * The Requested-Domain AVP is of type Enumerated, and indicates the access
 * domain for which certain data (e.g. user state) are requested. The following
 * values are defined:
 * <ul>
 * <li>CS-Domain (0) - The requested data apply to the CS domain.</li>
 * <li>PS-Domain (1) - The requested data apply to the PS domain.</li>
 * </ul>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RequestedDomainType implements Serializable, Enumerated {

  private static final long serialVersionUID = 1L;

  /**
   * Int value equal in diameter message - it indicates domain for which certain data has been requested - CS - see TS29.328 for description
   */
  public static final int _CS_DOMAIN = 0;

  /**
   * Int value equal in diameter message - it indicates domain for which certain data has been requested - PS - see TS29.328 for description
   */
  public static final int _PS_DOMAIN = 1;

  /**
   * Singleton representation of {@link _CS_DOMAIN}
   */
  public static final RequestedDomainType CS_DOMAIN = new RequestedDomainType(_CS_DOMAIN);

  /**
   * Singleton representation of {@link _PS_DOMAIN}
   */
  public static final RequestedDomainType PS_DOMAIN = new RequestedDomainType(_PS_DOMAIN);

  private RequestedDomainType(int value) {
    this.value = value;
  }

  public static RequestedDomainType fromInt(int type) {
    switch (type) {
    case _CS_DOMAIN:
      return CS_DOMAIN;
    case _PS_DOMAIN:
      return PS_DOMAIN;
    default:
      throw new IllegalArgumentException("Invalid RequestedDomain value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
    case _CS_DOMAIN:
      return "CS_DOMAIN";
    case _PS_DOMAIN:
      return "PS_DOMAIN";
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
