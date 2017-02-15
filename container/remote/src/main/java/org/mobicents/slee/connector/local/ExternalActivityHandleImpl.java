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

/*
 * ***************************************************
 *                                                 *
 *  Restcomm: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Dec 6, 2004 ActivityHandleImpl.java
 */
package org.mobicents.slee.connector.local;

import java.util.UUID;

import javax.slee.connection.ExternalActivityHandle;

/**
 * @author Tim
 * @author eduardomartins
 * 
 * Implementaion of a serializable handle to a null activity that lives on the SLEE
 * 
 */
public class ExternalActivityHandleImpl implements ExternalActivityHandle {
   private String activityContextId;
/*
   public ExternalActivityHandleImpl() {
	   this.activityContextId = "ExternalActivityHandle" + UUID.randomUUID().toString();
   }
  */ 
   ExternalActivityHandleImpl(String activityContextId) {
      this.activityContextId = activityContextId;
   }

   public boolean equals(Object other) {
      if(other != null && this.getClass() == other.getClass()) {
    	  return ((ExternalActivityHandleImpl)other).activityContextId.equals(this.activityContextId);
      }
      else {
    	  return false;
      }
   }

   public int hashCode() {
      return activityContextId.hashCode();
   }
   
   String getActivityContextId() { return activityContextId; }
}