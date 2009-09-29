package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.profile.ProfileTableImpl;

/**
 * 
 * JPAProfileTable.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
@Entity
@Table(name="JAINSLEEPROFILETABLES")
@NamedQueries({	
	@NamedQuery(name=JPAProfileTable.JPA_NAMED_QUERY_SELECT_ALL_TABLES,query="SELECT x FROM JPAProfileTable x"),
	@NamedQuery(name=JPAProfileTable.JPA_NAMED_QUERY_FIND_TABLE_BY_NAME,query="SELECT x FROM JPAProfileTable x WHERE x.profileTableName = :profileTableName"),
	@NamedQuery(name=JPAProfileTable.JPA_NAMED_QUERY_RENAME_TABLE,query="UPDATE JPAProfileTable x SET x.profileTableName = :newProfileTableName WHERE x.profileTableName = :oldProfileTableName"),
	@NamedQuery(name=JPAProfileTable.JPA_NAMED_QUERY_DELETE_TABLE,query="DELETE FROM JPAProfileTable x WHERE x.profileTableName = :profileTableName"),
	@NamedQuery(name=JPAProfileTable.JPA_NAMED_QUERY_SELECT_TABLES_BY_PROFILE_SPEC_ID,query="SELECT x FROM JPAProfileTable x WHERE x.profileSpecId = :profileSpecId")
})

public class JPAProfileTable implements Serializable {

  private static final long serialVersionUID = 5170225158725752562L;
  
  private static final String JPA_NAMED_QUERY_PREFIX = "SLEE_PROFILES_QUERY_";
  public static final String JPA_NAMED_QUERY_SELECT_ALL_TABLES = JPA_NAMED_QUERY_PREFIX + "selectAllTables";
  public static final String JPA_NAMED_QUERY_SELECT_TABLES_BY_PROFILE_SPEC_ID = JPA_NAMED_QUERY_PREFIX + "selectTablesByProfileSpecID";
  public static final String JPA_NAMED_QUERY_FIND_TABLE_BY_NAME = JPA_NAMED_QUERY_PREFIX + "findTableByName";
  public static final String JPA_NAMED_QUERY_RENAME_TABLE = JPA_NAMED_QUERY_PREFIX + "renameTable";
  public static final String JPA_NAMED_QUERY_DELETE_TABLE = JPA_NAMED_QUERY_PREFIX + "deleteTable";
  
  
  @Id
  private String profileTableName;
  private ProfileSpecificationID profileSpecId;

  private JPAProfileTable() {
  }
  
  public JPAProfileTable(ProfileTableImpl profileTable) {
    this();
    this.profileTableName = profileTable.getProfileTableName();
    this.profileSpecId = profileTable.getProfileSpecificationComponent().getProfileSpecificationID();
  }
  
  /**
   * @return the profileTableName
   */
  public String getProfileTableName() {
    return profileTableName;
  }

  /**
   * Sets 
   * @param profileTableName the profileTableName to set
   */
  public void setProfileTableName(String profileTableName) {
	  this.profileTableName = profileTableName;
  }

  /**
   * @return the profileSpecId
   */
  public ProfileSpecificationID getProfileSpecId() {
    return profileSpecId;
  }

  /**
   * Sets 
   * @param profileSpecId the profileSpecId to set
   */
  public void setProfileSpecId(ProfileSpecificationID profileSpecId) {
	  this.profileSpecId = profileSpecId;
  }

  @Override
  public String toString() {
    return "JPAProfileTable{ tableName=" + profileTableName + " , profileSpecId=" + profileSpecId + " }";
  }
}
