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

package org.mobicents.slee.resource.diameter.rx.events;

import net.java.slee.resource.diameter.rx.events.AAMessage;
import net.java.slee.resource.diameter.rx.events.avp.SupportedFeaturesAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.DiameterRxAvpCodes;
import org.mobicents.slee.resource.diameter.rx.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation of {@link AAMessage} implementation
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @see DiameterMessageImpl
 */
public abstract class AAMessageImpl extends DiameterMessageImpl implements AAMessage {

  /**
   * @param message
   */
  public AAMessageImpl(Message message) {
    super(message);
  }

  @Override
  public boolean hasSupportedFeaturesAvp() {
    return super.hasAvp(DiameterRxAvpCodes.SUPPORTED_FEATURES, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) super.getAvpsAsCustom(DiameterRxAvpCodes.SUPPORTED_FEATURES, DiameterRxAvpCodes.TGPP_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  @Override
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    super.addAvp(DiameterRxAvpCodes.SUPPORTED_FEATURES, DiameterRxAvpCodes.TGPP_VENDOR_ID,supportedFeatures.getExtensionAvps());
  }

  @Override
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    super.setExtensionAvps(supportedFeatureses);
  }

}
