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
 * @author Stefano Zappaterra
 * 
 */
public class ResourceAdaptorInfo extends ComponentInfo implements IsSerializable {

  private String resourceAdaptorTypeID;
  private boolean supportsActiveReconfiguration;

  public ResourceAdaptorInfo(String name, String source, String vendor, String version, String ID, String deployableUnitID, String resourceAdaptorTypeID, boolean supportsActiveReconfiguration, String[] libraryRefs) {
    super(name, source, vendor, version, ID, deployableUnitID, libraryRefs);
    this.resourceAdaptorTypeID = resourceAdaptorTypeID;
    this.supportsActiveReconfiguration = supportsActiveReconfiguration;
    componentType = ComponentInfo.RESOURCE_ADAPTOR;
  }

  public ResourceAdaptorInfo() {
    super();
  }

  public String getResourceAdaptorTypeID() {
    return resourceAdaptorTypeID;
  }

  public boolean getSupportsActiveReconfiguration() {
    return supportsActiveReconfiguration;
  }
}
