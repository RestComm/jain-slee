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

package net.java.slee.resource.diameter.gx.events;

import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;



/**
 * Interface defining GxReAuthAnswer message as defined in 3GPP TS 29.210 V6.7.0 (2006-12). It has following structure:
 *
 * 
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxReAuthRequest extends GxReAuthMessage {

    static final int commandCode = 258;

  /**
   * Returns true if the Auth-Application-Id AVP is present in the message.
   */
  boolean hasAuthApplicationId();

  /**
   * Returns the value of the Auth-Application-Id AVP, of type Unsigned32. Use
   * {@link #hasAuthApplicationId()} to check the existence of this AVP.
   * 
   * @return the value of the Auth-Application-Id AVP
   * @throws IllegalStateException
   *             if the Auth-Application-Id AVP has not been set on this
   *             message
   */
  long getAuthApplicationId();

  /**
   * Sets the value of the Auth-Application-Id AVP, of type Unsigned32.
   * 
   * @throws IllegalStateException
   *             if setAuthApplicationId has already been called
   */
  void setAuthApplicationId(long authApplicationId);

  /**
   * Returns true if the Re-Auth-Request-Type AVP is present in the message.
   */
  boolean hasReAuthRequestType();

  /**
   * Returns the value of the Re-Auth-Request-Type AVP, of type Enumerated.
   * 
   * @return the value of the Re-Auth-Request-Type AVP or null if it has not
   *         been set on this message
   */
  ReAuthRequestType getReAuthRequestType();

  /**
   * Sets the value of the Re-Auth-Request-Type AVP, of type Enumerated.
   * 
   * @throws IllegalStateException
   *             if setReAuthRequestType has already been called
   */
  void setReAuthRequestType(ReAuthRequestType reAuthRequestType);

}
