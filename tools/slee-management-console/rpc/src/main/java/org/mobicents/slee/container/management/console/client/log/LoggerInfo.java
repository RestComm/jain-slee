/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.container.management.console.client.log;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author baranowb
 * 
 */
public class LoggerInfo implements IsSerializable {

  private boolean useParentHandlers = true;

  private String fullName = null;

  private String filterClass = null;

  private String level = null;

  private HandlerInfo[] handlerInfos = null;

  public LoggerInfo() {
    super();
  }

  public LoggerInfo(boolean useParentHandlers, String fullName, String filterClass, String level, HandlerInfo[] his) {
    super();
    this.useParentHandlers = useParentHandlers;
    this.fullName = fullName;
    this.filterClass = filterClass;
    this.level = level;
    this.handlerInfos = his;
  }

  public boolean isUseParentHandlers() {
    return useParentHandlers;
  }

  public String getFullName() {
    return fullName;
  }

  public String getFilterClass() {
    return filterClass;
  }

  public String getLevel() {
    return level;
  }

  public HandlerInfo[] getHandlerInfos() {
    return handlerInfos;
  }

  public void setUseParentHandlers(boolean useParentHandlers) {
    this.useParentHandlers = useParentHandlers;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setFilterClass(String filterClass) {
    this.filterClass = filterClass;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public void setHandlerInfos(HandlerInfo[] handlerInfos) {
    this.handlerInfos = handlerInfos;
  }

}
