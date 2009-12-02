package net.java.slee.resource.diameter.ro.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the LocationEstimateType enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class LocationEstimateType implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _CURRENT_LOCATION = 0;

  public static final int _CURRENT_LAST_KNOWN_LOCATION = 1;

  public static final int _INITIAL_LOCATION = 2;

  public static final int _ACTIVATE_DEFERRED_LOCATION = 3;

  public static final int _CANCEL_DEFERRED_LOCATION = 4;

  public static final LocationEstimateType CURRENT_LOCATION = new LocationEstimateType(_CURRENT_LOCATION);

  public static final LocationEstimateType CURRENT_LAST_KNOWN_LOCATION = new LocationEstimateType(_CURRENT_LAST_KNOWN_LOCATION);

  public static final LocationEstimateType INITIAL_LOCATION = new LocationEstimateType(_INITIAL_LOCATION);

  public static final LocationEstimateType ACTIVATE_DEFERRED_LOCATION = new LocationEstimateType(_ACTIVATE_DEFERRED_LOCATION);

  public static final LocationEstimateType CANCEL_DEFERRED_LOCATION = new LocationEstimateType(_CANCEL_DEFERRED_LOCATION);

  private LocationEstimateType(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static LocationEstimateType fromInt(int type) {
    switch(type) {
    case _ACTIVATE_DEFERRED_LOCATION:
      return ACTIVATE_DEFERRED_LOCATION;
    case _CANCEL_DEFERRED_LOCATION:
      return CANCEL_DEFERRED_LOCATION;
    case _CURRENT_LAST_KNOWN_LOCATION:
      return CURRENT_LAST_KNOWN_LOCATION;
    case _CURRENT_LOCATION:
      return CURRENT_LOCATION;
    case _INITIAL_LOCATION:
      return INITIAL_LOCATION;

    default:
      throw new IllegalArgumentException("Invalid LocationEstimateType value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _ACTIVATE_DEFERRED_LOCATION:
      return "ACTIVATE_DEFERRED_LOCATION";
    case _CANCEL_DEFERRED_LOCATION:
      return "CANCEL_DEFERRED_LOCATION";
    case _CURRENT_LAST_KNOWN_LOCATION:
      return "CURRENT_LAST_KNOWN_LOCATION";
    case _CURRENT_LOCATION:
      return "CURRENT_LOCATION";
    case _INITIAL_LOCATION:
      return "INITIAL_LOCATION";

    default:
      return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  private int value = 0;

}
