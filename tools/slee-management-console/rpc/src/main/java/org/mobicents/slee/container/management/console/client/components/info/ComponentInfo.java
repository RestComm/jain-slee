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
public abstract class ComponentInfo implements IsSerializable {

  final static public String EVENT_TYPE = "Event Type";
  final static public String PROFILE_SPECIFICATION = "Profile Specification";
  final static public String RESOURCE_ADAPTOR = "Resource Adaptor";
  final static public String RESOURCE_ADAPTOR_TYPE = "Resource Adaptor Type";
  final static public String SBB = "SBB";
  final static public String SERVICE = "Service";
  final static public String LIBRARY = "Library";

  protected String componentType = "Component";

  private String name;
  private String source;
  private String vendor;
  private String version;

  private String ID;
  private String deployableUnitID;
  
  private String[] libraryRefs;

  public ComponentInfo() {
  }

  public ComponentInfo(String name, String source, String vendor, String version, String ID, String deployableUnitID, String[] libraryRefs) {
    super();
    this.name = name;
    this.source = source;
    this.vendor = vendor;
    this.version = version;
    this.ID = ID;
    this.deployableUnitID = deployableUnitID;
    this.libraryRefs = libraryRefs;
  }

  public String getID() {
    return ID;
  }

  public String getDeployableUnitID() {
    return deployableUnitID;
  }

  public String getName() {
    return name;
  }

  public String getSource() {
    return source;
  }

  public String getVendor() {
    return vendor;
  }

  public String getVersion() {
    return version;
  }

  public String getComponentType() {
    return componentType;
  }
  
  public String[] getLibraryRefs() {
    return libraryRefs;
  }
}
