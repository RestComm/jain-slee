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
 * Created on Dec 5, 2004 ConnectionMetaDataImpl.java
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