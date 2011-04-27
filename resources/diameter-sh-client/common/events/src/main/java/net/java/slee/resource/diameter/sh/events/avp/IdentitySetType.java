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
 * AVP representation of Identity-Set AVP. Defined in 3GPP TS 29.329 section
 * 6.3.10.<br>
 * The Identity-Set AVP is of type Enumerated and indicates the requested set of
 * IMS Public Identities. The following values are defined:
 * <ul>
 * <li>ALL_IDENTITIES (0)</li>
 * <li>REGISTERED_IDENTITIES (1)</li>
 * <li>IMPLICIT_IDENTITIES (2)</li>
 * <li>ALIAS_IDENTITIES (3)</li>
 * </ul>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class IdentitySetType implements Serializable, Enumerated {

  private static final long serialVersionUID = -1735412334098260950L;

  /**
   * Int value equal in diameter message - it indicates IMS identities - All - see TS29.328 for description
   */
  public static final int _ALL_IDENTITIES = 0;

  /**
   * Int value equal in diameter message - it indicates IMS identities - only registered - see TS29.328 for description
   */
  public static final int _REGISTERED_IDENTITIES = 1;

  /**
   * Int value equal in diameter message - it indicates IMS identities - implicit - see TS29.328 for description
   */
  public static final int _IMPLICIT_IDENTITIES = 2;
  
  /**
   * Int value equal in diameter message - it indicates IMS identities - alias - see TS29.328 for description
   */
  public static final int _ALIAS_IDENTITIES = 3;

  /**
   * Singleton representation of {@link _ALL_IDENTITIES}
   */
  public static final IdentitySetType ALL_IDENTITIES = new IdentitySetType(_ALL_IDENTITIES);

  /**
   * Singleton representation of {@link _REGISTERED_IDENTITIES}
   */
  public static final IdentitySetType REGISTERED_IDENTITIES = new IdentitySetType(_REGISTERED_IDENTITIES);

  /**
   * Singleton representation of {@link _IMPLICIT_IDENTITIES}
   */
  public static final IdentitySetType IMPLICIT_IDENTITIES = new IdentitySetType(_IMPLICIT_IDENTITIES);

  /**
   * Singleton representation of {@link _ALIAS_IDENTITIES}
   */
  public static final IdentitySetType ALIAS_IDENTITIES = new IdentitySetType(_ALIAS_IDENTITIES); // Added for Rel-9 compliance

  private IdentitySetType(int value) {
    this.value = value;
  }

  public static IdentitySetType fromInt(int type) {
    switch (type) {
    case _ALL_IDENTITIES:
      return ALL_IDENTITIES;
    case _REGISTERED_IDENTITIES:
      return REGISTERED_IDENTITIES;
    case _IMPLICIT_IDENTITIES:
      return IMPLICIT_IDENTITIES;
    case _ALIAS_IDENTITIES: // Added for Rel-9 compliance
      return ALIAS_IDENTITIES;
    default:
      throw new IllegalArgumentException("Invalid IdentitySet value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
    case _ALL_IDENTITIES:
      return "ALL_IDENTITIES";
    case _REGISTERED_IDENTITIES:
      return "REGISTERED_IDENTITIES";
    case _IMPLICIT_IDENTITIES:
      return "IMPLICIT_IDENTITIES";
    case _ALIAS_IDENTITIES:
      return "ALIAS_IDENTITIES"; // Added for Rel-9 compliance
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
