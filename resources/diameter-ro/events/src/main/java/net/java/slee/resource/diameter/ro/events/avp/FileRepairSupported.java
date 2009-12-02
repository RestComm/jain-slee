package net.java.slee.resource.diameter.ro.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the FileRepairSupported enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class FileRepairSupported implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _SUPPORTED = 1;

  public static final int _NOT_SUPPORTED = 2;

  /**
   * The MBMS user service does support point-to-point file repair.
   */
  public static final FileRepairSupported SUPPORTED = new FileRepairSupported(_SUPPORTED);

  /**
   * The MBMS user service does not support point-to-point file repair.
   */
  public static final FileRepairSupported NOT_SUPPORTED = new FileRepairSupported(_NOT_SUPPORTED);

  private FileRepairSupported(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static  FileRepairSupported fromInt(int type) {
    switch(type) {
    case _SUPPORTED: 
      return SUPPORTED;
    case _NOT_SUPPORTED: 
      return NOT_SUPPORTED;

    default: 
      throw new IllegalArgumentException("Invalid FileRepairSupported value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _SUPPORTED: 
      return "SUPPORTED";
    case _NOT_SUPPORTED: 
      return "NOT_SUPPORTED";

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
