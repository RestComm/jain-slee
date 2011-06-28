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

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.gx.events.GxCreditControlRequest;
import net.java.slee.resource.diameter.gx.events.GxReAuthRequest;


/**
 * Used by applications to create Diameter Gx request messages.
 * Gx answer messages can be created using the GxServerSessionActivity.createGxCreditControlAnswer() method.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxMessageFactory {

    public static final long _GX_TGPP_VENDOR_ID = 10415L;
    public static final int _GX_AUTH_APP_ID = 16777224; // 16777238;

    /**
     * Creates a Credit Control Request message.
     * @return
     */
    public GxCreditControlRequest createGxCreditControlRequest();
    
    
    public GxReAuthRequest createGxReAuthRequest();

    /**
     * Creates a Credit Control Request message with the Session-Id AVP populated with the sessionId parameter.
     * @param sessionId
     * @return
     */
    public GxCreditControlRequest createGxCreditControlRequest(String sessionId);
    
    public GxReAuthRequest createGxReAuthRequest(String sessionId);

    /**
     * @return Base Diameter message factory
     */
    public DiameterMessageFactory getBaseMessageFactory();
}
