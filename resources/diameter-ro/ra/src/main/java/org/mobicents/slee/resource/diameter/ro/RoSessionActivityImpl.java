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

package org.mobicents.slee.resource.diameter.ro;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.ro.RoAvpFactory;
import net.java.slee.resource.diameter.ro.RoMessageFactory;
import net.java.slee.resource.diameter.ro.RoSessionActivity;
import net.java.slee.resource.diameter.ro.events.RoCreditControlRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.ro.events.RoCreditControlAnswerImpl;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class RoSessionActivityImpl extends DiameterActivityImpl implements RoSessionActivity {

  private static final long serialVersionUID = 5037967180962414949L;

  protected transient RoMessageFactory roMessageFactory;

  /**
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param raEventListener
   * @param destinationHost
   * @param destinationRealm
   */
  public RoSessionActivityImpl(RoMessageFactory roMessageFactory, RoAvpFactory roAvpFactory, Session session, EventListener<Request, Answer> raEventListener,
      DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(roMessageFactory.getBaseMessageFactory(), roAvpFactory.getBaseFactory(), session, raEventListener, destinationHost, destinationRealm);
    this.roMessageFactory = roMessageFactory;
  }

  @Override
  public RoMessageFactory getRoMessageFactory() {
    return this.roMessageFactory;
  }

  public void setRoMessageFactory(RoMessageFactory roMessageFactory) {
    this.roMessageFactory = roMessageFactory;
  }

  public void setDestinationHost(DiameterIdentity destinationHost) {
    super.destinationHost = destinationHost;
  }

  public void setDestinationRealm(DiameterIdentity destinationRealm) {
    super.destinationRealm = destinationRealm;
  }

  @Override
  public DiameterMessage sendSyncMessage(DiameterMessage message) {
    DiameterMessage answer = null;

    Message receivedMessage = doSendMessage(message);

    if(receivedMessage != null) {
      if (!receivedMessage.isRequest()) {
        if(receivedMessage.isError()) {
          answer = new ErrorAnswerImpl(receivedMessage);
        }
        else {
          switch (receivedMessage.getCommandCode()) {
            case RoCreditControlRequest.commandCode:
              answer = new RoCreditControlAnswerImpl(receivedMessage);
              break;
            default:
              logger.error("Received an unknown type of Message for Ro Activity: " + receivedMessage);
              break;
          }
        }
      }
      else {
        logger.error("Received a REQUEST message when expecting an ANSWER.");
      }
    }
    else {
      logger.debug("No answer received. Returning null.");
    }

    return answer;
  }

}
