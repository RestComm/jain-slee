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

package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * 
 * InterOperatorIdentifierImpl.java
 *
 * <br>Project:  mobicents
 * <br>8:03:55 PM Apr 11, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class InterOperatorIdentifierImpl extends GroupedAvpImpl implements InterOperatorIdentifier {

  public InterOperatorIdentifierImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public InterOperatorIdentifierImpl( int code, long vendorId, int mnd, int prt, byte[] value )
  {
    super( code, vendorId, mnd, prt, value );
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#getOriginatingIoi()
   */
  public String getOriginatingIoi() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.ORIGINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#getTerminatingIoi()
   */
  public String getTerminatingIoi() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.TERMINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#hasOriginatingIoi()
   */
  public boolean hasOriginatingIoi() {
    return hasAvp(DiameterRoAvpCodes.ORIGINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#hasTerminatingIoi()
   */
  public boolean hasTerminatingIoi() {
    return hasAvp(DiameterRoAvpCodes.TERMINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#setOriginatingIoi(String)
   */
  public void setOriginatingIoi( String originatingIoi ) {
    addAvp(DiameterRoAvpCodes.ORIGINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID, originatingIoi);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.InterOperatorIdentifier#setTerminatingIoi(String)
   */
  public void setTerminatingIoi( String terminatingIoi ) {
    addAvp(DiameterRoAvpCodes.TERMINATING_IOI, DiameterRoAvpCodes.TGPP_VENDOR_ID, terminatingIoi);
  }

}
