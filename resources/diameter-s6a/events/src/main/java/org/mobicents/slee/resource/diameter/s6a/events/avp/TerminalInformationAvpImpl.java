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
import net.java.slee.resource.diameter.s6a.events.avp.TerminalInformationAvp;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link TerminalInformationAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class TerminalInformationAvpImpl extends GroupedAvpImpl implements TerminalInformationAvp {

  public TerminalInformationAvpImpl() {
    super();
  }

  public TerminalInformationAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasIMEI() {
    return hasAvp(DiameterS6aAvpCodes.IMEI, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setIMEI(String imei) {
    addAvp(DiameterS6aAvpCodes.IMEI, DiameterS6aAvpCodes.S6A_VENDOR_ID, imei);
  }

  public String getIMEI() {
    return getAvpAsUTF8String(DiameterS6aAvpCodes.IMEI, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean has3GPP2MEID() {
    return hasAvp(DiameterS6aAvpCodes.TGPP2_MEID, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void set3GPP2MEID(byte[] tgpp2Meid) {
    addAvp(DiameterS6aAvpCodes.TGPP2_MEID, DiameterS6aAvpCodes.S6A_VENDOR_ID, tgpp2Meid);
  }

  public byte[] get3GPP2MEID() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.TGPP2_MEID, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public boolean hasSoftwareVersion() {
    return hasAvp(DiameterS6aAvpCodes.SOFTWARE_VERSION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setSoftwareVersion(String softwareVersion) {
    addAvp(DiameterS6aAvpCodes.SOFTWARE_VERSION, DiameterS6aAvpCodes.S6A_VENDOR_ID, softwareVersion);
  }

  public String getSoftwareVersion() {
    return getAvpAsUTF8String(DiameterS6aAvpCodes.SOFTWARE_VERSION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

}
