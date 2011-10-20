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

package org.mobicents.slee.resource.diameter.s6a;

import java.util.ArrayList;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.s6a.S6aAVPFactory;
import net.java.slee.resource.diameter.s6a.S6aMessageFactory;
import net.java.slee.resource.diameter.s6a.S6aSessionActivity;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;

/**
 * Implementation for {@link S6aSessionActivity}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public abstract class S6aSessionImpl extends DiameterActivityImpl implements S6aSessionActivity, StateChangeListener<AppSession>{

  private static final long serialVersionUID = 4374137032596394588L;

  protected boolean terminated = false;

  protected transient S6aMessageFactoryImpl s6aMessageFactory = null;
  protected transient S6aAVPFactory s6aAvpFactory = null;

  protected transient DiameterMessage lastRequest = null;
  protected transient ArrayList<DiameterAvp> sessionAvps = new ArrayList<DiameterAvp>();

  public S6aSessionImpl(S6aMessageFactory messageFactory, S6aAVPFactory avpFactory, Session session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(null, null, session, raEventListener, destinationHost, destinationRealm);

    this.s6aMessageFactory = (S6aMessageFactoryImpl) messageFactory;
    this.s6aAvpFactory = avpFactory;
  }

  public S6aAVPFactory getS6aAvpFactory() {
    return this.s6aAvpFactory;
  }

  public S6aMessageFactory getS6aMessageFactory() {
    return this.s6aMessageFactory;
  }

  public void setS6aMessageFactory(S6aMessageFactoryImpl s6aMessageFactory) {
    this.s6aMessageFactory = s6aMessageFactory;
  }

  public void setS6aAvpFactory(S6aAVPFactory s6aAvpFactory) {
    this.s6aAvpFactory = s6aAvpFactory;
  }

  public String getSessionId() {
    return session.getSessionId();
  }

  public void fetchSessionData(DiameterMessage s6aRequest) {
    this.lastRequest = s6aRequest;
  }

  /**
   * Fills message with session AVPs if present and/or needed.
   * 
   * @param message
   */
  protected void fillSessionAVPs(DiameterMessage message) {
    // If there's Destination-Host/Realm, add the AVPs
    if(message.getHeader().isRequest()) {
      if (destinationHost != null) {
        message.setDestinationHost(destinationHost);
      }

      if (destinationRealm != null) {
        message.setDestinationRealm(destinationRealm);
      }
    }

    // Fill extension avps if present
    if (sessionAvps.size() > 0) {
      try {
        message.setExtensionAvps(sessionAvps.toArray(new DiameterAvp[sessionAvps.size()]));
      }
      catch (AvpNotAllowedException e) {
        logger.error("Failed to add Session AVPs to request.", e);
      }
    }

    // Guarantee session-id is present
    if (!message.hasSessionId() && sessionId != null) {
      message.setSessionId(sessionId);
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + (terminated ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    S6aSessionImpl other = (S6aSessionImpl) obj;
    if (terminated != other.terminated) {
      return false;
    }
    return true;
  }

}
