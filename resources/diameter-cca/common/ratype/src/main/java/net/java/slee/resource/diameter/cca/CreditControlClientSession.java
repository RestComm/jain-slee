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

package net.java.slee.resource.diameter.cca;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;

/**
 * 
 * A CreditControlClientSession represents a charging control session for Credit Control clients.
 *
 * <br>Super project:  mobicents
 * <br>10:58:08 AM Dec 30, 2008 
 * <br>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public interface CreditControlClientSession extends CreditControlSession {

  /**
   * Create a Credit-Control-Request message pre-populated with the AVPs
   * appropriate for this session.
   * 
   * @return a new CreditControlRequest
   */
  CreditControlRequest createCreditControlRequest();

  /**
   * Send an event Credit-Control-Request. An event containing the answer will
   * be fired on this activity.
   * 
   * @param ccr the CreditControlRequest to send
   * @throws IOException
   */
  void sendCreditControlRequest(CreditControlRequest ccr) throws IOException;

  /**
   * Send an initial Credit-Control-Request. An event containing the answer
   * will be fired on this activity.
   * 
   * @param ccr the CreditControlRequest to send
   * @throws IOException
   */
  void sendInitialCreditControlRequest(CreditControlRequest ccr) throws IOException;

  /**
   * Send a Re-Auth-Answer message.
   * 
   * @param rar the CreditControlRequest to send
   * @throws IOException
   */
  void sendReAuthAnswer(ReAuthAnswer rar) throws IOException;

  /**
   * Send an update (intermediate) Credit-Control-Request. An event containing
   * the answer will be fired on this activity.
   * 
   * @param ccr the CreditControlRequest to send
   * @throws IOException
   */
  void sendUpdateCreditControlRequest(CreditControlRequest ccr) throws IOException;

  /**
   * Send a termination Credit-Control-Request. An event containing the answer
   * will be fired on this activity. The activity will end when the event is
   * fired.
   * 
   * @param ccr the CreditControlRequest to send
   * @throws IOException 
   */
  void sendTerminationCreditControlRequest(CreditControlRequest ccr) throws IOException;

}
