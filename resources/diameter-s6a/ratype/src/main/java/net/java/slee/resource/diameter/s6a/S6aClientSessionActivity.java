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

package net.java.slee.resource.diameter.s6a;

import java.io.IOException;

import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest;
import net.java.slee.resource.diameter.s6a.events.CancelLocationAnswer;
import net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataAnswer;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer;
import net.java.slee.resource.diameter.s6a.events.NotifyRequest;
import net.java.slee.resource.diameter.s6a.events.PurgeUERequest;
import net.java.slee.resource.diameter.s6a.events.ResetAnswer;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationRequest;

/**
 * 
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface S6aClientSessionActivity extends S6aSessionActivity {

  public void sendUpdateLocationRequest(UpdateLocationRequest ulr) throws IOException;

  public void sendAuthenticationInformationRequest(AuthenticationInformationRequest air) throws IOException;

  public CancelLocationAnswer createCancelLocationAnswer();

  public void sendCancelLocationAnswer(CancelLocationAnswer cla) throws IOException;

  public InsertSubscriberDataAnswer createInsertSubscriberDataAnswer();

  public void sendInsertSubscriberDataAnswer(InsertSubscriberDataAnswer ida) throws IOException;

  public DeleteSubscriberDataAnswer createDeleteSubscriberDataAnswer();

  public void sendDeleteSubscriberDataAnswer(DeleteSubscriberDataAnswer dsa) throws IOException;

  public void sendPurgeUERequest(PurgeUERequest pur) throws IOException;

  public ResetAnswer createResetAnswer();

  public void sendResetAnswer(ResetAnswer rsa) throws IOException;

  public void sendNotifyRequest(NotifyRequest nor) throws IOException;

}
