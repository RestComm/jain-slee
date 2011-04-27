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
 * Interface to allow applications to retrieve Diameter header fields should they need to.
 *
 * @author Open Cloud
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface DiameterHeader extends Cloneable {

  /**
   * Return true if the request flag is set in this header.
   * @return request flag
   */
  boolean isRequest();

  /**
   * Return true if the proxiable flag is set in this header.
   * @return proxiable flag
   */
  boolean isProxiable();

  /**
   * Return true if the error flag is set in this header.
   * @return error flag
   */
  boolean isError();

  /**
   * Return true if the potentially retransmitted flag is set in this header.
   * @return potentially retransmitted flag
   */
  boolean isPotentiallyRetransmitted();

  /**
   * Return application ID from this Diameter header.
   * @return the application ID
   */
  long getApplicationId();

  /**
   * Return Hop-by-Hop ID from this Diameter header.
   * @return the hop-by-hop id
   */
  long getHopByHopId();

  /**
   * Return End-to-End ID from this Diameter header.
   * @return the end-to-end id
   */
  long getEndToEndId();

  /**
   * Return the Diameter version ID from this Diameter header.
   * @return the value 1
   */
  short getVersion();

  /**
   * Return the message length stored in this Diameter header.
   * Note that for outgoing messages, the correct length may not be known
   * until the message is encoded for transmission.
   * @return the message length
   */
  int getMessageLength();

  /**
   * Return the command code stored in this Diameter header.
   * @return the command code
   */
  int getCommandCode();

  /**
   * Creates and returns a deep copy of this diameter header instance.
   * @return a deep copy of this header.
   */
  Object clone();

  /**
   * Set Hop-by-Hop ID from this Diameter header. Used in case of message creation. In some cases value can be corrupted or not filled properly. 
   * @return the hop-by-hop id
   */
  void setHopByHopId(long hbh);

  /**
   * Set End-to-End ID from this Diameter header. Used in case of message creation. In some cases value can be corrupted or not filled properly.
   * @return the end-to-end id
   */
  void setEndToEndId(long etd);

}
