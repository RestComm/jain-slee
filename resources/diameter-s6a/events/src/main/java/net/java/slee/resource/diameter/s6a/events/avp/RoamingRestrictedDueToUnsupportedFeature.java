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
 * Java class representing the Current-Location-Retrieved enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.81 Roaming-Restricted-Due-To-Unsupported-Feature
 * 
 * The Roaming-Restricted-Due-To-Unsupported-Feature AVP is of type Enumerated and indicates that 
 * roaming is restricted due to unsupported feature. The following value is defined:
 * 
 *   Roaming-Restricted-Due-To-Unsupported-Feature (0)
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoamingRestrictedDueToUnsupportedFeature implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE = 0;

  public static final RoamingRestrictedDueToUnsupportedFeature ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE = new RoamingRestrictedDueToUnsupportedFeature(_ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE);

  private int value = -1;

  private RoamingRestrictedDueToUnsupportedFeature(int value) {
    this.value = value;
  }

  public static RoamingRestrictedDueToUnsupportedFeature fromInt(int type) {
    switch (type) {
      case _ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE:
        return ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE;
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
      case _ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE:
        return "Roaming-Restricted-Due-To-Unsupported-Feature";
      default:
        return "<Invalid Value>";
    }
  }
}