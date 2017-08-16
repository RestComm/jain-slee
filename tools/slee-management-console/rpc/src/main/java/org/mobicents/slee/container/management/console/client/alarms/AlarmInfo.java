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

package org.mobicents.slee.container.management.console.client.alarms;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Povilas Jurna
 *
 */
public class AlarmInfo implements IsSerializable {

  private String id;

  private String timestamp;

  private String level;

  private String type;

  private String instance;

  private Throwable cause;

  private String[] causeStringArray;

  private String message;

  public AlarmInfo(String id, String timestamp, String level, String type,
      String instance, Throwable cause, String[] causeStringArray, String message) {
    super();
    this.id = id;
    this.timestamp = timestamp;
    this.level = level;
    this.type = type;
    this.instance = instance;
    this.cause = cause;
    this.message = message;
    this.causeStringArray = causeStringArray;
  }

  public AlarmInfo() {
    super();
  }

  public String getId() {
    return id;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getLevel() {
    return level;
  }

  public String getType() {
    return type;
  }

  public String getInstance() {
    return instance;
  }

  public Throwable getCause() {
    return cause;
  }

  public String[] getCauseStringArray() {
    return causeStringArray;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "Alarm [id=" + id + ", timestamp=" + timestamp + ", level="
        + level + ", type=" + type + ", instance=" + instance + ", message="
        + message + "]";
  }

}
