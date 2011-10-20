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

import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.EPSLocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.MMELocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SGSNLocationInformationAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link EPSLocationInformationAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EPSLocationInformationAvpImpl extends GroupedAvpImpl implements EPSLocationInformationAvp {

  public EPSLocationInformationAvpImpl() {
    super();
  }

  public EPSLocationInformationAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasMMELocationInformation() {
    return hasAvp(DiameterS6aAvpCodes.MME_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setMMELocationInformation(MMELocationInformationAvp mmeli) {
    addAvp(DiameterS6aAvpCodes.MME_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, mmeli.byteArrayValue());
  }

  public MMELocationInformationAvp getMMELocationInformation() {
    return (MMELocationInformationAvp) getAvpAsCustom(DiameterS6aAvpCodes.MME_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, MMELocationInformationAvpImpl.class);
  }

  public boolean hasSGSNLocationInformation() {
    return hasAvp(DiameterS6aAvpCodes.SGSN_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setSGSNLocationInformation(SGSNLocationInformationAvp sgsnli) {
    addAvp(DiameterS6aAvpCodes.SGSN_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, sgsnli.byteArrayValue());
  }

  public SGSNLocationInformationAvp getSGSNLocationInformation() {
    return (SGSNLocationInformationAvp) getAvpAsCustom(DiameterS6aAvpCodes.SGSN_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, SGSNLocationInformationAvpImpl.class);
  }

}
