package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JPAProfileId implements Serializable {

  private static final long serialVersionUID = 52944414592091037L;
   
  @Id
  @Column(name="tableName")
  private String tableName;
  
  @Id
  @Column(name="profileName")
  private String profileName;

  public JPAProfileId()
  {
    this.tableName = null;
    this.profileName = null;
  }
  
  public JPAProfileId(String tableName, String safeProfileName)
  {
    this.tableName = tableName;
    this.profileName = safeProfileName;
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
  public boolean equals( Object obj ) {
    if (obj != null && obj.getClass() == this.getClass()) {
    	JPAProfileId other = (JPAProfileId)obj;
    	return this.profileName.equals(other.profileName) && this.tableName.equals(other.tableName);
    }
    else {
    	return false;
    }
  }
  
  @Override
  public int hashCode()
  {
    return profileName.hashCode()*31+tableName.hashCode();
  }
  
  @Override
  public String toString()
  {
    return this.getClass().getName() + "(TableName[" + this.tableName + "] ProfileName[" + this.profileName + "])";
  }
}
