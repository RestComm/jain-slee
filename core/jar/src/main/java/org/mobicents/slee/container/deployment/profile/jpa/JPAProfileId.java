package org.mobicents.slee.container.deployment.profile.jpa;

import java.io.Serializable;

import javax.persistence.Id;

public class JPAProfileId implements Serializable {

  private static final long serialVersionUID = 52944414592091037L;
   
  @Id String tableName;
  @Id String profileName;
}
