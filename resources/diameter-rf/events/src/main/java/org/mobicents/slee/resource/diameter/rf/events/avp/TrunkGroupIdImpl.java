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

import net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * TrunkGroupIdImpl.java
 *
 * <br>Project:  mobicents
 * <br>1:52:51 AM Apr 12, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class TrunkGroupIdImpl extends GroupedAvpImpl implements TrunkGroupId {

  public TrunkGroupIdImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public TrunkGroupIdImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId#getIncomingTrunkGroupId()
   */
  public String getIncomingTrunkGroupId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.INCOMING_TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId#getOutgoingTrunkGroupId()
   */
  public String getOutgoingTrunkGroupId() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.OUTGOING_TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId#hasIncomingTrunkGroupId()
   */
  public boolean hasIncomingTrunkGroupId() {
    return hasAvp( DiameterRfAvpCodes.INCOMING_TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId#hasOutgoingTrunkGroupId()
   */
  public boolean hasOutgoingTrunkGroupId() {
    return hasAvp( DiameterRfAvpCodes.OUTGOING_TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId#setIncomingTrunkGroupId(String)
   */
  public void setIncomingTrunkGroupId( String incomingTrunkGroupId ) {
    addAvp(DiameterRfAvpCodes.INCOMING_TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, incomingTrunkGroupId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId#setOutgoingTrunkGroupId(String)
   */
  public void setOutgoingTrunkGroupId( String outgoingTrunkGroupId ) {
    addAvp(DiameterRfAvpCodes.OUTGOING_TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, outgoingTrunkGroupId);
  }

}
