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

package org.mobicents.slee.resource.diameter.sh.events;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateAnswer;
import net.java.slee.resource.diameter.sh.events.PushNotificationRequest;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;

/**
 * 
 * Implementation of {@link PushNotificationRequest} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileUpdateAnswerImpl extends DiameterShMessageImpl implements ProfileUpdateAnswer {

  private static final long serialVersionUID = -6827447082325672602L;

  public ProfileUpdateAnswerImpl(Message msg) {
    super(msg);
    msg.setRequest(false);
    msg.setReTransmitted(false); // just in case. answers never have T flag set
    super.longMessageName = "Profile-Update-Answer";
    super.shortMessageName = "PUA";
  }

  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(Avp.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) {
    addAvp(Avp.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateAnswer#hasWildcardedPSI()
   */
  public boolean hasWildcardedPSI() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateAnswer#getWildcardedPSI()
   */
  public String getWildcardedPSI() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateAnswer#setWildcardedPSI(String)
   */
  public void setWildcardedPSI(String wildcardedPSI) {
    addAvp(DiameterShAvpCodes.WILDCARDED_PSI, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedPSI);
  }
  
  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateAnswer#hasWildcardedIMPU()
   */
  public boolean hasWildcardedIMPU() {
    return hasAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateAnswer#getWildcardedIMPU()
   */
  public String getWildcardedIMPU() {
    return getAvpAsUTF8String(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.server.events.ProfileUpdateAnswer#setWildcardedIMPU(String)
   */
  public void setWildcardedIMPU(String wildcardedIMPU) {
    addAvp(DiameterShAvpCodes.WILDCARDED_IMPU, DiameterShAvpCodes.SH_VENDOR_ID, wildcardedIMPU);
  }
}
