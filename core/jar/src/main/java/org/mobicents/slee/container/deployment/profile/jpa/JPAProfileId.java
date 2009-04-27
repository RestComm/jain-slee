package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JPAProfileId implements Serializable {

  private static final long serialVersionUID = 52944414592091037L;
   
  @Id
  private String tableName;
  
  @Id
  @Column(nullable=true)
  private String profileName;

  public JPAProfileId()
  {
    this.tableName = null;
    this.profileName = null;
  }
  
  public JPAProfileId(String tableName, String profileName)
  {
    this.tableName = tableName;
    this.profileName = profileName;
  }
  
  public String getProfileName()
  {
    return profileName;
  }
  
  public void setProfileName( String profileName )
  {
    this.profileName = profileName;
  }
  
  public String getTableName()
  {
    return tableName;
  }
  
  public void setTableName( String tableName )
  {
    this.tableName = tableName;
  }
  
  @Override
  public boolean equals( Object obj )
  {
    if(obj instanceof JPAProfileId)
    {
      JPAProfileId other = (JPAProfileId)obj;
      
      boolean pn = (this.profileName == other.profileName) || (this.profileName != null && this.profileName.equals(other.profileName));
      boolean tn = (this.tableName == other.tableName) || (this.tableName != null && this.tableName.equals(other.tableName));
      
      return pn && tn;
    }
    
    return false;
  }
  
  @Override
  public int hashCode()
  {
    return super.hashCode();
  }
}
