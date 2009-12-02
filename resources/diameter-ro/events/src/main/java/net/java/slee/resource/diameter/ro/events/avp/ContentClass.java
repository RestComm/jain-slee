package net.java.slee.resource.diameter.ro.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the ContentClass enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ContentClass implements Enumerated, Serializable{

  private static final long serialVersionUID = 1L;

  public static final int _TEXT = 0;

  public static final int _IMAGE_BASIC = 1;

  public static final int _IMAGE_RICH = 2;

  public static final int _VIDEO_BASIC = 3;

  public static final int _VIDEO_RICH = 4;

  public static final int _MEGAPIXEL = 5;

  public static final int _CONTENT_BASIC = 6;

  public static final int _CONTENT_RICH = 7;

  public static final ContentClass TEXT = new ContentClass(_TEXT);

  public static final ContentClass IMAGE_BASIC = new ContentClass(_IMAGE_BASIC);;

  public static final ContentClass IMAGE_RICH = new ContentClass(_IMAGE_RICH);;

  public static final ContentClass VIDEO_BASIC = new ContentClass(_VIDEO_BASIC);;

  public static final ContentClass VIDEO_RICH = new ContentClass(_VIDEO_RICH);;

  public static final ContentClass MEGAPIXEL = new ContentClass(_MEGAPIXEL);

  public static final ContentClass CONTENT_BASIC = new ContentClass(_CONTENT_BASIC);

  public static final ContentClass CONTENT_RICH = new ContentClass(_CONTENT_RICH);

  private ContentClass(int v) {
    this.value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static ContentClass fromInt(int type) {
    switch(type) {
    case _CONTENT_BASIC: return CONTENT_BASIC;

    case _CONTENT_RICH : return CONTENT_RICH;

    case _IMAGE_BASIC: return IMAGE_BASIC;

    case _IMAGE_RICH: return IMAGE_RICH;

    case _MEGAPIXEL: return MEGAPIXEL;

    case _TEXT: return TEXT;

    case _VIDEO_BASIC: return VIDEO_BASIC;

    case _VIDEO_RICH: return VIDEO_RICH;
    default: throw new IllegalArgumentException("Invalid DisconnectCause value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _CONTENT_BASIC: return "CONTENT_BASIC";

    case _CONTENT_RICH : return "CONTENT_RICH";

    case _IMAGE_BASIC: return "IMAGE_BASIC";

    case _IMAGE_RICH: return "IMAGE_RICH";

    case _MEGAPIXEL: return "MEGAPIXEL";

    case _TEXT: return "TEXT";

    case _VIDEO_BASIC: return "VIDEO_BASIC";

    case _VIDEO_RICH: return "VIDEO_RICH";
    default: return "<Invalid Value>";
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
