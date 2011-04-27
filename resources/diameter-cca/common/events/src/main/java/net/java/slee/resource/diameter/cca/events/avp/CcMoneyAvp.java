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
 * <pre><b>8.22. CC-Money AVP</b>
 *
 *
 *   The CC-Money AVP (AVP Code 413) is of type Grouped and specifies the
 *   monetary amount in the given currency.  The Currency-Code AVP SHOULD
 *   be included.  It is defined as follows (per the grouped-avp-def of
 *   RFC 3588 [DIAMBASE]):
 *
 *      CC-Money ::= < AVP Header: 413 >
 *                   { Unit-Value }
 *                   [ Currency-Code ]
 *   <pre>
 *      
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface CcMoneyAvp extends GroupedAvp {

  /**
   * Sets value of Init-Value avp of type Grouped AVP.
   * See: {@link UnitValueAvp} .
   * @param unitValue
   */
  public void setUnitValue(UnitValueAvp unitValue);

  /**
   * Return value of Unit-Value avp. Return value of null indicates its not present.
   * See: {@link UnitValueAvp} .
   * @return
   */
  public UnitValueAvp getUnitValue();

  /**
   * Returns true if Unit-Value avp is present
   * @return
   */
  public boolean hasUnitValue();

  /**
   * Returns Currency-Code avp (Unsigned32) value in use, if present value is greater than 0.
   * @return
   */
  public long getCurrencyCode();

  /**
   * Sets Currency-Code avp of type Unsigned32
   * @param code
   */
  public void setCurrencyCode(long code);

  /**
   * Returns true if Currency-Code avp is present.
   * @return
   */
  public boolean hasCurrencyCode();

}
