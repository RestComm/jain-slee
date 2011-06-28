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

package net.java.slee.resource.diameter.gx;

import java.io.IOException;

import net.java.slee.resource.diameter.gx.events.GxReAuthRequest;
import net.java.slee.resource.diameter.gx.events.GxCreditControlAnswer;

/**
 * An GxServerSessionActivity represents a policy control session.
 *
 * A single GxServerSessionActivity will be created for the Diameter session.
 * All requests received for the session will be fired as events on the same
 * GxServerSessionActivity.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxServerSessionActivity extends GxSessionActivity {

    /**
     * Create a Ro-specific Credit-Control-Answer message pre-populated with the
     * AVPs appropriate for this session.
     *
     * @return a new CreditControlAnswer
     */
    GxCreditControlAnswer createGxCreditControlAnswer();

    /**
     * Sends a Gx-specific Credit-Control-Answer message to the client.
     *
     * @param cca the CreditControlAnswer to send
     * @throws IOException if an error occurred while sending the request to the peer
     */
    void sendGxCreditControlAnswer(GxCreditControlAnswer cca) throws IOException;

    /**
     * Sends a re-auth request to the client.
     * @param rar the ReAuthRequest to send.
     * @throws IOException if an error occurred while sending the message to the client.
     */
    void sendGxReAuthRequest(GxReAuthRequest rar) throws IOException;
}
