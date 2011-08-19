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

package org.mobicents.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.CapabilitiesExchangeMessage;
import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvpImpl;

/**
 * Super class defining common methods for CER and CEA. Implements methods {@link CapabilitiesExchangeMessage}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see DiameterMessageImpl
 */
public abstract class CapabilitiesExchangeMessageImpl extends DiameterMessageImpl implements CapabilitiesExchangeMessage {

  /**
   * 
   * @param message
   */
  public CapabilitiesExchangeMessageImpl(Message message) {
    super(message);
  }

  public long[] getAcctApplicationIds() {
    return getAvpsAsUnsigned32(Avp.ACCT_APPLICATION_ID);
  }

  public long[] getAuthApplicationIds() {
    return getAvpsAsUnsigned32(Avp.AUTH_APPLICATION_ID);
  }

  public long getFirmwareRevision() {
    return getAvpAsUnsigned32(Avp.FIRMWARE_REVISION);
  }

  public Address[] getHostIpAddresses() {
    return getAvpsAsAddress(Avp.HOST_IP_ADDRESS);
  }

  public long[] getInbandSecurityIds() {
    return getAvpsAsUnsigned32(Avp.INBAND_SECURITY_ID);
  }

  public String getProductName() {
    return getAvpAsUTF8String(Avp.PRODUCT_NAME);
  }

  public long[] getSupportedVendorIds() {
    return getAvpsAsUnsigned32(Avp.SUPPORTED_VENDOR_ID);
  }

  public long getVendorId() {
    return getAvpAsUnsigned32(Avp.VENDOR_ID);
  }

  public VendorSpecificApplicationIdAvp[] getVendorSpecificApplicationIds() {
    return (VendorSpecificApplicationIdAvp[]) getAvpsAsCustom(Avp.VENDOR_SPECIFIC_APPLICATION_ID, VendorSpecificApplicationIdAvpImpl.class);
  }

  public boolean hasFirmwareRevision() {
    return hasAvp(Avp.FIRMWARE_REVISION);
  }

  public boolean hasProductName() {
    return hasAvp(Avp.PRODUCT_NAME);
  }

  public boolean hasVendorId() {
    return hasAvp(Avp.VENDOR_ID);
  }

  public void setAcctApplicationIds(long[] acctApplicationIds) {
    DiameterAvp[] values = new DiameterAvp[acctApplicationIds.length];

    for(int index = 0; index < acctApplicationIds.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.ACCT_APPLICATION_ID, acctApplicationIds[index]);
    }

    super.message.getAvps().removeAvp(Avp.ACCT_APPLICATION_ID);
    super.setExtensionAvps(values);
  }

  public void setAuthApplicationIds(long[] authApplicationIds) {
    DiameterAvp[] values = new DiameterAvp[authApplicationIds.length];

    for(int index = 0; index < authApplicationIds.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.AUTH_APPLICATION_ID, authApplicationIds[index]);
    }

    super.message.getAvps().removeAvp(Avp.AUTH_APPLICATION_ID);
    super.setExtensionAvps(values);
  }

  public void setFirmwareRevision(long firmwareRevision) {
    addAvp(Avp.FIRMWARE_REVISION, firmwareRevision);
  }

  public void setHostIpAddress(Address hostIpAddress) {
    addAvp(Avp.HOST_IP_ADDRESS, hostIpAddress.encode());
  }

  public void setHostIpAddresses(Address[] hostIpAddresses) {
    DiameterAvp[] values = new DiameterAvp[hostIpAddresses.length];

    for(int index = 0; index < hostIpAddresses.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.HOST_IP_ADDRESS, hostIpAddresses[index].encode()); //is this correct?
    }

    super.message.getAvps().removeAvp(Avp.HOST_IP_ADDRESS);
    super.setExtensionAvps(values);
  }

  public void setInbandSecurityId(long inbandSecurityId) {
    addAvp(Avp.INBAND_SECURITY_ID, inbandSecurityId);
  }

  public void setInbandSecurityIds(long[] inbandSecurityIds) {
    DiameterAvp[] values = new DiameterAvp[inbandSecurityIds.length];

    for(int index = 0; index < inbandSecurityIds.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.INBAND_SECURITY_ID, inbandSecurityIds[index]);
    }

    super.message.getAvps().removeAvp(Avp.INBAND_SECURITY_ID);
    super.setExtensionAvps(values);
  }

  public void setProductName(String productName) {
    addAvp(Avp.PRODUCT_NAME, productName);
  }

  public void setSupportedVendorId(long supportedVendorId) {
    addAvp(Avp.SUPPORTED_VENDOR_ID, supportedVendorId);
  }

  public void setSupportedVendorIds(long[] supportedVendorIds) {
    DiameterAvp[] values = new DiameterAvp[supportedVendorIds.length];

    for(int index = 0; index < supportedVendorIds.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.SUPPORTED_VENDOR_ID, supportedVendorIds[index]);
    }

    super.message.getAvps().removeAvp(Avp.SUPPORTED_VENDOR_ID);
    super.setExtensionAvps(values);
  }

  public void setVendorId(long vendorId) {
    addAvp(Avp.VENDOR_ID, vendorId);
  }

  public void setVendorSpecificApplicationIds(VendorSpecificApplicationIdAvp[] vendorSpecificApplicationIds) {
    DiameterAvp[] values = new DiameterAvp[vendorSpecificApplicationIds.length];

    for(int index = 0; index < vendorSpecificApplicationIds.length; index++) {
      values[index] = AvpUtilities.createAvp(Avp.VENDOR_SPECIFIC_APPLICATION_ID, vendorSpecificApplicationIds[index].getExtensionAvps());
    }

    super.message.getAvps().removeAvp(Avp.VENDOR_SPECIFIC_APPLICATION_ID);
    super.setExtensionAvps(values);
  }

  public Address getHostIpAddress() {
    return getAvpAsAddress(Avp.HOST_IP_ADDRESS);
  }

  public long getInbandSecurityId() {
    return getAvpAsUnsigned32(Avp.INBAND_SECURITY_ID);
  }

  public boolean hasHostIpAddress() {
    return hasAvp(Avp.HOST_IP_ADDRESS);
  }

  public boolean hasInbandSecurityId() {
    return hasAvp(Avp.INBAND_SECURITY_ID);
  }

  public boolean hasSupportedVendorId() {
    return hasAvp(Avp.SUPPORTED_VENDOR_ID);
  }

}
