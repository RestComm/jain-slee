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

package net.java.slee.resource.diameter.base.events;

/**
 * Diameter command.  Applications can use this interface to retrieve the code, short name, long name and request
 * flag of a command. 
 *
 * @author Open Cloud
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface DiameterCommand {

  /**
   * Return the code for this command, e.g., 257.
   * <p>
   * @return the code for this command
   */
  int getCode();

  /**
   * Return the application ID for this command, e.g., 0
   * <p>
   * @return the application ID for this command
   */
  long getApplicationId();

  /**
   * Return the short name for this command, e.g., "CER".
   * <p>
   * @return the short name for this command
   */
  String getShortName();

  /**
   * Return the long name for this command, e.g., "Capabilities-Exchange-Request".
   * <p>
   * @return the long name for this command
   */
  String getLongName();

  /**
   * Return true if and only if this command is a request.
   * <p>
   * @return true if and only if this command is a request
   */
  boolean isRequest();

  /**
   * Return true if and only if this command may be proxied.
   * <p>
   * @return true if and only if this command may be proxied
   */
  boolean isProxiable();
}
