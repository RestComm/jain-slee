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

package org.mobicents.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.DisconnectPeerRequest;
import net.java.slee.resource.diameter.base.events.avp.DisconnectCauseType;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;

/**
 * 
 * Implementation of {@link DisconnectPeerRequest}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @see DiameterMessageImpl
 */
public class DisconnectPeerRequestImpl extends DiameterMessageImpl implements DisconnectPeerRequest {

  public DisconnectPeerRequestImpl(Message message) {
    super(message);
  }

  @Override
  public String getLongName() {
    return "Disconnect-Peer-Request";
  }

  @Override
  public String getShortName() {
    return "DPR";
  }

  public DisconnectCauseType getDisconnectCause() {
    return (DisconnectCauseType) getAvpAsEnumerated(Avp.DISCONNECT_CAUSE, DisconnectCauseType.class);
  }

  public boolean hasDisconnectCause() {
    return hasAvp(Avp.DISCONNECT_CAUSE);
  }

  public void setDisconnectCause(DisconnectCauseType disconnectCause) {
    addAvp(Avp.DISCONNECT_CAUSE, disconnectCause.getValue());
  }

}
