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

package net.java.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Service-Information grouped AVP type.<br>
 * <br>
 * From the Diameter Rf Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.101 Service-Information AVP 
 * The Service-Information AVP (AVP code 873) is of type Grouped. 
 * Its purpose is to allow the transmission of additional 3GPP service specific information elements which are not
 * described in this document.
 * 
 * It has the following ABNF grammar: 
 *  Service-Information ::= AVP Header: 873 
 *      [ PS-Information ] 
 *      [ WLAN-Information ] 
 *      [ IMS-Information ] 
 *      [ MMS-Information ] 
 *      [ LCS-Information ] 
 *      [ PoC-Information ]
 *      [ MBMS-Information ] 
 *      
 *  The format and the contents of the fields inside the Service-Information AVP are specified in the middle-tier
 *  documents which are applicable for the specific service. Note that the formats of the fields are service-specific,
 *  i.e. the format will be different for the various services. Further fields may be included in the 
 *  Service-Information AVP when new services are introduced.
 *  </pre>
 *  
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ServiceInformation extends GroupedAvp {

  /**
   * Returns the value of the IMS-Information AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract ImsInformation getImsInformation();

  /**
   * Returns the value of the LCS-Information AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract LcsInformation getLcsInformation();

  /**
   * Returns the value of the MBMS-Information AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract MbmsInformation getMbmsInformation();

  /**
   * Returns the value of the MMS-Information AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract MmsInformation getMmsInformation();

  /**
   * Returns the value of the PoC-Information AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract PocInformation getPocInformation();

  /**
   * Returns the value of the PS-Information AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract PsInformation getPsInformation();

  /**
   * Returns the value of the WLAN-Information AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract WlanInformation getWlanInformation();

  /**
   * Returns true if the IMS-Information AVP is present in the message.
   */
  abstract boolean hasImsInformation();

  /**
   * Returns true if the LCS-Information AVP is present in the message.
   */
  abstract boolean hasLcsInformation();

  /**
   * Returns true if the MBMS-Information AVP is present in the message.
   */
  abstract boolean hasMbmsInformation();

  /**
   * Returns true if the MMS-Information AVP is present in the message.
   */
  abstract boolean hasMmsInformation();

  /**
   * Returns true if the PoC-Information AVP is present in the message.
   */
  abstract boolean hasPocInformation();

  /**
   * Returns true if the PS-Information AVP is present in the message.
   */
  abstract boolean hasPsInformation();

  /**
   * Returns true if the WLAN-Information AVP is present in the message.
   */
  abstract boolean hasWlanInformation();

  /**
   * Sets the value of the IMS-Information AVP, of type Grouped.
   */
  abstract void setImsInformation(ImsInformation imsInformation);

  /**
   * Sets the value of the LCS-Information AVP, of type Grouped.
   */
  abstract void setLcsInformation(LcsInformation lcsInformation);

  /**
   * Sets the value of the MBMS-Information AVP, of type Grouped.
   */
  abstract void setMbmsInformation(MbmsInformation mbmsInformation);

  /**
   * Sets the value of the MMS-Information AVP, of type Grouped.
   */
  abstract void setMmsInformation(MmsInformation mmsInformation);

  /**
   * Sets the value of the PoC-Information AVP, of type Grouped.
   */
  abstract void setPocInformation(PocInformation pocInformation);

  /**
   * Sets the value of the PS-Information AVP, of type Grouped.
   */
  abstract void setPsInformation(PsInformation psInformation);

  /**
   * Sets the value of the WLAN-Information AVP, of type Grouped.
   */
  abstract void setWlanInformation(WlanInformation wlanInformation);

}
