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

package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Application-Server-Information grouped AVP type.<br> 
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification: 
 * <pre>
 * 7.2.12 Application-Server-Information AVP
 * 
 * The Application-Server-Information AVP (AVP code 850) is of type Grouped and contains information about application
 * servers visited through ISC interface. 
 * 
 * It has the following ABNF grammar: 
 *  Application-Server-Information ::= AVP Header: 850 
 *      [ Application-Server ] 
 *    * [ Application-Provided-Called-Party-Address ]
 * <pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ApplicationServerInformation extends GroupedAvp{

  /**
   * Returns the set of Application-Provided-Called-Party-Address AVPs. The returned array contains the AVPs in the order they appear in the message. A return value of null implies that no Application-Provided-Called-Party-Address AVPs have been set. The elements in the given array are String objects.
   */
  abstract String[] getApplicationProvidedCalledPartyAddresses();

  /**
   * Returns the value of the Application-Server AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getApplicationServer();

  /**
   * Returns true if the Application-Server AVP is present in the message.
   */
  abstract boolean hasApplicationServer();

  /**
   * Sets a single Application-Provided-Called-Party-Address AVP in the message, of type UTF8String.
   */
  abstract void setApplicationProvidedCalledPartyAddress(String applicationProvidedCalledPartyAddress);

  /**
   * Sets the set of Application-Provided-Called-Party-Address AVPs, with all the values in the given array. The AVPs will be added to message in the order in which they appear in the array. Note: the array must not be altered by the caller following this call, and getApplicationProvidedCalledPartyAddresses() is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   */
  abstract void setApplicationProvidedCalledPartyAddresses(String[] applicationProvidedCalledPartyAddresses);

  /**
   * Sets the value of the Application-Server AVP, of type UTF8String.
   */
  abstract void setApplicationServer(String applicationServer);

}
