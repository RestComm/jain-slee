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

import net.java.slee.resource.diameter.ro.events.avp.LcsClientName;
import net.java.slee.resource.diameter.ro.events.avp.LcsFormatIndicator;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * LcsClientNameImpl.java
 *
 * <br>Project:  mobicents
 * <br>3:28:17 AM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LcsClientNameImpl extends GroupedAvpImpl implements LcsClientName {

  public LcsClientNameImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public LcsClientNameImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#getLcsDataCodingScheme()
   */
  public String getLcsDataCodingScheme() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#getLcsFormatIndicator()
   */
  public LcsFormatIndicator getLcsFormatIndicator() {
    return (LcsFormatIndicator) getAvpAsEnumerated(DiameterRoAvpCodes.LCS_FORMAT_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID, LcsFormatIndicator.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#getLcsNameString()
   */
  public String getLcsNameString() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_NAME_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#hasLcsDataCodingScheme()
   */
  public boolean hasLcsDataCodingScheme() {
    return hasAvp( DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#hasLcsFormatIndicator()
   */
  public boolean hasLcsFormatIndicator() {
    return hasAvp( DiameterRoAvpCodes.LCS_FORMAT_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#hasLcsNameString()
   */
  public boolean hasLcsNameString() {
    return hasAvp( DiameterRoAvpCodes.LCS_NAME_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#setLcsDataCodingScheme(String)
   */
  public void setLcsDataCodingScheme( String lcsDataCodingScheme ) {
    addAvp(DiameterRoAvpCodes.LCS_DATA_CODING_SCHEME, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsDataCodingScheme);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#setLcsFormatIndicator(net.java.slee.resource.diameter.ro.events.avp.LcsFormatIndicator)
   */
  public void setLcsFormatIndicator( LcsFormatIndicator lcsFormatIndicator ) {
    addAvp(DiameterRoAvpCodes.LCS_FORMAT_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsFormatIndicator.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientName#setLcsNameString(String)
   */
  public void setLcsNameString( String lcsNameString ) {
    addAvp(DiameterRoAvpCodes.LCS_NAME_STRING, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsNameString);
  }

}
