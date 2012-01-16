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

package net.java.slee.resource.diameter.base.events;

/**
 * 
 * Super interface fir STX messages
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface SessionTerminationMessage extends DiameterMessage {

  static final int commandCode = 275;

  /**
   * Returns true if the User-Name AVP is present in the message.
   */
  boolean hasUserName();

  /**
   * Returns the value of the User-Name AVP, of type UTF8String.
   * 
   * @return the value of the User-Name AVP or null if it has not been set on
   *         this message
   */
  String getUserName();

  /**
   * Sets the value of the User-Name AVP, of type UTF8String.
   * 
   * @throws IllegalStateException
   *             if setUserName has already been called
   */
  void setUserName(String userName);

  /**
   * Returns the set of Class AVPs. The returned array contains the AVPs in
   * the order they appear in the message. A return value of null implies that
   * no Class AVPs have been set. The elements in the given array are byte[]
   * objects.
   */
  byte[][] getClassAvps();

  /**
   * Sets a single Class AVP in the message, of type OctetString.
   * 
   * @throws IllegalStateException
   *             if setClassAvp or setClassAvps has already been called
   */
  void setClassAvp(byte[] classAvp);

  /**
   * Sets the set of Class AVPs, with all the values in the given array. The
   * AVPs will be added to message in the order in which they appear in the
   * array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getClassAvps() is not guaranteed to return the same array instance,
   * e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setClassAvp or setClassAvps has already been called
   */
  void setClassAvps(byte[][] classAvps);

}
