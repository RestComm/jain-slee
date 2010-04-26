/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Dec 6, 2004 ActivityHandleImpl.java
 */
package org.mobicents.slee.connector.server;

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