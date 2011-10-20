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

package net.java.slee.resource.diameter.s6a.events.avp;

import java.io.Serializable;
import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class representing the RAT-Type enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 5.3.31  RAT-Type AVP
 * 
 * The RAT-Type AVP (AVP code 1032) is of type Enumerated and is used to identify the radio access technology that is serving the UE. 
 * 
 *   NOTE 1:   Values 0-999 are used for generic radio access technologies that can apply to different IP-CAN types and are not IP-CAN specific.
 *   NOTE 2:   Values 1000-1999 are used for 3GPP specific radio access technology types.
 *   NOTE 3:   Values 2000-2999 are used for 3GPP2 specific radio access technology types.
 *   NOTE 4: The informative Annex C presents a mapping between the code values for different access network types.
 * 
 * The following values are defined:
 * 
 * WLAN (0)
 *   This value shall be used to indicate that the RAT is WLAN.
 * 
 * UTRAN (1000)
 *   This value shall be used to indicate that the RAT is UTRAN. For further details refer to 3GPP TS 29.060 [18].
 * 
 * GERAN (1001)
 *   This value shall be used to indicate that the RAT is GERAN. For further details refer to 3GPP TS 29.060 [18].
 * 
 * GAN (1002)
 *   This value shall be used to indicate that the RAT is GAN. For further details refer to 3GPP TS 29.060 [18] and 3GPP TS 43.318 [29].
 * 
 * HSPA_EVOLUTION (1003)
 *   This value shall be used to indicate that the RAT is HSPA Evolution. For further details refer to 3GPP TS 29.060 [18].
 * 
 * EUTRAN (1004)
 *   This value shall be used to indicate that the RAT is EUTRAN. For further details refer to 3GPP TS 29.274 [22]
 * 
 * CDMA2000_1X (2000)
 *   This value shall be used to indicate that the RAT is CDMA2000 1X. For further details refer to 3GPP2 X.S0011 [20].
 * 
 * HRPD (2001)
 *   This value shall be used to indicate that the RAT is HRPD. For further details refer to 3GPP2 X.S0011 [20].
 * 
 * UMB (2002)
 *   This value shall be used to indicate that the RAT is UMB. For further details refer to 3GPP2 X.S0011 [20].
 * 
 * EHRPD (2003)
 *   This value shall be used to indicate that the RAT is eHRPD. For further details refer to 3GPP2 X.S0057 [24].
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class RATType implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _WLAN = 0;
  public static final int _UTRAN = 1000;
  public static final int _GERAN = 1001;
  public static final int _GAN = 1002;
  public static final int _HSPA_EVOLUTION = 1003;
  public static final int _EUTRAN = 1004;
  public static final int _CDMA2000_1X = 2000;
  public static final int _HRPD = 2001;
  public static final int _UMB = 2002;
  public static final int _EHRPD = 2003;

  public static final RATType WLAN = new RATType(_WLAN);
  public static final RATType UTRAN = new RATType(_UTRAN);
  public static final RATType GERAN = new RATType(_GERAN);
  public static final RATType GAN = new RATType(_GAN);
  public static final RATType HSPA_EVOLUTION = new RATType(_HSPA_EVOLUTION);
  public static final RATType EUTRAN = new RATType(_EUTRAN);
  public static final RATType CDMA2000_1X = new RATType(_CDMA2000_1X);
  public static final RATType HRPD = new RATType(_HRPD);
  public static final RATType UMB = new RATType(_UMB);
  public static final RATType EHRPD = new RATType(_EHRPD);

  private int value = -1;

  private RATType(int value) {
    this.value = value;
  }

  public static RATType fromInt(int type) {
    switch (type) {
      case _WLAN:
        return WLAN;
      case _UTRAN:
        return UTRAN;
      case _GERAN:
        return GERAN;
      case _GAN:
        return GAN;
      case _HSPA_EVOLUTION:
        return HSPA_EVOLUTION;
      case _EUTRAN:
        return EUTRAN;
      case _CDMA2000_1X:
        return CDMA2000_1X;
      case _HRPD:
        return HRPD;
      case _UMB:
        return UMB;
      case _EHRPD:
        return EHRPD;
      default:
        throw new IllegalArgumentException("Invalid value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    switch (value) {
      case _WLAN:
        return "WLAN";
      case _UTRAN:
        return "UTRAN";
      case _GERAN:
        return "GERAN";
      case _GAN:
        return "GAN";
      case _HSPA_EVOLUTION:
        return "HSPA_EVOLUTION";
      case _EUTRAN:
        return "EUTRAN";
      case _CDMA2000_1X:
        return "CDMA2000_1X";
      case _HRPD:
        return "HRPD";
      case _UMB:
        return "UMB";
      case _EHRPD:
        return "EHRPD";
      default:
        return "<Invalid Value>";
    }
  }
}