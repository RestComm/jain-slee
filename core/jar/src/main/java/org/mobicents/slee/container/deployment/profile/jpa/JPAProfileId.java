package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JPAProfileId implements Serializable {

  private static final long serialVersionUID = 52944414592091037L;
   
  @Id
  private String tableName;
  
  @Id
  private String safeProfileName;

  public JPAProfileId()
  {
    this.tableName = null;
    this.safeProfileName = null;
  }
  
  public JPAProfileId(String tableName, String safeProfileName)
  {
    this.tableName = tableName;
    this.safeProfileName = safeProfileName;
  }
  
  public String getSafeProfileName()
  {
    return safeProfileName;
  }
  
  public void setSafeProfileName( String profileName )
  {
    this.safeProfileName = profileName;
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
    	return this.safeProfileName.equals(other.safeProfileName) && this.tableName.equals(other.tableName);
    }
    else {
    	return false;
    }
  }
  
  @Override
  public int hashCode()
  {
    return super.hashCode();
  }
}
