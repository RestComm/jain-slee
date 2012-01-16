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
import net.java.slee.resource.diameter.s6a.events.avp.EUTRANVectorAvp;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link EUTRANVectorAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class EUTRANVectorAvpImpl extends GroupedAvpImpl implements EUTRANVectorAvp {

  public EUTRANVectorAvpImpl() {
    super();
  }

  public EUTRANVectorAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  @Override
  public boolean hasRAND() {
    return hasAvp(DiameterS6aAvpCodes.RAND, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public byte[] getRAND() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.RAND, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public void setRAND(byte[] RAND) {
    addAvp(DiameterS6aAvpCodes.RAND, DiameterS6aAvpCodes.S6A_VENDOR_ID, RAND);
  }

  @Override
  public boolean hasXRES() {
    return hasAvp(DiameterS6aAvpCodes.XRES, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public byte[] getXRES() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.XRES, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public void setXRES(byte[] XRES) {
    addAvp(DiameterS6aAvpCodes.XRES, DiameterS6aAvpCodes.S6A_VENDOR_ID, XRES);
  }

  @Override
  public boolean hasAUTN() {
    return hasAvp(DiameterS6aAvpCodes.AUTN, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public byte[] getAUTN() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.AUTN, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public void setAUTN(byte[] AUTN) {
    addAvp(DiameterS6aAvpCodes.AUTN, DiameterS6aAvpCodes.S6A_VENDOR_ID, AUTN);
  }

  @Override
  public boolean hasKASME() {
    return hasAvp(DiameterS6aAvpCodes.KASME, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public byte[] getKASME() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.KASME, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public void setKASME(byte[] KASME) {
    addAvp(DiameterS6aAvpCodes.KASME, DiameterS6aAvpCodes.S6A_VENDOR_ID, KASME);
  }

  @Override
  public long getItemNumber() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.ITEM_NUMBER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  @Override
  public void setItemNumber(long itemNumber) {
    addAvp(DiameterS6aAvpCodes.ITEM_NUMBER, DiameterS6aAvpCodes.S6A_VENDOR_ID, itemNumber);
  }

  @Override
  public boolean hasItemNumber() {
    return hasAvp(DiameterS6aAvpCodes.ITEM_NUMBER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }
}
