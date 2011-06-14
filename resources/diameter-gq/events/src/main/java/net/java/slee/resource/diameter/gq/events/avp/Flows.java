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

package net.java.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the IP flows grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V1.4.0) specification:
 * 
 * <pre>
 * 7.3.20 Flows AVP
 * The Binding-information AVP (AVP Code 510) is of type Grouped AVP and it indicates IP flows via their flow
 * identifiers. 
 * 
 * It has the following ABNF grammar: 
 *  Flows ::= AVP Header: 510 13019
 *      [ Media-Component-Number ]
 *      [ Flow-number ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface Flows extends GroupedAvp {

  /**
   * Returns the value of the Media-Component-Number AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getMediaComponentNumber();

  /**
   * Returns the value of the Flow-number AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long[] getFlowNumber();

  /**
   * Returns true if the Media-Component-Number AVP is present in the message.
   */
  abstract boolean hasMediaComponentNumber();

  /**
   * Sets the value of the Media-Component-Number, of type Unsigned32.
   */
  abstract void setMediaComponentNumber(long mediaComponentNumber);

  /**
   * Sets the value of the Flow-number, of type Unsigned32.
   */
  abstract void setFlowNumber(long flowNumber);

  /**
   * Sets the value of the Flow-number, of type Unsigned32.
   */
  abstract void setFlowNumbers(long[] flowNumbers);
}
