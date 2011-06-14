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
 * Defines an interface representing the binding information grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V1.4.0) specification:
 * 
 * <pre>
 * 7.3.1 Binding-output-list AVP
 * The Binding-information AVP (AVP Code 450) is of type Grouped AVP and is sent between the AF and SPDF
 * in order to convey binding information required for NA(P)T,hosted NA(P)T and NA(P)T-PT control 
 * 
 * It has the following ABNF grammar: 
 *  Binding-information ::= AVP Header: 450 13019
 *      [ Binding-Input-List ]
 *      [ Binding-Output-List ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface BindingInformation extends GroupedAvp {

  /**
   * Returns the value of the Binding-Input-List AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract BindingInputList getBindingInputList();

  /**
   * Returns the value of the Binding-Output-List AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract BindingOutputList getBindingOutputList();

  /**
   * Returns true if the Binding-Input-List AVP is present in the message.
   */
  abstract boolean hasBindingInputList();

  /**
   * Returns true if the Binding-Output-List AVP is present in the message.
   */
  abstract boolean hasBindingOutputList();

  /**
   * Sets the value of the Binding-Input-List AVP, of type Grouped.
   */
  abstract void setBindingInputList(BindingInputList bindingInputList);

  /**
   * Sets the value of the Binding-Output-List AVP, of type Grouped.
   */
  abstract void setBindingOutputList(BindingOutputList bindingOutputList);
}
