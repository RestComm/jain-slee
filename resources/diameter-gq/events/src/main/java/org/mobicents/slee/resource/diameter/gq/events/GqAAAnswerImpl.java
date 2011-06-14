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

package org.mobicents.slee.resource.diameter.gq.events;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.gq.events.GqAAAnswer;
import net.java.slee.resource.diameter.gq.events.avp.BindingInformation;
import net.java.slee.resource.diameter.gq.events.avp.ReservationPriority;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.BindingInformationImpl;
import org.mobicents.slee.resource.diameter.gq.events.avp.DiameterGqAvpCodes;






/**
 * <pre>
 * <b>7.1.2 AA-Answer(AAA) command</b>
 * The AAA command, indicated by the Command-Code field set to 265 and the "R" bit cleared in the Command Flags
 * field, is sent by the SPDF to the AF in response to the AAR command.
 * Message Format:
 * &lt;AA-Answer&gt; ::= < Diameter Header: 265, PXY >
 *                 < Session-Id >
 *                 { Auth-Application-Id }
 *                 { Origin-Host }
 *                 { Origin-Realm }
 *                 [ Result-Code ]
 *                 [ Experimental-Result ]
 *                 [ Binding-Information ]
 *                 [ Reservation-Priority ]
 *                 [ Error-Message ]
 *                 [ Error-Reporting-Host ]
 *                 [ Authorization-Lifetime ]
 *                 [ Auth-Grace-Period ]
 *                 [ Failed-AVP ]
 *                 [ Proxy-Info ]
 *                 [ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class GqAAAnswerImpl extends DiameterMessageImpl implements GqAAAnswer {

  public GqAAAnswerImpl(Message message) {
    super(message);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#getAuthorizationLifetime
   */
  public long getAuthorizationLifetime() {
    return getAvpAsUnsigned32(DiameterAvpCodes.AUTHORIZATION_LIFETIME);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#setAuthorizationLifetime
   */
  public void setAuthorizationLifetime(long authorizationLifetime) throws IllegalStateException {
    addAvp(DiameterAvpCodes.AUTHORIZATION_LIFETIME, authorizationLifetime);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#hasAuthorizationLifetime
   */
  public boolean hasAuthorizationLifetime() {
    return hasAvp(DiameterAvpCodes.AUTHORIZATION_LIFETIME);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#hasAuthGracePeriod
   */
  public boolean hasAuthGracePeriod() {
    return hasAvp(DiameterAvpCodes.AUTH_GRACE_PERIOD);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#getAuthGracePeriod
   */
  public long getAuthGracePeriod() {
    return getAvpAsUnsigned32(DiameterAvpCodes.AUTH_GRACE_PERIOD);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#setAuthGracePeriod
   */
  public void setAuthGracePeriod(long authGracePeriod) {
    addAvp(DiameterAvpCodes.AUTH_GRACE_PERIOD, authGracePeriod);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#hasExperimentalResult
   */
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#getExperimentalResult
   */
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(DiameterAvpCodes.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#setExperimentalResult
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) throws IllegalStateException {
    addAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#hasBindingInformation
   */
  public boolean hasBindingInformation() {
    return hasAvp(DiameterGqAvpCodes.ETSI_BINDING_INFORMATION, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#getBindingInformation
   */
  public BindingInformation getBindingInformation() {
    return (BindingInformation) getAvpAsCustom(DiameterGqAvpCodes.ETSI_BINDING_INFORMATION, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        BindingInformationImpl.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#setBindingInformation
   */
  public void setBindingInformation(BindingInformation bindingInformation) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_BINDING_INFORMATION, DiameterGqAvpCodes.ETSI_VENDOR_ID, bindingInformation.byteArrayValue());
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#hasReservationPriority
   */
  public boolean hasReservationPriority() {
    return hasAvp(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#getReservationPriority
   */
  public ReservationPriority getReservationPriority() {
    return (ReservationPriority) getAvpAsEnumerated(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID,
        ReservationPriority.class);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.gq.events.GqAAAnswer#setReservationPriority
   */
  public void setReservationPriority(ReservationPriority reservationPriority) throws IllegalStateException {
    addAvp(DiameterGqAvpCodes.ETSI_RESERVATION_PRIORITY, DiameterGqAvpCodes.ETSI_VENDOR_ID, reservationPriority.getValue());
  }

  @Override
  public String getLongName() {
    return "AA-Answer";
  }

  @Override
  public String getShortName() {
    return "AAA";
  }

}
