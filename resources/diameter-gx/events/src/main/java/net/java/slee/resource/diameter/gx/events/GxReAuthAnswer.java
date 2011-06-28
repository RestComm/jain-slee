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

package net.java.slee.resource.diameter.gx.events;

import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.gx.events.avp.ChargingInformation;
import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleInstall;
import net.java.slee.resource.diameter.gx.events.avp.ChargingRuleRemove;
import net.java.slee.resource.diameter.gx.events.avp.EventTrigger;

/**
 * Interface defining GxReAuthAnswer message as defined in 3GPP TS 29.210 V6.7.0 (2006-12). It has following structure:
 *
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public interface GxReAuthAnswer extends GxReAuthMessage {


  public static final int commandCode = 258;

  /**
   * Check if the we have a Result-Code AVP in this message.
   * @return true if the Result-Code AVP is present in the message.
   */
  boolean hasResultCode();

  /**
   * Returns the value of the Result-Code AVP, of type Unsigned32. Use
   * {@link #hasResultCode()} to check the existence of this AVP.
   *
   * @return the value of the Result-Code AVP
   */
  long getResultCode();

  /**
   * Sets the value of the Result-Code AVP, of type Unsigned32.
   * @param resultCode the result code to set.
   */
  void setResultCode(long resultCode);

  /**
   * Returns the value of Event-Trigger AVP of type Enumerated.
   * @return EventTrigger the
   */
  EventTrigger getEventTrigger();

  /**
   * Check if the we have a Event-Trigger AVP in this message. Defined in 3GPP TS 29.210
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
   * @return boolean
   */
  boolean hasEventTrigger();

  /**
   * Sets the value of the Event-Trigger AVP, of type Enumerated. Defined in 3GPP TS 29.210
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
   * @param eventTrigger
   */
  void setEventTrigger(EventTrigger eventTrigger);

  /**
   * Returns the value of Charging-Rule-Remove AVP of type Grouped. Defined in 3GPP TS 29.210
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
   * @return ChargingRuleRemove
   */
  ChargingRuleRemove getChargingRuleRemove();

  /**
   * Returns true if Charging-Rule-Remove AVP is present in the message. Defined in 3GPP TS 29.210
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
   * @return boolean
   */
  boolean hasChargingRuleRemove();

  /**
   * Sets the value of the Charging-Rule-Remove AVP, of type Grouped. Defined in 3GPP TS 29.210
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
   * @param chargingRuleRemove
   */
  void setChargingRuleRemove(ChargingRuleRemove chargingRuleRemove);

  /**
   * Returns the value of Charging-Rule-Install AVP of type Grouped. Defined in 3GPP TS 29.210
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
   * @return ChargingRuleInstall
   */
  ChargingRuleInstall getChargingRuleInstall();

  /**
   * Returns true if Charging-Rule-Install AVP is present in the message. Defined in 3GPP TS 29.210
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
   * @return boolean
   */
  boolean hasChargingRuleInstall();

  /**
   * Sets the value of the Charging-Rule-Install AVP, of type Grouped. Defined in 3GPP TS 29.210
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
   * @param chargingRuleInstall
   */
  void setChargingRuleInstall(final ChargingRuleInstall chargingRuleInstall);

  /**
   * Returns the value of Charging-Information AVP of type Grouped. Defined in 3GPP TS 29.229
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0429229v6b0
   * @return ChargingInformation
   */
  ChargingInformation getChargingInformation();

  /**
   * Returns true if Charging-Information AVP is present in the message. Defined in 3GPP TS 29.229
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0429229v6b0
   * @return
   */
  boolean hasChargingInformation();

  /**
   * Sets the value of the Charging-Information AVP, of type Grouped. Defined in 3GPP TS 29.229
   * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0429229v6b0
   * @param chargingInformation
   */
  void setChargingInformation(final ChargingInformation chargingInformation);

  //TODO Need to add Error-Message and Error-Reporting-Host AVps
  /**
   * Returns the set of Failed-AVP AVPs.
   * @return an array of Failed-AVP AVPs
   */
  FailedAvp[] getFailedAvps();

  /**
   * Sets a single Failed-AVP AVP in the message, of type Grouped.
   *
   * @param failedAvp
   * @throws IllegalStateException
   */
  void setFailedAvp(final FailedAvp failedAvp) throws IllegalStateException;

  /**
   * Sets the set of Failed-AVP AVPs, with all the values in the given array.
   *
   * @param failedAvps
   * @throws IllegalStateException
   */
  void setFailedAvps(final FailedAvp[] failedAvps) throws IllegalStateException;
}
