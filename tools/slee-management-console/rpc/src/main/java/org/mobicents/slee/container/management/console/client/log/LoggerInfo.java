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
