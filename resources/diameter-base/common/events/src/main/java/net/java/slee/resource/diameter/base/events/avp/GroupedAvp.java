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
 * A GroupedAvp represents a Diameter Grouped AVP type.
 * This is the superinterface for the RA defined Grouped AVP types.
 * An array of the AVPs contained in the class can be accessed using
 * {@link org.mobicents.slee.resource.diameter.base.DiameterAvp#groupedAvpValue()}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface GroupedAvp extends DiameterAvp {

  /**
   * Sets the set of extension AVPs with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in
   * the array. Note: the array must not be altered by the caller following
   * this call, and getExtensionAvps() is not guaranteed to return the same
   * array instance, e.g. an "==" check would fail.
   * 
   * @param extensions
   * @throws AvpNotAllowedException 
   */
  public void setExtensionAvps(DiameterAvp[] extensions) throws AvpNotAllowedException;

  /**
   * Returns the set of extension AVPs. The returned array contains the
   * extension AVPs in the order they appear in the message. A return value of
   * null implies that no extensions AVPs have been set.
   * 
   * @return
   */
  public DiameterAvp[] getExtensionAvps();

}
