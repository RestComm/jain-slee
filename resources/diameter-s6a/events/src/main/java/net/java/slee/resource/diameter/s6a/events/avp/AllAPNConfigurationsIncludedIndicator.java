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
 * Java class representing the All-APN-Configurations-Included-Indicator enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.33 All-APN-Configurations-Included-Indicator
 * 
 * The All-APN-Configurations-Included-Indicator AVP is of type Enumerated.  The following values are defined:
 *  All_APN_CONFIGURATIONS_INCLUDED (0)
 *  MODIFIED/ADDED_APN_CONFIGURATIONS_INCLUDED (1)
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class AllAPNConfigurationsIncludedIndicator implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _ALL_APN_CONFIGURATIONS_INCLUDED = 0;
  public static final int _MODIFIED_ADDED_APN_CONFIGURATIONS_INCLUDED = 1;

  public static final AllAPNConfigurationsIncludedIndicator ALL_APN_CONFIGURATIONS_INCLUDED = new AllAPNConfigurationsIncludedIndicator(_ALL_APN_CONFIGURATIONS_INCLUDED);
  public static final AllAPNConfigurationsIncludedIndicator MODIFIED_ADDED_APN_CONFIGURATIONS_INCLUDED = new AllAPNConfigurationsIncludedIndicator(_MODIFIED_ADDED_APN_CONFIGURATIONS_INCLUDED);

  private int value = -1;

  private AllAPNConfigurationsIncludedIndicator(int value) {
    this.value = value;
  }

  public static AllAPNConfigurationsIncludedIndicator fromInt(int type) {
    switch (type) {
      case _ALL_APN_CONFIGURATIONS_INCLUDED:
        return ALL_APN_CONFIGURATIONS_INCLUDED;
      case _MODIFIED_ADDED_APN_CONFIGURATIONS_INCLUDED:
        return MODIFIED_ADDED_APN_CONFIGURATIONS_INCLUDED;
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
      case _ALL_APN_CONFIGURATIONS_INCLUDED:
        return "UE_PRESENT";
      case _MODIFIED_ADDED_APN_CONFIGURATIONS_INCLUDED:
        return "UE_MEMORY_AVAILABLE";
      default:
        return "<Invalid Value>";
    }
  }
}