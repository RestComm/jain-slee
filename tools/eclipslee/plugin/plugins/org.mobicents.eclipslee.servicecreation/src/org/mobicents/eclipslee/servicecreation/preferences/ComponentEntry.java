package org.mobicents.eclipslee.servicecreation.preferences;

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
