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
 * Defines an interface representing the Additional-Content-Information grouped AVP type. 
 * <br>
 * From the Diameter Rf Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.2 Additional-Content-Information AVP 
 * 
 * The Additional-Content-Information AVP (AVP code 1207) is of type Grouped and identifies any subsequent content 
 * types. It is used to identify each content (including re-occurences) within an MM when the Type-Number AVP or 
 * Additional-Type-Information AVP from the Content-Type AVP indicate a multi- part content. 
 * 
 * It has the following ABNF grammar:
 *  Additional-Content-Information::= AVP Header: 1207 
 *      [ Type-Number ] 
 *      [ Additional-Type-Information ] 
 *      [ Content-Size ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AdditionalContentInformation extends GroupedAvp{

  /**
   * Returns the value of the Additional-Type-Information AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getAdditionalTypeInformation();

  /**
   * Returns the value of the Content-Size AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getContentSize();

  /**
   * Returns the value of the Type-Number AVP, of type Integer32. A return value of null implies that the AVP has not been set.
   */
  abstract int getTypeNumber();

  /**
   * Returns true if the Additional-Type-Information AVP is present in the message.
   */
  abstract boolean hasAdditionalTypeInformation();

  /**
   * Returns true if the Content-Size AVP is present in the message.
   */
  abstract boolean hasContentSize();

  /**
   * Returns true if the Type-Number AVP is present in the message.
   */
  abstract boolean hasTypeNumber();

  /**
   * Sets the value of the Additional-Type-Information AVP, of type UTF8String.
   */
  abstract void setAdditionalTypeInformation(String additionalTypeInformation);

  /**
   * Sets the value of the Content-Size AVP, of type Unsigned32.
   */
  abstract void setContentSize(long contentSize);

  /**
   * Sets the value of the Type-Number AVP, of type Integer32.
   */
  abstract void setTypeNumber(int typeNumber);

}
