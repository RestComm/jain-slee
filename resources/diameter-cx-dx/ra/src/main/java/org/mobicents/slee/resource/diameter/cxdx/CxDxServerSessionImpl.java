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

package org.mobicents.slee.resource.diameter.cxdx;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cxdx.CxDxAVPFactory;
import net.java.slee.resource.diameter.cxdx.CxDxMessageFactory;
import net.java.slee.resource.diameter.cxdx.CxDxServerSessionActivity;
import net.java.slee.resource.diameter.cxdx.events.LocationInfoAnswer;
import net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest;
import net.java.slee.resource.diameter.cxdx.events.MultimediaAuthenticationAnswer;
import net.java.slee.resource.diameter.cxdx.events.MultimediaAuthenticationRequest;
import net.java.slee.resource.diameter.cxdx.events.PushProfileRequest;
import net.java.slee.resource.diameter.cxdx.events.RegistrationTerminationRequest;
import net.java.slee.resource.diameter.cxdx.events.ServerAssignmentAnswer;
import net.java.slee.resource.diameter.cxdx.events.ServerAssignmentRequest;
import net.java.slee.resource.diameter.cxdx.events.UserAuthorizationAnswer;
import net.java.slee.resource.diameter.cxdx.events.UserAuthorizationRequest;

import org.jdiameter.api.Answer;
import org.jdiameter.api.EventListener;
import org.jdiameter.api.InternalException;
import org.jdiameter.api.Request;
import org.jdiameter.api.Stack;
import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.cxdx.ServerCxDxSession;
import org.jdiameter.common.api.app.cxdx.CxDxSessionState;
import org.jdiameter.common.impl.app.cxdx.JLocationInfoAnswerImpl;
import org.jdiameter.common.impl.app.cxdx.JMultimediaAuthAnswerImpl;
import org.jdiameter.common.impl.app.cxdx.JPushProfileRequestImpl;
import org.jdiameter.common.impl.app.cxdx.JRegistrationTerminationRequestImpl;
import org.jdiameter.common.impl.app.cxdx.JServerAssignmentAnswerImpl;
import org.jdiameter.common.impl.app.cxdx.JUserAuthorizationAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * CxDxServerSessionImpl.java
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CxDxServerSessionImpl extends CxDxSessionImpl implements CxDxServerSessionActivity {

  private static final long serialVersionUID = 7518916596996009148L;

  protected transient ServerCxDxSession appSession;

  /**
   * 
   * @param cxdxMessageFactory
   * @param cxdxAvpFactory
   * @param session
   * @param raEventListener
   * @param destinationHost
   * @param destinationRealm
   * @param sleeEndpoint
   * @param stack
   */
  public CxDxServerSessionImpl(CxDxMessageFactory cxdxMessageFactory, CxDxAVPFactory cxdxAvpFactory, ServerCxDxSession session, EventListener<Request, Answer> raEventListener, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, Stack stack) {
    super(cxdxMessageFactory, cxdxAvpFactory, session.getSessions().get(0), raEventListener, destinationHost, destinationRealm);

    setSession(session);
    super.setCurrentWorkingSession(session.getSessions().get(0));
  }

  public void setSession(ServerCxDxSession session) {
    this.appSession = session;
    this.appSession.addStateChangeNotification(this);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#createPushProfileRequest()
   */
  public PushProfileRequest createPushProfileRequest() {
    // Create the request
    PushProfileRequest ppr = cxdxMessageFactory.createPushProfileRequest(super.getSessionId());

    // Fill session related AVPs, if present
    fillSessionAVPs(ppr);

    return ppr;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#createRegistrationTerminationRequest()
   */
  public RegistrationTerminationRequest createRegistrationTerminationRequest() {
    // Create the request
    RegistrationTerminationRequest rtr = cxdxMessageFactory.createRegistrationTerminationRequest(super.getSessionId());

    // Fill session related AVPs, if present
    fillSessionAVPs(rtr);

    return rtr;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#createLocationInfoAnswer()
   */
  public LocationInfoAnswer createLocationInfoAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof LocationInfoRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      LocationInfoAnswer lia = (LocationInfoAnswer) this.cxdxMessageFactory.createCxDxMessage(lastRequest.getHeader(), new DiameterAvp[]{}, LocationInfoAnswer.COMMAND_CODE, cxdxMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(lia);

      return lia;
    }
    catch (InternalException e) {
      logger.error("Failed to create Location-Info-Answer.", e);
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#createMultimediaAuthenticationAnswer()
   */
  public MultimediaAuthenticationAnswer createMultimediaAuthenticationAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof MultimediaAuthenticationRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      MultimediaAuthenticationAnswer maa = (MultimediaAuthenticationAnswer) this.cxdxMessageFactory.createCxDxMessage(lastRequest.getHeader(), new DiameterAvp[]{}, MultimediaAuthenticationAnswer.COMMAND_CODE, cxdxMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(maa);

      return maa;
    }
    catch (InternalException e) {
      logger.error("Failed to create Multimedia-Authentication-Answer.", e);
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#createServerAssignmentAnswer()
   */
  public ServerAssignmentAnswer createServerAssignmentAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof ServerAssignmentRequest)) {
      logger.warn("Invalid type of answer for this activity.");
      return null;
    }

    try {
      // Create the answer
      ServerAssignmentAnswer saa = (ServerAssignmentAnswer) this.cxdxMessageFactory.createCxDxMessage(lastRequest.getHeader(), new DiameterAvp[]{}, ServerAssignmentAnswer.COMMAND_CODE, cxdxMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(saa);

      return saa;
    }
    catch (InternalException e) {
      logger.error("Failed to create Server-Assignment-Answer.", e);
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#createUserAuthorizationAnswer()
   */
  public UserAuthorizationAnswer createUserAuthorizationAnswer() {
    // Make sure we have the correct type of Request
    if (!(lastRequest instanceof UserAuthorizationRequest)) {
      logger.warn("Invalid type of answer for this activity. Type: "+lastRequest);
      return null;
    }

    try {
      // Create the answer
      UserAuthorizationAnswer uaa = (UserAuthorizationAnswer) this.cxdxMessageFactory.createCxDxMessage(lastRequest.getHeader(), new DiameterAvp[]{}, UserAuthorizationAnswer.COMMAND_CODE, cxdxMessageFactory.getApplicationId());

      // Fill session related AVPs, if present
      fillSessionAVPs(uaa);

      return uaa;
    }
    catch (InternalException e) {
      logger.error("Failed to create User-Authorization-Answer.", e);
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#sendLocationInfoAnswer(net.java.slee.resource.diameter.cxdx.events.LocationInfoAnswer)
   */
  public void sendLocationInfoAnswer(LocationInfoAnswer locationInfoAnswer) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) locationInfoAnswer;
    JLocationInfoAnswerImpl answer = new JLocationInfoAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendLocationInformationAnswer(answer);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#sendMultimediaAuthenticationAnswer(net.java.slee.resource.diameter.cxdx.events.MultimediaAuthenticationAnswer)
   */
  public void sendMultimediaAuthenticationAnswer(MultimediaAuthenticationAnswer multimediaAuthenticationAnswer) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) multimediaAuthenticationAnswer;
    JMultimediaAuthAnswerImpl answer = new JMultimediaAuthAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendMultimediaAuthAnswer(answer);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#sendPushProfileRequest(net.java.slee.resource.diameter.cxdx.events.PushProfileRequest)
   */
  public void sendPushProfileRequest(PushProfileRequest pushProfileRequest) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) pushProfileRequest;
    JPushProfileRequestImpl request = new JPushProfileRequestImpl(msg.getGenericData());
    try {
      appSession.sendPushProfileRequest(request);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#sendRegistrationTerminationRequest(net.java.slee.resource.diameter.cxdx.events.RegistrationTerminationRequest)
   */
  public void sendRegistrationTerminationRequest(RegistrationTerminationRequest registrationTerminationRequest) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) registrationTerminationRequest;
    JRegistrationTerminationRequestImpl request = new JRegistrationTerminationRequestImpl(msg.getGenericData());
    try {
      appSession.sendRegistrationTerminationRequest(request);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#sendServerAssignmentAnswer(net.java.slee.resource.diameter.cxdx.events.ServerAssignmentAnswer)
   */
  public void sendServerAssignmentAnswer(ServerAssignmentAnswer serverAssignmentAnswer) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) serverAssignmentAnswer;
    JServerAssignmentAnswerImpl answer = new JServerAssignmentAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendServerAssignmentAnswer(answer);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.CxDxServerSession#sendUserAuthorizationAnswer(net.java.slee.resource.diameter.cxdx.events.UserAuthorizationAnswer)
   */
  public void sendUserAuthorizationAnswer(UserAuthorizationAnswer userAuthorizationAnswer) throws IOException {
    DiameterMessageImpl msg = (DiameterMessageImpl) userAuthorizationAnswer;
    JUserAuthorizationAnswerImpl answer = new JUserAuthorizationAnswerImpl((Answer) msg.getGenericData());
    try {
      appSession.sendUserAuthorizationAnswer(answer);
    }
    catch (org.jdiameter.api.validation.AvpNotAllowedException anae) {
      throw new AvpNotAllowedException(anae.getMessage(), anae.getAvpCode(), anae.getVendorId());
    }
    catch (Exception e) {
      if(logger.isDebugEnabled()) {
        logger.debug("Failed to send message.", e);
      }
      throw new IOException(e.getMessage());
    }
  }

  public void stateChanged(Enum oldState, Enum newState) {
    if (!terminated) {
      if (newState == CxDxSessionState.TERMINATED || newState == CxDxSessionState.TIMEDOUT) {
        terminated = true;
        this.setTerminateAfterProcessing(true);
        super.baseListener.startActivityRemoveTimer(getActivityHandle());
      }
    }
  }

  /*
   * (non-Javadoc)
   * @see org.jdiameter.api.app.StateChangeListener#stateChanged(java.lang.Object, java.lang.Enum, java.lang.Enum)
   */
  public void stateChanged(AppSession arg0, Enum oldState, Enum newState) {
    this.stateChanged(oldState, newState);

  }

  public void endActivity() {
    if (this.appSession != null) {
      this.appSession.release();
    }

    super.endActivity();
  }

}
