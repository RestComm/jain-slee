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

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.rx.events.ReAuthAnswer;
import net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.DiameterRxAvpCodes;
import org.mobicents.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvpImpl;

/**
 * Implementation of {@link ReAuthAnswer}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @see DiameterMessageImpl
 */
public class ReAuthAnswerImpl extends DiameterMessageImpl implements ReAuthAnswer {

  public ReAuthAnswerImpl(Message message) {
    super(message);
  }

  @Override
  public String getLongName() {
    return "Re-Auth-Answer";
  }

  @Override
  public String getShortName() {
    return "RAA";
  }

  @Override
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  @Override
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(DiameterAvpCodes.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  @Override
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) throws IllegalStateException {
    addAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT,experimentalResult.getExtensionAvps());
  }

  @Override
  public boolean hasMediaComponentDescription() {
    return hasAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setMediaComponentDescription(MediaComponentDescriptionAvp mcd) {
    addAvp(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID, mcd.getExtensionAvps());
  }

  @Override
  public MediaComponentDescriptionAvp[] getMediaComponentDescriptions() {
    return (MediaComponentDescriptionAvp[]) getAvpsAsCustom(DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION, DiameterRxAvpCodes.TGPP_VENDOR_ID,
        MediaComponentDescriptionAvpImpl.class);
  }

  @Override
  public void setMediaComponentDescriptions(MediaComponentDescriptionAvp[] mcds) {
    setExtensionAvps(mcds);
  }

  @Override
  public boolean hasServiceURN() {
    return hasAvp(DiameterRxAvpCodes.SERVICE_URN, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public void setServiceURN(byte[] serviceURN) {
    addAvp(DiameterRxAvpCodes.SERVICE_URN, DiameterRxAvpCodes.TGPP_VENDOR_ID, serviceURN);
  }

  @Override
  public byte[] getServiceURN() {
    return getAvpAsOctetString(DiameterRxAvpCodes.SERVICE_URN, DiameterRxAvpCodes.TGPP_VENDOR_ID);
  }

  @Override
  public boolean hasProxyInfo() {
    return hasAvp(DiameterAvpCodes.PROXY_INFO);
  }

  @Override
  public boolean hasFailedAvp() {
    return hasAvp(DiameterAvpCodes.FAILED_AVP);
  }

}
