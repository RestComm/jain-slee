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

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Terminal-Information grouped AVP type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.3  Terminal-Information
 * 
 * The Terminal-Information AVP is of type Grouped. This AVP shall contain the information about
 * the user’s terminal.
 * 
 * AVP format
 * Terminal Information ::= < AVP header: 1401 10415 >
 *                          [ IMEI ]
 *                          [ 3GPP2-MEID ]
 *                          [ Software-Version ]
 *                         *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface TerminalInformationAvp extends GroupedAvp {

  /**
   * Returns true if the IMEI AVP is present in the message.
   * 
   * @return true if the IMEI AVP is present in the message, false otherwise
   */
  public boolean hasIMEI();

  /**
   * Sets the value of the IMEI AVP, of type UTF8String.
   * 
   * @param imei
   */
  public void setIMEI(String imei);

  /**
   * Returns the value of the IMEI AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set.
   * 
   * @return
   */
  public String getIMEI();

  /**
   * Returns true if the 3GPP2-MEID AVP is present in the message.
   * 
   * @return true if the 3GPP2-MEID AVP is present in the message, false otherwise
   */
  public boolean has3GPP2MEID();

  /**
   * Sets the value of the 3GPP2-MEID AVP, of type OctetString.
   * 
   * @param tgpp2Meid
   */
  public void set3GPP2MEID(byte[] tgpp2Meid);

  /**
   * Returns the value of the 3GPP2-MEID AVP, of type OctetString.
   * A return value of null implies that the AVP has not been set.
   * 
   * @return
   */
  public byte[] get3GPP2MEID();

  /**
   * Returns true if the Software-Version AVP is present in the message.
   * 
   * @return true if the Software-Version AVP is present in the message, false otherwise
   */
  public boolean hasSoftwareVersion();

  /**
   * Sets the value of the Software-Version AVP, of type UTF8String.
   * 
   * @param softwareVersion
   */
  public void setSoftwareVersion(String softwareVersion);

  /**
   * Returns the value of the Software-Version AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set.
   * 
   * @return
   */
  public String getSoftwareVersion();

}
