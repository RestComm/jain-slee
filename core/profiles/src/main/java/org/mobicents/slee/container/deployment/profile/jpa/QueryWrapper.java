package org.mobicents.slee.container.deployment.profile.jpa;

import java.util.ArrayList;


/**
 * QueryWrapper.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class QueryWrapper {

  public static String TABLE_ENTITY_IDENTIFIER = "<CLASS>";
  
  private String querySQL;
  
  private long maxMatches = -1;
  
  private boolean isReadOnly = false;
  
  private ArrayList<Object> dynamicParameters;
  
  public QueryWrapper(String querySQL, long maxMatches, boolean isReadOnly)
  {
    this.querySQL = querySQL;
    this.maxMatches = maxMatches;
    this.isReadOnly = isReadOnly;
  }
  
  public QueryWrapper(String querySQL, ArrayList<Object> dynamicParameters)
  {
    this.querySQL = querySQL;
    this.dynamicParameters = dynamicParameters;
  }
  
  public String getQueryNativeSQL(String tableEntityName)
  {
    return querySQL.replaceFirst( TABLE_ENTITY_IDENTIFIER, tableEntityName );
  }
  
  public String getQuerySQL(String tableEntityName)
  {
    return querySQL.substring(querySQL.indexOf("FROM <")).replaceFirst( TABLE_ENTITY_IDENTIFIER, tableEntityName );
  }
  
  public long getMaxMatches()
  {
    return maxMatches;
  }
  
  public boolean isReadOnly()
  {
    return isReadOnly;
  }
  
  public ArrayList<Object> getDynamicParameters()
  {
    return dynamicParameters;
  }
  
}
