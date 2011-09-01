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

package net.java.slee.resource.diameter.rx;

import java.io.IOException;

import net.java.slee.resource.diameter.rx.events.AARequest;
import net.java.slee.resource.diameter.rx.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.rx.events.ReAuthAnswer;
import net.java.slee.resource.diameter.rx.events.SessionTerminationRequest;

/**
 * A RxClientSessionActivity represents an auth session for Rx clients.
 *
 * All requests for the session must be sent via the same RxClientSessionActivity.
 *
 * All responses related to the session will be received as events fired on the
 * same RxClientSessionActivity.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface RxClientSessionActivity extends RxSessionActivity {

  void sendRxAARequest(AARequest aar) throws IOException;

  void sendSessionTermRequest(SessionTerminationRequest str) throws IOException;

  public abstract void sendReAuthAnswer(ReAuthAnswer raa) throws IOException;

  public abstract void sendAbortSessionAnswer(AbortSessionAnswer asr) throws IOException;

  public AARequest createRxAARequest();

  public SessionTerminationRequest createSessionTermRequest();

}
