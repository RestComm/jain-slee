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

package org.mobicents.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.rf.events.avp.LcsClientId;
import net.java.slee.resource.diameter.rf.events.avp.LcsClientName;
import net.java.slee.resource.diameter.rf.events.avp.LcsClientType;
import net.java.slee.resource.diameter.rf.events.avp.LcsRequestorId;

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
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#getLcsApn()
   */
  public String getLcsApn() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.LCS_APN, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#getLcsClientDialedByMs()
   */
  public String getLcsClientDialedByMs() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.LCS_CLIENT_DIALED_BY_MS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#getLcsClientExternalId()
   */
  public String getLcsClientExternalId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.LCS_CLIENT_EXTERNAL_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#getLcsClientName()
   */
  public LcsClientName getLcsClientName() {
    return (LcsClientName) getAvpAsCustom(DiameterRfAvpCodes.LCS_CLIENT_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID, LcsClientNameImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#getLcsClientType()
   */
  public LcsClientType getLcsClientType() {
    return (LcsClientType) getAvpAsEnumerated(DiameterRfAvpCodes.LCS_CLIENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, LcsClientType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#getLcsRequestorId()
   */
  public LcsRequestorId getLcsRequestorId() {
    return (LcsRequestorId) getAvpAsCustom(DiameterRfAvpCodes.LCS_REQUESTOR_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, LcsRequestorIdImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#hasLcsApn()
   */
  public boolean hasLcsApn() {
    return hasAvp(DiameterRfAvpCodes.LCS_APN, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#hasLcsClientDialedByMs()
   */
  public boolean hasLcsClientDialedByMs() {
    return hasAvp(DiameterRfAvpCodes.LCS_CLIENT_DIALED_BY_MS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#hasLcsClientExternalId()
   */
  public boolean hasLcsClientExternalId() {
    return hasAvp(DiameterRfAvpCodes.LCS_CLIENT_EXTERNAL_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#hasLcsClientName()
   */
  public boolean hasLcsClientName() {
    return hasAvp(DiameterRfAvpCodes.LCS_CLIENT_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#hasLcsClientType()
   */
  public boolean hasLcsClientType() {
    return hasAvp(DiameterRfAvpCodes.LCS_CLIENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#hasLcsRequestorId()
   */
  public boolean hasLcsRequestorId() {
    return hasAvp(DiameterRfAvpCodes.LCS_REQUESTOR_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#setLcsApn(String)
   */
  public void setLcsApn( String lcsApn ) {
    addAvp(DiameterRfAvpCodes.LCS_APN, DiameterRfAvpCodes.TGPP_VENDOR_ID, lcsApn);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#setLcsClientDialedByMs(String)
   */
  public void setLcsClientDialedByMs( String lcsClientDialedByMs ) {
    addAvp(DiameterRfAvpCodes.LCS_CLIENT_DIALED_BY_MS, DiameterRfAvpCodes.TGPP_VENDOR_ID, lcsClientDialedByMs);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#setLcsClientExternalId(String)
   */
  public void setLcsClientExternalId( String lcsClientExternalId ) {
    addAvp(DiameterRfAvpCodes.LCS_CLIENT_EXTERNAL_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, lcsClientExternalId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#setLcsClientName(net.java.slee.resource.diameter.rf.events.avp.LcsClientName)
   */
  public void setLcsClientName( LcsClientName lcsClientName ) {
    addAvp(DiameterRfAvpCodes.LCS_CLIENT_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID, lcsClientName.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#setLcsClientType(String)
   */
  public void setLcsClientType( LcsClientType lcsClientType ) {
    addAvp(DiameterRfAvpCodes.LCS_CLIENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, lcsClientType.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.LcsClientId#setLcsRequestorId(net.java.slee.resource.diameter.rf.events.avp.LcsRequestorId)
   */
  public void setLcsRequestorId( LcsRequestorId lcsRequestorId ) {
    addAvp(DiameterRfAvpCodes.LCS_REQUESTOR_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, lcsRequestorId.byteArrayValue());
  }

}
