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

import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.GrantedServiceUnitAvp;
import net.java.slee.resource.diameter.cca.events.avp.UsedServiceUnitAvp;
import net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;
import org.mobicents.slee.resource.diameter.cca.events.avp.GrantedServiceUnitAvpImpl;
import org.mobicents.slee.resource.diameter.cca.events.avp.UsedServiceUnitAvpImpl;

/**
 * Implementation of {@link SponsoredConnectivityDataAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class SponsoredConnectivityDataAvpImpl extends GroupedAvpImpl implements SponsoredConnectivityDataAvp {

  /**
   * 
   */
  public SponsoredConnectivityDataAvpImpl() {
    super.code = DiameterRxAvpCodes.SPONSORED_CONNECTIVITY_DATA;
    super.vendorId = DiameterRxAvpCodes.TGPP_VENDOR_ID;
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public SponsoredConnectivityDataAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#hasSponsorIdentity()
   */
  @Override
  public boolean hasSponsorIdentity() {
    return super.hasAvp(DiameterRxAvpCodes.SPONSOR_IDENTITY,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#setSponsorIdentity(java.lang.String)
   */
  @Override
  public void setSponsorIdentity(String id) {
    super.addAvp(DiameterRxAvpCodes.SPONSOR_IDENTITY,DiameterRxAvpCodes.TGPP_VENDOR_ID,id);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#getSponsorIdentity()
   */
  @Override
  public String getSponsorIdentity() {
    return super.getAvpAsUTF8String(DiameterRxAvpCodes.SPONSOR_IDENTITY,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#hasApplicationServiceProviderIdentity()
   */
  @Override
  public boolean hasApplicationServiceProviderIdentity() {
    return super.hasAvp(DiameterRxAvpCodes.APPLICATION_SERVICE_PROVIDER_IDENTITY,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#setApplicationServiceProviderIdentity(java.lang.String)
   */
  @Override
  public void setApplicationServiceProviderIdentity(String id) {
    super.addAvp(DiameterRxAvpCodes.APPLICATION_SERVICE_PROVIDER_IDENTITY,DiameterRxAvpCodes.TGPP_VENDOR_ID,id);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#getApplicationServiceProviderIdentity()
   */
  @Override
  public String getApplicationServiceProviderIdentity() {
    return super.getAvpAsUTF8String(DiameterRxAvpCodes.APPLICATION_SERVICE_PROVIDER_IDENTITY,DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#hasGrantedServiceUnit()
   */
  @Override
  public boolean hasGrantedServiceUnit() {
    return super.hasAvp(CreditControlAVPCodes.Granted_Service_Unit);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#setGrantedServiceUnit(net.java.slee.resource.diameter.cca.events.avp.GrantedServiceUnitAvp)
   */
  @Override
  public void setGrantedServiceUnit(GrantedServiceUnitAvp gsu) {
    super.addAvp(CreditControlAVPCodes.Granted_Service_Unit,gsu.getExtensionAvps());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#getGrantedServiceUnit()
   */
  @Override
  public GrantedServiceUnitAvp getGrantedServiceUnit() {
    return (GrantedServiceUnitAvp)super.getAvpAsCustom(CreditControlAVPCodes.Granted_Service_Unit, GrantedServiceUnitAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#getUsedServiceUnit()
   */
  @Override
  public UsedServiceUnitAvp getUsedServiceUnit() {
    return (UsedServiceUnitAvp)super.getAvpAsCustom(CreditControlAVPCodes.Used_Service_Unit, UsedServiceUnitAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#setUsedServiceUnit(net.java.slee.resource.diameter.cca.events.avp.UsedServiceUnitAvp)
   */
  @Override
  public void setUsedServiceUnit(UsedServiceUnitAvp usedServiceUnit) {
    super.addAvp(CreditControlAVPCodes.Used_Service_Unit,usedServiceUnit.getExtensionAvps());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp#hasUsedServiceUnit()
   */
  @Override
  public boolean hasUsedServiceUnit() {
    return super.hasAvp(CreditControlAVPCodes.Used_Service_Unit);
  }

}
