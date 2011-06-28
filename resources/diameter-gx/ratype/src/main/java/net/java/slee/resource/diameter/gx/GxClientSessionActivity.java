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

import net.java.slee.resource.diameter.gx.events.GxReAuthAnswer;
import net.java.slee.resource.diameter.cca.events.avp.CcRequestType;
import net.java.slee.resource.diameter.gx.events.GxCreditControlRequest;
import net.java.slee.resource.diameter.gx.events.GxReAuthRequest;

/**
 * A GxClientSessionActivity represents a charging control session for Credit
 * Control clients.
 *
 * All requests for the session must be sent via the same GxClientSessionActivity.
 *
 * All responses related to the session will be received as events fired on the
 * same GxClientSessionActivity.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxClientSessionActivity extends GxSessionActivity {

    /**
     * Send an event Credit-Control-Request.
     *
     * @param ccr the CreditControlRequest to send
     * @throws IOException if an error occurred sending the request to the peer
     */
    void sendEventGxCreditControlRequest(GxCreditControlRequest ccr) throws IOException;

    /**
     * Send an initial Credit-Control-Request.
     *
     * @param ccr the GxCreditControlRequest to send
     * @throws IOException if an error occurred sending the request to the peer
     */
    void sendInitialGxCreditControlRequest(GxCreditControlRequest ccr) throws IOException;

    /**
     * Send an update (intermediate) Credit-Control-Request.
     *
     * @param ccr the GxCreditControlRequest to send
     * @throws IOException if an error occurred sending the request to the peer
     */
    void sendUpdateGxCreditControlRequest(GxCreditControlRequest ccr) throws IOException;

    /**
     * Send a termination Credit-Control-Request.
     *
     * @param ccr the GxCreditControlRequest to send
     * @throws IOException
     *             if an error occurred sending the request to the peer
     */
    public void sendTerminationGxCreditControlRequest(GxCreditControlRequest ccr) throws IOException;

    /**
     * Send a Re-Auth-Request.
     * @param raa ReAuthAnswer
     * @throws IOException
     */
    
    public GxReAuthAnswer createGxReAuthAnswer(GxReAuthRequest aar);
    
    public abstract void sendGxReAuthAnswer(GxReAuthAnswer raa) throws IOException;

    /**
     * Creates a GxCreditControlRequest.
     * @param type CcRequestType
     * @return GxCreditControlRequest
     */
    public GxCreditControlRequest createGxCreditControlRequest(CcRequestType type);
}
