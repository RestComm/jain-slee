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
 * The Specific-Action AVP (AVP code 513) is of type Enumerated. Within a PCRF
 * initiated Re-Authorization Request, the Specific-Action AVP determines the
 * type of the action. Within an initial AA request the AF may use the
 * Specific-Action AVP to request specific actions from the server at the bearer
 * events and to limit the contact to such bearer events where specific action
 * is required. If the Specific-Action AVP is omitted within the initial AA
 * request, no notification of any of the events defined below is requested. The
 * following values are defined:
 * 
 * <pre>
 * Void (0)
 * CHARGING_CORRELATION_EXCHANGE (1)
 *     Within a RAR, this value shall be used when the server reports the access network 
 *     charging identifier to the AF. The Access-Network-Charging-Identifier AVP shall be 
 *     included within the request. In the AAR, this value indicates that the AF requests 
 *     the server to provide the access network charging identifier to the AF for each authorized flow, 
 *     when the access network charging identifier becomes known at the PCRF.
 * INDICATION_OF_LOSS_OF_BEARER (2)
 *     Within a RAR, this value shall be used when the server reports a loss of a bearer 
 *     (e.g. in the case of GPRS PDP context bandwidth modification to 0 kbit) to the AF. 
 *     The SDFs that are deactivated as a consequence of this loss of bearer shall be provided within 
 *     the Flows AVP. In the AAR, this value indicates that the AF requests the server to provide
 *      a notification at the loss of a bearer.
 * INDICATION_OF_RECOVERY_OF_BEARER (3)
 *      Within a RAR, this value shall be used when the server reports a recovery of a bearer 
 *     (e.g. in the case of GPRS, PDP context bandwidth modification from 0 kbit to another value) 
 *     to the AF. The SDFs that are re-activated as a consequence of the recovery of bearer shall be 
 *     provided within the Flows AVP. In the AAR, this value indicates that the AF requests the server to 
 *     provide a notification at the recovery of a bearer.
 * INDICATION_OF_RELEASE_OF_BEARER (4)
 *     Within a RAR, this value shall be used when the server reports the release of a bearer 
 *     (e.g. PDP context removal for GPRS) to the AF. The SDFs that are deactivated as a consequence 
 *     of this release of bearer shall be provided within the Flows AVP. In the AAR, this value indicates 
 *     that the AF requests the server to provide a notification at the removal of a bearer.
 * Void (5)
 * IP-CAN_CHANGE (6)
 *     This value shall be used in RAR command by the PCRF to indicate a change in the IP-CAN type or 
 *     RAT type (if the IP-CAN type is GPRS). When used in an AAR command, this value indicates that 
 *     the AF is requesting subscription to IP-CAN change and RAT change notification. When used in RAR 
 *     it indicates that the PCRF generated the request because of an IP-CAN or RAT change. IP-CAN-Type 
 *     AVP and RAT-Type AVP (if the IP-CAN type is GPRS) shall be provided in the same request with the 
 *     new/valid value(s).  If an IP-CAN type or RAT type change is due to IP flow mobility and a 
 *     subset of the flows within the AF session is affected, the affected service data flows shall be 
 *     provided in the same request. 
 * INDICATION_OF_OUT_OF_CREDIT (7)
 *     Within a RAR, this value shall be used when the PCRF reports to the AF that SDFs have run out of 
 *     credit, and that the termination action indicated by the corresponding Final-Unit-Action AVP 
 *     applies (3GPP TS 32.240 [23] and 3GPP TS 32.299 [24). The SDFs that are impacted as a consequence 
 *     of the out of credit condition shall be provided within the Flows AVP. In the AAR, this value 
 *     indicates that the AF requests the PCRF to provide a notification of SDFs for which credit is no 
 *     longer available. Applicable to functionality introduced with the Rel8 feature as described in clause 5.4.1.
 * INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION (8)
 *     Within a RAR, this value shall be used by the PCRF to indicate that the resources requested for 
 *     particular service information have been successfully allocated. The SDFs corresponding to the 
 *    resources successfully allocated shall be provided within the Flows AVP.
 *     In the AAR, this value indicates that the AF requests the PCRF to provide a notification when 
 *     the resources associated to the corresponding service information have been allocated.
 *     Applicable to functionality introduced with the Rel8 feature as described in clause 5.4.1.
 *     NOTE:   This value applies to applications for which the successful resource allocation notification 
 *     is required for their operation since subscription to this value impacts the resource allocation 
 *     signalling overhead towards the PCEF/BBERF.
 * INDICATION_OF_FAILED_RESOURCES_ALLOCATION (9)
 *     Within a RAR, this value shall be used by the PCRF to indicate that the resources requested for a 
 *     particular service information cannot be successfully allocated. The SDFs corresponding to the resources
 *     that could not be allocated shall be provided within the Flows AVP. In the AAR, this value indicates 
 *     that the AF requests the PCRF to provide a notification when the resources associated to the corresponding 
 *     service information cannot be allocated. Applicable to functionality introduced with the Rel8 feature 
 *     as described in clause 5.4.1. NOTE:   This value applies to applications for which the unsuccessful 
 *     resource allocation notification is required for their operation since subscription to this value 
 *     impacts the resource allocation signalling overhead towards the PCEF/BBERF.
 * INDICATION_OF_LIMITED_PCC_DEPLOYMENT (10)
 *     Within a RAR, this value shall be used when the server reports the limited PCC deployment 
 *     (i.e. dynamically allocated resources are not applicable) as specified at Annex L in 3GPP TS 23.203 [2] 
 *     to the AF. In the AAR, this value indicates that the AF requests the server to provide a notification 
 *     for the limited PCC deployment. Applicable to functionality introduced with the Rel8 feature as described 
 *     in clause 5.4.1. 
 * USAGE_REPORT (11)
 *     In the RA-Request (RAR), this value shall be used by the PCRF to report accumulated usage volume when 
 *     the usage threshold provided by the AF has been reached. In the AA-Request (AAR), this value 
 *     indicates that the AF requests PCRF to report accumulated usage volume when it reaches the threshold.
 *     Applicable to functionality introduced with the SponsoredConnectivity feature as described in clause 5.4.1
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum SpecificAction implements Enumerated{

  Void(0),
  CHARGING_CORRELATION_EXCHANGE(1),
  INDICATION_OF_LOSS_OF_BEARER(2),
  INDICATION_OF_RECOVERY_OF_BEARER(3),
  INDICATION_OF_RELEASE_OF_BEARER(4),
  Void2(5),
  IP_CAN_CHANGE(6),
  INDICATION_OF_OUT_OF_CREDIT(7),
  INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION(8),
  INDICATION_OF_FAILED_RESOURCES_ALLOCATION(9),
  INDICATION_OF_LIMITED_PCC_DEPLOYMENT(10),
  USAGE_REPORT(11);

  public static final int _Void = Void.getValue();
  public static final int _CHARGING_CORRELATION_EXCHANGE = CHARGING_CORRELATION_EXCHANGE.getValue();
  public static final int _INDICATION_OF_LOSS_OF_BEARER = INDICATION_OF_LOSS_OF_BEARER.getValue();
  public static final int _INDICATION_OF_RECOVERY_OF_BEARER = INDICATION_OF_RECOVERY_OF_BEARER.getValue();
  public static final int _INDICATION_OF_RELEASE_OF_BEARER = INDICATION_OF_RELEASE_OF_BEARER.getValue();
  public static final int _Void2 = Void2.getValue();
  public static final int _IP_CAN_CHANGE = IP_CAN_CHANGE.getValue();
  public static final int _INDICATION_OF_OUT_OF_CREDIT = INDICATION_OF_OUT_OF_CREDIT.getValue();
  public static final int _INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION = INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION.getValue();
  public static final int _INDICATION_OF_FAILED_RESOURCES_ALLOCATION = INDICATION_OF_FAILED_RESOURCES_ALLOCATION.getValue();
  public static final int _INDICATION_OF_LIMITED_PCC_DEPLOYMENT = INDICATION_OF_LIMITED_PCC_DEPLOYMENT.getValue();
  public static final int _USAGE_REPORT = USAGE_REPORT.getValue();

  private int value = -1;

  private SpecificAction(int v) {
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

  public static SpecificAction fromInt(int type) throws IllegalArgumentException {
    switch (type) {
      case 0: return Void;
      case 1: return CHARGING_CORRELATION_EXCHANGE;
      case 2: return INDICATION_OF_LOSS_OF_BEARER;
      case 3: return INDICATION_OF_RECOVERY_OF_BEARER;
      case 4: return INDICATION_OF_RELEASE_OF_BEARER;
      case 5: return Void2;
      case 6: return IP_CAN_CHANGE;
      case 7: return INDICATION_OF_OUT_OF_CREDIT;
      case 8: return INDICATION_OF_SUCCESSFUL_RESOURCES_ALLOCATION;
      case 9: return INDICATION_OF_FAILED_RESOURCES_ALLOCATION;
      case 10: return INDICATION_OF_LIMITED_PCC_DEPLOYMENT;
      case 11: return USAGE_REPORT;

      default:
        throw new IllegalArgumentException();
    }
  }

  public int getValue() {
    return this.value;
  }

}
