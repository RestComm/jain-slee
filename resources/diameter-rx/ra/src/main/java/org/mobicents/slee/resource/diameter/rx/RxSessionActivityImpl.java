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

package org.mobicents.slee.resource.diameter.rx;

import java.util.concurrent.Future;

import javax.naming.OperationNotSupportedException;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.rx.RxAvpFactory;
import net.java.slee.resource.diameter.rx.RxMessageFactory;
import net.java.slee.resource.diameter.rx.RxSessionActivity;
import net.java.slee.resource.diameter.rx.events.AARequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.validation.AvpNotAllowedException;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.rx.events.*;

/**
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public abstract class RxSessionActivityImpl extends DiameterActivityImpl implements RxSessionActivity {

  private static final long serialVersionUID = 5037967180962414549L;

  protected transient RxMessageFactory rxMessageFactory;

  /**
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param raEventListener
   * @param destinationHost
   * @param destinationRealm
   */
  public RxSessionActivityImpl(RxMessageFactory rxMessageFactory, RxAvpFactory rxAvpFactory, Session session,
      EventListener<Request, Answer> raEventListener,
      DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(rxMessageFactory.getBaseMessageFactory(), rxAvpFactory.getBaseFactory(), session, raEventListener, destinationHost, destinationRealm);
    this.rxMessageFactory = rxMessageFactory;
  }

  @Override
  public RxMessageFactory getRxMessageFactory() {
    return this.rxMessageFactory;
  }

  public void setRxMessageFactory(RxMessageFactory rxMessageFactory) {
    this.rxMessageFactory = rxMessageFactory;
  }

  public void setDestinationHost(DiameterIdentity destinationHost) {
    super.destinationHost = destinationHost;
  }

  public void setDestinationRealm(DiameterIdentity destinationRealm) {
    super.destinationRealm = destinationRealm;
  }
  //some override from base

  protected Message doSendMessage(DiameterMessage message) {
    Message receivedMessage = null;
    try {
      if(message instanceof DiameterMessageImpl) {
        Future<Message> future = session.send(((DiameterMessageImpl)message).getGenericData());
        receivedMessage = future.get();
      }
      else {
        throw new OperationNotSupportedException((new StringBuilder()).append("Trying to send wrong type of message? [").append(message.getClass()).append("] \n").append(message).toString());
      }
    }
    catch(AvpNotAllowedException e) {
      throw new net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch(Exception e) {
      logger.error("Failure sending sync request.", e);
    }

    return receivedMessage;
  }

  public DiameterMessage sendSyncMessage(DiameterMessage message) {
    DiameterMessage answer = null;
    Message receivedMessage = doSendMessage(message);
    if(receivedMessage != null) {
      if(!receivedMessage.isRequest()) {
        if(receivedMessage.isError()) {
          answer = new ErrorAnswerImpl(receivedMessage);
        }
        else {
          switch(receivedMessage.getCommandCode())
          {
            case 274: 
              answer = new AbortSessionAnswerImpl(receivedMessage);
              break;

            case 258: 
              answer = new ReAuthAnswerImpl(receivedMessage);
              break;

            case 275: 
              answer = new SessionTerminationAnswerImpl(receivedMessage);
              break;
            case AARequest.commandCode:
              answer = new AAAnswerImpl(receivedMessage);
            default:
              logger.error((new StringBuilder()).append("Received an unknown type of Message for Base Activity: ").append(receivedMessage).toString());
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
