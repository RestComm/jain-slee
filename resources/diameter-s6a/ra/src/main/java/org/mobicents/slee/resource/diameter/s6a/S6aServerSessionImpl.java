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
import net.java.slee.resource.diameter.s6a.S6aMessageFactory;
import net.java.slee.resource.diameter.s6a.S6aServerSessionActivity;
import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer;
import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest;
import net.java.slee.resource.diameter.s6a.events.CancelLocationRequest;
import net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.NotifyAnswer;
import net.java.slee.resource.diameter.s6a.events.NotifyRequest;
import net.java.slee.resource.diameter.s6a.events.PurgeUEAnswer;
import net.java.slee.resource.diameter.s6a.events.PurgeUERequest;
import net.java.slee.resource.diameter.s6a.events.ResetRequest;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationAnswer;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.s6a.ServerS6aSession;
import org.jdiameter.common.api.app.s6a.S6aSessionState;
import org.jdiameter.common.impl.app.s6a.JAuthenticationInformationAnswerImpl;
import org.jdiameter.common.impl.app.s6a.JCancelLocationRequestImpl;
import org.jdiameter.common.impl.app.s6a.JDeleteSubscriberDataRequestImpl;
import org.jdiameter.common.impl.app.s6a.JInsertSubscriberDataRequestImpl;
import org.jdiameter.common.impl.app.s6a.JNotifyAnswerImpl;
import org.jdiameter.common.impl.app.s6a.JPurgeUEAnswerImpl;
import org.jdiameter.common.impl.app.s6a.JResetRequestImpl;
import org.jdiameter.common.impl.app.s6a.JUpdateLocationAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class S6aServerSessionImpl extends S6aSessionImpl implements S6aServerSessionActivity {

  private static final long serialVersionUID = 7518916596996009148L;
  protected transient ServerS6aSession appSession;

  public S6aServerSessionImpl(S6aMessageFactory s6aMessageFactory, S6aAVPFactory s6aAvpFactory, ServerS6aSession session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, Stack stack) {
    super(s6aMessageFactory, s6aAvpFactory, session.getSessions().get(0), raEventListener, destinationHost, destinationRealm);
    // FIXME: remove stack?

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
  }

  public void setSession(ServerS6aSession session) {
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

  public AuthenticationInformationAnswer createAuthenticationInformationAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof AuthenticationInformationRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      AuthenticationInformationAnswer aia = (AuthenticationInformationAnswer) this.s6aMessageFactory.createS6aMessage(lastRequest.getHeader(), new DiameterAvp[]{}, AuthenticationInformationAnswer.COMMAND_CODE, s6aMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(aia);

      return aia;
    }
    catch (InternalException e) {
      logger.error("Failed to create Authentication-Information-Answer.", e);
    }

    return null;
  }

  public void sendAuthenticationInformationAnswer(AuthenticationInformationAnswer aia) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) aia;
    JAuthenticationInformationAnswerImpl answer = new JAuthenticationInformationAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendAuthenticationInformationAnswer(answer);
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

  public PurgeUEAnswer createPurgeUEAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof PurgeUERequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      PurgeUEAnswer aia = (PurgeUEAnswer) this.s6aMessageFactory.createS6aMessage(lastRequest.getHeader(), new DiameterAvp[]{}, PurgeUEAnswer.COMMAND_CODE, s6aMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(aia);

      return aia;
    }
    catch (InternalException e) {
      logger.error("Failed to create Purge-UE-Answer.", e);
    }

    return null;
  }

  public void sendPurgeUEAnswer(PurgeUEAnswer pua) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) pua;
    JPurgeUEAnswerImpl answer = new JPurgeUEAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendPurgeUEAnswer(answer);
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

  public UpdateLocationAnswer createUpdateLocationAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof UpdateLocationRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      UpdateLocationAnswer aia = (UpdateLocationAnswer) this.s6aMessageFactory.createS6aMessage(lastRequest.getHeader(), new DiameterAvp[]{}, UpdateLocationAnswer.COMMAND_CODE, s6aMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(aia);

      return aia;
    }
    catch (InternalException e) {
      logger.error("Failed to create Update-Location-Answer.", e);
    }

    return null;
  }

  public void sendUpdateLocationAnswer(UpdateLocationAnswer ula) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) ula;
    JUpdateLocationAnswerImpl answer = new JUpdateLocationAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendUpdateLocationAnswer(answer);
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

  public void sendCancelLocationRequest(CancelLocationRequest clr) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) clr;
    JCancelLocationRequestImpl request = new JCancelLocationRequestImpl((Answer) msg.getGenericData());
    try {
      appSession.sendCancelLocationRequest(request);
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

  public void sendInsertSubscriberDataRequest(InsertSubscriberDataRequest idr) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) idr;
    JInsertSubscriberDataRequestImpl request = new JInsertSubscriberDataRequestImpl((Answer) msg.getGenericData());
    try {
      appSession.sendInsertSubscriberDataRequest(request);
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

  public void sendDeleteSubscriberDataRequest(DeleteSubscriberDataRequest dsr) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) dsr;
    JDeleteSubscriberDataRequestImpl request = new JDeleteSubscriberDataRequestImpl((Answer) msg.getGenericData());
    try {
      appSession.sendDeleteSubscriberDataRequest(request);
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

  public void sendResetRequest(ResetRequest rsr) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) rsr;
    JResetRequestImpl request = new JResetRequestImpl((Answer) msg.getGenericData());
    try {
      appSession.sendResetRequest(request);
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

  public NotifyAnswer createNotifyAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof NotifyRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      NotifyAnswer aia = (NotifyAnswer) this.s6aMessageFactory.createS6aMessage(lastRequest.getHeader(), new DiameterAvp[]{}, NotifyAnswer.COMMAND_CODE, s6aMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(aia);

      return aia;
    }
    catch (InternalException e) {
      logger.error("Failed to create Notify-Answer.", e);
    }

    return null;
  }

  public void sendNotifyAnswer(NotifyAnswer ula) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) ula;
    JNotifyAnswerImpl answer = new JNotifyAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendNotifyAnswer(answer);
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
