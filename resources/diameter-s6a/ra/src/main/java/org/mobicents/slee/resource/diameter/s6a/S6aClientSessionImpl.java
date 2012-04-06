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

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.s6a.S6aAVPFactory;
import net.java.slee.resource.diameter.s6a.S6aClientSessionActivity;
import net.java.slee.resource.diameter.s6a.S6aMessageFactory;
import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest;
import net.java.slee.resource.diameter.s6a.events.CancelLocationAnswer;
import net.java.slee.resource.diameter.s6a.events.CancelLocationRequest;
import net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataAnswer;
import net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.NotifyRequest;
import net.java.slee.resource.diameter.s6a.events.PurgeUERequest;
import net.java.slee.resource.diameter.s6a.events.ResetAnswer;
import net.java.slee.resource.diameter.s6a.events.ResetRequest;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.s6a.ClientS6aSession;
import org.jdiameter.common.api.app.s6a.S6aSessionState;
import org.jdiameter.common.impl.app.s6a.JAuthenticationInformationRequestImpl;
import org.jdiameter.common.impl.app.s6a.JCancelLocationAnswerImpl;
import org.jdiameter.common.impl.app.s6a.JDeleteSubscriberDataAnswerImpl;
import org.jdiameter.common.impl.app.s6a.JInsertSubscriberDataAnswerImpl;
import org.jdiameter.common.impl.app.s6a.JNotifyRequestImpl;
import org.jdiameter.common.impl.app.s6a.JPurgeUERequestImpl;
import org.jdiameter.common.impl.app.s6a.JResetAnswerImpl;
import org.jdiameter.common.impl.app.s6a.JUpdateLocationRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class S6aClientSessionImpl extends S6aSessionImpl implements S6aClientSessionActivity {

  private static final long serialVersionUID = 7518916596996009148L;
  protected transient ClientS6aSession appSession;

  public S6aClientSessionImpl(S6aMessageFactory s6aMessageFactory, S6aAVPFactory s6aAvpFactory, ClientS6aSession session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, Stack stack) {
    super(s6aMessageFactory, s6aAvpFactory, session.getSessions().get(0), raEventListener, destinationHost, destinationRealm);
    // FIXME: remove stack?

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
  }

  public void setSession(ClientS6aSession session) {
    this.appSession = session;
    this.appSession.addStateChangeNotification(this);
  }

  public void stateChanged(Enum oldState, Enum newState) {
    if (!terminated) {
      if (newState == S6aSessionState.TERMINATED || newState == S6aSessionState.TIMEDOUT) {
        terminated = true;
        endActivity();
      }
    }
  }

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Object, java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(AppSession appSession, Enum oldState, Enum newState) {
    this.stateChanged(oldState, newState);

  }

  @Override
  public void endActivity() {
    if (this.appSession != null) {
      this.appSession.release();
    }

    try {
      // endpoint.endActivity(this.getActivityHandle());
      super.baseListener.endActivity(this.getActivityHandle());
    }
    catch (Exception e) {
      logger.error("Failed to end activity [" + this + "].", e);
    }
  }

  public void sendUpdateLocationRequest(UpdateLocationRequest ulr) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) ulr;
    JUpdateLocationRequestImpl answer = new JUpdateLocationRequestImpl((Request) msg.getGenericData());
    try {
      appSession.sendUpdateLocationRequest(answer);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  public void sendAuthenticationInformationRequest(AuthenticationInformationRequest air) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) air;
    JAuthenticationInformationRequestImpl answer = new JAuthenticationInformationRequestImpl((Request) msg.getGenericData());
    try {
      appSession.sendAuthenticationInformationRequest(answer);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  public CancelLocationAnswer createCancelLocationAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof CancelLocationRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      CancelLocationAnswer cla = (CancelLocationAnswer) this.s6aMessageFactory.createS6aMessage(lastRequest.getHeader(), new DiameterAvp[]{}, CancelLocationAnswer.COMMAND_CODE, s6aMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(cla);

      return cla;
    }
    catch (InternalException e) {
      logger.error("Failed to create Cancel-Location-Answer.", e);
    }

    return null;
  }

  public void sendCancelLocationAnswer(CancelLocationAnswer cla) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) cla;
    JCancelLocationAnswerImpl request = new JCancelLocationAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendCancelLocationAnswer(request);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  public InsertSubscriberDataAnswer createInsertSubscriberDataAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof InsertSubscriberDataRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      InsertSubscriberDataAnswer ida = (InsertSubscriberDataAnswer) this.s6aMessageFactory.createS6aMessage(lastRequest.getHeader(), new DiameterAvp[]{}, InsertSubscriberDataAnswer.COMMAND_CODE, s6aMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(ida);

      return ida;
    }
    catch (InternalException e) {
      logger.error("Failed to create Insert-Subscriber-Data-Answer.", e);
    }

    return null;
  }

  public void sendInsertSubscriberDataAnswer(InsertSubscriberDataAnswer ida) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) ida;
    JInsertSubscriberDataAnswerImpl request = new JInsertSubscriberDataAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendInsertSubscriberDataAnswer(request);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  public DeleteSubscriberDataAnswer createDeleteSubscriberDataAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof DeleteSubscriberDataRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      DeleteSubscriberDataAnswer dsa = (DeleteSubscriberDataAnswer) this.s6aMessageFactory.createS6aMessage(lastRequest.getHeader(), new DiameterAvp[]{}, DeleteSubscriberDataAnswer.COMMAND_CODE, s6aMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(dsa);

      return dsa;
    }
    catch (InternalException e) {
      logger.error("Failed to create Delete-Subscriber-Data-Answer.", e);
    }

    return null;
  }

  public void sendDeleteSubscriberDataAnswer(DeleteSubscriberDataAnswer dsa) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) dsa;
    JDeleteSubscriberDataAnswerImpl request = new JDeleteSubscriberDataAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendDeleteSubscriberDataAnswer(request);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  public void sendPurgeUERequest(PurgeUERequest pur) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) pur;
    JPurgeUERequestImpl answer = new JPurgeUERequestImpl((Request) msg.getGenericData());
    try {
      appSession.sendPurgeUERequest(answer);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  public ResetAnswer createResetAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof ResetRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      ResetAnswer rsa = (ResetAnswer) this.s6aMessageFactory.createS6aMessage(lastRequest.getHeader(), new DiameterAvp[]{}, ResetAnswer.COMMAND_CODE, s6aMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(rsa);

      return rsa;
    }
    catch (InternalException e) {
      logger.error("Failed to create Reset-Answer.", e);
    }

    return null;
  }

  public void sendResetAnswer(ResetAnswer rsa) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) rsa;
    JResetAnswerImpl request = new JResetAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendResetAnswer(request);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  public void sendNotifyRequest(NotifyRequest nor) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) nor;
    JNotifyRequestImpl request = new JNotifyRequestImpl((Request) msg.getGenericData());
    try {
      appSession.sendNotifyRequest(request);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

}
