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

package org.mobicents.slee.resource.diameter.rx.events.avp;

import net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp;
import net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation of {@link AcceptableServiceInfoAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class AcceptableServiceInfoAvpImpl extends GroupedAvpImpl implements AcceptableServiceInfoAvp {

  /**
   * 
   */
  public AcceptableServiceInfoAvpImpl() {
    super.code = DiameterRxAvpCodes.ACCEPTABLE_SERVICE_INFO;
    super.vendorId = DiameterRxAvpCodes.TGPP_VENDOR_ID;
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public AcceptableServiceInfoAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#hasMediaComponentDescription()
   */
  @Override
  public boolean hasMediaComponentDescription() {
    return super.hasAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#setMediaComponentDescription(net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp)
   */
  @Override
  public void setMediaComponentDescription(MediaComponentDescriptionAvp mcd) {
    super.addAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID, mcd.getExtensionAvps());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#setMediaComponentDescriptions(net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp[])
   */
  @Override
  public void setMediaComponentDescriptions(MediaComponentDescriptionAvp[] mcds) {
    super.setExtensionAvps(mcds);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#getMediaComponentDescriptions()
   */
  @Override
  public MediaComponentDescriptionAvp[] getMediaComponentDescriptions() {
    return (MediaComponentDescriptionAvp[])super.getAvpsAsCustom(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID, MediaComponentDescriptionAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#hasMaxRequestedBandwidthDL()
   */
  @Override
  public boolean hasMaxRequestedBandwidthDL() {
    return super.hasAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#setMaxRequestedBandwidthDL(long)
   */
  @Override
  public void setMaxRequestedBandwidthDL(long mrbdl) {
    super.addAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterRxAvpCodes.TGPP_VENDOR_ID,mrbdl);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#getMaxRequestedBandwidthDL()
   */
  @Override
  public long getMaxRequestedBandwidthDL() {
    return super.getAvpAsUnsigned32(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_DL, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#hasMaxRequestedBandwidthUL()
   */
  @Override
  public boolean hasMaxRequestedBandwidthUL() {
    return super.hasAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#setMaxRequestedBandwidthUL(long)
   */
  @Override
  public void setMaxRequestedBandwidthUL(long mrbUL) {
    super.addAvp(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterRxAvpCodes.TGPP_VENDOR_ID,mrbUL);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp#getMaxRequestedBandwidthUL()
   */
  @Override
  public long getMaxRequestedBandwidthUL() {
    return super.getAvpAsUnsigned32(DiameterRxAvpCodes.MAX_REQUESTED_BANDWIDTH_UL, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

}
