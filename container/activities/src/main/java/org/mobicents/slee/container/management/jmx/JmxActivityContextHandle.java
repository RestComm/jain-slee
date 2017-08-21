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

package org.mobicents.slee.container.management.jmx;

import java.io.Serializable;

import org.mobicents.slee.container.activity.ActivityType;

public class JmxActivityContextHandle implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String activitySource;
  private final ActivityType activityType;

  private transient int hashcode = 0;
  private transient String toString = null;
  private String activityHandleBase64;
  private String activityHandleToString;

  public JmxActivityContextHandle(ActivityType activityType, String activitySource, String activityHandleBase64, String activityHandleToString) {
    this.activityHandleBase64 = activityHandleBase64;
    this.activitySource = activitySource;
    this.activityType  = activityType;
    this.activityHandleToString = activityHandleToString;
  }

  public String getActivityHandleBase64() {
    return activityHandleBase64;
  }

  public String getActivityHandleToString() {
    return activityHandleToString;
  }

  public String getActivitySource() {
    return activitySource;
  }

  public ActivityType getActivityType() {
    return activityType;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (obj.getClass() == this.getClass()) {
      final JmxActivityContextHandle other = (JmxActivityContextHandle) obj;
      if (other.activityHandleBase64.equals(this.activityHandleBase64) && other.activityType == this.activityType) {
        // only compare the source if the activity type is external
        if (this.activityType == ActivityType.RA) {
          return other.activitySource.equals(this.activitySource);
        } else {
          return true;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    if (hashcode == 0) {
      final int prime = 31;
      int result = activityHandleBase64.hashCode();
      result = prime * result + activitySource.hashCode();
      result = prime * result + activityType.hashCode();
      hashcode = result;
    }
    return hashcode;
  }

  @Override
  public String toString() {
    if (toString == null) {
      toString = new StringBuilder ("ACH=").append(activityType).append('>').append(activitySource).append('>').append(activityHandleToString).toString(); 
    }
    return toString;
  }
}
