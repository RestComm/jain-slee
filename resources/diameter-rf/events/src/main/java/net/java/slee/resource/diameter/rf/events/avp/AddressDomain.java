/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package net.java.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Address-Domain grouped AVP type.<br>
 * <br>
 * From the Diameter Rf Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification: 
 * <pre>
 * 7.2.5 Address-Domain AVP
 *  
 * The Address-Domain AVP (AVP code 898) is of type Grouped and indicates the domain/network to which the associated
 * address resides. If this AVP is present, at least one of the AVPs described within the grouping must be included.
 * 
 * It has the following ABNF grammar:
 *  Address-Domain ::= AVP Header: 898 
 *      [ Domain-Name ] 
 *      [ TGPP-IMSI-MCC-MNC ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AddressDomain extends GroupedAvp {

  /**
   * Returns the value of the Domain-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getDomainName();

  /**
   * Returns the value of the TGPP-IMSI-MCC-MNC AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getTgppImsiMccMnc();

  /**
   * Returns true if the Domain-Name AVP is present in the message.
   */
  abstract boolean hasDomainName();

  /**
   * Returns true if the TGPP-IMSI-MCC-MNC AVP is present in the message.
   */
  abstract boolean hasTgppImsiMccMnc();

  /**
   * Sets the value of the Domain-Name AVP, of type UTF8String.
   */
  abstract void setDomainName(String domainName);

  /**
   * Sets the value of the TGPP-IMSI-MCC-MNC AVP, of type OctetString.
   */
  abstract void setTgppImsiMccMnc(byte[] tgppImsiMccMnc);

}
