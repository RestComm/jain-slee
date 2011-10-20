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
 * Java class representing the PDN-Type enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.62  PDN-Type
 * 
 * The PDN-Type AVP is of type Enumerated and indicates the address type of PDN. The following values are defined:
 *   IPv4 (0)
 * This value shall be used to indicate that the PDN can be accessed only in IPv4 mode.
 * 
 *   IPv6 (1) 
 * This value shall be used to indicate that the PDN can be accessed only in IPv6 mode.
 * 
 *   IPv4v6 (2)
 * This value shall be used to indicate that the PDN can be accessed both in IPv4 mode, in IPv6 
 * mode, and also from UEs supporting dualstack IPv4v6.
 * 
 *   IPv4_OR_IPv6 (3)
 * This value shall be used to indicate that the PDN can be accessed either in IPv4 mode, or in 
 * IPv6 mode, but not from UEs supporting dualstack IPv4v6. It should be noted that this value 
 * will never be used as a requested PDN Type from the UE, since UEs will only use one of their 
 * supported PDN Types, i.e., IPv4 only, IPv6 only or IPv4v6 (dualstack). This value is only used
 * as part of the APN subscription context, as an authorization mechanism between HSS and MME.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class PDNType implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _IPv4 = 0;
  public static final int _IPv6 = 1;
  public static final int _IPv4v6 = 2;
  public static final int _IPv4_OR_IPv6 = 3;

  public static final PDNType IPv4 = new PDNType(_IPv4);
  public static final PDNType IPv6 = new PDNType(_IPv6);
  public static final PDNType IPv4v6 = new PDNType(_IPv4v6);
  public static final PDNType IPv4_OR_IPv6 = new PDNType(_IPv4_OR_IPv6);

  private int value = -1;

  private PDNType(int value) {
    this.value = value;
  }

  public static PDNType fromInt(int type) {
    switch (type) {
      case _IPv4:
        return IPv4;
      case _IPv6:
        return IPv6;
      case _IPv4v6:
        return IPv4v6;
      case _IPv4_OR_IPv6:
        return IPv4_OR_IPv6;
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
      case _IPv4:
        return "IPv4";
      case _IPv6:
        return "IPv6";
      case _IPv4v6:
        return "IPv4v6";
      case _IPv4_OR_IPv6:
        return "IPv4_OR_IPv6";
      default:
        return "<Invalid Value>";
    }
  }
}