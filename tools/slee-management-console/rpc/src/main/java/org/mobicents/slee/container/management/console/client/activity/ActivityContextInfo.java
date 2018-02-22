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

package org.mobicents.slee.container.management.console.client.activity;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Vladimir Ralev
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ActivityContextInfo implements IsSerializable {

  private String id;

  private String activityClass;

  private String lastAccessTime;

  private String TTL;

  private String raEntityId;

  private String[] sbbAttachments;

  private String[] namesBoundTo;

  private String[] attachedTimers;

  private String[] dataAttributes;

  private String isEnding;
  
  public ActivityContextInfo(String id) {
    this.id = id;
  }

  public ActivityContextInfo() {
  }

  public String getId() {
    return id;
  }

  public String getActivityClass() {
    return activityClass;
  }

  public void setActivityClass(String activityClass) {
    this.activityClass = activityClass;
  }

  public String[] getAttachedTimers() {
    return attachedTimers;
  }

  public void setAttachedTimers(String[] attachedTimers) {
    this.attachedTimers = attachedTimers;
  }

  public String[] getDataAttributes() {
    return dataAttributes;
  }

  public void setDataAttributes(String[] dataAttributes) {
    this.dataAttributes = dataAttributes;
  }

  public String getLastAccessTime() {
    return lastAccessTime;
  }

  public void setLastAccessTime(String lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }

  public String[] getNamesBoundTo() {
    return namesBoundTo;
  }

  public void setNamesBoundTo(String[] namesBoundTo) {
    this.namesBoundTo = namesBoundTo;
  }

  public String[] getSbbAttachments() {
    return sbbAttachments;
  }

  public void setSbbAttachments(String[] sbbAttachments) {
    this.sbbAttachments = sbbAttachments;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRaEntityId() {
    return raEntityId;
  }

  public void setRaEntityId(String raId) {
    this.raEntityId = raId;
  }

  public String getTTL() {
    return TTL;
  }

  public void setTTL(String ttl) {
    TTL = ttl;
  }

  public String getIsEnding() {
	return isEnding;
  }

  public void setIsEnding(String isEnding) {
	this.isEnding = isEnding;
  }
  
}
