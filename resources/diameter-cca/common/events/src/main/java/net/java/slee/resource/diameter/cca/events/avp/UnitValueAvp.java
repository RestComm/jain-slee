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

package net.java.slee.resource.diameter.cca.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 *<pre> <b>8.8. Unit-Value AVP</b>
 *
 *
 *   Unit-Value AVP is of type Grouped (AVP Code 445) and specifies the
 *   units as decimal value.  The Unit-Value is a value with an exponent;
 *   i.e., Unit-Value = Value-Digits AVP * 10^Exponent.  This
 *   representation avoids unwanted rounding off.  For example, the value
 *   of 2,3 is represented as Value-Digits = 23 and Exponent = -1.  The
 *   absence of the exponent part MUST be interpreted as an exponent equal
 *   to zero.
 *
 *   It is defined as follows (per the grouped-avp-def of
 *   RFC 3588 [DIAMBASE]):
 *
 *                    Unit-Value ::= < AVP Header: 445 >
 *                                   { Value-Digits }
 *                                   [ Exponent ]
 *	</pre>
 *      
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface UnitValueAvp extends GroupedAvp {

  /**
   * Sets the value of the Value-Digits AVP, of type Integer64.
   * 
   * @param digits
   */
  public void setValueDigits(long digits);

  /**
   * Returns the value of the Value-Digits AVP, of type Integer64.
   * 
   * @return
   */
  public long getValueDigits();

  /**
   * Sets the value of the Exponent AVP, of type Integer32.
   * 
   * @param expotent
   */
  public void setExponent(int exponent);

  /**
   * Returns the value of the Exponent AVP, of type Integer32.
   * 
   * @return
   */
  public int getExponent();

  /**
   * Returns true if the Exponent AVP is present in the message.
   * 
   * @return
   */
  boolean hasExponent();

  /**
   * Returns true if the Value-Digits AVP is present in the message.
   * 
   * @return
   */
  boolean hasValueDigits();

}
