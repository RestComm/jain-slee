/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.diameter.sh.events;

import java.util.Date;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer;
import net.java.slee.resource.diameter.sh.events.avp.DiameterShAvpCodes;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;

/**
 * 
 * Implementation of {@link SubscribeNotificationsAnswer} interface.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SubscribeNotificationsAnswerImpl extends DiameterShMessageImpl implements SubscribeNotificationsAnswer {

  private static final long serialVersionUID = -66848235143082970L;

  /**
   * 
   * @param msg
   */
  public SubscribeNotificationsAnswerImpl(Message msg) {
    super(msg);
    msg.setRequest(false);
    super.longMessageName = "Subscribe-Notification-Answer";
    super.shortMessageName = "SNA";
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#getExpiryTime()
   */
  public Date getExpiryTime() {
    return getAvpAsTime(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#hasExpiryTime()
   */
  public boolean hasExpiryTime() {
    return hasAvp(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#setExpiryTime(java.util.Date)
   */
  public void setExpiryTime(Date expiryTime) {
    addAvp(DiameterShAvpCodes.EXPIRY_TIME, DiameterShAvpCodes.SH_VENDOR_ID, expiryTime);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#hasExperimentalResult()
   */
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#getExperimentalResult()
   */
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(Avp.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  /* 
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.sh.events.SubscribeNotificationsAnswer#setExperimentalResult(net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp)
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) {
    addAvp(Avp.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

}
