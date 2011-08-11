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

package org.mobicents.eclipslee.servicecreation.preferences;

/**
 * Class representing a JAIN SLEE component entry for the preferences list
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ComponentEntry {

  private ComponentType type;

  private String groupId;
  private String artifactId;
  private String version;

  private String description;

  public ComponentEntry(ComponentType type, String groupId, String artifactId, String version, String description) {
    super();
    this.type = type;
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
    this.description = description.equals("") ? null : description;
  }

  public ComponentType getType() {
    return type;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public String getVersion() {
    return version;
  }

  public String getDescription() {
    return description;
  }

  protected enum ComponentType {
    ENABLER, RATYPE, LIBRARY;
    
    public static ComponentType fromString(String x) {
      if (x.equals("enablers") || x.equals("Enabler")) {
        return ENABLER;
      }
      else if (x.equals("resource-adaptor-types") || x.equals("Resource Adaptor Type")) {
        return RATYPE;
      }
      else if (x.equals("libraries") || x.equals("Library")) {
        return LIBRARY;
      }
      else {
        return valueOf(x);
      }
    }
  }
  
  @Override
  public int hashCode() {
    int hash = 1;
    hash = hash * 31 + artifactId.hashCode();
    hash = hash * 31 + groupId.hashCode();
    hash = hash * 31 + version.hashCode();
    hash = hash * 31 + type.ordinal();
    
    return hash;
  }
  
  @Override
  public boolean equals(Object object) {
    if (!(object instanceof ComponentEntry)) {
      return false;
    }

    ComponentEntry that = (ComponentEntry) object;
    
    if(this.type == that.type && this.groupId.equals(that.groupId) && 
        this.artifactId.equals(that.artifactId) && this.version.equals(that.version)) {
      return true;
    }
    
    return false;
  }
}
