/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package net.java.slee.resource.diameter.ro;

/**
 * RoSessionActivity.java
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RoSessionActivity {

  /**
   * Provides session state information. CC session must conform to CC FSM as
   * described in <a href="link http://rfc.net/rfc4006.html#s7">section 7 of
   * rfc4006</a>
   * 
   * @return instance of {@link CreditControlSessionState}
   */
  public RoSessionState getState();

  /**
   * Return a message factory to be used to create concrete implementations of
   * credit control messages.
   * 
   * @return
   */
  public RoMessageFactory getRoMessageFactory();

  /**
   * Returns the session ID of the credit control session, which uniquely
   * identifies the session.
   * 
   * @return the session ID
   */
  public String getSessionId();
}
