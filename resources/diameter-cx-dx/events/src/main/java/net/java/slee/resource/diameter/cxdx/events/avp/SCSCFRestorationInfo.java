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

package net.java.slee.resource.diameter.cxdx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * <pre>
 * <b>6.3.46  SCSCF-Restoration-Info AVP</b>
 * The SCSCF-Restoration-Info AVP is of type Grouped and it contains the information required for 
 * an S-CSCF to handle the requests for a user.
 * 
 * AVP format
 * SCSCF-Restoration-Info ::= < AVP Header: 639, 10415>
 *                        { User-Name }
 *                      1*{ Restoration-Info }
 *                       *[ AVP ]
 *
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface SCSCFRestorationInfo extends GroupedAvp {
  /**
   * Returns true if the User-Name AVP is present in the message.
   */
  boolean hasUserName();

  /**
   * Returns the value of the User-Name AVP, of type UTF8String.
   * @return the value of the User-Name AVP or null if it has not been set on this message
   */
  String getUserName();

  /**
   * Sets the value of the User-Name AVP, of type UTF8String.
   * @throws IllegalStateException if setUserName has already been called
   */
  void setUserName(String userName) throws IllegalStateException;

  /**
   * Returns the value of the Restoration-Info AVP, of type Grouped.
   * @return the value of the Restoration-Info AVP or null if it has not been set on this message
   */
  RestorationInfo[] getRestorationInfos();

  /**
   * Sets the value of the Restoration-Info AVP, of type Grouped.
   * @throws IllegalStateException if setRestorationInfo has already been called
   */
  void setRestorationInfo(RestorationInfo restorationInfo) throws IllegalStateException;

  /**
   * Sets the value of the Restoration-Info AVP, of type Grouped.
   * @throws IllegalStateException if setRestorationInfo has already been called
   */
  void setRestorationInfos(RestorationInfo[] restorationInfos) throws IllegalStateException;

}
