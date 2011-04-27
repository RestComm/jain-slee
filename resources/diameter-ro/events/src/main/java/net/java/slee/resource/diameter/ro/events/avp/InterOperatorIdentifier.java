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
 * Defines an interface representing the Inter-Operator-Identifier grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification: 
 * <pre>
 * 7.2.39 Inter-Operator-Identifier AVP 
 * The Inter-Operator-Identifier AVP (AVP code 838) is of type Grouped and holds the identification of the network
 * neighbours (originating and terminating) as exchanged via SIP signalling and described in [404].
 * 
 * It has the following ABNF grammar: 
 *  Inter-Operator-Identifier ::= AVP Header: 838 
 *      [ Originating-IOI ] 
 *      [ Terminating-IOI ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface InterOperatorIdentifier extends GroupedAvp {

  /**
   * Returns the value of the Originating-IOI AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getOriginatingIoi();

  /**
   * Returns the value of the Terminating-IOI AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getTerminatingIoi();

  /**
   * Returns true if the Originating-IOI AVP is present in the message.
   */
  abstract boolean hasOriginatingIoi();

  /**
   * Returns true if the Terminating-IOI AVP is present in the message.
   */
  abstract boolean hasTerminatingIoi();

  /**
   * Sets the value of the Originating-IOI AVP, of type UTF8String.
   */
  abstract void setOriginatingIoi(String originatingIoi);

  /**
   * Sets the value of the Terminating-IOI AVP, of type UTF8String.
   */
  abstract void setTerminatingIoi(String terminatingIoi);

}
