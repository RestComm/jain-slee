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

package org.mobicents.slee.resource.diameter.base.events.avp;

import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;

import org.jdiameter.api.Avp;

/**
 * 
 * Implementation of {@link ExperimentalResultAvp}
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class ExperimentalResultAvpImpl extends GroupedAvpImpl implements ExperimentalResultAvp {

  public ExperimentalResultAvpImpl() {
    super();
  }  

  /**
   * 
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public ExperimentalResultAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp#getExperimentalResultCode()
   */
  public long getExperimentalResultCode() {
    return getAvpAsUnsigned32(Avp.EXPERIMENTAL_RESULT_CODE);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp#hasExperimentalResultCode()
   */
  public boolean hasExperimentalResultCode() {
    return hasAvp(Avp.EXPERIMENTAL_RESULT_CODE);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp#setExperimentalResultCode(long)
   */
  public void setExperimentalResultCode(long experimentalResultCode) {
    addAvp(Avp.EXPERIMENTAL_RESULT_CODE, experimentalResultCode);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.avp.DiameterAvpImpl#getVendorId()
   */
  public long getVendorIdAVP() {
    return getAvpAsUnsigned32(Avp.VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp#hasVendorId()
   */
  public boolean hasVendorIdAVP() {
    return hasAvp(Avp.VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp#setVendorId(long)
   */
  public void setVendorIdAVP(long vendorId) {
    addAvp(Avp.VENDOR_ID, vendorId);
  }

}
