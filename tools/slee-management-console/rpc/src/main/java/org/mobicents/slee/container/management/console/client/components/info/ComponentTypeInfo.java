/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.console.client.components.info;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ComponentTypeInfo implements IsSerializable {

  final static public String EVENT_TYPE = "Event Type";

  final static public String PROFILE_SPECIFICATION = "Profile Specification";

  final static public String SBB = "SBB";

  final static public String RESOURCE_ADAPTOR_TYPE = "Resource Adaptor Type";

  final static public String RESOURCE_ADAPTOR = "Resource Adaptor";

  final static public String SERVICE = "Service";

  final static public String LIBRARY = "Library";

  private String type = "";

  private int componentNumber;

  public ComponentTypeInfo() {
  }

  public ComponentTypeInfo(String type, int componentNumber) {
    this.type = type;
    this.componentNumber = componentNumber;
  }

  public int getComponentNumber() {
    return componentNumber;
  }

  public String getType() {
    return type;
  }

  public String toString() {
    return "ComponentTypeInfo: type=[" + type + "] componentNumber=[" + componentNumber + "]";
  }

  /*
   * public ComponentTypeInfo clone() { return new ComponentTypeInfo(new String(type), componentNumber); }
   */
}
