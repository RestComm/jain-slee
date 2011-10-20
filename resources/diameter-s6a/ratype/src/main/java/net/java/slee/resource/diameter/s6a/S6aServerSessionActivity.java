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

import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer;
import net.java.slee.resource.diameter.s6a.events.CancelLocationRequest;
import net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.NotifyAnswer;
import net.java.slee.resource.diameter.s6a.events.ResetRequest;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationAnswer;
import net.java.slee.resource.diameter.s6a.events.PurgeUEAnswer;

/**
 * 
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface S6aServerSessionActivity extends S6aSessionActivity {

  public UpdateLocationAnswer createUpdateLocationAnswer();

  public void sendUpdateLocationAnswer(UpdateLocationAnswer ula) throws IOException;

  public AuthenticationInformationAnswer createAuthenticationInformationAnswer();

  public void sendAuthenticationInformationAnswer(AuthenticationInformationAnswer aia) throws IOException;

  public void sendCancelLocationRequest(CancelLocationRequest clr) throws IOException;

  public void sendInsertSubscriberDataRequest(InsertSubscriberDataRequest idr) throws IOException;

  public void sendDeleteSubscriberDataRequest(DeleteSubscriberDataRequest dsr) throws IOException;

  public PurgeUEAnswer createPurgeUEAnswer();

  public void sendPurgeUEAnswer(PurgeUEAnswer pua) throws IOException;

  public void sendResetRequest(ResetRequest rsr) throws IOException;

  public NotifyAnswer createNotifyAnswer();

  public void sendNotifyAnswer(NotifyAnswer noa) throws IOException;

}
