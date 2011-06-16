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

package org.mobicents.slee.connector.adaptor;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

/**
 * @author Tim
 */
public class ConnectionMetaDataImpl implements ManagedConnectionMetaData {
   /*
    * Get the product name that the JCA adaptor is for
    * 
    * @see javax.resource.spi.ManagedConnectionMetaData#getEISProductName()
    */
   public String getEISProductName() throws ResourceException {
      return "Mobicents JAIN SLEE Implementation";
   }

   /*
    * Get the product version that the JCA adaptor is for
    * 
    * @see javax.resource.spi.ManagedConnectionMetaData#getEISProductVersion()
    */
   public String getEISProductVersion() throws ResourceException {
      return "Version 0.1";
   }

   /*
    * Get the maximum number of connections that the JCA adaptor supports
    * 
    * @see javax.resource.spi.ManagedConnectionMetaData#getMaxConnections()
    */
   public int getMaxConnections() throws ResourceException {
      return 200;
   }

   /*
    * Default username. (Is this ever used?)
    * 
    * @see javax.resource.spi.ManagedConnectionMetaData#getUserName()
    */
   public String getUserName() throws ResourceException {
      return "tim";
   }
}