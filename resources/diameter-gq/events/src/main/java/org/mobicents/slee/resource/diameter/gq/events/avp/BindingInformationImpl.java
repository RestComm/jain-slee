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

package org.mobicents.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.gq.events.avp.BindingInformation;
import net.java.slee.resource.diameter.gq.events.avp.BindingInputList;
import net.java.slee.resource.diameter.gq.events.avp.BindingOutputList;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;



/**
 * Implementation for {@link BindingInformation}
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class BindingInformationImpl extends GroupedAvpImpl implements BindingInformation {

  public BindingInformationImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public BindingInformationImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.BindingInformation#getBindingInputList()
   */
  public BindingInputList getBindingInputList() {
    return (BindingInputList) getAvpAsCustom(DiameterGqAvpCodes.ETSI_BINDING_INPUT_LIST, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        BindingInputListImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.BindingInformation#getBindingOutputList()
   */
  public BindingOutputList getBindingOutputList() {
    return (BindingOutputList) getAvpAsCustom(DiameterGqAvpCodes.ETSI_BINDING_OUTPUT_LIST, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        BindingOutputListImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.BindingInformation#hasBindingInputList()
   */
  public boolean hasBindingInputList() {
    return hasAvp(DiameterGqAvpCodes.ETSI_BINDING_INPUT_LIST, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.BindingInformation#hasBindingOutputList()
   */
  public boolean hasBindingOutputList() {
    return hasAvp(DiameterGqAvpCodes.ETSI_BINDING_OUTPUT_LIST, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.BindingInformation#setBindingInputList()
   */
  public void setBindingInputList(BindingInputList bindingInputList) {
    addAvp(DiameterGqAvpCodes.ETSI_BINDING_INPUT_LIST, DiameterGqAvpCodes.ETSI_VENDOR_ID, bindingInputList.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.avp.BindingInformation#setBindingOutputList()
   */
  public void setBindingOutputList(BindingOutputList bindingOutputList) {
    addAvp(DiameterGqAvpCodes.ETSI_BINDING_OUTPUT_LIST, DiameterGqAvpCodes.ETSI_VENDOR_ID, bindingOutputList.byteArrayValue());
  }
}
