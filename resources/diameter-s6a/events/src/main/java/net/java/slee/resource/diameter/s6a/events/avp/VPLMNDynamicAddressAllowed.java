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
 * Java class representing the VPLMN-Dynamic-Address-Allowed enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.38 VPLMN-Dynamic-Address-Allowed
 * 
 * The VPLMN Dynamic Address Allowed AVP is of type Enumerated. It shall indicate whether for this
 * APN, the UE is allowed to use the PDN GW in the domain of the HPLMN only, or additionally, the
 * PDN GW in the domain of the VPLMN.. If this AVP is not present, this means that the UE is not
 * allowed to use PDN GWs in the domain of the VPLMN. The following values are defined:
 * 
 *   NOTALLOWED (0)
 * 
 *   ALLOWED (1)
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class VPLMNDynamicAddressAllowed implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _NOTALLOWED = 0;
  public static final int _ALLOWED = 1;

  public static final VPLMNDynamicAddressAllowed NOTALLOWED = new VPLMNDynamicAddressAllowed(_NOTALLOWED);
  public static final VPLMNDynamicAddressAllowed ALLOWED = new VPLMNDynamicAddressAllowed(_ALLOWED);

  private int value = -1;

  private VPLMNDynamicAddressAllowed(int value) {
    this.value = value;
  }

  public static VPLMNDynamicAddressAllowed fromInt(int type) {
    switch (type) {
      case _NOTALLOWED:
        return NOTALLOWED;
      case _ALLOWED:
        return ALLOWED;
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
      case _NOTALLOWED:
        return "NOTALLOWED";
      case _ALLOWED:
        return "ALLOWED";
      default:
        return "<Invalid Value>";
    }
  }
}