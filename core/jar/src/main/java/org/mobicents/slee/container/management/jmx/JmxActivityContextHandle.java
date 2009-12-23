package org.mobicents.slee.container.management.jmx;

import java.io.Serializable;

import org.mobicents.slee.runtime.activity.ActivityType;

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
