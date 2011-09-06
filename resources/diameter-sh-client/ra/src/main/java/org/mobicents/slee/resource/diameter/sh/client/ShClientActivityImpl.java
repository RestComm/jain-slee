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

package org.mobicents.slee.resource.diameter.sh.client;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.sh.DiameterShAvpFactory;
import net.java.slee.resource.diameter.sh.client.ShClientActivity;
import net.java.slee.resource.diameter.sh.client.ShClientMessageFactory;
import net.java.slee.resource.diameter.sh.events.ProfileUpdateRequest;
import net.java.slee.resource.diameter.sh.events.SubscribeNotificationsRequest;
import net.java.slee.resource.diameter.sh.events.UserDataRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Request;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.jdiameter.api.sh.ClientShSession;
import org.jdiameter.common.impl.app.sh.ProfileUpdateRequestImpl;
import org.jdiameter.common.impl.app.sh.SubscribeNotificationsRequestImpl;
import org.jdiameter.common.impl.app.sh.UserDataRequestImpl;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * Sh Client activity created for request/response use casses
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see  ShClientActivity
 */
public class ShClientActivityImpl extends DiameterActivityImpl implements ShClientActivity , StateChangeListener<AppSession> {

  private static final long serialVersionUID = -1182214629020823688L;

  protected transient ClientShSession clientSession = null;
  protected transient DiameterShAvpFactory shAvpFactory = null;
  protected transient ShClientMessageFactory messageFactory = null;

  //Is there any way to add

  public ShClientActivityImpl(ShClientMessageFactory shClientMessageFactory, DiameterShAvpFactory shAvpFactory, ClientShSession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(shClientMessageFactory.getBaseMessageFactory(), shAvpFactory.getBaseFactory(), null, (EventListener<Request, Answer>) session, destinationHost, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(this.clientSession.getSessions().get(0));
    this.shAvpFactory = shAvpFactory;
    this.messageFactory = shClientMessageFactory;
  }

  public void setSession(ClientShSession session) {
    this.clientSession = session;
    this.clientSession.addStateChangeNotification(this);
  }

  public void sendProfileUpdateRequest(ProfileUpdateRequest message) throws IOException {
    try {
      DiameterMessageImpl msg = (DiameterMessageImpl) message;
      clientSession.sendProfileUpdateRequest(new ProfileUpdateRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e.getLocalizedMessage());
    }
  }

  public void sendSubscribeNotificationsRequest(
      SubscribeNotificationsRequest message) throws IOException {
    // FIXME: IMHO this should not be here.
    try {
      DiameterMessageImpl msg = (DiameterMessageImpl) message;
      this.clientSession
      .sendSubscribeNotificationsRequest(new SubscribeNotificationsRequestImpl(
          (Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public void sendUserDataRequest(UserDataRequest message) throws IOException {
    try {
      DiameterMessageImpl msg = (DiameterMessageImpl) message;
      this.clientSession.sendUserDataRequest(new UserDataRequestImpl((Request) msg.getGenericData()));
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  public ShClientMessageFactory getClientMessageFactory() {
    return this.messageFactory;
  }

  public DiameterShAvpFactory getClientAvpFactory() {
    return this.shAvpFactory;
  }

  public void setClientMessageFactory(ShClientMessageFactory v) {
    this.messageFactory = v;
  }

  public void setClientAvpFactory(DiameterShAvpFactory v) {
    this.shAvpFactory = v;
  }
  public String getSessionId() {
    return super.getSessionId();
  }

  public void sendMessage(DiameterMessage message) throws IOException {
    super.sendMessage(message);
  }

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Object, java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(AppSession source, Enum oldState, Enum newState) {
    this.stateChanged(oldState, newState);
  }

  public void stateChanged(Enum oldState, Enum newState) {
    // no state changes, its stateless!
  }

  ClientShSession getClientSession() {
    return this.clientSession;
  }

  @Override
  public void endActivity() {
    this.clientSession.release();
    this.clientSession.removeStateChangeNotification(this);
    super.endActivity();
  }
}
