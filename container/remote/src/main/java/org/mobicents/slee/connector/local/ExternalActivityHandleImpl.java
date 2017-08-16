/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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