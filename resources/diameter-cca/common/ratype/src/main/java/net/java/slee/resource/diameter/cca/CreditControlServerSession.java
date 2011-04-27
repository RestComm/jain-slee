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

import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.cca.events.CreditControlAnswer;

/**
 * 
 * A CreditControlServerSession represents a charging control session for Credit Control servers.
 *
 * <br>Super project:  mobicents
 * <br>10:59:47 AM Dec 30, 2008 
 * <br>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 */
public interface CreditControlServerSession extends CreditControlSession {

  /**
   * Create a Credit-Control-Answer message pre-populated with the AVPs
   * appropriate for this session.
   * 
   * @return a new CreditControlAnswer
   */
  CreditControlAnswer createCreditControlAnswer();

  /**
   * Send a Credit-Control-Answer message to the CC client.
   * 
   * @param cca the CreditControlAnswer to send
   * @throws IOException
   */
  void sendCreditControlAnswer(CreditControlAnswer cca) throws IOException;

  /**
   * Send a Re-Auth-Request message to the CC client.
   * 
   * @param rar the ReAuthRequest to send
   * @throws IOException 
   * @throws IOException
   */
  void sendReAuthRequest(ReAuthRequest rar) throws IOException;

}
