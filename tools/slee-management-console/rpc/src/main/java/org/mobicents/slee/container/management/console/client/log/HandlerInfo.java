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

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author baranowb
 * 
 */
public class HandlerInfo implements IsSerializable {

  private int index = -1;
  private String name = null;

  private String filterClassName = null;

  private String formatterClassName = null;

  private String handelerClassName = null;

  private String level = null;

  private HashMap<String, String> otherOptions = null;

  public HandlerInfo() {
    super();
  }

  public HandlerInfo(int index, String name, String filterClassName, String formatterClassName, String handelerClassName, String level,
      HashMap<String, String> otherOptions) {
    super();
    this.index = index;
    this.name = name;
    this.filterClassName = filterClassName;
    this.formatterClassName = formatterClassName;
    this.handelerClassName = handelerClassName;
    this.level = level;
    this.otherOptions = otherOptions;
  }

  public int getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public String getFilterClassName() {
    return filterClassName;
  }

  public String getFormatterClassName() {
    return formatterClassName;
  }

  public String getHandelerClassName() {
    return handelerClassName;
  }

  public String getLevel() {
    return level;
  }

  public HashMap<String, String> getOtherOptions() {
    return otherOptions;
  }

}
