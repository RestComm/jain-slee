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

package org.mobicents.slee.resource.diameter.base;

import java.io.IOException;
import java.util.concurrent.Future;

import javax.naming.OperationNotSupportedException;

import net.java.slee.resource.diameter.base.DiameterActivity;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.CapabilitiesExchangeAnswer;
import net.java.slee.resource.diameter.base.events.DeviceWatchdogAnswer;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.DisconnectPeerAnswer;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;

import org.apache.log4j.Logger;
import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.Message;
import org.jdiameter.api.Request;
import org.jdiameter.api.Session;
import org.mobicents.slee.resource.diameter.base.events.AbortSessionAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.AccountingAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.CapabilitiesExchangeAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DeviceWatchdogAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.DisconnectPeerAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ErrorAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.SessionTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.base.handlers.DiameterRAInterface;

/**
 * 
 * Implementation of {@link DiameterActivity}.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DiameterActivityImpl implements DiameterActivity {

  private static final long serialVersionUID = -1065881101052214841L;

  protected static Logger logger = Logger.getLogger(DiameterActivityImpl.class);

  protected String sessionId = null;

  protected DiameterIdentity destinationHost = null;
  protected DiameterIdentity destinationRealm = null;

  protected boolean terminateAfterProcessing = false;

  //base session used by this activity
  protected transient Session session = null;
  //event listener, in case of DiameterActivityImpl it will be RA, in specific application session it will
  //be app session impl. Required to properly receive messages back ?
  protected transient EventListener<Request, Answer> eventListener = null;
  protected transient DiameterRAInterface baseListener = null;

  protected transient DiameterMessageFactory messageFactory = null;
  protected transient DiameterAvpFactory avpFactory = null;
  protected transient DiameterActivityHandle handle = null;

  public DiameterActivityImpl(DiameterMessageFactory messageFactory, DiameterAvpFactory avpFactory, Session session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm) {
    super();

    this.messageFactory = messageFactory;
    this.avpFactory = avpFactory;
    this.session = session;
    this.eventListener = raEventListener;

    this.destinationHost = destinationHost;
    this.destinationRealm = destinationRealm;

    if(session != null) {
      this.setCurrentWorkingSession(session);
      this.sessionId = session.getSessionId();
    }
  }

  // setters, only used by cluster part
  public void setCurrentWorkingSession(Session session) {
    this.sessionId = session.getSessionId();
    this.session = session;

    if (this.handle == null) {
      this.handle = new DiameterActivityHandle(this.sessionId);
    }
  }

  /**
   * @param eventListener
   *            the eventListener to set
   */
  public void setEventListener(EventListener<Request, Answer> eventListener) {
    this.eventListener = eventListener;
  }

  /**
   * @param messageFactory
   *            the messageFactory to set
   */
  public void setMessageFactory(DiameterMessageFactory messageFactory) {
    this.messageFactory = messageFactory;
  }

  /**
   * @param avpFactory
   *            the avpFactory to set
   */
  public void setAvpFactory(DiameterAvpFactory avpFactory) {
    this.avpFactory = avpFactory;
  }

  public void endActivity() {
	 //TODO: check if this should be synced to ensure calls from SBBs work properly.
	this.setTerminateAfterProcessing(false);
    this.baseListener.stopActivityRemoveTimer((DiameterActivityHandle) handle);
    if(session != null) {
      session.release();
    }

    try {
      //endpoint.endActivity(this.getActivityHandle());
      this.baseListener.endActivity(this.getActivityHandle());
    }
    catch (Exception e) {
      logger.error("Failed to end activity [" + this + "].", e);
    }
  }

  public DiameterAvpFactory getDiameterAvpFactory() {
    return this.avpFactory;
  }

  public DiameterMessageFactory getDiameterMessageFactory() {
    return this.messageFactory;
  }

  public String getSessionId() {
    return this.sessionId;
  }

  public void sendMessage(DiameterMessage message) throws IOException {
    try {
      if (message instanceof DiameterMessageImpl) {
        DiameterMessageImpl msg = (DiameterMessageImpl) message;
        this.session.send(msg.getGenericData(), this.eventListener);
      }
      else {
        throw new OperationNotSupportedException("Trying to send wrong type of message? [" + message.getClass() + "] \n" + message);
      }
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      throw new IOException("Failed to send message, due to: " + e);
    }
  }

  // ============= IMPL methods

  public DiameterActivityHandle getActivityHandle() {
    if (this.handle == null) {
      this.handle = new DiameterActivityHandle(this.sessionId);
    }
    return this.handle;
  }

  protected Message doSendMessage(DiameterMessage message) {
    Message receivedMessage = null;
    try {
      if (message instanceof DiameterMessageImpl) {
        Future<Message> future = this.session.send(((DiameterMessageImpl) message).getGenericData());
  
        receivedMessage = future.get(); 
      }
      else {
        throw new OperationNotSupportedException("Trying to send wrong type of message? [" + message.getClass() + "] \n" + message);
      }
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException e) {
      throw new AvpNotAllowedException("Message validation failed.", e, e.getAvpCode(), e.getVendorId());
    }
    catch (Exception e) {
      logger.error("Failure sending sync request.", e);
    }

    return receivedMessage;
  }
  
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
            case AbortSessionAnswer.commandCode:
              answer = new AbortSessionAnswerImpl(receivedMessage);
              break;
            case AccountingAnswer.commandCode:
              answer = new AccountingAnswerImpl(receivedMessage);
              break;
            case CapabilitiesExchangeAnswer.commandCode:
              answer = new CapabilitiesExchangeAnswerImpl(receivedMessage);
              break;
            case DeviceWatchdogAnswer.commandCode:
              answer = new DeviceWatchdogAnswerImpl(receivedMessage);
              break;
            case DisconnectPeerAnswer.commandCode:
              answer = new DisconnectPeerAnswerImpl(receivedMessage);
              break;
            case ReAuthAnswer.commandCode:
              answer = new ReAuthAnswerImpl(receivedMessage);
              break;
            case SessionTerminationAnswer.commandCode:
              answer = new SessionTerminationAnswerImpl(receivedMessage);
              break;
            default:
              logger.error("Received an unknown type of Message for Base Activity: " + receivedMessage);
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

  public boolean isValid() {
    return this.session.isValid();
  }

  public Object getSessionListener() {
    return baseListener;
  }

  public void setSessionListener(Object ra) {
    this.baseListener = (DiameterRAInterface) ra;
  }

  public void setTerminateAfterProcessing(boolean terminateAfterProcessing) {
    this.terminateAfterProcessing = terminateAfterProcessing;
  }

  public boolean isTerminateAfterProcessing() {
    return terminateAfterProcessing;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((destinationHost == null) ? 0 : destinationHost.hashCode());
    result = prime * result + ((destinationRealm == null) ? 0 : destinationRealm.hashCode());
    result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
    result = prime * result + (terminateAfterProcessing ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    DiameterActivityImpl other = (DiameterActivityImpl) obj;
    if (destinationHost == null) {
      if (other.destinationHost != null) {
        return false;
      }
    }
    else if (!destinationHost.equals(other.destinationHost)) {
      return false;
    }
    if (destinationRealm == null) {
      if (other.destinationRealm != null) {
        return false;
      }
    }
    else if (!destinationRealm.equals(other.destinationRealm)) {
      return false;
    }
    if (sessionId == null) {
      if (other.sessionId != null) {
        return false;
      }
    }
    else if (!sessionId.equals(other.sessionId)) {
      return false;
    }
    if (terminateAfterProcessing != other.terminateAfterProcessing) {
      return false;
    }

    return true;
  }

}
