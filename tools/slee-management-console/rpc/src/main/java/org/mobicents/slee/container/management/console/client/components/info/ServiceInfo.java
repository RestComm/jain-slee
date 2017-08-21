/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.container.management.console.client.components.info;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ServiceInfo extends ComponentInfo implements IsSerializable {

  private String addressProfileTable;

  private String resourceInfoProfileTable;

  private String rootSbbID;

  public ServiceInfo() {
    super();
  }

  public ServiceInfo(String name, String source, String vendor, String version, String ID, String deployableUnitID, String addressProfileTable,
      String resourceInfoProfileTable, String rootSbbID, String[] libraryRefs) {
    super(name, source, vendor, version, ID, deployableUnitID, libraryRefs);
    this.addressProfileTable = addressProfileTable;
    this.resourceInfoProfileTable = resourceInfoProfileTable;
    this.rootSbbID = rootSbbID;

    componentType = ComponentInfo.SERVICE;
  }

  public String getAddressProfileTable() {
    return addressProfileTable;
  }

  public String getResourceInfoProfileTable() {
    return resourceInfoProfileTable;
  }

  public String getRootSbbID() {
    return rootSbbID;
  }

}
