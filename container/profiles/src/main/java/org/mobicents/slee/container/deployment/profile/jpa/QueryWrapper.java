/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
