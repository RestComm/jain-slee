package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.mobicents.slee.container.profile.ProfileTableImpl;

/**
 * 
 * JPAProfileTable.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
@Entity
@Table(name="JAINSLEEPROFILETABLES")
public class JPAProfileTable implements Serializable {

  private static final long serialVersionUID = 5170225158725752562L;
  
  @Id
  private String profileTableName;
  private String profileSpecId;

  private JPAProfileTable() {
  }
  
  public JPAProfileTable(ProfileTableImpl profileTable) {
    this();
    this.profileTableName = profileTable.getProfileTableName();
    this.profileSpecId = profileTable.getProfileSpecificationComponent().getProfileSpecificationID().toString();
  }
  
  /**
   * @return the profileTableName
   */
  public String getProfileTableName() {
    return profileTableName;
  }

  /**
   * @return the profileSpecId
   */
  public String getProfileSpecId() {
    return profileSpecId;
  }
  
  @Override
  public String toString() {
    return "JPAProfileTable{tableName=" + profileTableName + "; profileSpecId=" + profileSpecId + "}";
  }
}
