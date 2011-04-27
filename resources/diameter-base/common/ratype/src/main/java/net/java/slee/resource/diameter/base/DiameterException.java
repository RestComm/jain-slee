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

package net.java.slee.resource.diameter.base;

/**
 * Class that should be used to indicate Diameter problems.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski</a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DiameterException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public DiameterException() {
    super();
  }

  /**
   * 
   * @param message
   */
  public DiameterException(String message) {
    super(message);
  }

  /**
   * 
   * @param cause
   */
  public DiameterException(Throwable cause) {
    super(cause);
  }

  /**
   * 
   * @param message
   * @param cause
   */
  public DiameterException(String message, Throwable cause) {
    super(message, cause);
  }

}
