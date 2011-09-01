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

import net.java.slee.resource.diameter.rx.events.AAAnswer;
import net.java.slee.resource.diameter.rx.events.AbortSessionRequest;
import net.java.slee.resource.diameter.rx.events.ReAuthRequest;
import net.java.slee.resource.diameter.rx.events.SessionTerminationAnswer;

/**
 * An RxServerSessionActivity represents a an auth session for Rx servers.
 *
 * A single RxServerSessionActivity will be created for the Diameter session.
 * All requests received for the session will be fired as events on the same
 * RxServerSessionActivity.
 *s
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface RxServerSessionActivity extends RxSessionActivity {

  AAAnswer createAAAnswer();

  SessionTerminationAnswer createSessionTermAnswer();

  void sendAAAnswer(AAAnswer aaa) throws IOException;

  void sendSessionTermAnswer(SessionTerminationAnswer sta) throws IOException;

  void sendReAuthRequest(ReAuthRequest rar) throws IOException;

  void sendAbortSessionRequest(AbortSessionRequest asr) throws IOException;

}
