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

package org.mobicents.slee.container.management.console.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Stefano
 * 
 */
public class ManagementConsoleException extends Exception implements IsSerializable {

  private static final long serialVersionUID = -5186026198385897339L;

  private String message;

  public ManagementConsoleException() {
    super();
  }

  public ManagementConsoleException(Exception e) {
    message = e.getMessage();
  }

  public ManagementConsoleException(String message) {
    super(message);
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
