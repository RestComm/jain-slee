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

package org.mobicents.slee.container.management.console.client.sbb.entity;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Vladimir Ralev
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbEntityInfo implements IsSerializable {

  String sbbEntityId;
  String rootId;
  String parentId;
  String serviceId;
  String priority;
  String sbbId;
  String serviceConvergenceName;
  String usageParameterPath;

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public String getRootId() {
    return rootId;
  }

  public void setRootId(String rootId) {
    this.rootId = rootId;
  }

  public String getSbbEntityId() {
    return sbbEntityId;
  }

  public void setSbbEntityId(String sbbEntityId) {
    this.sbbEntityId = sbbEntityId;
  }

  public String getSbbId() {
    return sbbId;
  }

  public void setSbbId(String sbbId) {
    this.sbbId = sbbId;
  }

  public String getServiceConvergenceName() {
    return serviceConvergenceName;
  }

  public void setServiceConvergenceName(String serviceConvergenceName) {
    this.serviceConvergenceName = serviceConvergenceName;
  }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

}
