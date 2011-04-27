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

import net.java.slee.resource.diameter.ro.events.avp.LcsClientId;
import net.java.slee.resource.diameter.ro.events.avp.LcsClientName;
import net.java.slee.resource.diameter.ro.events.avp.LcsClientType;
import net.java.slee.resource.diameter.ro.events.avp.LcsRequestorId;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * LcsClientIdImpl.java
 *
 * <br>Project:  mobicents
 * <br>3:24:25 AM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */ 
public class LcsClientIdImpl extends GroupedAvpImpl implements LcsClientId {

  public LcsClientIdImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public LcsClientIdImpl( int code, long vendorId, int mnd, int prt, byte[] value )
  {
    super( code, vendorId, mnd, prt, value );
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#getLcsApn()
   */
  public String getLcsApn() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_APN, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#getLcsClientDialedByMs()
   */
  public String getLcsClientDialedByMs() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_CLIENT_DIALED_BY_MS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#getLcsClientExternalId()
   */
  public String getLcsClientExternalId() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.LCS_CLIENT_EXTERNAL_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#getLcsClientName()
   */
  public LcsClientName getLcsClientName() {
    return (LcsClientName) getAvpAsCustom(DiameterRoAvpCodes.LCS_CLIENT_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID, LcsClientNameImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#getLcsClientType()
   */
  public LcsClientType getLcsClientType() {
    return (LcsClientType) getAvpAsEnumerated(DiameterRoAvpCodes.LCS_CLIENT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, LcsClientType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#getLcsRequestorId()
   */
  public LcsRequestorId getLcsRequestorId() {
    return (LcsRequestorId) getAvpAsCustom(DiameterRoAvpCodes.LCS_REQUESTOR_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, LcsRequestorIdImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#hasLcsApn()
   */
  public boolean hasLcsApn() {
    return hasAvp(DiameterRoAvpCodes.LCS_APN, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#hasLcsClientDialedByMs()
   */
  public boolean hasLcsClientDialedByMs() {
    return hasAvp(DiameterRoAvpCodes.LCS_CLIENT_DIALED_BY_MS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#hasLcsClientExternalId()
   */
  public boolean hasLcsClientExternalId() {
    return hasAvp(DiameterRoAvpCodes.LCS_CLIENT_EXTERNAL_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#hasLcsClientName()
   */
  public boolean hasLcsClientName() {
    return hasAvp(DiameterRoAvpCodes.LCS_CLIENT_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#hasLcsClientType()
   */
  public boolean hasLcsClientType() {
    return hasAvp(DiameterRoAvpCodes.LCS_CLIENT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#hasLcsRequestorId()
   */
  public boolean hasLcsRequestorId() {
    return hasAvp(DiameterRoAvpCodes.LCS_REQUESTOR_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#setLcsApn(String)
   */
  public void setLcsApn( String lcsApn ) {
    addAvp(DiameterRoAvpCodes.LCS_APN, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsApn);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#setLcsClientDialedByMs(String)
   */
  public void setLcsClientDialedByMs( String lcsClientDialedByMs ) {
    addAvp(DiameterRoAvpCodes.LCS_CLIENT_DIALED_BY_MS, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsClientDialedByMs);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#setLcsClientExternalId(String)
   */
  public void setLcsClientExternalId( String lcsClientExternalId ) {
    addAvp(DiameterRoAvpCodes.LCS_CLIENT_EXTERNAL_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsClientExternalId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#setLcsClientName(net.java.slee.resource.diameter.ro.events.avp.LcsClientName)
   */
  public void setLcsClientName( LcsClientName lcsClientName ) {
    addAvp(DiameterRoAvpCodes.LCS_CLIENT_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsClientName.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#setLcsClientType(String)
   */
  public void setLcsClientType( LcsClientType lcsClientType ) {
    addAvp(DiameterRoAvpCodes.LCS_CLIENT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsClientType.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.LcsClientId#setLcsRequestorId(net.java.slee.resource.diameter.ro.events.avp.LcsRequestorId)
   */
  public void setLcsRequestorId( LcsRequestorId lcsRequestorId ) {
    addAvp(DiameterRoAvpCodes.LCS_REQUESTOR_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, lcsRequestorId.byteArrayValue());
  }

}
