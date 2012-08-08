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
