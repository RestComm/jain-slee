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

package net.java.slee.resource.diameter.base.events.avp;

/**
 * Defines an interface representing the Experimental-Result grouped AVP type.
 *
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * <pre>
 * 7.6.  Experimental-Result AVP
 * 
 *    The Experimental-Result AVP (AVP Code 297) is of type Grouped, and
 *    indicates whether a particular vendor-specific request was completed
 *    successfully or whether an error occurred.  Its Data field has the
 *    following ABNF grammar:
 * 
 *    AVP Format
 * 
 *       Experimental-Result ::= &lt; AVP Header: 297 &gt;
 *                                  { Vendor-Id }
 *                                  { Experimental-Result-Code }
 *    The Vendor-Id AVP (see Section 5.3.3) in this grouped AVP identifies
 *    the vendor responsible for the assignment of the result code which
 *    follows.  All Diameter answer messages defined in vendor-specific
 *    applications MUST include either one Result-Code AVP or one
 *    Experimental-Result AVP.
 * 
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ExperimentalResultAvp extends GroupedAvp {

  /**
   * Returns true if the Vendor-Id AVP is present in the message.
   */
  public boolean hasVendorIdAVP();

  /**
   * Returns the value of the Vendor-Id AVP, of type Unsigned32.
   * A return value of Long.MIN_VALUE implies that the AVP has not been set or some error has been encountered.
   */
  public long getVendorIdAVP();

  /**
   * Sets the value of the Vendor-Id AVP, of type Unsigned32.
   * @throws IllegalStateException if setVendorId has already been called
   */
  public void setVendorIdAVP(long vendorId);

  /**
   * Returns true if the Experimental-Result-Code AVP is present in the message.
   */
  public boolean hasExperimentalResultCode();

  /**
   * Returns the value of the Experimental-Result-Code AVP, of type Unsigned32.
   * A return value of Long.MIN_VALUE implies that the AVP has not been set or some error has been encountered.
   */
  public long getExperimentalResultCode();

  /**
   * Sets the value of the Experimental-Result-Code AVP, of type Unsigned32.
   * @throws IllegalStateException if setExperimentalResultCode has already been called
   */
  public void setExperimentalResultCode(long experimentalResultCode);

}
