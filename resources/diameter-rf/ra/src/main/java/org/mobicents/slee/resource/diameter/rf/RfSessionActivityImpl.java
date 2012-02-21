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

package org.mobicents.slee.resource.diameter.rf;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rf.RfAvpFactory;
import net.java.slee.resource.diameter.rf.RfMessageFactory;
import net.java.slee.resource.diameter.rf.RfSessionActivity;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.rf.events.RfAccountingRequestImpl;

/**
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class RfSessionActivityImpl extends DiameterActivityImpl implements RfSessionActivity, StateChangeListener<AppSession>{

  private static final long serialVersionUID = 4602851199202393775L;

  protected RfMessageFactory rfMessageFactory;

  /**
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param raEventListener
   * @param destinationHost
   * @param destinationRealm
   */
  public RfSessionActivityImpl(RfMessageFactory rfMessageFactory, RfAvpFactory rfAvpFactory, Session session, EventListener<Request, Answer> raEventListener,
      DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(rfMessageFactory.getBaseMessageFactory(), rfAvpFactory.getBaseFactory(), session, raEventListener, destinationHost, destinationRealm);
    this.rfMessageFactory = rfMessageFactory;
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
            case RfAccountingRequest.commandCode:
              answer = new RfAccountingRequestImpl(receivedMessage);
              break;
            default:
              logger.error("Received an unknown type of Message for Rf Activity: " + receivedMessage);
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
