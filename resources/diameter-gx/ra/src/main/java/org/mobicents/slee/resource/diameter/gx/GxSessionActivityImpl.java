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

package org.mobicents.slee.resource.diameter.gx;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.gx.GxAvpFactory;
import net.java.slee.resource.diameter.gx.GxMessageFactory;
import net.java.slee.resource.diameter.gx.GxSessionActivity;
import net.java.slee.resource.diameter.gx.events.GxCreditControlRequest;
import net.java.slee.resource.diameter.gx.events.GxReAuthRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.mobicents.slee.resource.diameter.base.DiameterActivityImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.gx.events.GxCreditControlRequestImpl;
import org.mobicents.slee.resource.diameter.gx.events.GxReAuthRequestImpl;

/**
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public abstract class GxSessionActivityImpl extends DiameterActivityImpl implements GxSessionActivity {

    private static final long serialVersionUID = 5037967180962414549L;

    protected transient GxMessageFactory gxMessageFactory;
    protected transient GxAvpFactory gxAvpFactory;

    /**
     * @param messageFactory
     * @param avpFactory
     * @param session
     * @param raEventListener
     * @param destinationHost
     * @param destinationRealm
     */
    public GxSessionActivityImpl(GxMessageFactory gxMessageFactory, GxAvpFactory gxAvpFactory, Session session,
                                 EventListener<Request, Answer> raEventListener,
                                 DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
        super(gxMessageFactory.getBaseMessageFactory(), gxAvpFactory.getBaseFactory(), session, raEventListener, destinationHost, destinationRealm);
        this.gxMessageFactory = gxMessageFactory;
        this.gxAvpFactory = gxAvpFactory;
    }

    @Override
    public GxMessageFactory getGxMessageFactory() {
        return this.gxMessageFactory;
    }

    public void setGxMessageFactory(GxMessageFactory gxMessageFactory) {
        this.gxMessageFactory = gxMessageFactory;
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
              case GxCreditControlRequest.commandCode:
                answer = new GxCreditControlRequestImpl(receivedMessage);
                break;
              case GxReAuthRequest.commandCode:
                answer = new GxReAuthRequestImpl(receivedMessage);
                break;
              default:
                logger.error("Received an unknown type of Message for Gx Activity: " + receivedMessage);
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
