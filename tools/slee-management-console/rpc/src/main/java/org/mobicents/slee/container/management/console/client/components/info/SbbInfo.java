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
public class SbbInfo extends ComponentInfo implements IsSerializable {

  private String addressProfileSpecificationID;

  private String[] eventTypeIDs;

  private String[] profileSpecificationIDs;

  private String[] resourceAdaptorEntityLinks;

  private String[] resourceAdaptorTypeIDs;

  private String[] sbbIDs;

  private String[] envEntries;

  public SbbInfo() {
    super();
  }

  public SbbInfo(String name, String source, String vendor, String version, String ID, String deployableUnitID, String addressProfileSpecificationID,
      String[] eventTypeIDs, String[] profileSpecificationIDs, String[] resourceAdaptorEntityLinks, String[] resourceAdaptorTypeIDs, String[] sbbIDs, String[] libraryRefs) {
    super(name, source, vendor, version, ID, deployableUnitID, libraryRefs);
    this.addressProfileSpecificationID = addressProfileSpecificationID;
    this.eventTypeIDs = eventTypeIDs;
    this.profileSpecificationIDs = profileSpecificationIDs;
    this.resourceAdaptorEntityLinks = resourceAdaptorEntityLinks;
    this.resourceAdaptorTypeIDs = resourceAdaptorTypeIDs;
    this.sbbIDs = sbbIDs;

    componentType = ComponentInfo.SBB;
  }

  public String getAddressProfileSpecificationID() {
    return addressProfileSpecificationID;
  }

  public String[] getEventTypeIDs() {
    return eventTypeIDs;
  }

  public String[] getSbbIDs() {
    return sbbIDs;
  }

  public String[] getProfileSpecificationIDs() {
    return profileSpecificationIDs;
  }

  public String[] getResourceAdaptorEntityLinks() {
    return resourceAdaptorEntityLinks;
  }

  public String[] getResourceAdaptorTypeIDs() {
    return resourceAdaptorTypeIDs;
  }
  public String[] getEnvEntries() {
	  return envEntries;
  }
  public void setEnvEntries(String[] envEntries) {
	  this.envEntries = envEntries;
  }
}
