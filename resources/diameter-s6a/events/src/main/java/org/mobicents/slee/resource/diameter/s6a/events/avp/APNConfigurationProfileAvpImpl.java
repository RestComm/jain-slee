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

package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.APNConfigurationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.APNConfigurationProfileAvp;
import net.java.slee.resource.diameter.s6a.events.avp.AllAPNConfigurationsIncludedIndicator;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link APNConfigurationProfileAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class APNConfigurationProfileAvpImpl extends GroupedAvpImpl implements APNConfigurationProfileAvp {

  public APNConfigurationProfileAvpImpl() {
    super();
  }

  public APNConfigurationProfileAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasContextIdentifier() {
    return hasAvp(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public long getContextIdentifier() {
    return this.getAvpAsUnsigned32(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setContextIdentifier(long contextIdentifier) {
    addAvp(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID, contextIdentifier);
  }

  public boolean hasAPNConfiguration() {
    return hasAvp(DiameterS6aAvpCodes.APN_CONFIGURATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public APNConfigurationAvp getAPNConfiguration() {
    return (APNConfigurationAvp) getAvpAsCustom(DiameterS6aAvpCodes.APN_CONFIGURATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, APNConfigurationAvpImpl.class);
  }

  public void setAPNConfiguration(APNConfigurationAvp apnconfig) {
    addAvp(DiameterS6aAvpCodes.APN_CONFIGURATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, apnconfig.byteArrayValue());
  }

  public boolean hasAllAPNConfigurationsIncludedIndicator() {
    return hasAvp(DiameterS6aAvpCodes.ALL_APN_CONFIGURATIONS_INCLUDED_INDICATOR, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public AllAPNConfigurationsIncludedIndicator getAllAPNConfigurationsIncludedIndicator() {
    return (AllAPNConfigurationsIncludedIndicator) getAvpAsEnumerated(DiameterS6aAvpCodes.ALL_APN_CONFIGURATIONS_INCLUDED_INDICATOR, DiameterS6aAvpCodes.S6A_VENDOR_ID, AllAPNConfigurationsIncludedIndicator.class);
  }

  public void setAllAPNConfigurationsIncludedIndicator(AllAPNConfigurationsIncludedIndicator indicator) {
    addAvp(DiameterS6aAvpCodes.ALL_APN_CONFIGURATIONS_INCLUDED_INDICATOR, DiameterS6aAvpCodes.S6A_VENDOR_ID, indicator.getValue());
  }
}
