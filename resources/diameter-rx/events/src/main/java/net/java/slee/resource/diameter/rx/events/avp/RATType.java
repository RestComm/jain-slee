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

package net.java.slee.resource.diameter.rx.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * The RAT-Type AVP (AVP code 1032) is of type Enumerated and is used to
 * identify the radio access technology that is serving the UE. NOTE 1: Values
 * 0-999 are used for generic radio access technologies that can apply to
 * different IP-CAN types and are not IP-CAN specific. NOTE 2: Values 1000-1999
 * are used for 3GPP specific radio access technology types. NOTE 3: Values
 * 2000-2999 are used for 3GPP2 specific radio access technology types. NOTE 4:
 * The informative Annex C presents a mapping between the code values for
 * different access network types. <br>
 * 
 * <br>
 * The following values are defined:
 * 
 * <pre>
 * WLAN (0)
 *   This value shall be used to indicate that the RAT is WLAN.   
 * VIRTUAL (1)
 *   This value shall be used to indicate that the RAT is unknown. For further details refer to 3GPP TS 29.274 [22].
 * UTRAN (1000)
 *   This value shall be used to indicate that the RAT is UTRAN. For further details refer to 3GPP TS 29.060 [18].
 * GERAN (1001)
 *   This value shall be used to indicate that the RAT is GERAN. For further details refer to 3GPP TS 29.060 [18].
 * GAN (1002)
 *   This value shall be used to indicate that the RAT is GAN. For further details refer to 3GPP TS 29.060 [18] and 3GPP TS 43.318 [29].
 * HSPA_EVOLUTION (1003)
 *   This value shall be used to indicate that the RAT is HSPA Evolution. For further details refer to 3GPP TS 29.060 [18].
 * EUTRAN (1004)
 *   This value shall be used to indicate that the RAT is EUTRAN. For further details refer to 3GPP TS 29.274 [22]
 * CDMA2000_1X (2000)
 *   This value shall be used to indicate that the RAT is CDMA2000 1X. For further details refer to 3GPP2 X.S0011 [20].
 * HRPD (2001)
 *   This value shall be used to indicate that the RAT is HRPD. For further details refer to 3GPP2 X.S0011 [20].
 * UMB (2002)
 *   This value shall be used to indicate that the RAT is UMB. For further details refer to 3GPP2 X.S0011 [20].
 * EHRPD (2003)
 *   This value shall be used to indicate that the RAT is eHRPD. For further details refer to 3GPP2 X.S0057 [24].
 * </pre>
 * 
 * NOTE: defined in TS 29.212
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum RATType implements Enumerated{

  WLAN(0),
  VIRTUAL(1),
  UTRAN(1000),
  GERAN(1001),
  GAN(1002),
  HSPA_EVOLUTION(1003),
  EUTRAN(1004),
  CDMA2000_1X(2000),
  HRPD(2001),
  UMB(2002),
  EHRPD(2003);

  public static final int _WLAN = WLAN.getValue();
  public static final int _VIRTUAL = VIRTUAL.getValue();
  public static final int _UTRAN = UTRAN.getValue();
  public static final int _GERAN = GERAN.getValue();
  public static final int _GAN = GAN.getValue();
  public static final int _HSPA_EVOLUTION = HSPA_EVOLUTION.getValue();
  public static final int _EUTRAN = EUTRAN.getValue();
  public static final int _CDMA2000_1X = CDMA2000_1X.getValue();
  public static final int _HRPD = HRPD.getValue();
  public static final int _UMB = UMB.getValue();
  public static final int _EHRPD = EHRPD.getValue();

  private int value = -1;

  private RATType(int v) {
    this.value=v;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static RATType fromInt(int type) throws IllegalArgumentException {
    switch (type) {
      case 0:
        return WLAN;
      case 1:
        return VIRTUAL;
      case 1000:
        return UTRAN;
      case 1001:
        return GERAN;
      case 1002:
        return GAN;
      case 1003:
        return HSPA_EVOLUTION;
      case 1004:
        return EUTRAN;
      case 2000:
        return CDMA2000_1X;
      case 2001:
        return HRPD;
      case 2002:
        return UMB;
      case 2003:
        return EHRPD;

      default:
        throw new IllegalArgumentException();
    }
  }

  public int getValue() {
    return this.value;
  }

}
