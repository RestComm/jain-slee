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
 * Java class representing the Pre-emption-Capability enumerated type.
 * From the Diameter Gx Reference Point Protocol Details (3GPP TS 29.212 V9.6.0) specification:
 * 
 * <pre>
 * 5.3.46  Pre-emption-Capability AVP
 * 
 * The Pre-emption-Capability AVP (AVP code 1047) is of type Enumerated. The AVP defines whether a 
 * service data flow can get resources that were already assigned to another service data flow with 
 * a lower priority level.
 * 
 * The following values are defined:
 *   PRE-EMPTION_CAPABILITY_ENABLED (0)
 * This value indicates that the service data flow is allowed to get resources that were already
 * assigned to another service data flow with a lower priority level.
 *   PRE-EMPTION_CAPABILITY_DISABLED (1)
 * This value indicates that the service data flow is not allowed to get resources that were 
 * already assigned to another service data flow with a lower priority level. This is the default 
 * value applicable if this AVP is not supplied.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class PreEmptionCapability implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _PRE_EMPTION_CAPABILITY_ENABLED = 0;
  public static final int _PRE_EMPTION_CAPABILITY_DISABLED = 1;

  public static final PreEmptionCapability PRE_EMPTION_CAPABILITY_ENABLED = new PreEmptionCapability(_PRE_EMPTION_CAPABILITY_ENABLED);
  public static final PreEmptionCapability PRE_EMPTION_CAPABILITY_DISABLED = new PreEmptionCapability(_PRE_EMPTION_CAPABILITY_DISABLED);

  private int value = -1;

  private PreEmptionCapability(int value) {
    this.value = value;
  }

  public static PreEmptionCapability fromInt(int type) {
    switch (type) {
      case _PRE_EMPTION_CAPABILITY_ENABLED:
        return PRE_EMPTION_CAPABILITY_ENABLED;
      case _PRE_EMPTION_CAPABILITY_DISABLED:
        return PRE_EMPTION_CAPABILITY_DISABLED;
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
      case _PRE_EMPTION_CAPABILITY_ENABLED:
        return "PRE-EMPTION_CAPABILITY_ENABLED";
      case _PRE_EMPTION_CAPABILITY_DISABLED:
        return "PRE-EMPTION_CAPABILITY_DISABLED";
      default:
        return "<Invalid Value>";
    }
  }
}