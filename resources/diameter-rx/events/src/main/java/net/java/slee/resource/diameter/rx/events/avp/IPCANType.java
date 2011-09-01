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
 * The IP-CAN-Type AVP (AVP code 1027) is of type Enumerated, and it shall
 * indicate the type of Connectivity Access Network in which the user is
 * connected. The IP-CAN-Type AVP shall always be present during the IP-CAN
 * session establishment. During an IP-CAN session modification, this AVP shall
 * be present when there has been a change in the IP-CAN type and the PCRF
 * requested to be informed of this event. The Event-Trigger AVP with value
 * IP-CAN-CHANGE shall be provided together with the IP-CAN-Type AVP. NOTE: The
 * informative Annex C presents a mapping between the code values for different
 * access network types. The following values are defined:
 * 
 * <pre>
 * 3GPP-GPRS (0)
 *   This value shall be used to indicate that the IP-CAN is associated with a 3GPP GPRS access 
 *   that is connected to the GGSN based on the Gn/Gp interfaces and is further detailed by the 
 *   RAT-Type AVP. RAT-Type AVP will include applicable 3GPP values, except EUTRAN.
 * DOCSIS (1)
 *   This value shall be used to indicate that the IP-CAN is associated with a DOCSIS access.
 * xDSL (2)
 *   This value shall be used to indicate that the IP-CAN is associated with an xDSL access.
 * WiMAX (3)
 *   This value shall be used to indicate that the IP-CAN is associated with a WiMAX access (IEEE 802.16).
 * 3GPP2 (4)
 *   This value shall be used to indicate that the IP-CAN is associated with a 3GPP2 access connected 
 *   to the 3GPP2 packet core as specified in 3GPP2 X.S0011 [20] and is further detailed by the RAT-Type AVP.
 * 3GPP-EPS (5)
 *   This value shall be used to indicate that the IP-CAN associated with a 3GPP EPS access and is further 
 *   detailed by the RAT-Type AVP.
 * Non-3GPP-EPS (6)
 *   This value shall be used to indicate that the IP-CAN associated with an EPC based non-3GPP access 
 *   and is further detailed by the RAT-Type AVP.
 * 
 * </pre>
 * 
 * NOTE: defined in TS 29.212
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum IPCANType implements Enumerated{

  TGPP_GPRS(0),
  DOCSIS(1),
  xDSL(2),
  WiMAX(3),
  TGPP2(4),
  TGPP_EPS(5),
  Non_3GPP_EPS (6);

  public static final int _TGPP_GPRS = TGPP_GPRS.getValue();
  public static final int _DOCSIS = DOCSIS.getValue();
  public static final int _xDSL = xDSL.getValue();
  public static final int _WiMAX = WiMAX.getValue();
  public static final int _TGPP2 = TGPP2.getValue();
  public static final int _TGPP_EPS = TGPP_EPS.getValue();
  public static final int _Non_3GPP_EPS = Non_3GPP_EPS.getValue();

  private int value = -1;

  private IPCANType(int v) {
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

  public static IPCANType fromInt(int type) throws IllegalArgumentException {
    switch (type) {
      case 0:
        return TGPP_GPRS;
      case 1:
        return DOCSIS;
      case 2:
        return xDSL;
      case 3:
        return WiMAX;
      case 4:
        return TGPP2;
      case 5:
        return TGPP_EPS;
      case 6:
        return Non_3GPP_EPS;

      default:
        throw new IllegalArgumentException();
    }
  }

  public int getValue() {
    return this.value;
  }

}
