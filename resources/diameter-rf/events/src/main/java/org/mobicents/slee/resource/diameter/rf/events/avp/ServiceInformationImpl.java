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

import net.java.slee.resource.diameter.rf.events.avp.ImsInformation;
import net.java.slee.resource.diameter.rf.events.avp.LcsInformation;
import net.java.slee.resource.diameter.rf.events.avp.MbmsInformation;
import net.java.slee.resource.diameter.rf.events.avp.MmsInformation;
import net.java.slee.resource.diameter.rf.events.avp.PocInformation;
import net.java.slee.resource.diameter.rf.events.avp.PsInformation;
import net.java.slee.resource.diameter.rf.events.avp.ServiceInformation;
import net.java.slee.resource.diameter.rf.events.avp.WlanInformation;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;


/**
 * ServiceInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>3:17:26 PM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServiceInformationImpl extends GroupedAvpImpl implements ServiceInformation {

  public ServiceInformationImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public ServiceInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#getImsInformation()
   */
  public ImsInformation getImsInformation() {
    return (ImsInformation) getAvpAsCustom(DiameterRfAvpCodes.IMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, ImsInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#getLcsInformation()
   */
  public LcsInformation getLcsInformation() {
    return (LcsInformation) getAvpAsCustom(DiameterRfAvpCodes.LCS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, LcsInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#getMbmsInformation()
   */
  public MbmsInformation getMbmsInformation() {
    return (MbmsInformation) getAvpAsCustom(DiameterRfAvpCodes.MBMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, MbmsInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#getMmsInformation()
   */
  public MmsInformation getMmsInformation() {
    return (MmsInformation) getAvpAsCustom(DiameterRfAvpCodes.MMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, MmsInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#getPocInformation()
   */
  public PocInformation getPocInformation() {
    return (PocInformation) getAvpAsCustom(DiameterRfAvpCodes.POC_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, PocInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#getPsInformation()
   */
  public PsInformation getPsInformation() {
    return (PsInformation) getAvpAsCustom(DiameterRfAvpCodes.PS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, PsInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#getWlanInformation()
   */
  public WlanInformation getWlanInformation() {
    return (WlanInformation) getAvpAsCustom(DiameterRfAvpCodes.WLAN_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, WlanInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#hasImsInformation()
   */
  public boolean hasImsInformation() {
    return hasAvp( DiameterRfAvpCodes.IMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#hasLcsInformation()
   */
  public boolean hasLcsInformation() {
    return hasAvp( DiameterRfAvpCodes.LCS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#hasMbmsInformation()
   */
  public boolean hasMbmsInformation() {
    return hasAvp( DiameterRfAvpCodes.MBMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#hasMmsInformation()
   */
  public boolean hasMmsInformation() {
    return hasAvp( DiameterRfAvpCodes.MMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#hasPocInformation()
   */
  public boolean hasPocInformation() {
    return hasAvp( DiameterRfAvpCodes.POC_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#hasPsInformation()
   */
  public boolean hasPsInformation() {
    return hasAvp( DiameterRfAvpCodes.PS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#hasWlanInformation()
   */
  public boolean hasWlanInformation() {
    return hasAvp( DiameterRfAvpCodes.WLAN_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#setImsInformation(net.java.slee.resource.diameter.rf.events.avp.ImsInformation)
   */
  public void setImsInformation( ImsInformation imsInformation ) {
    addAvp(DiameterRfAvpCodes.IMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, imsInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#setLcsInformation(net.java.slee.resource.diameter.rf.events.avp.LcsInformation)
   */
  public void setLcsInformation( LcsInformation lcsInformation ) {
    addAvp(DiameterRfAvpCodes.LCS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, lcsInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#setMbmsInformation(net.java.slee.resource.diameter.rf.events.avp.MbmsInformation)
   */
  public void setMbmsInformation( MbmsInformation mbmsInformation ) {
    addAvp(DiameterRfAvpCodes.MBMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, mbmsInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#setMmsInformation(net.java.slee.resource.diameter.rf.events.avp.MmsInformation)
   */
  public void setMmsInformation( MmsInformation mmsInformation ) {
    addAvp(DiameterRfAvpCodes.MMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, mmsInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#setPocInformation(net.java.slee.resource.diameter.rf.events.avp.PocInformation)
   */
  public void setPocInformation( PocInformation mmsInformation ) {
    addAvp(DiameterRfAvpCodes.POC_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, mmsInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#setPsInformation(net.java.slee.resource.diameter.rf.events.avp.PsInformation)
   */
  public void setPsInformation( PsInformation psInformation ) {
    addAvp(DiameterRfAvpCodes.PS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, psInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.ServiceInformation#setWlanInformation(net.java.slee.resource.diameter.rf.events.avp.WlanInformation)
   */
  public void setWlanInformation( WlanInformation wlanInformation ) {
    addAvp(DiameterRfAvpCodes.WLAN_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, wlanInformation.byteArrayValue());
  }

}
