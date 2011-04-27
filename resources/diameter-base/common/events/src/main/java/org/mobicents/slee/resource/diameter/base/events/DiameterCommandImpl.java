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

package org.mobicents.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.DiameterCommand;

/**
 * 
 * Class implementing {@link DiameterCommand} interface.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DiameterCommandImpl implements DiameterCommand {

  private int code;
  private long applicationId;

  private String shortName = "undefined";
  private String longName = "undefined";

  private boolean request;
  private boolean proxiable;

  public DiameterCommandImpl(int code, int applicationId, boolean request, boolean proxiable) {
    this.code = code;
    this.applicationId = applicationId;
    this.request = request;
    this.proxiable = proxiable;
  }

  public DiameterCommandImpl(int code, long applicationId, String shortName, String longName, boolean request, boolean proxiable) {
    this.code = code;
    this.applicationId = applicationId;
    this.shortName = shortName;
    this.longName = longName;
    this.request = request;
    this.proxiable = proxiable;
  }

  public int getCode() {
    return code;
  }

  public long getApplicationId() {
    return applicationId;
  }

  public String getShortName() {
    return shortName;
  }

  public String getLongName() {
    return longName;
  }

  public boolean isRequest() {
    return request;
  }

  public boolean isProxiable() {
    return proxiable;
  }

  public String toString() {
    return "DiameterCommand : applicationId[" + getApplicationId() + "], " + "code[" + getCode() + "], " + "longName[" + longName + "], " + "shortName[" + shortName + "], " +
      "isProxiable[" + isProxiable() + "], " + "isRequest[" + isRequest() + "]";
  }

}
