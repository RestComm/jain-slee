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

package org.mobicents.slee.resource.diameter.cca;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.cca.CreditControlSession;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.app.StateChangeListener;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.cca.events.CreditControlAnswerImpl;

/**
 * Implementation of {@link CreditControlSession}
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public abstract class CreditControlSessionImpl extends DiameterActivityImpl implements CreditControlSession, StateChangeListener<AppSession>{

  private static final long serialVersionUID = 7914534365520919705L;

  protected transient CreditControlMessageFactory ccaMessageFactory = null;
  protected transient CreditControlAVPFactory ccaAvpFactory = null;

  public CreditControlSessionImpl(CreditControlMessageFactory messageFactory, CreditControlAVPFactory avpFactory, Session session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super(null, null, session, raEventListener, destinationHost, destinationRealm);

    this.ccaMessageFactory = messageFactory;
    this.ccaAvpFactory = avpFactory;
  }

  public CreditControlAVPFactory getCCAAvpFactory() {
    return this.ccaAvpFactory;
  }

  public CreditControlMessageFactory getCCAMessageFactory() {
    return this.ccaMessageFactory;
  }

  public void setCCAMessageFactory(CreditControlMessageFactory ccaMessageFactory) {
    this.ccaMessageFactory = ccaMessageFactory;
  }

  public void setCCAAvpFactory(CreditControlAVPFactory ccaAvpFactory) {
    this.ccaAvpFactory = ccaAvpFactory;
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
            case CreditControlRequest.commandCode:
              answer = new CreditControlAnswerImpl(receivedMessage);
              break;
            default:
              logger.error("Received an unknown type of Message for Credit-Control Activity: " + receivedMessage);
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
