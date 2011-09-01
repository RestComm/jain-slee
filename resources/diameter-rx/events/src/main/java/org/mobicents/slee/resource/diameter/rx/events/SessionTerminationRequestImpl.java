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

import net.java.slee.resource.diameter.rx.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;

/**
 * Implementation of {@link SessionTerminationRequest}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @see SessionTerminationMessageImpl
 */
public class SessionTerminationRequestImpl  extends SessionTerminationMessageImpl implements SessionTerminationRequest{

  public SessionTerminationRequestImpl(Message message) {
    super(message);
  }

  @Override
  public String getLongName() {
    return "Session-Termination-Request";
  }

  @Override
  public String getShortName() {
    return "STR";
  }

  public boolean hasTerminationCause() {
    return hasAvp(Avp.TERMINATION_CAUSE);
  }

  public TerminationCauseType getTerminationCause() {
    return (TerminationCauseType) getAvpAsEnumerated(Avp.TERMINATION_CAUSE, TerminationCauseType.class);
  }

  public void setTerminationCause(TerminationCauseType terminationCause) {
    addAvp(Avp.TERMINATION_CAUSE, terminationCause.getValue());
  }

  @Override
  public boolean hasRouteRecord() {
    return super.hasAvp(DiameterAvpCodes.ROUTE_RECORD);
  }
}
